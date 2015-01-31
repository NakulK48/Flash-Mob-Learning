package uk.ac.cam.grpproj.lima.flashmoblearning.database.memory;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.Database;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.LoginManager;

public class MemoryDatabase extends Database {

	@Override
	public void close() {
		// Ignore.
	}

	DocumentManager docManager = new MemoryDocumentManager(this);
	
	LoginManager loginManager = new MemoryLoginManager(this);
	
	@Override
	public DocumentManager getDocumentManager() {
		return docManager;
	}

	@Override
	public LoginManager getLoginManager() {
		return loginManager;
	}

}
