package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.ChartDataPoint;
import cr.tec.bd.crv.model.StatisticSummary;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculates the numbers and chart data shown in the statistics dashboard.
 *
 * <p>The controller should only display values. This repository performs the
 * counting, grouping, filtering, and percentage calculations against Oracle.</p>
 */
public class StatisticsRepository {

    /**
     * Loads statistics without filters.
     */
    public StatisticSummary loadSummary() throws SQLException {
        return loadSummary(null, null, null, null);
    }

    /**
     * Loads statistics filtered by date range, pet type, and breed.
     */
    public StatisticSummary loadSummary(LocalDate from, LocalDate to, Long petTypeId, Long breedId) throws SQLException {
        try (Connection connection = ConexionBD.conectar()) {
            List<Object> petParameters = petFilterParameters(from, to, petTypeId, breedId);
            String petFilter = petFilterSql("p", from, to, petTypeId, breedId);

            int totalPets = count(connection, "SELECT COUNT(*) FROM pet p WHERE 1 = 1 " + petFilter, petParameters);
            int adoptedPets = count(connection, """
                    SELECT COUNT(*)
                    FROM pet p
                    JOIN petStatus ps
                        ON ps.id = p.idPetStatus
                    WHERE LOWER(NVL(ps.status, '')) IN ('adopted', 'adoptado', 'adoptada')
                    """ + petFilter, petParameters);
            int activeFosterHomes = count(connection, "SELECT COUNT(*) FROM fosterHome");

            return new StatisticSummary(
                    totalPets,
                    adoptedPets,
                    adoptionRate(totalPets, adoptedPets),
                    donationTotals(connection, from, to),
                    activeFosterHomes,
                    groupedCounts(connection, """
                            SELECT NVL(ps.status, 'No status') AS label, COUNT(*) AS value
                            FROM pet p
                            LEFT JOIN petStatus ps
                                ON ps.id = p.idPetStatus
                            WHERE 1 = 1
                            """ + petFilter + """
                            GROUP BY NVL(ps.status, 'No status')
                            ORDER BY COUNT(*) DESC, label
                            """, petParameters),
                    groupedCounts(connection, """
                            SELECT NVL(pt.name, 'No type') AS label, COUNT(*) AS value
                            FROM pet p
                            LEFT JOIN petType pt
                                ON pt.id = p.idPetType
                            WHERE 1 = 1
                            """ + petFilter + """
                            GROUP BY NVL(pt.name, 'No type')
                            ORDER BY COUNT(*) DESC, label
                            """, petParameters),
                    donationTotalsByAssociation(connection, from, to),
                    adoptionOutcome(connection, petFilter, petParameters),
                    petsByAgeRange(connection, petFilter, petParameters),
                    criticalAdoptionPetsByType(connection, petFilter, petParameters)
            );
        }
    }

    private int count(Connection connection, String sql) throws SQLException {
        return count(connection, sql, List.of());
    }

