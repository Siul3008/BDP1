package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.ReportRow;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains prepared SQL reports for the admin report screen.
 *
 * <p>Each public method returns rows with five generic columns. The controller
 * changes the visible table headers depending on which report is selected.</p>
 */
public class ReportRepository {

    /**
     * Counts pets grouped by status and pet type.
     */
    public List<ReportRow> findPetsByStatus(String textFilter, LocalDate from, LocalDate to) throws SQLException {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT
                    NVL(ps.status, 'No status') AS column1,
                    NVL(pt.name, 'No type') AS column2,
                    TO_CHAR(COUNT(*)) AS column3,
                    NVL(TO_CHAR(MAX(p.eventDate), 'YYYY-MM-DD'), 'No date') AS column4,
                    TO_CHAR(ROUND(100 * COUNT(*) / NULLIF(SUM(COUNT(*)) OVER (), 0), 2), 'FM990.00') || '%' AS column5
                FROM pet p
                LEFT JOIN petStatus ps
                    ON ps.id = p.idPetStatus
                LEFT JOIN petType pt
                    ON pt.id = p.idPetType
                WHERE 1 = 1
                """);

        addDateFilters(sql, parameters, "p.eventDate", from, to);
        addTextFilter(sql, parameters, textFilter, "ps.status", "pt.name", "p.name");

        sql.append("""
                GROUP BY NVL(ps.status, 'No status'), NVL(pt.name, 'No type')
                ORDER BY COUNT(*) DESC, column1, column2
                """);

        return queryRows(sql.toString(), parameters);
    }

    /**
     * Summarizes donated money by association and currency.
     */
    public List<ReportRow> findDonationsByAssociation(String textFilter, LocalDate from, LocalDate to)
            throws SQLException {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT
                    a.name AS column1,
                    NVL(c.acronym, 'N/A') AS column2,
                    TO_CHAR(SUM(d.amount), 'FM9999999990.00') AS column3,
                    TO_CHAR(COUNT(*)) AS column4,
                    NVL(TO_CHAR(MAX(d.donationDate), 'YYYY-MM-DD'), 'No date') AS column5
                FROM donation d
                JOIN associationxDonation ad
                    ON ad.idDonation = d.id
                JOIN association a
                    ON a.id = ad.idAssociation
                LEFT JOIN currency c
                    ON c.id = d.idCurrency
                LEFT JOIN personxDonation pd
                    ON pd.idDonation = d.id
                LEFT JOIN person p
                    ON p.id = pd.idPerson
                WHERE 1 = 1
                """);

        addDateFilters(sql, parameters, "d.donationDate", from, to);
        addTextFilter(sql, parameters, textFilter, "a.name", "c.acronym", "p.firstName", "p.firstLastName");

        sql.append("""
                GROUP BY a.name, NVL(c.acronym, 'N/A')
                ORDER BY SUM(d.amount) DESC, a.name
                """);

