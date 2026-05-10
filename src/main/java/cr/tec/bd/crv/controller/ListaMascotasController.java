package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.CatalogRepository;
import cr.tec.bd.crv.database.FosterHomeRepository;
import cr.tec.bd.crv.database.PetRepository;
import cr.tec.bd.crv.model.CatalogOption;
import cr.tec.bd.crv.model.Mascota;
import cr.tec.bd.crv.model.PetSearchCriteria;
import cr.tec.bd.crv.util.NavigationUtil;
import cr.tec.bd.crv.util.SessionContext;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
 * Controls the pet list screen.
 *
 * <p>This screen lets users review pets, focus on their own publications, edit
 * pets they control, change allowed pet statuses, and optionally transfer a pet
 * publication to an active foster home.</p>
 */
public class ListaMascotasController {

    private final CatalogRepository catalogRepository = new CatalogRepository();
    private final FosterHomeRepository fosterHomeRepository = new FosterHomeRepository();
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
    private ComboBox<CatalogOption> cbNuevoEstado;

    @FXML
    private ComboBox<CatalogOption> cbCasaCuna;

    @FXML
    private Button btnTransferirControl;

    @FXML
    private CheckBox chkTransferirCasaCuna;

    @FXML
    private CheckBox chkSoloMisMascotas;

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
        // The table and selectors must be ready before the first pet search runs.
        configureColumns();
        loadStatuses();
        loadFosterHomes();
        configurarTransferenciaCasaCuna();
        loadPets();
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        String menuPath = SessionContext.isAdmin() ? "/view/admin_menu.fxml" : "/view/menu.fxml";
        NavigationUtil.openWindow(event, menuPath, "BDP1 - Animal Welfare");
    }

    @FXML
    public void abrirRegistro(ActionEvent event) throws IOException {
        SessionContext.setEditingPetId(null);
        NavigationUtil.openWindow(event, "/view/registrar_mascota.fxml", "Register Pet");
    }

    @FXML
    public void editarMascotaSeleccionada(ActionEvent event) throws IOException {
        // The selected id is stored temporarily so the registration form opens in edit mode.
        Mascota selectedPet = tablaMascotas.getSelectionModel().getSelectedItem();
        if (selectedPet == null) {
            lblMensaje.setText("Select a pet from the list.");
            return;
        }

        SessionContext.setEditingPetId((long) selectedPet.getId());
        NavigationUtil.openWindow(event, "/view/registrar_mascota.fxml", "Edit Pet");
    }

    @FXML
    public void abrirEstadisticas(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/estadisticas.fxml", "Statistics");
    }

    @FXML
    public void cambiarEstadoMascota() {
        // Status changes are checked again in the repository so users cannot bypass permissions.
        Mascota selectedPet = tablaMascotas.getSelectionModel().getSelectedItem();
        CatalogOption selectedStatus = cbNuevoEstado.getValue();

        if (selectedPet == null) {
            lblMensaje.setText("Select a pet from the list.");
            return;
        }

        if (selectedStatus == null) {
            lblMensaje.setText("Select the new status.");
            return;
        }

        try {
            petRepository.updatePetStatus(
                    selectedPet.getId(),
                    selectedStatus.getId(),
                    SessionContext.getCurrentPersonId(),
                    SessionContext.isAdmin()
            );
            loadPets();
            lblMensaje.setText("Status updated successfully.");
        } catch (IllegalArgumentException e) {
            lblMensaje.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensaje.setText("Could not change the status: " + e.getMessage());
        }
    }

    @FXML
    public void recargarMascotas() {
        loadPets();
    }

    @FXML
    public void configurarTransferenciaCasaCuna() {
        // The foster home selector stays disabled unless the user clearly asks to transfer control.
        boolean transferEnabled = chkTransferirCasaCuna != null && chkTransferirCasaCuna.isSelected();
        cbCasaCuna.setDisable(!transferEnabled);
        btnTransferirControl.setDisable(!transferEnabled);
        if (!transferEnabled) {
            cbCasaCuna.getSelectionModel().clearSelection();
        }
    }

    @FXML
    public void pasarControlCasaCuna() {
        // Transfer is separate from status changes: the user can change status without choosing a foster home.
        if (chkTransferirCasaCuna == null || !chkTransferirCasaCuna.isSelected()) {
            lblMensaje.setText("Use transfer only if you want to move control to a foster home.");
            return;
        }

        Mascota selectedPet = tablaMascotas.getSelectionModel().getSelectedItem();
        CatalogOption selectedFosterHome = cbCasaCuna.getValue();

        if (selectedPet == null) {
            lblMensaje.setText("Select a pet from the list.");
            return;
        }

        if (selectedFosterHome == null) {
            lblMensaje.setText("Select the foster home that will receive control.");
            return;
        }

        try {
            petRepository.transferPetControlToFosterHome(
                    selectedPet.getId(),
                    selectedFosterHome.getId(),
                    SessionContext.getCurrentPersonId(),
                    SessionContext.isAdmin()
            );
            chkSoloMisMascotas.setSelected(false);
            chkTransferirCasaCuna.setSelected(false);
            configurarTransferenciaCasaCuna();
            loadPets();
            lblMensaje.setText("Control transferred to the selected foster home.");
        } catch (IllegalArgumentException e) {
            lblMensaje.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensaje.setText("Could not transfer control: " + e.getMessage());
        }
    }

    private void configureColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colRaza.setCellValueFactory(new PropertyValueFactory<>("raza"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void loadStatuses() {
        try {
            cbNuevoEstado.setItems(FXCollections.observableArrayList(catalogRepository.findPetStatuses()));
        } catch (SQLException e) {
            lblMensaje.setText("Could not load statuses: " + e.getMessage());
        }
    }

    private void loadFosterHomes() {
        try {
            cbCasaCuna.setItems(FXCollections.observableArrayList(fosterHomeRepository.findFosterHomeOptions()));
        } catch (SQLException e) {
            lblMensaje.setText("Could not load foster homes: " + e.getMessage());
        }
    }

    private void loadPets() {
        try {
            // Normal users can filter to the pets they control; admins always have the full view.
            Long ownerFilter = chkSoloMisMascotas != null
                    && chkSoloMisMascotas.isSelected()
                    && !SessionContext.isAdmin()
                    ? SessionContext.getCurrentPersonId()
                    : null;
            List<Mascota> pets = petRepository.findPets(new PetSearchCriteria(null, null, null, null), ownerFilter);
            tablaMascotas.setItems(FXCollections.observableArrayList(pets));
            updateStats(pets);
            lblMensaje.setText("");
        } catch (SQLException e) {
            lblMensaje.setText("Could not load pets: " + e.getMessage());
        }
    }

    private void updateStats(List<Mascota> pets) {
        // These small counters give a quick summary of the rows currently visible in the table.
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
