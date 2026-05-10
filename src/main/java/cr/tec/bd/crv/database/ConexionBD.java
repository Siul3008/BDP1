package cr.tec.bd.crv.database;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Creates Oracle database connections for the whole application.
 *
 * <p>Repositories do not store their own URL, user, or password. They all call
 * this class, which keeps the connection settings in one place and makes future
 * changes less risky.</p>
 */
public class ConexionBD {

    private static final String URL = "jdbc:oracle:thin:@//100.85.86.27:1521/dbprueba";
    private static final String USER = "PROJECT1";
    private static final String PASSWORD = "PBD1";

    private ConexionBD() {
    }

    /**
     * Opens a new Oracle connection.
     *
     * <p>Each repository method closes the connection after finishing its work,
     * usually through try-with-resources.</p>
     */
    public static Connection conectar() throws SQLException {
        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException("The system connection is not configured.");
        }

        // OracleDataSource is used instead of DriverManager because it sends Oracle-specific
        // connection properties more reliably for this project setup.
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setURL(URL);
        dataSource.setUser(USER);
        dataSource.setPassword(PASSWORD);
        return dataSource.getConnection();
    }
}
