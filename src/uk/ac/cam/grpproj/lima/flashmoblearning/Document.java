package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.List;

public interface Document {
	
	/** Every document has an immutable Owner */
	public User getOwner();
	
	/** Get the current list of tags */
	public List<Tag> getTags();
	
	/** Add a tag */
	public void addTag(Tag t);

}
