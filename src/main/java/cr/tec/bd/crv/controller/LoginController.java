package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.util.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtCorreoLoginUsuario;

    @FXML
    private PasswordField txtContrasenaLoginUsuario;

    @FXML
    private TextField txtCorreoLoginAsociacion;

    @FXML
    private PasswordField txtContrasenaLoginAsociacion;

    @FXML
    private Label lblMensajeUsuario;

    @FXML
    private Label lblMensajeAsociacion;

    @FXML
    public void iniciarSesionUsuario(ActionEvent event) throws IOException {
        String email = txtCorreoLoginUsuario.getText().trim();
        String password = txtContrasenaLoginUsuario.getText();

        if (!email.isEmpty() && !password.isEmpty()) {
            lblMensajeUsuario.setText("");
            NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
            return;
        }

        lblMensajeUsuario.setText("Complete correo y contraseña.");
    }

    @FXML
    public void iniciarSesionAsociacion(ActionEvent event) throws IOException {
        String email = txtCorreoLoginAsociacion.getText().trim();
        String password = txtContrasenaLoginAsociacion.getText();

        if (!email.isEmpty() && !password.isEmpty()) {
            lblMensajeAsociacion.setText("");
            NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
            return;
        }

        lblMensajeAsociacion.setText("Complete correo y contraseña.");
    }

    @FXML
    public void registrarUsuario() {
        lblMensajeUsuario.setText("Formulario listo para crear cuenta de usuario.");
    }

    @FXML
    public void registrarAsociacion() {
        lblMensajeAsociacion.setText("Formulario listo para crear cuenta de asociación.");
    }

    @FXML
    public void limpiarUsuario() {
        txtCorreoLoginUsuario.clear();
        txtContrasenaLoginUsuario.clear();
        lblMensajeUsuario.setText("");
    }

    @FXML
    public void limpiarAsociacion() {
        txtCorreoLoginAsociacion.clear();
        txtContrasenaLoginAsociacion.clear();
        lblMensajeAsociacion.setText("");
    }
}

