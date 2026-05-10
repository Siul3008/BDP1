package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.AdoptionFormData;
import cr.tec.bd.crv.model.AdoptionRecord;
import cr.tec.bd.crv.model.CatalogOption;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Handles adoption registration and follow-up data.
 *
 * <p>Registering an adoption is more than one insert: it saves the application,
 * the rating, the adoption record, optional photos, changes the pet status, and
 * transfers pet control to the adopter.</p>
 */
public class AdoptionRepository {

    private final ApplicationAuditRepository auditRepository = new ApplicationAuditRepository();

    /**
     * Returns pets that can currently be adopted by the current user or by an admin.
     */
    public List<CatalogOption> findAdoptablePets(Long currentPersonId, boolean admin) throws SQLException {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT p.id, p.name || ' - ' || NVL(ps.status, 'No status') AS label
                FROM pet p
                LEFT JOIN petStatus ps
                    ON ps.id = p.idPetStatus
                WHERE LOWER(NVL(ps.status, '')) IN ('for adoption', 'en adopcion', 'en adopción')
                """);

        if (!admin) {
            sql.append("""
                     AND EXISTS (
                         SELECT 1
                         FROM rescuerxPet rp
                         WHERE rp.idPet = p.id
                           AND rp.idRescuer = ?
                     )
                    """);
            parameters.add(currentPersonId);
        }

        sql.append(" ORDER BY p.name");

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            setParameters(statement, parameters);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<CatalogOption> pets = new ArrayList<>();
                while (resultSet.next()) {
                    pets.add(new CatalogOption(resultSet.getLong("id"), resultSet.getString("label")));
                }
                return pets;
            }
        }
    }

    /**
     * Returns recent adoption records visible to the current account.
     */
    public List<AdoptionRecord> findRecentAdoptions(Long currentPersonId, boolean admin) throws SQLException {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT
                    a.id,
                    ar.name AS adopterName,
                    p.name AS petName,
                    a.adoptionDate,
                    ar.star,
                    a.followUpNotes
                FROM adoption a
                JOIN adopterRating ar
                    ON ar.id = a.idAdopterRating
                JOIN adoptionxPet ap
                    ON ap.idAdoption = a.id
                JOIN pet p
                    ON p.id = ap.idPet
                WHERE 1 = 1
                """);

        if (!admin) {
            sql.append("""
                     AND EXISTS (
                         SELECT 1
                         FROM adoptionxRescuer axr
                         WHERE axr.idAdoption = a.id
                           AND axr.idRescuer = ?
                     )
                    """);
            parameters.add(currentPersonId);
        }

