package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;

/** A document that is still being worked on. Has a revision history. Has not been published. */
public class WIPDocument extends Document {
	
	public WIPDocument(PublishedDocument forked, User newOwner) {
		super(forked.docType, newOwner, forked, forked.getTitle());
		revisions = new LinkedList<Revision>();
	}
	
	private final LinkedList<Revision> revisions;

	/** Find all revisions */
	public synchronized List<Revision> getRevisions() {
		return Collections.unmodifiableList(new ArrayList<Revision>(revisions));
	}
	
	/** Called when a new revision is saved */
	public void saveRevision(Revision r) {
		synchronized(this) {
			assert(!revisions.contains(r));
			revisions.add(r);
		}
		DocumentManager.getInstance().addRevision(this, r);
	}
	
	/** Publish as a PublishedDocument. Creates a new PublishedDocument using the final revision
	 * and calls the database to store it. */
	public PublishedDocument publish() {
		return new PublishedDocument(this);
	}

	public Revision getLastRevision() {
		synchronized(this) {
			return revisions.getLast();
		}
	}

}
