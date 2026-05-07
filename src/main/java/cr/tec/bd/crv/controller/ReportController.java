package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.ReportRepository;
import cr.tec.bd.crv.model.ReportRow;
import cr.tec.bd.crv.util.NavigationUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for administrative reports.
 */
public class ReportController {

    private static final String PETS_BY_STATUS = "Mascotas por estado";
    private static final String DONATIONS_BY_ASSOCIATION = "Donaciones por asociacion";
    private static final String POTENTIAL_MATCHES = "Matches potenciales";
    private static final String BLACKLIST = "Lista negra";

    private final ReportRepository reportRepository = new ReportRepository();

    @FXML
    private ComboBox<String> cbTipoReporte;

    @FXML
    private TextField txtFiltroReporte;

    @FXML
    private DatePicker dpReporteDesde;

    @FXML
    private DatePicker dpReporteHasta;

    @FXML
    private TableView<ReportRow> tablaReportes;

    @FXML
    private TableColumn<ReportRow, String> colReporte1;

    @FXML
    private TableColumn<ReportRow, String> colReporte2;

    @FXML
    private TableColumn<ReportRow, String> colReporte3;

    @FXML
    private TableColumn<ReportRow, String> colReporte4;

    @FXML
    private TableColumn<ReportRow, String> colReporte5;

    @FXML
    private Label lblResumenReporte;

    @FXML
    private Label lblMensajeReporte;

    @FXML
    public void initialize() {
        configureColumns();
        cbTipoReporte.setItems(FXCollections.observableArrayList(
                PETS_BY_STATUS,
                DONATIONS_BY_ASSOCIATION,
                POTENTIAL_MATCHES,
                BLACKLIST
        ));
        cbTipoReporte.setValue(PETS_BY_STATUS);
        generarReporte();
    }

    @FXML
    public void generarReporte() {
        try {
            String reportType = cbTipoReporte.getValue();
            configureHeaders(reportType);
            List<ReportRow> rows = switch (reportType) {
                case DONATIONS_BY_ASSOCIATION -> reportRepository.findDonationsByAssociation(
                        txtFiltroReporte.getText(),
                        dpReporteDesde.getValue(),
                        dpReporteHasta.getValue()
                );
                case POTENTIAL_MATCHES -> reportRepository.findPotentialMatches(
                        txtFiltroReporte.getText(),
                        dpReporteDesde.getValue(),
                        dpReporteHasta.getValue()
                );
                case BLACKLIST -> reportRepository.findBlacklist(
                        txtFiltroReporte.getText(),
                        dpReporteDesde.getValue(),
                        dpReporteHasta.getValue()
                );
                default -> reportRepository.findPetsByStatus(
                        txtFiltroReporte.getText(),
                        dpReporteDesde.getValue(),
                        dpReporteHasta.getValue()
                );
            };

            tablaReportes.setItems(FXCollections.observableArrayList(rows));
            lblResumenReporte.setText(rows.size() + " registro(s)");
            lblMensajeReporte.setText("");
        } catch (SQLException e) {
            lblMensajeReporte.setText("No se pudo generar el reporte: " + e.getMessage());
        }
    }

    @FXML
    public void limpiarReporte() {
        txtFiltroReporte.clear();
        dpReporteDesde.setValue(null);
        dpReporteHasta.setValue(null);
        generarReporte();
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administracion");
    }

    private void configureColumns() {
        colReporte1.setCellValueFactory(new PropertyValueFactory<>("column1"));
        colReporte2.setCellValueFactory(new PropertyValueFactory<>("column2"));
        colReporte3.setCellValueFactory(new PropertyValueFactory<>("column3"));
        colReporte4.setCellValueFactory(new PropertyValueFactory<>("column4"));
        colReporte5.setCellValueFactory(new PropertyValueFactory<>("column5"));
    }

    private void configureHeaders(String reportType) {
        switch (reportType) {
            case DONATIONS_BY_ASSOCIATION -> setHeaders("Asociacion", "Moneda", "Total", "Cantidad", "Ultima donacion");
            case POTENTIAL_MATCHES -> setHeaders("Mascota", "Tipo", "Raza", "Color", "Fecha reporte");
            case BLACKLIST -> setHeaders("Reporta", "Reportado", "Rating", "Razon", "Fecha");
            default -> setHeaders("Estado", "Tipo", "Cantidad", "Ultima fecha", "Porcentaje");
        }
    }

    private void setHeaders(String first, String second, String third, String fourth, String fifth) {
        colReporte1.setText(first);
        colReporte2.setText(second);
        colReporte3.setText(third);
        colReporte4.setText(fourth);
        colReporte5.setText(fifth);
    }
}
