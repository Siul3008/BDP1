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
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Label lblMensaje;

    @FXML
    public void iniciarSesion(ActionEvent event) throws IOException {
        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();

        if (usuario.equals("admin") && contrasena.equals("1234")) {
            lblMensaje.setText("Inicio de sesión correcto");
            NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos");
        }
    }

    @FXML
    public void limpiarCampos() {
        txtUsuario.clear();
        txtContrasena.clear();
        lblMensaje.setText("");
    }
}

