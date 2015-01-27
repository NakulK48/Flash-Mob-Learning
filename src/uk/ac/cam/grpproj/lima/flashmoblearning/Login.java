package uk.ac.cam.grpproj.lima.flashmoblearning;

/** Another singleton */
public abstract class Login {

	/** Find a user by name */
	public abstract User getUser(String username);
	
	/** Log in, checking password. */
	public abstract User logIn(String username, String password) throws BadPasswordException;

	/** Set a user's password */
	public abstract void setPassword(User user, String newPassword);
	
	/** Delete a user and all their data */
	public abstract void deleteUser(User user);

}
