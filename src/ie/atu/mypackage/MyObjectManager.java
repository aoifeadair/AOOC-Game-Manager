package ie.atu.mypackage;

import java.util.ArrayList;

/**
 * MyObjectManager stores and manages your collection of {@link MyObject}s.
 *
 * GUIDANCE (delete these notes as you complete the class):
 *  - Store your objects in the ArrayList below.
 *  - Implement every method. The brief requires: add, remove, search,
 *    a total count, and save/load to a file.
 *  - Use the Stream API together with a lambda in at least one method
 *    (the search methods are a good place for this).
 *  - Saving must use OBJECT SERIALISATION; loading must DESERIALISE.
 *  - Wrap your file reading/writing in try/catch (exception handling).
 *
 * The stubs below return placeholder values so the project COMPILES before you
 * start (remember: code that does not compile is capped at 40%). Replace each
 * method body with your own code.
 */
public class MyObjectManager {

    /** The in-memory store that will hold every object. */
    private ArrayList<MyObject> items = new ArrayList<>();

    /**
     * Add an object to the list.
     *
     * @param item the object to add
     */
    public void add(MyObject item) {
        // TODO: add 'item' to the ArrayList.
    }

    /**
     * Remove the object with the given id.
     *
     * @param id the unique id to remove
     * @return true if an object was removed, false otherwise
     */
    public boolean remove(String id) {
        // TODO: find and remove the matching object; return whether it worked.
        return false;
    }

    /**
     * Find an object by its unique id.
     * Hint: this is a good place to use the Stream API and a lambda.
     *
     * @param id the id to search for
     * @return the matching object, or null if none was found
     */
    public MyObject findById(String id) {
        // TODO: search the list for the object with this id and return it.
        return null;
    }

    /**
     * Find an object by its name.
     *
     * @param name the name to search for
     * @return the matching object, or null if none was found
     */
    public MyObject findByName(String name) {
        // TODO: search the list for the object with this name and return it.
        return null;
    }

    /**
     * @return the total number of objects currently stored
     */
    public int getTotal() {
        // TODO: return how many objects are in the list.
        return 0;
    }

    /**
     * Save (serialise) the whole list to a file.
     *
     * @param fileName the file to write to (e.g. "resources/myObjects.ser")
     */
    public void saveToFile(String fileName) {
        // TODO: write 'items' to the file using object serialisation.
        //       Remember to handle exceptions with try/catch.
    }

    /**
     * Load (deserialise) the list back from a file.
     *
     * @param fileName the file to read from (e.g. "resources/myObjects.ser")
     */
    public void loadFromFile(String fileName) {
        // TODO: read 'items' back from the file using deserialisation.
        //       Remember to handle exceptions with try/catch.
    }
}
