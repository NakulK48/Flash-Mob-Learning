package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.lang.ref.SoftReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.QueryParam;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** A document that is still being worked on. Has a revision history. Has not been published. */
public class WIPDocument extends Document {
	
	public WIPDocument(PublishedDocument forked, User newOwner, Revision initialRevision) throws NotInitializedException, SQLException, NoSuchObjectException {
		super(forked.docType, newOwner, forked, forked.getTitle(), System.currentTimeMillis());
		DocumentManager.getInstance().addRevision(this, initialRevision);
	}
	
	/** Publish as a PublishedDocument. Creates a new PublishedDocument using the final revision
	 * and calls the database to store it. */
	public PublishedDocument publish() {
		return new PublishedDocument(this);
	}

}
