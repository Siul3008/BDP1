package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.AuditRepository;
import cr.tec.bd.crv.model.AuditRecord;
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
 * Controller for the audit and journal review screen.
 */
public class AuditController {

    private final AuditRepository auditRepository = new AuditRepository();

    @FXML
    private TextField txtModuloBitacora;

    @FXML
    private TextField txtUsuarioBitacora;

    @FXML
    private TextField txtCampoBitacora;

    @FXML
    private TableView<AuditRecord> tablaBitacora;

    @FXML
    private TableColumn<AuditRecord, String> colFechaBitacora;

    @FXML
    private TableColumn<AuditRecord, String> colModuloBitacora;

    @FXML
    private TableColumn<AuditRecord, String> colUsuarioBitacora;

    @FXML
    private TableColumn<AuditRecord, String> colCampoBitacora;

    @FXML
    private TableColumn<AuditRecord, String> colAnteriorBitacora;

    @FXML
    private TableColumn<AuditRecord, String> colNuevoBitacora;

    @FXML
    private Label lblResumenBitacora;

    @FXML
    private Label lblMensajeBitacora;

    @FXML
    public void initialize() {
        configureColumns();
        consultarBitacora();
    }

    @FXML
    public void consultarBitacora() {
        try {
            List<AuditRecord> records = auditRepository.findAuditRecords(
                    txtModuloBitacora.getText(),
                    txtUsuarioBitacora.getText(),
                    txtCampoBitacora.getText()
            );
            tablaBitacora.setItems(FXCollections.observableArrayList(records));
            lblResumenBitacora.setText(records.size() + " registro(s)");
            lblMensajeBitacora.setText("");
        } catch (SQLException e) {
            lblMensajeBitacora.setText("No se pudo consultar la bitacora: " + e.getMessage());
        }
    }

    @FXML
    public void limpiarBitacora() {
        txtModuloBitacora.clear();
        txtUsuarioBitacora.clear();
        txtCampoBitacora.clear();
        consultarBitacora();
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administracion");
    }

    private void configureColumns() {
        colFechaBitacora.setCellValueFactory(new PropertyValueFactory<>("changeDateText"));
        colModuloBitacora.setCellValueFactory(new PropertyValueFactory<>("moduleName"));
        colUsuarioBitacora.setCellValueFactory(new PropertyValueFactory<>("changedBy"));
        colCampoBitacora.setCellValueFactory(new PropertyValueFactory<>("fieldName"));
        colAnteriorBitacora.setCellValueFactory(new PropertyValueFactory<>("previousValue"));
        colNuevoBitacora.setCellValueFactory(new PropertyValueFactory<>("currentValue"));
    }
}
