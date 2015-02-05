package uk.ac.cam.grpproj.lima.flashmoblearning;

import junit.framework.Assert;

import org.junit.Test;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.Database;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.LoginManager;

/** Only includes tests that can be done on a raw Document. */
public class DocumentTest {
	
	final DocumentType docType = DocumentType.PLAINTEXT;
	private User owner;
	final String titleSimple = "Test document";
	
    @org.junit.Before
    public void setUp() throws Exception {
    	Database.init();
    	owner = LoginManager.getInstance().getUser(Database.DEFAULT_TEACHER_USERNAME);
    }

    @org.junit.After
    public void tearDown() throws Exception {
        Database.getInstance().close();
    }

	final int id = 1;
	final int otherID = 2;
	
    @Test
    public void testSetIDTwice() throws IDAlreadySetException {
    	Document doc = new Document(-1, docType, owner, titleSimple, 
    			System.currentTimeMillis());
    	Assert.assertEquals(doc.getID(), -1);
    	doc.setID(id);
    	Assert.assertEquals(id, doc.getID());
    	try {
    		doc.setID(otherID);
    		Assert.fail("Must throw here");
    	} catch (IDAlreadySetException e) {
    		// Ok.
    	}
    }

    @Test
    public void testSetIDOnCreation() throws IDAlreadySetException {
    	Document doc = new Document(id, docType, owner, titleSimple, 
    			System.currentTimeMillis());
    	Assert.assertEquals(doc.getID(), id);
    	try {
    		doc.setID(otherID);
    		Assert.fail("Must throw here");
    	} catch (IDAlreadySetException e) {
    		// Ok.
    	}
    }
    
}
