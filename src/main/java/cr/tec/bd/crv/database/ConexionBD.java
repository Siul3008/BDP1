package cr.tec.bd.crv.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Cambiá estos datos por los de tu base
    private static final String URL = "jdbc:oracle:thin:@//100.85.86.27:1521/dbprueba";
    private static final String USER = "Project1";
    private static final String PASSWORD = "PBD1";

    private ConexionBD() {
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}