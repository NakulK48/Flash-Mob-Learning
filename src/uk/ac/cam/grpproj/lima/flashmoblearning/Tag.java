package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateEntryException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

public class Tag {
	
	/** Tag name */
	public final String name;
	/** Every tag has a unique ID which never changes.
	 * This must be set by the database when the tag is first stored. It cannot be changed
	 * after that point. */
	private long id;
	
	/** Has the tag been banned? If so it may not be added to documents. */
	private boolean banned;
	
	/** Create a tag and store it to the database */
	public static Tag create(String name) throws NotInitializedException, SQLException, NoSuchObjectException, DuplicateEntryException {
		return DocumentManager.getInstance().createTag(name, false);
	}

	/** Get the tag ID. Called by database */
	public long getID() {
		return id;
	}

	/** Called by the database when a tag is first stored */
	public void setID(long newID) throws IDAlreadySetException {
		if(this.id == -1) {
			this.id = newID;
		} else {
			if(id != newID) // OK to set to existing ID
				throw new IDAlreadySetException();
		}
	}
	
	/** SHOULD ONLY BE CALLED BY DATABASE! */
	public Tag(long id, String n, boolean banned) {
		this.name = n;
		this.id = id;
		this.banned = banned;
		if(n == null) throw new NullPointerException(); // Fail early.
	}
	
	/** Has the tag been banned? */
	public boolean getBanned() {
		return banned;
	}
	
	/** Ban or unban the tag. Updates the database.
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public void setBanned(boolean b) throws NotInitializedException, SQLException, NoSuchObjectException {
		if(banned == b) return;
		banned = b;
		DocumentManager.getInstance().updateTagBanned(this);
		if(b)
			DocumentManager.getInstance().deleteTagReferences(this);
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
