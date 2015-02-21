package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import uk.ac.cam.grpproj.lima.flashmoblearning.*;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateEntryException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * The document manager provides read and write access to documents, revisions, tags and votes within
 * the database. All search, create and listing functionality is implemented here as well.
 */
public class DocumentManager {

	private Database m_Database;

	/**
	 * Creates a new DocumentManager instance with given database.
	 * @param database an initialised database.
	 */
	protected DocumentManager(Database database) {
		m_Database = database;
	}

	/**
	 * Obtain the static DocumentManager instance.
	 * @return The static DocumentManager instance.
	 * @throws NotInitializedException the static DocumentManager instance isn't initialised.
	 */
	public static DocumentManager getInstance() throws NotInitializedException {
		return Database.getInstance().getDocumentManager();
	}

	/**
	 * Returns a list of Published Documents from a result set obtained from a document query.
	 * @param rs ResultSet of Published Documents.
	 * @param user owner of documents (if known, otherwise pass in null).
	 * @return The list of Published Documents from the result set.
	 * @throws SQLException an error has occurred in the database.
	 * @throws NoSuchObjectException unable to load the user for a document.
	 */
	private List<PublishedDocument> getPublishedDocumentsFromResultSet(ResultSet rs, User user) throws SQLException, NoSuchObjectException {
		List<PublishedDocument> ret = new ArrayList<PublishedDocument>();

		while(rs.next())
			ret.add(getPublishedDocumentFromResultSet(rs, user));

		return ret;
	}

	/**
	 * Returns a list of Work-In-Progress Documents from a result set obtained from a document query.
	 * @param rs ResultSet of Work-In-Progress Documents.
	 * @param user owner of documents (if known, otherwise pass in null).
	 * @return The list of Published Documents from the result set.
	 * @throws SQLException an error has occurred in the database.
	 * @throws NoSuchObjectException unable to load the user for a document.
	 */
	private List<WIPDocument> getWIPDocumentsFromResultSet(ResultSet rs, User user) throws SQLException, NoSuchObjectException {
		List<WIPDocument> ret = new ArrayList<WIPDocument>();

		while(rs.next())
			ret.add(getWIPDocumentFromResultSet(rs, user));

		return ret;
	}

	/**
	 * Returns a single Published Document from a result set obtained from a document query.
	 * @param rs ResultSet of Published Documents.
	 * @param user owner of documents (if known, otherwise pass in null).
	 * @return A Published Document from the result set.
	 * @throws SQLException an error has occurred in the database.
	 * @throws NoSuchObjectException unable to load the user for a document.
	 */
	private PublishedDocument getPublishedDocumentFromResultSet(ResultSet rs, User user) throws SQLException, NoSuchObjectException {
		return new PublishedDocument(rs.getLong("id"), DocumentType.getValue(rs.getInt("type")), user == null ? LoginManager.getInstance().getUser(rs.getLong("user_id")) : user,
				rs.getString("title"), rs.getTimestamp("update_time").getTime(), rs.getInt("vote_count"), rs.getDouble("score"));
	}

	/**
	 * Returns a single Work-In-Progress Document from a result set obtained from a document query.
	 * @param rs ResultSet of Work-In-Progress Documents.
	 * @param user owner of documents (if known, otherwise pass in null).
	 * @return A Work-In-Progress Document from the result set.
	 * @throws SQLException an error has occurred in the database.
	 * @throws NoSuchObjectException unable to load the user for a document.
	 */
	private WIPDocument getWIPDocumentFromResultSet(ResultSet rs, User user) throws SQLException, NoSuchObjectException {
		return new WIPDocument(rs.getLong("id"), DocumentType.getValue(rs.getInt("type")), user == null ? LoginManager.getInstance().getUser(rs.getLong("user_id")) : user,
                rs.getString("title"), rs.getTimestamp("update_time").getTime());
	}

