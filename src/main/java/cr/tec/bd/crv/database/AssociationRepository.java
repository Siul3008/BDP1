package cr.tec.bd.crv.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access class for association records managed by admins.
 */
public class AssociationRepository {

    // Associations are registered by an admin; they no longer have their own login account.
    public void registerAssociation(String name) throws SQLException {
        requireValue(name, "El nombre de la asociacion es obligatorio.");

        try (Connection connection = ConexionBD.conectar()) {
            long associationId = nextSequenceValue(connection, "sAssociation");
            String sql = "INSERT INTO association(id, name) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, associationId);
                statement.setString(2, name.trim());
                statement.executeUpdate();
            }
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
