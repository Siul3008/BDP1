package cr.tec.bd.crv.controller;

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

/**
 * Handles adoption registration and adoption follow-up from the JavaFX screen.
 */
public class AdoptionController {

    private final AdoptionRepository adoptionRepository = new AdoptionRepository();

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
    private TextField txtTipoVivienda;

    @FXML
    private TextField txtEjercicio;

    @FXML
    private TextField txtOtrasMascotas;

    @FXML
    private TextArea txtRespuestas;

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
        configureColumns();
        cbCalificacion.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5"));
        dpFechaAdopcion.setValue(LocalDate.now());
        loadAdoptablePets();
        loadAdoptions();
    }

    @FXML
    public void registrarAdopcion() {
        try {
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

    private void loadAdoptablePets() {
        try {
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
