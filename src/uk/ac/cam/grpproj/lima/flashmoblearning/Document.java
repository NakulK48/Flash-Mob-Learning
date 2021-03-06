package uk.ac.cam.grpproj.lima.flashmoblearning;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.QueryParam;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateEntryException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

import java.sql.SQLException;
import java.util.*;

/** Base class for Document's. */
public class Document {
	
	/** A Document has a Type */
	public final DocumentType docType;
	/** Every document has an immutable Owner */
	public final User owner;
	/** Time and date of creation of the document */
	public final long creationTime;

	/** Every document has a unique ID which never changes.
	 * This is initially -1, and set to its permanent value by the database 
	 * when the document is first stored. */
	private long id;
	/** Title of the document (can be changed). */
	private String title;

	/** Create a Document.
	 * @param id If loaded from the database, this is a (non-negative) ID for 
	 * the document. If it hasn't been stored yet this should be -1.
	 * @param docType Document type e.g. Skulpt. Cannot be ALL.
	 * @param owner All documents are owned by a single user.
	 * @param title Title of the document. Can change.
	 * @param time Creation time.
	 */
	Document(long id, DocumentType docType, User owner,
			String title, long time) {
		if(docType == null || docType == DocumentType.ALL)
			throw new IllegalArgumentException();
		this.id = id;
		this.docType = docType;
		this.owner = owner;
		this.title = title;
		this.creationTime = time;
		if(title == null) throw new NullPointerException();
	}

	/** Called by database */
	public long getID() {
		return id;
	}
	
	/** Called by the database when a document is first stored */
	public void setID(long newID) throws IDAlreadySetException {
		if(this.id == -1) {
			this.id = newID;
		} else {
			if(id != newID) // OK to set to existing ID
				throw new IDAlreadySetException();
		}
	}

	/** Get the current list of tags. Read-only. 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public Set<Tag> getTags() throws NotInitializedException, SQLException {
		return Collections.unmodifiableSet(DocumentManager.getInstance().getTags(this));
	}
	
	/** Add a tag.
	 * @return True unless the tag was already present. 
	 * @throws NoSuchObjectException If the Document has not been stored.
	 * @throws SQLException 
	 * @throws NotInitializedException If the database has not been initialised. 
	 * @throws BannedTagException If the tag has been banned. */
	public boolean addTag(Tag t) throws NotInitializedException, SQLException, NoSuchObjectException, BannedTagException {
		try {
			DocumentManager.getInstance().addTag(this, t);
			return true;
		} catch (DuplicateEntryException e) {
			return false;
		}
	}
	
	/** Remove a tag.
	 * @return  
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public void deleteTag(Tag t) throws NotInitializedException, SQLException, NoSuchObjectException {
		DocumentManager.getInstance().deleteTag(this, t);
	}
	
	/** Get the title of the document */
	public String getTitle() {
		return title;
	}

	/** Set the title of the document 
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public void setTitle(String title) throws NotInitializedException, SQLException, NoSuchObjectException {
		if(this.title.equals(title)) return;
		this.title = title;
		DocumentManager.getInstance().updateDocument(this);
	}
	
	/** Get the list of revisions.
	 * @param param How many revisions, in what order etc.
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public List<Revision> getRevisions(QueryParam param) throws NotInitializedException, SQLException, NoSuchObjectException {
		List<Revision> r = Collections.unmodifiableList(new ArrayList<Revision>(innerGetRevisions(param)));
		return r;
	}
	
	/** Get the list of revisions without copying them */
	private List<Revision> innerGetRevisions(QueryParam param) throws NotInitializedException, SQLException, NoSuchObjectException {
		List<Revision> revisions =
				DocumentManager.getInstance().getRevisions(this, param);
		assert(revisionsBelongToMe(revisions));
		return revisions;
	}

	/** Sanity check that the revisions belong to this document */
	private boolean revisionsBelongToMe(Collection<Revision> revisions) {
		for(Revision r : revisions) {
			if(r.document != this) return false;
		}
		return true;
	}

	/** Called when a new revision is saved.
	 * @param d Creation time of the revision
	 * @param content Content of the revision
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	protected void addRevision(Date d, String content) throws NotInitializedException, SQLException, NoSuchObjectException {
		Revision.createRevision(this, d, content);
	}
	
	private static final QueryParam LAST_REVISION_QUERY = 
			new QueryParam(1, 0, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
	
	/** Get the most recent revision only */
	public Revision getLastRevision() throws NotInitializedException, SQLException, NoSuchObjectException {
		return getRevisions(LAST_REVISION_QUERY).get(0);
	}

	/** Get the parent document, if any, that this document was forked from. 
	 * Preserved when publishing, updated when forking. */
	public Document getParentDocument() throws SQLException, NoSuchObjectException {
		return DocumentManager.getInstance().getParentDocument(this);
	}

	/** Copy the last revision, tags and any other mutable metadata to a new 
	 * document. Used by fork() and publish().
	 * @param d A new document which hasn't been stored yet.
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException 
	 */
	protected void copyTo(Document d, Document newParent) throws NotInitializedException, SQLException, NoSuchObjectException {
		String content = this.getLastRevision().getContent();
		try {
			DocumentManager.getInstance().createDocument(d);
			if(newParent != null)
				DocumentManager.getInstance().setParentDocument(d, newParent);
		} catch (IDAlreadySetException e) {
			throw new IllegalStateException("ID already set but just created?!");
		} catch (DuplicateEntryException e) {
			throw new IllegalStateException("ID already set but just created?!");
		}
		Revision.createRevision(d, new Date(), content);
		for(Tag t : getTags())
			try {
				d.addTag(t);
			} catch (BannedTagException e) {
				// Don't copy banned tags.
			}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (creationTime ^ (creationTime >>> 32));
		result = prime * result + ((docType == null) ? 0 : docType.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		if (creationTime != other.creationTime)
			return false;
		if (docType != other.docType)
			return false;
		if (id != other.id)
			return false;
		if (!owner.equals(other.owner))
			return false;
		return title.equals(other.title);
	}

}
