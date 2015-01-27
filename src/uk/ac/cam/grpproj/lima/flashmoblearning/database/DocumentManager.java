package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import java.util.List;

import uk.ac.cam.grpproj.lima.flashmoblearning.Document;
import uk.ac.cam.grpproj.lima.flashmoblearning.PublishedDocument;
import uk.ac.cam.grpproj.lima.flashmoblearning.Revision;
import uk.ac.cam.grpproj.lima.flashmoblearning.Tag;
import uk.ac.cam.grpproj.lima.flashmoblearning.User;
import uk.ac.cam.grpproj.lima.flashmoblearning.WIPDocument;

/** Searches for documents in the database via various keys. Singleton. All database access for
 * documents is through this class, so we can change the schema here.
 * 
 * Rough schema:
 * Published documents (single table, including content and vote count)
 * Works in progress
 * Revisions of works in progress
 * Votes
 * Tags (a single document [ID] can have many tags)
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
	
	/** Delete all documents owned by a user. Called by Login.deleteUser(). */
	public abstract void deleteUser(User u, QueryParam param);
	
	/** Get all revisions of a given Work In Progress document */
	public abstract List<Revision> getRevisions(WIPDocument d, QueryParam param);
	
	/** Get the one and only revision of a given PublishedDocument */
	public abstract Revision getFinalRevision(PublishedDocument d, QueryParam param);
	
	// Note that WIPDocument's and PublishedDocument's are stored differently due to issues
	// with Revision's: WIPDocument's have many revisions in a separate table, PublishedDocument's
	// have one which is stored with the rest of the record. Revision's can be large.
	
	/** Delete a work-in-progress document. */
	public abstract void deleteDocument(WIPDocument d);
	
	/** Delete a published document. Be careful with forks! */
	public abstract void deleteDocument(PublishedDocument d);
	
	/** Add a revision to a Work In Progress document */
	public abstract void addRevision(WIPDocument d, Revision r);
	
	/** Add a new Work In Progress document, either with no revisions or with a single
	 * revision based on another Document. */
	public abstract void createDocument(WIPDocument d);
	
	/** Add a new published document. The revision is stored with the document. */
	public abstract void createDocument(PublishedDocument d);
	
	/** Update a document's metadata (not revisions) */
	public abstract void updateDocument(WIPDocument d);

	/** Update a document's metadata (not revisions) */
	public abstract void updateDocument(PublishedDocument d);
	
	/** Update a document's tags (stored separately) */
	public abstract void updateTags(Document d);
	
	/** Add a (positive) vote on a given document */
	public abstract void addVote(User u, PublishedDocument d);

}
