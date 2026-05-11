package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.Mascota;
import cr.tec.bd.crv.model.PetFormData;
import cr.tec.bd.crv.model.PetSearchCriteria;
import cr.tec.bd.crv.util.PhotoStorageUtil;
import cr.tec.bd.crv.util.PhotoStorageUtil.StoredPhoto;

import java.math.BigDecimal;
import java.sql.Blob;
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
 * Handles pet publication data.
 *
 * <p>This is one of the central repositories of the application. It saves new
 * pet posts, edits existing posts, searches pets, changes status, transfers
 * control to foster homes, and stores optional health/veterinarian information.</p>
 */
public class PetRepository {

    private final ApplicationAuditRepository auditRepository = new ApplicationAuditRepository();
    public record PetPhotoPair(byte[] beforePhoto, byte[] afterPhoto) {
    }

    /**
     * Saves a new pet publication and links it to the user who published it.
     */
    public List<String> savePet(PetFormData data, Long publisherPersonId) throws SQLException {
        validatePet(data);

        try (Connection connection = ConexionBD.conectar()) {
            connection.setAutoCommit(false);

            try {
                // Schema versions can differ, so the repository checks real columns at runtime.
                Set<String> petColumns = findColumnNames(connection, "PET");
                Map<String, Integer> petColumnSizes = findColumnSizes(connection, "PET");
                List<String> warnings = new ArrayList<>();

                Long photoId = insertPetPhoto(connection, null, "BEFORE", data.getPhotoBeforePath());
                long contactId = insertPetContact(connection);
                insertOptionalPetContactEmail(connection, contactId, data.getContactEmail());
                insertOptionalPetContactPhone(connection, contactId, data.getContactPhone());

                long petId = nextSequenceValue(connection, "sPet");
                insertPet(connection, petId, data, photoId, contactId, petColumns, petColumnSizes, warnings);
                linkPhotoToPetIfPossible(connection, photoId, petId, "BEFORE");
                insertPetPhoto(connection, petId, "AFTER", data.getPhotoAfterPath());
                insertOptionalReward(connection, petId, data.getCurrencyId(), data.getRewardAmount());

                if (publisherPersonId != null) {
                    ensureRescuerProfile(connection, publisherPersonId);
                    linkRescuerToPet(connection, publisherPersonId, petId);
                }
                insertOptionalHealthAndVeterinarian(connection, petId, data);
                insertPetReportIfNeeded(connection, data);
                auditRepository.log(connection, "Pets", "Create", "-", data.getName());

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

    /**
     * Loads the editable fields for one pet so the registration screen can reuse the same form.
     */
    public PetFormData findPetForEdit(long petId) throws SQLException {
        try (Connection connection = ConexionBD.conectar()) {
            String sql = """
                    WITH latestReward AS (
                        SELECT idPet, MAX(id) AS idReward
                        FROM reward
                        GROUP BY idPet
                    ),
                    latestHealth AS (
                        SELECT idPet, MAX(idHealthStatus) AS idHealthStatus
                        FROM petxHealthStatus
                        GROUP BY idPet
                    )
                    SELECT
                        p.idPetType,
                        p.idBreed,
                        p.idPetStatus,
                        p.idTrainingEase,
                        p.idLocation,
                        p.idColor,
                        p.idSize,
                        p.name,
                        p.description,
                        p.needSpace,
                        p.energyLevel,
                        p.chip,
                        p.eventDate,
                        pp.photoPath AS beforePhotoPath,
                        afterPhoto.photoPath AS afterPhotoPath,
                        rw.idCurrency,
                        rw.amount,
                        hs.illnessState,
                        hs.description AS healthDescription,
                        (
                            SELECT MIN(e.emailAddress)
                            FROM petContactxEmail pce
                            JOIN email e ON e.id = pce.idEmail
                            WHERE pce.idPetContact = p.idPetContact
                        ) AS contactEmail,
                        (
                            SELECT MIN(ph.phoneNumber)
                            FROM petContactxPhone pcp
                            JOIN phone ph ON ph.id = pcp.idPhone
                            WHERE pcp.idPetContact = p.idPetContact
                        ) AS contactPhone,
                        (
                            SELECT MIN(hd.idDisease)
                            FROM healthStxDisease hd
                            WHERE hd.idHealthStatus = hs.id
                        ) AS idDisease,
                        (
                            SELECT MIN(ht.idTreatment)
                            FROM healthStxTreatment ht
                            WHERE ht.idHealthStatus = hs.id
                        ) AS idTreatment,
                        (
                            SELECT MIN(hm.idMedicine)
                            FROM healthStxMedicine hm
                            WHERE hm.idHealthStatus = hs.id
                        ) AS idMedicine,
                        (
                            SELECT MIN(hm.dose)
                            FROM healthStxMedicine hm
                            WHERE hm.idHealthStatus = hs.id
                        ) AS medicineDose,
                        (
                            SELECT MIN(pv.idVeterinarian)
                            FROM petxVeterinarian pv
                            WHERE pv.idPet = p.id
                        ) AS idVeterinarian
                    FROM pet p
                    LEFT JOIN petPhoto pp
                        ON pp.id = p.idPetPhoto
                    LEFT JOIN petPhoto afterPhoto
                        ON afterPhoto.idPet = p.id
                       AND UPPER(NVL(afterPhoto.photoType, '')) = 'AFTER'
                    LEFT JOIN latestReward lr
                        ON lr.idPet = p.id
                    LEFT JOIN reward rw
                        ON rw.id = lr.idReward
                    LEFT JOIN latestHealth lh
                        ON lh.idPet = p.id
                    LEFT JOIN healthStatus hs
                        ON hs.id = lh.idHealthStatus
                    WHERE p.id = ?
                    """;

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, petId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new IllegalArgumentException("The selected pet does not exist.");
                    }

                    Date eventDate = resultSet.getDate("eventDate");
                    return new PetFormData(
                            resultSet.getString("name"),
                            longOrNull(resultSet, "idPetType"),
                            longOrNull(resultSet, "idBreed"),
                            longOrNull(resultSet, "idPetStatus"),
                            longOrNull(resultSet, "idTrainingEase"),
                            longOrNull(resultSet, "idLocation"),
                            longOrNull(resultSet, "idCurrency"),
                            longOrNull(resultSet, "idColor"),
                            resultSet.getString("chip"),
                            longOrNull(resultSet, "idSize"),
                            resultSet.getString("needSpace"),
                            resultSet.getString("energyLevel"),
                            resultSet.getString("contactPhone"),
                            resultSet.getString("contactEmail"),
                            resultSet.getBigDecimal("amount"),
                            eventDate == null ? null : eventDate.toLocalDate(),
                            resultSet.getString("beforePhotoPath"),
                            resultSet.getString("afterPhotoPath"),
                            resultSet.getString("description"),
                            resultSet.getString("illnessState"),
                            resultSet.getString("healthDescription"),
                            longOrNull(resultSet, "idDisease"),
                            longOrNull(resultSet, "idTreatment"),
                            longOrNull(resultSet, "idMedicine"),
                            resultSet.getString("medicineDose"),
                            longOrNull(resultSet, "idVeterinarian"),
                            null
                    );
                }
            }
        }
    }

