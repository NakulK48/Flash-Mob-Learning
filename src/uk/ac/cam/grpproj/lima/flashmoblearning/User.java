package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.LoginManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateNameException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** Base of Student and Teacher. */
public class User {
	
	/** Name of the user.
	 * FIXME Consider ability (for teacher?) to change user names. */
	public final String name;

	/** User has a fixed numerical ID, even if their name is changed. This is 
	 * set by the database when the document is first stored, and cannot be 
	 * changed after that. */
	private long id;
	private String encryptedPassword;
	
	/** Called by database */
	public synchronized long getID() {
		return id;
	}
	
	/** Called by the database when a document is first stored */
	public synchronized void setID(long newID) throws IDAlreadySetException {
		if(this.id == -1) {
			this.id = newID;
		} else {
			if(id != newID) // OK to set to existing ID
				throw new IDAlreadySetException();
		}
	}

	/** Try to log in.
	 * @return True if the password is correct. */
	public synchronized boolean checkPassword(String password) {
		// FIXME SECURITY: Hash passwords with a salt, use MessageDigest.isEqual etc.
		return password.equals(encryptedPassword);
	}
	
	/** Set password 
	 * @throws DuplicateNameException 
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public void setPassword(String newPassword) throws NotInitializedException, SQLException, NoSuchObjectException, DuplicateNameException {
		// FIXME SECURITY: Hash passwords with a salt, use MessageDigest.isEqual etc.
		synchronized(this) {
			if(this.encryptedPassword.equals(newPassword)) return;
		}
		LoginManager.getInstance().modifyUser(this);
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	
	/** Only called by LoginManager. See LoginManager.createUser(). */
	public User(long id, String name, String epass) {
		this.id = id;
		this.name = name;
		this.encryptedPassword = epass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return name.equals(other.name);
	}


}
