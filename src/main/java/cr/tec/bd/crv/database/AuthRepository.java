package cr.tec.bd.crv.database;

import cr.tec.bd.crv.model.UserRegistrationData;
import cr.tec.bd.crv.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access class for login and account registration.
 */
public class AuthRepository {

    // Public login entry points keep account type details out of the controller.
    public boolean loginUser(String email, String password) throws SQLException {
        return login("USER", email, password);
    }

    public boolean loginAdmin(String email, String password) throws SQLException {
        return login("ADMIN", email, password);
    }

    // User registration touches several tables, so it runs inside a single transaction.
    public void registerUser(UserRegistrationData data) throws SQLException {
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
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    // Both user and admin login use appAccount, filtered by accountType.
    private boolean login(String accountType, String email, String password) throws SQLException {
        String sql = """
                SELECT 1
                FROM appAccount
                WHERE accountType = ?
                  AND loginEmail = ?
                  AND passwordHash = ?
                  AND isActive = 'Y'
                """;

        try (Connection connection = ConexionBD.conectar();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountType);
            statement.setString(2, email.trim());
            statement.setString(3, PasswordUtil.hash(password));

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
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
    }

    private void requireValue(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
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
