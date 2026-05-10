package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.DonationRecord;

import java.math.BigDecimal;
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
 * Handles donation registration, search, and deletion.
 *
 * <p>Donations touch several tables: the donation itself, its currency/allocation
 * data, the person who donated, and the association that receives it. The
 * repository keeps those related inserts and deletes in one safe place.</p>
 */
public class DonationRepository {

    private final ApplicationAuditRepository auditRepository = new ApplicationAuditRepository();

    /**
     * Saves a donation made by the current user to an association.
     */
    public void registerDonation(
            Long donorPersonId,
            Long associationId,
            Long currencyId,
            BigDecimal amount,
            LocalDate donationDate
    ) throws SQLException {
        validateDonation(donorPersonId, associationId, currencyId, amount, donationDate);

        try (Connection connection = ConexionBD.conectar()) {
            connection.setAutoCommit(false);

            try {
                long allocationId = nextSequenceValue(connection, "sDonationAllLocation");
                long donationId = nextSequenceValue(connection, "sDonation");

                insertDonationAllocation(connection, allocationId, amount);
                insertDonation(connection, donationId, currencyId, allocationId, donationDate, amount);
                linkPersonDonation(connection, donorPersonId, donationId);
                linkAssociationDonation(connection, associationId, donationId);
                auditRepository.log(connection, "Donations", "Create", "-", amount.toPlainString());

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
     * Searches donation records using optional date, donor, association, and user filters.
     */
    public List<DonationRecord> findDonations(
            LocalDate fromDate,
            LocalDate toDate,
            String donorText,
            Long associationId,
            BigDecimal exactAmount,
            Long visiblePersonId
    ) throws SQLException {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT
                    d.id,
                    d.amount,
                    cu.acronym AS currency,
                    TRIM(
                        p.firstName ||
                        CASE WHEN p.secondName IS NOT NULL THEN ' ' || p.secondName ELSE '' END ||
                        ' ' || p.firstLastName ||
                        CASE WHEN p.secondLastName IS NOT NULL THEN ' ' || p.secondLastName ELSE '' END
                    ) AS donorName,
                    a.name AS associationName,
                    d.donationDate
                FROM donation d
                INNER JOIN currency cu ON d.idCurrency = cu.id
                INNER JOIN personxDonation pd ON d.id = pd.idDonation
                INNER JOIN person p ON pd.idPerson = p.id
                INNER JOIN associationxDonation ad ON d.id = ad.idDonation
                INNER JOIN association a ON ad.idAssociation = a.id
                WHERE 1 = 1
                """);

        if (visiblePersonId != null) {
            sql.append(" AND pd.idPerson = ?\n");
            parameters.add(visiblePersonId);
        }

        if (fromDate != null) {
            sql.append(" AND d.donationDate >= ?\n");
            parameters.add(Date.valueOf(fromDate));
        }

        if (toDate != null) {
            sql.append(" AND d.donationDate <= ?\n");
            parameters.add(Date.valueOf(toDate));
        }

        String normalizedDonorText = emptyToNull(donorText);
        if (normalizedDonorText != null) {
            sql.append("""
                     AND LOWER(
                        p.firstName || ' ' ||
                        NVL(p.secondName, '') || ' ' ||
                        p.firstLastName || ' ' ||
                        NVL(p.secondLastName, '')
                    ) LIKE ?
                    """);
            parameters.add("%" + normalizedDonorText.toLowerCase(Locale.ROOT) + "%");
        }

        if (associationId != null) {
            sql.append(" AND ad.idAssociation = ?\n");
            parameters.add(associationId);
        }

        if (exactAmount != null) {
            sql.append(" AND d.amount = ?\n");
            parameters.add(exactAmount);
        }

        sql.append(" ORDER BY d.donationDate DESC, d.id DESC");

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            setParameters(statement, parameters);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<DonationRecord> donations = new ArrayList<>();
                while (resultSet.next()) {
                    Date donationDate = resultSet.getDate("donationDate");
                    donations.add(new DonationRecord(
                            resultSet.getLong("id"),
                            valueOrEmpty(resultSet.getString("donorName")),
                            valueOrEmpty(resultSet.getString("associationName")),
                            resultSet.getBigDecimal("amount"),
                            valueOrEmpty(resultSet.getString("currency")),
                            donationDate == null ? null : donationDate.toLocalDate()
                    ));
                }
                return donations;
            }
        }
    }

    /**
     * Deletes a donation and its related linking/allocation rows.
     */
    public void deleteDonation(long donationId) throws SQLException {
        try (Connection connection = ConexionBD.conectar()) {
            connection.setAutoCommit(false);

            try {
                Long allocationId = findDonationAllocationId(connection, donationId);

                deleteByDonationId(connection, "associationxDonation", donationId);
                deleteByDonationId(connection, "personxDonation", donationId);
                deleteById(connection, "donation", donationId);

                if (allocationId != null) {
                    deleteById(connection, "donationAllLocation", allocationId);
                }
                auditRepository.log(connection, "Donations", "Delete", "id:" + donationId, "-");

                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private void validateDonation(
            Long donorPersonId,
            Long associationId,
            Long currencyId,
            BigDecimal amount,
            LocalDate donationDate
    ) {
        if (donorPersonId == null) {
            throw new IllegalArgumentException("You must sign in as a user to register a donation.");
        }
        if (associationId == null) {
            throw new IllegalArgumentException("Select the beneficiary association.");
        }
        if (currencyId == null) {
            throw new IllegalArgumentException("Select the currency.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (donationDate == null) {
            throw new IllegalArgumentException("Select the donation date.");
        }
    }

    private void insertDonationAllocation(Connection connection, long allocationId, BigDecimal amount) throws SQLException {
        String sql = "INSERT INTO donationAllLocation(id, allocatedAmount, percentage) VALUES (?, ?, 100)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, allocationId);
            statement.setBigDecimal(2, amount);
            statement.executeUpdate();
        }
    }

    private void insertDonation(
            Connection connection,
            long donationId,
            Long currencyId,
            long allocationId,
            LocalDate donationDate,
            BigDecimal amount
    ) throws SQLException {
        String sql = """
                INSERT INTO donation(id, idCurrency, idDonAllLocation, donationDate, amount)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, donationId);
            statement.setLong(2, currencyId);
            statement.setLong(3, allocationId);
            statement.setDate(4, Date.valueOf(donationDate));
            statement.setBigDecimal(5, amount);
            statement.executeUpdate();
        }
    }

    private void linkPersonDonation(Connection connection, Long personId, long donationId) throws SQLException {
        String sql = "INSERT INTO personxDonation(idPerson, idDonation) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);
            statement.setLong(2, donationId);
            statement.executeUpdate();
        }
    }

    private void linkAssociationDonation(Connection connection, Long associationId, long donationId) throws SQLException {
        String sql = "INSERT INTO associationxDonation(idAssociation, idDonation) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, associationId);
            statement.setLong(2, donationId);
            statement.executeUpdate();
        }
    }

    private Long findDonationAllocationId(Connection connection, long donationId) throws SQLException {
        String sql = "SELECT idDonAllLocation FROM donation WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, donationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    long value = resultSet.getLong("idDonAllLocation");
                    return resultSet.wasNull() ? null : value;
                }
                return null;
            }
        }
    }

    private void deleteByDonationId(Connection connection, String tableName, long donationId) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE idDonation = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, donationId);
            statement.executeUpdate();
        }
    }

    private void deleteById(Connection connection, String tableName, long id) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
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
            Object value = values.get(index);
            if (value instanceof Date date) {
                statement.setDate(index + 1, date);
            } else {
                statement.setObject(index + 1, value);
            }
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
