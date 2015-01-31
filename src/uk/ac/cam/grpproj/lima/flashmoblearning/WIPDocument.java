package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** A document that is still being worked on. Has a revision history. Has not been published. */
public class WIPDocument extends Document {
	
	/** Only called by PublishedDocument.fork() */
	WIPDocument(PublishedDocument forked, User newOwner, Revision initialRevision) throws NotInitializedException, SQLException, NoSuchObjectException {
		super(forked.docType, newOwner, forked, forked.getTitle(), System.currentTimeMillis());
	}

	/** Only called by DocumentManager */
	public WIPDocument(DocumentType docType, User owner, Document parentDoc,
			String title, long time) {
		super(docType, owner, parentDoc, title, time);
	}
	
	/** Publish as a PublishedDocument. Creates a new PublishedDocument using the final revision
	 * and calls the database to store it. */
	public PublishedDocument publish() {
		return new PublishedDocument(this);
	}

}