        return queryRows(sql.toString(), parameters);
    }

    /**
     * Looks for possible matches between reports and pets with similar characteristics.
     */
    public List<ReportRow> findPotentialMatches(String textFilter, LocalDate from, LocalDate to) throws SQLException {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.name AS column1,
                    pt.name AS column2,
                    b.name AS column3,
                    co.name AS column4,
                    NVL(TO_CHAR(pr.reportDate, 'YYYY-MM-DD'), 'No date') AS column5
                FROM petReport pr
                JOIN pet p
                    ON p.idPetType = pr.idPetType
                   AND p.idBreed = pr.idBreed
                   AND p.idSize = pr.idSize
                   AND p.idColor = pr.idColor
                JOIN petType pt
                    ON pt.id = p.idPetType
                JOIN breed b
                    ON b.id = p.idBreed
                JOIN color co
                    ON co.id = p.idColor
                WHERE 1 = 1
                """);

        addDateFilters(sql, parameters, "pr.reportDate", from, to);
        addTextFilter(sql, parameters, textFilter, "p.name", "pt.name", "b.name", "co.name");

        sql.append(" ORDER BY pr.reportDate DESC NULLS LAST, p.name");
        return queryRows(sql.toString(), parameters);
    }

    /**
     * Shows blacklist reports with reporter, reported person, rating, reason, and date.
     */
    public List<ReportRow> findBlacklist(String textFilter, LocalDate from, LocalDate to) throws SQLException {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT
                    reporter.firstName || ' ' || reporter.firstLastName AS column1,
                    reportee.firstName || ' ' || reportee.firstLastName AS column2,
                    NVL(sr.star, 'N/A') AS column3,
                    bl.reason AS column4,
                    TO_CHAR(bl.reportDate, 'YYYY-MM-DD') AS column5
                FROM blacklistReport bl
                LEFT JOIN person reporter
                    ON reporter.id = bl.idReporter
                JOIN person reportee
                    ON reportee.id = bl.idReportee
                LEFT JOIN starRating sr
                    ON sr.id = bl.idStarRating
                WHERE 1 = 1
                """);

        addDateFilters(sql, parameters, "bl.reportDate", from, to);
        addTextFilter(
                sql,
                parameters,
                textFilter,
                "reporter.firstName",
                "reporter.firstLastName",
                "reportee.firstName",
                "reportee.firstLastName",
                "bl.reason"
        );

        sql.append(" ORDER BY bl.reportDate DESC, bl.id DESC");
        return queryRows(sql.toString(), parameters);
    }

    public List<ReportRow> findNotAdoptedAfterTwoMonths(String textFilter) throws SQLException {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.name AS column1,
                    NVL(pt.name, 'No type') AS column2,
                    NVL(b.name, 'No breed') AS column3,
                    NVL(TO_CHAR(p.eventDate, 'YYYY-MM-DD'), 'No date') AS column4,
                    TO_CHAR(TRUNC(SYSDATE - NVL(p.eventDate, SYSDATE))) || ' dias' AS column5
                FROM pet p
                LEFT JOIN petType pt
                    ON pt.id = p.idPetType
                LEFT JOIN breed b
                    ON b.id = p.idBreed
                LEFT JOIN petStatus ps
                    ON ps.id = p.idPetStatus
                WHERE (
                    LOWER(NVL(ps.status, '')) IN ('for adoption', 'en adopcion')
                    OR LOWER(NVL(ps.status, '')) LIKE 'en adopci%'
                )
                  AND p.eventDate <= ADD_MONTHS(TRUNC(SYSDATE), -2)
                """);

        addTextFilter(sql, parameters, textFilter, "p.name", "pt.name", "b.name", "ps.status");
        sql.append(" ORDER BY p.eventDate ASC NULLS LAST, p.name");
        return queryRows(sql.toString(), parameters);
    }

    public List<ReportRow> findTopRescuers() throws SQLException {
        String sql = """
                SELECT *
                FROM (
                    SELECT
                        p.firstName || ' ' || p.firstLastName AS column1,
                        TO_CHAR(COUNT(rxp.idPet)) AS column2,
                        NVL(p.secondName, '-') AS column3,
                        NVL(p.secondLastName, '-') AS column4,
                        TO_CHAR(p.id) AS column5
                    FROM rescuerxPet rxp
                    JOIN rescuer r
                        ON rxp.idRescuer = r.idPerson
                    JOIN person p
                        ON r.idPerson = p.id
                    GROUP BY p.id, p.firstName, p.secondName, p.firstLastName, p.secondLastName
                    ORDER BY COUNT(rxp.idPet) DESC
                )
                WHERE ROWNUM <= 10
                """;
        return queryRows(sql, List.of());
    }

    public List<ReportRow> findFosterHomesByAcceptedType() throws SQLException {
        String sql = """
                SELECT
                    p.firstName || ' ' || p.firstLastName AS column1,
                    TO_CHAR(COUNT(DISTINCT pt.id)) AS column2,
                    LISTAGG(pt.name, ', ') WITHIN GROUP (ORDER BY pt.name) AS column3,
                    TO_CHAR(fh.idPerson) AS column4,
                    'Casa cuna' AS column5
                FROM fosterHomexFosterCondition fhxfc
                JOIN fosterHome fh
                    ON fhxfc.idFosterHome = fh.idPerson
                JOIN fosterConditionxAccType fcxat
                    ON fhxfc.idFosterCondition = fcxat.idFosterCondition
                JOIN petType pt
                    ON fcxat.idPetType = pt.id
                JOIN person p
                    ON fh.idPerson = p.id
                GROUP BY fh.idPerson, p.firstName, p.firstLastName
                ORDER BY COUNT(DISTINCT pt.id) DESC, column1
                """;
        return queryRows(sql, List.of());
    }

    public List<ReportRow> findCriticalPetsInAdoption(String textFilter) throws SQLException {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.name AS column1,
                    NVL(pt.name, 'No type') AS column2,
                    NVL(b.name, 'No breed') AS column3,
                    NVL(hs.illnessState, 'No health') AS column4,
                    NVL(hs.description, '-') AS column5
                FROM pet p
                LEFT JOIN petType pt
                    ON pt.id = p.idPetType
                LEFT JOIN breed b
                    ON b.id = p.idBreed
                JOIN petStatus ps
                    ON ps.id = p.idPetStatus
                JOIN petxHealthStatus pxhs
                    ON pxhs.idPet = p.id
                JOIN healthStatus hs
                    ON hs.id = pxhs.idHealthStatus
                WHERE (
                    LOWER(NVL(ps.status, '')) IN ('for adoption', 'en adopcion')
                    OR LOWER(NVL(ps.status, '')) LIKE 'en adopci%'
                )
                  AND (
                    LOWER(NVL(hs.illnessState, '')) LIKE '%critical%'
                    OR LOWER(NVL(hs.illnessState, '')) LIKE '%critico%'
                    OR LOWER(NVL(hs.illnessState, '')) LIKE '%grave%'
                  )
                """);

        addTextFilter(sql, parameters, textFilter, "p.name", "pt.name", "b.name", "hs.illnessState", "hs.description");
        sql.append(" ORDER BY p.name");
        return queryRows(sql.toString(), parameters);
    }

    private List<ReportRow> queryRows(String sql, List<Object> parameters) throws SQLException {
        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, parameters);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<ReportRow> rows = new ArrayList<>();
                while (resultSet.next()) {
                    rows.add(new ReportRow(
                            valueOrEmpty(resultSet.getString("column1")),
                            valueOrEmpty(resultSet.getString("column2")),
                            valueOrEmpty(resultSet.getString("column3")),
                            valueOrEmpty(resultSet.getString("column4")),
                            valueOrEmpty(resultSet.getString("column5"))
                    ));
                }
                return rows;
            }
        }
    }

    private void addDateFilters(
            StringBuilder sql,
            List<Object> parameters,
            String columnName,
            LocalDate from,
            LocalDate to
    ) {
        if (from != null) {
            sql.append(" AND ").append(columnName).append(" >= ?\n");
            parameters.add(Date.valueOf(from));
        }

        if (to != null) {
            sql.append(" AND ").append(columnName).append(" <= ?\n");
            parameters.add(Date.valueOf(to));
        }
    }

    private void addTextFilter(StringBuilder sql, List<Object> parameters, String textFilter, String... columns) {
        String normalizedFilter = emptyToNull(textFilter);
        if (normalizedFilter == null || columns.length == 0) {
            return;
        }

        List<String> predicates = new ArrayList<>();
        for (String column : columns) {
            predicates.add("LOWER(NVL(" + column + ", '')) LIKE ?");
            parameters.add("%" + normalizedFilter.toLowerCase() + "%");
        }
        sql.append(" AND (").append(String.join(" OR ", predicates)).append(")\n");
    }

    private void setParameters(PreparedStatement statement, List<Object> parameters) throws SQLException {
        for (int index = 0; index < parameters.size(); index++) {
            Object value = parameters.get(index);
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
