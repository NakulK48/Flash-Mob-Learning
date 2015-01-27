package uk.ac.cam.grpproj.lima.flashmoblearning;

/** A document that has been published for everyone to see. */
public abstract class PublishedDocument extends Document{
	
	/** Get the one and only revision */
	public abstract Revision getContentRevision();

}
