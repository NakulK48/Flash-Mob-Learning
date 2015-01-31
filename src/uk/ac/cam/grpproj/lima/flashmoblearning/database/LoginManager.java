package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import uk.ac.cam.grpproj.lima.flashmoblearning.User;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateNameException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

import java.sql.SQLException;

public abstract class LoginManager {

	private Database m_Database;

	protected LoginManager(Database database) {
		m_Database = database;
	}

	public static LoginManager getInstance() throws NotInitializedException {
		return Database.getInstance().getLoginManager();
	}
	
	/** Get a user by username */
	public abstract User getUser(String username) throws SQLException, NoSuchObjectException;
	
	/** Delete a user by username */
	public abstract void deleteUser(User user) throws SQLException, NoSuchObjectException;
	
	/** Create a user */
	public abstract User createUser(String username, String saltedPassword) throws SQLException, DuplicateNameException;
	
	/** Modify a user, index by userId. */
	public abstract User modifyUser(User u) throws SQLException, NoSuchObjectException, DuplicateNameException;

	/** Get the login banner */
	public abstract String getLoginBanner() throws SQLException;

	/** Set the login banner */
	public abstract void setLoginBanner(String banner) throws SQLException;
}
