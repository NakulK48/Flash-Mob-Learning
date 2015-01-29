package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import java.util.Set;

import uk.ac.cam.grpproj.lima.flashmoblearning.Tag;

/** Singleton class which opens the database connection, and deals with global config and Tags.
 * Most of the work is done by DocumentManager and LoginManager.
 */
public abstract class Database {
	
	private static Database instance;

//	public static init() {
//		
//	}
//	
//	public static initTest() {
//		
//	}
	
	public static Database getInstance() {
		return instance;
	}
	
	/** Shutdown the database */
	public abstract void close();
	
	/** Get the login banner */
	public abstract String getLoginBanner();

	/** Set the login banner */
	public abstract void setLoginBanner(String banner);
	
	/** Get a unique ID for a document */
	public abstract long createDocumentID();
	
	/** Get a unique ID for a user */
	public abstract long createUserID();

	/** Get the DocumentManager */
	public abstract DocumentManager getDocumentManager();

	/** Get the LoginManager */
	public abstract LoginManager getLoginManager();
	
}
