package uk.ac.cam.grpproj.lima.flashmoblearning;

public abstract class PublishedDocument implements Document{
	
	/** Get the one and only revision */
	public abstract Revision getContentRevision();

}
