package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class BuscarMascotaController {

    public Label lblResultado;

    public void buscarMascota() {
        lblResultado.setText("Búsqueda realizada (modo prueba)");
    }

    public void volverMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/view/menu.fxml"));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource("/styles/style.css").toExternalForm());
        stage.setTitle("BDP1 - Sistema de Mascotas");
        stage.setScene(scene);
        stage.show();
    }
}