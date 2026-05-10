package cr.tec.bd.crv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Starts the Bienestar Animal desktop application.
 *
 * <p>Think of this class as the front door of the program: JavaFX calls it first,
 * it loads the first visual screen, applies the shared design file, and then
 * shows the window to the user.</p>
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // The app starts at the login screen so every action can be tied to a user or admin account.
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

    /**
     * Standard Java launcher. It hands control to JavaFX, which then calls start().
     */
    public static void main(String[] args) {
        launch(args);
    }
}
