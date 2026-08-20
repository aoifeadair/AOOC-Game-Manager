package ie.atu.mypackage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main is the entry point of the GameVault JavaFX application.
 * It provides the user with access to the seven required core operations.
 */
public class Main extends Application {

    /** Manages all Game objects used by the application. */
    private GameManager gameManager = new GameManager();

    /** File used to save and load the serialized game database. */
    private static final String FILE_NAME = "resources/games.ser";

    /**
     * Builds and displays the main GameVault window.
     *
     * @param stage the primary JavaFX stage
     */
    @Override
    public void start(Stage stage) {

        /** Displays the application title. */
        Label titleLabel = new Label("GameVault - Game Collection Manager");

        /** Loads the serialized game database. */
        Button loadButton = new Button("Load DB");

        /** Adds a new game to the collection. */
        Button addButton = new Button("Add Game");

        /** Deletes a game using its unique ID. */
        Button deleteButton = new Button("Delete Game");

        /** Finds a game using its ID or title. */
        Button findButton = new Button("Find Game");

        /** Shows the total number of games. */
        Button totalButton = new Button("Show Total");

        /** Saves the current collection to the database file. */
        Button saveButton = new Button("Save DB");

        /** Closes the application. */
        Button quitButton = new Button("Quit");

        loadButton.setOnAction(event -> {
            gameManager.loadFromFile(FILE_NAME);
            showMessage(
                    "Database Loaded",
                    "The game database has been loaded successfully."
            );
        });

        addButton.setOnAction(event -> addGame());

        deleteButton.setOnAction(event -> deleteGame());

        findButton.setOnAction(event -> findGame());

        totalButton.setOnAction(event -> {
            showMessage(
                    "Total Games",
                    "There are " + gameManager.getTotalGames()
                            + " games in the collection."
            );
        });

        saveButton.setOnAction(event -> {
            gameManager.saveToFile(FILE_NAME);
            showMessage(
                    "Database Saved",
                    "The game database has been saved successfully."
            );
        });

        quitButton.setOnAction(event -> stage.close());

        VBox root = new VBox(
                12,
                titleLabel,
                loadButton,
                addButton,
                deleteButton,
                findButton,
                totalButton,
                saveButton,
                quitButton
        );

        root.setPadding(new Insets(20));

        stage.setTitle("GameVault");
        stage.setScene(new Scene(root, 420, 420));
        stage.show();
    }

    /**
     * Opens input dialogs and adds a new game to the collection.
     */
    private void addGame() {

        String id = getInput("Add Game", "Enter game ID:");
        if (id == null) {
            return;
        }

        if (gameManager.findGameById(id) != null) {
            showMessage(
                    "Duplicate ID",
                    "A game with that ID already exists."
            );
            return;
        }

        String title = getInput("Add Game", "Enter game title:");
        if (title == null) {
            return;
        }

        String genre = getInput("Add Game", "Enter genre:");
        if (genre == null) {
            return;
        }

        String platform = getInput("Add Game", "Enter platform:");
        if (platform == null) {
            return;
        }

        String yearText = getInput("Add Game", "Enter release year:");
        if (yearText == null) {
            return;
        }

        String ratingText = getInput(
                "Add Game",
                "Enter rating from 0 to 10:"
        );

        if (ratingText == null) {
            return;
        }

        try {
            int releaseYear = Integer.parseInt(yearText);
            double rating = Double.parseDouble(ratingText);

            Game game = new Game(
                    id,
                    title,
                    genre,
                    platform,
                    releaseYear,
                    rating
            );

            gameManager.addGame(game);

            showMessage(
                    "Game Added",
                    title + " was added successfully."
            );

        } catch (NumberFormatException exception) {
            showMessage(
                    "Invalid Input",
                    "Release year must be a whole number and rating must be a number."
            );
        }
    }

    /**
     * Deletes a game from the collection using its unique ID.
     */
    private void deleteGame() {

        String id = getInput(
                "Delete Game",
                "Enter the ID of the game to delete:"
        );

        if (id == null) {
            return;
        }

        boolean removed = gameManager.removeGame(id);

        if (removed) {
            showMessage(
                    "Game Deleted",
                    "The game was deleted successfully."
            );
        } else {
            showMessage(
                    "Game Not Found",
                    "No game with that ID was found."
            );
        }
    }

    /**
     * Searches for a game by ID first and then by title.
     */
    private void findGame() {

        String searchText = getInput(
                "Find Game",
                "Enter a game ID or exact title:"
        );

        if (searchText == null) {
            return;
        }

        Game game = gameManager.findGameById(searchText);

        if (game == null) {
            game = gameManager.findGameByTitle(searchText);
        }

        if (game != null) {
            showMessage(
                    "Game Found",
                    game.toString()
            );
        } else {
            showMessage(
                    "Game Not Found",
                    "No matching game was found."
            );
        }
    }

    /**
     * Displays a text input dialog.
     *
     * @param title the dialog title
     * @param message the message shown to the user
     * @return the entered text, or null if cancelled
     */
    private String getInput(String title, String message) {

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        return dialog.showAndWait().orElse(null);
    }

    /**
     * Displays an information alert.
     *
     * @param title the alert title
     * @param message the message shown to the user
     */
    private void showMessage(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}