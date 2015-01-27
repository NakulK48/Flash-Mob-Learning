package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.List;

/** A document that is still being worked on. Has a revision history. Has not been published. */
public abstract class WIPDocument extends Document {
	
	/** Every work-in-progress document has a unique ID which never changes. */
	public abstract long getID();
	
	/** Find all revisions */
	public abstract List<Revision> getRevisions();
	
	/** Called when a new revision is saved */
	public abstract void saveRevision(Revision r);

}