    private int count(Connection connection, String sql, List<Object> parameters) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ) {
            setParameters(statement, parameters);
            try (ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
            }
        }
    }

    private List<ChartDataPoint> groupedCounts(Connection connection, String sql) throws SQLException {
        return groupedCounts(connection, sql, List.of());
    }

    private List<ChartDataPoint> groupedCounts(Connection connection, String sql, List<Object> parameters)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ) {
            setParameters(statement, parameters);
            try (ResultSet resultSet = statement.executeQuery()) {
            List<ChartDataPoint> values = new ArrayList<>();
            while (resultSet.next()) {
                values.add(new ChartDataPoint(
                        valueOrEmpty(resultSet.getString("label")),
                        resultSet.getInt("value")
                ));
            }
            return values;
            }
        }
    }

    private String donationTotals(Connection connection, LocalDate from, LocalDate to) throws SQLException {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT NVL(c.acronym, 'N/A') AS currency, SUM(d.amount) AS total
                FROM donation d
                LEFT JOIN currency c
                    ON c.id = d.idCurrency
                WHERE 1 = 1
                """);
        addDateFilter(sql, parameters, "d.donationDate", from, to);
        sql.append("""
                GROUP BY NVL(c.acronym, 'N/A')
                ORDER BY currency
                """);

        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            setParameters(statement, parameters);
            try (ResultSet resultSet = statement.executeQuery()) {
            List<String> totals = new ArrayList<>();
            while (resultSet.next()) {
                totals.add(resultSet.getString("currency") + " " + resultSet.getBigDecimal("total").toPlainString());
            }
            return totals.isEmpty() ? "0.00" : String.join(" / ", totals);
            }
        }
    }

    private List<ChartDataPoint> donationTotalsByAssociation(Connection connection, LocalDate from, LocalDate to)
            throws SQLException {
        List<Object> parameters = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
                SELECT a.name AS label, ROUND(SUM(d.amount)) AS value
                FROM donation d
                JOIN associationxDonation ad
                    ON ad.idDonation = d.id
                JOIN association a
                    ON a.id = ad.idAssociation
                WHERE 1 = 1
                """);
        addDateFilter(sql, parameters, "d.donationDate", from, to);
        sql.append("""
                GROUP BY a.name
                ORDER BY SUM(d.amount) DESC, a.name
                """);
        return groupedCounts(connection, sql.toString(), parameters);
    }

    private List<ChartDataPoint> adoptionOutcome(Connection connection, String petFilter, List<Object> petParameters)
            throws SQLException {
        String sql = """
                SELECT outcome AS label, COUNT(*) AS value
                FROM (
                    SELECT CASE
                        WHEN LOWER(NVL(ps.status, '')) IN ('for adoption', 'en adopcion')
                            OR LOWER(NVL(ps.status, '')) LIKE 'en adopci%'
                            THEN 'En espera'
                        WHEN LOWER(NVL(ps.status, '')) IN ('adopted', 'adoptado', 'adoptada')
                            THEN 'Adoptadas'
                        ELSE 'Otros estados'
                    END AS outcome
                    FROM pet p
                    LEFT JOIN petStatus ps
                        ON ps.id = p.idPetStatus
                    WHERE 1 = 1
                    """ + petFilter + """
                )
                GROUP BY outcome
                ORDER BY value DESC
                """;
        return groupedCounts(connection, sql, petParameters);
    }

    private List<ChartDataPoint> petsByAgeRange(Connection connection, String petFilter, List<Object> petParameters)
            throws SQLException {
        String sql = """
                SELECT ageRange AS label, COUNT(*) AS value
                FROM (
                    SELECT CASE
                        WHEN p.age BETWEEN 0 AND 1 THEN '0 a 1'
                        WHEN p.age BETWEEN 2 AND 5 THEN '2 a 5'
                        WHEN p.age BETWEEN 6 AND 9 THEN '6 a 9'
                        WHEN p.age BETWEEN 10 AND 12 THEN '10 a 12'
                        WHEN p.age > 12 THEN 'Mayor de 12'
                        ELSE 'Sin edad'
                    END AS ageRange
                    FROM pet p
                    WHERE 1 = 1
                    """ + petFilter + """
                )
                GROUP BY ageRange
                ORDER BY MIN(CASE ageRange
                    WHEN '0 a 1' THEN 1
                    WHEN '2 a 5' THEN 2
                    WHEN '6 a 9' THEN 3
                    WHEN '10 a 12' THEN 4
                    WHEN 'Mayor de 12' THEN 5
                    ELSE 6
                END)
                """;
        return groupedCounts(connection, sql, petParameters);
    }

    private List<ChartDataPoint> criticalAdoptionPetsByType(
            Connection connection,
            String petFilter,
            List<Object> petParameters
    ) throws SQLException {
        String sql = """
                SELECT NVL(pt.name, 'No type') AS label, COUNT(DISTINCT p.id) AS value
                FROM pet p
                LEFT JOIN petType pt
                    ON pt.id = p.idPetType
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
                """ + petFilter + """
                GROUP BY NVL(pt.name, 'No type')
                ORDER BY COUNT(DISTINCT p.id) DESC, label
                """;
        return groupedCounts(connection, sql, petParameters);
    }

    private String adoptionRate(int totalPets, int adoptedPets) {
        if (totalPets == 0) {
            return "0%";
        }

        double percentage = (adoptedPets * 100.0) / totalPets;
        return String.format("%.1f%%", percentage);
    }

    private String petFilterSql(String petAlias, LocalDate from, LocalDate to, Long petTypeId, Long breedId) {
        StringBuilder filter = new StringBuilder();
        if (from != null) {
            filter.append(" AND ").append(petAlias).append(".eventDate >= ?\n");
        }
        if (to != null) {
            filter.append(" AND ").append(petAlias).append(".eventDate <= ?\n");
        }
        if (petTypeId != null) {
            filter.append(" AND ").append(petAlias).append(".idPetType = ?\n");
        }
        if (breedId != null) {
            filter.append(" AND ").append(petAlias).append(".idBreed = ?\n");
        }
        return filter.toString();
    }

    private List<Object> petFilterParameters(LocalDate from, LocalDate to, Long petTypeId, Long breedId) {
        List<Object> parameters = new ArrayList<>();
        if (from != null) {
            parameters.add(Date.valueOf(from));
        }
        if (to != null) {
            parameters.add(Date.valueOf(to));
        }
        if (petTypeId != null) {
            parameters.add(petTypeId);
        }
        if (breedId != null) {
            parameters.add(breedId);
        }
        return parameters;
    }

    private void addDateFilter(
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

    private void setParameters(PreparedStatement statement, List<Object> parameters) throws SQLException {
        for (int index = 0; index < parameters.size(); index++) {
            statement.setObject(index + 1, parameters.get(index));
        }
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
