package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.List;

/** Searches for documents in the database via various keys. Singleton. All database access for
 * documents is through this class, so we can change the schema here.
 */
public abstract class DocumentManager {
	
	/** Get the documents a user has published */
	public abstract ResultList<PublishedDocument> getPublishedByUser(User u);
	
	/** Get the documents a user is working on */
	public abstract ResultList<WIPDocument> getWorkInProgressByUser(User u);
	
	/** Get documents by tag */
	public abstract ResultList<PublishedDocument> getPublishedByTag(Tag t);

	/** Get all featured documents */
	public abstract ResultList<PublishedDocument> getAllFeatured();
	
	/** Get all featured documents by tag */
	public abstract ResultList<PublishedDocument> getFeaturedByTag(Tag t);
	
	/** Get all documents published. Should then be sorted by the ResultList. */
	public abstract ResultList<PublishedDocument> getAllPublished();
	
	/** Delete everything owned by a user. Called by Login.deleteUser(). */
	public abstract void deleteUser(User u);
	
	/** Get all revisions of a given Work In Progress document */
	abstract List<Revision> getRevisions(WIPDocument d);
	
	/** Get the one and only revision of a given PublishedDocument */
	abstract Revision getFinalRevision(PublishedDocument d);
}
