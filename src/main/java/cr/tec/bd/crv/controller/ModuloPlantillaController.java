package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.util.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class ModuloPlantillaController {

    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Bienestar Animal");
    }

    @FXML
    public void mostrarAviso() {
        NavigationUtil.showInfo(
                "Plantilla visual",
                "Esta sección quedó preparada a nivel de interfaz para integrar la lógica del sistema."
        );
    }
}

