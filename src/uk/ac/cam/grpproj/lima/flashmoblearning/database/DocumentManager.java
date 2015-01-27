package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import java.util.List;

import uk.ac.cam.grpproj.lima.flashmoblearning.PublishedDocument;
import uk.ac.cam.grpproj.lima.flashmoblearning.Revision;
import uk.ac.cam.grpproj.lima.flashmoblearning.Tag;
import uk.ac.cam.grpproj.lima.flashmoblearning.User;
import uk.ac.cam.grpproj.lima.flashmoblearning.WIPDocument;

/** Searches for documents in the database via various keys. Singleton. All database access for
 * documents is through this class, so we can change the schema here.
 */
public abstract class DocumentManager {
	
	/** Get the documents a user has published */
	public abstract List<PublishedDocument> getPublishedByUser(User u, QueryParam param); 
	
	/** Get the documents a user is working on */
	public abstract List<WIPDocument> getWorkInProgressByUser(User u, QueryParam param);
	
	/** Get documents by tag */
	public abstract List<PublishedDocument> getPublishedByTag(Tag t, QueryParam param);

	/** Get featured documents */
	public abstract List<PublishedDocument> getFeatured(QueryParam param);
	
	/** Get all featured documents by tag */
	public abstract List<PublishedDocument> getFeaturedByTag(Tag t, QueryParam param);
	
	/** Get all documents published. Should then be sorted by the ResultList. */
	public abstract List<PublishedDocument> getPublished(QueryParam param);
	
	/** Delete everything owned by a user. Called by Login.deleteUser(). */
	public abstract void deleteUser(User u, QueryParam param);
	
	/** Get all revisions of a given Work In Progress document */
	public abstract List<Revision> getRevisions(WIPDocument d, QueryParam param);
	
	/** Get the one and only revision of a given PublishedDocument */
	public abstract Revision getFinalRevision(PublishedDocument d, QueryParam param);
}
