package uk.ac.cam.grpproj.lima.flashmoblearning.database.memory;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.Database;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.LoginManager;

public class MemoryDatabase extends Database {

	@Override
	public void close() {
		// Ignore.
	}

	private String loginBanner = "Test";
	
	private long docID = 0;
	private long userID = 0;
	
	DocumentManager docManager = new MemoryDocumentManager(this);
	
	LoginManager loginManager = new MemoryLoginManager(this);
	
	@Override
	public synchronized String getLoginBanner() {
		return loginBanner;
	}

	@Override
	public synchronized void setLoginBanner(String banner) {
		loginBanner = banner;
	}

	@Override
	public synchronized long createDocumentID() {
		return docID++;
	}

	@Override
	public synchronized long createUserID() {
		return userID++;
	}

	@Override
	public DocumentManager getDocumentManager() {
		return docManager;
	}

	@Override
	public LoginManager getLoginManager() {
		return loginManager;
	}

}
