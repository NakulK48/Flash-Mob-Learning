package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import uk.ac.cam.grpproj.lima.flashmoblearning.Document;
import uk.ac.cam.grpproj.lima.flashmoblearning.User;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateEntryException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.IllegalDatabaseStateException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * The database class is responsible for initialising the database, setting up the tables (if database is empty),
 * and maintaining the static instances of Login Manager and Document Manager.
 *
 * A copy of the database schema is stored in the setup(connection) method.
 */
public class Database {
	
	private static Database m_Instance;
	private Connection m_Connection = null;
	private LoginManager m_LoginManagerInstance;
	private DocumentManager m_DocumentManagerInstance;

	// FIXME these should be passed in on command line or better a config file.
	private static final String c_Username = "flashmoblearning";
	private static final String c_Password = "flashmoblearning";
	private static final String c_JDBCURL = "jdbc:mysql://localhost/flashmoblearning";
	
	/**
	 * Obtain the static Database instance.
	 * @return The static Database instance.
	 * @throws NotInitializedException the static Database instance isn't initialised.
	 */
	public static Database getInstance() throws NotInitializedException {
        if(m_Instance != null)
            return m_Instance;
        else
            throw new NotInitializedException();
	}
	
	public static final String DEFAULT_TEACHER_USERNAME = "Teacher";
	private static final String DEFAULT_TEACHER_PASSWORD = "password";
	private static final String LOGIN_PASSWORD_NAG = 
			"Welcome to Flash Mob Learning! Please login to the account " +
			"username \"Teacher\" with password \"password\" (no quotes " +
			"needed), change your password, and change this login banner to " +
			"something like \"Welcome to coding at XYZ school! Please create " +
			"an account with your real name.\".";

