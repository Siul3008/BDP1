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
 * Data access class for journal and audit-column review.
 */
public class AuditRepository {

    public List<AuditRecord> findAuditRecords(String moduleFilter, String userFilter, String fieldFilter)
            throws SQLException {
        try (Connection connection = ConexionBD.conectar()) {
            List<AuditRecord> records = new ArrayList<>();
            addJournalRecords(connection, records);
            addAuditTableRecords(connection, records, "PET", "Mascotas", "name");
            addAuditTableRecords(connection, records, "ADOPTION", "Adopciones", "TO_CHAR(id)");
            addAuditTableRecords(connection, records, "DONATION", "Donaciones", "TO_CHAR(amount)");
            addAuditTableRecords(connection, records, "ASSOCIATION", "Asociaciones", "name");
            addAuditTableRecords(connection, records, "PERSON", "Personas", "firstName || ' ' || firstLastName");

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
                SELECT id, fieldName, previousValue, currentValue, changedBy, changeDate
                FROM journal
                ORDER BY changeDate DESC NULLS LAST, id DESC
                FETCH FIRST 150 ROWS ONLY
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                records.add(new AuditRecord(
                        resultSet.getLong("id"),
                        "Journal",
                        valueOrEmpty(resultSet.getString("fieldName")),
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
                SELECT
                    id,
                    CAST(NULL AS VARCHAR2(25)) AS previousValue,
                    %s AS currentValue,
                    %s AS changedBy,
                    %s AS changeDate
                FROM %s
                WHERE %s IS NOT NULL
                ORDER BY %s DESC NULLS LAST, id DESC
                FETCH FIRST 80 ROWS ONLY
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
}
