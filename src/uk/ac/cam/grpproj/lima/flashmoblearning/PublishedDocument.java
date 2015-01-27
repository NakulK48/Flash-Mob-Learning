package uk.ac.cam.grpproj.lima.flashmoblearning;

/** A document that has been published for everyone to see. Note that the content of the document
 * cannot be modified once it has been published, but the tags and title can, by the owner. */
public abstract class PublishedDocument extends Document{
	
	/** Get the one and only revision */
	public abstract Revision getContentRevision();

}
