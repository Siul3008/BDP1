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
 * Controls the pet registration and pet editing screen.
 *
 * <p>The screen collects everything a user needs to publish a pet: basic data,
 * location, contact information, photos, reward information, and optional health
 * notes. When a pet is being edited, the controller receives the pet id from
 * {@link SessionContext} and loads the existing values into the same form.</p>
 */
public class RegistrarMascotaController {

    private final CatalogRepository catalogRepository = new CatalogRepository();
    private final PetRepository petRepository = new PetRepository();
    private Long editingPetId;

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
    private ComboBox<CatalogOption> cbVeterinario;

    @FXML
    private TextField txtEstadoSalud;

    @FXML
    private ComboBox<CatalogOption> cbEnfermedad;

    @FXML
    private ComboBox<CatalogOption> cbTratamiento;

    @FXML
    private ComboBox<CatalogOption> cbMedicamento;

    @FXML
    private TextField txtDosisMedicamento;

    @FXML
    private TextArea txtDetalleSalud;

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
        configureDependentSelectors();
        loadCatalogs();
        editingPetId = SessionContext.consumeEditingPetId();
        if (editingPetId != null) {
            loadPetForEditing(editingPetId);
        }
    }

    @FXML
    public void guardarMascota() {
        try {
            // A null editing id means "new pet"; otherwise the same form updates the selected pet.
            List<String> warnings;
            if (editingPetId == null) {
                warnings = petRepository.savePet(buildFormData(), SessionContext.getCurrentPersonId());
            } else {
                warnings = petRepository.updatePet(
                        editingPetId,
                        buildFormData(),
                        SessionContext.getCurrentPersonId(),
                        SessionContext.isAdmin()
                );
            }
            if (warnings.isEmpty()) {
                lblMensaje.setText(editingPetId == null
                        ? "Pet saved successfully."
                        : "Pet updated successfully.");
            } else {
                lblMensaje.setText("Pet saved. Review: " + String.join(" ", warnings));
            }
        } catch (IllegalArgumentException e) {
            lblMensaje.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensaje.setText("Could not save the pet: " + e.getMessage());
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
                txtDescripcion,
                txtEstadoSalud,
                txtDosisMedicamento,
                txtDetalleSalud
        );
        clearSelection(
                cbTipo,
                cbRaza,
                cbEstado,
                cbColor,
                cbTamano,
                cbProvincia,
                cbCanton,
                cbDistrito,
                cbEntrenamiento,
                cbMoneda,
                cbVeterinario,
                cbEnfermedad,
                cbTratamiento,
                cbMedicamento
        );
        cbEnergia.getSelectionModel().clearSelection();
        dpEvento.setValue(null);
        lblMensaje.setText("");
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

    private void loadCatalogs() {
        try {
            // Catalogs come from the database so the interface only offers valid existing options.
            cbTipo.setItems(FXCollections.observableArrayList(catalogRepository.findPetTypes()));
            cbEstado.setItems(FXCollections.observableArrayList(catalogRepository.findPetStatuses()));
            cbColor.setItems(FXCollections.observableArrayList(catalogRepository.findColors()));
            cbTamano.setItems(FXCollections.observableArrayList(catalogRepository.findPetSizes()));
            cbMoneda.setItems(FXCollections.observableArrayList(catalogRepository.findCurrencies()));
            cbEntrenamiento.setItems(FXCollections.observableArrayList(catalogRepository.findTrainingEases()));
            cbVeterinario.setItems(FXCollections.observableArrayList(catalogRepository.findVeterinarians()));
            cbEnfermedad.setItems(FXCollections.observableArrayList(catalogRepository.findDiseases()));
            cbTratamiento.setItems(FXCollections.observableArrayList(catalogRepository.findTreatments()));
            cbMedicamento.setItems(FXCollections.observableArrayList(catalogRepository.findMedicines()));
            cbProvincia.setItems(FXCollections.observableArrayList(catalogRepository.findProvinces()));
            cbEnergia.setItems(FXCollections.observableArrayList(catalogRepository.findEnergyLevels()));
        } catch (SQLException e) {
            lblMensaje.setText("Could not load catalogs: " + e.getMessage());
        }
    }

    private void configureDependentSelectors() {
        // Breed, canton, and district lists depend on a previous selection.
        // The user sees fewer options and avoids choosing impossible combinations.
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
            lblMensaje.setText("Could not load breeds: " + e.getMessage());
        }
    }

    private void loadCantons(long provinceId) {
        try {
            cbCanton.setItems(FXCollections.observableArrayList(catalogRepository.findCantons(provinceId)));
        } catch (SQLException e) {
            lblMensaje.setText("Could not load cantons: " + e.getMessage());
        }
    }

    private void loadDistricts(long cantonId) {
        try {
            cbDistrito.setItems(FXCollections.observableArrayList(catalogRepository.findDistricts(cantonId)));
        } catch (SQLException e) {
            lblMensaje.setText("Could not load districts: " + e.getMessage());
        }
    }

    private PetFormData buildFormData() {
        // The repository receives one data object instead of many separate UI controls.
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
                valueOf(txtDescripcion),
                valueOf(txtEstadoSalud),
                valueOf(txtDetalleSalud),
                idOf(cbEnfermedad),
                idOf(cbTratamiento),
                idOf(cbMedicamento),
                valueOf(txtDosisMedicamento),
                idOf(cbVeterinario),
                null
        );
    }

    private void loadPetForEditing(long petId) {
        try {
            // Editing reuses the registration form, so we fill the visible controls with saved values.
            PetFormData data = petRepository.findPetForEdit(petId);
            txtNombre.setText(data.getName());
            selectById(cbTipo, data.getPetTypeId());
            if (data.getPetTypeId() != null) {
                loadBreeds(data.getPetTypeId());
            }
            selectById(cbRaza, data.getBreedId());
            selectById(cbEstado, data.getPetStatusId());
            selectById(cbColor, data.getColorId());
            txtChip.setText(data.getChip());
            selectById(cbTamano, data.getPetSizeId());
            cbEnergia.setValue(data.getEnergyLevel());
            loadLocationForEditing(data.getDistrictId());
            dpEvento.setValue(data.getEventDate());
            txtTelefonoContacto.setText(data.getContactPhone());
            txtCorreoContacto.setText(data.getContactEmail());
            selectById(cbEntrenamiento, data.getTrainingEaseId());
            selectById(cbVeterinario, data.getVeterinarianId());
            txtEstadoSalud.setText(data.getHealthState());
            selectById(cbEnfermedad, data.getDiseaseId());
            selectById(cbTratamiento, data.getTreatmentId());
            selectById(cbMedicamento, data.getMedicineId());
            txtDosisMedicamento.setText(data.getMedicineDose());
            txtDetalleSalud.setText(data.getHealthDescription());
            txtEspacio.setText(data.getNeedSpace());
            selectById(cbMoneda, data.getCurrencyId());
            txtRecompensa.setText(data.getRewardAmount() == null ? "" : data.getRewardAmount().toPlainString());
            txtFotoAntes.setText(data.getPhotoBeforePath());
            txtFotoDespues.setText(data.getPhotoAfterPath());
            txtDescripcion.setText(data.getDescription());
            lblMensaje.setText("Edit mode: update the data and press Save.");
        } catch (SQLException e) {
            lblMensaje.setText("Could not load the pet for editing: " + e.getMessage());
        }
    }

    private BigDecimal parseAmount(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            return null;
        }

        try {
            return new BigDecimal(amount.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Reward amount must be numeric.");
        }
    }

    private Long idOf(ComboBox<CatalogOption> comboBox) {
        CatalogOption selected = comboBox.getValue();
        return selected == null ? null : selected.getId();
    }

    private String valueOf(TextInputControl field) {
        return field == null ? null : field.getText();
    }

    private void selectById(ComboBox<CatalogOption> comboBox, Long id) {
        if (id == null || comboBox == null || comboBox.getItems() == null) {
            return;
        }

        comboBox.getItems().stream()
                .filter(option -> option.getId() == id)
                .findFirst()
                .ifPresent(option -> comboBox.getSelectionModel().select(option));
    }

    private void loadLocationForEditing(Long districtId) throws SQLException {
        if (districtId == null) {
            return;
        }

        Long cantonId = catalogRepository.findCantonIdByDistrict(districtId);
        Long provinceId = cantonId == null ? null : catalogRepository.findProvinceIdByCanton(cantonId);

        selectById(cbProvincia, provinceId);
        if (provinceId != null) {
            loadCantons(provinceId);
        }
        selectById(cbCanton, cantonId);
        if (cantonId != null) {
            loadDistricts(cantonId);
        }
        selectById(cbDistrito, districtId);
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
