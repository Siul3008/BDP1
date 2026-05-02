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
                "Accion disponible",
                "Esta opcion se conectara con la informacion del sistema."
        );
    }
}
