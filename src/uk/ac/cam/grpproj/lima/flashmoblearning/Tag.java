package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateNameException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

public class Tag {
	
	/** Tag name */
	public final String name;
	
	private boolean banned;
	
	/** Create a tag and store it to the database */
	public static Tag create(String name) throws NotInitializedException, SQLException, NoSuchObjectException, DuplicateNameException {
		Tag t = new Tag(name);
		DocumentManager.getInstance().createTag(t);
		return t;
	}
	
	/** SHOULD ONLY BE CALLED BY DATABASE! */
	public Tag(String n) {
		name = n;
		if(n == null) throw new NullPointerException(); // Fail early.
	}
	
	/** Has the tag been banned? */
	public synchronized boolean getBanned() {
		return banned;
	}
	
	/** Ban or unban the tag 
	 * @throws DuplicateNameException 
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public void setBanned(boolean b) throws NotInitializedException, SQLException, NoSuchObjectException, DuplicateNameException {
		synchronized(this) {
			if(banned == b) return;
			banned = b;
		}
		DocumentManager.getInstance().updateTag(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (banned ? 1231 : 1237);
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
		Tag other = (Tag) obj;
		if (banned != other.banned)
			return false;
		return name.equals(other.name);
	}

}
