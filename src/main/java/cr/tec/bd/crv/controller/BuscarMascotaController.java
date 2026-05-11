package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.CatalogRepository;
import cr.tec.bd.crv.database.PetRepository;
import cr.tec.bd.crv.database.PetRepository.PetPhotoPair;
import cr.tec.bd.crv.model.CatalogOption;
import cr.tec.bd.crv.model.Mascota;
import cr.tec.bd.crv.model.PetSearchCriteria;
import cr.tec.bd.crv.util.NavigationUtil;
import cr.tec.bd.crv.util.SessionContext;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controls the pet search screen.
 *
 * <p>The user can search by text, status, and event dates. The controller only
 * builds the search request; the repository turns it into the database query.</p>
 */
public class BuscarMascotaController {

    private final CatalogRepository catalogRepository = new CatalogRepository();
    private final PetRepository petRepository = new PetRepository();

    @FXML
    private TextField txtBusqueda;

    @FXML
    private ComboBox<CatalogOption> cbEstado;

    @FXML
    private DatePicker dpDesde;

    @FXML
    private DatePicker dpHasta;

    @FXML
    private TableView<Mascota> tablaResultados;

    @FXML
    private TableColumn<Mascota, String> colMascota;

    @FXML
    private TableColumn<Mascota, String> colEstado;

    @FXML
    private TableColumn<Mascota, String> colLugar;

    @FXML
    private TableColumn<Mascota, String> colFecha;

    @FXML
    private Label lblResultado;

    @FXML
    private ImageView imgPetBeforePreview;

    @FXML
    private ImageView imgPetAfterPreview;

    @FXML
    private Label lblPhotoStatus;

    @FXML
    public void initialize() {
        configureColumns();
        tablaResultados.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, selectedPet) -> loadSelectedPetPhoto(selectedPet));
        loadStatuses();
    }

    @FXML
    public void buscarMascota() {
        try {
            // The criteria object keeps the search values together and easy to pass around.
            List<Mascota> results = petRepository.findPets(buildCriteria());
            tablaResultados.setItems(FXCollections.observableArrayList(results));
            lblResultado.setText(results.size() + " resultado(s) encontrados.");
        } catch (SQLException e) {
            lblResultado.setText("Could not complete the search: " + e.getMessage());
        }
    }

    @FXML
    public void limpiarBusqueda() {
        txtBusqueda.clear();
        cbEstado.getSelectionModel().clearSelection();
        dpDesde.setValue(null);
        dpHasta.setValue(null);
        tablaResultados.getItems().clear();
        clearPhotoPreviews();
        lblPhotoStatus.setText("Select a result to preview its stored photos.");
        lblResultado.setText("");
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        String menuPath = SessionContext.isAdmin() ? "/view/admin_menu.fxml" : "/view/menu.fxml";
        NavigationUtil.openWindow(event, menuPath, "BDP1 - Animal Welfare");
    }

    @FXML
    public void abrirListaMascotas(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/lista_mascotas.fxml", "Pet List");
    }

    @FXML
    public void abrirReportes(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/reportes.fxml", "Queries and Reports");
    }

    private void configureColumns() {
        colMascota.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colLugar.setCellValueFactory(new PropertyValueFactory<>("lugar"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaEvento"));
    }

    private void loadSelectedPetPhoto(Mascota selectedPet) {
        clearPhotoPreviews();
        if (selectedPet == null) {
            lblPhotoStatus.setText("Select a result to preview its stored photos.");
            return;
        }

        try {
            PetPhotoPair photos = petRepository.findPetPhotoPair(selectedPet.getId());
            boolean beforeLoaded = loadPhoto(imgPetBeforePreview, photos.beforePhoto());
            boolean afterLoaded = loadPhoto(imgPetAfterPreview, photos.afterPhoto());
            if (!beforeLoaded && !afterLoaded) {
                lblPhotoStatus.setText("This pet does not have stored photos yet.");
                return;
            }
            lblPhotoStatus.setText(selectedPet.getNombre());
        } catch (SQLException e) {
            lblPhotoStatus.setText("Could not load the photos: " + e.getMessage());
        }
    }

    private boolean loadPhoto(ImageView preview, byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            preview.setImage(null);
            return false;
        }
        preview.setImage(new Image(new ByteArrayInputStream(imageBytes)));
        return true;
    }

    private void clearPhotoPreviews() {
        imgPetBeforePreview.setImage(null);
        imgPetAfterPreview.setImage(null);
    }

    private void loadStatuses() {
        try {
            cbEstado.setItems(FXCollections.observableArrayList(catalogRepository.findPetStatuses()));
        } catch (SQLException e) {
            lblResultado.setText("Could not load statuses: " + e.getMessage());
        }
    }

    private PetSearchCriteria buildCriteria() {
        // Empty fields become null filters, which means "do not restrict by this value".
        CatalogOption selectedStatus = cbEstado.getValue();
        Long statusId = selectedStatus == null ? null : selectedStatus.getId();
        return new PetSearchCriteria(txtBusqueda.getText(), statusId, dpDesde.getValue(), dpHasta.getValue());
    }
}
