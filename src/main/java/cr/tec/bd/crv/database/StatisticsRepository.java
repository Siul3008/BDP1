package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.ChartDataPoint;
import cr.tec.bd.crv.model.StatisticSummary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access class for dashboard statistics.
 */
public class StatisticsRepository {

    public StatisticSummary loadSummary() throws SQLException {
        try (Connection connection = ConexionBD.conectar()) {
            int totalPets = count(connection, "SELECT COUNT(*) FROM pet");
            int adoptedPets = count(connection, """
                    SELECT COUNT(*)
                    FROM pet p
                    JOIN petStatus ps
                        ON ps.id = p.idPetStatus
                    WHERE LOWER(ps.status) LIKE '%adopt%'
                       OR LOWER(ps.status) LIKE '%adop%'
                    """);
            int activeFosterHomes = count(connection, "SELECT COUNT(*) FROM fosterHome");

            return new StatisticSummary(
                    totalPets,
                    adoptedPets,
                    adoptionRate(totalPets, adoptedPets),
                    donationTotals(connection),
                    activeFosterHomes,
                    groupedCounts(connection, """
                            SELECT NVL(ps.status, 'No status') AS label, COUNT(*) AS value
                            FROM pet p
                            LEFT JOIN petStatus ps
                                ON ps.id = p.idPetStatus
                            GROUP BY NVL(ps.status, 'No status')
                            ORDER BY COUNT(*) DESC, label
                            """),
                    groupedCounts(connection, """
                            SELECT NVL(pt.name, 'No type') AS label, COUNT(*) AS value
                            FROM pet p
                            LEFT JOIN petType pt
                                ON pt.id = p.idPetType
                            GROUP BY NVL(pt.name, 'No type')
                            ORDER BY COUNT(*) DESC, label
                            """)
            );
        }
    }

    private int count(Connection connection, String sql) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    private List<ChartDataPoint> groupedCounts(Connection connection, String sql) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
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

    private String donationTotals(Connection connection) throws SQLException {
        String sql = """
                SELECT NVL(c.acronym, 'N/A') AS currency, SUM(d.amount) AS total
                FROM donation d
                LEFT JOIN currency c
                    ON c.id = d.idCurrency
                GROUP BY NVL(c.acronym, 'N/A')
                ORDER BY currency
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<String> totals = new ArrayList<>();
            while (resultSet.next()) {
                totals.add(resultSet.getString("currency") + " " + resultSet.getBigDecimal("total").toPlainString());
            }
            return totals.isEmpty() ? "0.00" : String.join(" / ", totals);
        }
    }

    private String adoptionRate(int totalPets, int adoptedPets) {
        if (totalPets == 0) {
            return "0%";
        }

        double percentage = (adoptedPets * 100.0) / totalPets;
        return String.format("%.1f%%", percentage);
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
