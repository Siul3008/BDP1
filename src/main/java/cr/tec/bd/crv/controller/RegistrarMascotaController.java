package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.util.NavigationUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;

public class RegistrarMascotaController {

    @FXML
    private ComboBox<String> cbTipo;

    @FXML
    private ComboBox<String> cbEstado;

    @FXML
    private ComboBox<String> cbMoneda;

    @FXML
    private ComboBox<String> cbEnergia;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        cbTipo.setItems(FXCollections.observableArrayList(
                "Perro",
                "Gato",
                "Ave",
                "Conejo",
                "Otro"
        ));

        cbEstado.setItems(FXCollections.observableArrayList(
                "Perdido",
                "Encontrado",
                "En adopción",
                "Adoptado",
                "En observación"
        ));

        cbMoneda.setItems(FXCollections.observableArrayList(
                "CRC",
                "USD"
        ));

        cbEnergia.setItems(FXCollections.observableArrayList(
                "Atlético",
                "Corredor",
                "Caminador",
                "Tranquilo",
                "Para ver TV"
        ));
    }

    @FXML
    public void guardarMascota() {
        lblMensaje.setText("Formulario visual preparado para registrar la mascota.");
    }

    @FXML
    public void limpiarFormulario() {
        lblMensaje.setText("");
    }

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
    }

    @FXML
    public void abrirListaMascotas(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/lista_mascotas.fxml", "Lista de Mascotas");
    }
}

