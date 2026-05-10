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
 * Controls the administrative report screen.
 *
 * <p>The combo box selects which prepared query should run. Each report uses the
 * same table, so this controller also changes the column titles to match the
 * selected report.</p>
 */
public class ReportController {

    private static final String PETS_BY_STATUS = "Pets by status";
    private static final String DONATIONS_BY_ASSOCIATION = "Donations by association";
    private static final String POTENTIAL_MATCHES = "Potential matches";
    private static final String BLACKLIST = "Blacklist";
    private static final String NOT_ADOPTED_TWO_MONTHS = "Not adopted after 2 months";
    private static final String TOP_RESCUERS = "Top rescuers";
    private static final String FOSTER_HOME_TYPES = "Foster homes by type";
    private static final String CRITICAL_ADOPTION_PETS = "Critical pets for adoption";

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
                BLACKLIST,
                NOT_ADOPTED_TWO_MONTHS,
                TOP_RESCUERS,
                FOSTER_HOME_TYPES,
                CRITICAL_ADOPTION_PETS
        ));
        cbTipoReporte.setValue(PETS_BY_STATUS);
        generarReporte();
    }

    @FXML
    public void generarReporte() {
        try {
            // The selected name decides which repository method runs.
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
                case NOT_ADOPTED_TWO_MONTHS -> reportRepository.findNotAdoptedAfterTwoMonths(
                        txtFiltroReporte.getText()
                );
                case TOP_RESCUERS -> reportRepository.findTopRescuers();
                case FOSTER_HOME_TYPES -> reportRepository.findFosterHomesByAcceptedType();
                case CRITICAL_ADOPTION_PETS -> reportRepository.findCriticalPetsInAdoption(
                        txtFiltroReporte.getText()
                );
                default -> reportRepository.findPetsByStatus(
                        txtFiltroReporte.getText(),
                        dpReporteDesde.getValue(),
                        dpReporteHasta.getValue()
                );
            };

            tablaReportes.setItems(FXCollections.observableArrayList(rows));
            lblResumenReporte.setText(rows.size() + " record(s)");
            lblMensajeReporte.setText("");
        } catch (SQLException e) {
            lblMensajeReporte.setText("Could not generate the report: " + e.getMessage());
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
        NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administration");
    }

    private void configureColumns() {
        colReporte1.setCellValueFactory(new PropertyValueFactory<>("column1"));
        colReporte2.setCellValueFactory(new PropertyValueFactory<>("column2"));
        colReporte3.setCellValueFactory(new PropertyValueFactory<>("column3"));
        colReporte4.setCellValueFactory(new PropertyValueFactory<>("column4"));
        colReporte5.setCellValueFactory(new PropertyValueFactory<>("column5"));
    }

    private void configureHeaders(String reportType) {
        // One table is reused for many reports, so the visible headers change with the report.
        switch (reportType) {
            case DONATIONS_BY_ASSOCIATION -> setHeaders("Association", "Currency", "Total", "Count", "Last donation");
            case POTENTIAL_MATCHES -> setHeaders("Pet", "Type", "Breed", "Color", "Report date");
            case BLACKLIST -> setHeaders("Reporter", "Reported person", "Rating", "Reason", "Date");
            case NOT_ADOPTED_TWO_MONTHS -> setHeaders("Pet", "Type", "Breed", "From", "Waiting time");
            case TOP_RESCUERS -> setHeaders("Rescuer", "Pets", "Middle name", "Second last name", "ID");
            case FOSTER_HOME_TYPES -> setHeaders("Foster home", "Accepted types", "Detail", "ID", "Module");
            case CRITICAL_ADOPTION_PETS -> setHeaders("Pet", "Type", "Breed", "Health", "Description");
            default -> setHeaders("Status", "Type", "Count", "Last date", "Porcentaje");
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
