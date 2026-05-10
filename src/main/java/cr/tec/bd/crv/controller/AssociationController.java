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
 * Controls association maintenance for administrators.
 *
 * <p>Associations are the organizations that receive donations. The screen lets
 * an admin create them, rename them, and review donation totals attached to each
 * association.</p>
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
        // Selecting a row copies its name into the form so the admin can edit it directly.
        configureColumns();
        loadAssociations();
        tablaAsociaciones.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected != null) {
                txtNombreAsociacion.setText(selected.getName());
                lblMensajeAsociacion.setText("Editing selected association.");
            }
        });
    }

    /**
     * Creates a new association using the name typed in the form.
     */
    @FXML
    public void guardarAsociacion() {
        try {
            associationRepository.registerAssociation(txtNombreAsociacion.getText());
            txtNombreAsociacion.clear();
            lblMensajeAsociacion.setText("Association registered successfully.");
            loadAssociations();
        } catch (IllegalArgumentException e) {
            lblMensajeAsociacion.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeAsociacion.setText("Could not register the association: " + e.getMessage());
        }
    }

    @FXML
    public void actualizarAsociacion() {
        // Updates only the selected association to avoid changing the wrong organization.
        AssociationRecord selectedAssociation = tablaAsociaciones.getSelectionModel().getSelectedItem();
        if (selectedAssociation == null) {
            lblMensajeAsociacion.setText("Select an association from the list.");
            return;
        }

        try {
            associationRepository.updateAssociationName(selectedAssociation.getId(), txtNombreAsociacion.getText());
            lblMensajeAsociacion.setText("Association updated successfully.");
            loadAssociations();
        } catch (IllegalArgumentException e) {
            lblMensajeAsociacion.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeAsociacion.setText("Could not update the association: " + e.getMessage());
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
        NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administration");
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
            lblResumenAsociaciones.setText(associations.size() + " association(s)");
        } catch (SQLException e) {
            lblMensajeAsociacion.setText("Could not load associations: " + e.getMessage());
        }
    }
}
