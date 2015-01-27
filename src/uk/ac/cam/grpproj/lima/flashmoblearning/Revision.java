package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.Date;

/** Represents a single version of a document. */
public abstract class Revision {
	
	/** Created at... */
	public abstract Date getCreationTime();
	
	/** Content */
	public abstract String getContent();

}