	/**
	 * Initialises the database with the default MySQL connection parameters.
	 * Defaults: localhost, username/password flashmoblearning.
	 * @throws ClassNotFoundException unable to load the MySQL driver.
	 * @throws SQLException an error has occurred in the database.
	 */
	public static void init() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
        init(c_JDBCURL, c_Username, c_Password);
	}

	/** Portable setup from an arbitrary JDBC URL */
	/**
	 * Initialises the database given a JDBC URL, username and password.
	 * This method is thread-safe.
	 * This method will return silently if the database is already initialised.
	 * @param databaseURL database JDBC URL.
	 * @param username database username.
	 * @param password database password.
	 * @throws SQLException an error has occurred in the database.
	 */
	public static synchronized void init(String databaseURL, String username, String password) throws SQLException {
		// Check if we already have an active connection, and return if we do.
		if(m_Instance != null && m_Instance.getConnection() != null && !m_Instance.getConnection().isClosed()) {
			return;
		}
		
		// No active connection, either uninitialised/closed - start one.
		Connection connection = DriverManager.getConnection(databaseURL, username, password);
		setup(connection);
        m_Instance = new Database(connection);
		createDefaultUser();
	}

	/**
	 * Creates the default user and updates the login banner to set a password-change nag.
	 * Should only be called internally on setup, if no users exist.
	 * @throws SQLException an error has occurred in the database.
	 */
	private static void createDefaultUser() throws SQLException {
		// Check if there are any users, if not, create one.
		if(LoginManager.getInstance().getAllUsers(new QueryParam(1)).size() == 0) {
            LoginManager lm = LoginManager.getInstance();
            try {
                User teacher = lm.createUser(DEFAULT_TEACHER_USERNAME, "", true);
                teacher.setPassword(DEFAULT_TEACHER_PASSWORD);
                lm.modifyUser(teacher);
                lm.setLoginBanner(LOGIN_PASSWORD_NAG);
                System.out.println("Created default user and set login banner");
            } catch (DuplicateEntryException e) {
                throw new IllegalStateException("Duplicate name in spite of empty database?!", e);
            } catch (NoSuchObjectException e) {
                throw new IllegalStateException("Impossible setting up database: "+e, e);
            }
        }
	}

	/**
	 * Creates a new Database instance with given connection.
	 * @param connection an initialised connection.
	 * @throws SQLException an error has occurred in the database.
	 */
	private Database(Connection connection) throws SQLException {
		m_Connection = connection;
		m_LoginManagerInstance = new LoginManager(this);
		m_DocumentManagerInstance = new DocumentManager(this);
	}

	/**
	 * Obtains the existing database connection.
	 * @return The backing database connection.
	 * @throws SQLException an error has occurred in the database.
	 */
	public Connection getConnection() throws SQLException {
		return m_Connection;
	}

	/**
	 * Obtains a new statement, ready for execution
	 * @return new database statement.
	 * @throws SQLException an error has occurred in the database.
	 */
	public Statement getStatement() throws SQLException {
		return m_Connection.createStatement();
	}

	/**
	 * Shuts down the database and closes the connection.
	 */
	public void close() {
		try {
			m_Connection.close();
		} catch (SQLException e) {
			// ignore, this connection is already closed.
		}
	}

	/**
	 * Obtain the static DocumentManager instance.
	 * @return The static DocumentManager instance.
	 * @throws NotInitializedException the static DocumentManager instance isn't initialised.
	 */
	public DocumentManager getDocumentManager() throws NotInitializedException {
		if(m_DocumentManagerInstance != null)
			return m_DocumentManagerInstance;
		else
			throw new NotInitializedException();
	}

	/**
	 * Obtain the static LoginManager instance.
	 * @return The static LoginManager instance.
	 * @throws NotInitializedException the static LoginManager instance isn't initialised.
	 */
	public LoginManager getLoginManager() throws NotInitializedException {
		if(m_LoginManagerInstance != null)
			return m_LoginManagerInstance;
		else
			throw new NotInitializedException();
	}

	/**
	 * Creates all necessary tables if they do not exist.
	 * If the database is not empty but incomplete, it is deemed corrupt and setup fails with an exception.
	 * @param connection connection to run table setup.
	 * @throws SQLException an error has occurred in the database.
	 */
	private static void setup(Connection connection) throws SQLException {
        String create_documents = "CREATE TABLE documents (\n" +
				"  id bigint NOT NULL AUTO_INCREMENT,\n" +
				"  user_id bigint NOT NULL,\n" +
				"  type int NOT NULL,\n" +
				"  title text NOT NULL,\n" +
				"  published_flag tinyint NOT NULL DEFAULT '0',\n" +
				"  featured_flag tinyint NOT NULL DEFAULT '0',\n" +
				"  update_time timestamp NOT NULL,\n" +
				"  vote_count int NOT NULL DEFAULT '0',\n" +
				"  score float NOT NULL DEFAULT '0',\n" +
				"  PRIMARY KEY (id)\n" +
				")\n";

		String create_document_tags = "CREATE TABLE document_tags (\n" +
				"  tag_id bigint NOT NULL,\n" +
				"  document_id bigint NOT NULL,\n" +
				"  PRIMARY KEY (tag_id, document_id)\n" +
				")";
		
		String create_document_parents = "CREATE TABLE document_parents (\n" +
				"  document_id bigint NOT NULL,\n" +
				"  parent_document_id bigint NOT NULL,\n" +
				"  PRIMARY KEY (document_id)\n" +
				")";

		String create_revisions = "CREATE TABLE revisions (\n" +
				"  id bigint NOT NULL AUTO_INCREMENT,\n" +
				"  document_id bigint NOT NULL,\n" +
				"  update_time timestamp NOT NULL,\n" +
				"  content text NOT NULL,\n" +
				"  PRIMARY KEY (id)\n" +
				")";

		String create_tags = "CREATE TABLE tags (\n" +
				"  id bigint NOT NULL AUTO_INCREMENT,\n" +
				"  name varchar(255) NOT NULL,\n" +
				"  banned_flag tinyint NOT NULL DEFAULT '0',\n" +
				"  PRIMARY KEY (id)\n" +
				")";

		String create_users	 = "CREATE TABLE users (\n" +
				"  id bigint NOT NULL AUTO_INCREMENT,\n" +
				"  username varchar(255) NOT NULL,\n" +
				"  password varchar(255) NOT NULL,\n" +
				"  teacher_flag tinyint NOT NULL DEFAULT '0',\n" +
				"  PRIMARY KEY (id)\n" +
				")";

		String create_votes = "CREATE TABLE votes (\n" +
				"  user_id bigint NOT NULL,\n" +
				"  document_id bigint NOT NULL,\n" +
				"  PRIMARY KEY (user_id,document_id)\n" +
				")\n";

		String create_settings = "CREATE TABLE settings (\n" +
				"  setting_name varchar(255) NOT NULL,\n" +
				"  setting_value text NOT NULL\n" +
				")";
		
		String[] create_indexes = new String[] 
				{ "CREATE INDEX documents_user_id on documents(user_id)",
				  "CREATE INDEX document_tags_document_id on document_tags(document_id)",
				  "CREATE INDEX document_parents_parent on document_parents(parent_document_id)",
				  "CREATE INDEX revisions_document_id on revisions(document_id)",
				  "CREATE UNIQUE INDEX users_username on users(username)",
				  "CREATE INDEX votes_document_id on votes(document_id)",
				  "CREATE UNIQUE INDEX setting_name on settings(setting_name)",
                  "CREATE UNIQUE INDEX tag_name on tags(name)",
				};
		
		String[] create_fks = new String[]
				{"ALTER TABLE documents ADD CONSTRAINT documents_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE",
				 "ALTER TABLE document_tags ADD CONSTRAINT document_tags_ibfk_2 FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE",
				 "ALTER TABLE document_tags ADD CONSTRAINT document_tags_ibfk_1 FOREIGN KEY (document_id) REFERENCES documents (id) ON DELETE CASCADE",
				 "ALTER TABLE document_parents ADD CONSTRAINT document_parents_ibfk_2 FOREIGN KEY (parent_document_id) REFERENCES documents (id) ON DELETE CASCADE",
				 "ALTER TABLE document_parents ADD CONSTRAINT document_parents_ibfk_1 FOREIGN KEY (document_id) REFERENCES documents (id) ON DELETE CASCADE",
				 "ALTER TABLE revisions ADD CONSTRAINT revisions_ibfk_1 FOREIGN KEY (document_id) REFERENCES documents (id) ON DELETE CASCADE",
				 "ALTER TABLE votes ADD CONSTRAINT votes_ibfk_2 FOREIGN KEY (document_id) REFERENCES documents (id) ON DELETE CASCADE",
				 "ALTER TABLE votes ADD CONSTRAINT votes_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE"};
		
		String[] create_triggers = new String[]
				{"CREATE TRIGGER add_vote AFTER INSERT ON votes FOR EACH ROW UPDATE documents SET vote_count = vote_count+1, score = vote_count * EXP(-1 * POWER(time_to_sec(timediff(NOW(),update_time)) / 3600,2)/" + Document.AGING_CONSTANT + ") WHERE id = NEW.document_id;"};

		String check_login_banner = "SELECT * FROM settings WHERE setting_name = 'login_banner'";
		String create_login_banner = "INSERT INTO settings (setting_name, setting_value) VALUES ('login_banner', 'Welcome to Flash Mob Learning!')";

		// Check if tables exist
		int table_count = 0;
		List<String> tables = Arrays.asList(new String[] { "documents", "document_parents", "document_tags", "revisions", "tags", "users", "votes" });

		Statement statement = connection.createStatement();
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		ResultSet rs = databaseMetaData.getTables(null, null, "%", null);
		while(rs.next()) {
			String tnam = rs.getString("TABLE_NAME");
			if(tables.contains(tnam) || tables.contains(tnam.toLowerCase()))
				table_count++;
		}


		// Database does not contain all tables
		if(table_count < tables.size()) {
			// If some tables exist - database corrupted!
			if(table_count > 0)
				throw new IllegalDatabaseStateException(table_count + " of " + tables.size() + " tables exist, database state corrupt.");

			System.out.println("Creating tables");
			// Create tables
			statement.execute(create_documents);
			statement.execute(create_document_tags);
			statement.execute(create_document_parents);
			statement.execute(create_revisions);
			statement.execute(create_tags);
			statement.execute(create_users);
			statement.execute(create_votes);
			statement.execute(create_settings);
		}

		// Checks and creates login banner if it does not exist.
		if (!statement.executeQuery(check_login_banner).next())
			statement.execute(create_login_banner);

		// Create indexes - will throw an exception if they already exist, which can be ignored.
		for (String create_index : create_indexes) {
			try {
				statement.execute(create_index);
			} catch (SQLException e) {
			}
		}
		
		// Create foreign keys - will throw an exception if they already exist, which can be ignored.
		for (String create_fk : create_fks) {
			try {
				statement.execute(create_fk);
			} catch (SQLException e) {
			}
		}
		
		// Create triggers - will throw an exception if they already exist, which can be ignored.
		for (String create_trigger : create_triggers) {
			try {
				statement.execute(create_trigger);
			} catch (SQLException e) {
			}
		}
	}
}
