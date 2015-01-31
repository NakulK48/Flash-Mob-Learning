package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.lang.ref.SoftReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	/** The parent Document this was forked from or null if this 
	 * document was created from scratch. */
	public final Document parentDoc;
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
	
	public Document(DocumentType docType, User owner, Document parentDoc,
			String title, long time) {
		id = -1;
		this.docType = docType;
		this.owner = owner;
		this.parentDoc = parentDoc;
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
	
	private SoftReference<LinkedList<Revision>> revisionsRef;

	public List<Revision> getRevisions() throws NotInitializedException, SQLException, NoSuchObjectException {
		return Collections.unmodifiableList(new ArrayList<Revision>(innerGetRevisions()));
	}
	
	/** Find all revisions 
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	private LinkedList<Revision> innerGetRevisions() throws NotInitializedException, SQLException, NoSuchObjectException {
		synchronized(this) {
			if(revisionsRef != null) {
				LinkedList<Revision> r = revisionsRef.get();
				if(r != null) {
					return r;
				} else {
					revisionsRef = null;
				}
			}
		}
		LinkedList<Revision> revisions = DocumentManager.getInstance().getRevisions(this, QueryParam.UNSORTED);
		synchronized(this) {
			if(revisionsRef != null) {
				LinkedList<Revision> r1 = revisionsRef.get();
				if(r1 != null) return r1;
			}
			revisionsRef = new SoftReference<LinkedList<Revision>>(revisions);
		}
		assert(revisionsBelongToMe(revisions));
		return revisions;
	}
	
	private boolean revisionsBelongToMe(Collection<Revision> revisions) {
		for(Revision r : revisions) {
			if(r.document != this) return false;
		}
		return true;
	}

	/** Called when a new revision is saved. CALLER MUST PIN THE CONTENT OF THE REVISION!
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public void saveRevision(Revision r) throws NotInitializedException, SQLException, NoSuchObjectException {
		assert(r.document == this);
		synchronized(this) {
			if(revisionsRef != null && revisionsRef.get() != null) {
				revisionsRef.get().add(r);
			}
		}
		DocumentManager.getInstance().addRevision(this, r);
	}
	
	public Revision getLastRevision() throws NotInitializedException, SQLException, NoSuchObjectException {
		// FIXME OPT Consider caching separately. Be careful with consistency between the two!
		return innerGetRevisions().getLast();
	}

}
