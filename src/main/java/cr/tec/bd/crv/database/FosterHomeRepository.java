package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.CatalogOption;
import cr.tec.bd.crv.model.FosterHomeDirectoryRecord;
import cr.tec.bd.crv.model.FosterHomeProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles foster home profiles and their accepted conditions.
 *
 * <p>A foster home is a user who can temporarily receive pets. The database
 * stores the user profile separately from the conditions they accept, so this
 * repository coordinates both parts.</p>
 */
public class FosterHomeRepository {

    private final ApplicationAuditRepository auditRepository = new ApplicationAuditRepository();

    /**
     * Returns active foster homes as combo-box options.
     */
    public List<CatalogOption> findFosterHomeOptions() throws SQLException {
        String sql = """
                SELECT
                    fh.idPerson AS id,
                    p.firstName || ' ' || p.firstLastName AS label
                FROM fosterHome fh
                JOIN person p
                    ON p.id = fh.idPerson
                ORDER BY p.firstName, p.firstLastName
                """;

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<CatalogOption> fosterHomes = new ArrayList<>();
            while (resultSet.next()) {
                fosterHomes.add(new CatalogOption(resultSet.getLong("id"), resultSet.getString("label")));
            }
            return fosterHomes;
        }
    }

    /**
     * Returns active foster homes with contact data and accepted conditions.
     */
    public List<FosterHomeDirectoryRecord> findDirectory() throws SQLException {
        String sql = """
                WITH active_condition AS (
                    SELECT idFosterHome, MAX(idFosterCondition) AS idFosterCondition
                    FROM fosterHomexFosterCondition
                    GROUP BY idFosterHome
                )
                SELECT
                    TRIM(
                        p.firstName ||
                        CASE WHEN p.secondName IS NOT NULL THEN ' ' || p.secondName ELSE '' END ||
                        ' ' || p.firstLastName ||
                        CASE WHEN p.secondLastName IS NOT NULL THEN ' ' || p.secondLastName ELSE '' END
                    ) AS fosterName,
                    (
                        SELECT MIN(e.emailAddress)
                        FROM personxEmail pe
                        JOIN email e ON e.id = pe.idEmail
                        WHERE pe.idPerson = p.id
                    ) AS email,
                    (
                        SELECT MIN(ph.phoneNumber)
                        FROM personxPhone pp
                        JOIN phone ph ON ph.id = pp.idPhone
                        WHERE pp.idPerson = p.id
                    ) AS phone,
                    (
                        SELECT LISTAGG(pt.name, ', ') WITHIN GROUP (ORDER BY pt.name)
                        FROM fosterConditionxAccType fct
                        JOIN petType pt ON pt.id = fct.idPetType
                        WHERE fct.idFosterCondition = ac.idFosterCondition
                    ) AS acceptedTypes,
                    (
                        SELECT LISTAGG(ps.name, ', ') WITHIN GROUP (ORDER BY ps.name)
                        FROM fosterConditionxAccSize fcs
                        JOIN petSize ps ON ps.id = fcs.idPetSize
                        WHERE fcs.idFosterCondition = ac.idFosterCondition
                    ) AS acceptedSizes,
                    rfd.name AS foodDonation,
                    fc.notes
                FROM fosterHome fh
                JOIN person p
                    ON p.id = fh.idPerson
                LEFT JOIN active_condition ac
                    ON ac.idFosterHome = fh.idPerson
                LEFT JOIN fosterCondition fc
                    ON fc.id = ac.idFosterCondition
                LEFT JOIN requiresFoodDonation rfd
                    ON rfd.id = fc.idFoodDonation
                ORDER BY p.firstName, p.firstLastName
                """;

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<FosterHomeDirectoryRecord> directory = new ArrayList<>();
            while (resultSet.next()) {
                directory.add(new FosterHomeDirectoryRecord(
                        valueOrEmpty(resultSet.getString("fosterName")),
                        valueOrEmpty(resultSet.getString("email")),
                        valueOrEmpty(resultSet.getString("phone")),
                        valueOrEmpty(resultSet.getString("acceptedTypes")),
                        valueOrEmpty(resultSet.getString("acceptedSizes")),
                        valueOrEmpty(resultSet.getString("foodDonation")),
                        valueOrEmpty(resultSet.getString("notes"))
                ));
            }
            return directory;
        }
    }

    /**
     * Checks whether a user already has an active foster home profile.
     */
    public boolean isFosterHome(Long personId) throws SQLException {
        if (personId == null) {
            return false;
        }

        String sql = "SELECT 1 FROM fosterHome WHERE idPerson = ?";
        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    /**
     * Loads the foster home profile and its selected pet type/size conditions.
     */
    public FosterHomeProfile findProfile(Long personId) throws SQLException {
        if (personId == null) {
            return new FosterHomeProfile(false, null, "", List.of(), List.of());
        }

        try (Connection connection = ConexionBD.conectar()) {
            Long conditionId = findCurrentConditionId(connection, personId);
            if (conditionId == null) {
                return new FosterHomeProfile(isFosterHome(personId), null, "", List.of(), List.of());
            }

            String sql = """
                    SELECT idFoodDonation, notes
                    FROM fosterCondition
                    WHERE id = ?
                    """;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, conditionId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return new FosterHomeProfile(true, null, "", List.of(), List.of());
                    }

                    Long foodDonationId = resultSet.getObject("idFoodDonation") == null
                            ? null
                            : resultSet.getLong("idFoodDonation");

                    return new FosterHomeProfile(
                            true,
                            foodDonationId,
                            resultSet.getString("notes"),
                            findAcceptedTypes(connection, conditionId),
                            findAcceptedSizes(connection, conditionId)
                    );
                }
            }
        }
    }

    /**
     * Creates or updates the foster home profile and replaces its condition links.
     */
    public void saveProfile(Long personId, Long foodDonationId, List<Long> typeIds, List<Long> sizeIds, String notes)
            throws SQLException {
        validateProfile(personId, foodDonationId, typeIds, sizeIds);

        try (Connection connection = ConexionBD.conectar()) {
            connection.setAutoCommit(false);

            try {
                ensureFosterHome(connection, personId);
                removeCurrentConditions(connection, personId);

                long conditionId = nextSequenceValue(connection, "sFosterCondition");
                insertCondition(connection, conditionId, foodDonationId, notes);
                linkHomeCondition(connection, personId, conditionId);
                insertAcceptedTypes(connection, conditionId, typeIds);
                insertAcceptedSizes(connection, conditionId, sizeIds);
                auditRepository.log(connection, "Foster home", "Perfil", "person:" + personId, "activa");

                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private void validateProfile(Long personId, Long foodDonationId, List<Long> typeIds, List<Long> sizeIds) {
        if (personId == null) {
            throw new IllegalArgumentException("You must sign in as a user to activate foster home.");
        }

        if (foodDonationId == null) {
            throw new IllegalArgumentException("Select whether food donation is required.");
        }

        if (typeIds == null || typeIds.isEmpty()) {
            throw new IllegalArgumentException("Select at least one accepted pet type.");
        }

        if (sizeIds == null || sizeIds.isEmpty()) {
            throw new IllegalArgumentException("Select at least one accepted size.");
        }
    }

    private void ensureFosterHome(Connection connection, long personId) throws SQLException {
        String sql = """
                MERGE INTO fosterHome target
                USING (SELECT ? AS idPerson FROM dual) source
                ON (target.idPerson = source.idPerson)
                WHEN NOT MATCHED THEN
                    INSERT (idPerson) VALUES (source.idPerson)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);
            statement.executeUpdate();
        }
    }

    private Long findCurrentConditionId(Connection connection, long personId) throws SQLException {
        String sql = """
                SELECT idFosterCondition
                FROM fosterHomexFosterCondition
                WHERE idFosterHome = ?
                ORDER BY idFosterCondition DESC
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("idFosterCondition");
                }
                return null;
            }
        }
    }

    private void removeCurrentConditions(Connection connection, long personId) throws SQLException {
        List<Long> conditionIds = new ArrayList<>();
        String selectSql = "SELECT idFosterCondition FROM fosterHomexFosterCondition WHERE idFosterHome = ?";

        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            statement.setLong(1, personId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    conditionIds.add(resultSet.getLong("idFosterCondition"));
                }
            }
        }

        for (Long conditionId : conditionIds) {
            deleteConditionLinks(connection, conditionId);
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM fosterHomexFosterCondition WHERE idFosterHome = ?"
        )) {
            statement.setLong(1, personId);
            statement.executeUpdate();
        }

        for (Long conditionId : conditionIds) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM fosterCondition WHERE id = ?")) {
                statement.setLong(1, conditionId);
                statement.executeUpdate();
            }
        }
    }

    private void deleteConditionLinks(Connection connection, long conditionId) throws SQLException {
        String[] deleteSqlStatements = {
                "DELETE FROM fosterConditionxAccType WHERE idFosterCondition = ?",
                "DELETE FROM fosterConditionxAccSize WHERE idFosterCondition = ?",
                "DELETE FROM rescuerxFosterCondition WHERE idFosterCondition = ?"
        };

        for (String sql : deleteSqlStatements) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, conditionId);
                statement.executeUpdate();
            }
        }
    }

    private void insertCondition(Connection connection, long conditionId, long foodDonationId, String notes)
            throws SQLException {
        String sql = "INSERT INTO fosterCondition(id, idFoodDonation, notes) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, conditionId);
            statement.setLong(2, foodDonationId);
            statement.setString(3, emptyToNull(notes));
            statement.executeUpdate();
        }
    }

    private void linkHomeCondition(Connection connection, long personId, long conditionId) throws SQLException {
        String sql = """
                INSERT INTO fosterHomexFosterCondition(idFosterHome, idFosterCondition)
                VALUES (?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);
            statement.setLong(2, conditionId);
            statement.executeUpdate();
        }
    }

    private void insertAcceptedTypes(Connection connection, long conditionId, List<Long> typeIds) throws SQLException {
        String sql = "INSERT INTO fosterConditionxAccType(idFosterCondition, idPetType) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Long typeId : typeIds) {
                statement.setLong(1, conditionId);
                statement.setLong(2, typeId);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void insertAcceptedSizes(Connection connection, long conditionId, List<Long> sizeIds) throws SQLException {
        String sql = "INSERT INTO fosterConditionxAccSize(idFosterCondition, idPetSize) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Long sizeId : sizeIds) {
                statement.setLong(1, conditionId);
                statement.setLong(2, sizeId);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private List<Long> findAcceptedTypes(Connection connection, long conditionId) throws SQLException {
        return findIds(connection, "SELECT idPetType FROM fosterConditionxAccType WHERE idFosterCondition = ?", conditionId);
    }

    private List<Long> findAcceptedSizes(Connection connection, long conditionId) throws SQLException {
        return findIds(connection, "SELECT idPetSize FROM fosterConditionxAccSize WHERE idFosterCondition = ?", conditionId);
    }

    private List<Long> findIds(Connection connection, String sql, long conditionId) throws SQLException {
        List<Long> ids = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, conditionId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ids.add(resultSet.getLong(1));
                }
            }
        }
        return ids;
    }

    private long nextSequenceValue(Connection connection, String sequenceName) throws SQLException {
        String sql = "SELECT " + sequenceName + ".NEXTVAL FROM dual";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getLong(1);
        }
    }

    private String emptyToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
