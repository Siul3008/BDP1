package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.PetRepository;
import cr.tec.bd.crv.model.Mascota;
import cr.tec.bd.crv.model.PetSearchCriteria;
import cr.tec.bd.crv.util.NavigationUtil;
import cr.tec.bd.crv.util.SessionContext;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * Controller for the pet list screen.
 */
public class ListaMascotasController {

    private final PetRepository petRepository = new PetRepository();

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
    private Label lblTotalMascotas;

    @FXML
    private Label lblAdopcion;

    @FXML
    private Label lblPerdidas;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        configureColumns();
        loadPets();
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        String menuPath = SessionContext.isAdmin() ? "/view/admin_menu.fxml" : "/view/menu.fxml";
        NavigationUtil.openWindow(event, menuPath, "BDP1 - Bienestar Animal");
    }

    @FXML
    public void abrirRegistro(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/registrar_mascota.fxml", "Registrar Mascota");
    }

    @FXML
    public void abrirEstadisticas(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/estadisticas.fxml", "Estadisticas");
    }

    private void configureColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colRaza.setCellValueFactory(new PropertyValueFactory<>("raza"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void loadPets() {
        try {
            List<Mascota> pets = petRepository.findPets(new PetSearchCriteria(null, null, null, null));
            tablaMascotas.setItems(FXCollections.observableArrayList(pets));
            updateStats(pets);
            lblMensaje.setText("");
        } catch (SQLException e) {
            lblMensaje.setText("No se pudieron cargar mascotas: " + e.getMessage());
        }
    }

    private void updateStats(List<Mascota> pets) {
        long adoption = countByStatusText(pets, "adop");
        long lost = countByStatusText(pets, "lost") + countByStatusText(pets, "perd");

        lblTotalMascotas.setText(String.valueOf(pets.size()));
        lblAdopcion.setText(String.valueOf(adoption));
        lblPerdidas.setText(String.valueOf(lost));
    }

    private long countByStatusText(List<Mascota> pets, String text) {
        return pets.stream()
                .map(Mascota::getEstado)
                .filter(status -> status != null && status.toLowerCase(Locale.ROOT).contains(text))
                .count();
    }
}
