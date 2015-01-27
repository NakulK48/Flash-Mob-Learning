package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import java.util.Set;

import uk.ac.cam.grpproj.lima.flashmoblearning.Tag;

/** Singleton class which opens the database connection, and deals with global config and Tags.
 * Most of the work is done by DocumentManager and LoginManager.
 */
public abstract class Database {

	//static public Database getInstance();
	
	/** Shutdown the database */
	public abstract void close();
	
	/** Get the login banner */
	public abstract String getLoginBanner();

	/** Set the login banner */
	public abstract void setLoginBanner(String banner);
	
	/** List all tags */
	public abstract Set<Tag> getTags();
	
	/** Get tag by name */
	public abstract Tag getTag(String name);
	
}
