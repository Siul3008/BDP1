package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.AssociationRepository;
import cr.tec.bd.crv.model.AssociationRecord;
import cr.tec.bd.crv.util.NavigationUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Admin controller for creating and reviewing association records.
 */
public class AssociationController {

    private final AssociationRepository associationRepository = new AssociationRepository();

    @FXML
    private TextField txtNombreAsociacion;

    @FXML
    private Label lblMensajeAsociacion;

    @FXML
    private TableView<AssociationRecord> tablaAsociaciones;

    @FXML
    private TableColumn<AssociationRecord, Long> colAsociacionId;

    @FXML
    private TableColumn<AssociationRecord, String> colAsociacionNombre;

    @FXML
    private TableColumn<AssociationRecord, Integer> colAsociacionDonaciones;

    @FXML
    private TableColumn<AssociationRecord, String> colAsociacionTotal;

    @FXML
    private Label lblResumenAsociaciones;

    @FXML
    public void initialize() {
        configureColumns();
        loadAssociations();
        tablaAsociaciones.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected != null) {
                txtNombreAsociacion.setText(selected.getName());
                lblMensajeAsociacion.setText("Editando asociacion seleccionada.");
            }
        });
    }

    // The current database table only stores id and name for associations.
    @FXML
    public void guardarAsociacion() {
        try {
            associationRepository.registerAssociation(txtNombreAsociacion.getText());
            txtNombreAsociacion.clear();
            lblMensajeAsociacion.setText("Asociacion registrada correctamente.");
            loadAssociations();
        } catch (IllegalArgumentException e) {
            lblMensajeAsociacion.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeAsociacion.setText("No se pudo registrar la asociacion: " + e.getMessage());
        }
    }

    @FXML
    public void actualizarAsociacion() {
        AssociationRecord selectedAssociation = tablaAsociaciones.getSelectionModel().getSelectedItem();
        if (selectedAssociation == null) {
            lblMensajeAsociacion.setText("Seleccione una asociacion de la tabla.");
            return;
        }

        try {
            associationRepository.updateAssociationName(selectedAssociation.getId(), txtNombreAsociacion.getText());
            lblMensajeAsociacion.setText("Asociacion actualizada correctamente.");
            loadAssociations();
        } catch (IllegalArgumentException e) {
            lblMensajeAsociacion.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeAsociacion.setText("No se pudo actualizar la asociacion: " + e.getMessage());
        }
    }

    @FXML
    public void limpiarFormulario() {
        tablaAsociaciones.getSelectionModel().clearSelection();
        txtNombreAsociacion.clear();
        lblMensajeAsociacion.setText("");
    }

    @FXML
    public void recargarAsociaciones() {
        loadAssociations();
    }

    @FXML
    public void volverAdmin(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administracion");
    }

    private void configureColumns() {
        colAsociacionId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAsociacionNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAsociacionDonaciones.setCellValueFactory(new PropertyValueFactory<>("donationCount"));
        colAsociacionTotal.setCellValueFactory(new PropertyValueFactory<>("totalDonated"));
    }

    private void loadAssociations() {
        try {
            List<AssociationRecord> associations = associationRepository.findAssociationRecords();
            tablaAsociaciones.setItems(FXCollections.observableArrayList(associations));
            lblResumenAsociaciones.setText(associations.size() + " asociacion(es)");
        } catch (SQLException e) {
            lblMensajeAsociacion.setText("No se pudieron cargar asociaciones: " + e.getMessage());
        }
    }
}
