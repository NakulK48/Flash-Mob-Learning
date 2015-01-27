package uk.ac.cam.grpproj.lima.flashmoblearning.database;

/**
 * Created by Spencer on 27/1/2015.
 *
 * This exception is thrown when an invalid query param is provided to the Document/Login manager.
 * For example, requesting a sort by popularity on an unpublished document.
 *
 */
public class InvalidQueryParamException extends Exception {
}