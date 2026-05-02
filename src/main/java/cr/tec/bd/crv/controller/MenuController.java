package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.ConexionBD;
import cr.tec.bd.crv.util.NavigationUtil;
import cr.tec.bd.crv.util.SessionContext;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Main menu controller. It routes users to the different modules after login.
 */
public class MenuController {

    // All menu buttons reuse the same navigation helper to keep window behavior consistent.
    private void openModule(ActionEvent event, String fxmlPath, String title) throws IOException {
        NavigationUtil.openWindow(event, fxmlPath, title);
    }

    public void abrirRegistrarMascota(ActionEvent event) throws IOException {
        openModule(event, "/view/registrar_mascota.fxml", "Registrar Mascota");
    }

    public void abrirBuscarMascota(ActionEvent event) throws IOException {
        openModule(event, "/view/buscar_mascota.fxml", "Buscar Mascota");
    }

    public void abrirListaMascotas(ActionEvent event) throws IOException {
        openModule(event, "/view/lista_mascotas.fxml", "Lista de Mascotas");
    }

    public void abrirPerfil(ActionEvent event) throws IOException {
        openModule(event, "/view/perfil_usuario.fxml", "Mi Perfil");
    }

    public void abrirCasasCuna(ActionEvent event) throws IOException {
        openModule(event, "/view/casas_cuna.fxml", "Casas Cuna");
    }

    public void abrirAdopciones(ActionEvent event) throws IOException {
        openModule(event, "/view/adopciones.fxml", "Adopciones");
    }

    public void abrirDonaciones(ActionEvent event) throws IOException {
        openModule(event, "/view/donaciones.fxml", "Donaciones");
    }

    public void abrirAsociaciones(ActionEvent event) throws IOException {
        openModule(event, "/view/asociaciones.fxml", "Asociaciones");
    }

    public void abrirBitacora(ActionEvent event) throws IOException {
        openModule(event, "/view/bitacora.fxml", "Bitácora");
    }

    public void abrirEstadisticas(ActionEvent event) throws IOException {
        openModule(event, "/view/estadisticas.fxml", "Estadísticas");
    }

    public void abrirReportes(ActionEvent event) throws IOException {
        openModule(event, "/view/reportes.fxml", "Consultas y Reportes");
    }

    // Returns to the first login screen without closing the desktop application.
    public void cerrarSesion(ActionEvent event) throws IOException {
        SessionContext.clear();
        NavigationUtil.openWindow(event, "/view/login.fxml", "BDP1 - Bienestar Animal");
    }

    // Keeps a manual database check available from the UI while the project is being integrated.
    public void probarConexion() {
        try (Connection connection = ConexionBD.conectar()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Conexión");
            alert.setHeaderText(null);
            alert.setContentText("Conexión exitosa con Oracle");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de conexión");
            alert.setHeaderText("No se pudo conectar a la base de datos");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // This is the only action that closes the JavaFX Stage completely.
    public void salirSistema(ActionEvent event) {
        SessionContext.clear();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
