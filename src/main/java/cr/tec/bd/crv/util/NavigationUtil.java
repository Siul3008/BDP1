package cr.tec.bd.crv.util;

import cr.tec.bd.crv.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Shared navigation helper for all JavaFX screens.
 *
 * <p>Controllers call this class when the user presses buttons such as
 * "Back", "My profile", or "Donations". Keeping the navigation code here
 * avoids copying the same window-loading steps in every controller.</p>
 */
public final class NavigationUtil {

    private NavigationUtil() {
    }

    /**
     * Opens a screen using the default desktop size used by the application.
     */
    public static void openWindow(ActionEvent event, String fxmlPath, String title) throws IOException {
        openWindow(event, fxmlPath, title, 1200, 780);
    }

    /**
     * Replaces the current screen with another FXML view.
     *
     * <p>The same Stage is reused, so the user stays in one application window
     * instead of accumulating many separate windows.</p>
     */
    public static void openWindow(ActionEvent event, String fxmlPath, String title, double width, double height)
            throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource(fxmlPath));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(wrapScrollable(root), width, height);
        scene.getStylesheets().add(Main.class.getResource("/styles/style.css").toExternalForm());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Gives every screen a scrollable shell so controls stay reachable in small windows.
     */
    public static ScrollPane wrapScrollable(Parent root) {
        root.getStyleClass().add("app-content");

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);
        scrollPane.getStyleClass().add("app-scroll");
        return scrollPane;
    }

    /**
     * Shows a simple information pop-up for short messages.
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
