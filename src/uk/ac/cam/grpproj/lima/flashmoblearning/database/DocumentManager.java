package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import uk.ac.cam.grpproj.lima.flashmoblearning.Document;
import uk.ac.cam.grpproj.lima.flashmoblearning.PublishedDocument;
import uk.ac.cam.grpproj.lima.flashmoblearning.Revision;
import uk.ac.cam.grpproj.lima.flashmoblearning.Tag;
import uk.ac.cam.grpproj.lima.flashmoblearning.User;
import uk.ac.cam.grpproj.lima.flashmoblearning.WIPDocument;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateNameException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

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

	private Database m_Database;

	protected DocumentManager(Database database) {
		m_Database = database;
	}

	public static DocumentManager getInstance() throws NotInitializedException {
		return Database.getInstance().getDocumentManager();
	}
	
	/** Get the documents a user has published */
	public abstract List<PublishedDocument> getPublishedByUser(User u, QueryParam param) throws SQLException, NoSuchObjectException;

	/** Get the documents a user is working on */
	public abstract List<WIPDocument> getWorkInProgressByUser(User u, QueryParam param) throws SQLException, NoSuchObjectException;

	/** Get documents by tag */
	public abstract List<PublishedDocument> getPublishedByTag(Tag t, QueryParam param) throws SQLException, NoSuchObjectException;

	/** Get featured documents */
	public abstract List<PublishedDocument> getFeatured(QueryParam param) throws SQLException, NoSuchObjectException;

	/** Get all featured documents by tag */
	public abstract List<PublishedDocument> getFeaturedByTag(Tag t, QueryParam param) throws SQLException, NoSuchObjectException;

	/** Get all documents published. Should then be sorted by the ResultList. */
	public abstract List<PublishedDocument> getPublished(QueryParam param) throws SQLException, NoSuchObjectException;

	/** Delete all documents owned by a user. Called by Login.deleteUser(). */
	public abstract void deleteAllDocumentsByUser(User u, QueryParam param) throws SQLException, NoSuchObjectException;

	/** Get all revisions of a given document; published documents will have a single revision */
	public abstract LinkedList<Revision> getRevisions(Document d, QueryParam param) throws SQLException, NoSuchObjectException;

	/** Delete a work-in-progress document. */
	public abstract void deleteDocument(Document d) throws SQLException, NoSuchObjectException;

	/** Called when a revision is added to a document. Also saves the content 
	 * of the revision, so <b>the caller must pin the content in memory</b> 
	 * (e.g. by holding a reference to it as a String). */
	public abstract void addRevision(Document d, Revision r) throws SQLException, NoSuchObjectException;

	/** Called when a document is deleted or published */
	public abstract void deleteRevision(List<Revision> r) throws SQLException, NoSuchObjectException;

	/** Add a new document, either with no revisions or with a single revision based on another Document. */
	public abstract void createDocument(Document d) throws SQLException, NoSuchObjectException;

	/** Update a document's metadata (not revisions). **/
	/** If a document's status changes from unpublished to published, all revisions but the latest is truncated **/
	public abstract void updateDocument(Document d) throws SQLException, NoSuchObjectException;

	/** List all tags. */
	public abstract Set<Tag> getTags() throws SQLException, NoSuchObjectException;
	
	/** List all tags which have documents (for browsing by tag). */
	public abstract Set<Tag> getTagsNotEmpty() throws SQLException, NoSuchObjectException;
	
	/** List all tags which exist but have not been banned (for adding a tag). */
	public abstract Set<Tag> getTagsNotBanned() throws SQLException, NoSuchObjectException;
	
	/** Get tag by name */
	public abstract Tag getTag(String name) throws SQLException, NoSuchObjectException;

	/** Create tag, or return an existing Tag of the same name, atomically. **/
	public abstract Tag createTag(Tag tag) throws SQLException, NoSuchObjectException, DuplicateNameException;

	/** Update a document's tags (stored separately) */
	public abstract void updateTags(Document d) throws SQLException, NoSuchObjectException;

	/** Update a tag. I.e. it may go from banned to unbanned or vice versa. */
	public abstract void updateTag(Tag tag) throws SQLException, NoSuchObjectException, DuplicateNameException;

	/** Delete a tag. Database will delete references from all documents to this tag. */
	public abstract void deleteTag(Tag tag) throws SQLException, NoSuchObjectException;

	/** Delete all references from documents to a given tag. Will be called internally by
	 * deleteTag() but also useful when a tag is banned. */
	public abstract void deleteTagReferences(Tag tag) throws SQLException, NoSuchObjectException;
	
	/** Add a (positive) vote on a given document */
	public abstract void addVote(User u, PublishedDocument d) throws SQLException, NoSuchObjectException;

	/** Get the content of a Revision. This may be kept separately and fetched 
	 * lazily, given its size, and given that we mostly don't need all the 
	 * revisions for all the documents; in particular, we DON'T want to preload
	 * every revision of every document when browsing the index! */
	public abstract String getRevisionContent(Revision revision) throws SQLException, NoSuchObjectException;

}
