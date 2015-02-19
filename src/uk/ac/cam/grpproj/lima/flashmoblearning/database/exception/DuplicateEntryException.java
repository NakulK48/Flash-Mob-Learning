package uk.ac.cam.grpproj.lima.flashmoblearning.database.exception;

/**
 * DuplicateEntryException occurs when an insertion or update results in a duplicate entry
 * in the database where duplicates are not allowed. This is commonly the result of duplicate
 * names or many:1 parent-child relations where a 1:1 relationship is expected.
 */
public class DuplicateEntryException extends Exception {

}
