package cr.tec.bd.crv.database;

import cr.tec.bd.crv.util.SessionContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Writes application-level audit rows.
 *
 * <p>All users connect to Oracle with the same database account, so database
 * audit columns alone cannot tell which app profile made the change. This class
 * records the logged-in email from {@link SessionContext} in the journal table.</p>
 */
public class ApplicationAuditRepository {

    /**
     * Saves a compact audit entry inside the transaction that made the change.
     */
    public void log(Connection connection, String moduleName, String fieldName, String previousValue, String currentValue)
            throws SQLException {
        String actor = SessionContext.getCurrentEmail();
        if (actor == null || actor.trim().isEmpty()) {
            actor = SessionContext.isAdmin() ? "ADMIN" : "UNKNOWN";
        }

        String sql = """
                INSERT INTO journal(
                    id,
                    fieldName,
                    previousValue,
                    currentValue,
                    changeDate,
                    changedBy,
                    createdBy,
                    modifiedBy,
                    createdAt,
                    modifiedAt
                )
                VALUES(sJournal.NEXTVAL, ?, ?, ?, SYSDATE, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fit(moduleName + "." + fieldName, 25));
            statement.setString(2, fit(previousValue, 25));
            statement.setString(3, fit(currentValue, 25));
            statement.setString(4, fit(actor, 25));
            statement.setString(5, fit(actor, 25));
            statement.setString(6, fit(actor, 25));
            statement.setString(7, "APP");
            statement.setString(8, "APP");
            statement.executeUpdate();
        }
    }

    private String fit(String value, int maxLength) {
        if (value == null || value.trim().isEmpty()) {
            return "-";
        }

        String normalizedValue = value.trim();
        if (normalizedValue.length() > maxLength) {
            return normalizedValue.substring(0, maxLength);
        }
        return normalizedValue;
    }
}
