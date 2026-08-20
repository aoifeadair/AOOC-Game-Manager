package ie.atu.mypackage;

import java.io.Serializable;

/**
 * Represents one game in the GameVault collection.
 * Each game has a unique ID used to identify it.
 */
public class Game implements Serializable {

    /** Keeps serialisation stable across different versions of the class. */
    private static final long serialVersionUID = 1L;

    /** The unique identifier for the game. */
    private String id;

    /** The title of the game. */
    private String title;

    /** The genre of the game. */
    private String genre;

    /** The platform the game is played on. */
    private String platform;

    /** The year the game was released. */
    private int releaseYear;

    /** The user's rating for the game. */
    private double rating;

    /**
     * Creates a new Game object.
     *
     * @param id the unique identifier
     * @param title the title of the game
     * @param genre the genre of the game
     * @param platform the platform of the game
     * @param releaseYear the release year
     * @param rating the user's rating
     */
    public Game(String id, String title, String genre, String platform,
                int releaseYear, double rating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    /**
     * Gets the unique game ID.
     *
     * @return the game ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the game ID.
     *
     * @param id the new game ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the game title.
     *
     * @return the game title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the game title.
     *
     * @param title the new game title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the game genre.
     *
     * @return the game genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the game genre.
     *
     * @param genre the new game genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Gets the game platform.
     *
     * @return the game platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Sets the game platform.
     *
     * @param platform the new game platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * Gets the release year.
     *
     * @return the release year
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Sets the release year.
     *
     * @param releaseYear the new release year
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Gets the game rating.
     *
     * @return the game rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Sets the game rating.
     *
     * @param rating the new rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Returns a readable description of the game.
     *
     * @return a string containing the game's details
     */
    @Override
    public String toString() {
        return id + " - " + title + " (" + genre + ", " + platform
                + ", " + releaseYear + ", Rating: " + rating + ")";
    }
}