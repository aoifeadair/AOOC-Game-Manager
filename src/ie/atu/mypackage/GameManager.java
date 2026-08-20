package ie.atu.mypackage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Manages the collection of Game objects.
 * Provides methods for adding, removing, searching, saving and loading games.
 */
public class GameManager {

    /** The ArrayList that stores all games in memory. */
    private ArrayList<Game> games = new ArrayList<>();

    /**
     * Adds a game to the collection.
     *
     * @param game the game to add
     */
    public void addGame(Game game) {
        games.add(game);
    }

    /**
     * Removes a game using its unique ID.
     *
     * @param id the ID of the game to remove
     * @return true if a game was removed, otherwise false
     */
    public boolean removeGame(String id) {
        return games.removeIf(game -> game.getId().equalsIgnoreCase(id));
    }

    /**
     * Finds a game using its unique ID.
     * Uses the Stream API and a lambda expression.
     *
     * @param id the ID to search for
     * @return the matching game, or null if not found
     */
    public Game findGameById(String id) {
        return games.stream()
                .filter(game -> game.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a game using its title.
     * Uses the Stream API and a lambda expression.
     *
     * @param title the title to search for
     * @return the matching game, or null if not found
     */
    public Game findGameByTitle(String title) {
        return games.stream()
                .filter(game -> game.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the total number of games in the collection.
     *
     * @return the number of games
     */
    public int getTotalGames() {
        return games.size();
    }

    /**
     * Saves the complete game collection to a file using object serialisation.
     *
     * @param fileName the file to save the collection to
     */
    public void saveToFile(String fileName) {
        try (ObjectOutputStream outputStream =
                     new ObjectOutputStream(new FileOutputStream(fileName))) {

            outputStream.writeObject(games);

        } catch (IOException e) {
            System.err.println("Error saving games: " + e.getMessage());
        }
    }

    /**
     * Loads the game collection from a serialised file.
     *
     * @param fileName the file to load the collection from
     */
    @SuppressWarnings("unchecked")
    public void loadFromFile(String fileName) {
        try (ObjectInputStream inputStream =
                     new ObjectInputStream(new FileInputStream(fileName))) {

            games = (ArrayList<Game>) inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading games: " + e.getMessage());
        }
    }
}