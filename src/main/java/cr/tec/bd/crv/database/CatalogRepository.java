package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.CatalogOption;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Reads database catalogs used by form controls.
 */
public class CatalogRepository {

    public List<CatalogOption> findPetTypes() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM petType ORDER BY name");
    }

    public List<CatalogOption> findBreeds() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM breed ORDER BY name");
    }

    public List<CatalogOption> findBreedsByPetType(long petTypeId) throws SQLException {
        try (Connection connection = ConexionBD.conectar()) {
            String petTypeName = findPetTypeName(connection, petTypeId);

            if (!hasColumn(connection, "breed", "idPetType")) {
                List<CatalogOption> prefixedBreeds = findPrefixedBreeds(connection, petTypeName);
                if (!prefixedBreeds.isEmpty()) {
                    return prefixedBreeds;
                }
                return findBreeds();
            }

            String sql = """
                    SELECT id, name AS label
                    FROM breed
                    WHERE idPetType = ?
                    ORDER BY name
                    """;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, petTypeId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    List<CatalogOption> typedBreeds = readOptions(resultSet);
                    if (!typedBreeds.isEmpty()) {
                        return typedBreeds;
                    }
                }
            }

            List<CatalogOption> prefixedBreeds = findPrefixedBreeds(connection, petTypeName);
            if (!prefixedBreeds.isEmpty()) {
                return prefixedBreeds;
            }
            return findBreeds();
        }
    }

    private String findPetTypeName(Connection connection, long petTypeId) throws SQLException {
        String sql = "SELECT name FROM petType WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, petTypeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("name");
                }
                return "";
            }
        }
    }

    private List<CatalogOption> findPrefixedBreeds(Connection connection, String petTypeName) throws SQLException {
        String sql = """
                SELECT id, name AS label
                FROM breed
                WHERE LOWER(name) LIKE LOWER(?)
                ORDER BY name
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, petTypeName + " - %");

            try (ResultSet resultSet = statement.executeQuery()) {
                return readOptions(resultSet);
            }
        }
    }

    public List<CatalogOption> findPetStatuses() throws SQLException {
        return findSimpleOptions("SELECT id, status AS label FROM petStatus ORDER BY status");
    }

    public List<CatalogOption> findColors() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM color ORDER BY name");
    }

    public List<CatalogOption> findPetSizes() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM petSize ORDER BY name");
    }

    public List<CatalogOption> findCurrencies() throws SQLException {
        return findSimpleOptions("SELECT id, acronym || ' - ' || name AS label FROM currency ORDER BY acronym");
    }

    public List<CatalogOption> findTrainingEases() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM trainingEase ORDER BY name");
    }

    public List<CatalogOption> findProvinces() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM province ORDER BY name");
    }

    public List<CatalogOption> findCantons(long provinceId) throws SQLException {
        return findChildOptions("SELECT id, name AS label FROM canton WHERE idProvince = ? ORDER BY name", provinceId);
    }

    public List<CatalogOption> findDistricts(long cantonId) throws SQLException {
        return findChildOptions("SELECT id, name AS label FROM district WHERE idCanton = ? ORDER BY name", cantonId);
    }

    private List<CatalogOption> findSimpleOptions(String sql) throws SQLException {
        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return readOptions(resultSet);
        }
    }

    private List<CatalogOption> findChildOptions(String sql, long parentId) throws SQLException {
        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, parentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return readOptions(resultSet);
            }
        }
    }

    private List<CatalogOption> readOptions(ResultSet resultSet) throws SQLException {
        List<CatalogOption> options = new ArrayList<>();
        while (resultSet.next()) {
            options.add(new CatalogOption(resultSet.getLong("id"), resultSet.getString("label")));
        }
        return options;
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
}
