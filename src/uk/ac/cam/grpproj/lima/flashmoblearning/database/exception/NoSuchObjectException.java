package uk.ac.cam.grpproj.lima.flashmoblearning.database.exception;

/**
 * This error occurs when the requested object to operate on is not found.
 */
public class NoSuchObjectException extends Exception {

    public NoSuchObjectException(String message) {
        super(message);
    }

}
