package ie.atu.mypackage;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main is the entry point of the GameVault JavaFX application.
 * It provides the user with access to the seven required core operations
 * and displays the game collection in a JavaFX TableView.
 */
public class Main extends Application {

    /** Manages all Game objects used by the application. */
    private GameManager gameManager = new GameManager();

    /** Displays the game collection in table form. */
    private TableView<Game> gameTable = new TableView<>();

    /** File used to save and load the serialised game database. */
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

        /** Column used to display each game's unique ID. */
        TableColumn<Game, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        /** Column used to display each game's title. */
        TableColumn<Game, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        /** Column used to display each game's genre. */
        TableColumn<Game, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        /** Column used to display each game's platform. */
        TableColumn<Game, String> platformColumn =
                new TableColumn<>("Platform");
        platformColumn.setCellValueFactory(
                new PropertyValueFactory<>("platform")
        );

        /** Column used to display each game's release year. */
        TableColumn<Game, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(
                new PropertyValueFactory<>("releaseYear")
        );

        /** Column used to display each game's rating. */
        TableColumn<Game, Double> ratingColumn =
                new TableColumn<>("Rating");
        ratingColumn.setCellValueFactory(
                new PropertyValueFactory<>("rating")
        );

        gameTable.getColumns().addAll(
                idColumn,
                titleColumn,
                genreColumn,
                platformColumn,
                yearColumn,
                ratingColumn
        );

        gameTable.setPrefHeight(300);

        /** Loads the serialised game database. */
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

            refreshTable();

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

        /** Holds the database-related buttons. */
        HBox databaseButtons = new HBox(
                10,
                loadButton,
                saveButton
        );

        /** Holds the main game management buttons. */
        HBox actionButtons = new HBox(
                10,
                addButton,
                deleteButton,
                findButton,
                totalButton
        );

        /** Main vertical layout used by the application. */
        VBox root = new VBox(
                15,
                titleLabel,
                databaseButtons,
                gameTable,
                actionButtons,
                quitButton
        );

        root.setPadding(new Insets(20));

        stage.setTitle("GameVault");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    /**
     * Opens input dialogs and adds a new game to the collection.
     */
    private void addGame() {

        /** Stores the unique ID entered by the user. */
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

        /** Stores the game title entered by the user. */
        String title = getInput("Add Game", "Enter game title:");

        if (title == null) {
            return;
        }

        /** Stores the genre entered by the user. */
        String genre = getInput("Add Game", "Enter genre:");

        if (genre == null) {
            return;
        }

        /** Stores the platform entered by the user. */
        String platform = getInput("Add Game", "Enter platform:");

        if (platform == null) {
            return;
        }

        /** Stores the release year as text before conversion. */
        String yearText = getInput(
                "Add Game",
                "Enter release year:"
        );

        if (yearText == null) {
            return;
        }

        /** Stores the rating as text before conversion. */
        String ratingText = getInput(
                "Add Game",
                "Enter rating from 0 to 10:"
        );

        if (ratingText == null) {
            return;
        }

        try {

            /** Converts the release year input into an integer. */
            int releaseYear = Integer.parseInt(yearText);

            /** Converts the rating input into a double. */
            double rating = Double.parseDouble(ratingText);

            /** Creates a new Game object from the entered information. */
            Game game = new Game(
                    id,
                    title,
                    genre,
                    platform,
                    releaseYear,
                    rating
            );

            gameManager.addGame(game);

            refreshTable();

            showMessage(
                    "Game Added",
                    title + " was added successfully."
            );

        } catch (NumberFormatException exception) {

            showMessage(
                    "Invalid Input",
                    "Release year must be a whole number "
                            + "and rating must be a number."
            );
        }
    }

    /**
     * Deletes a game from the collection using its unique ID.
     */
    private void deleteGame() {

        /** Stores the unique ID entered by the user. */
        String id = getInput(
                "Delete Game",
                "Enter the ID of the game to delete:"
        );

        if (id == null) {
            return;
        }

        /** Records whether a matching game was successfully removed. */
        boolean removed = gameManager.removeGame(id);

        if (removed) {

            refreshTable();

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

        /** Stores the ID or title entered by the user. */
        String searchText = getInput(
                "Find Game",
                "Enter a game ID or exact title:"
        );

        if (searchText == null) {
            return;
        }

        /** Stores the matching game if one is found. */
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
     * Refreshes the table so it displays the current contents
     * of the GameManager ArrayList.
     */
    private void refreshTable() {

        /** Observable version of the GameManager game collection. */
        ObservableList<Game> tableData =
                FXCollections.observableArrayList(
                        gameManager.getGames()
                );

        gameTable.setItems(tableData);
    }

    /**
     * Displays a text input dialog.
     *
     * @param title the dialog title
     * @param message the message shown to the user
     * @return the entered text, or null if cancelled
     */
    private String getInput(String title, String message) {

        /** Dialog used to collect text input from the user. */
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

        /** Alert used to display information to the user. */
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