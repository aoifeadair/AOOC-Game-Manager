package ie.atu.mypackage;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Main is the entry point of the GameVault JavaFX application.
 * It provides access to all required core operations along with
 * enhanced searching, filtering, sorting and collection statistics.
 */
public class Main extends Application {

    /** Manages all Game objects used by the application. */
    private GameManager gameManager = new GameManager();

    /** Displays the game collection in table form. */
    private TableView<Game> gameTable = new TableView<>();

    /** Search field used to search games by title or ID. */
    private TextField searchField = new TextField();

    /** Drop-down menu used to filter games by genre. */
    private ComboBox<String> genreFilter = new ComboBox<>();

    /** Drop-down menu used to choose a sorting method. */
    private ComboBox<String> sortOptions = new ComboBox<>();

    /** Displays the total number of games. */
    private Label totalLabel = new Label("Total Games: 0");

    /** Displays the average game rating. */
    private Label averageLabel = new Label("Average Rating: 0.0");

    /** Displays the highest-rated game. */
    private Label highestRatedLabel = new Label("Highest Rated: None");

    /** File used to save and load the serialised game database. */
    private static final String FILE_NAME = "resources/games.ser";

    /**
     * Builds and displays the main GameVault window.
     *
     * @param stage the primary JavaFX stage
     */
    @Override
    public void start(Stage stage) {

        /** Main application title. */
        Label titleLabel = new Label("GAMEVAULT");
        titleLabel.getStyleClass().add("title-label");

        /** Subtitle displayed underneath the main title. */
        Label subtitleLabel = new Label("Personal Game Collection Manager");
        subtitleLabel.getStyleClass().add("subtitle-label");

        /** Column used to display each game's unique ID. */
        TableColumn<Game, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        /** Column used to display each game's title. */
        TableColumn<Game, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<>("title")
        );

        /** Column used to display each game's genre. */
        TableColumn<Game, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(
                new PropertyValueFactory<>("genre")
        );

        /** Column used to display each game's platform. */
        TableColumn<Game, String> platformColumn = new TableColumn<>("Platform");
        platformColumn.setCellValueFactory(
                new PropertyValueFactory<>("platform")
        );

        /** Column used to display each game's release year. */
        TableColumn<Game, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(
                new PropertyValueFactory<>("releaseYear")
        );

        /** Column used to display each game's rating. */
        TableColumn<Game, Double> ratingColumn = new TableColumn<>("Rating");
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

        gameTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        idColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        titleColumn.setMaxWidth(1f * Integer.MAX_VALUE * 28);
        genreColumn.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        platformColumn.setMaxWidth(1f * Integer.MAX_VALUE * 16);
        yearColumn.setMaxWidth(1f * Integer.MAX_VALUE * 12);
        ratingColumn.setMaxWidth(1f * Integer.MAX_VALUE * 14);

        gameTable.setPlaceholder(
                new Label(
                        "No games to display. Load the database or add a game."
                )
        );

        /** Loads the saved game database. */
        Button loadButton = new Button("Load DB");
        loadButton.getStyleClass().add("secondary-button");

        /** Saves the current game database. */
        Button saveButton = new Button("Save DB");
        saveButton.getStyleClass().add("primary-button");

        /** Adds a new game to the collection. */
        Button addButton = new Button("+ Add Game");
        addButton.getStyleClass().add("primary-button");

        /** Deletes an existing game. */
        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("danger-button");

        /** Finds an exact game using its ID or title. */
        Button findButton = new Button("Find");

        /** Displays collection statistics. */
        Button totalButton = new Button("Statistics");

        /** Clears search and filtering controls. */
        Button clearFiltersButton = new Button("Clear");

        /** Closes the application. */
        Button quitButton = new Button("Quit");
        quitButton.getStyleClass().add("quit-button");

        searchField.setPromptText("Search title or ID...");
        searchField.setPrefWidth(220);

        genreFilter.getItems().add("All Genres");
        genreFilter.setValue("All Genres");
        genreFilter.setPrefWidth(140);

        sortOptions.getItems().addAll(
                "Default",
                "Title A-Z",
                "Highest Rating",
                "Newest First"
        );

        sortOptions.setValue("Default");
        sortOptions.setPrefWidth(145);

        loadButton.setOnAction(event -> {
            gameManager.loadFromFile(FILE_NAME);

            updateGenreFilter();
            refreshTable();
            updateStatistics();

            showMessage(
                    "Database Loaded",
                    "The game database has been loaded successfully."
            );
        });

        saveButton.setOnAction(event -> {
            gameManager.saveToFile(FILE_NAME);

            showMessage(
                    "Database Saved",
                    "The game database has been saved successfully."
            );
        });