    /**
     * Updates a pet after checking that the current user is allowed to control it.
     */
    public List<String> updatePet(long petId, PetFormData data, Long currentPersonId, boolean admin) throws SQLException {
        validatePetUpdate(petId, data, currentPersonId, admin);

        try (Connection connection = ConexionBD.conectar()) {
            connection.setAutoCommit(false);

            try {
                ensurePetExists(connection, petId);
                if (!admin && !userControlsPet(connection, petId, currentPersonId)) {
                    throw new IllegalArgumentException("Only the publication controller can edit this pet.");
                }

                Set<String> petColumns = findColumnNames(connection, "PET");
                Map<String, Integer> petColumnSizes = findColumnSizes(connection, "PET");
                List<String> warnings = new ArrayList<>();

                updatePetCore(connection, petId, data, petColumns, petColumnSizes, warnings);
                updatePetPhoto(connection, petId, "BEFORE", data.getPhotoBeforePath(), true);
                updatePetPhoto(connection, petId, "AFTER", data.getPhotoAfterPath(), false);
                updatePetContact(connection, petId, data.getContactEmail(), data.getContactPhone());
                upsertReward(connection, petId, data.getCurrencyId(), data.getRewardAmount());
                insertOptionalHealthAndVeterinarian(connection, petId, data);
                insertPetReportIfNeeded(connection, data);
                auditRepository.log(connection, "Pets", "Edit", "pet:" + petId, data.getName());

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

    /**
     * Searches pets without restricting by owner.
     */
    public List<Mascota> findPets(PetSearchCriteria criteria) throws SQLException {
        return findPets(criteria, null);
    }

    /**
     * Searches pets and optionally limits the results to the pets controlled by one person.
     */
    public List<Mascota> findPets(PetSearchCriteria criteria, Long ownerPersonId) throws SQLException {
        try (Connection connection = ConexionBD.conectar()) {
            Set<String> petColumns = findColumnNames(connection, "PET");
            boolean hasChip = petColumns.contains("CHIP");
            boolean hasEventDate = petColumns.contains("EVENTDATE");
            boolean hasCreationDate = petColumns.contains("CREATION_DATE");

            List<Object> parameters = new ArrayList<>();
            String sql = buildPetSearchSql(criteria, hasChip, hasEventDate, hasCreationDate, ownerPersonId, parameters);

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

    /**
     * Returns the stored image bytes for a pet preview.
     *
     * <p>New schemas use petPhoto.photoData. Older rows may only have a path, so
     * this method still tries that path as a fallback.</p>
     */
    public byte[] findPetPhotoBytes(long petId) throws SQLException {
        return findPetPhotoBytes(petId, "BEFORE");
    }

    public PetPhotoPair findPetPhotoPair(long petId) throws SQLException {
        return new PetPhotoPair(
                findPetPhotoBytes(petId, "BEFORE"),
                findPetPhotoBytes(petId, "AFTER")
        );
    }

    private byte[] findPetPhotoBytes(long petId, String photoType) throws SQLException {
        try (Connection connection = ConexionBD.conectar()) {
            Set<String> photoColumns = findColumnNames(connection, "PETPHOTO");
            boolean hasPhotoData = photoColumns.contains("PHOTODATA");
            boolean hasTypedPhotos = photoColumns.contains("IDPET") && photoColumns.contains("PHOTOTYPE");
            boolean afterPhoto = "AFTER".equalsIgnoreCase(photoType);
            String sql;
            if (afterPhoto && hasTypedPhotos) {
                sql = hasPhotoData
                        ? "SELECT photoData, photoPath FROM petPhoto WHERE idPet = ? AND UPPER(NVL(photoType, '')) = 'AFTER' ORDER BY id DESC"
                        : "SELECT photoPath FROM petPhoto WHERE idPet = ? AND UPPER(NVL(photoType, '')) = 'AFTER' ORDER BY id DESC";
            } else {
                sql = hasPhotoData
                        ? """
                        SELECT pp.photoData, pp.photoPath
                        FROM pet p
                        LEFT JOIN petPhoto pp ON pp.id = p.idPetPhoto
                        WHERE p.id = ?
                        """
                        : """
                        SELECT pp.photoPath
                        FROM pet p
                        LEFT JOIN petPhoto pp ON pp.id = p.idPetPhoto
                        WHERE p.id = ?
                        """;
            }

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, petId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return null;
                    }

                    if (hasPhotoData) {
                        Blob blob = resultSet.getBlob("photoData");
                        if (blob != null && blob.length() > 0) {
                            return blob.getBytes(1, (int) blob.length());
                        }
                    }
                    return PhotoStorageUtil.readBytesIfFileExists(resultSet.getString("photoPath"));
                }
            }
        }
    }

    /**
     * Changes the visible status of a pet when the user has permission.
     */
    public void updatePetStatus(long petId, long statusId, Long currentPersonId, boolean admin) throws SQLException {
        validateStatusChange(petId, statusId, currentPersonId, admin);

        try (Connection connection = ConexionBD.conectar()) {
            ensurePetStatusExists(connection, statusId);
            ensureStatusCanBeChangedFromList(connection, statusId);

            String sql;
            if (admin) {
                sql = "UPDATE pet SET idPetStatus = ? WHERE id = ?";
            } else {
                sql = """
                        UPDATE pet
                        SET idPetStatus = ?
                        WHERE id = ?
                          AND EXISTS (
                              SELECT 1
                              FROM rescuerxPet rp
                              WHERE rp.idPet = pet.id
                                AND rp.idRescuer = ?
                          )
                        """;
            }

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, statusId);
                statement.setLong(2, petId);
                if (!admin) {
                    statement.setLong(3, currentPersonId);
                }

                int updatedRows = statement.executeUpdate();
                if (updatedRows == 0) {
                    throw new IllegalArgumentException(
                            "Only the publication creator or an admin can change that status."
                    );
                }
                insertPetReportForExistingPetIfNeeded(connection, petId);
                auditRepository.log(connection, "Pets", "Status", "pet:" + petId, "status:" + statusId);
            }
        }
    }