	/**
	 * Returns a single Document from a result set obtained from a document query.
	 * @param rs ResultSet of Documents.
	 * @param user owner of documents (if known, otherwise pass in null).
	 * @return A Document from the result set.
	 * @throws SQLException an error has occurred in the database.
	 * @throws NoSuchObjectException unable to load the user for a document.
	 */
	private Document getDocumentFromResultSet(ResultSet rs, User user) throws SQLException, NoSuchObjectException {
		if(rs.getBoolean("published_flag")) {
			return getPublishedDocumentFromResultSet(rs, user);
		} else {
			return getWIPDocumentFromResultSet(rs, user);
		}
	}

	/**
	 * Returns a list of Revisions from a result set obtained from a revision query.
	 * @param rs ResultSet of Revisions.
	 * @param document document Revisions belong to.
	 * @return The list of Revisions from the result set.
	 * @throws SQLException an error has occurred in the database.
	 */
	private LinkedList<Revision> getRevisionsFromResultSet(ResultSet rs, Document document) throws SQLException {
		LinkedList<Revision> ret = new LinkedList<Revision>();

		while(rs.next())
			ret.add(new Revision(rs.getLong("id"), rs.getDate("update_time"), document));

		return ret;
	}

    private String getDocumentTypeSQL(DocumentType type) {
        if(type == DocumentType.ALL) return "";
        else return " AND type = " + type.getId();
    }

