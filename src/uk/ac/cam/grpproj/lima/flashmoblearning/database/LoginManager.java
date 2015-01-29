package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import uk.ac.cam.grpproj.lima.flashmoblearning.User;

public abstract class LoginManager {
	
	public static LoginManager getInstance() {
		return Database.getInstance().getLoginManager();
	}
	
	/** Get a user by username */
	public abstract User getUser(String username) throws NoSuchObjectException;
	
	/** Delete a user by username */
	public abstract void deleteUser(User user) throws NoSuchObjectException;
	
	/** Create a user */
	public abstract User createUser(String username, String saltedPassword) throws DuplicateNameException;
	
	/** Modify a user, index by userId. */
	public abstract User modifyUser(User u) throws NoSuchObjectException, DuplicateNameException;
	
}
