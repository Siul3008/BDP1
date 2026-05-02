package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.AuthRepository;
import cr.tec.bd.crv.database.ConexionBD;
import cr.tec.bd.crv.model.AssociationRegistrationData;
import cr.tec.bd.crv.model.UserRegistrationData;
import cr.tec.bd.crv.util.NavigationUtil;
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
    private TextField txtCorreoLoginAsociacion;

    @FXML
    private PasswordField txtContrasenaLoginAsociacion;

    @FXML
    private TextField txtNombreAsociacion;

    @FXML
    private TextField txtIdentificacionAsociacion;

    @FXML
    private Label lblMensajeUsuario;

    @FXML
    private Label lblMensajeAsociacion;

    // Navigation between the separated authentication screens.
    @FXML
    public void abrirLoginUsuario(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/login_usuario.fxml", "Inicio de sesion - Usuario");
    }

    @FXML
    public void abrirLoginAsociacion(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/login_asociacion.fxml", "Inicio de sesion - Asociacion");
    }

    @FXML
    public void abrirRegistroUsuario(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/registro_usuario.fxml", "Crear cuenta - Usuario");
    }

    @FXML
    public void abrirRegistroAsociacion(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/registro_asociacion.fxml", "Crear cuenta - Asociacion");
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

    // Login only asks for the credentials needed to identify an existing user.
    @FXML
    public void iniciarSesionUsuario(ActionEvent event) throws IOException {
        try {
            boolean authenticated = authRepository.loginUser(
                    txtCorreoLoginUsuario.getText(),
                    txtContrasenaLoginUsuario.getText()
            );

            if (authenticated) {
                lblMensajeUsuario.setText("");
                NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
                return;
            }

            lblMensajeUsuario.setText("Correo o contrasena incorrectos.");
        } catch (SQLException e) {
            lblMensajeUsuario.setText("No se pudo iniciar sesion: " + e.getMessage());
        }
    }

    // Association login is separated because associations are stored with a different account type.
    @FXML
    public void iniciarSesionAsociacion(ActionEvent event) throws IOException {
        try {
            boolean authenticated = authRepository.loginAssociation(
                    txtCorreoLoginAsociacion.getText(),
                    txtContrasenaLoginAsociacion.getText()
            );

            if (authenticated) {
                lblMensajeAsociacion.setText("");
                NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
                return;
            }

            lblMensajeAsociacion.setText("Correo o contrasena incorrectos.");
        } catch (SQLException e) {
            lblMensajeAsociacion.setText("No se pudo iniciar sesion: " + e.getMessage());
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
            lblMensajeUsuario.setText("");
            NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
        } catch (IllegalArgumentException e) {
            lblMensajeUsuario.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeUsuario.setText("No se pudo registrar el usuario: " + e.getMessage());
        }
    }

    // New association registration creates the association record and its login account.
    @FXML
    public void registrarAsociacion(ActionEvent event) throws IOException {
        try {
            authRepository.registerAssociation(new AssociationRegistrationData(
                    txtNombreAsociacion.getText(),
                    txtIdentificacionAsociacion.getText(),
                    txtCorreoLoginAsociacion.getText(),
                    txtContrasenaLoginAsociacion.getText()
            ));
            lblMensajeAsociacion.setText("");
            NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
        } catch (IllegalArgumentException e) {
            lblMensajeAsociacion.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeAsociacion.setText("No se pudo registrar la asociacion: " + e.getMessage());
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
    public void limpiarAsociacion() {
        clearFields(
                txtCorreoLoginAsociacion,
                txtContrasenaLoginAsociacion,
                txtNombreAsociacion,
                txtIdentificacionAsociacion
        );
        clearMessage(lblMensajeAsociacion);
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
