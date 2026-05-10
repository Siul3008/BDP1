package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.CatalogRepository;
import cr.tec.bd.crv.database.AdoptionRepository;
import cr.tec.bd.crv.model.AdoptionFormData;
import cr.tec.bd.crv.model.AdoptionRecord;
import cr.tec.bd.crv.model.CatalogOption;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controls adoption registration and follow-up.
 *
 * <p>The screen connects a pet that is currently available for adoption with an
 * adopter account. After saving, the repository marks the pet as adopted and
 * transfers control to the adopter so later updates belong to the right person.</p>
 */
public class AdoptionController {

    private final AdoptionRepository adoptionRepository = new AdoptionRepository();
    private final CatalogRepository catalogRepository = new CatalogRepository();

    @FXML
    private ComboBox<CatalogOption> cbMascotaAdopcion;

    @FXML
    private TextField txtCorreoAdoptante;

    @FXML
    private DatePicker dpFechaAdopcion;

    @FXML
    private ComboBox<String> cbCalificacion;

    @FXML
    private TextField txtPatio;

    @FXML
    private Label lblPatio;

    @FXML
    private TextField txtTipoVivienda;

    @FXML
    private Label lblTipoVivienda;

    @FXML
    private TextField txtEjercicio;

    @FXML
    private Label lblEjercicio;

    @FXML
    private TextField txtOtrasMascotas;

    @FXML
    private Label lblOtrasMascotas;

    @FXML
    private TextArea txtRespuestas;

    @FXML
    private Label lblRespuestas;

    @FXML
    private TextField txtFotoAdopcion;

    @FXML
    private TextField txtFotoSeguimiento;

    @FXML
    private TextArea txtNotasAdoptante;

    @FXML
    private TextArea txtSeguimiento;

    @FXML
    private TableView<AdoptionRecord> tablaAdopciones;

    @FXML
    private TableColumn<AdoptionRecord, String> colAdoptante;

    @FXML
    private TableColumn<AdoptionRecord, String> colMascota;

    @FXML
    private TableColumn<AdoptionRecord, String> colFecha;

    @FXML
    private TableColumn<AdoptionRecord, String> colCalificacion;

    @FXML
    private TableColumn<AdoptionRecord, String> colSeguimiento;

    @FXML
    private Label lblMensajeAdopcion;

    @FXML
    private Label lblTotalAdopciones;

