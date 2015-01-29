package uk.ac.cam.grpproj.lima.flashmoblearning;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;

/** A document that has been published for everyone to see. Note that the content of the document
 * cannot be modified once it has been published, but the tags and title can, by the owner. */
public class PublishedDocument extends Document{
	
	/** Content stored as a Revision */
	private final Revision content;
	
	/** Has the "featured" flag been set by the administrator? */
	private boolean isFeatured;
	
	public PublishedDocument(WIPDocument original) {
		super(original.docType, original.owner, original.parentDoc, original.getTitle());
		isFeatured = false;
		content = original.getLastRevision();
	}
	
	/** Get the one and only revision */
	public Revision getContentRevision() {
		return content;
	}

	/** Copy a document so we can edit it */
	public WIPDocument fork(User newOwner) {
		return new WIPDocument(this, newOwner);
	}
	
	/** Is this document Featured? */
	public synchronized boolean getFeatured() {
		return isFeatured;
	}
	
	/** Set the Featured flag */
	public void setFeatured(boolean set) {
		synchronized(this) {
			if(set == isFeatured) return;
			isFeatured = set;
		}
		DocumentManager.getInstance().updateDocument(this);
	}
}
