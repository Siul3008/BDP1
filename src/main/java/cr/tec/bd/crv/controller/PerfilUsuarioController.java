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
 * Controller for the user's profile sections.
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
        Long personId = SessionContext.getCurrentPersonId();
        if (personId == null) {
            lblMensajePerfil.setText("Debe iniciar sesion como usuario.");
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
            lblMensajePerfil.setText("Datos personales actualizados.");
        } catch (IllegalArgumentException e) {
            lblMensajePerfil.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajePerfil.setText("No se pudo actualizar la cuenta: " + e.getMessage());
        }
    }

    @FXML
    public void guardarContacto() {
        Long personId = SessionContext.getCurrentPersonId();
        if (personId == null) {
            lblMensajePerfil.setText("Debe iniciar sesion como usuario.");
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
            lblMensajePerfil.setText("Contacto actualizado.");
        } catch (IllegalArgumentException e) {
            lblMensajePerfil.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajePerfil.setText("No se pudo actualizar el contacto: " + e.getMessage());
        }
    }

    @FXML
    public void cambiarContrasena() {
        Long personId = SessionContext.getCurrentPersonId();
        if (personId == null) {
            lblMensajePerfil.setText("Debe iniciar sesion como usuario.");
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
            lblMensajePerfil.setText("Contrasena actualizada.");
        } catch (IllegalArgumentException e) {
            lblMensajePerfil.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajePerfil.setText("No se pudo actualizar la contrasena: " + e.getMessage());
        }
    }

    @FXML
    public void abrirCasaCuna(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/casas_cuna.fxml", "Casa Cuna");
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
    }

    private void loadProfile() {
        Long personId = SessionContext.getCurrentPersonId();
        if (personId == null) {
            lblMensajePerfil.setText("Debe iniciar sesion como usuario.");
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
            lblMensajePerfil.setText("No se pudo cargar el perfil: " + e.getMessage());
        }
    }

    private void loadFosterHomeState() {
        try {
            boolean active = fosterHomeRepository.isFosterHome(SessionContext.getCurrentPersonId());
            lblEstadoCasaCuna.setText(active
                    ? "Tu perfil ya esta activo como casa cuna."
                    : "Activa casa cuna solo si puedes albergar mascotas temporalmente.");
            btnCasaCuna.setText(active ? "Configurar casa cuna" : "Activar casa cuna");
        } catch (SQLException e) {
            lblEstadoCasaCuna.setText("No se pudo revisar casa cuna: " + e.getMessage());
        }
    }

    private void showSection(VBox selectedPane) {
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
