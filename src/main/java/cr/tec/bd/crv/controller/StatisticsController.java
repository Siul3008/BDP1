package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.CatalogRepository;
import cr.tec.bd.crv.database.StatisticsRepository;
import cr.tec.bd.crv.model.ChartDataPoint;
import cr.tec.bd.crv.model.CatalogOption;
import cr.tec.bd.crv.model.StatisticSummary;
import cr.tec.bd.crv.util.NavigationUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Controls the statistics dashboard.
 *
 * <p>The dashboard summarizes database information into cards and charts. The
 * filters at the top limit the calculations by dates, pet type, or breed.</p>
 */
public class StatisticsController {

    private final StatisticsRepository statisticsRepository = new StatisticsRepository();
    private final CatalogRepository catalogRepository = new CatalogRepository();

    @FXML
    private DatePicker dpStatsFrom;

    @FXML
    private DatePicker dpStatsTo;

    @FXML
    private ComboBox<CatalogOption> cbStatsPetType;

    @FXML
    private ComboBox<CatalogOption> cbStatsBreed;

    @FXML
    private Label lblMascotasRegistradas;

    @FXML
    private Label lblAdopcionesExitosas;

    @FXML
    private Label lblDonacionesAcumuladas;

    @FXML
    private Label lblCasasCunaActivas;

    @FXML
    private Label lblResumenEstadisticas;

    @FXML
    private BarChart<String, Number> chartMascotasEstado;

    @FXML
    private BarChart<String, Number> chartMascotasTipo;

    @FXML
    private BarChart<String, Number> chartDonacionesAsociacion;

    @FXML
    private BarChart<String, Number> chartAdopciones;

    @FXML
    private BarChart<String, Number> chartEdad;

    @FXML
    private BarChart<String, Number> chartCriticas;

    @FXML
    public void initialize() {
        dpStatsFrom.setValue(LocalDate.now().withDayOfYear(1));
        dpStatsTo.setValue(LocalDate.now());
        loadCatalogFilters();
        configureBreedReload();
        actualizarEstadisticas();
    }

    @FXML
    public void actualizarEstadisticas() {
        try {
            // The repository returns already-calculated numbers and chart points.
            StatisticSummary summary = statisticsRepository.loadSummary(
                    dpStatsFrom.getValue(),
                    dpStatsTo.getValue(),
                    idOf(cbStatsPetType),
                    idOf(cbStatsBreed)
            );
            lblMascotasRegistradas.setText(String.valueOf(summary.getTotalPets()));
            lblAdopcionesExitosas.setText(summary.getAdoptionRateText());
            lblDonacionesAcumuladas.setText(summary.getDonationTotalText());
            lblCasasCunaActivas.setText(String.valueOf(summary.getActiveFosterHomes()));
            lblResumenEstadisticas.setText(
                    summary.getAdoptedPets() + " mascota(s) adoptada(s) de "
                            + summary.getTotalPets() + " registrada(s)."
            );

            loadChart(chartMascotasEstado, "Estados", summary.getPetsByStatus());
            loadChart(chartMascotasTipo, "Tipos", summary.getPetsByType());
            loadChart(chartDonacionesAsociacion, "Donaciones", summary.getDonationsByAssociation());
            loadChart(chartAdopciones, "Adopciones", summary.getAdoptionOutcome());
            loadChart(chartEdad, "Edades", summary.getPetsByAgeRange());
            loadChart(chartCriticas, "Criticas", summary.getCriticalAdoptionPetsByType());
        } catch (SQLException e) {
            lblResumenEstadisticas.setText("No se pudieron cargar estadisticas: " + e.getMessage());
        }
    }

    @FXML
    public void limpiarFiltros() {
        dpStatsFrom.setValue(LocalDate.now().withDayOfYear(1));
        dpStatsTo.setValue(LocalDate.now());
        cbStatsPetType.getSelectionModel().clearSelection();
        cbStatsBreed.getSelectionModel().clearSelection();
        actualizarEstadisticas();
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administracion");
    }

    private void loadChart(BarChart<String, Number> chart, String seriesName, Iterable<ChartDataPoint> values) {
        // JavaFX charts display one or more series; each summary list becomes one series here.
        chart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(seriesName);

        for (ChartDataPoint value : values) {
            series.getData().add(new XYChart.Data<>(value.getLabel(), value.getValue()));
        }

        chart.getData().add(series);
    }

    private void loadCatalogFilters() {
        try {
            cbStatsPetType.setItems(FXCollections.observableArrayList(catalogRepository.findPetTypes()));
            cbStatsBreed.setItems(FXCollections.observableArrayList(catalogRepository.findBreeds()));
        } catch (SQLException e) {
            lblResumenEstadisticas.setText("No se pudieron cargar filtros: " + e.getMessage());
        }
    }

    private void configureBreedReload() {
        // Choosing a pet type narrows the breed list to breeds that belong to that type.
        cbStatsPetType.valueProperty().addListener((observable, oldValue, newValue) -> {
            cbStatsBreed.getSelectionModel().clearSelection();
            try {
                if (newValue == null) {
                    cbStatsBreed.setItems(FXCollections.observableArrayList(catalogRepository.findBreeds()));
                } else {
                    cbStatsBreed.setItems(FXCollections.observableArrayList(
                            catalogRepository.findBreedsByPetType(newValue.getId())
                    ));
                }
            } catch (SQLException e) {
                lblResumenEstadisticas.setText("No se pudieron cargar razas: " + e.getMessage());
            }
        });
    }

    private Long idOf(ComboBox<CatalogOption> comboBox) {
        CatalogOption selected = comboBox.getValue();
        return selected == null ? null : selected.getId();
    }
}
