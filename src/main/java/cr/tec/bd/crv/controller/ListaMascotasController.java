package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.Main;
import cr.tec.bd.crv.model.Mascota;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class ListaMascotasController {

    @FXML
    private TableView<Mascota> tablaMascotas;

    @FXML
    private TableColumn<Mascota, Integer> colId;

    @FXML
    private TableColumn<Mascota, String> colNombre;

    @FXML
    private TableColumn<Mascota, String> colTipo;

    @FXML
    private TableColumn<Mascota, String> colRaza;

    @FXML
    private TableColumn<Mascota, String> colColor;

    @FXML
    private TableColumn<Mascota, String> colEstado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colRaza.setCellValueFactory(new PropertyValueFactory<>("raza"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        ObservableList<Mascota> datos = FXCollections.observableArrayList(
                new Mascota(1, "Luna", "Gato", "Siamés", "Blanco", "En adopción"),
                new Mascota(2, "Max", "Perro", "Labrador", "Negro", "Perdido"),
                new Mascota(3, "Coco", "Ave", "Perico", "Verde", "Encontrado"),
                new Mascota(4, "Milo", "Perro", "Pug", "Café", "En adopción")
        );

        tablaMascotas.setItems(datos);
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/view/menu.fxml"));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(Main.class.getResource("/styles/style.css").toExternalForm());
        stage.setTitle("BDP1 - Sistema de Mascotas");
        stage.setScene(scene);
        stage.show();
    }
}