package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.ParameterRecord;

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

    public static final String PET_TYPES = "Tipos de mascota";
    public static final String BREEDS = "Razas";
    public static final String PET_STATUSES = "Estados de mascota";
    public static final String COLORS = "Colores";
    public static final String PET_SIZES = "Tamanos";
    public static final String TRAINING_EASES = "Facilidad de entrenamiento";
    public static final String DISEASES = "Enfermedades";
    public static final String TREATMENTS = "Tratamientos";
    public static final String MEDICINES = "Medicamentos";
    public static final String VETERINARIANS = "Veterinarios";
    public static final String CURRENCIES = "Monedas";
    public static final String FOOD_DONATION = "Donacion de alimento";
    public static final String SYSTEM_PARAMETERS = "Parametros del sistema";

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
                    SELECT b.id, b.name, NVL(pt.name, 'Sin tipo') AS extra
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
            return queryRecords("SELECT id, name, value AS extra FROM sysParameter ORDER BY name");
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
            throw new IllegalArgumentException("Seleccione un catalogo simple valido.");
        }
        requireValue(name, "Digite el nombre.");
        validateLength(name, catalog.maxLength(), "El nombre supera el maximo permitido.");

        try (Connection connection = ConexionBD.conectar()) {
            if (id == null) {
                long newId = nextSequenceValue(connection, catalog.sequenceName());
                String sql = "INSERT INTO " + catalog.tableName() + "(id, " + catalog.nameColumn() + ") VALUES (?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setLong(1, newId);
                    statement.setString(2, name.trim());
                    statement.executeUpdate();
                }
                auditRepository.log(connection, "Parametros", "Crear", category, name);
                return;
            }

            String sql = "UPDATE " + catalog.tableName() + " SET " + catalog.nameColumn() + " = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name.trim());
                statement.setLong(2, id);
                statement.executeUpdate();
            }
            auditRepository.log(connection, "Parametros", "Editar", category, name);
        }
    }

    /**
     * Saves a breed and the pet type it belongs to.
     */
    public void saveBreed(Long id, String name, Long petTypeId) throws SQLException {
        requireValue(name, "Digite el nombre de la raza.");
        validateLength(name, 25, "La raza no puede superar 25 caracteres.");
        if (petTypeId == null) {
            throw new IllegalArgumentException("Seleccione el tipo de mascota de la raza.");
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
                auditRepository.log(connection, "Parametros", "Raza", "-", name);
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
            auditRepository.log(connection, "Parametros", "Raza", "id:" + id, name);
        }
    }

    public void saveCurrency(Long id, String name, String acronym) throws SQLException {
        requireValue(name, "Digite el nombre de la moneda.");
        requireValue(acronym, "Digite el acronimo.");
        validateLength(name, 25, "El nombre de la moneda no puede superar 25 caracteres.");
        validateLength(acronym, 3, "El acronimo no puede superar 3 caracteres.");

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
                auditRepository.log(connection, "Parametros", "Moneda", "-", acronym);
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
            auditRepository.log(connection, "Parametros", "Moneda", "id:" + id, acronym);
        }
    }

    public void saveSystemParameter(Long id, String name, String description, String value) throws SQLException {
        requireValue(name, "Digite el nombre del parametro.");
        requireValue(value, "Digite el valor.");
        validateLength(name, 20, "El nombre del parametro no puede superar 20 caracteres.");
        validateLength(description, 50, "La descripcion no puede superar 50 caracteres.");
        validateLength(value, 255, "El valor no puede superar 255 caracteres.");

        try (Connection connection = ConexionBD.conectar()) {
            if (id == null) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO sysParameter(id, name, description, value) VALUES (?, ?, ?, ?)"
                )) {
                    statement.setLong(1, nextSequenceValue(connection, "sSysParameter"));
                    statement.setString(2, name.trim());
                    statement.setString(3, emptyToNull(description));
                    statement.setString(4, value.trim());
                    statement.executeUpdate();
                }
                auditRepository.log(connection, "Parametros", "Sistema", "-", name);
                return;
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE sysParameter SET name = ?, description = ?, value = ? WHERE id = ?"
            )) {
                statement.setString(1, name.trim());
                statement.setString(2, emptyToNull(description));
                statement.setString(3, value.trim());
                statement.setLong(4, id);
                statement.executeUpdate();
            }
            auditRepository.log(connection, "Parametros", "Sistema", "id:" + id, name);
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
                        valueOrEmpty(resultSet.getString("extra"))
                ));
            }
            return records;
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

    private record SimpleCatalog(String tableName, String sequenceName, String nameColumn, int maxLength) {
    }
}
