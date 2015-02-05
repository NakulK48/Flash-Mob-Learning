package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

import uk.ac.cam.grpproj.lima.flashmoblearning.User;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateNameException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.IllegalDatabaseStateException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** Singleton class which opens the database connection, and deals with global config and Tags.
 * Most of the work is done by DocumentManager and LoginManager.
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
	
	public static Database getInstance() {
		return m_Instance;
	}
	
	private static final String DEFAULT_TEACHER_PASSWORD = "password";
	private static final String LOGIN_PASSWORD_NAG = 
			"Welcome to Flash Mob Learning! Please login to the account " +
			"username \"Teacher\" with password \"password\" (no quotes " +
			"needed), change your password, and change this login banner to " +
			"something like \"Welcome to coding at XYZ school! Please create " +
			"an account with your real name.\".";

	/** Initializes and tests a MySQL database connection, setting up the 
	 * database if necessary, and exiting if it is incorrectly setup.
	 * REQUIREMENTS: An external mysql server with username and password as above,
	 * a database called flashmoblearning and appropriate permissions. **/
	public static void init() throws ClassNotFoundException, SQLException {
		if(Boolean.getBoolean("useHsqlDb")) {
            Class.forName("org.hsqldb.jdbcDriver");
            try {
                File f = null;
                if(Boolean.getBoolean("unitTests")) {
                    f = File.createTempFile("flashmoblearning", ".test.db");
                    f.deleteOnExit();
                } else {
                    f = new File(System.getProperty("user.home"),".flashmoblearning.hdb");
                }
                init("jdbc:hsqldb:"+f+";sql.syntax_mys=true","SA","");
            } catch (IOException e) {
                throw new RuntimeException("Failed to create temporary JDBC database file.");
            }
        } else {
            Class.forName("com.mysql.jdbc.Driver");
            init(c_JDBCURL, c_Username, c_Password);
        }
	}

	/** Portable setup from an arbitrary JDBC URL */
	private static void init(String databaseURL, String username, String password) throws ClassNotFoundException, SQLException {
		Connection connection = DriverManager.getConnection(databaseURL, username, password);
		boolean createUser = setup(connection);
        m_Instance = new Database(connection);

        if(createUser) {
            LoginManager lm = LoginManager.getInstance();
            try {
                User teacher = lm.createUser("Teacher", "", true);
                teacher.setPassword(DEFAULT_TEACHER_PASSWORD);
                lm.modifyUser(teacher);
                lm.setLoginBanner(LOGIN_PASSWORD_NAG);
                System.out.println("Created default user and set login banner");
            } catch (DuplicateNameException e) {
                throw new IllegalStateException("Duplicate name in spite of empty database?!", e);
            } catch (NoSuchObjectException e) {
                throw new IllegalStateException("Impossible setting up database: "+e, e);
            }
        }
	}
	
	private Database(Connection connection) throws SQLException {
		m_Connection = connection;
		m_LoginManagerInstance = new LoginManager(this);
		m_DocumentManagerInstance = new DocumentManager(this);
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

	/** Creates all necessary tables if they do not exist.
	 * @return True if we created tables. **/
	private static boolean setup(Connection connection) throws SQLException {
        boolean ret = false;

        String create_documents = "CREATE TABLE documents (\n" +
				"  id bigint NOT NULL AUTO_INCREMENT,\n" +
				"  user_id bigint NOT NULL,\n" +
				"  type int NOT NULL,\n" +
				"  title text NOT NULL,\n" +
				"  published_flag tinyint NOT NULL DEFAULT '0',\n" +
				"  featured_flag tinyint NOT NULL DEFAULT '0',\n" +
				"  update_time timestamp NOT NULL,\n" +
				"  vote_count int NOT NULL DEFAULT '0',\n" +
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
				"  name text NOT NULL,\n" +
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
				  "CREATE UNIQUE INDEX setting_name on settings(setting_name)"
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

            ret = true;
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
		return ret;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		init();
		System.out.println("Success!");
	}
}
