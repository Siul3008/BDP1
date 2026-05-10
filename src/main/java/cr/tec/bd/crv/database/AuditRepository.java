package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.AuditRecord;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Reads audit information for the admin audit screen.
 *
 * <p>The application prefers rows from the journal table because those rows use
 * the logged-in app profile. If journal does not exist, it falls back to table
 * audit columns when available.</p>
 */
public class AuditRepository {

    /**
     * Returns recent audit rows filtered by module, user/email, or changed field.
     */
    public List<AuditRecord> findAuditRecords(String moduleFilter, String userFilter, String fieldFilter)
            throws SQLException {
        try (Connection connection = ConexionBD.conectar()) {
            List<AuditRecord> records = new ArrayList<>();

            // Journal is the application audit source. If it exists but is empty, we show an empty list
            // instead of falling back to table audit columns that only know the shared Oracle user.
            if (hasTable(connection, "JOURNAL")) {
                addJournalRecords(connection, records);
            } else {
                addAuditTableRecords(connection, records, "PET", "Mascotas", "name");
                addAuditTableRecords(connection, records, "ADOPTION", "Adopciones", "TO_CHAR(id)");
                addAuditTableRecords(connection, records, "DONATION", "Donaciones", "TO_CHAR(amount)");
                addAuditTableRecords(connection, records, "ASSOCIATION", "Asociaciones", "name");
                addAuditTableRecords(connection, records, "PERSON", "Personas", "firstName || ' ' || firstLastName");
            }

            String module = normalized(moduleFilter);
            String user = normalized(userFilter);
            String field = normalized(fieldFilter);

            return records.stream()
                    .filter(record -> contains(record.getModuleName(), module))
                    .filter(record -> contains(record.getChangedBy(), user))
                    .filter(record -> contains(record.getFieldName(), field))
                    .sorted(Comparator.comparing(AuditRecord::getChangeDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                    .limit(150)
                    .toList();
        }
    }

    private void addJournalRecords(Connection connection, List<AuditRecord> records) throws SQLException {
        if (!hasTable(connection, "JOURNAL")) {
            return;
        }

        String sql = """
                SELECT *
                FROM (
                    SELECT id, fieldName, previousValue, currentValue, changedBy, changeDate
                    FROM journal
                    ORDER BY changeDate DESC NULLS LAST, id DESC
                )
                WHERE ROWNUM <= 150
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String rawField = valueOrEmpty(resultSet.getString("fieldName"));
                records.add(new AuditRecord(
                        resultSet.getLong("id"),
                        moduleFromJournalField(rawField),
                        fieldFromJournalField(rawField),
                        valueOrEmpty(resultSet.getString("previousValue")),
                        valueOrEmpty(resultSet.getString("currentValue")),
                        valueOrEmpty(resultSet.getString("changedBy")),
                        resultSet.getDate("changeDate") == null ? null : resultSet.getDate("changeDate").toLocalDate()
                ));
            }
        }
    }

    private void addAuditTableRecords(
            Connection connection,
            List<AuditRecord> records,
            String tableName,
            String moduleName,
            String valueExpression
    ) throws SQLException {
        if (!hasTable(connection, tableName)
                || !hasColumn(connection, tableName, "CREATION_DATE")
                || !hasColumn(connection, tableName, "CREATED_BY")) {
            return;
        }

        boolean hasUpdatedDate = hasColumn(connection, tableName, "UPDATED_DATE");
        boolean hasUpdatedBy = hasColumn(connection, tableName, "UPDATED_BY");
        String dateExpression = hasUpdatedDate
                ? "NVL(updated_Date, creation_Date)"
                : "creation_Date";
        String userExpression = hasUpdatedBy
                ? "NVL(updated_By, created_By)"
                : "created_By";

        String sql = """
                SELECT *
                FROM (
                    SELECT
                        id,
                        CAST(NULL AS VARCHAR2(25)) AS previousValue,
                        %s AS currentValue,
                        %s AS changedBy,
                        %s AS changeDate
                    FROM %s
                    WHERE %s IS NOT NULL
                    ORDER BY %s DESC NULLS LAST, id DESC
                )
                WHERE ROWNUM <= 80
                """.formatted(valueExpression, userExpression, dateExpression, tableName, dateExpression, dateExpression);

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                records.add(new AuditRecord(
                        resultSet.getLong("id"),
                        moduleName,
                        "Registro / actualizacion",
                        valueOrEmpty(resultSet.getString("previousValue")),
                        valueOrEmpty(resultSet.getString("currentValue")),
                        valueOrEmpty(resultSet.getString("changedBy")),
                        resultSet.getDate("changeDate") == null ? null : resultSet.getDate("changeDate").toLocalDate()
                ));
            }
        }
    }

    private boolean hasTable(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet resultSet = metaData.getTables(null, null, tableName.toUpperCase(Locale.ROOT), null)) {
            return resultSet.next();
        }
    }

    private boolean hasColumn(Connection connection, String tableName, String columnName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet resultSet = metaData.getColumns(
                null,
                null,
                tableName.toUpperCase(Locale.ROOT),
                columnName.toUpperCase(Locale.ROOT)
        )) {
            return resultSet.next();
        }
    }

    private boolean contains(String value, String filter) {
        if (filter == null) {
            return true;
        }
        return value != null && value.toLowerCase(Locale.ROOT).contains(filter);
    }

    private String normalized(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private String moduleFromJournalField(String fieldName) {
        int separatorIndex = fieldName.indexOf('.');
        if (separatorIndex <= 0) {
            return "Journal";
        }
        return fieldName.substring(0, separatorIndex);
    }

    private String fieldFromJournalField(String fieldName) {
        int separatorIndex = fieldName.indexOf('.');
        if (separatorIndex < 0 || separatorIndex >= fieldName.length() - 1) {
            return fieldName;
        }
        return fieldName.substring(separatorIndex + 1);
    }
}
