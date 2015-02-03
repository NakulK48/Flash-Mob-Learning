package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;
import java.util.*;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.QueryParam;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** Base class for Document's. */
public class Document {
	
	/** A Document has a Type */
	public final DocumentType docType;
	/** Every document has an immutable Owner */
	public final User owner;
	/** Time and date of creation of the document */
	public final long creationTime;

	/** Tags associated with this document. Can be modified, when modified we update the
	 * database automatically. Non-null. */
	private Set<Tag> tags;
	/** Every work-in-progress document has a unique ID which never changes.
	 * This must be set by the database when the document is first stored. It cannot be changed
	 * after that point. */
	private long id;
	/** Title of the document. Mutable. */
	private String title;
	
	/** Create a Document.
	 * @param id If loaded from the database, this is a (non-negative) ID for 
	 * the document. If it hasn't been stored yet this should be -1.
	 * @param docType Document type e.g. Skulpt.
	 * @param owner All documents are owned by a single user.
	 * @param title Title of the document. Can change.
	 * @param time Creation time.
	 */
	public Document(long id, DocumentType docType, User owner,
			String title, long time) {
		this.id = id;
		this.docType = docType;
		this.owner = owner;
		this.tags = new HashSet<Tag>();
		this.title = title;
		this.creationTime = time;
	}

	/** Called by database */
	public synchronized long getID() {
		return id;
	}
	
	/** Called by the database when a document is first stored */
	public synchronized void setID(long newID) throws IDAlreadySetException {
		if(this.id == -1) {
			this.id = newID;
		} else {
			if(id != newID) // OK to set to existing ID
				throw new IDAlreadySetException();
		}
	}

	/** Get the current list of tags. Read-only. */
	public synchronized Set<Tag> getTags() {
		return Collections.unmodifiableSet(new HashSet<Tag>(tags));
	}
	
	/** Add a tag.
	 * @return True unless the tag was already present. 
	 * @throws NoSuchObjectException If the Document has not been stored.
	 * @throws SQLException 
	 * @throws NotInitializedException If the database has not been initialised. */
	public boolean addTag(Tag t) throws NotInitializedException, SQLException, NoSuchObjectException {
		boolean ret;
		synchronized(this) {
			ret = tags.add(t);
		}
		DocumentManager.getInstance().updateTags(this);
		return ret;
	}
	
	/** Remove a tag.
	 * @return  
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public boolean deleteTag(Tag t) throws NotInitializedException, SQLException, NoSuchObjectException {
		boolean ret;
		synchronized(this) {
			ret = tags.remove(t);
		}
		DocumentManager.getInstance().updateTags(this);
		return ret;
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
		synchronized(this) {
			if(this.title.equals(title)) return;
			this.title = title;
		}
		DocumentManager.getInstance().updateDocument(this);
	}
	
	public List<Revision> getRevisions(QueryParam param) throws NotInitializedException, SQLException, NoSuchObjectException {
		List<Revision> r = Collections.unmodifiableList(new ArrayList<Revision>(innerGetRevisions(param)));
		return r;
	}
	
	/** Find all revisions 
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	private List<Revision> innerGetRevisions(QueryParam param) throws NotInitializedException, SQLException, NoSuchObjectException {
		List<Revision> revisions =
				DocumentManager.getInstance().getRevisions(this, param);
		assert(revisionsBelongToMe(revisions));
		return revisions;
	}
	
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
	public void addRevision(Date d, String content) throws NotInitializedException, SQLException, NoSuchObjectException {
		Revision.createRevision(this, d, content);
	}
	
	private static final QueryParam LAST_REVISION_QUERY = 
			new QueryParam(1, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
	
	public Revision getLastRevision() throws NotInitializedException, SQLException, NoSuchObjectException {
		return getRevisions(LAST_REVISION_QUERY).get(0);
	}
	
	public Document getParentDocument() throws SQLException, NoSuchObjectException {
		return DocumentManager.getInstance().getParentDocument(this);
	}

	public void setParentDocument(Document parent) throws SQLException, NoSuchObjectException {
		DocumentManager.getInstance().setParentDocument(this, parent);
	}

}
