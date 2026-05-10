package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.CatalogRepository;
import cr.tec.bd.crv.database.DonationRepository;
import cr.tec.bd.crv.model.CatalogOption;
import cr.tec.bd.crv.model.DonationRecord;
import cr.tec.bd.crv.util.NavigationUtil;
import cr.tec.bd.crv.util.SessionContext;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controls donation registration and donation review.
 *
 * <p>Normal users use this screen to register their own donations. Admin users
 * use the same screen as a review and maintenance area, so role checks decide
 * which buttons and panels are available.</p>
 */
public class DonationController {

    private final CatalogRepository catalogRepository = new CatalogRepository();
    private final DonationRepository donationRepository = new DonationRepository();

    @FXML
    private VBox pnlRegistroDonacion;

    @FXML
    private ComboBox<CatalogOption> cbAsociacionDonacion;

    @FXML
    private ComboBox<CatalogOption> cbMonedaDonacion;

    @FXML
    private TextField txtMontoDonacion;

    @FXML
    private DatePicker dpFechaDonacion;

    @FXML
    private Button btnRegistrarDonacion;

    @FXML
    private Label lblModoDonacion;

    @FXML
    private DatePicker dpDesdeFiltro;

    @FXML
    private DatePicker dpHastaFiltro;

    @FXML
    private TextField txtDonadorFiltro;

    @FXML
    private ComboBox<CatalogOption> cbFiltroAsociacion;

    @FXML
    private TextField txtMontoFiltro;

    @FXML
    private Button btnEliminarDonacion;

    @FXML
    private TableView<DonationRecord> tablaDonaciones;

    @FXML
    private TableColumn<DonationRecord, String> colDonador;

    @FXML
    private TableColumn<DonationRecord, String> colAsociacion;

    @FXML
    private TableColumn<DonationRecord, String> colMonto;

    @FXML
    private TableColumn<DonationRecord, String> colFecha;

    @FXML
    private Label lblMensajeDonacion;

    @FXML
    private Label lblResumenDonaciones;

    @FXML
    public void initialize() {
        configureColumns();
        configureRole();
        loadCatalogs();
        dpFechaDonacion.setValue(LocalDate.now());
        buscarDonaciones();
    }

    @FXML
    public void registrarDonacion() {
        try {
            // The current user is used as the donor; the form only asks where and how much they donate.
            donationRepository.registerDonation(
                    SessionContext.getCurrentPersonId(),
                    idOf(cbAsociacionDonacion),
                    idOf(cbMonedaDonacion),
                    parseAmount(txtMontoDonacion.getText()),
                    dpFechaDonacion.getValue()
            );

            lblMensajeDonacion.setText("Donacion registrada correctamente.");
            limpiarFormularioDonacion();
            buscarDonaciones();
        } catch (IllegalArgumentException e) {
            lblMensajeDonacion.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeDonacion.setText("No se pudo registrar la donacion: " + e.getMessage());
        }
    }

    @FXML
    public void buscarDonaciones() {
        try {
            // Users see their own donations. Admins pass null to review all donations.
            Long visiblePersonId = SessionContext.isAdmin() ? null : SessionContext.getCurrentPersonId();
            List<DonationRecord> donations = donationRepository.findDonations(
                    dpDesdeFiltro.getValue(),
                    dpHastaFiltro.getValue(),
                    txtDonadorFiltro.getText(),
                    idOf(cbFiltroAsociacion),
                    parseOptionalAmount(txtMontoFiltro.getText()),
                    visiblePersonId
            );

            tablaDonaciones.setItems(FXCollections.observableArrayList(donations));
            updateSummary(donations);
            clearMessageIfNeeded();
        } catch (IllegalArgumentException e) {
            lblMensajeDonacion.setText(e.getMessage());
        } catch (SQLException e) {
            lblMensajeDonacion.setText("No se pudieron consultar donaciones: " + e.getMessage());
        }
    }

    @FXML
    public void limpiarFiltros() {
        dpDesdeFiltro.setValue(null);
        dpHastaFiltro.setValue(null);
        txtDonadorFiltro.clear();
        txtMontoFiltro.clear();
        cbFiltroAsociacion.getSelectionModel().clearSelection();
        buscarDonaciones();
    }

    @FXML
    public void limpiarFormularioDonacion() {
        cbAsociacionDonacion.getSelectionModel().clearSelection();
        cbMonedaDonacion.getSelectionModel().clearSelection();
        txtMontoDonacion.clear();
        dpFechaDonacion.setValue(LocalDate.now());
    }

