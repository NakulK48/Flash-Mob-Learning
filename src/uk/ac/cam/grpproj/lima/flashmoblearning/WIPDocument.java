package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.QueryParam;

/** A document that is still being worked on. Has a revision history. Has not been published. */
public class WIPDocument extends Document {
	
	public WIPDocument(PublishedDocument forked, User newOwner) {
		super(forked.docType, newOwner, forked, forked.getTitle());
	}
	
	private SoftReference<LinkedList<Revision>> revisionsRef;

	public List<Revision> getRevisions() {
		return Collections.unmodifiableList(new ArrayList<Revision>(innerGetRevisions()));
	}
	
	/** Find all revisions */
	private LinkedList<Revision> innerGetRevisions() {
		synchronized(this) {
			if(revisionsRef != null) {
				LinkedList<Revision> r = revisionsRef.get();
				if(r != null) {
					return r;
				} else {
					revisionsRef = null;
				}
			}
		}
		LinkedList<Revision> revisions = DocumentManager.getInstance().getRevisions(this, QueryParam.UNSORTED);
		synchronized(this) {
			if(revisionsRef != null) {
				LinkedList<Revision> r1 = revisionsRef.get();
				if(r1 != null) return r1;
			}
			revisionsRef = new SoftReference<LinkedList<Revision>>(revisions);
		}
		return revisions;
	}
	
	/** Called when a new revision is saved */
	public void saveRevision(Revision r) {
		synchronized(this) {
			if(revisionsRef != null && revisionsRef.get() != null) {
				revisionsRef.get().add(r);
			}
		}
		DocumentManager.getInstance().addRevision(this, r);
	}
	
	/** Publish as a PublishedDocument. Creates a new PublishedDocument using the final revision
	 * and calls the database to store it. */
	public PublishedDocument publish() {
		return new PublishedDocument(this);
	}

	public Revision getLastRevision() {
		// FIXME OPT Consider caching separately. Be careful with consistency between the two!
		return innerGetRevisions().getLast();
	}

}
