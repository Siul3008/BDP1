package cr.tec.bd.crv.database;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Centralizes the Oracle connection settings used by the application.
 */
public class ConexionBD {

    private static final String URL = "jdbc:oracle:thin:@//100.85.86.27:1521/dbprueba";
    private static final String USER = "PROJECT1";
    private static final String PASSWORD = "PBD1";

    private ConexionBD() {
    }

    public static Connection conectar() throws SQLException {
        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException("La contrasena de la base de datos no esta configurada.");
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
