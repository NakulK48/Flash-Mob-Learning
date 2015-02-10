package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import java.sql.*;
import java.util.*;
import java.util.Date;

import uk.ac.cam.grpproj.lima.flashmoblearning.*;
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
public class DocumentManager {

	private Database m_Database;

	protected DocumentManager(Database database) {
		m_Database = database;
	}

	public static DocumentManager getInstance() throws NotInitializedException {
		return Database.getInstance().getDocumentManager();
	}

	private List<PublishedDocument> getPublishedDocumentsFromResultSet(ResultSet rs, User u) throws SQLException, NoSuchObjectException {
		List<PublishedDocument> ret = new ArrayList<PublishedDocument>();

		while(rs.next())
			ret.add(getPublishedDocumentFromResultSet(rs, u));

		return ret;
	}

	private List<WIPDocument> getWIPDocumentsFromResultSet(ResultSet rs, User u) throws SQLException, NoSuchObjectException {
		List<WIPDocument> ret = new ArrayList<WIPDocument>();

		while(rs.next())
			ret.add(getWIPDocumentFromResultSet(rs, u));

		return ret;
	}

	private PublishedDocument getPublishedDocumentFromResultSet(ResultSet rs, User u) throws SQLException, NoSuchObjectException {
		return new PublishedDocument(rs.getLong("id"), DocumentType.getValue(rs.getInt("type")), u == null ? LoginManager.getInstance().getUser(rs.getLong("user_id")) : u,
				rs.getString("title"), rs.getTimestamp("update_time").getTime(), rs.getInt("vote_count"), rs.getDouble("score"));
	}

	private WIPDocument getWIPDocumentFromResultSet(ResultSet rs, User u) throws SQLException, NoSuchObjectException {
		return new WIPDocument(rs.getLong("id"), DocumentType.getValue(rs.getInt("type")), u == null ? LoginManager.getInstance().getUser(rs.getLong("user_id")) : u,
                rs.getString("title"), rs.getTimestamp("update_time").getTime());
	}

	private Document getDocumentFromResultSet(ResultSet rs, User u) throws SQLException, NoSuchObjectException {
		if(rs.getBoolean("published_flag")) {
			return getPublishedDocumentFromResultSet(rs, u);
		} else {
			return getWIPDocumentFromResultSet(rs, u);
		}
	}

	private LinkedList<Revision> getRevisionsFromResultSet(ResultSet rs, Document d) throws SQLException {
		LinkedList<Revision> ret = new LinkedList<Revision>();

		while(rs.next())
			ret.add(new Revision(rs.getLong("id"), rs.getDate("update_time"), d));

		return ret;
	}

