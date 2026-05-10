package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.CatalogRepository;
import cr.tec.bd.crv.database.FosterHomeRepository;
import cr.tec.bd.crv.model.CatalogOption;
import cr.tec.bd.crv.model.FosterHomeDirectoryRecord;
import cr.tec.bd.crv.model.FosterHomeProfile;
import cr.tec.bd.crv.util.NavigationUtil;
import cr.tec.bd.crv.util.SessionContext;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controls foster home activation and accepted conditions.
 *
 * <p>A user becomes a foster home by saving what they can temporarily receive:
 * accepted pet types, accepted pet sizes, food donation preference, and notes.
 * Check boxes are used instead of multi-select lists so the user does not need
 * special keyboard shortcuts.</p>
 */
public class CasaCunaController {

    private final CatalogRepository catalogRepository = new CatalogRepository();
    private final FosterHomeRepository fosterHomeRepository = new FosterHomeRepository();

    @FXML
    private VBox boxTiposAceptados;

    @FXML
    private VBox boxTamanosAceptados;

    @FXML
    private ComboBox<CatalogOption> cbDonacionAlimento;

    @FXML
    private TextArea txtNotasCasaCuna;

    @FXML
    private Label lblEstadoCasaCuna;

    @FXML
    private Label lblMensajeCasaCuna;

    @FXML
    private TableView<FosterHomeDirectoryRecord> tablaCasasCuna;

    @FXML
    private TableColumn<FosterHomeDirectoryRecord, String> colCasaNombre;

    @FXML
    private TableColumn<FosterHomeDirectoryRecord, String> colCasaContacto;

    @FXML
    private TableColumn<FosterHomeDirectoryRecord, String> colCasaTipos;

    @FXML
    private TableColumn<FosterHomeDirectoryRecord, String> colCasaTamanos;

    @FXML
    private TableColumn<FosterHomeDirectoryRecord, String> colCasaAlimento;

    @FXML
    private TableColumn<FosterHomeDirectoryRecord, String> colCasaNotas;

    @FXML
    public void initialize() {
        configureDirectoryColumns();
        loadCatalogsAndProfile();
        loadDirectory();
    }

    @FXML
    public void guardarCasaCuna() {
        try {
            // Saving the profile creates the foster home if it does not exist yet.
            fosterHomeRepository.saveProfile(
                    SessionContext.getCurrentPersonId(),
                    idOf(cbDonacionAlimento),
                    idsOf(boxTiposAceptados),
                    idsOf(boxTamanosAceptados),
                    txtNotasCasaCuna.getText()
            );
            lblEstadoCasaCuna.setText("Casa cuna activa.");
            lblMensajeCasaCuna.setText("Condiciones guardadas correctamente.");
            loadDirectory();
        } catch (IllegalArgumentException e) {
            lblMensajeCasaCuna.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeCasaCuna.setText("No se pudo guardar casa cuna: " + e.getMessage());
        }
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
    }

    @FXML
    public void recargarCasasCuna() {
        loadDirectory();
    }

    private void configureDirectoryColumns() {
        colCasaNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCasaContacto.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colCasaTipos.setCellValueFactory(new PropertyValueFactory<>("acceptedTypes"));
        colCasaTamanos.setCellValueFactory(new PropertyValueFactory<>("acceptedSizes"));
        colCasaAlimento.setCellValueFactory(new PropertyValueFactory<>("foodDonation"));
        colCasaNotas.setCellValueFactory(new PropertyValueFactory<>("notes"));
    }

    private void loadCatalogsAndProfile() {
        try {
            // Catalog values come from the database so foster home conditions match real pet options.
            loadCheckOptions(boxTiposAceptados, catalogRepository.findPetTypes());
            loadCheckOptions(boxTamanosAceptados, catalogRepository.findPetSizes());
            cbDonacionAlimento.setItems(FXCollections.observableArrayList(catalogRepository.findFoodDonationOptions()));

            FosterHomeProfile profile = fosterHomeRepository.findProfile(SessionContext.getCurrentPersonId());
            lblEstadoCasaCuna.setText(profile.isActive()
                    ? "Casa cuna activa."
                    : "Completa estas condiciones para activar casa cuna.");
            txtNotasCasaCuna.setText(profile.getNotes() == null ? "" : profile.getNotes());
            selectByIds(boxTiposAceptados, profile.getAcceptedTypeIds());
            selectByIds(boxTamanosAceptados, profile.getAcceptedSizeIds());
            selectComboById(cbDonacionAlimento, profile.getFoodDonationId());
            lblMensajeCasaCuna.setText("");
        } catch (SQLException e) {
            lblMensajeCasaCuna.setText("No se pudieron cargar las condiciones: " + e.getMessage());
        }
    }

    private void loadDirectory() {
        try {
            tablaCasasCuna.setItems(FXCollections.observableArrayList(fosterHomeRepository.findDirectory()));
        } catch (SQLException e) {
            lblMensajeCasaCuna.setText("No se pudieron cargar casas cuna activas: " + e.getMessage());
        }
    }

    private void loadCheckOptions(VBox container, List<CatalogOption> options) {
        // Each database option becomes one visible check box.
        container.getChildren().clear();
        for (CatalogOption option : options) {
            CheckBox checkBox = new CheckBox(option.getLabel());
            checkBox.setUserData(option);
            checkBox.getStyleClass().add("check-option");
            container.getChildren().add(checkBox);
        }
    }

    private Long idOf(ComboBox<CatalogOption> comboBox) {
        CatalogOption selected = comboBox.getValue();
        return selected == null ? null : selected.getId();
    }

    private List<Long> idsOf(VBox container) {
        // The repository only needs the selected database ids, not the visual check boxes themselves.
        return container.getChildren()
                .stream()
                .filter(node -> node instanceof CheckBox checkBox && checkBox.isSelected())
                .map(node -> ((CatalogOption) node.getUserData()).getId())
                .toList();
    }

    private void selectByIds(VBox container, List<Long> ids) {
        // Existing profiles reopen with their previously saved options already checked.
        for (Node node : container.getChildren()) {
            if (node instanceof CheckBox checkBox) {
                CatalogOption option = (CatalogOption) checkBox.getUserData();
                checkBox.setSelected(ids.contains(option.getId()));
            }
        }
    }

    private void selectComboById(ComboBox<CatalogOption> comboBox, Long id) {
        if (id == null) {
            comboBox.getSelectionModel().clearSelection();
            return;
        }

        comboBox.getItems()
                .stream()
                .filter(option -> option.getId() == id)
                .findFirst()
                .ifPresent(comboBox::setValue);
    }
}
