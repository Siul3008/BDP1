package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.Main;
import cr.tec.bd.crv.database.ConexionBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class MenuController {

    private void cambiarVentana(ActionEvent event, String rutaFXML, String titulo) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource(rutaFXML));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource("/styles/style.css").toExternalForm());
        stage.setTitle(titulo);
        stage.setScene(scene);
        stage.show();
    }

    public void abrirRegistrarMascota(ActionEvent event) throws IOException {
        cambiarVentana(event, "/view/registrar_mascota.fxml", "Registrar Mascota");
    }

    public void abrirBuscarMascota(ActionEvent event) throws IOException {
        cambiarVentana(event, "/view/buscar_mascota.fxml", "Buscar Mascota");
    }

    public void probarConexion() {
        try (Connection conexion = ConexionBD.conectar()) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Conexión");
            alerta.setHeaderText(null);
            alerta.setContentText("Conexión exitosa con Oracle");
            alerta.showAndWait();
        } catch (SQLException e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de conexión");
            alerta.setHeaderText("No se pudo conectar a la base de datos");
            alerta.setContentText(e.getMessage());
            alerta.showAndWait();
        }
    }

    public void salirSistema(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}