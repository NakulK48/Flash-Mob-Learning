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
		Connection connection = DriverManager.getConnection(c_JDBCURL + "?user=" + c_Username + "&password=" + c_Password);
		setup(connection);
		m_Instance = new Database(connection);
	}

	private Database(Connection connection) throws SQLException {
		m_Connection = connection;
		m_LoginManagerInstance = new LoginManager(this);
	}

	/** Obtain database connection **/
	public Connection getConnection() throws SQLException {
		return m_Connection;
	}

	/** Obtain a new statement, ready for execution **/
	public Statement getStatement() throws SQLException {
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
	private static void setup(Connection connection) throws SQLException {
		String create_documents = "CREATE TABLE IF NOT EXISTS `documents` (\n" +
				"  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
				"  `user_id` bigint(20) NOT NULL,\n" +
				"  `type` int(11) NOT NULL,\n" +
				"  `title` text NOT NULL,\n" +
				"  `published_flag` tinyint(1) NOT NULL DEFAULT '0',\n" +
				"  `featured_flag` tinyint(1) NOT NULL DEFAULT '0',\n" +
				"  `update_time` timestamp NOT NULL,\n" +
				"  `vote_count` int(11) NOT NULL DEFAULT '0',\n" +
				"  PRIMARY KEY (`id`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n";

		String create_document_tags = "CREATE TABLE IF NOT EXISTS `document_tags` (\n" +
				"  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
				"  `tag_id` bigint(20) NOT NULL,\n" +
				"  `document_id` bigint(20) NOT NULL,\n" +
				"  PRIMARY KEY (`id`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1;";

		String create_revisions = "CREATE TABLE IF NOT EXISTS `revisions` (\n" +
				"  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
				"  `document_id` bigint(20) NOT NULL,\n" +
				"  `creation_time` timestamp NOT NULL,\n" +
				"  `content` text NOT NULL,\n" +
				"  PRIMARY KEY (`id`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1;";

		String create_tags = "CREATE TABLE IF NOT EXISTS `tags` (\n" +
				"  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
				"  `name` text NOT NULL,\n" +
				"  `banned_flag` tinyint(1) NOT NULL DEFAULT '0',\n" +
				"  `reference_count` int(11) NOT NULL DEFAULT '0',\n" +
				"  PRIMARY KEY (`id`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1;";

		String create_users	 = "CREATE TABLE IF NOT EXISTS `users` (\n" +
				"  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
				"  `username` varchar(255) NOT NULL,\n" +
				"  `password` varchar(255) NOT NULL,\n" +
				"  `teacher_flag` tinyint(1) NOT NULL DEFAULT '0',\n" +
				"  PRIMARY KEY (`id`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1;";

		String create_votes = "CREATE TABLE IF NOT EXISTS `votes` (\n" +
				"  `user_id` bigint(20) NOT NULL,\n" +
				"  `document_id` bigint(20) NOT NULL,\n" +
				"  PRIMARY KEY (`user_id`,`document_id`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n";

		String create_settings = "CREATE TABLE IF NOT EXISTS `settings` (\n" +
				"  `setting_name` varchar(255) NOT NULL,\n" +
				"  `setting_value` text NOT NULL,\n" +
				"  UNIQUE KEY `setting_name` (`setting_name`)\n" +
				") ENGINE=InnoDB DEFAULT CHARSET=latin1;";

		String create_login_banner = "INSERT IGNORE INTO `flashmoblearning`.`settings` (`setting_name`, `setting_value`) VALUES ('login_banner', 'Welcome to Flash Mob Learning!')";

		Statement statement = connection.createStatement();
		statement.execute(create_documents);
		statement.execute(create_document_tags);
		statement.execute(create_revisions);
		statement.execute(create_tags);
		statement.execute(create_users);
		statement.execute(create_votes);
		statement.execute(create_settings);
		statement.execute(create_login_banner);
	}
	
}
