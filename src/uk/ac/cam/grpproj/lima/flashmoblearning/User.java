package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.LoginManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateEntryException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** Base of Student and Teacher. */
public class User {
	
	/** Name of the user. Can be changed by administrator. */
	private String name;

	/** Get the user's name. */
	public String getName() {
		return name;
	}

	/** Change the user's name.
	 * @throw DuplicateEntryException There is already another user with the new name. */
	public void setName(String n) throws NotInitializedException, SQLException, NoSuchObjectException, DuplicateEntryException {
		name = n;
		LoginManager.getInstance().modifyUser(this);
	}
	
	/** User has a fixed numerical ID, even if their name is changed. This is 
	 * set by the database when the document is first stored, and cannot be 
	 * changed after that. */
	private long id;
	/** Password, may be hashed/salted. */
	private String encryptedPassword;
	
	/** Called by database */
	public long getID() {
		return id;
	}
	
	/** Called by the database when a document is first stored */
	public void setID(long newID) throws IDAlreadySetException {
		if(this.id == -1) {
			this.id = newID;
		} else {
			if(id != newID) // OK to set to existing ID
				throw new IDAlreadySetException();
		}
	}

	/** Try to log in.
	 * @return True if the password is correct. */
	public boolean checkPassword(String password) {
		// FIXME SECURITY: Hash passwords with a salt, use MessageDigest.isEqual etc.
		return password.equals(encryptedPassword);
	}
	
	/** Set password 
	 * @throws DuplicateEntryException Should not happen unless there are 
	 * renames happening at the same time.
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public void setPassword(String newPassword) throws NotInitializedException, SQLException, NoSuchObjectException, DuplicateEntryException {
		// FIXME SECURITY: Hash passwords with a salt, use MessageDigest.isEqual etc.
		if(this.encryptedPassword.equals(newPassword)) return;
		encryptedPassword = newPassword;
		LoginManager.getInstance().modifyUser(this);
	}

	/** Only called by database tests */
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	
	/** Only called by LoginManager. See LoginManager.createUser(). */
	public User(long id, String name, String epass) {
		this.id = id;
		this.name = name;
		this.encryptedPassword = epass;
		if(name == null) throw new NullPointerException();
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
