package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import uk.ac.cam.grpproj.lima.flashmoblearning.User;

import java.sql.SQLException;

public abstract class LoginManager {
	
	public static LoginManager getInstance() {
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
