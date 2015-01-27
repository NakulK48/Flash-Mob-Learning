package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.Set;

/** Base class for Document's. */
public abstract class Document {
	
	/** A Document has a Type */
	public abstract DocumentType getType();
	
	/** Every document has an immutable Owner */
	public abstract User getOwner();

	/** @return The parent Document this was forked from or null if this 
	 * document was created from scratch. */
	public abstract Document getParent();
	
	/** Get the current list of tags. Read-only. */
	public abstract Set<Tag> getTags();
	
	/** Add a tag */
	public abstract void addTag(Tag t);
	
	/** Remove a tag */
	public abstract void deleteTag(Tag t);

}
