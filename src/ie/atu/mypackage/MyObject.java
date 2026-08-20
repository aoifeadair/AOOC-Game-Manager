package ie.atu.mypackage;

import java.io.Serializable;

/**
 * MyObject represents ONE item that your application manages.
 *
 * GUIDANCE (delete these notes as you complete the class):
 *  - Rename this class to the type you are managing (e.g. Book, Car, Phone).
 *  - Keep "implements Serializable" so a list of these can be saved to a file.
 *  - 'id' must UNIQUELY identify each object (e.g. ISBN, registration, IMEI).
 *  - Add any other fields you need, then write a constructor, getters/setters
 *    and a toString().
 *  - You must be able to explain everything you write here in your screencast.
 */
public class MyObject implements Serializable {

    /** Keeps serialisation stable across versions — leave this as-is. */
    private static final long serialVersionUID = 1L;

    /** The UNIQUE identifier for this object. */
    private String id;

    /** A human-readable name for this object. */
    private String name;

    // TODO: add a constructor that sets the fields above.

    // TODO: add getters (and setters if you need them) for each field.

    // TODO: override toString() to return a readable description of the object.
}
