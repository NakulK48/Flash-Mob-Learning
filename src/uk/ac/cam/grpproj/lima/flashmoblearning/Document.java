package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.List;

/** Base class for Document's. */
public abstract class Document {
	
	/** A Document has a Type */
	public abstract DocumentType getType();
	
	/** Every document has an immutable Owner */
	public abstract User getOwner();
	
	/** Get the current list of tags */
	public abstract List<Tag> getTags();
	
	/** Add a tag */
	public abstract void addTag(Tag t);

}
