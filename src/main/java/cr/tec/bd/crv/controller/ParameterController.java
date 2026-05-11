package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.CatalogRepository;
import cr.tec.bd.crv.database.ParameterRepository;
import cr.tec.bd.crv.model.CatalogOption;
import cr.tec.bd.crv.model.ParameterRecord;
import cr.tec.bd.crv.util.NavigationUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controls administrator catalog maintenance.
 *
 * <p>This screen lets an admin add or update shared values used by other forms:
 * pet types, breeds, colors, currencies, sizes, illnesses, treatments, and
 * similar catalog data.</p>
 */
public class ParameterController {

    private static final String MATCH_JOB_FREQUENCY = "matchJobfreq";

    private final ParameterRepository parameterRepository = new ParameterRepository();
    private final CatalogRepository catalogRepository = new CatalogRepository();

    private Long selectedId;

    @FXML
    private ComboBox<String> cbCategory;

    @FXML
    private ComboBox<CatalogOption> cbPetType;

    @FXML
    private VBox boxPetType;

    @FXML
    private VBox boxExtra;

    @FXML
    private VBox boxDescription;

    @FXML
    private Label lblName;

    @FXML
    private Label lblExtra;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtExtra;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TableView<ParameterRecord> tableParameters;

    @FXML
    private TableColumn<ParameterRecord, Number> colId;

    @FXML
    private TableColumn<ParameterRecord, String> colName;

    @FXML
    private TableColumn<ParameterRecord, String> colExtra;

    @FXML
    private Label lblSummary;

    @FXML
    private Label lblMessage;

    @FXML
    public void initialize() {
        // The form changes depending on the selected category because not all catalogs need the same fields.
        configureColumns();
        cbCategory.setItems(FXCollections.observableArrayList(parameterRepository.findCategories()));
        cbCategory.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedId = null;
            clearFields();
            configureForm(newValue);
            loadRecords();
        });
        tableParameters.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadSelectedRecord(newValue);
            }
        });
        loadPetTypes();
        cbCategory.setValue(ParameterRepository.PET_TYPES);
    }

    @FXML
    public void saveParameter() {
        try {
            // Each category maps to a different table shape, so the save method is chosen here.
            String category = cbCategory.getValue();
            if (ParameterRepository.BREEDS.equals(category)) {
                parameterRepository.saveBreed(selectedId, txtName.getText(), idOf(cbPetType));
            } else if (ParameterRepository.CURRENCIES.equals(category)) {
                parameterRepository.saveCurrency(selectedId, txtName.getText(), txtExtra.getText());
            } else if (ParameterRepository.SYSTEM_PARAMETERS.equals(category)) {
                parameterRepository.saveSystemParameter(
                        selectedId,
                        txtName.getText(),
                        txtDescription.getText(),
                        txtExtra.getText()
                );
            } else {
                parameterRepository.saveSimple(category, selectedId, txtName.getText());
            }

            lblMessage.setText("Parameter saved successfully.");
            if (ParameterRepository.SYSTEM_PARAMETERS.equals(category)
                    && MATCH_JOB_FREQUENCY.equalsIgnoreCase(txtName.getText().trim())) {
                lblMessage.setText("Parameter saved successfully. Match job schedule updated.");
            }
            selectedId = null;
            clearFields();
            if (ParameterRepository.PET_TYPES.equals(category) || ParameterRepository.BREEDS.equals(category)) {
                loadPetTypes();
            }
            loadRecords();
        } catch (IllegalArgumentException e) {
            lblMessage.setText(e.getMessage());
        } catch (SQLException e) {
            lblMessage.setText("Could not save the parameter: " + e.getMessage());
        }
    }

    @FXML
    public void newParameter() {
        selectedId = null;
        tableParameters.getSelectionModel().clearSelection();
        clearFields();
        lblMessage.setText("");
    }

    @FXML
    public void loadRecords() {
        try {
            if (ParameterRepository.BREEDS.equals(cbCategory.getValue())) {
                loadPetTypes();
            }
            var records = parameterRepository.findRecords(cbCategory.getValue());
            tableParameters.setItems(FXCollections.observableArrayList(records));
            lblSummary.setText(records.size() + " record(s)");
            lblMessage.setText("");
        } catch (SQLException e) {
            lblMessage.setText("Could not load parameters: " + e.getMessage());
        }
    }

    @FXML
    public void backToMenu(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administration");
    }

    private void configureColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colExtra.setCellValueFactory(new PropertyValueFactory<>("extra"));
    }

    private void configureForm(String category) {
        // Breed needs a pet type; currency needs an acronym; system parameters need value and description.
        boolean breed = ParameterRepository.BREEDS.equals(category);
        boolean currency = ParameterRepository.CURRENCIES.equals(category);
        boolean systemParameter = ParameterRepository.SYSTEM_PARAMETERS.equals(category);

        boxPetType.setVisible(breed);
        boxPetType.setManaged(breed);
        boxExtra.setVisible(currency || systemParameter);
        boxExtra.setManaged(currency || systemParameter);
        boxDescription.setVisible(systemParameter);
        boxDescription.setManaged(systemParameter);
        if (breed) {
            loadPetTypes();
        }

        lblName.setText(systemParameter ? "Parameter name" : "Name");
        lblExtra.setText(currency ? "Acronym" : "Value");
        colExtra.setText(breed ? "Type" : currency ? "Acronym" : systemParameter ? "Value" : "Detail");

        if (systemParameter) {
            txtName.setPromptText("Example: matchJobfreq");
            txtExtra.setPromptText("Value in hours or configured value");
            txtDescription.setPromptText("Parameter description");
        } else {
            txtName.setPromptText("Record name");
            txtExtra.setPromptText("Additional value");
        }
    }

    private void loadSelectedRecord(ParameterRecord record) {
        // Selecting a table row switches the form into edit mode for that record id.
        selectedId = record.getId();
        txtName.setText(record.getName());
        txtExtra.setText(record.getExtra());
        txtDescription.setText(record.getDescription());
        if (ParameterRepository.BREEDS.equals(cbCategory.getValue())) {
            selectPetTypeByLabel(record.getExtra());
        }
        lblMessage.setText("Editing record #" + selectedId + ".");
    }

    private void loadPetTypes() {
        try {
            cbPetType.setItems(FXCollections.observableArrayList(catalogRepository.findPetTypes()));
        } catch (SQLException e) {
            lblMessage.setText("Could not load pet types: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtName.clear();
        txtExtra.clear();
        txtDescription.clear();
        cbPetType.getSelectionModel().clearSelection();
    }

    private void selectPetTypeByLabel(String label) {
        for (CatalogOption option : cbPetType.getItems()) {
            if (option.getLabel().equals(label)) {
                cbPetType.setValue(option);
                return;
            }
        }
    }

    private Long idOf(ComboBox<CatalogOption> comboBox) {
        CatalogOption selected = comboBox.getValue();
        return selected == null ? null : selected.getId();
    }
}
