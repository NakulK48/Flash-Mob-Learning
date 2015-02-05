package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Test;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.Database;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.LoginManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateNameException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

public class UserTest {
	
	public final String testUser = "TestUser";
	public final String testPassword = "password";
	public final String testPassword2 = "123456;_";
	
    @org.junit.Before
    public void setUp() throws Exception {
    	Database.init();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        Database.getInstance().close();
    }
    
	final int id = 1;
	final int otherID = 2;
	
    @Test
    public void testSetIDTwice() throws IDAlreadySetException {
    	User u = new User(-1, testUser, "");
    	Assert.assertEquals(u.getID(), -1);
    	u.setID(id);
    	Assert.assertEquals(id, u.getID());
    	try {
    		u.setID(otherID);
    		Assert.fail("Must throw here");
    	} catch (IDAlreadySetException e) {
    		// Ok.
    	}
    }
    
	@Test
	public void testSetPassword() throws NotInitializedException, SQLException, DuplicateNameException, NoSuchObjectException {
		Student s = (Student) LoginManager.getInstance().createUser(testUser, "", false);
		Assert.assertNotSame(s.getID(), -1);
		Assert.assertEquals(s, LoginManager.getInstance().getUser(s.getID()));
		Assert.assertEquals(s, LoginManager.getInstance().getUser(testUser));
		Assert.assertFalse(s.checkPassword(testPassword));
		s.setPassword(testPassword);
		Assert.assertTrue(s.checkPassword(testPassword));
		s.setPassword(testPassword2);
		Assert.assertFalse(s.checkPassword(testPassword));
		Assert.assertTrue(s.checkPassword(testPassword2));
	}

	@Test
	public void testSetPasswordTeacher() throws NotInitializedException, SQLException, DuplicateNameException, NoSuchObjectException {
		Teacher s = (Teacher) LoginManager.getInstance().createUser(testUser, "", true);
		Assert.assertNotSame(s.getID(), -1);
		Assert.assertEquals(s, LoginManager.getInstance().getUser(s.getID()));
		Assert.assertEquals(s, LoginManager.getInstance().getUser(testUser));
		Assert.assertFalse(s.checkPassword(testPassword));
		s.setPassword(testPassword);
		Assert.assertTrue(s.checkPassword(testPassword));
		s.setPassword(testPassword2);
		Assert.assertFalse(s.checkPassword(testPassword));
		Assert.assertTrue(s.checkPassword(testPassword2));
	}

}
