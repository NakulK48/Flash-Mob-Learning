package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;
import java.util.Date;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** A document that is still being worked on. Has a revision history. Has not been published. */
public class WIPDocument extends Document {
	
	/** Only called by PublishedDocument.fork() */
	WIPDocument(PublishedDocument forked, User newOwner) throws NotInitializedException, SQLException, NoSuchObjectException {
		super(-1, forked.docType, newOwner, forked.getTitle(), System.currentTimeMillis());
	}

    /** Create a new empty document from scratch. Caller must then add a 
     * Revision. If the document is based on another Document, you should call
     * fork() / publish() on that Document instead. */
    public static WIPDocument createDocument(DocumentType doctype, User owner, String title, long time) throws SQLException {
        WIPDocument doc = new WIPDocument(-1, doctype, owner, title, time);
        try {
            DocumentManager.getInstance().createDocument(doc);
        } catch (IDAlreadySetException e) {
            throw new IllegalStateException("ID already set but just created?!");
        }
        return doc;
    }

	/** Only called by DocumentManager */
	public WIPDocument(long id, DocumentType docType, User owner, 
			String title, long time) {
		super(id, docType, owner, title, time);
	}
	
	/** Publish as a PublishedDocument. Creates a new PublishedDocument using the final revision
	 * and calls the database to store it. 
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public PublishedDocument publish() throws NotInitializedException, SQLException, NoSuchObjectException {
		PublishedDocument d = new PublishedDocument(this);
		copyTo(d, getParentDocument());
		return d;
	}
	
	/** Add a Revision to a Document. */
	public void addRevision(Date d, String content) throws NotInitializedException, SQLException, NoSuchObjectException {
		super.addRevision(d, content);
	}

}
