package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	
	public static DocumentManager getInstance() {
		return Database.getInstance().getDocumentManager();
	}
	
	/** Get the documents a user has published */
	public abstract List<PublishedDocument> getPublishedByUser(User u, QueryParam param) throws NoSuchObjectException;

	/** Get the documents a user is working on */
	public abstract List<WIPDocument> getWorkInProgressByUser(User u, QueryParam param) throws NoSuchObjectException;
	
	/** Get documents by tag */
	public abstract List<PublishedDocument> getPublishedByTag(Tag t, QueryParam param) throws NoSuchObjectException;

	/** Get featured documents */
	public abstract List<PublishedDocument> getFeatured(QueryParam param) throws NoSuchObjectException;
	
	/** Get all featured documents by tag */
	public abstract List<PublishedDocument> getFeaturedByTag(Tag t, QueryParam param) throws NoSuchObjectException;
	
	/** Get all documents published. Should then be sorted by the ResultList. */
	public abstract List<PublishedDocument> getPublished(QueryParam param) throws NoSuchObjectException;
	
	/** Delete all documents owned by a user. Called by Login.deleteUser(). */
	public abstract void deleteAllDocumentsByUser(User u, QueryParam param) throws NoSuchObjectException;
	
	/** Get all revisions of a given document; published documents will have a single revision */
	public abstract LinkedList<Revision> getRevisions(Document d, QueryParam param) throws NoSuchObjectException;
	
	/** Delete a work-in-progress document. */
	public abstract void deleteDocument(Document d) throws NoSuchObjectException;
	
	/** Called when a revision is added to a document */
	public abstract void addRevision(Document d, Revision r) throws NoSuchObjectException;

	/** Called when a document is deleted or published */
	public abstract void deleteRevision(List<Revision> r) throws NoSuchObjectException;
	
	/** Add a new document, either with no revisions or with a single revision based on another Document. */
	public abstract void createDocument(Document d) throws NoSuchObjectException;
	
	/** Update a document's metadata (not revisions). **/
	/** If a document's status changes from unpublished to published, all revisions but the latest is truncated **/
	public abstract void updateDocument(Document d) throws NoSuchObjectException;
	
	/** List all tags. Note tags are deleted once all references are deleted. */
	public abstract Set<Tag> getTags() throws NoSuchObjectException;
	
	/** Get tag by name */
	public abstract Tag getTag(String name) throws NoSuchObjectException;

	/** Create tag **/
	public abstract void createTag(Tag tag) throws NoSuchObjectException, DuplicateNameException;
	
	/** Update a document's tags (stored separately) */
	public abstract void updateTags(Document d) throws NoSuchObjectException;
	
	/** Update a tag. I.e. it may go from banned to unbanned o vice versa. */
	public abstract void updateTag(Tag tag) throws NoSuchObjectException, DuplicateNameException;
	
	/** Delete a tag. Database will delete references from all documents to this tag. */
	public abstract void deleteTag(Tag tag) throws NoSuchObjectException;

	/** Add a (positive) vote on a given document */
	public abstract void addVote(User u, PublishedDocument d) throws NoSuchObjectException;

}
