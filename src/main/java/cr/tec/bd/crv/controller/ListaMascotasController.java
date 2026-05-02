package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.model.Mascota;
import cr.tec.bd.crv.util.NavigationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

/**
 * Controller for the pet list screen.
 */
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

    // PropertyValueFactory reads JavaBean getters from Mascota, such as getNombre().
    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colRaza.setCellValueFactory(new PropertyValueFactory<>("raza"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Sample data keeps the table usable until pet records are loaded from Oracle.
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
        NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
    }

    @FXML
    public void abrirRegistro(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/registrar_mascota.fxml", "Registrar Mascota");
    }

    @FXML
    public void abrirEstadisticas(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/estadisticas.fxml", "Estadísticas");
    }
}
