package uk.ac.cam.grpproj.lima.flashmoblearning;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DuplicateNameException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.LoginManager;

/** Base of Student and Teacher. */
public class User {
	
	/** User has a fixed numerical ID, even if their name is changed. */
	public final long id;

	/** Name of the user */
	public final String name;

	private String encryptedPassword;
	
	/** Try to log in.
	 * @return True if the password is correct. */
	public synchronized boolean checkPassword(String password) {
		// FIXME SECURITY: Hash passwords with a salt, use MessageDigest.isEqual etc.
		return password.equals(encryptedPassword);
	}
	
	/** Set password */
	public void setPassword(String newPassword) {
		// FIXME SECURITY: Hash passwords with a salt, use MessageDigest.isEqual etc.
		synchronized(this) {
			if(this.encryptedPassword.equals(newPassword)) return;
		}
		try {
			LoginManager.getInstance().modifyUser(this);
		} catch (DuplicateNameException e) {
			// Impossible.
			throw new Error(e);
		}
	}
	
	public User(long id, String name, String epass) {
		this.id = id;
		this.name = name;
		this.encryptedPassword = epass;
	}
	
}
