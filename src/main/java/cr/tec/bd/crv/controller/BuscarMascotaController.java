package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.util.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller for the pet search screen.
 */
public class BuscarMascotaController {

    @FXML
    private TextField txtBusqueda;

    @FXML
    private Label lblResultado;

    // Current search behavior is visual feedback; real queries can be added behind this method.
    @FXML
    public void buscarMascota() {
        String criterio = txtBusqueda.getText().trim();
        if (criterio.isEmpty()) {
            lblResultado.setText("Escriba un criterio para mostrar resultados.");
            return;
        }

        lblResultado.setText("Consulta lista para filtrar por: " + criterio);
    }

    @FXML
    public void limpiarBusqueda() {
        txtBusqueda.clear();
        lblResultado.setText("");
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
    }

    @FXML
    public void abrirListaMascotas(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/lista_mascotas.fxml", "Lista de Mascotas");
    }

    // Search connects to reports because both flows depend on filtered pet information.
    @FXML
    public void abrirReportes(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/reportes.fxml", "Consultas y Reportes");
    }
}
