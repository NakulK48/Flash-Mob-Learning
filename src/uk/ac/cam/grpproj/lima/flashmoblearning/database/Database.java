package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import uk.ac.cam.grpproj.lima.flashmoblearning.Tag;

/** Singleton class which opens the database connection, and deals with global config and Tags.
 * Most of the work is done by DocumentManager and LoginManager.
 */
public abstract class Database {
	
	private static Database m_Instance;
	private static Connection m_Connection = null;
	
	public static Database getInstance() {
		return m_Instance;
	}

	/** Initializes and tests the database connection, setting it up if necessary **/
	public static void init() {

	}

	/** Creates all necessary tables if they do not exist. **/
	private static void setup() {

	}

	/** Obtain database connection **/
	protected abstract Connection getConnection() throws SQLException;

	/** Obtain a new statement, ready for execution **/
	protected abstract Statement getStatement() throws SQLException;

	/** Shutdown the database */
	public abstract void close();

	/** Get the DocumentManager */
	public abstract DocumentManager getDocumentManager() throws NotInitializedException;

	/** Get the LoginManager */
	public abstract LoginManager getLoginManager() throws NotInitializedException;
	
}
