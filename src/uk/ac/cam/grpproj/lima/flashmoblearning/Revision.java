package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.Date;

/** Represents a single version of a document. Note that this does not necessarily correspond to a
 * single database table. */
public abstract class Revision {
	
	/** Owned by a specific user */
	public abstract User getOwner();
	
	/** Created at... */
	public abstract Date getCreationTime();
	
	/** Content */
	public abstract String getContent();

}
