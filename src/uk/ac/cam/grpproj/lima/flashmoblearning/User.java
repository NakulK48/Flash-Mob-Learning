package uk.ac.cam.grpproj.lima.flashmoblearning;

/** Base of Student and Teacher. */
public abstract class User {
	
	/** User has a fixed numerical ID, even if their name is changed. */
	public abstract long getID();
	
	/** Name of the user */
	public abstract String getName();
	
	/** Try to log in */
	public abstract boolean login(String password);
	
}
