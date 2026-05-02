package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.AssociationRepository;
import cr.tec.bd.crv.util.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Admin controller for creating and reviewing association records.
 */
public class AssociationController {

    private final AssociationRepository associationRepository = new AssociationRepository();

    @FXML
    private TextField txtNombreAsociacion;

    @FXML
    private Label lblMensajeAsociacion;

    // The current database table only stores id and name for associations.
    @FXML
    public void guardarAsociacion() {
        try {
            associationRepository.registerAssociation(txtNombreAsociacion.getText());
            txtNombreAsociacion.clear();
            lblMensajeAsociacion.setText("Asociacion registrada correctamente.");
        } catch (IllegalArgumentException e) {
            lblMensajeAsociacion.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeAsociacion.setText("No se pudo registrar la asociacion: " + e.getMessage());
        }
    }

    @FXML
    public void limpiarFormulario() {
        txtNombreAsociacion.clear();
        lblMensajeAsociacion.setText("");
    }

    @FXML
    public void volverAdmin(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administracion");
    }
}
