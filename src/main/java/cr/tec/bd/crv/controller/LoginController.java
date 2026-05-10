package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.AuthRepository;
import cr.tec.bd.crv.model.AuthenticatedAccount;
import cr.tec.bd.crv.model.UserRegistrationData;
import cr.tec.bd.crv.util.NavigationUtil;
import cr.tec.bd.crv.util.SessionContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controls login and user registration.
 *
 * <p>This controller is reused by the login and registration FXML files. Some
 * fields are only present in one screen, so helper methods check for null before
 * clearing or reading optional controls.</p>
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

    // Older buttons still call these methods, but the app now uses one shared login screen.
    @FXML
    public void abrirLoginUsuario(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/login.fxml", "BDP1 - Animal Welfare");
    }

    @FXML
    public void abrirLoginAdmin(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/login.fxml", "BDP1 - Animal Welfare");
    }

    @FXML
    public void abrirRegistroUsuario(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/registro_usuario.fxml", "Create account - User");
    }

    @FXML
    public void volverSeleccion(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/login.fxml", "BDP1 - Animal Welfare");
    }

    /**
     * Authenticates the account and opens the correct menu for its role.
     */
    @FXML
    public void iniciarSesion(ActionEvent event) throws IOException {
        try {
            AuthenticatedAccount account = authRepository.loginAccount(
                    txtCorreoLoginUsuario.getText(),
                    txtContrasenaLoginUsuario.getText()
            );

            if (account == null) {
                lblMensajeUsuario.setText("Email or password is incorrect.");
                return;
            }

            clearMessage(lblMensajeUsuario);
            if (account.isAdmin()) {
                SessionContext.setAdminSession(account.getLoginEmail());
                NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administration");
                return;
            }

            if (account.isUser() && account.getPersonId() != null) {
                SessionContext.setUserSession(account.getPersonId(), account.getLoginEmail());
                NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Animal Welfare");
                return;
            }

            lblMensajeUsuario.setText("This account does not have an active user profile.");
        } catch (SQLException e) {
            lblMensajeUsuario.setText("Could not sign in: " + e.getMessage());
        }
    }

    /**
     * Compatibility method for older FXML files that still call the user login action.
     */
    @FXML
    public void iniciarSesionUsuario(ActionEvent event) throws IOException {
        iniciarSesion(event);
    }

    /**
     * Compatibility method for older admin login screens.
     */
    @FXML
    public void iniciarSesionAdmin(ActionEvent event) throws IOException {
        try {
            boolean authenticated = authRepository.loginAdmin(
                    txtCorreoLoginAdmin.getText(),
                    txtContrasenaLoginAdmin.getText()
            );

            if (authenticated) {
                clearMessage(lblMensajeAdmin);
                SessionContext.setAdminSession(txtCorreoLoginAdmin.getText().trim());
                NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administration");
                return;
            }

            lblMensajeAdmin.setText("Email or password is incorrect.");
        } catch (SQLException e) {
            lblMensajeAdmin.setText("Could not sign in: " + e.getMessage());
        }
    }

    /**
     * Registers a normal user account and starts a session immediately after saving.
     */
    @FXML
    public void registrarUsuario(ActionEvent event) throws IOException {
        try {
            long personId = authRepository.registerUser(new UserRegistrationData(
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
            SessionContext.setUserSession(personId, txtCorreoLoginUsuario.getText().trim());
            NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Animal Welfare");
        } catch (IllegalArgumentException e) {
            lblMensajeUsuario.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeUsuario.setText("Could not register the user: " + e.getMessage());
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

}
