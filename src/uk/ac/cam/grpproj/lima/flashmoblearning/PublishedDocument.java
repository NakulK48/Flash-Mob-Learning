package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.lang.ref.SoftReference;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.QueryParam;

/** A document that has been published for everyone to see. Note that the content of the document
 * cannot be modified once it has been published, but the tags and title can, by the owner. */
public class PublishedDocument extends Document{
	
	/** Content stored as a Revision */
	private SoftReference<Revision> contentRef;
	
	/** Has the "featured" flag been set by the administrator? */
	private boolean isFeatured;
	
	public PublishedDocument(WIPDocument original) {
		super(original.docType, original.owner, original.parentDoc, original.getTitle());
		isFeatured = false;
	}
	
	/** Get the one and only revision */
	public Revision getContentRevision() {
		synchronized(this) {
			if(contentRef != null) {
				Revision r = contentRef.get();
				if(r != null) {
					return r;
				} else {
					contentRef = null;
				}
			}
		}
		Revision r = DocumentManager.getInstance().getFinalRevision(this, QueryParam.UNSORTED);
		synchronized(this) {
			if(contentRef != null) {
				Revision r1 = contentRef.get();
				if(r1 != null) return r1;
			}
			contentRef = new SoftReference<Revision>(r);
		}
		return r;
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