    /**
     * Transfers control of an adoptable pet to a foster home user.
     */
    public void transferPetControlToFosterHome(long petId, long fosterHomePersonId, Long currentPersonId, boolean admin)
            throws SQLException {
        validateControlTransfer(petId, fosterHomePersonId, currentPersonId, admin);

        try (Connection connection = ConexionBD.conectar()) {
            connection.setAutoCommit(false);

            try {
                ensurePetExists(connection, petId);
                ensurePetIsForAdoption(connection, petId);
                ensureFosterHomeExists(connection, fosterHomePersonId);
                if (!admin && !userControlsPet(connection, petId, currentPersonId)) {
                    throw new IllegalArgumentException(
                            "Only the publication controller can transfer it to a foster home."
                    );
                }

                ensureRescuerProfile(connection, fosterHomePersonId);
                replacePetController(connection, petId, fosterHomePersonId);
                auditRepository.log(connection, "Pets", "Foster home", "pet:" + petId, "person:" + fosterHomePersonId);
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private String buildPetSearchSql(
            PetSearchCriteria criteria,
            boolean hasChip,
            boolean hasEventDate,
            boolean hasCreationDate,
            Long ownerPersonId,
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

        if (ownerPersonId != null) {
            sql.append("""
                     AND EXISTS (
                         SELECT 1
                         FROM rescuerxPet rp
                         WHERE rp.idPet = p.id
                           AND rp.idRescuer = ?
                     )
                    """);
            parameters.add(ownerPersonId);
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
            warnings.add("The date was not saved because the event date field is unavailable.");
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

    private void updatePetCore(
            Connection connection,
            long petId,
            PetFormData data,
            Set<String> petColumns,
            Map<String, Integer> petColumnSizes,
            List<String> warnings
    ) throws SQLException {
        List<String> assignments = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        addAssignment(assignments, values, petColumnSizes, "name", "NAME", data.getName());
        addAssignment(assignments, values, "idPetType", data.getPetTypeId());
        addAssignment(assignments, values, "idBreed", data.getBreedId());
        addAssignment(assignments, values, "idPetStatus", data.getPetStatusId());
        addAssignment(assignments, values, "idTrainingEase", data.getTrainingEaseId());
        addAssignment(assignments, values, "idColor", data.getColorId());
        addAssignment(assignments, values, "idSize", data.getPetSizeId());
        addAssignment(assignments, values, petColumnSizes, "description", "DESCRIPTION", data.getDescription());
        addAssignment(assignments, values, petColumnSizes, "needSpace", "NEEDSPACE", data.getNeedSpace());
        addAssignment(assignments, values, petColumnSizes, "energyLevel", "ENERGYLEVEL", data.getEnergyLevel());

        if (data.getDistrictId() != null) {
            addAssignment(assignments, values, "idLocation", data.getDistrictId());
        }

        if (petColumns.contains("CHIP")) {
            addAssignment(assignments, values, petColumnSizes, "chip", "CHIP", data.getChip());
        } else if (emptyToNull(data.getChip()) != null) {
            warnings.add("The chip was not updated because the pet.chip field is unavailable.");
        }

        if (petColumns.contains("EVENTDATE") && data.getEventDate() != null) {
            addAssignment(assignments, values, "eventDate", Date.valueOf(data.getEventDate()));
        }

        if (assignments.isEmpty()) {
            return;
        }

        values.add(petId);
        String sql = "UPDATE pet SET " + String.join(", ", assignments) + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, values);
            statement.executeUpdate();
        }
    }

    private void insertOptionalHealthAndVeterinarian(Connection connection, long petId, PetFormData data)
            throws SQLException {
        Long healthStatusId = insertOptionalHealthStatus(connection, data);
        if (healthStatusId != null) {
            mergePetHealthStatus(connection, petId, healthStatusId);
            mergeHealthDisease(connection, healthStatusId, data.getDiseaseId(), data.getHealthDescription());
            mergeHealthTreatment(connection, healthStatusId, data.getTreatmentId());
            mergeHealthMedicine(connection, healthStatusId, data.getMedicineId(), data.getMedicineDose());
        }

        Long veterinarianId = data.getVeterinarianId();
        if (veterinarianId == null && emptyToNull(data.getVeterinarianName()) != null) {
            veterinarianId = insertVeterinarian(connection, data.getVeterinarianName());
        }
        if (veterinarianId != null) {
            mergePetVeterinarian(connection, petId, veterinarianId);
        }
    }

    private Long insertOptionalHealthStatus(Connection connection, PetFormData data) throws SQLException {
        boolean hasHealthData = emptyToNull(data.getHealthState()) != null
                || emptyToNull(data.getHealthDescription()) != null
                || data.getDiseaseId() != null
                || data.getTreatmentId() != null
                || data.getMedicineId() != null;

        if (!hasHealthData) {
            return null;
        }

        long healthStatusId = nextSequenceValue(connection, "sHealthStatus");
        String sql = "INSERT INTO healthStatus(id, illnessState, description) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, healthStatusId);
            statement.setString(2, fit(data.getHealthState(), "Not specified", 25));
            statement.setString(3, fit(data.getHealthDescription(), null, 50));
            statement.executeUpdate();
        }
        return healthStatusId;
    }

    private Long insertVeterinarian(Connection connection, String veterinarianName) throws SQLException {
        long veterinarianId = nextSequenceValue(connection, "sVeterinarian");
        String sql = "INSERT INTO veterinarian(id, name) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, veterinarianId);
            statement.setString(2, fit(veterinarianName, null, 25));
            statement.executeUpdate();
        }
        return veterinarianId;
    }

    private void mergePetHealthStatus(Connection connection, long petId, long healthStatusId) throws SQLException {
        String sql = """
                MERGE INTO petxHealthStatus target
                USING (SELECT ? AS idPet, ? AS idHealthStatus FROM dual) source
                ON (target.idPet = source.idPet AND target.idHealthStatus = source.idHealthStatus)
                WHEN NOT MATCHED THEN
                    INSERT (idPet, idHealthStatus) VALUES (source.idPet, source.idHealthStatus)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, petId);
            statement.setLong(2, healthStatusId);
            statement.executeUpdate();
        }
    }

    private void mergeHealthDisease(Connection connection, long healthStatusId, Long diseaseId, String description)
            throws SQLException {
        if (diseaseId == null) {
            return;
        }

        String sql = "INSERT INTO healthStxDisease(idHealthStatus, idDisease, description) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, healthStatusId);
            statement.setLong(2, diseaseId);
            statement.setString(3, fit(description, null, 50));
            statement.executeUpdate();
        }
    }

    private void mergeHealthTreatment(Connection connection, long healthStatusId, Long treatmentId) throws SQLException {
        if (treatmentId == null) {
            return;
        }

        String sql = "INSERT INTO healthStxTreatment(idHealthStatus, idTreatment) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, healthStatusId);
            statement.setLong(2, treatmentId);
            statement.executeUpdate();
        }
    }

    private void mergeHealthMedicine(Connection connection, long healthStatusId, Long medicineId, String dose)
            throws SQLException {
        if (medicineId == null) {
            return;
        }

        String sql = "INSERT INTO healthStxMedicine(idHealthStatus, idMedicine, dose) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, healthStatusId);
            statement.setLong(2, medicineId);
            statement.setString(3, fit(dose, "Vet directed", 20));
            statement.executeUpdate();
        }
    }

    private void mergePetVeterinarian(Connection connection, long petId, long veterinarianId) throws SQLException {
        String sql = """
                MERGE INTO petxVeterinarian target
                USING (SELECT ? AS idPet, ? AS idVeterinarian FROM dual) source
                ON (target.idPet = source.idPet AND target.idVeterinarian = source.idVeterinarian)
                WHEN NOT MATCHED THEN
                    INSERT (idPet, idVeterinarian) VALUES (source.idPet, source.idVeterinarian)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, petId);
            statement.setLong(2, veterinarianId);
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
            warnings.add("The " + sqlName + " value was not saved because the pet." + sqlName + " field is unavailable.");
        }
    }

    private Long insertPetPhoto(Connection connection, Long petId, String photoType, String photoPath) throws SQLException {
        String normalizedPath = emptyToNull(photoPath);
        if (normalizedPath == null) {
            return null;
        }

        Set<String> photoColumns = findColumnNames(connection, "PETPHOTO");
        boolean storesBlob = photoColumns.contains("PHOTODATA");
        StoredPhoto photo = PhotoStorageUtil.fromPath(normalizedPath, storesBlob);

        long photoId = nextSequenceValue(connection, "sPetPhoto");
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        addColumn(columns, values, "id", photoId);
        addColumn(columns, values, "photoPath", fit(photo.storedPath(), normalizedPath, 255));

        if (photoColumns.contains("IDPET") && petId != null) {
            addColumn(columns, values, "idPet", petId);
        }
        if (photoColumns.contains("PHOTOTYPE")) {
            addColumn(columns, values, "photoType", fit(photoType, null, 20));
        }
        if (photoColumns.contains("FILENAME")) {
            addColumn(columns, values, "fileName", fit(photo.fileName(), null, 255));
        }
        if (photoColumns.contains("MIMETYPE")) {
            addColumn(columns, values, "mimeType", fit(photo.mimeType(), null, 80));
        }
        if (storesBlob && photo.data() != null) {
            addColumn(columns, values, "photoData", photo.data());
        }

        StringJoiner placeholders = new StringJoiner(", ");
        for (int index = 0; index < columns.size(); index++) {
            placeholders.add("?");
        }

        String sql = "INSERT INTO petPhoto(" + String.join(", ", columns) + ") VALUES (" + placeholders + ")";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, values);
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

    private void updatePetPhoto(Connection connection, long petId, String photoType, String photoPath, boolean mainPhoto)
            throws SQLException {
        String normalizedPath = emptyToNull(photoPath);
        if (normalizedPath == null) {
            return;
        }

        Set<String> photoColumns = findColumnNames(connection, "PETPHOTO");
        boolean storesBlob = photoColumns.contains("PHOTODATA");
        Long photoId = findTypedPhotoId(connection, petId, photoType);
        if (photoId == null && mainPhoto) {
            photoId = findPetLinkedId(connection, petId, "idPetPhoto");
        }
        if (photoId == null) {
            Long newPhotoId = insertPetPhoto(connection, petId, photoType, normalizedPath);
            if (mainPhoto && newPhotoId != null) {
                updatePetLinkedId(connection, petId, "idPetPhoto", newPhotoId);
            }
            return;
        }

        StoredPhoto photo = PhotoStorageUtil.fromPath(normalizedPath, false);
        if (storesBlob && photo.data() == null) {
            return;
        }

        List<String> assignments = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        addAssignment(assignments, values, "photoPath", fit(photo.storedPath(), normalizedPath, 255));

        if (photoColumns.contains("IDPET")) {
            addAssignment(assignments, values, "idPet", petId);
        }
        if (photoColumns.contains("PHOTOTYPE")) {
            addAssignment(assignments, values, "photoType", fit(photoType, null, 20));
        }
        if (photoColumns.contains("FILENAME")) {
            addAssignment(assignments, values, "fileName", fit(photo.fileName(), null, 255));
        }
        if (photoColumns.contains("MIMETYPE")) {
            addAssignment(assignments, values, "mimeType", fit(photo.mimeType(), null, 80));
        }
        if (storesBlob && photo.data() != null) {
            addAssignment(assignments, values, "photoData", photo.data());
        }

        values.add(photoId);
        String sql = "UPDATE petPhoto SET " + String.join(", ", assignments) + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, values);
            statement.executeUpdate();
        }
    }

    private void linkPhotoToPetIfPossible(Connection connection, Long photoId, long petId, String photoType) throws SQLException {
        if (photoId == null) {
            return;
        }

        Set<String> photoColumns = findColumnNames(connection, "PETPHOTO");
        List<String> assignments = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        if (photoColumns.contains("IDPET")) {
            addAssignment(assignments, values, "idPet", petId);
        }
        if (photoColumns.contains("PHOTOTYPE")) {
            addAssignment(assignments, values, "photoType", fit(photoType, null, 20));
        }
        if (assignments.isEmpty()) {
            return;
        }

        values.add(photoId);
        String sql = "UPDATE petPhoto SET " + String.join(", ", assignments) + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, values);
            statement.executeUpdate();
        }
    }

    private Long findTypedPhotoId(Connection connection, long petId, String photoType) throws SQLException {
        Set<String> photoColumns = findColumnNames(connection, "PETPHOTO");
        if (!photoColumns.contains("IDPET") || !photoColumns.contains("PHOTOTYPE")) {
            return null;
        }

        String sql = """
                SELECT id
                FROM petPhoto
                WHERE idPet = ?
                  AND UPPER(NVL(photoType, '')) = UPPER(?)
                ORDER BY id DESC
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, petId);
            statement.setString(2, photoType);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return resultSet.getLong("id");
            }
        }
    }

    private void updatePetContact(Connection connection, long petId, String email, String phone) throws SQLException {
        String normalizedEmail = emptyToNull(email);
        String normalizedPhone = emptyToNull(phone);
        if (normalizedEmail == null && normalizedPhone == null) {
            return;
        }

        Long contactId = findPetLinkedId(connection, petId, "idPetContact");
        if (contactId == null) {
            contactId = insertPetContact(connection);
            updatePetLinkedId(connection, petId, "idPetContact", contactId);
        }

        if (normalizedEmail != null) {
            upsertPetContactEmail(connection, contactId, normalizedEmail);
        }
        if (normalizedPhone != null) {
            upsertPetContactPhone(connection, contactId, normalizedPhone);
        }
    }

    private void upsertPetContactEmail(Connection connection, long contactId, String email) throws SQLException {
        Long emailId = findFirstLinkedId(
                connection,
                "SELECT MIN(idEmail) FROM petContactxEmail WHERE idPetContact = ?",
                contactId
        );
        if (emailId == null) {
            insertOptionalPetContactEmail(connection, contactId, email);
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement("UPDATE email SET emailAddress = ? WHERE id = ?")) {
            statement.setString(1, email);
            statement.setLong(2, emailId);
            statement.executeUpdate();
        }
    }

    private void upsertPetContactPhone(Connection connection, long contactId, String phone) throws SQLException {
        Long phoneId = findFirstLinkedId(
                connection,
                "SELECT MIN(idPhone) FROM petContactxPhone WHERE idPetContact = ?",
                contactId
        );
        if (phoneId == null) {
            insertOptionalPetContactPhone(connection, contactId, phone);
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement("UPDATE phone SET phoneNumber = ? WHERE id = ?")) {
            statement.setString(1, phone);
            statement.setLong(2, phoneId);
            statement.executeUpdate();
        }
    }

    private void upsertReward(Connection connection, long petId, Long currencyId, BigDecimal amount) throws SQLException {
        if (currencyId == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        Long rewardId = findFirstLinkedId(connection, "SELECT MAX(id) FROM reward WHERE idPet = ?", petId);
        if (rewardId == null) {
            insertOptionalReward(connection, petId, currencyId, amount);
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE reward SET idCurrency = ?, amount = ? WHERE id = ?"
        )) {
            statement.setLong(1, currencyId);
            statement.setBigDecimal(2, amount);
            statement.setLong(3, rewardId);
            statement.executeUpdate();
        }
    }

    private Long findPetLinkedId(Connection connection, long petId, String columnName) throws SQLException {
        String sql = "SELECT " + columnName + " FROM pet WHERE id = ?";
        return findFirstLinkedId(connection, sql, petId);
    }

    private void updatePetLinkedId(Connection connection, long petId, String columnName, Long value) throws SQLException {
        String sql = "UPDATE pet SET " + columnName + " = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, value);
            statement.setLong(2, petId);
            statement.executeUpdate();
        }
    }

    private Long findFirstLinkedId(Connection connection, String sql, long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }

                long value = resultSet.getLong(1);
                return resultSet.wasNull() ? null : value;
            }
        }
    }

    private void insertPetReportForExistingPetIfNeeded(Connection connection, long petId) throws SQLException {
        String sql = """
                SELECT idPetType, idBreed, idPetStatus, idColor, idSize, eventDate
                FROM pet
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, petId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return;
                }

                Date eventDate = resultSet.getDate("eventDate");
                insertPetReportIfNeeded(
                        connection,
                        longOrNull(resultSet, "idPetType"),
                        longOrNull(resultSet, "idBreed"),
                        longOrNull(resultSet, "idPetStatus"),
                        longOrNull(resultSet, "idColor"),
                        longOrNull(resultSet, "idSize"),
                        eventDate == null ? LocalDate.now() : eventDate.toLocalDate()
                );
            }
        }
    }

    private void insertPetReportIfNeeded(Connection connection, PetFormData data) throws SQLException {
        insertPetReportIfNeeded(
                connection,
                data.getPetTypeId(),
                data.getBreedId(),
                data.getPetStatusId(),
                data.getColorId(),
                data.getPetSizeId(),
                data.getEventDate() == null ? LocalDate.now() : data.getEventDate()
        );
    }

    private void insertPetReportIfNeeded(
            Connection connection,
            Long petTypeId,
            Long breedId,
            Long statusId,
            Long colorId,
            Long sizeId,
            LocalDate reportDate
    ) throws SQLException {
        if (petTypeId == null || breedId == null || statusId == null || colorId == null || sizeId == null) {
            return;
        }

        if (!isLostOrFoundStatus(connection, statusId)) {
            return;
        }

        if (petReportExists(connection, petTypeId, breedId, statusId, colorId, sizeId, reportDate)) {
            return;
        }

        String sql = """
                INSERT INTO petReport(id, idColor, idSize, idPetStatus, idBreed, idPetType, reportDate)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, nextSequenceValue(connection, "sMatch"));
            statement.setLong(2, colorId);
            statement.setLong(3, sizeId);
            statement.setLong(4, statusId);
            statement.setLong(5, breedId);
            statement.setLong(6, petTypeId);
            statement.setDate(7, Date.valueOf(reportDate));
            statement.executeUpdate();
        }
    }

    private boolean petReportExists(
            Connection connection,
            long petTypeId,
            long breedId,
            long statusId,
            long colorId,
            long sizeId,
            LocalDate reportDate
    ) throws SQLException {
        String sql = """
                SELECT 1
                FROM petReport
                WHERE idPetType = ?
                  AND idBreed = ?
                  AND idPetStatus = ?
                  AND idColor = ?
                  AND idSize = ?
                  AND TRUNC(reportDate) = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, petTypeId);
            statement.setLong(2, breedId);
            statement.setLong(3, statusId);
            statement.setLong(4, colorId);
            statement.setLong(5, sizeId);
            statement.setDate(6, Date.valueOf(reportDate));

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private boolean isLostOrFoundStatus(Connection connection, long statusId) throws SQLException {
        String sql = "SELECT LOWER(NVL(status, '')) FROM petStatus WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, statusId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return false;
                }

                String statusName = resultSet.getString(1);
                return statusName.contains("lost")
                        || statusName.contains("perd")
                        || statusName.contains("found")
                        || statusName.contains("encontr");
            }
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
        requireValue(data.getName(), "Pet name is required.");
        requireSelected(data.getPetTypeId(), "Select the pet type.");
        requireSelected(data.getBreedId(), "Select the breed.");
        requireSelected(data.getPetStatusId(), "Select the status.");
        requireSelected(data.getColorId(), "Select the color.");
        requireSelected(data.getPetSizeId(), "Select the size.");
        requireSelected(data.getDistrictId(), "Select province, canton, and district.");
        if (data.getEventDate() == null) {
            throw new IllegalArgumentException("Select the event or publication date.");
        }

        if (emptyToNull(data.getContactEmail()) == null && emptyToNull(data.getContactPhone()) == null) {
            throw new IllegalArgumentException("Enter at least one contact email or phone.");
        }

        String phone = emptyToNull(data.getContactPhone());
        if (phone != null && !phone.matches("\\d{8}")) {
            throw new IllegalArgumentException("Contact phone must have 8 digits.");
        }

        String email = emptyToNull(data.getContactEmail());
        if (email != null && !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Contact email has an invalid format.");
        }

        if (data.getRewardAmount() != null
                && data.getRewardAmount().compareTo(BigDecimal.ZERO) > 0
                && data.getCurrencyId() == null) {
            throw new IllegalArgumentException("Select the reward currency.");
        }
    }

    private void validateStatusChange(long petId, long statusId, Long currentPersonId, boolean admin) {
        if (petId <= 0) {
            throw new IllegalArgumentException("Select a valid pet.");
        }

        if (statusId <= 0) {
            throw new IllegalArgumentException("Select a valid status.");
        }

        if (!admin && currentPersonId == null) {
            throw new IllegalArgumentException("You must sign in to change the status.");
        }
    }

    private void validatePetUpdate(long petId, PetFormData data, Long currentPersonId, boolean admin) {
        if (petId <= 0) {
            throw new IllegalArgumentException("Select a valid pet.");
        }
        validatePet(data);

        if (!admin && currentPersonId == null) {
            throw new IllegalArgumentException("You must sign in to edit pets.");
        }
    }

    private void validateControlTransfer(long petId, long fosterHomePersonId, Long currentPersonId, boolean admin) {
        if (petId <= 0) {
            throw new IllegalArgumentException("Select a valid pet.");
        }

        if (fosterHomePersonId <= 0) {
            throw new IllegalArgumentException("Select a valid foster home.");
        }

        if (!admin && currentPersonId == null) {
            throw new IllegalArgumentException("You must sign in to transfer control.");
        }
    }

    private void ensurePetExists(Connection connection, long petId) throws SQLException {
        String sql = "SELECT 1 FROM pet WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, petId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("The selected pet does not exist.");
                }
            }
        }
    }

    private void ensurePetIsForAdoption(Connection connection, long petId) throws SQLException {
        String sql = """
                SELECT LOWER(NVL(ps.status, '')) AS statusName
                FROM pet p
                LEFT JOIN petStatus ps
                    ON ps.id = p.idPetStatus
                WHERE p.id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, petId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("The selected pet does not exist.");
                }

                String statusName = resultSet.getString("statusName");
                if (!isForAdoptionStatus(statusName)) {
                    throw new IllegalArgumentException(
                            "Control can only be transferred for pets that are for adoption."
                    );
                }
            }
        }
    }

    private void ensureFosterHomeExists(Connection connection, long fosterHomePersonId) throws SQLException {
        String sql = "SELECT 1 FROM fosterHome WHERE idPerson = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, fosterHomePersonId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("The selected foster home is not active.");
                }
            }
        }
    }

    private boolean userControlsPet(Connection connection, long petId, Long personId) throws SQLException {
        if (personId == null) {
            return false;
        }

        String sql = """
                SELECT 1
                FROM rescuerxPet
                WHERE idPet = ?
                  AND idRescuer = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, petId);
            statement.setLong(2, personId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private void replacePetController(Connection connection, long petId, long fosterHomePersonId) throws SQLException {
        try (PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM rescuerxPet WHERE idPet = ?")) {
            deleteStatement.setLong(1, petId);
            deleteStatement.executeUpdate();
        }

        linkRescuerToPet(connection, fosterHomePersonId, petId);
    }

    private boolean isForAdoptionStatus(String statusName) {
        if (statusName == null) {
            return false;
        }

        String normalizedStatus = statusName.toLowerCase(Locale.ROOT);
        return normalizedStatus.equals("for adoption")
                || normalizedStatus.equals("en adopcion")
                || normalizedStatus.equals("en adopción");
    }

    private void ensurePetStatusExists(Connection connection, long statusId) throws SQLException {
        String sql = "SELECT 1 FROM petStatus WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, statusId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("The selected status is not available.");
                }
            }
        }
    }

    private void ensureStatusCanBeChangedFromList(Connection connection, long statusId) throws SQLException {
        String sql = "SELECT LOWER(NVL(status, '')) AS statusName FROM petStatus WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, statusId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("The selected status is not available.");
                }

                String statusName = resultSet.getString("statusName");
                if (isAdoptedStatus(statusName)) {
                    throw new IllegalArgumentException(
                            "To mark a pet as adopted, use the Adoptions module so the adopter is recorded."
                    );
                }
            }
        }
    }

    private boolean isAdoptedStatus(String statusName) {
        if (statusName == null) {
            return false;
        }

        String normalizedStatus = statusName.toLowerCase(Locale.ROOT);
        return normalizedStatus.equals("adopted") || normalizedStatus.equals("adoptado");
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

    private void addAssignment(List<String> assignments, List<Object> values, String column, Object value) {
        assignments.add(column + " = ?");
        values.add(value);
    }

    private void addAssignment(
            List<String> assignments,
            List<Object> values,
            Map<String, Integer> columnSizes,
            String sqlColumn,
            String metadataColumn,
            String value
    ) {
        assignments.add(sqlColumn + " = ?");
        values.add(fitToColumn(value, columnSizes, metadataColumn));
    }

    private void setParameters(PreparedStatement statement, List<Object> values) throws SQLException {
        for (int index = 0; index < values.size(); index++) {
            Object value = values.get(index);
            if (value instanceof Date date) {
                statement.setDate(index + 1, date);
            } else if (value instanceof BigDecimal decimal) {
                statement.setBigDecimal(index + 1, decimal);
            } else if (value instanceof byte[] bytes) {
                statement.setBytes(index + 1, bytes);
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

    private String fit(String value, String defaultValue, int maxLength) {
        String normalizedValue = emptyToNull(value);
        if (normalizedValue == null) {
            normalizedValue = defaultValue;
        }
        if (normalizedValue != null && normalizedValue.length() > maxLength) {
            return normalizedValue.substring(0, maxLength);
        }
        return normalizedValue;
    }

    private Long longOrNull(ResultSet resultSet, String columnName) throws SQLException {
        long value = resultSet.getLong(columnName);
        return resultSet.wasNull() ? null : value;
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
