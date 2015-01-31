package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** Singleton class which opens the database connection, and deals with global config and Tags.
 * Most of the work is done by DocumentManager and LoginManager.
 */
public class Database {
	
	private static Database m_Instance;
	private Connection m_Connection = null;
	private LoginManager m_LoginManagerInstance;
	private DocumentManager m_DocumentManagerInstance;

	private static final String c_Username = "flashmoblearning";
	private static final String c_Password = "flashmoblearning";
	private static final String c_JDBCURL = "jdbc:mysql://localhost/flashmoblearning";
	
	public static Database getInstance() {
		return m_Instance;
	}

	/** Initializes and tests the database connection, setting it up if necessary **/
	public static void init() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(c_JDBCURL + "?user=" + c_Username + "?password" + c_Password);
		setup(connection);
		m_Instance = new Database(connection);
	}

	private Database(Connection connection) throws SQLException {
		m_Connection = connection;
	}

	/** Obtain database connection **/
	protected Connection getConnection() throws SQLException {
		return m_Connection;
	}

	/** Obtain a new statement, ready for execution **/
	protected Statement getStatement() throws SQLException {
		return m_Connection.createStatement();
	}

	/** Shutdown the database */
	public void close() {
		try {
			m_Connection.close();
		} catch (SQLException e) {
			// ignore, this connection is already closed.
		}
	}

	/** Get the DocumentManager */
	public DocumentManager getDocumentManager() throws NotInitializedException {
		if(m_DocumentManagerInstance != null)
			return m_DocumentManagerInstance;
		else
			throw new NotInitializedException();
	}

	/** Get the LoginManager */
	public LoginManager getLoginManager() throws NotInitializedException {
		if(m_LoginManagerInstance != null)
			return m_LoginManagerInstance;
		else
			throw new NotInitializedException();
	}

	/** Creates all necessary tables if they do not exist. **/
	private static void setup(Connection connection) {
	}
	
}
