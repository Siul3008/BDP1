package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.AuthenticatedAccount;
import cr.tec.bd.crv.model.UserRegistrationData;
import cr.tec.bd.crv.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles login and account creation in the database.
 *
 * <p>Controllers should not know how passwords are hashed, how accounts are
 * looked up, or how the person/adopter/contact rows are created. This repository
 * keeps all of that database work together.</p>
 */
public class AuthRepository {

    /**
     * Returns the authenticated account when email and password match an active account.
     */
    public AuthenticatedAccount loginAccount(String email, String password) throws SQLException {
        String normalizedEmail = emptyToNull(email);
        String normalizedPassword = emptyToNull(password);
        if (normalizedEmail == null || normalizedPassword == null) {
            return null;
        }

        String sql = """
                SELECT id, accountType, loginEmail, idPerson
                FROM appAccount
                WHERE loginEmail = ?
                  AND passwordHash = ?
                  AND isActive = 'Y'
                """;

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, normalizedEmail);
            statement.setString(2, PasswordUtil.hash(normalizedPassword));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }

                long accountId = resultSet.getLong("id");
                Long personId = resultSet.getObject("idPerson") == null ? null : resultSet.getLong("idPerson");
                AuthenticatedAccount account = new AuthenticatedAccount(
                        resultSet.getString("accountType"),
                        resultSet.getString("loginEmail"),
                        personId
                );
                updateLastLogin(connection, accountId);
                return account;
            }
        }
    }

    /**
     * Convenience check used by older user-login flows.
     */
    public boolean loginUser(String email, String password) throws SQLException {
        return loginUserPersonId(email, password) != null;
    }

    /**
     * Returns true only when the credentials belong to an admin account.
     */
    public boolean loginAdmin(String email, String password) throws SQLException {
        AuthenticatedAccount account = loginAccount(email, password);
        return account != null && account.isAdmin();
    }

    /**
     * Returns the linked person id for a normal user account.
     */
    public Long loginUserPersonId(String email, String password) throws SQLException {
        AuthenticatedAccount account = loginAccount(email, password);
        if (account != null && account.isUser()) {
            return account.getPersonId();
        }
        return null;
    }

    /**
     * Creates a complete user account: person, adopter profile, contacts, and app login.
     *
     * <p>All inserts run in one transaction. If one insert fails, everything is
     * rolled back so the database does not keep a half-created user.</p>
     */
    public long registerUser(UserRegistrationData data) throws SQLException {
        validateUserRegistration(data);

        try (Connection connection = ConexionBD.conectar()) {
            connection.setAutoCommit(false);

            try {
                ensureAccountIsUnique(connection, data.getPrimaryEmail(), data.getIdentification());

                // The person id is reused as the adopter id because adopter is a subtype of person.
                long personId = nextSequenceValue(connection, "sPerson");
                insertPerson(connection, personId, data);
                insertAdopter(connection, personId);
                insertPersonEmail(connection, personId, data.getPrimaryEmail());
                insertOptionalPersonEmail(connection, personId, data.getSecondaryEmail());
                insertPersonPhone(connection, personId, data.getPrimaryPhone());
                insertOptionalPersonPhone(connection, personId, data.getSecondaryPhone());
                insertAccount(
                        connection,
                        nextSequenceValue(connection, "sAppAccount"),
                        "USER",
                        data.getPrimaryEmail(),
                        data.getIdentification(),
                        data.getPassword(),
                        personId
                );

                connection.commit();
                return personId;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    // Last login is only informational, so a failure here should not block access.
    private void updateLastLogin(Connection connection, long accountId) throws SQLException {
        String sql = "UPDATE appAccount SET lastLoginAt = SYSDATE WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountId);
            statement.executeUpdate();
        }
    }

    // Validation stays in the repository because it protects database operations too.
    private void validateUserRegistration(UserRegistrationData data) {
        requireValue(data.getFirstName(), "El primer nombre es obligatorio.");
        requireValue(data.getFirstLastName(), "El primer apellido es obligatorio.");
        requireValue(data.getIdentification(), "La identificacion es obligatoria.");
        requireValue(data.getPrimaryEmail(), "El correo principal es obligatorio.");
        requireValue(data.getPrimaryPhone(), "El telefono principal es obligatorio.");
        requireValue(data.getPassword(), "La contrasena es obligatoria.");
        validateEmail(data.getPrimaryEmail(), "El correo principal no tiene un formato valido.");
        validateOptionalEmail(data.getSecondaryEmail(), "El correo adicional no tiene un formato valido.");
        validatePhone(data.getPrimaryPhone(), "El telefono principal debe tener 8 digitos.");
        validateOptionalPhone(data.getSecondaryPhone(), "El telefono adicional debe tener 8 digitos.");
        validatePassword(data.getPassword());
    }

    private void requireValue(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateOptionalEmail(String email, String message) {
        if (emptyToNull(email) != null) {
            validateEmail(email, message);
        }
    }

    private void validateEmail(String email, String message) {
        if (!email.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateOptionalPhone(String phone, String message) {
        if (emptyToNull(phone) != null) {
            validatePhone(phone, message);
        }
    }

    private void validatePhone(String phone, String message) {
        if (!phone.trim().matches("\\d{8}")) {
            throw new IllegalArgumentException(message);
        }
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

    // Prevents duplicate active accounts by checking the two fields used for login/blacklist control.
    private void ensureAccountIsUnique(Connection connection, String email, String identification) throws SQLException {
        String sql = """
                SELECT 1
                FROM appAccount
                WHERE loginEmail = ?
                   OR identificationValue = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email.trim());
            statement.setString(2, identification.trim());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    throw new IllegalArgumentException("Ya existe una cuenta con ese correo o identificacion.");
                }
            }
        }
    }

    // Sequence names are passed explicitly because the schema has one sequence per table.
    private long nextSequenceValue(Connection connection, String sequenceName) throws SQLException {
        String sql = "SELECT " + sequenceName + ".NEXTVAL FROM dual";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            resultSet.next();
            return resultSet.getLong(1);
        }
    }

    // Person data is kept separate from account credentials to match the logical database model.
    private void insertPerson(Connection connection, long personId, UserRegistrationData data) throws SQLException {
        String sql = """
                INSERT INTO person(id, firstName, secondName, firstLastName, secondLastName)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);
            statement.setString(2, data.getFirstName().trim());
            statement.setString(3, emptyToNull(data.getSecondName()));
            statement.setString(4, data.getFirstLastName().trim());
            statement.setString(5, emptyToNull(data.getSecondLastName()));
            statement.executeUpdate();
        }
    }

    // Every registered user starts as an adopter; other roles can be enabled later from profile flows.
    private void insertAdopter(Connection connection, long personId) throws SQLException {
        String sql = """
                INSERT INTO adopter(idPerson, idStarRating, note)
                VALUES (?, NULL, NULL)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, personId);
            statement.executeUpdate();
        }
    }

    // Emails are normalized into their own table, then linked through personxEmail.
    private void insertPersonEmail(Connection connection, long personId, String email) throws SQLException {
        long emailId = nextSequenceValue(connection, "sEmail");
        String insertEmailSql = "INSERT INTO email(id, emailAddress) VALUES (?, ?)";
        String linkSql = "INSERT INTO personxEmail(idPerson, idEmail) VALUES (?, ?)";

        try (PreparedStatement insertEmail = connection.prepareStatement(insertEmailSql);
             PreparedStatement insertLink = connection.prepareStatement(linkSql)) {
            insertEmail.setLong(1, emailId);
            insertEmail.setString(2, email.trim());
            insertEmail.executeUpdate();

            insertLink.setLong(1, personId);
            insertLink.setLong(2, emailId);
            insertLink.executeUpdate();
        }
    }

    // Optional contact values are skipped instead of inserting empty rows.
    private void insertOptionalPersonEmail(Connection connection, long personId, String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            return;
        }
        insertPersonEmail(connection, personId, email);
    }

    // Phones follow the same normalized pattern used by emails.
    private void insertPersonPhone(Connection connection, long personId, String phoneNumber) throws SQLException {
        long phoneId = nextSequenceValue(connection, "sPhone");
        String insertPhoneSql = "INSERT INTO phone(id, phoneNumber) VALUES (?, ?)";
        String linkSql = "INSERT INTO personxPhone(idPerson, idPhone) VALUES (?, ?)";

        try (PreparedStatement insertPhone = connection.prepareStatement(insertPhoneSql);
             PreparedStatement insertLink = connection.prepareStatement(linkSql)) {
            insertPhone.setLong(1, phoneId);
            insertPhone.setString(2, phoneNumber.trim());
            insertPhone.executeUpdate();

            insertLink.setLong(1, personId);
            insertLink.setLong(2, phoneId);
            insertLink.executeUpdate();
        }
    }

    // Optional contact values are skipped instead of inserting empty rows.
    private void insertOptionalPersonPhone(Connection connection, long personId, String phoneNumber) throws SQLException {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return;
        }
        insertPersonPhone(connection, personId, phoneNumber);
    }

    // appAccount is an application-level table added to support login without changing person tables.
    private void insertAccount(
            Connection connection,
            long accountId,
            String accountType,
            String loginEmail,
            String identification,
            String rawPassword,
            Long personId
    ) throws SQLException {
        String sql = """
                INSERT INTO appAccount(
                    id,
                    accountType,
                    loginEmail,
                    passwordHash,
                    identificationValue,
                    idPerson,
                    idAssociation,
                    isActive,
                    createdAt
                )
                VALUES (?, ?, ?, ?, ?, ?, NULL, 'Y', SYSDATE)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, accountId);
            statement.setString(2, accountType);
            statement.setString(3, loginEmail.trim());
            statement.setString(4, PasswordUtil.hash(rawPassword));
            statement.setString(5, identification.trim());
            statement.setLong(6, personId);
            statement.executeUpdate();
        }
    }

    private String emptyToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}
