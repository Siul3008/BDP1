package cr.tec.bd.crv.util;

import cr.tec.bd.crv.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public final class NavigationUtil {

    private NavigationUtil() {
    }

    // Default size for the redesigned desktop screens.
    public static void openWindow(ActionEvent event, String fxmlPath, String title) throws IOException {
        openWindow(event, fxmlPath, title, 1200, 780);
    }

    // Loads an FXML view and reapplies the shared stylesheet.
    public static void openWindow(ActionEvent event, String fxmlPath, String title, double width, double height)
            throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource(fxmlPath));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(Main.class.getResource("/styles/style.css").toExternalForm());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    // Small helper used by placeholder screens until business logic is connected.
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

