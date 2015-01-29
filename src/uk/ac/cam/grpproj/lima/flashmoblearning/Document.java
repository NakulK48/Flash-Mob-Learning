package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.Database;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;

/** Base class for Document's. */
public class Document {
	
	/** Every work-in-progress document has a unique ID which never changes. */
	public final long id;
	/** A Document has a Type */
	public final DocumentType docType;
	/** Every document has an immutable Owner */
	public final User owner;
	/** The parent Document this was forked from or null if this 
	 * document was created from scratch. */
	public final Document parentDoc;

	/** Tags associated with this document. Can be modified, when modified we update the
	 * database automatically. Non-null. */
	private Set<Tag> tags;
	
	/** Title of the document. Mutable. */
	private String title;
	
	public Document(DocumentType docType, User owner, Document parentDoc,
			String title) {
		id = Database.getInstance().createDocumentID();
		this.docType = docType;
		this.owner = owner;
		this.parentDoc = parentDoc;
		this.tags = new HashSet<Tag>();
		this.title = title;
	}

	/** Get the current list of tags. Read-only. */
	public synchronized Set<Tag> getTags() {
		return Collections.unmodifiableSet(new HashSet<Tag>(tags));
	}
	
	/** Add a tag.
	 * @return True unless the tag was already present. */
	public boolean addTag(Tag t) {
		boolean ret;
		synchronized(this) {
			ret = tags.add(t);
		}
		DocumentManager.getInstance().updateTags(this);
		return ret;
	}
	
	/** Remove a tag.
	 * @return  */
	public boolean deleteTag(Tag t) {
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

	/** Set the title of the document */
	public void setTitle(String title) {
		synchronized(this) {
			if(this.title.equals(title)) return;
			this.title = title;
		}
		DocumentManager.getInstance().updateDocument(this);
	}

}
