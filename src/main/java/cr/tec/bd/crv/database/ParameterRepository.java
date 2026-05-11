package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.ParameterRecord;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Saves and reads admin-maintained catalog values.
 *
 * <p>Many screens use the same small lookup tables. This repository gives admins
 * one place to add or edit those values without writing SQL by hand.</p>
 */
public class ParameterRepository {
    private static final String MATCH_JOB_FREQUENCY = "matchJobfreq";


    public static final String PET_TYPES = "Pet types";
    public static final String BREEDS = "Breeds";
    public static final String PET_STATUSES = "Pet statuses";
    public static final String COLORS = "Colors";
    public static final String PET_SIZES = "Sizes";
    public static final String TRAINING_EASES = "Training ease";
    public static final String DISEASES = "Diseases";
    public static final String TREATMENTS = "Treatments";
    public static final String MEDICINES = "Medicines";
    public static final String VETERINARIANS = "Veterinarians";
    public static final String CURRENCIES = "Currencies";
    public static final String FOOD_DONATION = "Food donation";
    public static final String SYSTEM_PARAMETERS = "System parameters";

    private final ApplicationAuditRepository auditRepository = new ApplicationAuditRepository();

    private final Map<String, SimpleCatalog> simpleCatalogs = Map.ofEntries(
            Map.entry(PET_TYPES, new SimpleCatalog("petType", "sPetType", "name", 25)),
            Map.entry(PET_STATUSES, new SimpleCatalog("petStatus", "sPetStatus", "status", 25)),
            Map.entry(COLORS, new SimpleCatalog("color", "sColor", "name", 25)),
            Map.entry(PET_SIZES, new SimpleCatalog("petSize", "sPetSize", "name", 25)),
            Map.entry(TRAINING_EASES, new SimpleCatalog("trainingEase", "sTrainingEase", "name", 25)),
            Map.entry(DISEASES, new SimpleCatalog("disease", "sDisease", "name", 25)),
            Map.entry(TREATMENTS, new SimpleCatalog("treatment", "sTreatment", "name", 25)),
            Map.entry(MEDICINES, new SimpleCatalog("medicine", "sMedicine", "name", 25)),
            Map.entry(VETERINARIANS, new SimpleCatalog("veterinarian", "sVeterinarian", "name", 25)),
            Map.entry(FOOD_DONATION, new SimpleCatalog("requiresFoodDonation", "sRequiresFoodDonation", "name", 20))
    );

    /**
     * Returns the catalog groups supported by the parameter screen.
     */
    public List<String> findCategories() {
        return List.of(
                PET_TYPES,
                BREEDS,
                PET_STATUSES,
                COLORS,
                PET_SIZES,
                TRAINING_EASES,
                DISEASES,
                TREATMENTS,
                MEDICINES,
                VETERINARIANS,
                CURRENCIES,
                FOOD_DONATION,
                SYSTEM_PARAMETERS
        );
    }

    /**
     * Reads records for the selected catalog category.
     */
    public List<ParameterRecord> findRecords(String category) throws SQLException {
        if (BREEDS.equals(category)) {
            return queryRecords("""
                    SELECT b.id, b.name, NVL(pt.name, 'No type') AS extra
                    FROM breed b
                    LEFT JOIN petType pt
                        ON pt.id = b.idPetType
                    ORDER BY b.name
                    """);
        }

        if (CURRENCIES.equals(category)) {
            return queryRecords("SELECT id, name, acronym AS extra FROM currency ORDER BY acronym, name");
        }

        if (SYSTEM_PARAMETERS.equals(category)) {
            return queryRecords("SELECT id, name, value AS extra, description FROM sysParameter ORDER BY name");
        }

        SimpleCatalog catalog = simpleCatalogs.get(category);
        if (catalog == null) {
            return List.of();
        }
        return queryRecords("SELECT id, " + catalog.nameColumn() + " AS name, NULL AS extra FROM "
                + catalog.tableName() + " ORDER BY " + catalog.nameColumn());
    }

