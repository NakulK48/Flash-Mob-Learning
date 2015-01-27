package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import java.util.List;

import uk.ac.cam.grpproj.lima.flashmoblearning.User;

public abstract class LoginManager {
	
	/** Get a user by username */
	public abstract User getUser(String username);
	
	/** Delete a user by username */
	public abstract User deleteUser(String username);
	
	/** Create a user */
	public abstract User createUser(String username, String saltedPassword);
	
	/** Modify a user, index by userId */
	public abstract User modifyUser(User u);
	
	
	
}
