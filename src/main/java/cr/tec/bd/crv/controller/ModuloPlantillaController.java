package cr.tec.bd.crv.controller;

import cr.tec.bd.crv.util.NavigationUtil;
import cr.tec.bd.crv.util.SessionContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

/**
 * Shared controller for simple placeholder-style actions.
 *
 * <p>Most modules now have their own controller. This class remains useful for
 * small screens or buttons that only need to return to the menu or show a short
 * informational message.</p>
 */
public class ModuloPlantillaController {

    /**
     * Returns to the correct menu depending on the current account type.
     */
    @FXML
    public void volverMenu(ActionEvent event) throws IOException {
        if (SessionContext.isAdmin()) {
            NavigationUtil.openWindow(event, "/view/admin_menu.fxml", "BDP1 - Administration");
            return;
        }

        NavigationUtil.openWindow(event, "/view/menu.fxml", "BDP1 - Animal Welfare");
    }

    /**
     * Gives the user feedback instead of leaving a button with no response.
     */
    @FXML
    public void mostrarAviso() {
        NavigationUtil.showInfo(
                "Accion disponible",
                "Esta opcion se conectara con la informacion del sistema."
        );
    }
}
