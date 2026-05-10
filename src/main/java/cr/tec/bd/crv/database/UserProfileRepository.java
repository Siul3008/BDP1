package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.UserProfile;
import cr.tec.bd.crv.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Reads and updates the information shown in "Mi perfil".
 *
 * <p>The profile screen changes person names, contact data, and passwords. Each
 * update is kept separate so a user can edit one section without accidentally
 * changing another.</p>
 */
public class UserProfileRepository {

    private final ApplicationAuditRepository auditRepository = new ApplicationAuditRepository();

    /**
     * Loads the signed-in user's current profile values.
     */
    public UserProfile findProfile(long personId) throws SQLException {
        String sql = """
                SELECT
                    p.firstName,
                    p.secondName,
                    p.firstLastName,
                    p.secondLastName,
                    aa.identificationValue,
                    aa.loginEmail
                FROM person p
                LEFT JOIN appAccount aa
                    ON aa.idPerson = p.id
                   AND aa.accountType = 'USER'
                   AND aa.isActive = 'Y'
                WHERE p.id = ?
                """;

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalArgumentException("No se encontro el perfil del usuario.");
                }

                return new UserProfile(
                        personId,
                        resultSet.getString("firstName"),
                        resultSet.getString("secondName"),
                        resultSet.getString("firstLastName"),
                        resultSet.getString("secondLastName"),
                        resultSet.getString("identificationValue"),
                        resultSet.getString("loginEmail"),
                        findPrimaryPhone(connection, personId)
                );
            }
        }
    }

    /**
     * Updates the user's separated name fields.
     */
    public void updateNames(long personId, String firstName, String secondName, String firstLastName, String secondLastName)
            throws SQLException {
        validateNames(firstName, secondName, firstLastName, secondLastName);

        String sql = """
                UPDATE person
                SET firstName = ?,
                    secondName = ?,
                    firstLastName = ?,
                    secondLastName = ?
                WHERE id = ?
                """;

        try (Connection connection = ConexionBD.conectar()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, firstName.trim());
                statement.setString(2, emptyToNull(secondName));
                statement.setString(3, firstLastName.trim());
                statement.setString(4, emptyToNull(secondLastName));
                statement.setLong(5, personId);
                statement.executeUpdate();
                auditRepository.log(connection, "Perfil", "Nombres", "person:" + personId, firstName.trim());
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Updates primary email and phone in both account/contact tables.
     */
    public void updateContact(long personId, String email, String phone) throws SQLException {
        validateContact(email, phone);

        try (Connection connection = ConexionBD.conectar()) {
            connection.setAutoCommit(false);

            try {
                ensureEmailIsAvailable(connection, personId, email.trim());
                updateAccountEmail(connection, personId, email.trim());
                upsertPrimaryEmail(connection, personId, email.trim());
                upsertPrimaryPhone(connection, personId, phone.trim());
                auditRepository.log(connection, "Perfil", "Contacto", "person:" + personId, email.trim());
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    /**
     * Changes the password after checking the current password and confirmation.
     */
    public void updatePassword(long personId, String currentPassword, String newPassword, String confirmation)
            throws SQLException {
        validatePasswordChange(currentPassword, newPassword, confirmation);

        try (Connection connection = ConexionBD.conectar()) {
            connection.setAutoCommit(false);

            try {
                if (!currentPasswordMatches(connection, personId, currentPassword)) {
                    throw new IllegalArgumentException("La contrasena actual no coincide.");
                }

                String sql = """
                        UPDATE appAccount
                        SET passwordHash = ?
                        WHERE idPerson = ?
                          AND accountType = 'USER'
                          AND isActive = 'Y'
                        """;

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, PasswordUtil.hash(newPassword.trim()));
                    statement.setLong(2, personId);
                    statement.executeUpdate();
                }
                auditRepository.log(connection, "Perfil", "Contrasena", "person:" + personId, "actualizada");
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private String findPrimaryPhone(Connection connection, long personId) throws SQLException {
        String sql = """
                SELECT ph.phoneNumber
                FROM personxPhone px
                JOIN phone ph
                    ON ph.id = px.idPhone
                WHERE px.idPerson = ?
                ORDER BY ph.id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("phoneNumber");
                }
                return "";
            }
        }
    }

    private void ensureEmailIsAvailable(Connection connection, long personId, String email) throws SQLException {
        String sql = """
                SELECT 1
                FROM appAccount
                WHERE LOWER(loginEmail) = LOWER(?)
                  AND NVL(idPerson, -1) <> ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setLong(2, personId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    throw new IllegalArgumentException("Ese correo ya esta usado por otra cuenta.");
                }
            }
        }
    }

    private void updateAccountEmail(Connection connection, long personId, String email) throws SQLException {
        String sql = """
                UPDATE appAccount
                SET loginEmail = ?
                WHERE idPerson = ?
                  AND accountType = 'USER'
                  AND isActive = 'Y'
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setLong(2, personId);
            statement.executeUpdate();
        }
    }

    private void upsertPrimaryEmail(Connection connection, long personId, String email) throws SQLException {
        Long emailId = findFirstLinkedId(connection, "personxEmail", "idEmail", "idPerson", personId);
        if (emailId == null) {
            emailId = nextSequenceValue(connection, "sEmail");
            try (PreparedStatement insertEmail = connection.prepareStatement(
                    "INSERT INTO email(id, emailAddress) VALUES (?, ?)"
            );
                 PreparedStatement linkEmail = connection.prepareStatement(
                         "INSERT INTO personxEmail(idPerson, idEmail) VALUES (?, ?)"
                 )) {
                insertEmail.setLong(1, emailId);
                insertEmail.setString(2, email);
                insertEmail.executeUpdate();

                linkEmail.setLong(1, personId);
                linkEmail.setLong(2, emailId);
                linkEmail.executeUpdate();
            }
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement("UPDATE email SET emailAddress = ? WHERE id = ?")) {
            statement.setString(1, email);
            statement.setLong(2, emailId);
            statement.executeUpdate();
        }
    }

    private void upsertPrimaryPhone(Connection connection, long personId, String phone) throws SQLException {
        Long phoneId = findFirstLinkedId(connection, "personxPhone", "idPhone", "idPerson", personId);
        if (phoneId == null) {
            phoneId = nextSequenceValue(connection, "sPhone");
            try (PreparedStatement insertPhone = connection.prepareStatement(
                    "INSERT INTO phone(id, phoneNumber) VALUES (?, ?)"
            );
                 PreparedStatement linkPhone = connection.prepareStatement(
                         "INSERT INTO personxPhone(idPerson, idPhone) VALUES (?, ?)"
                 )) {
                insertPhone.setLong(1, phoneId);
                insertPhone.setString(2, phone);
                insertPhone.executeUpdate();

                linkPhone.setLong(1, personId);
                linkPhone.setLong(2, phoneId);
                linkPhone.executeUpdate();
            }
            return;
        }

        try (PreparedStatement statement = connection.prepareStatement("UPDATE phone SET phoneNumber = ? WHERE id = ?")) {
            statement.setString(1, phone);
            statement.setLong(2, phoneId);
            statement.executeUpdate();
        }
    }

    private Long findFirstLinkedId(
            Connection connection,
            String tableName,
            String targetColumn,
            String ownerColumn,
            long ownerId
    ) throws SQLException {
        String sql = "SELECT " + targetColumn + " FROM " + tableName + " WHERE " + ownerColumn + " = ? ORDER BY " + targetColumn;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, ownerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(targetColumn);
                }
                return null;
            }
        }
    }

    private boolean currentPasswordMatches(Connection connection, long personId, String currentPassword) throws SQLException {
        String sql = """
                SELECT 1
                FROM appAccount
                WHERE idPerson = ?
                  AND accountType = 'USER'
                  AND passwordHash = ?
                  AND isActive = 'Y'
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);
            statement.setString(2, PasswordUtil.hash(currentPassword.trim()));

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
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

    private void validateNames(String firstName, String secondName, String firstLastName, String secondLastName) {
        requireValue(firstName, "El primer nombre es obligatorio.");
        requireValue(firstLastName, "El primer apellido es obligatorio.");
        validateLength(firstName, 20, "El primer nombre no puede superar 20 caracteres.");
        validateOptionalLength(secondName, 20, "El segundo nombre no puede superar 20 caracteres.");
        validateLength(firstLastName, 25, "El primer apellido no puede superar 25 caracteres.");
        validateOptionalLength(secondLastName, 25, "El segundo apellido no puede superar 25 caracteres.");
    }

    private void validateContact(String email, String phone) {
        requireValue(email, "El correo principal es obligatorio.");
        requireValue(phone, "El telefono principal es obligatorio.");
        if (!email.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("El correo principal no tiene un formato valido.");
        }
        validateLength(email, 100, "El correo no puede superar 100 caracteres.");
        if (!phone.trim().matches("\\d{8}")) {
            throw new IllegalArgumentException("El telefono principal debe tener 8 digitos.");
        }
    }

    private void validatePasswordChange(String currentPassword, String newPassword, String confirmation) {
        requireValue(currentPassword, "Digite la contrasena actual.");
        requireValue(newPassword, "Digite la contrasena nueva.");
        requireValue(confirmation, "Confirme la contrasena nueva.");
        if (!newPassword.trim().equals(confirmation.trim())) {
            throw new IllegalArgumentException("La confirmacion no coincide.");
        }
        validatePassword(newPassword);
    }

    private void validatePassword(String password) {
        String normalizedPassword = password.trim();
        if (normalizedPassword.length() < 8
                || !normalizedPassword.matches(".*[A-Z].*")
                || !normalizedPassword.matches(".*[a-z].*")
                || !normalizedPassword.matches(".*\\d.*")
                || !normalizedPassword.matches(".*[^A-Za-z0-9].*")) {
            throw new IllegalArgumentException(
                    "La contrasena debe tener minimo 8 caracteres, mayuscula, minuscula, numero y caracter especial."
            );
        }
    }

    private void requireValue(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateLength(String value, int maxLength, String message) {
        if (value != null && value.trim().length() > maxLength) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateOptionalLength(String value, int maxLength, String message) {
        if (emptyToNull(value) != null) {
            validateLength(value, maxLength, message);
        }
    }

    private String emptyToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}
