package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.FosterHomeRepository;
import cr.tec.bd.crv.database.UserProfileRepository;
import cr.tec.bd.crv.model.UserProfile;
import cr.tec.bd.crv.util.NavigationUtil;
import cr.tec.bd.crv.util.SessionContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controls the user's profile screen.
 *
 * <p>The profile is split into simple sections, similar to settings pages in
 * common apps: personal data, contact data, password changes, and foster home
 * activation. Each sidebar option only shows the section the user wants to edit.</p>
 */
public class PerfilUsuarioController {

    private final FosterHomeRepository fosterHomeRepository = new FosterHomeRepository();
    private final UserProfileRepository userProfileRepository = new UserProfileRepository();

    @FXML
    private Label lblPerfilNombre;

    @FXML
    private Label lblPerfilCorreo;

    @FXML
    private Label lblEstadoCasaCuna;

    @FXML
    private Label lblMensajePerfil;

    @FXML
    private Button btnCasaCuna;

    @FXML
    private VBox paneCuenta;

    @FXML
    private VBox paneContacto;

    @FXML
    private VBox paneSeguridad;

    @FXML
    private VBox paneCasaCuna;

    @FXML
    private TextField txtPrimerNombre;

    @FXML
    private TextField txtSegundoNombre;

    @FXML
    private TextField txtPrimerApellido;

    @FXML
    private TextField txtSegundoApellido;

    @FXML
    private TextField txtIdentificacion;

    @FXML
    private TextField txtCorreoPrincipal;

    @FXML
    private TextField txtTelefonoPrincipal;

    @FXML
    private PasswordField txtContrasenaActual;

    @FXML
    private PasswordField txtContrasenaNueva;

    @FXML
    private PasswordField txtConfirmarContrasena;

    @FXML
    public void initialize() {
        // Start on the account section because it contains the most basic profile information.
        showSection(paneCuenta);
        loadProfile();
        loadFosterHomeState();
    }

    @FXML
    public void mostrarCuenta() {
        showSection(paneCuenta);
    }

    @FXML
    public void mostrarContacto() {
        showSection(paneContacto);
    }

    @FXML
    public void mostrarSeguridad() {
        showSection(paneSeguridad);
    }

    @FXML
    public void mostrarCasaCuna() {
        showSection(paneCasaCuna);
    }

    @FXML
    public void guardarCuenta() {
        // Names are separated because the database stores each part in its own column.
        Long personId = SessionContext.getCurrentPersonId();
        if (personId == null) {
            lblMensajePerfil.setText("You must sign in as a user.");
            return;
        }

        try {
            userProfileRepository.updateNames(
                    personId,
                    txtPrimerNombre.getText(),
                    txtSegundoNombre.getText(),
                    txtPrimerApellido.getText(),
                    txtSegundoApellido.getText()
            );
            loadProfile();
            lblMensajePerfil.setText("Personal data updated.");
        } catch (IllegalArgumentException e) {
            lblMensajePerfil.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajePerfil.setText("Could not update the account: " + e.getMessage());
        }
    }

    @FXML
    public void guardarContacto() {
        // Updating the main email also updates the active session so later audit rows use the new email.
        Long personId = SessionContext.getCurrentPersonId();
        if (personId == null) {
            lblMensajePerfil.setText("You must sign in as a user.");
            return;
        }

        try {
            userProfileRepository.updateContact(
                    personId,
                    txtCorreoPrincipal.getText(),
                    txtTelefonoPrincipal.getText()
            );
            SessionContext.updateCurrentEmail(txtCorreoPrincipal.getText().trim());
            loadProfile();
            lblMensajePerfil.setText("Contact updated.");
        } catch (IllegalArgumentException e) {
            lblMensajePerfil.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajePerfil.setText("Could not update the contact: " + e.getMessage());
        }
    }

    @FXML
    public void cambiarContrasena() {
        // The repository checks the current password before replacing it with the new hash.
        Long personId = SessionContext.getCurrentPersonId();
        if (personId == null) {
            lblMensajePerfil.setText("You must sign in as a user.");
            return;
        }

        try {
            userProfileRepository.updatePassword(
                    personId,
                    txtContrasenaActual.getText(),
                    txtContrasenaNueva.getText(),
                    txtConfirmarContrasena.getText()
            );
            txtContrasenaActual.clear();
            txtContrasenaNueva.clear();
            txtConfirmarContrasena.clear();
            lblMensajePerfil.setText("Password updated.");
        } catch (IllegalArgumentException e) {
            lblMensajePerfil.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajePerfil.setText("Could not update the password: " + e.getMessage());
        }
    }

    @FXML
    public void abrirCasaCuna(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/casas_cuna.fxml", "Foster Home");
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Animal Welfare");
    }

    private void loadProfile() {
        // Refreshing after each save keeps the labels and form fields synchronized with the database.
        Long personId = SessionContext.getCurrentPersonId();
        if (personId == null) {
            lblMensajePerfil.setText("You must sign in as a user.");
            return;
        }

        try {
            UserProfile profile = userProfileRepository.findProfile(personId);
            lblPerfilNombre.setText(profile.getDisplayName());
            lblPerfilCorreo.setText(profile.getPrimaryEmail());
            txtPrimerNombre.setText(valueOrEmpty(profile.getFirstName()));
            txtSegundoNombre.setText(valueOrEmpty(profile.getSecondName()));
            txtPrimerApellido.setText(valueOrEmpty(profile.getFirstLastName()));
            txtSegundoApellido.setText(valueOrEmpty(profile.getSecondLastName()));
            txtIdentificacion.setText(valueOrEmpty(profile.getIdentification()));
            txtCorreoPrincipal.setText(valueOrEmpty(profile.getPrimaryEmail()));
            txtTelefonoPrincipal.setText(valueOrEmpty(profile.getPrimaryPhone()));
            lblMensajePerfil.setText("");
        } catch (SQLException e) {
            lblMensajePerfil.setText("Could not load the profile: " + e.getMessage());
        }
    }

    private void loadFosterHomeState() {
        try {
            // The button text tells the user whether they are activating or editing foster home settings.
            boolean active = fosterHomeRepository.isFosterHome(SessionContext.getCurrentPersonId());
            lblEstadoCasaCuna.setText(active
                    ? "Your profile is already active as a foster home."
                    : "Activate foster home only if you can host pets temporarily.");
            btnCasaCuna.setText(active ? "Configure foster home" : "Activate foster home");
        } catch (SQLException e) {
            lblEstadoCasaCuna.setText("Could not check foster home: " + e.getMessage());
        }
    }

    private void showSection(VBox selectedPane) {
        // Only one content panel is visible at a time, which keeps the profile screen easy to scan.
        setPaneVisible(paneCuenta, selectedPane == paneCuenta);
        setPaneVisible(paneContacto, selectedPane == paneContacto);
        setPaneVisible(paneSeguridad, selectedPane == paneSeguridad);
        setPaneVisible(paneCasaCuna, selectedPane == paneCasaCuna);
        if (lblMensajePerfil != null) {
            lblMensajePerfil.setText("");
        }
    }

    private void setPaneVisible(VBox pane, boolean visible) {
        if (pane != null) {
            pane.setVisible(visible);
            pane.setManaged(visible);
        }
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