    /**
     * Get the list of Published Documents by a particular user.
     * @param user owner of document
     * @param documentType type of document
     * @param param query parameter to filter/sort the results.
     * @return The list of Published Documents by a particular user.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException unable to load the user for a document.
     */
	public List<PublishedDocument> getPublishedByUser(User user, DocumentType documentType, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE user_id = ? AND published_flag = true" + getDocumentTypeSQL(documentType)));
		ps.setLong(1, user.getID());
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, user);
	}

    /**
     * Get the list of Work-In-Progress Documents by a particular user.
     * @param user owner of document
     * @param documentType type of document
     * @param param query parameter to filter/sort the results.
     * @return The list of Work-In-Progress Documents by a particular user.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException unable to load the user for a document.
     */
	public List<WIPDocument> getWorkInProgressByUser(User user, DocumentType documentType, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE user_id = ? AND published_flag = false" + getDocumentTypeSQL(documentType)));
		ps.setLong(1, user.getID());
		ResultSet rs = ps.executeQuery();
		return getWIPDocumentsFromResultSet(rs, user);
	}

    /**
     * Get the list of Published Documents by a particular tag.
     * @param tag tag to search for
     * @param documentType type of document
     * @param param query parameter to filter/sort the results.
     * @return The list of Published Documents by a particular tag.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException unable to load the user for a document.
     */
	public List<PublishedDocument> getPublishedByTag(Tag tag, DocumentType documentType, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(
				param.updateQuery("SELECT * FROM documents LEFT JOIN document_tags ON (documents.id = document_tags.document_id) WHERE document_tags.tag_id = ? AND documents.published_flag = true" + getDocumentTypeSQL(documentType)));
		ps.setLong(1, tag.getID());
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, null);
	}

    /**
     * Get the list of Published Documents by a particular title (case-insensitive).
     * @param title title to search for
     * @param documentType type of document
     * @param param query parameter to filter/sort the results.
     * @return The list of Published Documents by a particular title (case-insensitive).
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException unable to load the user for a document.
     */
	public List<PublishedDocument> getPublishedByTitle(String title, DocumentType documentType, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE LOWER(title) LIKE ? AND published_flag = true" + getDocumentTypeSQL(documentType)));
		ps.setString(1, "%" + title.toLowerCase() + "%");
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, null);
	}

    /**
     * Get the list of Published Documents by a particular title (case-sensitive).
     * @param title title to search for
     * @param documentType type of document
     * @param param query parameter to filter/sort the results.
     * @return The list of Published Documents by a particular title (case-sensitive).
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException unable to load the user for a document.
     */
	public List<PublishedDocument> getPublishedByExactTitle(String title, DocumentType documentType, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE title = ? AND published_flag = true" + getDocumentTypeSQL(documentType)));
		ps.setString(1, title);
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, null);
	}

    /**
     * Get the list of Work-In-Progress Documents by a particular title (case-sensitive).
     * @param title title to search for
     * @param documentType type of document
     * @param param query parameter to filter/sort the results.
     * @return The list of Work-In-Progress Documents by a particular title (case-sensitive).
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException unable to load the user for a document.
     */
	public List<WIPDocument> getWIPByExactTitle(String title, DocumentType documentType, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE title = ? AND published_flag = false" + getDocumentTypeSQL(documentType)));
		ps.setString(1, title);
		ResultSet rs = ps.executeQuery();
		return getWIPDocumentsFromResultSet(rs, null);
	}

    /**
     * Get the list of featured documents.
     * @param documentType type of document
     * @param param query parameter to filter/sort the results.
     * @return The list of featured documents.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException unable to load the user for a document.
     */
    public List<PublishedDocument> getFeatured(DocumentType documentType, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE featured_flag = true" + getDocumentTypeSQL(documentType)));
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, null);
	}

    /**
     * Get the list of featured documents by tag.
     * @param tag tag to search for.
     * @param param query parameter to filter/sort the results.
     * @return The list of featured documents by tag.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException unable to load the user for a document.
     */
	public List<PublishedDocument> getFeaturedByTag(Tag tag, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(
				param.updateQuery("SELECT * FROM documents LEFT JOIN document_tags ON (documents.id = document_tags.document_id) WHERE document_tags.tag_id = ? AND documents.featured_flag = true"));
		ps.setLong(1, tag.getID());
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, null);
	}

    /**
     * Get the list of Published Documents.
     * @param documentType type of document
     * @param param query parameter to filter/sort the results.
     * @return The list of Published Documents.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException unable to load the user for a document.
     */
	public List<PublishedDocument> getPublished(DocumentType documentType, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT * FROM documents WHERE published_flag = true") + getDocumentTypeSQL(documentType));
		ResultSet rs = ps.executeQuery();
		return getPublishedDocumentsFromResultSet(rs, null);
	}

    /**
     * Deletes all documents owned by a particular user.
     * @param user user whose documents to delete.
     * @throws SQLException an error has occurred in the database.
     */
	public void deleteAllDocumentsByUser(User user) throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("DELETE FROM documents WHERE user_id = ?");
		ps.setLong(1, user.getID());
		ps.executeUpdate();
	}

    /**
     * Get all revisions of a given document; published documents will have a single revision.
     * The content of the revision is not retrieved here, but is rather done on-demand/lazily.
     * @param document document to obtain revisions for.
     * @param param query parameter to filter/sort the results.
     * @return The revisions of a given document.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException unable to load any revision.
     */
	public LinkedList<Revision> getRevisions(Document document, QueryParam param) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(param.updateQuery("SELECT id, document_id, update_time FROM revisions WHERE document_id = ?"));
		ps.setLong(1, document.getID());
		ResultSet rs = ps.executeQuery();
		return getRevisionsFromResultSet(rs, document);
	}

    /**
     * Delete a given document.
     * @param document document to delete.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException document does not exist.
     */
	public void deleteDocument(Document document) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("DELETE FROM documents WHERE id = ?");
		ps.setLong(1, document.getID());

		int affected_rows = ps.executeUpdate();
		if(affected_rows < 1) throw new NoSuchObjectException("document " + document.getID());
	}

    /**
     * Add a revision to a given document.
     * @param document document to add revision to.
     * @param date date revision is created.
     * @param payload the actual content of the revision, which is not stored in the Revision object.
     * @return The created Revision after insertion into the database.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException document does not exist.
     */
 	public Revision addRevision(Document document, Date date, String payload) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("INSERT INTO revisions (document_id, update_time, content) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, document.getID());
		ps.setTimestamp(2, new Timestamp(date.getTime()));
		ps.setString(3, payload);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys(); rs.next();
		return new Revision(rs.getLong(1), date, document);
	}

    /**
     * Delete revision(s) from a document, typically called when documents are deleted or published.
     * @param revisions list of revisions to delete
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException revision does not exist.
     */
	public void deleteRevision(List<Revision> revisions) throws SQLException, NoSuchObjectException {
		for(Revision r : revisions) {
			PreparedStatement ps = m_Database.getConnection().prepareStatement("DELETE FROM revisions WHERE id = ?");
			ps.setLong(1, r.getID());

			int affected_rows = ps.executeUpdate();
			if(affected_rows < 1) throw new NoSuchObjectException("revision " + r.getID());
		}
	}

    /**
     * Get a document by its' document ID.
     * @param id id of document to retrieve.
     * @return The document corresponding to the requested ID.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException document does not exist.
     */
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

    /**
     * Get the parent document of a given document.
     * @param document document whose parent to retrieve.
     * @return The parent document of a the given document, null if one does not exist.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException the document exists, but failed to load.
     */
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

    /**
     * Sets the parent document of a given document. (Can only be set once!)
     * @param document document whose parent to set.
     * @param parentDoc the parent document to set.
     * @throws SQLException an error has occurred in the database.
     * @throws DuplicateEntryException If the parent has already been set for this document.
     */
	public void setParentDocument(Document document, Document parentDoc) throws SQLException, DuplicateEntryException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("INSERT INTO document_parents (document_id, parent_document_id) VALUES (?, ?)");
		ps.setLong(1, document.getID());
		ps.setLong(2, parentDoc.getID());
        try {
        	ps.executeUpdate();
        } catch (SQLException e) {
            // Catch any duplicate name exceptions and throw our own.
            DuplicateEntryException.handle(e);
        }
	}

    /**
     * Creates a new document in the database, and updates the given document's ID.
     * @param document document to create.
     * @throws SQLException an error has occurred in the database.
     * @throws IDAlreadySetException the given document's ID has already been set.
     */
	public void createDocument(Document document) throws SQLException, IDAlreadySetException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement
				("INSERT INTO documents (user_id, type, title, published_flag, featured_flag, update_time, vote_count) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, document.owner.getID());
		ps.setInt(2, document.docType.getId());
		ps.setString(3, document.getTitle());
		ps.setBoolean(4, document instanceof PublishedDocument);
		ps.setBoolean(5, document instanceof PublishedDocument && ((PublishedDocument)document).getFeatured());
		ps.setTimestamp(6, new Timestamp(document.creationTime));
		ps.setInt(7, 0);
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys(); rs.next();
		document.setID(rs.getLong(1));
	}

    /**
     * Updates the given document's metadata, including owner, type, title, published/featured flags and update time.
     * @param document document to update.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException document does not exist.
     */
	public void updateDocument(Document document) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement
				("UPDATE documents SET user_id = ?, type = ?, title = ?, published_flag = ?, featured_flag = ?, update_time = ? where id = ?");
		ps.setLong(1, document.owner.getID());
		ps.setInt(2, document.docType.getId());
		ps.setString(3, document.getTitle());
		ps.setBoolean(4, document instanceof PublishedDocument);
		ps.setBoolean(5, document instanceof PublishedDocument && ((PublishedDocument) document).getFeatured());
		ps.setTimestamp(6, new Timestamp(document.creationTime));
		ps.setLong(7, document.getID());

		int affected_rows = ps.executeUpdate();
		if(affected_rows < 1) throw new NoSuchObjectException("document " + document.getID());
	}

    /**
     * Gets the set of all tags in the database.
     * @return The set of all tags in the database (no duplicates).
     * @throws SQLException an error has occurred in the database.
     */
	public Set<Tag> getTags() throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT * FROM tags");
		ResultSet rs = ps.executeQuery();
		return getTagsFromResultSet(rs);
	}

    /**
     * Returns a set of tags from a result set obtained from a tag query.
     * @param rs ResultSet of tags.
     * @return The set of tags from the result set.
     * @throws SQLException an error has occurred in the database.
     */
	private Set<Tag> getTagsFromResultSet(ResultSet rs) throws SQLException {
		Set<Tag> ret = new HashSet<Tag>();

		while(rs.next())
			ret.add(getTagFromResultSet(rs));

		return ret;
	}

    /**
     * Returns a single tag from a result set obtained from a tag query.
     * @param rs ResultSet of tags.
     * @return A single tag from the result set.
     * @throws SQLException an error has occurred in the database.
     */
	private Tag getTagFromResultSet(ResultSet rs) throws SQLException {
		return new Tag(rs.getLong("id"), rs.getString("name"), rs.getBoolean("banned_flag"));
	}

    /**
     * Get the set of tags in the database which are in use.
     * @return The set of tags in the database which are in use.
     * @throws SQLException an error has occurred in the database.
     */
	public Set<Tag> getTagsNotEmpty() throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT * FROM tags INNER JOIN document_tags on document_tags.tag_id = tags.id GROUP BY tags.id");
		ResultSet rs = ps.executeQuery();
		return getTagsFromResultSet(rs);
	}

    /**
     * Get the set of tags in the database which are not banned/available for use.
     * @return The set of tags in the database which are not banned/available for use.
     * @throws SQLException an error has occurred in the database.
     */
	public Set<Tag> getTagsNotBanned() throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT * FROM tags WHERE banned_flag = false");
		ResultSet rs = ps.executeQuery();
		return getTagsFromResultSet(rs);
	}

    /**
     * Get a tag by name (case-sensitive).
     * @param name name of tag.
     * @return The tag corresponding to the name given.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException the tag with the given name does not exist.
     */
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

    /**
     * Get all tags for a given document.
     * @param document document whose tags to retrieve.
     * @return The set of tags for a given document.
     * @throws SQLException an error has occurred in the database.
     */
	public Set<Tag> getTags(Document document) throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT * FROM document_tags INNER JOIN tags on document_tags.tag_id = tags.id WHERE document_id = ?");
		ps.setLong(1, document.getID());
		ResultSet rs = ps.executeQuery();
		return getTagsFromResultSet(rs);
	}

    /**
     * Creates a tag in the database.
     * @param name name of tag to create.
     * @param isBanned true if tag is banned, false otherwise.
     * @return The created tag, along with its' tag ID.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException tag creation failed, and thus cannot be retrieved.
     * @throws DuplicateEntryException tag with the same name already exists.
     */
	public Tag createTag(String name, boolean isBanned) throws SQLException, NoSuchObjectException, DuplicateEntryException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement
				("INSERT INTO tags (name, banned_flag) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, name);
		ps.setBoolean(2, isBanned);

        try {
		    ps.executeUpdate();
        } catch (SQLException e) {
            // Catch any duplicate name exceptions and throw our own.
            DuplicateEntryException.handle(e);
        }

		ResultSet rs = ps.getGeneratedKeys();
		if(rs.next())
			return new Tag(rs.getLong(1), name, isBanned);
		else
			throw new NoSuchObjectException("failed to create tag " + name);
	}

    /**
     * Add a tag to a given document.
     * @param document document to add a tag to.
     * @param tag tag to add.
     * @throws SQLException an error has occurred in the database.
     */
	public void addTag(Document document, Tag tag) throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("INSERT IGNORE INTO document_tags (tag_id, document_id) VALUES (?, ?)");
		ps.setLong(1, tag.getID());
		ps.setLong(2, document.getID());
        ps.executeUpdate();
	}

    /**
     * Deletes a tag from a given document.
     * @param document document to delete a tag from.
     * @param tag tag to delete.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException tag cannot be found on given document.
     */
	public void deleteTag(Document document, Tag tag) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("DELETE FROM document_tags WHERE tag_id = ? AND document_id = ?");
		ps.setLong(1, tag.getID());
		ps.setLong(2, document.getID());

		int affected_rows = ps.executeUpdate();
		if(affected_rows < 1) throw new NoSuchObjectException("tag " + tag.getID() + " on " + document.getID());
	}

    /**
     * Updates a given tag's metadata, such as name and banned flag.
     * @param tag tag to update.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException tag not found.
     * @throws DuplicateEntryException a tag with the same name already exists.
     */
	public void updateTag(Tag tag) throws SQLException, NoSuchObjectException, DuplicateEntryException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement
				("UPDATE tags SET name = ?, banned_flag = ? where id = ?");
		ps.setString(1, tag.name);
		ps.setBoolean(2, tag.getBanned());
		ps.setLong(3, tag.getID());
        try {
            int affected_rows = ps.executeUpdate();
            if (affected_rows < 1) throw new NoSuchObjectException("tag " + tag.getID());
        } catch (SQLException e) {
            // Catch any duplicate name exceptions and throw our own.
            DuplicateEntryException.handle(e);
        }
	}

    /**
     * Deletes a given tag from the database.
     * @param tag tag to delete
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException tag not found.
     */
	public void deleteTag(Tag tag) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("DELETE FROM tags WHERE id = ?");
		ps.setLong(1, tag.getID());

		int affected_rows = ps.executeUpdate();
		if(affected_rows < 1) throw new NoSuchObjectException("tag " + tag.getID());
	}

    /**
     * Delete all document references to a tag, particularly useful when a tag is banned.
     * @param tag tag to delete all document references from.
     * @throws SQLException an error has occurred in the database.
     */
 	public void deleteTagReferences(Tag tag) throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("DELETE FROM document_tags WHERE tag_id = ?");
		ps.setLong(1, tag.getID());
		ps.executeUpdate();
	}

    /**
     * Add a (positive) vote on a given document. The total vote count/score is also updated through triggers.
     * @param user user who added the vote.
     * @param document document to add the vote to.
     * @throws SQLException an error has occurred in the database.
     * @throws DuplicateEntryException the user has already voted for the document.
     */
 	public void addVote(User user, PublishedDocument document) throws SQLException, DuplicateEntryException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("INSERT INTO votes (user_id, document_id) VALUES (?, ?)");
		ps.setLong(1, user.getID());
		ps.setLong(2, document.getID());
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
        // Catch any duplicate name exceptions and throw our own.
            DuplicateEntryException.handle(e);
        }
	}

    /**
     * Get the content of a revision on-the-fly. The content is kept separately and fetched lazily.
     * This design is due to the size of the content and the fact that we don't need all revisions for
     * all documents, especially when trying to load pages of documents.
     * @param revision revision whose content to retrieve.
     * @return The contents of the given revision.
     * @throws SQLException an error has occurred in the database.
     * @throws NoSuchObjectException revision does not exist.
     */
 	public String getRevisionContent(Revision revision) throws SQLException, NoSuchObjectException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement("SELECT content FROM revisions WHERE id = ?");
		ps.setLong(1, revision.getID());
		ResultSet rs = ps.executeQuery();

		if(!rs.next()) throw new NoSuchObjectException("revision " + revision.getID());
		return rs.getString("content");
	}

    /**
     * Age all the scores in the database; this should be done on a scheduled basis.
     * To speed up the process, all scores currently <= 0 are ignored.
     * @param param query parameter to filter/sort the results.
     * @throws SQLException an error has occurred in the database.
     */
	public void ageScores(QueryParam param) throws SQLException {
		PreparedStatement ps = m_Database.getConnection().prepareStatement(
				param.updateQuery("UPDATE documents SET score = vote_count * EXP(-1 * POWER(time_to_sec(timediff(NOW(),update_time)) / 3600,2)/" + Document.AGING_CONSTANT + ") WHERE score > 0"));
		ps.executeUpdate();
	}
}
