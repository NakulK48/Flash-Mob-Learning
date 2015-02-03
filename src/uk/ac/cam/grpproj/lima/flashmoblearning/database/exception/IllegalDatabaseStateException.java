package uk.ac.cam.grpproj.lima.flashmoblearning.database.exception;

import java.sql.SQLException;

/**
 * Created by Spencer on 3/2/2015.
 */
public class IllegalDatabaseStateException extends SQLException {

    public IllegalDatabaseStateException(String message) {
        super(message);
    }

}
