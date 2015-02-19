package uk.ac.cam.grpproj.lima.flashmoblearning.database.exception;

import java.sql.SQLException;

/**
 * The database has been deemed to be in an illegal state.
 * This occurs when the tables are checked at initialization and found to be present but incomplete.
 */
public class IllegalDatabaseStateException extends SQLException {

    public IllegalDatabaseStateException(String message) {
        super(message);
    }

}
