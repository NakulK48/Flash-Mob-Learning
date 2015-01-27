package uk.ac.cam.grpproj.lima.flashmoblearning;

public abstract class Tag {
	
	/** Tag name */
	public final String name;
	
	public Tag(String n) {
		name = n;
	}
	
	/** Has the tag been banned? */
	public abstract boolean getBanned();
	
	/** Ban or unban the tag */
	public abstract void setBanned(boolean banned);

}
