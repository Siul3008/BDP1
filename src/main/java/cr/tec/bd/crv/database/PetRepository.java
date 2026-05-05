package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.Mascota;
import cr.tec.bd.crv.model.PetFormData;
import cr.tec.bd.crv.model.PetSearchCriteria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Data access class for pet publications.
 */
public class PetRepository {

    public List<String> savePet(PetFormData data, Long publisherPersonId) throws SQLException {
        validatePet(data);

        try (Connection connection = ConexionBD.conectar()) {
            connection.setAutoCommit(false);

            try {
                Set<String> petColumns = findColumnNames(connection, "PET");
                Map<String, Integer> petColumnSizes = findColumnSizes(connection, "PET");
                List<String> warnings = new ArrayList<>();

                Long photoId = insertPetPhoto(connection, firstValue(data.getPhotoBeforePath(), data.getPhotoAfterPath()));
                long contactId = insertPetContact(connection);
                insertOptionalPetContactEmail(connection, contactId, data.getContactEmail());
                insertOptionalPetContactPhone(connection, contactId, data.getContactPhone());

                long petId = nextSequenceValue(connection, "sPet");
                insertPet(connection, petId, data, photoId, contactId, petColumns, petColumnSizes, warnings);
                insertOptionalReward(connection, petId, data.getCurrencyId(), data.getRewardAmount());

                if (publisherPersonId != null) {
                    ensureRescuerProfile(connection, publisherPersonId);
                    linkRescuerToPet(connection, publisherPersonId, petId);
                }

                connection.commit();
                return warnings;
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<Mascota> findPets(PetSearchCriteria criteria) throws SQLException {
        try (Connection connection = ConexionBD.conectar()) {
            Set<String> petColumns = findColumnNames(connection, "PET");
            boolean hasChip = petColumns.contains("CHIP");
            boolean hasEventDate = petColumns.contains("EVENTDATE");
            boolean hasCreationDate = petColumns.contains("CREATION_DATE");

            List<Object> parameters = new ArrayList<>();
            String sql = buildPetSearchSql(criteria, hasChip, hasEventDate, hasCreationDate, parameters);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                setParameters(statement, parameters);

                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Mascota> pets = new ArrayList<>();
                    while (resultSet.next()) {
                        pets.add(new Mascota(
                                resultSet.getInt("id"),
                                valueOrEmpty(resultSet.getString("name")),
                                valueOrEmpty(resultSet.getString("typeName")),
                                valueOrEmpty(resultSet.getString("breedName")),
                                valueOrEmpty(resultSet.getString("color")),
                                valueOrEmpty(resultSet.getString("statusName")),
                                valueOrEmpty(resultSet.getString("locationName")),
                                valueOrEmpty(resultSet.getString("eventDateText"))
                        ));
                    }
                    return pets;
                }
            }
        }
    }

    private String buildPetSearchSql(
            PetSearchCriteria criteria,
            boolean hasChip,
            boolean hasEventDate,
            boolean hasCreationDate,
            List<Object> parameters
    ) {
        String dateExpression = null;
        if (hasEventDate) {
            dateExpression = "p.eventDate";
        } else if (hasCreationDate) {
            dateExpression = "p.creation_Date";
        }
        String eventDateSelect = dateExpression == null
                ? "CAST(NULL AS VARCHAR2(10))"
                : "TO_CHAR(" + dateExpression + ", 'YYYY-MM-DD')";

        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.id,
                    p.name,
                    pt.name AS typeName,
                    b.name AS breedName,
                    co.name AS color,
                    ps.status AS statusName,
                    TRIM(
                        NVL(d.name, '') ||
                        CASE WHEN c.name IS NOT NULL THEN ', ' || c.name ELSE '' END ||
                        CASE WHEN pr.name IS NOT NULL THEN ', ' || pr.name ELSE '' END
                    ) AS locationName,
                    """);
        sql.append(eventDateSelect).append(" AS eventDateText\n");
        sql.append("""
                FROM pet p
                LEFT JOIN petType pt ON p.idPetType = pt.id
                LEFT JOIN breed b ON p.idBreed = b.id
                LEFT JOIN petStatus ps ON p.idPetStatus = ps.id
                LEFT JOIN color co ON p.idColor = co.id
                LEFT JOIN petSize sz ON p.idSize = sz.id
                LEFT JOIN district d ON p.idLocation = d.id
                LEFT JOIN canton c ON d.idCanton = c.id
                LEFT JOIN province pr ON c.idProvince = pr.id
                WHERE 1 = 1
                """);

        if (criteria != null && criteria.getStatusId() != null) {
            sql.append(" AND p.idPetStatus = ?\n");
            parameters.add(criteria.getStatusId());
        }

        if (criteria != null && dateExpression != null && criteria.getFromDate() != null) {
            sql.append(" AND ").append(dateExpression).append(" >= ?\n");
            parameters.add(Date.valueOf(criteria.getFromDate()));
        }

        if (criteria != null && dateExpression != null && criteria.getToDate() != null) {
            sql.append(" AND ").append(dateExpression).append(" <= ?\n");
            parameters.add(Date.valueOf(criteria.getToDate()));
        }

        String text = criteria == null ? null : emptyToNull(criteria.getText());
        if (text != null) {
            List<String> predicates = new ArrayList<>();
            predicates.add("LOWER(p.name) LIKE ?");
            predicates.add("LOWER(NVL(pt.name, '')) LIKE ?");
            predicates.add("LOWER(NVL(b.name, '')) LIKE ?");
            predicates.add("LOWER(NVL(co.name, '')) LIKE ?");
            predicates.add("LOWER(NVL(sz.name, '')) LIKE ?");
            predicates.add("LOWER(NVL(ps.status, '')) LIKE ?");
            predicates.add("LOWER(NVL(d.name, '')) LIKE ?");
            predicates.add("LOWER(NVL(c.name, '')) LIKE ?");
            predicates.add("LOWER(NVL(pr.name, '')) LIKE ?");
            predicates.add("LOWER(NVL(p.description, '')) LIKE ?");

            if (hasChip) {
                predicates.add("LOWER(NVL(p.chip, '')) LIKE ?");
            }

            sql.append(" AND (").append(String.join(" OR ", predicates)).append(")\n");
            String searchValue = "%" + text.toLowerCase(Locale.ROOT) + "%";
            for (int index = 0; index < predicates.size(); index++) {
                parameters.add(searchValue);
            }
        }

        if (dateExpression != null) {
            sql.append(" ORDER BY ").append(dateExpression).append(" DESC NULLS LAST, p.id DESC");
        } else {
            sql.append(" ORDER BY p.id DESC");
        }

        return sql.toString();
    }

    private void insertPet(
            Connection connection,
            long petId,
            PetFormData data,
            Long photoId,
            long contactId,
            Set<String> petColumns,
            Map<String, Integer> petColumnSizes,
            List<String> warnings
    ) throws SQLException {
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        addColumn(columns, values, "id", petId);
        addColumn(columns, values, "idPetType", data.getPetTypeId());
        addColumn(columns, values, "idBreed", data.getBreedId());
        addColumn(columns, values, "idPetPhoto", photoId);
        addColumn(columns, values, "idPetContact", contactId);
        addColumn(columns, values, "idPetStatus", data.getPetStatusId());
        addColumn(columns, values, "idTrainingEase", data.getTrainingEaseId());
        addColumn(columns, values, "idLocation", data.getDistrictId());
        addColumn(columns, values, "name", fitToColumn(data.getName(), petColumnSizes, "NAME"));
        addColumn(columns, values, "description", fitToColumn(data.getDescription(), petColumnSizes, "DESCRIPTION"));
        addColumn(columns, values, "needSpace", fitToColumn(data.getNeedSpace(), petColumnSizes, "NEEDSPACE"));
        addColumn(columns, values, "energyLevel", fitToColumn(data.getEnergyLevel(), petColumnSizes, "ENERGYLEVEL"));
        addColumn(columns, values, "idColor", data.getColorId());
        addColumn(columns, values, "idSize", data.getPetSizeId());

        addOptionalPetColumn(columns, values, petColumns, petColumnSizes, "CHIP", "chip", data.getChip(), warnings);
        if (petColumns.contains("EVENTDATE")) {
            addColumn(columns, values, "eventDate", data.getEventDate() == null ? null : Date.valueOf(data.getEventDate()));
        } else if (data.getEventDate() != null) {
            warnings.add("La fecha no se guardo porque falta la columna pet.eventDate.");
        }

        StringJoiner columnJoiner = new StringJoiner(", ");
        StringJoiner placeholderJoiner = new StringJoiner(", ");
        for (String column : columns) {
            columnJoiner.add(column);
            placeholderJoiner.add("?");
        }

        String sql = "INSERT INTO pet(" + columnJoiner + ") VALUES (" + placeholderJoiner + ")";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, values);
            statement.executeUpdate();
        }
    }

    private void addOptionalPetColumn(
            List<String> columns,
            List<Object> values,
            Set<String> petColumns,
            Map<String, Integer> petColumnSizes,
            String metadataName,
            String sqlName,
            String value,
            List<String> warnings
    ) {
        if (petColumns.contains(metadataName)) {
            addColumn(columns, values, sqlName, fitToColumn(value, petColumnSizes, metadataName));
            return;
        }

        if (emptyToNull(value) != null) {
            warnings.add("No se guardo " + sqlName + " porque falta la columna pet." + sqlName + ".");
        }
    }

    private Long insertPetPhoto(Connection connection, String photoPath) throws SQLException {
        String normalizedPath = emptyToNull(photoPath);
        if (normalizedPath == null) {
            return null;
        }

        long photoId = nextSequenceValue(connection, "sPetPhoto");
        String sql = "INSERT INTO petPhoto(id, photoPath) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, photoId);
            statement.setString(2, normalizedPath);
            statement.executeUpdate();
            return photoId;
        }
    }

    private long insertPetContact(Connection connection) throws SQLException {
        long contactId = nextSequenceValue(connection, "sPetContact");
        String sql = "INSERT INTO petContact(id) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, contactId);
            statement.executeUpdate();
            return contactId;
        }
    }

    private void insertOptionalPetContactEmail(Connection connection, long contactId, String email) throws SQLException {
        String normalizedEmail = emptyToNull(email);
        if (normalizedEmail == null) {
            return;
        }

        long emailId = nextSequenceValue(connection, "sEmail");
        try (PreparedStatement insertEmail = connection.prepareStatement("INSERT INTO email(id, emailAddress) VALUES (?, ?)");
             PreparedStatement linkEmail = connection.prepareStatement(
                     "INSERT INTO petContactxEmail(idPetContact, idEmail) VALUES (?, ?)"
             )) {
            insertEmail.setLong(1, emailId);
            insertEmail.setString(2, normalizedEmail);
            insertEmail.executeUpdate();

            linkEmail.setLong(1, contactId);
            linkEmail.setLong(2, emailId);
            linkEmail.executeUpdate();
        }
    }

    private void insertOptionalPetContactPhone(Connection connection, long contactId, String phone) throws SQLException {
        String normalizedPhone = emptyToNull(phone);
        if (normalizedPhone == null) {
            return;
        }

        long phoneId = nextSequenceValue(connection, "sPhone");
        try (PreparedStatement insertPhone = connection.prepareStatement("INSERT INTO phone(id, phoneNumber) VALUES (?, ?)");
             PreparedStatement linkPhone = connection.prepareStatement(
                     "INSERT INTO petContactxPhone(idPetContact, idPhone) VALUES (?, ?)"
             )) {
            insertPhone.setLong(1, phoneId);
            insertPhone.setString(2, normalizedPhone);
            insertPhone.executeUpdate();

            linkPhone.setLong(1, contactId);
            linkPhone.setLong(2, phoneId);
            linkPhone.executeUpdate();
        }
    }

    private void insertOptionalReward(Connection connection, long petId, Long currencyId, BigDecimal amount) throws SQLException {
        if (currencyId == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        long rewardId = nextSequenceValue(connection, "sReward");
        String sql = "INSERT INTO reward(id, idPet, idCurrency, amount) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, rewardId);
            statement.setLong(2, petId);
            statement.setLong(3, currencyId);
            statement.setBigDecimal(4, amount);
            statement.executeUpdate();
        }
    }

    private void ensureRescuerProfile(Connection connection, long personId) throws SQLException {
        String sql = """
                MERGE INTO rescuer target
                USING (SELECT ? AS idPerson FROM dual) source
                ON (target.idPerson = source.idPerson)
                WHEN NOT MATCHED THEN
                    INSERT (idPerson) VALUES (source.idPerson)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);
            statement.executeUpdate();
        }
    }

    private void linkRescuerToPet(Connection connection, long personId, long petId) throws SQLException {
        String sql = """
                MERGE INTO rescuerxPet target
                USING (SELECT ? AS idRescuer, ? AS idPet FROM dual) source
                ON (target.idRescuer = source.idRescuer AND target.idPet = source.idPet)
                WHEN NOT MATCHED THEN
                    INSERT (idRescuer, idPet) VALUES (source.idRescuer, source.idPet)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);
            statement.setLong(2, petId);
            statement.executeUpdate();
        }
    }

    private void validatePet(PetFormData data) {
        requireValue(data.getName(), "El nombre de la mascota es obligatorio.");
        requireSelected(data.getPetTypeId(), "Seleccione el tipo de mascota.");
        requireSelected(data.getBreedId(), "Seleccione la raza.");
        requireSelected(data.getPetStatusId(), "Seleccione el estado.");
        requireSelected(data.getColorId(), "Seleccione el color.");
        requireSelected(data.getPetSizeId(), "Seleccione el tamano.");
        requireSelected(data.getDistrictId(), "Seleccione provincia, canton y distrito.");
        if (data.getEventDate() == null) {
            throw new IllegalArgumentException("Seleccione la fecha del evento o publicacion.");
        }

        if (emptyToNull(data.getContactEmail()) == null && emptyToNull(data.getContactPhone()) == null) {
            throw new IllegalArgumentException("Indique al menos un correo o telefono de contacto.");
        }

        String phone = emptyToNull(data.getContactPhone());
        if (phone != null && !phone.matches("\\d{8}")) {
            throw new IllegalArgumentException("El telefono de contacto debe tener 8 digitos.");
        }

        String email = emptyToNull(data.getContactEmail());
        if (email != null && !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("El correo de contacto no tiene un formato valido.");
        }

        if (data.getRewardAmount() != null
                && data.getRewardAmount().compareTo(BigDecimal.ZERO) > 0
                && data.getCurrencyId() == null) {
            throw new IllegalArgumentException("Seleccione la moneda de la recompensa.");
        }
    }

    private void requireValue(String value, String message) {
        if (emptyToNull(value) == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireSelected(Long value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
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

    private Set<String> findColumnNames(Connection connection, String tableName) throws SQLException {
        Set<String> columns = new HashSet<>();
        DatabaseMetaData metaData = connection.getMetaData();

        try (ResultSet resultSet = metaData.getColumns(null, null, tableName.toUpperCase(Locale.ROOT), null)) {
            while (resultSet.next()) {
                columns.add(resultSet.getString("COLUMN_NAME").toUpperCase(Locale.ROOT));
            }
        }
        return columns;
    }

    private Map<String, Integer> findColumnSizes(Connection connection, String tableName) throws SQLException {
        Map<String, Integer> columns = new HashMap<>();
        DatabaseMetaData metaData = connection.getMetaData();

        try (ResultSet resultSet = metaData.getColumns(null, null, tableName.toUpperCase(Locale.ROOT), null)) {
            while (resultSet.next()) {
                columns.put(
                        resultSet.getString("COLUMN_NAME").toUpperCase(Locale.ROOT),
                        resultSet.getInt("COLUMN_SIZE")
                );
            }
        }
        return columns;
    }

    private void addColumn(List<String> columns, List<Object> values, String column, Object value) {
        columns.add(column);
        values.add(value);
    }

    private void setParameters(PreparedStatement statement, List<Object> values) throws SQLException {
        for (int index = 0; index < values.size(); index++) {
            Object value = values.get(index);
            if (value instanceof Date date) {
                statement.setDate(index + 1, date);
            } else if (value instanceof BigDecimal decimal) {
                statement.setBigDecimal(index + 1, decimal);
            } else {
                statement.setObject(index + 1, value);
            }
        }
    }

    private String fitToColumn(String value, Map<String, Integer> columnSizes, String columnName) {
        String normalizedValue = emptyToNull(value);
        if (normalizedValue == null) {
            return null;
        }

        Integer size = columnSizes.get(columnName);
        if (size != null && size > 0 && normalizedValue.length() > size) {
            return normalizedValue.substring(0, size);
        }
        return normalizedValue;
    }

    private String firstValue(String first, String second) {
        String normalizedFirst = emptyToNull(first);
        if (normalizedFirst != null) {
            return normalizedFirst;
        }
        return emptyToNull(second);
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
