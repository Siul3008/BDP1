package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.AssociationRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes association information.
 *
 * <p>Associations are organizations that can receive donations. This repository
 * keeps the admin screen from dealing directly with SQL.</p>
 */
public class AssociationRepository {

    private final ApplicationAuditRepository auditRepository = new ApplicationAuditRepository();

    /**
     * Creates a new association. Associations are registered by an admin and do not log in.
     */
    public void registerAssociation(String name) throws SQLException {
        requireValue(name, "Association name is required.");

        try (Connection connection = ConexionBD.conectar()) {
            long associationId = nextSequenceValue(connection, "sAssociation");
            String sql = "INSERT INTO association(id, name) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, associationId);
                statement.setString(2, name.trim());
                statement.executeUpdate();
                auditRepository.log(connection, "Associations", "Crear", "-", name.trim());
            }
        }
    }

    /**
     * Lists associations together with donation totals used by the admin table.
     */
    public List<AssociationRecord> findAssociationRecords() throws SQLException {
        String sql = """
                SELECT
                    a.id,
                    a.name,
                    COUNT(d.id) AS donationCount,
                    NVL(TO_CHAR(SUM(d.amount), 'FM9999999990.00'), '0.00') AS totalDonated
                FROM association a
                LEFT JOIN associationxDonation ad
                    ON ad.idAssociation = a.id
                LEFT JOIN donation d
                    ON d.id = ad.idDonation
                GROUP BY a.id, a.name
                ORDER BY a.name
                """;

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<AssociationRecord> associations = new ArrayList<>();
            while (resultSet.next()) {
                associations.add(new AssociationRecord(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("donationCount"),
                        resultSet.getString("totalDonated")
                ));
            }
            return associations;
        }
    }

    /**
     * Renames an existing association.
     */
    public void updateAssociationName(long associationId, String name) throws SQLException {
        requireValue(name, "Association name is required.");

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement("UPDATE association SET name = ? WHERE id = ?")) {
            statement.setString(1, name.trim());
            statement.setLong(2, associationId);

            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) {
                throw new IllegalArgumentException("The selected association does not exist.");
            }
            auditRepository.log(connection, "Associations", "Name", "id:" + associationId, name.trim());
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

    private void requireValue(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}
