package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.CatalogRepository;
import cr.tec.bd.crv.database.PetRepository;
import cr.tec.bd.crv.model.CatalogOption;
import cr.tec.bd.crv.model.PetFormData;
import cr.tec.bd.crv.util.NavigationUtil;
import cr.tec.bd.crv.util.SessionContext;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for the pet registration screen.
 */
public class RegistrarMascotaController {

    private final CatalogRepository catalogRepository = new CatalogRepository();
    private final PetRepository petRepository = new PetRepository();

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<CatalogOption> cbTipo;

    @FXML
    private ComboBox<CatalogOption> cbRaza;

    @FXML
    private ComboBox<CatalogOption> cbEstado;

    @FXML
    private ComboBox<CatalogOption> cbColor;

    @FXML
    private TextField txtChip;

    @FXML
    private ComboBox<CatalogOption> cbTamano;

    @FXML
    private ComboBox<String> cbEnergia;

    @FXML
    private ComboBox<CatalogOption> cbProvincia;

    @FXML
    private ComboBox<CatalogOption> cbCanton;

    @FXML
    private ComboBox<CatalogOption> cbDistrito;

    @FXML
    private DatePicker dpEvento;

    @FXML
    private TextField txtTelefonoContacto;

    @FXML
    private TextField txtCorreoContacto;

    @FXML
    private ComboBox<CatalogOption> cbEntrenamiento;

    @FXML
    private TextField txtEspacio;

    @FXML
    private ComboBox<CatalogOption> cbMoneda;

    @FXML
    private TextField txtRecompensa;

    @FXML
    private TextField txtFotoAntes;

    @FXML
    private TextField txtFotoDespues;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        cbEnergia.setItems(FXCollections.observableArrayList(
                "Athletic",
                "Runner",
                "Walker",
                "Calm",
                "Couch companion"
        ));

        configureDependentSelectors();
        loadCatalogs();
    }

    @FXML
    public void guardarMascota() {
        try {
            List<String> warnings = petRepository.savePet(buildFormData(), SessionContext.getCurrentPersonId());
            if (warnings.isEmpty()) {
                lblMensaje.setText("Mascota guardada correctamente.");
            } else {
                lblMensaje.setText("Mascota guardada. Revise: " + String.join(" ", warnings));
            }
        } catch (IllegalArgumentException e) {
            lblMensaje.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensaje.setText("No se pudo guardar la mascota: " + e.getMessage());
        }
    }

    @FXML
    public void limpiarFormulario() {
        clearFields(
                txtNombre,
                txtChip,
                txtTelefonoContacto,
                txtCorreoContacto,
                txtEspacio,
                txtRecompensa,
                txtFotoAntes,
                txtFotoDespues,
                txtDescripcion
        );
        clearSelection(cbTipo, cbRaza, cbEstado, cbColor, cbTamano, cbProvincia, cbCanton, cbDistrito, cbEntrenamiento, cbMoneda);
        cbEnergia.getSelectionModel().clearSelection();
        dpEvento.setValue(null);
        lblMensaje.setText("");
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        String menuPath = SessionContext.isAdmin() ? "/view/admin_menu.fxml" : "/view/menu.fxml";
        NavigationUtil.openWindow(event, menuPath, "BDP1 - Bienestar Animal");
    }

    @FXML
    public void abrirListaMascotas(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/lista_mascotas.fxml", "Lista de Mascotas");
    }

    private void loadCatalogs() {
        try {
            cbTipo.setItems(FXCollections.observableArrayList(catalogRepository.findPetTypes()));
            cbEstado.setItems(FXCollections.observableArrayList(catalogRepository.findPetStatuses()));
            cbColor.setItems(FXCollections.observableArrayList(catalogRepository.findColors()));
            cbTamano.setItems(FXCollections.observableArrayList(catalogRepository.findPetSizes()));
            cbMoneda.setItems(FXCollections.observableArrayList(catalogRepository.findCurrencies()));
            cbEntrenamiento.setItems(FXCollections.observableArrayList(catalogRepository.findTrainingEases()));
            cbProvincia.setItems(FXCollections.observableArrayList(catalogRepository.findProvinces()));
        } catch (SQLException e) {
            lblMensaje.setText("No se pudieron cargar los catalogos: " + e.getMessage());
        }
    }

    private void configureDependentSelectors() {
        cbTipo.valueProperty().addListener((observable, oldValue, newValue) -> {
            cbRaza.getItems().clear();
            if (newValue != null) {
                loadBreeds(newValue.getId());
            }
        });

        cbProvincia.valueProperty().addListener((observable, oldValue, newValue) -> {
            cbCanton.getItems().clear();
            cbDistrito.getItems().clear();
            if (newValue != null) {
                loadCantons(newValue.getId());
            }
        });

        cbCanton.valueProperty().addListener((observable, oldValue, newValue) -> {
            cbDistrito.getItems().clear();
            if (newValue != null) {
                loadDistricts(newValue.getId());
            }
        });
    }

    private void loadBreeds(long petTypeId) {
        try {
            cbRaza.setItems(FXCollections.observableArrayList(catalogRepository.findBreedsByPetType(petTypeId)));
        } catch (SQLException e) {
            lblMensaje.setText("No se pudieron cargar las razas: " + e.getMessage());
        }
    }

    private void loadCantons(long provinceId) {
        try {
            cbCanton.setItems(FXCollections.observableArrayList(catalogRepository.findCantons(provinceId)));
        } catch (SQLException e) {
            lblMensaje.setText("No se pudieron cargar los cantones: " + e.getMessage());
        }
    }

    private void loadDistricts(long cantonId) {
        try {
            cbDistrito.setItems(FXCollections.observableArrayList(catalogRepository.findDistricts(cantonId)));
        } catch (SQLException e) {
            lblMensaje.setText("No se pudieron cargar los distritos: " + e.getMessage());
        }
    }

    private PetFormData buildFormData() {
        return new PetFormData(
                valueOf(txtNombre),
                idOf(cbTipo),
                idOf(cbRaza),
                idOf(cbEstado),
                idOf(cbEntrenamiento),
                idOf(cbDistrito),
                idOf(cbMoneda),
                idOf(cbColor),
                valueOf(txtChip),
                idOf(cbTamano),
                valueOf(txtEspacio),
                cbEnergia.getValue(),
                valueOf(txtTelefonoContacto),
                valueOf(txtCorreoContacto),
                parseAmount(valueOf(txtRecompensa)),
                dpEvento.getValue(),
                valueOf(txtFotoAntes),
                valueOf(txtFotoDespues),
                valueOf(txtDescripcion)
        );
    }

    private BigDecimal parseAmount(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            return null;
        }

        try {
            return new BigDecimal(amount.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El monto de recompensa debe ser numerico.");
        }
    }

    private Long idOf(ComboBox<CatalogOption> comboBox) {
        CatalogOption selected = comboBox.getValue();
        return selected == null ? null : selected.getId();
    }

    private String valueOf(TextInputControl field) {
        return field == null ? null : field.getText();
    }

    private void clearFields(TextInputControl... fields) {
        for (TextInputControl field : fields) {
            if (field != null) {
                field.clear();
            }
        }
    }

    @SafeVarargs
    private void clearSelection(ComboBox<CatalogOption>... comboBoxes) {
        for (ComboBox<CatalogOption> comboBox : comboBoxes) {
            comboBox.getSelectionModel().clearSelection();
        }
    }
}
