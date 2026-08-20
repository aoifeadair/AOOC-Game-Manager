package ie.atu.mypackage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main is the entry point of the application and builds the JavaFX GUI.
 *
 * GUIDANCE (delete these notes as you build your app):
 *  - Create a MyObjectManager to hold your objects.
 *  - Build a GUI that gives the user access to the 7 required operations:
 *    Load DB, Add Item, Delete Item, Find Item, Show Total, Save to DB, Quit.
 *  - Add a control (e.g. a Button) for each operation and, when it is clicked,
 *    call the matching MyObjectManager method.
 *  - This skeleton just opens an empty window so the project runs from day one;
 *    replace the placeholder below with your own layout and controls.
 */
public class Main extends Application {

    // TODO: create a MyObjectManager instance here to hold your objects.

    /**
     * Builds and shows the main window.
     *
     * @param stage the primary stage supplied by JavaFX
     */
    @Override
    public void start(Stage stage) {
        // TODO: replace this placeholder with your buttons/layout and wire
        //       each button to a MyObjectManager method.
        VBox root = new VBox(new Label("Items Application Manager — build your GUI here!"));

        stage.setTitle("Items Application Manager");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
