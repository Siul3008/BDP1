package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.BlacklistRecord;
import cr.tec.bd.crv.model.CatalogOption;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Handles blacklist reporting and review.
 *
 * <p>The blacklist links a reporter, a reported person, a rating, and the reason
 * for the report. This repository also checks that the newer blacklist columns
 * exist before trying to use them.</p>
 */
public class BlacklistRepository {

    private final ApplicationAuditRepository auditRepository = new ApplicationAuditRepository();

    /**
     * Returns people as combo-box options for reporter/reportee selection.
     */
    public List<CatalogOption> findPeopleOptions() throws SQLException {
        String sql = """
                SELECT
                    p.id,
                    TRIM(
                        p.firstName ||
                        CASE WHEN p.secondName IS NOT NULL THEN ' ' || p.secondName ELSE '' END ||
                        ' ' || p.firstLastName ||
                        CASE WHEN p.secondLastName IS NOT NULL THEN ' ' || p.secondLastName ELSE '' END
                    ) ||
                    CASE WHEN aa.loginEmail IS NOT NULL THEN ' - ' || aa.loginEmail ELSE '' END AS label
                FROM person p
                LEFT JOIN appAccount aa
                    ON aa.idPerson = p.id
                   AND aa.accountType = 'USER'
                   AND aa.isActive = 'Y'
                ORDER BY p.firstName, p.firstLastName
                """;

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<CatalogOption> people = new ArrayList<>();
            while (resultSet.next()) {
                people.add(new CatalogOption(resultSet.getLong("id"), resultSet.getString("label")));
            }
            return people;
        }
    }

    /**
     * Returns blacklist reports, optionally filtered by text.
     */
    public List<BlacklistRecord> findReports(String textFilter) throws SQLException {
        try (Connection connection = ConexionBD.conectar()) {
            ensureModernBlacklistSchema(connection);

            List<Object> parameters = new ArrayList<>();
            StringBuilder sql = new StringBuilder("""
                    SELECT
                        bl.id,
                        NVL(
                            TRIM(
                                reporter.firstName ||
                                CASE WHEN reporter.secondName IS NOT NULL THEN ' ' || reporter.secondName ELSE '' END ||
                                ' ' || reporter.firstLastName ||
                                CASE WHEN reporter.secondLastName IS NOT NULL THEN ' ' || reporter.secondLastName ELSE '' END
                            ),
                            'Administrador'
                        ) AS reporterName,
                        TRIM(
                            reportee.firstName ||
                            CASE WHEN reportee.secondName IS NOT NULL THEN ' ' || reportee.secondName ELSE '' END ||
                            ' ' || reportee.firstLastName ||
                            CASE WHEN reportee.secondLastName IS NOT NULL THEN ' ' || reportee.secondLastName ELSE '' END
                        ) AS reporteeName,
                        NVL(sr.star, 'N/A') AS rating,
                        NVL(TO_CHAR(ROUND((
                            SELECT AVG(TO_NUMBER(srAvg.star))
                            FROM blacklistReport blAvg
                            JOIN starRating srAvg
                                ON srAvg.id = blAvg.idStarRating
                            WHERE blAvg.idReportee = bl.idReportee
                        ), 2), 'FM990.00'), 'N/A') AS averageRating,
                        bl.reason,
                        NVL(bl.active, 'Y') AS active,
                        bl.reportDate
                    FROM blacklistReport bl
                    LEFT JOIN person reporter
                        ON reporter.id = bl.idReporter
                    JOIN person reportee
                        ON reportee.id = bl.idReportee
                    LEFT JOIN starRating sr
                        ON sr.id = bl.idStarRating
                    WHERE 1 = 1
                    """);

            String normalizedFilter = emptyToNull(textFilter);
            if (normalizedFilter != null) {
                sql.append("""
                         AND (
                            LOWER(NVL(reporter.firstName, '') || ' ' || NVL(reporter.firstLastName, '')) LIKE ?
                            OR LOWER(reportee.firstName || ' ' || reportee.firstLastName) LIKE ?
                            OR LOWER(bl.reason) LIKE ?
                            OR LOWER(NVL(sr.star, '')) LIKE ?
                         )
                        """);
                String value = "%" + normalizedFilter.toLowerCase(Locale.ROOT) + "%";
                parameters.add(value);
                parameters.add(value);
                parameters.add(value);
                parameters.add(value);
            }

            sql.append(" ORDER BY bl.reportDate DESC, bl.id DESC");

            try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
                setParameters(statement, parameters);

                try (ResultSet resultSet = statement.executeQuery()) {
                    List<BlacklistRecord> reports = new ArrayList<>();
                    while (resultSet.next()) {
                        Date reportDate = resultSet.getDate("reportDate");
                        reports.add(new BlacklistRecord(
                                resultSet.getLong("id"),
                                valueOrEmpty(resultSet.getString("reporterName")),
                                valueOrEmpty(resultSet.getString("reporteeName")),
                                valueOrEmpty(resultSet.getString("rating")),
                                valueOrEmpty(resultSet.getString("averageRating")),
                                valueOrEmpty(resultSet.getString("reason")),
                                activeLabel(resultSet.getString("active")),
                                reportDate == null ? null : reportDate.toLocalDate()
                        ));
                    }
                    return reports;
                }
            }
        }
    }

    /**
     * Saves a blacklist report and the rating attached to it.
     */
    public void registerReport(Long reporterId, Long reporteeId, String rating, String reason, LocalDate reportDate)
            throws SQLException {
        validateReport(reporterId, reporteeId, rating, reason, reportDate);

        try (Connection connection = ConexionBD.conectar()) {
            ensureModernBlacklistSchema(connection);
            connection.setAutoCommit(false);

            try {
                String reporteeName = findPersonName(connection, reporteeId);
                long starRatingId = nextSequenceValue(connection, "sStarRating");
                long blacklistId = nextSequenceValue(connection, "sBlackistReport");

                insertStarRating(connection, starRatingId, reporteeName, rating, reportDate);
                insertBlacklistReport(connection, blacklistId, reporterId, reporteeId, starRatingId, reason, reportDate);
                linkPersonBlacklistReport(connection, reporteeId, blacklistId);
                updateAdopterRating(connection, reporteeId, starRatingId, reason);
                auditRepository.log(connection, "Lista negra", "Reporte", "person:" + reporterId, reporteeName);

                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private void validateReport(Long reporterId, Long reporteeId, String rating, String reason, LocalDate reportDate) {
        if (reporterId == null) {
            throw new IllegalArgumentException("Seleccione quien realiza el reporte.");
        }
        if (reporteeId == null) {
            throw new IllegalArgumentException("Seleccione la persona reportada.");
        }
        if (reporterId.equals(reporteeId)) {
            throw new IllegalArgumentException("La persona reportante y reportada no pueden ser la misma.");
        }
        if (emptyToNull(rating) == null) {
            throw new IllegalArgumentException("Seleccione una calificacion.");
        }
        if (emptyToNull(reason) == null) {
            throw new IllegalArgumentException("Digite la razon del reporte.");
        }
        if (reportDate == null) {
            throw new IllegalArgumentException("Seleccione la fecha del reporte.");
        }
    }

    private void ensureModernBlacklistSchema(Connection connection) throws SQLException {
        if (!hasColumn(connection, "blacklistReport", "idReporter")
                || !hasColumn(connection, "blacklistReport", "idReportee")
                || !hasColumn(connection, "blacklistReport", "idStarRating")) {
            throw new IllegalStateException(
                    "Falta actualizar blacklistReport. Ejecute el script 20_patch_current_schema_front_alignment.sql."
            );
        }
    }

    private String findPersonName(Connection connection, long personId) throws SQLException {
        String sql = """
                SELECT TRIM(firstName || ' ' || firstLastName) AS name
                FROM person
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("La persona seleccionada no existe.");
                }
                return resultSet.getString("name");
            }
        }
    }

    private void insertStarRating(
            Connection connection,
            long starRatingId,
            String personName,
            String rating,
            LocalDate reportDate
    ) throws SQLException {
        String sql = "INSERT INTO starRating(id, name, star, ratingDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, starRatingId);
            statement.setString(2, fit(personName, 20));
            statement.setString(3, fit(rating, 10));
            statement.setDate(4, Date.valueOf(reportDate));
            statement.executeUpdate();
        }
    }

    private void insertBlacklistReport(
            Connection connection,
            long blacklistId,
            long reporterId,
            long reporteeId,
            long starRatingId,
            String reason,
            LocalDate reportDate
    ) throws SQLException {
        String sql = """
                INSERT INTO blacklistReport(id, idReporter, idReportee, idStarRating, reason, active, reportDate)
                VALUES (?, ?, ?, ?, ?, 'Y', ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, blacklistId);
            statement.setLong(2, reporterId);
            statement.setLong(3, reporteeId);
            statement.setLong(4, starRatingId);
            statement.setString(5, fit(reason, 60));
            statement.setDate(6, Date.valueOf(reportDate));
            statement.executeUpdate();
        }
    }

    private void linkPersonBlacklistReport(Connection connection, long personId, long blacklistId) throws SQLException {
        if (!hasTable(connection, "personxBListRep")) {
            return;
        }

        String sql = """
                MERGE INTO personxBListRep target
                USING (SELECT ? AS idPerson, ? AS idBLReport FROM dual) source
                ON (target.idPerson = source.idPerson AND target.idBLReport = source.idBLReport)
                WHEN NOT MATCHED THEN
                    INSERT (idPerson, idBLReport) VALUES (source.idPerson, source.idBLReport)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);
            statement.setLong(2, blacklistId);
            statement.executeUpdate();
        }
    }

    private void updateAdopterRating(Connection connection, long personId, long starRatingId, String reason)
            throws SQLException {
        String sql = "UPDATE adopter SET idStarRating = ?, note = ? WHERE idPerson = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, starRatingId);
            statement.setString(2, fit(reason, 50));
            statement.setLong(3, personId);
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

    private void setParameters(PreparedStatement statement, List<Object> values) throws SQLException {
        for (int index = 0; index < values.size(); index++) {
            statement.setObject(index + 1, values.get(index));
        }
    }

    private String activeLabel(String active) {
        return "N".equalsIgnoreCase(active) ? "Inactivo" : "Activo";
    }

    private String fit(String value, int maxLength) {
        String normalizedValue = emptyToNull(value);
        if (normalizedValue == null) {
            return null;
        }
        return normalizedValue.length() <= maxLength
                ? normalizedValue
                : normalizedValue.substring(0, maxLength);
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