    @FXML
    public void initialize() {
        // The form starts ready to register today's adoption.
        configureColumns();
        cbCalificacion.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5"));
        dpFechaAdopcion.setValue(LocalDate.now());
        tablaAdopciones.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, selected) -> loadSelectedFollowUp(selected));
        loadQuestionLabels();
        loadAdoptablePets();
        loadAdoptions();
    }

    @FXML
    public void registrarAdopcion() {
        try {
            // The selected pet is converted into a small form object before touching the database.
            CatalogOption selectedPet = cbMascotaAdopcion.getValue();
            AdoptionFormData data = new AdoptionFormData(
                    selectedPet == null ? null : selectedPet.getId(),
                    txtCorreoAdoptante.getText(),
                    dpFechaAdopcion.getValue(),
                    txtPatio.getText(),
                    txtEjercicio.getText(),
                    txtTipoVivienda.getText(),
                    txtOtrasMascotas.getText(),
                    txtRespuestas.getText(),
                    cbCalificacion.getValue(),
                    txtNotasAdoptante.getText(),
                    txtSeguimiento.getText(),
                    txtFotoAdopcion.getText(),
                    txtFotoSeguimiento.getText()
            );

            adoptionRepository.registerAdoption(
                    data,
                    SessionContext.getCurrentPersonId(),
                    SessionContext.isAdmin()
            );

            lblMensajeAdopcion.setText("Adopcion registrada correctamente.");
            limpiarFormularioAdopcion();
            loadAdoptablePets();
            loadAdoptions();
        } catch (IllegalArgumentException e) {
            lblMensajeAdopcion.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeAdopcion.setText("No se pudo registrar la adopcion: " + e.getMessage());
        }
    }

    @FXML
    public void limpiarFormularioAdopcion() {
        cbMascotaAdopcion.getSelectionModel().clearSelection();
        txtCorreoAdoptante.clear();
        dpFechaAdopcion.setValue(LocalDate.now());
        cbCalificacion.getSelectionModel().clearSelection();
        txtPatio.clear();
        txtTipoVivienda.clear();
        txtEjercicio.clear();
        txtOtrasMascotas.clear();
        txtRespuestas.clear();
        txtFotoAdopcion.clear();
        txtFotoSeguimiento.clear();
        txtNotasAdoptante.clear();
        txtSeguimiento.clear();
    }

    @FXML
    public void recargarAdopciones() {
        loadAdoptablePets();
        loadAdoptions();
    }

    @FXML
    public void actualizarSeguimiento() {
        AdoptionRecord selectedAdoption = tablaAdopciones.getSelectionModel().getSelectedItem();
        if (selectedAdoption == null) {
            lblMensajeAdopcion.setText("Seleccione una adopcion del historial.");
            return;
        }

        try {
            adoptionRepository.updateFollowUp(
                    selectedAdoption.getId(),
                    txtSeguimiento.getText(),
                    txtFotoSeguimiento.getText(),
                    SessionContext.getCurrentPersonId(),
                    SessionContext.isAdmin()
            );
            lblMensajeAdopcion.setText("Seguimiento actualizado correctamente.");
            txtFotoSeguimiento.clear();
            loadAdoptions();
        } catch (IllegalArgumentException e) {
            lblMensajeAdopcion.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeAdopcion.setText("No se pudo actualizar seguimiento: " + e.getMessage());
        }
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        String menuPath = SessionContext.isAdmin() ? "/view/admin_menu.fxml" : "/view/menu.fxml";
        NavigationUtil.openWindow(event, menuPath, "BDP1 - Bienestar Animal");
    }

    private void configureColumns() {
        colAdoptante.setCellValueFactory(new PropertyValueFactory<>("adopterName"));
        colMascota.setCellValueFactory(new PropertyValueFactory<>("petName"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("adoptionDateText"));
        colCalificacion.setCellValueFactory(new PropertyValueFactory<>("rating"));
        colSeguimiento.setCellValueFactory(new PropertyValueFactory<>("followUpNotes"));
    }

    private void loadSelectedFollowUp(AdoptionRecord selectedAdoption) {
        if (selectedAdoption == null) {
            return;
        }
        txtSeguimiento.setText(selectedAdoption.getFollowUpNotes());
    }

    private void loadQuestionLabels() {
        try {
            Map<String, String> labels = catalogRepository.findSystemParametersByPrefix("adopt.");
            setLabelIfPresent(lblPatio, labels.get("adopt.yard"));
            setLabelIfPresent(lblTipoVivienda, labels.get("adopt.home"));
            setLabelIfPresent(lblEjercicio, labels.get("adopt.exercise"));
            setLabelIfPresent(lblOtrasMascotas, labels.get("adopt.pets"));
            setLabelIfPresent(lblRespuestas, labels.get("adopt.answers"));
        } catch (SQLException e) {
            lblMensajeAdopcion.setText("No se pudieron cargar preguntas configurables: " + e.getMessage());
        }
    }

    private void setLabelIfPresent(Label label, String value) {
        if (label != null && value != null && !value.trim().isEmpty()) {
            label.setText(value.trim());
        }
    }

    private void loadAdoptablePets() {
        try {
            // Normal users only see pets they control; admins can register adoption for any eligible pet.
            List<CatalogOption> pets = adoptionRepository.findAdoptablePets(
                    SessionContext.getCurrentPersonId(),
                    SessionContext.isAdmin()
            );
            cbMascotaAdopcion.setItems(FXCollections.observableArrayList(pets));
        } catch (SQLException e) {
            lblMensajeAdopcion.setText("No se pudieron cargar mascotas disponibles: " + e.getMessage());
        }
    }

    private void loadAdoptions() {
        try {
            // The table gives immediate confirmation of the latest saved adoptions.
            List<AdoptionRecord> adoptions = adoptionRepository.findRecentAdoptions(
                    SessionContext.getCurrentPersonId(),
                    SessionContext.isAdmin()
            );
            tablaAdopciones.setItems(FXCollections.observableArrayList(adoptions));
            lblTotalAdopciones.setText(adoptions.size() + " registro(s)");
        } catch (SQLException e) {
            lblMensajeAdopcion.setText("No se pudieron cargar adopciones: " + e.getMessage());
        }
    }
}