    /**
     * Saves a catalog that only has id and name/status columns.
     */
    public void saveSimple(String category, Long id, String name) throws SQLException {
        SimpleCatalog catalog = simpleCatalogs.get(category);
        if (catalog == null) {
            throw new IllegalArgumentException("Select a valid simple catalog.");
        }
        requireValue(name, "Enter the name.");
        validateLength(name, catalog.maxLength(), "Name exceeds the maximum length.");

        try (Connection connection = ConexionBD.conectar()) {
            if (id == null) {
                long newId = nextSequenceValue(connection, catalog.sequenceName());
                String sql = "INSERT INTO " + catalog.tableName() + "(id, " + catalog.nameColumn() + ") VALUES (?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setLong(1, newId);
                    statement.setString(2, name.trim());
                    statement.executeUpdate();
                }
                auditRepository.log(connection, "Parameters", "Create", category, name);
                return;
            }

            String sql = "UPDATE " + catalog.tableName() + " SET " + catalog.nameColumn() + " = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name.trim());
                statement.setLong(2, id);
                statement.executeUpdate();
            }
            auditRepository.log(connection, "Parameters", "Edit", category, name);
        }
    }

    /**
     * Saves a breed and the pet type it belongs to.
     */
    public void saveBreed(Long id, String name, Long petTypeId) throws SQLException {
        requireValue(name, "Enter the breed name.");
        validateLength(name, 25, "Breed name cannot exceed 25 characters.");
        if (petTypeId == null) {
            throw new IllegalArgumentException("Select the pet type for the breed.");
        }

        try (Connection connection = ConexionBD.conectar()) {
            if (id == null) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO breed(id, name, idPetType) VALUES (?, ?, ?)"
                )) {
                    statement.setLong(1, nextSequenceValue(connection, "sBreed"));
                    statement.setString(2, name.trim());
                    statement.setLong(3, petTypeId);
                    statement.executeUpdate();
                }
                auditRepository.log(connection, "Parameters", "Breed", "-", name);
                return;
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE breed SET name = ?, idPetType = ? WHERE id = ?"
            )) {
                statement.setString(1, name.trim());
                statement.setLong(2, petTypeId);
                statement.setLong(3, id);
                statement.executeUpdate();
            }
            auditRepository.log(connection, "Parameters", "Breed", "id:" + id, name);
        }
    }

    public void saveCurrency(Long id, String name, String acronym) throws SQLException {
        requireValue(name, "Enter the currency name.");
        requireValue(acronym, "Enter the acronym.");
        validateLength(name, 25, "Currency name cannot exceed 25 characters.");
        validateLength(acronym, 3, "Acronym cannot exceed 3 characters.");

        try (Connection connection = ConexionBD.conectar()) {
            if (id == null) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO currency(id, name, acronym) VALUES (?, ?, ?)"
                )) {
                    statement.setLong(1, nextSequenceValue(connection, "sCurrency"));
                    statement.setString(2, name.trim());
                    statement.setString(3, acronym.trim().toUpperCase());
                    statement.executeUpdate();
                }
                auditRepository.log(connection, "Parameters", "Currency", "-", acronym);
                return;
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE currency SET name = ?, acronym = ? WHERE id = ?"
            )) {
                statement.setString(1, name.trim());
                statement.setString(2, acronym.trim().toUpperCase());
                statement.setLong(3, id);
                statement.executeUpdate();
            }
            auditRepository.log(connection, "Parameters", "Currency", "id:" + id, acronym);
        }
    }

    public void saveSystemParameter(Long id, String name, String description, String value) throws SQLException {
        requireValue(name, "Enter the parameter name.");
        requireValue(value, "Enter the value.");
        validateLength(name, 20, "Parameter name cannot exceed 20 characters.");
        validateLength(description, 50, "Description cannot exceed 50 characters.");
        validateLength(value, 255, "Value cannot exceed 255 characters.");
        validateSystemParameterValue(name, value);

        String normalizedName = name.trim();
        String normalizedValue = value.trim();

        try (Connection connection = ConexionBD.conectar()) {
            if (id == null) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO sysParameter(id, name, description, value) VALUES (?, ?, ?, ?)"
                )) {
                    statement.setLong(1, nextSequenceValue(connection, "sSysParameter"));
                    statement.setString(2, normalizedName);
                    statement.setString(3, emptyToNull(description));
                    statement.setString(4, normalizedValue);
                    statement.executeUpdate();
                }
                updateMatchSchedulerIfNeeded(connection, normalizedName);
                auditRepository.log(connection, "Parameters", "System", "-", name);
                return;
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE sysParameter SET name = ?, description = ?, value = ? WHERE id = ?"
            )) {
                statement.setString(1, normalizedName);
                statement.setString(2, emptyToNull(description));
                statement.setString(3, normalizedValue);
                statement.setLong(4, id);
                statement.executeUpdate();
            }
            updateMatchSchedulerIfNeeded(connection, normalizedName);
            auditRepository.log(connection, "Parameters", "System", "id:" + id, name);
        }
    }

    private List<ParameterRecord> queryRecords(String sql) throws SQLException {
        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<ParameterRecord> records = new ArrayList<>();
            while (resultSet.next()) {
                records.add(new ParameterRecord(
                        resultSet.getLong("id"),
                        valueOrEmpty(resultSet.getString("name")),
                        valueOrEmpty(resultSet.getString("extra")),
                        safeGet(resultSet, "description")
                ));
            }
            return records;
        }
    }

    private void validateSystemParameterValue(String name, String value) {
        if (!MATCH_JOB_FREQUENCY.equalsIgnoreCase(name.trim())) {
            return;
        }

        try {
            int hours = Integer.parseInt(value.trim());
            if (hours <= 0) {
                throw new IllegalArgumentException("Match job frequency must be a whole number greater than 0.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Match job frequency must be a whole number of hours.");
        }
    }

    private void updateMatchSchedulerIfNeeded(Connection connection, String parameterName) throws SQLException {
        if (!MATCH_JOB_FREQUENCY.equalsIgnoreCase(parameterName)) {
            return;
        }

        try (CallableStatement statement = connection.prepareCall("{ call updateJobFreq }")) {
            statement.execute();
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
        if (emptyToNull(value) == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateLength(String value, int maxLength, String message) {
        if (value != null && value.trim().length() > maxLength) {
            throw new IllegalArgumentException(message);
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

    private String safeGet(ResultSet resultSet, String columnName) throws SQLException {
        try {
            return valueOrEmpty(resultSet.getString(columnName));
        } catch (SQLException e) {
            return "";
        }
    }

    private record SimpleCatalog(String tableName, String sequenceName, String nameColumn, int maxLength) {
    }
}