        sql.append(" ORDER BY a.adoptionDate DESC, a.id DESC");

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            setParameters(statement, parameters);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<AdoptionRecord> adoptions = new ArrayList<>();
                while (resultSet.next()) {
                    Date adoptionDate = resultSet.getDate("adoptionDate");
                    adoptions.add(new AdoptionRecord(
                            resultSet.getLong("id"),
                            valueOrEmpty(resultSet.getString("adopterName")),
                            valueOrEmpty(resultSet.getString("petName")),
                            adoptionDate == null ? null : adoptionDate.toLocalDate(),
                            valueOrEmpty(resultSet.getString("star")),
                            valueOrEmpty(resultSet.getString("followUpNotes"))
                    ));
                }
                return adoptions;
            }
        }
    }

    /**
     * Saves the adoption and updates pet ownership/status in a single transaction.
     */
    public void registerAdoption(AdoptionFormData data, Long currentPersonId, boolean admin) throws SQLException {
        validateAdoption(data, currentPersonId, admin);

        try (Connection connection = ConexionBD.conectar()) {
            connection.setAutoCommit(false);

            try {
                PersonSummary adopter = findAdopterByEmail(connection, data.getAdopterEmail());
                ensurePetIsForAdoption(connection, data.getPetId());
                if (!admin && !userControlsPet(connection, data.getPetId(), currentPersonId)) {
                    throw new IllegalArgumentException("Only the pet controller can register its adoption.");
                }

                long applicationId = nextSequenceValue(connection, "sAdoptionAppication");
                long ratingId = nextSequenceValue(connection, "sAdopterRating");
                long adoptionId = nextSequenceValue(connection, "sAdoption");

                insertApplication(connection, applicationId, data);
                insertRating(connection, ratingId, adopter.displayName(), data);
                insertAdoption(connection, adoptionId, applicationId, ratingId, data);
                linkAdoptionPet(connection, adoptionId, data.getPetId());
                linkAdoptionRescuer(connection, adoptionId, currentPersonId);
                linkAdoptionRescuer(connection, adoptionId, adopter.personId());
                updateAdopterRating(connection, adopter.personId(), ratingId, data.getAdopterNotes());
                insertOptionalPhoto(connection, adoptionId, "Adoption", data.getAdoptionPhotoPath());
                insertOptionalPhoto(connection, adoptionId, "Follow-up", data.getFollowUpPhotoPath());
                updatePetToAdopted(connection, data.getPetId());
                transferPetControlToAdopter(connection, data.getPetId(), adopter.personId());
                auditRepository.log(
                        connection,
                        "Adoptions",
                        "Record",
                        "pet:" + data.getPetId(),
                        adopter.displayName()
                );

                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Updates follow-up notes and optionally adds a new follow-up photo.
     */
    public void updateFollowUp(long adoptionId, String followUpNotes, String followUpPhotoPath, Long currentPersonId, boolean admin)
            throws SQLException {
        if (adoptionId <= 0) {
            throw new IllegalArgumentException("Select an adoption from the list.");
        }
        requireValue(followUpNotes, "Enter follow-up notes.");
        if (!admin && currentPersonId == null) {
            throw new IllegalArgumentException("You must sign in to update follow-up.");
        }

        try (Connection connection = ConexionBD.conectar()) {
            connection.setAutoCommit(false);

            try {
                String sql;
                if (admin) {
                    sql = "UPDATE adoption SET followUpNotes = ? WHERE id = ?";
                } else {
                    sql = """
                            UPDATE adoption
                            SET followUpNotes = ?
                            WHERE id = ?
                              AND EXISTS (
                                  SELECT 1
                                  FROM adoptionxRescuer axr
                                  WHERE axr.idAdoption = adoption.id
                                    AND axr.idRescuer = ?
                              )
                            """;
                }

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, fit(followUpNotes, 100));
                    statement.setLong(2, adoptionId);
                    if (!admin) {
                        statement.setLong(3, currentPersonId);
                    }

                    int updatedRows = statement.executeUpdate();
                    if (updatedRows == 0) {
                        throw new IllegalArgumentException(
                                "Only someone linked to the adoption or an admin can update follow-up."
                        );
                    }
                }

                insertOptionalPhoto(connection, adoptionId, "Follow-up", followUpPhotoPath);
                auditRepository.log(connection, "Adoptions", "Follow-up", "id:" + adoptionId, followUpNotes);
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private void validateAdoption(AdoptionFormData data, Long currentPersonId, boolean admin) {
        requireSelected(data.getPetId(), "Select the pet.");
        requireValue(data.getAdopterEmail(), "Enter the adopter email.");
        if (!data.getAdopterEmail().trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Adopter email has an invalid format.");
        }
        if (data.getAdoptionDate() == null) {
            throw new IllegalArgumentException("Select the adoption date.");
        }
        requireValue(data.getYard(), "Indique si el adoptante tiene patio.");
        requireValue(data.getExerciseTime(), "Indique el tiempo de ejercicio.");
        requireValue(data.getHousingType(), "Indique el tipo de vivienda.");
        requireValue(data.getOtherPets(), "Indicate whether there are other pets.");
        requireValue(data.getAnswers(), "Complete las respuestas de la solicitud.");
        requireValue(data.getRating(), "Select the rating.");

        if (!admin && currentPersonId == null) {
            throw new IllegalArgumentException("You must sign in as a user to register adoptions.");
        }
    }

    private PersonSummary findAdopterByEmail(Connection connection, String email) throws SQLException {
        String sql = """
                SELECT
                    p.id,
                    p.firstName || ' ' || p.firstLastName AS displayName
                FROM appAccount aa
                JOIN person p
                    ON p.id = aa.idPerson
                JOIN adopter ad
                    ON ad.idPerson = p.id
                WHERE aa.accountType = 'USER'
                  AND aa.isActive = 'Y'
                  AND LOWER(aa.loginEmail) = LOWER(?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email.trim());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("No adopter user exists with that email.");
                }

                return new PersonSummary(resultSet.getLong("id"), resultSet.getString("displayName"));
            }
        }
    }

    private boolean userControlsPet(Connection connection, long petId, Long personId) throws SQLException {
        if (personId == null) {
            return false;
        }

        String sql = """
                SELECT 1
                FROM rescuerxPet
                WHERE idPet = ?
                  AND idRescuer = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, petId);
            statement.setLong(2, personId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private void ensurePetIsForAdoption(Connection connection, long petId) throws SQLException {
        String sql = """
                SELECT LOWER(NVL(ps.status, '')) AS statusName
                FROM pet p
                LEFT JOIN petStatus ps
                    ON ps.id = p.idPetStatus
                WHERE p.id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, petId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("The selected pet does not exist.");
                }

                String statusName = resultSet.getString("statusName");
                if (!isForAdoptionStatus(statusName)) {
                    throw new IllegalArgumentException(
                            "Adoption can only be registered for pets that are for adoption."
                    );
                }
            }
        }
    }

    private void insertApplication(Connection connection, long applicationId, AdoptionFormData data) throws SQLException {
        String sql = """
                INSERT INTO adoptionApplication(id, yard, exerciseTime, answers, otherPets, housingType)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, applicationId);
            statement.setString(2, fit(data.getYard(), 25));
            statement.setString(3, fit(data.getExerciseTime(), 10));
            statement.setString(4, fit(data.getAnswers(), 100));
            statement.setString(5, fit(data.getOtherPets(), 50));
            statement.setString(6, fit(data.getHousingType(), 25));
            statement.executeUpdate();
        }
    }

    private void insertRating(Connection connection, long ratingId, String adopterName, AdoptionFormData data)
            throws SQLException {
        String sql = """
                INSERT INTO adopterRating(id, name, star, ratingDate, note)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, ratingId);
            statement.setString(2, fit(adopterName, 25));
            statement.setString(3, fit(data.getRating(), 10));
            statement.setDate(4, Date.valueOf(data.getAdoptionDate()));
            statement.setString(5, fit(data.getAdopterNotes(), 50));
            statement.executeUpdate();
        }
    }

    private void insertAdoption(
            Connection connection,
            long adoptionId,
            long applicationId,
            long ratingId,
            AdoptionFormData data
    ) throws SQLException {
        String sql = """
                INSERT INTO adoption(id, idApplication, idAdopterRating, adoptionDate, adopterNotes, followUpNotes)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, adoptionId);
            statement.setLong(2, applicationId);
            statement.setLong(3, ratingId);
            statement.setDate(4, Date.valueOf(data.getAdoptionDate()));
            statement.setString(5, fit(data.getAdopterNotes(), 100));
            statement.setString(6, fit(data.getFollowUpNotes(), 100));
            statement.executeUpdate();
        }
    }

    private void linkAdoptionPet(Connection connection, long adoptionId, long petId) throws SQLException {
        String sql = "INSERT INTO adoptionxPet(idAdoption, idPet) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, adoptionId);
            statement.setLong(2, petId);
            statement.executeUpdate();
        }
    }

    private void linkAdoptionRescuer(Connection connection, long adoptionId, Long rescuerId) throws SQLException {
        if (rescuerId == null) {
            return;
        }

        ensureRescuerProfile(connection, rescuerId);
        String sql = """
                MERGE INTO adoptionxRescuer target
                USING (SELECT ? AS idAdoption, ? AS idRescuer FROM dual) source
                ON (target.idAdoption = source.idAdoption AND target.idRescuer = source.idRescuer)
                WHEN NOT MATCHED THEN
                    INSERT (idAdoption, idRescuer) VALUES (source.idAdoption, source.idRescuer)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, adoptionId);
            statement.setLong(2, rescuerId);
            statement.executeUpdate();
        }
    }

    private void updateAdopterRating(Connection connection, long adopterId, long ratingId, String note) throws SQLException {
        String sql = "UPDATE adopter SET idStarRating = ?, note = ? WHERE idPerson = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, ratingId);
            statement.setString(2, fit(note, 50));
            statement.setLong(3, adopterId);
            statement.executeUpdate();
        }
    }

    private void insertOptionalPhoto(Connection connection, long adoptionId, String photoType, String photoPath)
            throws SQLException {
        String normalizedPath = emptyToNull(photoPath);
        if (normalizedPath == null) {
            return;
        }

        long photoId = nextSequenceValue(connection, "sAdoptionPhoto");
        try (PreparedStatement insertPhoto = connection.prepareStatement(
                "INSERT INTO adoptionPhoto(id, photoType, photoPath) VALUES (?, ?, ?)"
        );
             PreparedStatement linkPhoto = connection.prepareStatement(
                     "INSERT INTO adoptionxAdpPhoto(idAdoption, idPhoto) VALUES (?, ?)"
             )) {
            insertPhoto.setLong(1, photoId);
            insertPhoto.setString(2, fit(photoType, 20));
            insertPhoto.setString(3, fit(normalizedPath, 255));
            insertPhoto.executeUpdate();

            linkPhoto.setLong(1, adoptionId);
            linkPhoto.setLong(2, photoId);
            linkPhoto.executeUpdate();
        }
    }

    private void updatePetToAdopted(Connection connection, long petId) throws SQLException {
        Long adoptedStatusId = findAdoptedStatusId(connection);
        String sql = "UPDATE pet SET idPetStatus = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, adoptedStatusId);
            statement.setLong(2, petId);
            statement.executeUpdate();
        }
    }

    private void transferPetControlToAdopter(Connection connection, long petId, long adopterPersonId) throws SQLException {
        ensureRescuerProfile(connection, adopterPersonId);

        try (PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM rescuerxPet WHERE idPet = ?")) {
            deleteStatement.setLong(1, petId);
            deleteStatement.executeUpdate();
        }

        String sql = """
                MERGE INTO rescuerxPet target
                USING (SELECT ? AS idRescuer, ? AS idPet FROM dual) source
                ON (target.idRescuer = source.idRescuer AND target.idPet = source.idPet)
                WHEN NOT MATCHED THEN
                    INSERT (idRescuer, idPet) VALUES (source.idRescuer, source.idPet)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, adopterPersonId);
            statement.setLong(2, petId);
            statement.executeUpdate();
        }
    }

    private Long findAdoptedStatusId(Connection connection) throws SQLException {
        String exactSql = """
                SELECT id
                FROM petStatus
                WHERE LOWER(status) IN ('adopted', 'adoptado')
                """;

        try (PreparedStatement statement = connection.prepareStatement(exactSql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
        }

        throw new IllegalArgumentException("The Adopted status is not available.");
    }

    private void ensureRescuerProfile(Connection connection, long personId) throws SQLException {
        String sql = """
                MERGE INTO rescuer target
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

    private boolean isForAdoptionStatus(String statusName) {
        if (statusName == null) {
            return false;
        }

        String normalizedStatus = statusName.toLowerCase(Locale.ROOT);
        return normalizedStatus.equals("for adoption")
                || normalizedStatus.equals("en adopcion")
                || normalizedStatus.equals("en adopción");
    }

    private long nextSequenceValue(Connection connection, String sequenceName) throws SQLException {
        String sql = "SELECT " + sequenceName + ".NEXTVAL FROM dual";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getLong(1);
        }
    }

    private void setParameters(PreparedStatement statement, List<Object> values) throws SQLException {
        for (int index = 0; index < values.size(); index++) {
            statement.setObject(index + 1, values.get(index));
        }
    }

    private void requireSelected(Long value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireValue(String value, String message) {
        if (emptyToNull(value) == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private String fit(String value, int maxLength) {
        String normalizedValue = emptyToNull(value);
        if (normalizedValue == null || normalizedValue.length() <= maxLength) {
            return normalizedValue;
        }
        return normalizedValue.substring(0, maxLength);
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

    private record PersonSummary(long personId, String displayName) {
    }
}