        addButton.setOnAction(event -> addGame());

        deleteButton.setOnAction(event -> deleteGame());

        findButton.setOnAction(event -> findGame());

        totalButton.setOnAction(event -> showStatistics());

        clearFiltersButton.setOnAction(event -> {
            searchField.clear();
            genreFilter.setValue("All Genres");
            sortOptions.setValue("Default");
            refreshTable();
        });

        quitButton.setOnAction(event -> stage.close());

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> refreshTable()
        );

        genreFilter.setOnAction(
                event -> refreshTable()
        );

        sortOptions.setOnAction(event -> {
            applySorting();
            refreshTable();
        });

        /** Holds the title and subtitle. */
        VBox titleBox = new VBox(
                2,
                titleLabel,
                subtitleLabel
        );

        HBox.setHgrow(
                titleBox,
                Priority.ALWAYS
        );

        /** Holds database controls on the right of the header. */
        HBox databaseButtons = new HBox(
                8,
                loadButton,
                saveButton
        );

        databaseButtons.setAlignment(Pos.CENTER_RIGHT);

        /** Top section containing branding and database controls. */
        HBox header = new HBox(
                15,
                titleBox,
                databaseButtons
        );

        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header");

        /** Label describing the search section. */
        Label searchLabel = new Label("Search & Filter");
        searchLabel.getStyleClass().add("section-label");

        /** First row of search controls. */
        HBox filterRowOne = new HBox(
                10,
                searchField,
                genreFilter
        );

        filterRowOne.setAlignment(Pos.CENTER_LEFT);

        HBox.setHgrow(
                searchField,
                Priority.ALWAYS
        );

        searchField.setMaxWidth(Double.MAX_VALUE);

        /** Second row of sorting controls. */
        HBox filterRowTwo = new HBox(
                10,
                sortOptions,
                clearFiltersButton
        );

        filterRowTwo.setAlignment(Pos.CENTER_LEFT);

        /** Holds the search and filtering controls in two rows. */
        VBox filterControls = new VBox(
                8,
                filterRowOne,
                filterRowTwo
        );

        /** Holds the collection statistics. */
        HBox statisticsBox = new HBox(
                10,
                createStatCard(totalLabel),
                createStatCard(averageLabel),
                createStatCard(highestRatedLabel)
        );

        statisticsBox.setAlignment(Pos.CENTER);

        HBox.setHgrow(
                statisticsBox.getChildren().get(0),
                Priority.ALWAYS
        );

        HBox.setHgrow(
                statisticsBox.getChildren().get(1),
                Priority.ALWAYS
        );

        HBox.setHgrow(
                statisticsBox.getChildren().get(2),
                Priority.ALWAYS
        );

        /** Holds the main management buttons. */
        HBox actionButtons = new HBox(
                8,
                addButton,
                deleteButton,
                findButton,
                totalButton
        );

        actionButtons.setAlignment(Pos.CENTER_LEFT);

        /** Bottom bar containing management controls and Quit. */
        HBox bottomBar = new HBox(
                10,
                actionButtons,
                quitButton
        );

        HBox.setHgrow(
                actionButtons,
                Priority.ALWAYS
        );

        bottomBar.setAlignment(Pos.CENTER);

        /** Main vertical layout used by GameVault. */
        VBox root = new VBox(
                14,
                header,
                searchLabel,
                filterControls,
                gameTable,
                statisticsBox,
                bottomBar
        );

        root.setPadding(new Insets(18));

        /** Main JavaFX scene. */
        Scene scene = new Scene(
                root,
                820,
                600
        );

        scene.getStylesheets().add(
                getClass()
                        .getResource("/styles.css")
                        .toExternalForm()
        );

        stage.setTitle("GameVault");
        stage.setMinWidth(760);
        stage.setMinHeight(560);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates a styled statistics card containing a label.
     *
     * @param label the statistic label to display
     * @return a VBox representing the statistics card
     */
    private VBox createStatCard(Label label) {

        /** Container used to display one statistic. */
        VBox card = new VBox(label);

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(12));
        card.setMinWidth(0);
        card.setMaxWidth(Double.MAX_VALUE);
        card.getStyleClass().add("stat-card");

        return card;
    }

    /**
     * Opens input dialogs and creates a new Game object.
     */
    private void addGame() {

        /** Stores the unique ID entered by the user. */
        String id = getInput(
                "Add Game",
                "Enter game ID:"
        );

        if (id == null || id.trim().isEmpty()) {
            return;
        }

        id = id.trim();

        if (gameManager.findGameById(id) != null) {
            showMessage(
                    "Duplicate ID",
                    "A game with that ID already exists."
            );
            return;
        }

        /** Stores the title entered by the user. */
        String title = getInput(
                "Add Game",
                "Enter game title:"
        );

        if (title == null || title.trim().isEmpty()) {
            showMessage(
                    "Invalid Title",
                    "The game title cannot be empty."
            );
            return;
        }

        /** Stores the genre entered by the user. */
        String genre = getInput(
                "Add Game",
                "Enter genre:"
        );

        if (genre == null || genre.trim().isEmpty()) {
            showMessage(
                    "Invalid Genre",
                    "The genre cannot be empty."
            );
            return;
        }

        /** Stores the platform entered by the user. */
        String platform = getInput(
                "Add Game",
                "Enter platform:"
        );

        if (platform == null || platform.trim().isEmpty()) {
            showMessage(
                    "Invalid Platform",
                    "The platform cannot be empty."
            );
            return;
        }

        /** Stores release year input before conversion. */
        String yearText = getInput(
                "Add Game",
                "Enter release year:"
        );

        if (yearText == null) {
            return;
        }

        /** Stores rating input before conversion. */
        String ratingText = getInput(
                "Add Game",
                "Enter rating from 0 to 10:"
        );

        if (ratingText == null) {
            return;
        }

        try {

            /** Converted numeric release year. */
            int releaseYear = Integer.parseInt(
                    yearText.trim()
            );

            /** Converted numeric game rating. */
            double rating = Double.parseDouble(
                    ratingText.trim()
            );

            /** Stores the current calendar year. */
            int currentYear = Year.now().getValue();

            if (releaseYear < 1950
                    || releaseYear > currentYear + 2) {

                showMessage(
                        "Invalid Year",
                        "Please enter a realistic release year."
                );
                return;
            }

            if (rating < 0 || rating > 10) {

                showMessage(
                        "Invalid Rating",
                        "The rating must be between 0 and 10."
                );
                return;
            }

            /** Newly created Game object. */
            Game game = new Game(
                    id,
                    title.trim(),
                    genre.trim(),
                    platform.trim(),
                    releaseYear,
                    rating
            );

            gameManager.addGame(game);

            updateGenreFilter();
            refreshTable();
            updateStatistics();

            showMessage(
                    "Game Added",
                    game.getTitle()
                            + " was added successfully."
            );

        } catch (NumberFormatException exception) {

            showMessage(
                    "Invalid Input",
                    "Release year must be a whole number "
                            + "and rating must be numeric."
            );
        }
    }

    /**
     * Deletes a game after asking the user for confirmation.
     */
    private void deleteGame() {

        /** Currently selected game in the table. */
        Game selectedGame =
                gameTable
                        .getSelectionModel()
                        .getSelectedItem();

        /** ID of the game that should be removed. */
        String id;

        if (selectedGame != null) {

            id = selectedGame.getId();

        } else {

            id = getInput(
                    "Delete Game",
                    "Select a game in the table or enter its ID:"
            );

            if (id == null || id.trim().isEmpty()) {
                return;
            }

            id = id.trim();
        }

        /** Game matching the supplied ID. */
        Game gameToDelete =
                gameManager.findGameById(id);

        if (gameToDelete == null) {

            showMessage(
                    "Game Not Found",
                    "No game with that ID was found."
            );

            return;
        }

        /** Confirmation dialog shown before deletion. */
        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle("Confirm Delete");

        confirmation.setHeaderText(
                "Delete " + gameToDelete.getTitle() + "?"
        );

        confirmation.setContentText(
                "This will remove the game from the collection."
        );

        /** Stores the user's confirmation choice. */
        Optional<ButtonType> result =
                confirmation.showAndWait();

        if (result.isPresent()
                && result.get() == ButtonType.OK) {

            gameManager.removeGame(id);

            updateGenreFilter();
            refreshTable();
            updateStatistics();

            showMessage(
                    "Game Deleted",
                    gameToDelete.getTitle()
                            + " was deleted successfully."
            );
        }
    }

    /**
     * Searches for a game using its ID or exact title.
     */
    private void findGame() {

        /** Stores the search value entered by the user. */
        String searchText = getInput(
                "Find Game",
                "Enter a game ID or exact title:"
        );

        if (searchText == null
                || searchText.trim().isEmpty()) {

            return;
        }

        /** Stores a matching game if one is found. */
        Game game =
                gameManager.findGameById(
                        searchText.trim()
                );

        if (game == null) {

            game = gameManager.findGameByTitle(
                    searchText.trim()
            );
        }

        if (game != null) {

            searchField.clear();
            genreFilter.setValue("All Genres");

            refreshTable();

            gameTable
                    .getSelectionModel()
                    .select(game);

            gameTable.scrollTo(game);

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
     * Sorts the collection using the selected sorting option.
     */
    private void applySorting() {

        /** Current sorting option selected by the user. */
        String selectedSort =
                sortOptions.getValue();

        if ("Title A-Z".equals(selectedSort)) {

            gameManager.sortByTitle();

        } else if ("Highest Rating".equals(selectedSort)) {

            gameManager.sortByRating();

        } else if ("Newest First".equals(selectedSort)) {

            gameManager.sortByYear();
        }
    }

    /**
     * Refreshes the table using the active search and genre filters.
     */
    private void refreshTable() {

        /** Temporary copy of all stored games. */
        List<Game> displayedGames =
                new ArrayList<>(
                        gameManager.getGames()
                );

        /** Current text entered into the search field. */
        String searchText =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();

        /** Current genre selected by the user. */
        String selectedGenre =
                genreFilter.getValue();

        displayedGames =
                displayedGames
                        .stream()
                        .filter(game ->
                                searchText.isEmpty()
                                        || game.getTitle()
                                        .toLowerCase()
                                        .contains(searchText)
                                        || game.getId()
                                        .toLowerCase()
                                        .contains(searchText)
                        )
                        .filter(game ->
                                selectedGenre == null
                                        || selectedGenre.equals(
                                        "All Genres"
                                )
                                        || game.getGenre()
                                        .equalsIgnoreCase(
                                                selectedGenre
                                        )
                        )
                        .toList();

        /** Observable version of the filtered collection. */
        ObservableList<Game> tableData =
                FXCollections.observableArrayList(
                        displayedGames
                );

        gameTable.setItems(tableData);
    }

    /**
     * Updates the genre filter using genres found in the collection.
     */
    private void updateGenreFilter() {

        /** Stores the currently selected genre. */
        String selectedGenre =
                genreFilter.getValue();

        /** Unique sorted genres found in the collection. */
        List<String> genres =
                gameManager
                        .getGames()
                        .stream()
                        .map(Game::getGenre)
                        .distinct()
                        .sorted(
                                String.CASE_INSENSITIVE_ORDER
                        )
                        .toList();

        genreFilter.getItems().clear();
        genreFilter.getItems().add("All Genres");
        genreFilter.getItems().addAll(genres);

        if (selectedGenre != null
                && genreFilter
                .getItems()
                .contains(selectedGenre)) {

            genreFilter.setValue(selectedGenre);

        } else {

            genreFilter.setValue("All Genres");
        }
    }

    /**
     * Updates the statistics displayed underneath the table.
     */
    private void updateStatistics() {

        /** Total number of stored games. */
        int total =
                gameManager.getTotalGames();

        /** Average rating across the collection. */
        double average =
                gameManager.getAverageRating();

        /** Highest-rated game in the collection. */
        Game highest =
                gameManager.getHighestRatedGame();

        totalLabel.setText(
                "Total Games\n" + total
        );

        averageLabel.setText(
                String.format(
                        "Average Rating\n%.1f / 10",
                        average
                )
        );

        if (highest != null) {

            highestRatedLabel.setText(
                    "Highest Rated\n"
                            + highest.getTitle()
                            + " — "
                            + highest.getRating()
            );

        } else {

            highestRatedLabel.setText(
                    "Highest Rated\nNone"
            );
        }
    }

    /**
     * Displays the main collection statistics in an alert.
     */
    private void showStatistics() {

        /** Highest-rated game in the collection. */
        Game highest =
                gameManager.getHighestRatedGame();

        /** Text describing the highest-rated game. */
        String highestText =
                highest == null
                        ? "None"
                        : highest.getTitle()
                        + " ("
                        + highest.getRating()
                        + ")";

        showMessage(
                "Collection Statistics",
                "Total Games: "
                        + gameManager.getTotalGames()
                        + "\nAverage Rating: "
                        + String.format(
                        "%.1f",
                        gameManager.getAverageRating()
                )
                        + "\nHighest Rated: "
                        + highestText
        );
    }

    /**
     * Displays a text input dialog.
     *
     * @param title the dialog title
     * @param message the message shown to the user
     * @return entered text, or null if cancelled
     */
    private String getInput(
            String title,
            String message
    ) {

        /** Dialog used to collect user text input. */
        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        return dialog
                .showAndWait()
                .orElse(null);
    }

    /**
     * Displays an information alert.
     *
     * @param title the alert title
     * @param message the message shown to the user
     */
    private void showMessage(
            String title,
            String message
    ) {

        /** Alert used to display information. */
        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

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
