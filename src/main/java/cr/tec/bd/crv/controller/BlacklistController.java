package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.BlacklistRepository;
import cr.tec.bd.crv.model.BlacklistRecord;
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
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Controls blacklist reporting and review.
 *
 * <p>Users can report another person, while admins can choose both the reporter
 * and the reported person. The repository stores the report and links it to a
 * rating so repeated behavior can be reviewed later.</p>
 */
public class BlacklistController {

    private final BlacklistRepository blacklistRepository = new BlacklistRepository();

    @FXML
    private VBox boxReporter;

    @FXML
    private Label lblReporterInfo;

    @FXML
    private ComboBox<CatalogOption> cbReporter;

    @FXML
    private ComboBox<CatalogOption> cbReportee;

    @FXML
    private ComboBox<String> cbRating;

    @FXML
    private DatePicker dpReportDate;

    @FXML
    private TextArea txtReason;

    @FXML
    private TextField txtFilter;

    @FXML
    private TableView<BlacklistRecord> tableBlacklist;

    @FXML
    private TableColumn<BlacklistRecord, LocalDate> colDate;

    @FXML
    private TableColumn<BlacklistRecord, String> colReporter;

    @FXML
    private TableColumn<BlacklistRecord, String> colReportee;

    @FXML
    private TableColumn<BlacklistRecord, String> colRating;

    @FXML
    private TableColumn<BlacklistRecord, String> colAverage;

    @FXML
    private TableColumn<BlacklistRecord, String> colStatus;

    @FXML
    private TableColumn<BlacklistRecord, String> colReason;

    @FXML
    private Label lblSummary;

    @FXML
    private Label lblMessage;

    @FXML
    private Label lblDetalleReportante;

    @FXML
    private Label lblDetalleReportado;

    @FXML
    private Label lblDetalleCalificacion;

    @FXML
    private Label lblDetallePromedio;

    @FXML
    private Label lblDetalleEstado;

    @FXML
    private TextArea txtDetalleRazon;

    @FXML
    public void initialize() {
        configureColumns();
        configureSelection();
        configureRoleFields();
        cbRating.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5"));
        dpReportDate.setValue(LocalDate.now());
        loadPeople();
        loadReports();
    }

    @FXML
    public void saveReport() {
        try {
            // Non-admin users always report as themselves; admins may select the reporter manually.
            Long reporterId = SessionContext.isAdmin()
                    ? idOf(cbReporter)
                    : SessionContext.getCurrentPersonId();

            blacklistRepository.registerReport(
                    reporterId,
                    idOf(cbReportee),
                    cbRating.getValue(),
                    txtReason.getText(),
                    dpReportDate.getValue()
            );

            lblMessage.setText("Report added to blacklist.");
            clearFormOnly();
            loadReports();
        } catch (IllegalArgumentException | IllegalStateException e) {
            lblMessage.setText(e.getMessage());
        } catch (SQLException e) {
            lblMessage.setText("Could not save the report: " + e.getMessage());
        }
    }

    @FXML
    public void loadReports() {
        try {
            List<BlacklistRecord> records = blacklistRepository.findReports(txtFilter.getText());
            tableBlacklist.setItems(FXCollections.observableArrayList(records));
            lblSummary.setText(records.size() + " record(s)");
            if (records.isEmpty() && lblMessage.getText().isEmpty()) {
                lblMessage.setText("No reports were found with those filters.");
            }
        } catch (SQLException | IllegalStateException e) {
            lblMessage.setText("Could not search blacklist: " + e.getMessage());
        }
    }

    @FXML
    public void clearFilters() {
        txtFilter.clear();
        lblMessage.setText("");
        loadReports();
    }

    @FXML
    public void clearForm() {
        clearFormOnly();
        lblMessage.setText("");
    }

    @FXML
    public void backToMenu(ActionEvent event) throws IOException {
        if (SessionContext.isAdmin()) {
            NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administration");
        } else {
            NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Animal Welfare");
        }
    }

    private void configureColumns() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
        colReporter.setCellValueFactory(new PropertyValueFactory<>("reporterName"));
        colReportee.setCellValueFactory(new PropertyValueFactory<>("reporteeName"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        colAverage.setCellValueFactory(new PropertyValueFactory<>("averageRating"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("active"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
    }

    private void configureSelection() {
        tableBlacklist.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, selected) -> showReportDetails(selected));
    }

    private void showReportDetails(BlacklistRecord selectedRecord) {
        if (selectedRecord == null) {
            lblDetalleReportante.setText("-");
            lblDetalleReportado.setText("-");
            lblDetalleCalificacion.setText("-");
            lblDetallePromedio.setText("-");
            lblDetalleEstado.setText("-");
            txtDetalleRazon.clear();
            return;
        }

        lblDetalleReportante.setText(selectedRecord.getReporterName());
        lblDetalleReportado.setText(selectedRecord.getReporteeName());
        lblDetalleCalificacion.setText(selectedRecord.getRating());
        lblDetallePromedio.setText(selectedRecord.getAverageRating());
        lblDetalleEstado.setText(selectedRecord.getActive());
        txtDetalleRazon.setText(selectedRecord.getReason());
    }

    private void configureRoleFields() {
        // Reporter selection is only visible when an admin is entering the report.
        boolean admin = SessionContext.isAdmin();
        boxReporter.setVisible(admin);
        boxReporter.setManaged(admin);
        lblReporterInfo.setText(admin
                ? "Select who is making the report."
                : "The report will be registered with the signed-in user.");
    }

    private void loadPeople() {
        try {
            List<CatalogOption> people = blacklistRepository.findPeopleOptions();
            cbReporter.setItems(FXCollections.observableArrayList(people));
            cbReportee.setItems(FXCollections.observableArrayList(people));
        } catch (SQLException e) {
            lblMessage.setText("Could not load people: " + e.getMessage());
        }
    }

    private void clearFormOnly() {
        cbReporter.getSelectionModel().clearSelection();
        cbReportee.getSelectionModel().clearSelection();
        cbRating.getSelectionModel().clearSelection();
        dpReportDate.setValue(LocalDate.now());
        txtReason.clear();
    }

    private Long idOf(ComboBox<CatalogOption> comboBox) {
        CatalogOption selected = comboBox.getValue();
        return selected == null ? null : selected.getId();
    }
}
