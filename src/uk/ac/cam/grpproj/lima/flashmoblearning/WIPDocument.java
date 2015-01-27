package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.List;

public abstract class WIPDocument implements Document {
	
	/** Every work-in-progress document has a unique ID which never changes. */
	public abstract long getID();
	
	/** Find all revisions */
	public abstract List<Revision> getRevisions();
	
	/** Called when a new revision is saved */
	public abstract void saveRevision(Revision r);

}
