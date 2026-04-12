package cr.tec.bd.crv.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Label lblMensaje;

    @FXML
    public void iniciarSesion() {
        String usuario = txtUsuario.getText();
        String contrasena = txtContrasena.getText();

        if (usuario.equals("admin") && contrasena.equals("1234")) {
            lblMensaje.setText("Inicio de sesión correcto");
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos");
        }
    }
}
