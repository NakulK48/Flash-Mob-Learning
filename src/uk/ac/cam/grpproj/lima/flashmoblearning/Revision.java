package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.Date;

/** Represents a single version of a document. Note that this does not necessarily correspond to a
 * single database table. */
public class Revision {
	
	/** Owned by a specific user */
	public final User owner;
	/** Creation time */
	public final Date creationTime;
	/** Content. May be relatively large, but no more than a few pages. In any case Revision's
	 * should be short-lived. */
	private final String content;
	// FIXME OPT Consider:
	// 1) Lazily fetching revisions. E.g. keep a RevisionTag with a weak link to a Revision.
	// 2) Lazily fetching content. E.g. keep a weak link to the content.
	// THIS AFFECTS DATABASE SCHEMA! But complexity, best to look at it after have working system.
	
	public Revision(User u, Date d, String c) {
		owner = u;
		creationTime = d;
		content = c;
	}
	
	public String getContent() {
		return content;
	}

}
