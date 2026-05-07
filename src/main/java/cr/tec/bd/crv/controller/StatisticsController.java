package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.database.StatisticsRepository;
import cr.tec.bd.crv.model.ChartDataPoint;
import cr.tec.bd.crv.model.StatisticSummary;
import cr.tec.bd.crv.util.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for the statistics dashboard.
 */
public class StatisticsController {

    private final StatisticsRepository statisticsRepository = new StatisticsRepository();

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
    public void initialize() {
        actualizarEstadisticas();
    }

    @FXML
    public void actualizarEstadisticas() {
        try {
            StatisticSummary summary = statisticsRepository.loadSummary();
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
        } catch (SQLException e) {
            lblResumenEstadisticas.setText("No se pudieron cargar estadisticas: " + e.getMessage());
        }
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administracion");
    }

    private void loadChart(BarChart<String, Number> chart, String seriesName, Iterable<ChartDataPoint> values) {
        chart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(seriesName);

        for (ChartDataPoint value : values) {
            series.getData().add(new XYChart.Data<>(value.getLabel(), value.getValue()));
        }

        chart.getData().add(series);
    }
}
