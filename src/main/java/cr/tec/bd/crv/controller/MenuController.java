package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.util.NavigationUtil;
import cr.tec.bd.crv.util.SessionContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controls the main menu screens.
 *
 * <p>This controller is the traffic director of the app. It does not save data
 * by itself; it only decides which module should open when the user or admin
 * presses a menu button.</p>
 */
public class MenuController {

    @FXML
    private Button btnCasasCuna;

    @FXML
    public void initialize() {
        // Foster home stays visible because users can activate their own profile
        // or review active foster homes from the same module.
    }

    // All menu buttons reuse the same navigation helper to keep window behavior consistent.
    private void openModule(ActionEvent event, String fxmlPath, String title) throws IOException {
        NavigationUtil.openWindow(event, fxmlPath, title);
    }

    /**
     * Opens the form used to publish or edit a pet.
     */
    public void abrirRegistrarMascota(ActionEvent event) throws IOException {
        openModule(event, "/view/registrar_mascota.fxml", "Register Pet");
    }

    /**
     * Opens the search screen, where pets can be filtered by status and text.
     */
    public void abrirBuscarMascota(ActionEvent event) throws IOException {
        openModule(event, "/view/buscar_mascota.fxml", "Pet Search");
    }

    /**
     * Opens the list used to review pets and change allowed statuses.
     */
    public void abrirListaMascotas(ActionEvent event) throws IOException {
        openModule(event, "/view/lista_mascotas.fxml", "Pet List");
    }

    /**
     * Opens the account profile for the signed-in user.
     */
    public void abrirPerfil(ActionEvent event) throws IOException {
        openModule(event, "/view/perfil_usuario.fxml", "My Profile");
    }

    /**
     * Opens the foster home area when the user has activated that profile.
     */
    public void abrirCasasCuna(ActionEvent event) throws IOException {
        openModule(event, "/view/casas_cuna.fxml", "Foster Homes");
    }

    /**
     * Opens the adoption registration and follow-up screen.
     */
    public void abrirAdopciones(ActionEvent event) throws IOException {
        openModule(event, "/view/adopciones.fxml", "Adoptions");
    }

    /**
     * Opens donations. Normal users can donate; admins can also review records.
     */
    public void abrirDonaciones(ActionEvent event) throws IOException {
        openModule(event, "/view/donaciones.fxml", "Donations");
    }

    /**
     * Opens the blacklist report administration screen.
     */
    public void abrirListaNegra(ActionEvent event) throws IOException {
        openModule(event, "/view/lista_negra.fxml", "Blacklist");
    }

    /**
     * Opens the catalog maintenance screen for admin-managed values.
     */
    public void abrirParametros(ActionEvent event) throws IOException {
        openModule(event, "/view/parametros.fxml", "Parameters");
    }

    /**
     * Opens the association management screen.
     */
    public void abrirAsociaciones(ActionEvent event) throws IOException {
        openModule(event, "/view/asociaciones.fxml", "Associations");
    }

    /**
     * Opens the audit log, where system changes can be reviewed.
     */
    public void abrirBitacora(ActionEvent event) throws IOException {
        openModule(event, "/view/bitacora.fxml", "Audit log");
    }

    /**
     * Opens charts and summarized numbers about the system.
     */
    public void abrirEstadisticas(ActionEvent event) throws IOException {
        openModule(event, "/view/estadisticas.fxml", "Statistics");
    }

    /**
     * Opens predefined database reports.
     */
    public void abrirReportes(ActionEvent event) throws IOException {
        openModule(event, "/view/reportes.fxml", "Queries and Reports");
    }

    /**
     * Ends the current session and returns to login without closing the program.
     */
    public void cerrarSesion(ActionEvent event) throws IOException {
        SessionContext.clear();
        NavigationUtil.openWindow(event, "/view/login.fxml", "BDP1 - Animal Welfare");
    }

    /**
     * Closes the desktop window completely.
     */
    public void salirSistema(ActionEvent event) {
        SessionContext.clear();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
