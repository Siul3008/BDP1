package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.CatalogOption;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Reads catalog values used by combo boxes and filters.
 *
 * <p>Catalog tables hold controlled options such as pet types, breeds, colors,
 * currencies, provinces, and health-related values. Loading them from the
 * database prevents users from typing values that do not exist in the schema.</p>
 */
public class CatalogRepository {

    /**
     * Returns pet types such as dog, cat, bird, and other supported animal groups.
     */
    public List<CatalogOption> findPetTypes() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM petType ORDER BY name");
    }

    /**
     * Returns every breed, regardless of pet type.
     */
    public List<CatalogOption> findBreeds() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM breed ORDER BY name");
    }

    /**
     * Returns only breeds linked to the selected pet type when the schema supports it.
     */
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

    /**
     * Returns pet statuses used for publication state changes.
     */
    public List<CatalogOption> findPetStatuses() throws SQLException {
        return findSimpleOptions("SELECT id, status AS label FROM petStatus ORDER BY status");
    }

    public List<CatalogOption> findColors() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM color ORDER BY name");
    }

    public List<CatalogOption> findPetSizes() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM petSize ORDER BY name");
    }

    public List<CatalogOption> findFoodDonationOptions() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM requiresFoodDonation ORDER BY id");
    }

    public List<CatalogOption> findCurrencies() throws SQLException {
        return findSimpleOptions("SELECT id, acronym || ' - ' || name AS label FROM currency ORDER BY acronym");
    }

    public List<CatalogOption> findAssociations() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM association ORDER BY name");
    }

    public List<CatalogOption> findTrainingEases() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM trainingEase ORDER BY name");
    }

    public List<CatalogOption> findDiseases() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM disease ORDER BY name");
    }

    public List<CatalogOption> findTreatments() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM treatment ORDER BY name");
    }

    public List<CatalogOption> findMedicines() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM medicine ORDER BY name");
    }

    public List<CatalogOption> findVeterinarians() throws SQLException {
        return findSimpleOptions("SELECT id, name AS label FROM veterinarian ORDER BY name");
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

    /**
     * Returns energy labels maintained as system parameters.
     */
    public List<String> findEnergyLevels() throws SQLException {
        String sql = """
                SELECT value
                FROM sysParameter
                WHERE LOWER(name) LIKE 'pet.energy.%'
                ORDER BY name
                """;

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<String> levels = new ArrayList<>();
            while (resultSet.next()) {
                String value = resultSet.getString("value");
                if (value != null && !value.trim().isEmpty()) {
                    levels.add(value.trim());
                }
            }
            return levels;
        }
    }

    /**
     * Returns system parameters by prefix, keeping the names so callers can map
     * each value to the right label or prompt.
     */
    public Map<String, String> findSystemParametersByPrefix(String prefix) throws SQLException {
        String sql = """
                SELECT name, value
                FROM sysParameter
                WHERE LOWER(name) LIKE LOWER(?)
                ORDER BY name
                """;

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, prefix + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                Map<String, String> values = new LinkedHashMap<>();
                while (resultSet.next()) {
                    values.put(resultSet.getString("name"), resultSet.getString("value"));
                }
                return values;
            }
        }
    }

    /**
     * Finds the canton that owns a district. This lets edit forms rebuild the
     * province/canton/district selector chain from the saved district id.
     */
    public Long findCantonIdByDistrict(long districtId) throws SQLException {
        return findParentId("SELECT idCanton FROM district WHERE id = ?", districtId);
    }

    /**
     * Finds the province that owns a canton.
     */
    public Long findProvinceIdByCanton(long cantonId) throws SQLException {
        return findParentId("SELECT idProvince FROM canton WHERE id = ?", cantonId);
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

    private Long findParentId(String sql, long childId) throws SQLException {
        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, childId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }

                long value = resultSet.getLong(1);
                return resultSet.wasNull() ? null : value;
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