	/** Get the documents a user has published */
	public List<PublishedDocument> getPublishedByUser(User u, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE user_id = ? AND published_flag = true"));
		ps.setLong(1, u.getID());
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, u);
	}

	/** Get the documents a user is working on */
	public List<WIPDocument> getWorkInProgressByUser(User u, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE user_id = ? AND published_flag = false"));
		ps.setLong(1, u.getID());
		ResultSet rs = ps.executeQuery();
		return getWIPDocumentsFromResultSet(rs, u);
	}

	/** Get documents by tag */
	public List<PublishedDocument> getPublishedByTag(Tag t, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(
				param.updateQuery("SELECT * FROM documents LEFT JOIN document_tags ON (documents.id = document_tags.document_id) WHERE document_tags.tag_id = ? AND documents.published_flag = true"));
		ps.setLong(1, t.getID());
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, null);
	}
	
	/** Search for published documents by title */
	public List<PublishedDocument> getPublishedByTitle(String title, QueryParam param) throws SQLException, NoSuchObjectException { 
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE LOWER(title) LIKE ? AND published_flag = true"));
		ps.setString(1, "%" + title.toLowerCase() + "%");
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, null);
	}

	/** Search for published documents by title */
	public List<PublishedDocument> getPublishedByExactTitle(String title, QueryParam param) throws SQLException, NoSuchObjectException { 
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE title = ? AND published_flag = true"));
		ps.setString(1, title);
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, null);
	}

	/** Search for in-progress documents by title */
	public List<WIPDocument> getWIPByExactTitle(String title, QueryParam param) throws SQLException, NoSuchObjectException { 
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE title = ? AND published_flag = false"));
		ps.setString(1, title);
		ResultSet rs = ps.executeQuery();
		return getWIPDocumentsFromResultSet(rs, null);
	}

	/** Get featured documents */
	public List<PublishedDocument> getFeatured(QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE featured_flag = true"));
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, null);
	}

	/** Get all featured documents by tag */
	public List<PublishedDocument> getFeaturedByTag(Tag t, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(
				param.updateQuery("SELECT * FROM documents LEFT JOIN document_tags ON (documents.id = document_tags.document_id) WHERE document_tags.tag_id = ? AND documents.featured_flag = true"));
		ps.setLong(1, t.getID());
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, null);
	}

	/** Get all documents published. Should then be sorted by the ResultList. */
	public List<PublishedDocument> getPublished(QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE published_flag = true"));
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, null);
	}

	/** Delete all documents owned by a user. Called by Login.deleteUser(). */
	public void deleteAllDocumentsByUser(User u) throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("DELETE FROM documents WHERE user_id = ?");
		ps.setLong(1, u.getID());
		ps.executeUpdate();
	}

	/** Get all revisions of a given document; published documents will have a single revision */
	public LinkedList<Revision> getRevisions(Document d, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT id, document_id, update_time FROM revisions WHERE document_id = ?"));
		ps.setLong(1, d.getID());
		ResultSet rs = ps.executeQuery();
		return getRevisionsFromResultSet(rs, d);
	}

	/** Delete a work-in-progress document. */
	public void deleteDocument(Document d) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("DELETE FROM documents WHERE id = ?");
		ps.setLong(1, d.getID());

		int affected_rows = ps.executeUpdate();
		if(affected_rows < 1) throw new NoSuchObjectException("document " + d.getID());
	}

	/** Called when a revision is added to a document.
	 * @param payload The actual content of the revision, which is not stored 
	 * in the Revision object. */
	public Revision addRevision(Document doc, Date d, String payload) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("INSERT INTO revisions (document_id, update_time, content) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, doc.getID());
		ps.setTimestamp(2, new Timestamp(d.getTime()));
		ps.setString(3, payload);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys(); rs.next();
		return new Revision(rs.getLong(1), d, doc);
	}

	/** Called when a document is deleted or published */
	public void deleteRevision(List<Revision> revisions) throws SQLException, NoSuchObjectException {
		for(Revision r : revisions) {
			PreparedStatement ps = m_Database.getConnection().prepareStatement("DELETE FROM revisions WHERE id = ?");
			ps.setLong(1, r.getID());

			int affected_rows = ps.executeUpdate();
			if(affected_rows < 1) throw new NoSuchObjectException("revision " + r.getID());
		}
	}

	/** Get a given Document by ID */
	public Document getDocumentById(long id) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT * FROM documents WHERE id = ?");
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			return getDocumentFromResultSet(rs, null);
		} else {
			throw new NoSuchObjectException("document " + id);
		}
	}

	/** Get the parent document for a given Document. Null if there is no parent. */
	public Document getParentDocument(Document document) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT parent_document_id FROM document_parents WHERE document_id = ?");
		ps.setLong(1, document.getID());
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			return getDocumentById(rs.getLong("parent_document_id"));
		} else {
			return null;
		}
	}

	/** Set the parent document for a given document. */
	public void setParentDocument(Document d, Document parentDoc) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("INSERT INTO document_parents (document_id, parent_document_id) VALUES (?, ?)");
		ps.setLong(1, d.getID());
		ps.setLong(2, parentDoc.getID());
		ps.executeUpdate();
	}

	/** Add a new document, either with no revisions or with a single revision based on another Document. */
	public void createDocument(Document d) throws SQLException, IDAlreadySetException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement
				("INSERT INTO documents (user_id, type, title, published_flag, featured_flag, update_time, vote_count) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, d.owner.getID());
		ps.setInt(2, d.docType.getId());
		ps.setString(3, d.getTitle());
		ps.setBoolean(4, d instanceof PublishedDocument);
		ps.setBoolean(5, d instanceof PublishedDocument && ((PublishedDocument)d).getFeatured());
		ps.setTimestamp(6, new Timestamp(d.creationTime));
		ps.setInt(7, 0);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys(); rs.next();
		d.setID(rs.getLong(1));
	}

	/** Update a document's metadata (not revisions). Not including votes and count. **/
	/** If a document's status changes from unpublished to published, all revisions but the latest is truncated **/
	public void updateDocument(Document d) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement
				("UPDATE documents SET user_id = ?, type = ?, title = ?, published_flag = ?, featured_flag = ?, update_time = ? where id = ?");
		ps.setLong(1, d.owner.getID());
		ps.setInt(2, d.docType.getId());
		ps.setString(3, d.getTitle());
		ps.setBoolean(4, d instanceof PublishedDocument);
		ps.setBoolean(5, d instanceof PublishedDocument && ((PublishedDocument) d).getFeatured());
		ps.setTimestamp(6, new Timestamp(d.creationTime));
		ps.setLong(7, d.getID());

		int affected_rows = ps.executeUpdate();
		if(affected_rows < 1) throw new NoSuchObjectException("document " + d.getID());
	}

	/** List all tags. */
	public Set<Tag> getTags() throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT * FROM tags");
		ResultSet rs = ps.executeQuery();
		return getTagsFromResultSet(rs);
	}

	private Set<Tag> getTagsFromResultSet(ResultSet rs) throws SQLException {
		Set<Tag> ret = new HashSet<Tag>();

		while(rs.next())
			ret.add(getTagFromResultSet(rs));

		return ret;
	}

	private Tag getTagFromResultSet(ResultSet rs) throws SQLException {
		return new Tag(rs.getLong("id"), rs.getString("name"), rs.getBoolean("banned_flag"));
	}

	/** List all tags which have documents (for browsing by tag). */
	public Set<Tag> getTagsNotEmpty() throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT * FROM tags INNER JOIN document_tags on document_tags.tag_id = tags.id GROUP BY tags.id");
		ResultSet rs = ps.executeQuery();
		return getTagsFromResultSet(rs);
	}
	
	/** List all tags which exist but have not been banned (for adding a tag). */
	public Set<Tag> getTagsNotBanned() throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT * FROM tags WHERE banned_flag = false");
		ResultSet rs = ps.executeQuery();
		return getTagsFromResultSet(rs);
	}
	
	/** Get tag by name */
	public Tag getTag(String name) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT * FROM tags WHERE name = ?");
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();

		if(rs.next()) {
			return getTagFromResultSet(rs);
		} else {
			throw new NoSuchObjectException("tag " + name);
		}
	}

	/** Get all tags for a particular document */
	public Set<Tag> getTags(Document document) throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT * FROM document_tags INNER JOIN tags on document_tags.tag_id = tags.id WHERE document_id = ?");
		ps.setLong(1, document.getID());
		ResultSet rs = ps.executeQuery();
		return getTagsFromResultSet(rs);
	}

	/** Create tag, or return an existing Tag of the same name, atomically. **/
	public Tag createTag(Tag tag) throws SQLException, NoSuchObjectException, DuplicateNameException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement
				("INSERT INTO tags (name, banned_flag) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, tag.name);
		ps.setBoolean(2, tag.getBanned());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		if(rs.next())
			return new Tag(rs.getLong(1), tag.name, tag.getBanned());
		else
			throw new NoSuchObjectException("failed to create tag " + tag.name);
	}

	/** Add a tag to a document. Throws if the tag is already attached. */
	public void addTag(Document doc, Tag tag) throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("INSERT INTO document_tags (tag_id, document_id) VALUES (?, ?)");
		ps.setLong(1, tag.getID());
		ps.setLong(2, doc.getID());
		ps.executeUpdate();
	}

	/** Remove a tag from a document. Throws if the tag is not present. */
	public void deleteTag(Document doc, Tag tag) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("DELETE FROM document_tags WHERE tag_id = ? AND document_id = ?");
		ps.setLong(1, tag.getID());
		ps.setLong(2, doc.getID());

		int affected_rows = ps.executeUpdate();
		if(affected_rows < 1) throw new NoSuchObjectException("tag " + tag.getID() + " on " + doc.getID());
	}

	/** Update a tag. I.e. it may go from banned to unbanned or vice versa. */
	public void updateTag(Tag tag) throws SQLException, NoSuchObjectException, DuplicateNameException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement
				("UPDATE tags SET name = ?, banned_flag = ? where id = ?");
		ps.setString(1, tag.name);
		ps.setBoolean(2, tag.getBanned());
		ps.setLong(3, tag.getID());
		int affected_rows = ps.executeUpdate();
		if(affected_rows < 1) throw new NoSuchObjectException("tag " + tag.getID());
	}

	/** Delete a tag. Database will delete references from all documents to this tag. */
	public void deleteTag(Tag tag) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("DELETE FROM tags WHERE id = ?");
		ps.setLong(1, tag.getID());

		int affected_rows = ps.executeUpdate();
		if(affected_rows < 1) throw new NoSuchObjectException("tag " + tag.getID());
	}

	/** Delete all references from documents to a given tag. Will be called internally by
	 * deleteTag() but also useful when a tag is banned. */
	public void deleteTagReferences(Tag tag) throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("DELETE FROM document_tags WHERE tag_id = ?");
		ps.setLong(1, tag.getID());
		ps.executeUpdate();
	}
	
	/** Add a (positive) vote on a given document. Updates the total vote count and score
	 * on the document (by statically calling PublishedDocument.getScore(votes,...).
	 * Returns true if succesful, false if failed (user has already voted) */
	public void addVote(User u, PublishedDocument d) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("INSERT INTO votes (user_id, document_id) VALUES (?, ?)");
		ps.setLong(1, u.getID());
		ps.setLong(2, d.getID());
		ps.executeUpdate();

		ps = m_Database.getConnection().prepareStatement("UPDATE documents SET vote_count = vote_count + 1, score = score + 1 WHERE id = ?");
		ps.setLong(1, d.getID());
		ps.executeUpdate();
	}

	/** Get the content of a Revision. This may be kept separately and fetched 
	 * lazily, given its size, and given that we mostly don't need all the 
	 * revisions for all the documents; in particular, we DON'T want to preload
	 * every revision of every document when browsing the index! */
	public String getRevisionContent(Revision revision) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT content FROM revisions WHERE id = ?");
		ps.setLong(1, revision.getID());
		ResultSet rs = ps.executeQuery();

		if(!rs.next()) throw new NoSuchObjectException("revision " + revision.getID());
		return rs.getString("content");
	}

	public void ageScores(QueryParam param) throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(
				param.updateQuery("UPDATE documents SET score = vote_count * EXP(POWER(time_to_sec(timediff(NOW(),update_time)) / 3600,2)/50000) WHERE score > 0"));
		ps.executeUpdate();
	}
}
