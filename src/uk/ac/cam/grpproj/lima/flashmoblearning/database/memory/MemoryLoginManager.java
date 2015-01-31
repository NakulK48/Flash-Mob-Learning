package uk.ac.cam.grpproj.lima.flashmoblearning.database.memory;

import java.util.HashMap;

import uk.ac.cam.grpproj.lima.flashmoblearning.User;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateNameException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.LoginManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;

public class MemoryLoginManager extends LoginManager {
	
	MemoryLoginManager(MemoryDatabase db) {
		this.db = db;
		this.usersByID = new HashMap<Long,User>();
		this.usersByName = new HashMap<String,User>();
	}
	
	private final MemoryDatabase db;
	private final HashMap<Long,User> usersByID;
	private final HashMap<String,User> usersByName;

	@Override
	public synchronized User getUser(String username) {
		if(username == null) throw new NullPointerException();
		return usersByName.get(username);
	}

	@Override
	public synchronized void deleteUser(User u) throws NoSuchObjectException {
		if(usersByID.get(u.id) == null) throw new NoSuchObjectException();
		assert(usersByID.get(u.id) == u);
		assert(usersByName.get(u.name) == u);
		usersByID.remove(u.id);
		usersByName.remove(u.name);
	}

	@Override
	public synchronized User createUser(String username, String saltedPassword)
			throws DuplicateNameException {
		if(usersByName.containsKey(username)) throw new DuplicateNameException();
		User u = new User(db.createUserID(), username, saltedPassword);
		usersByID.put(u.id, u);
		usersByName.put(u.name, u);
		return u;
	}

	@Override
	public User modifyUser(User u) {
		// Do nothing.
		return u;
	}

}
