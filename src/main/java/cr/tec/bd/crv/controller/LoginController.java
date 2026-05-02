package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.AuthRepository;
import cr.tec.bd.crv.database.ConexionBD;
import cr.tec.bd.crv.model.UserRegistrationData;
import cr.tec.bd.crv.util.NavigationUtil;
import cr.tec.bd.crv.util.SessionContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Handles account selection, login, registration, and database connection checks.
 */
public class LoginController {

    private final AuthRepository authRepository = new AuthRepository();

    // The same controller is reused by the selector, login, and registration FXML files.
    // Fields that do not exist in the current FXML simply remain null.
    @FXML
    private TextField txtCorreoLoginUsuario;

    @FXML
    private PasswordField txtContrasenaLoginUsuario;

    @FXML
    private TextField txtPrimerNombreUsuario;

    @FXML
    private TextField txtSegundoNombreUsuario;

    @FXML
    private TextField txtPrimerApellidoUsuario;

    @FXML
    private TextField txtSegundoApellidoUsuario;

    @FXML
    private TextField txtIdentificacionUsuario;

    @FXML
    private TextField txtTelefonoUsuario;

    @FXML
    private TextField txtTelefonoSecundarioUsuario;

    @FXML
    private TextField txtCorreoSecundarioUsuario;

    @FXML
    private TextField txtCorreoLoginAdmin;

    @FXML
    private PasswordField txtContrasenaLoginAdmin;

    @FXML
    private Label lblMensajeUsuario;

    @FXML
    private Label lblMensajeAdmin;

    // Navigation between the separated authentication screens.
    @FXML
    public void abrirLoginUsuario(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/login_usuario.fxml", "Inicio de sesion - Usuario");
    }

    @FXML
    public void abrirLoginAdmin(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/login_admin.fxml", "Inicio de sesion - Admin");
    }

    @FXML
    public void abrirRegistroUsuario(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/registro_usuario.fxml", "Crear cuenta - Usuario");
    }

    @FXML
    public void volverSeleccion(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/login.fxml", "BDP1 - Bienestar Animal");
    }

    // Quick connection test used during setup and demonstrations.
    @FXML
    public void probarConexion() {
        try (Connection connection = ConexionBD.conectar()) {
            showAlert(Alert.AlertType.INFORMATION, "Conexion", "Conexion exitosa con Oracle");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de conexion", e.getMessage());
        }
    }

    // User login routes to the regular application menu.
    @FXML
    public void iniciarSesionUsuario(ActionEvent event) throws IOException {
        try {
            boolean authenticated = authRepository.loginUser(
                    txtCorreoLoginUsuario.getText(),
                    txtContrasenaLoginUsuario.getText()
            );

            if (authenticated) {
                clearMessage(lblMensajeUsuario);
                SessionContext.setRole("USER");
                NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
                return;
            }

            lblMensajeUsuario.setText("Correo o contrasena incorrectos.");
        } catch (SQLException e) {
            lblMensajeUsuario.setText("No se pudo iniciar sesion: " + e.getMessage());
        }
    }

    // Admin login routes to a restricted menu for system management tasks.
    @FXML
    public void iniciarSesionAdmin(ActionEvent event) throws IOException {
        try {
            boolean authenticated = authRepository.loginAdmin(
                    txtCorreoLoginAdmin.getText(),
                    txtContrasenaLoginAdmin.getText()
            );

            if (authenticated) {
                clearMessage(lblMensajeAdmin);
                SessionContext.setRole("ADMIN");
                NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administracion");
                return;
            }

            lblMensajeAdmin.setText("Correo o contrasena incorrectos.");
        } catch (SQLException e) {
            lblMensajeAdmin.setText("No se pudo iniciar sesion: " + e.getMessage());
        }
    }

    // New user registration creates the person, adopter profile, contact data, and app account.
    @FXML
    public void registrarUsuario(ActionEvent event) throws IOException {
        try {
            authRepository.registerUser(new UserRegistrationData(
                    txtPrimerNombreUsuario.getText(),
                    txtSegundoNombreUsuario.getText(),
                    txtPrimerApellidoUsuario.getText(),
                    txtSegundoApellidoUsuario.getText(),
                    txtIdentificacionUsuario.getText(),
                    txtCorreoLoginUsuario.getText(),
                    txtCorreoSecundarioUsuario.getText(),
                    txtTelefonoUsuario.getText(),
                    txtTelefonoSecundarioUsuario.getText(),
                    txtContrasenaLoginUsuario.getText()
            ));
            clearMessage(lblMensajeUsuario);
            SessionContext.setRole("USER");
            NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
        } catch (IllegalArgumentException e) {
            lblMensajeUsuario.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeUsuario.setText("No se pudo registrar el usuario: " + e.getMessage());
        }
    }

    // Clear helpers are null-safe because this controller is shared across several FXML screens.
    @FXML
    public void limpiarUsuario() {
        clearFields(
                txtCorreoLoginUsuario,
                txtContrasenaLoginUsuario,
                txtPrimerNombreUsuario,
                txtSegundoNombreUsuario,
                txtPrimerApellidoUsuario,
                txtSegundoApellidoUsuario,
                txtIdentificacionUsuario,
                txtTelefonoUsuario,
                txtTelefonoSecundarioUsuario,
                txtCorreoSecundarioUsuario
        );
        clearMessage(lblMensajeUsuario);
    }

    @FXML
    public void limpiarAdmin() {
        clearFields(txtCorreoLoginAdmin, txtContrasenaLoginAdmin);
        clearMessage(lblMensajeAdmin);
    }

    private void clearFields(TextInputControl... fields) {
        for (TextInputControl field : fields) {
            if (field != null) {
                field.clear();
            }
        }
    }

    private void clearMessage(Label label) {
        if (label != null) {
            label.setText("");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
