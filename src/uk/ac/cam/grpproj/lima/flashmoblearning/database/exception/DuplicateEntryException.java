package uk.ac.cam.grpproj.lima.flashmoblearning.database.exception;

import java.sql.SQLException;

/**
 * DuplicateEntryException occurs when an insertion or update results in a duplicate entry
 * in the database where duplicates are not allowed. This is commonly the result of duplicate
 * names or many:1 parent-child relations where a 1:1 relationship is expected.
 */
public class DuplicateEntryException extends Exception {

    public static void handle(SQLException e) throws SQLException, DuplicateEntryException {
        if(e.getSQLState().equals("23000")) throw new DuplicateEntryException();
        else throw e;
    }

}
