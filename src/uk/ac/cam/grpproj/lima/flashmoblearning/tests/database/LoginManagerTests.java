package uk.ac.cam.grpproj.lima.flashmoblearning.tests.database;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.cam.grpproj.lima.flashmoblearning.User;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.Database;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.LoginManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Connection;

public class LoginManagerTests {

    public static final String c_TestUsername = "loginmanager_test";
    public static final String c_TestPassword = "test_password";
    private Statement m_Statement;
    private Connection m_Connection;

    @Before
    public void setUp() throws Exception {
        Database.init();
        m_Statement = Database.getInstance().getStatement();
        m_Connection = Database.getInstance().getConnection();

        // Create a test user
        m_Statement.executeUpdate("DELETE FROM users WHERE username = 'loginmanager_test'");
        PreparedStatement ps = m_Connection.prepareStatement("INSERT INTO users (`username`, `password`, `teacher_flag`) VALUES (?, ?, 0)");
        ps.setString(1, c_TestUsername);
        ps.setString(2, "test_password");
        ps.executeUpdate();
    }

    @After
    public void tearDown() throws Exception {
        Database.getInstance().getStatement().executeUpdate("DELETE FROM users WHERE username = 'loginmanager_test'");
        Database.getInstance().close();
    }

    @Test
    public void testIsInitialized() throws Exception {
        LoginManager m = LoginManager.getInstance();
        // exception should be thrown above if it's not initialized.
    }

    @Test
    // Tests both the fact that we can get the user, and that the user in setup has been created.
    public void testGetUser() throws Exception {
        User user = LoginManager.getInstance().getUser(c_TestUsername);
        Assert.assertEquals("User retrieved", c_TestUsername, user.name);
    }

    @Test(expected=NoSuchObjectException.class)
    public void testGetNonexistentUser() throws Exception {
        User u = LoginManager.getInstance().getUser("user_which_does_not_exist");
    }

    @Test(expected=NoSuchObjectException.class)
    public void testDeleteUser() throws Exception {
        User u = LoginManager.getInstance().getUser(c_TestUsername);
        LoginManager.getInstance().deleteUser(u);

        // Trying to get this user should return a NoSuchObjectException
        LoginManager.getInstance().getUser(c_TestUsername);
    }

    @Test
    public void testModifyUserPassword() throws Exception {
        User u = LoginManager.getInstance().getUser(c_TestUsername);
        u.setPassword(c_TestPassword);
        LoginManager.getInstance().modifyUser(u);

        User u2 = LoginManager.getInstance().getUser(c_TestUsername);
        Assert.assertEquals(true, u2.checkPassword(c_TestPassword));
    }

    @Test
    public void testSetAndGetLoginBanner() throws Exception {
        String original = LoginManager.getInstance().getLoginBanner();

        // Perform the actual test.
        String test_banner = "This is a test banner.";
        LoginManager.getInstance().setLoginBanner(test_banner);
        Assert.assertEquals(test_banner, LoginManager.getInstance().getLoginBanner());

        // Restore the original banner (if any)
        LoginManager.getInstance().setLoginBanner(original);
    }
}