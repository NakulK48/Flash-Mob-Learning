package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** A document that is still being worked on. Has a revision history. Has not been published. */
public class WIPDocument extends Document {
	
	/** Only called by PublishedDocument.fork() */
	WIPDocument(PublishedDocument forked, User newOwner) throws NotInitializedException, SQLException, NoSuchObjectException {
		super(forked.docType, newOwner, forked, forked.getTitle(), System.currentTimeMillis());
	}

	/** Only called by DocumentManager */
	public WIPDocument(long id, DocumentType docType, User owner, Document parentDoc,
			String title, long time) {
		super(docType, owner, parentDoc, title, time);
		this.id = id;
	}
	
	/** Publish as a PublishedDocument. Creates a new PublishedDocument using the final revision
	 * and calls the database to store it. 
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public PublishedDocument publish() throws NotInitializedException, SQLException, NoSuchObjectException {
		PublishedDocument d = new PublishedDocument(this);
		try {
			DocumentManager.getInstance().createDocument(d);
		} catch (IDAlreadySetException e) {
			throw new IllegalStateException("ID already set but just created?!");
		}
		return d;
	}

}
