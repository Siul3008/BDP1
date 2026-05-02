package cr.tec.bd.crv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * JavaFX entry point for the desktop application.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // The app starts at the account type selector, not directly at the main menu.
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/view/login.fxml")
        );

        // All screens share the same stylesheet so the visual identity stays consistent.
        Scene scene = new Scene(loader.load(), 1200, 780);
        scene.getStylesheets().add(
                Main.class.getResource("/styles/style.css").toExternalForm()
        );

        stage.setTitle("BDP1 - Bienestar Animal");
        stage.setScene(scene);
        stage.setResizable(true);

        // The icon is optional so the app can still run if the image resource is missing.
        if (Main.class.getResourceAsStream("/images/logo.png") != null) {
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/logo.png")));
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