    @FXML
    public void eliminarDonacionSeleccionada() {
        // Deleting a donation is restricted to admins because it changes historical money records.
        if (!SessionContext.isAdmin()) {
            lblMensajeDonacion.setText("Solo el administrador puede eliminar donaciones.");
            return;
        }

        DonationRecord selectedDonation = tablaDonaciones.getSelectionModel().getSelectedItem();
        if (selectedDonation == null) {
            lblMensajeDonacion.setText("Seleccione una donacion de la tabla.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar eliminacion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Desea eliminar la donacion seleccionada?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            donationRepository.deleteDonation(selectedDonation.getId());
            lblMensajeDonacion.setText("Donacion eliminada correctamente.");
            buscarDonaciones();
        } catch (SQLException e) {
            lblMensajeDonacion.setText("No se pudo eliminar la donacion: " + e.getMessage());
        }
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        String menuPath = SessionContext.isAdmin() ? "/view/admin_menu.fxml" : "/view/menu.fxml";
        NavigationUtil.openWindow(event, menuPath, "BDP1 - Bienestar Animal");
    }

    private void configureColumns() {
        colDonador.setCellValueFactory(new PropertyValueFactory<>("donorName"));
        colAsociacion.setCellValueFactory(new PropertyValueFactory<>("associationName"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("amountText"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("donationDateText"));
    }

    private void configureRole() {
        // The same FXML serves two modes: user donation entry and admin review.
        boolean admin = SessionContext.isAdmin();
        pnlRegistroDonacion.setDisable(admin);
        btnRegistrarDonacion.setDisable(admin);
        btnEliminarDonacion.setVisible(admin);
        btnEliminarDonacion.setManaged(admin);

        if (admin) {
            lblModoDonacion.setText("Modo administrador: consulta y administra donaciones registradas.");
        } else {
            lblModoDonacion.setText("Su donacion quedara asociada a la cuenta con la que inicio sesion.");
        }
    }

    private void loadCatalogs() {
        try {
            List<CatalogOption> associations = catalogRepository.findAssociations();
            cbAsociacionDonacion.setItems(FXCollections.observableArrayList(associations));
            cbFiltroAsociacion.setItems(FXCollections.observableArrayList(associations));
            cbMonedaDonacion.setItems(FXCollections.observableArrayList(catalogRepository.findCurrencies()));
        } catch (SQLException e) {
            lblMensajeDonacion.setText("No se pudieron cargar catalogos de donaciones: " + e.getMessage());
        }
    }

    private void updateSummary(List<DonationRecord> donations) {
        Map<String, BigDecimal> totalsByCurrency = new LinkedHashMap<>();
        for (DonationRecord donation : donations) {
            String currency = donation.getCurrency().isBlank() ? "N/A" : donation.getCurrency();
            BigDecimal amount = donation.getAmount() == null ? BigDecimal.ZERO : donation.getAmount();
            totalsByCurrency.merge(currency, amount, BigDecimal::add);
        }

        StringBuilder summary = new StringBuilder();
        summary.append(donations.size()).append(" registro(s)");

        if (!totalsByCurrency.isEmpty()) {
            summary.append(" | Total: ");
            boolean first = true;
            for (Map.Entry<String, BigDecimal> entry : totalsByCurrency.entrySet()) {
                if (!first) {
                    summary.append(" / ");
                }
                summary.append(entry.getKey()).append(" ").append(entry.getValue().toPlainString());
                first = false;
            }
        }

        lblResumenDonaciones.setText(summary.toString());
    }

    private void clearMessageIfNeeded() {
        String message = lblMensajeDonacion.getText();
        if (message != null && message.startsWith("No se pudieron consultar")) {
            lblMensajeDonacion.setText("");
        }
    }

    private Long idOf(ComboBox<CatalogOption> comboBox) {
        CatalogOption selected = comboBox.getValue();
        return selected == null ? null : selected.getId();
    }

    private BigDecimal parseAmount(String rawAmount) {
        if (rawAmount == null || rawAmount.trim().isEmpty()) {
            return null;
        }

        try {
            return new BigDecimal(rawAmount.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El monto debe ser numerico.");
        }
    }

    private BigDecimal parseOptionalAmount(String rawAmount) {
        if (rawAmount == null || rawAmount.trim().isEmpty()) {
            return null;
        }
        return parseAmount(rawAmount);
    }
}
