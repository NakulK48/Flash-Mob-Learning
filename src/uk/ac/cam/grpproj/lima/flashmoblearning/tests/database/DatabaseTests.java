package uk.ac.cam.grpproj.lima.flashmoblearning.tests.database;

import junit.framework.Assert;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.Database;

import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseTests {

    @org.junit.Before
    public void setUp() throws Exception {
        Database.init();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        Database.getInstance().close();
    }

    @org.junit.Test
    // This test ensures that our database connection was successful.
    public void testGetConnection() throws Exception {
        Assert.assertEquals("Connection should be valid", true, Database.getInstance().getConnection().isValid(1000));
    }

    @org.junit.Test
    // This test ensures we can execute a query over the connection.
    public void testGetStatement() throws Exception {
        Statement statement = Database.getInstance().getStatement();
        ResultSet rs = statement.executeQuery("SELECT 1");
        Assert.assertEquals("Should be able to execute a query", true, rs.next());
    }

    @org.junit.Test
     // This test ensures that our database has all the tables we need.
     public void testSetup() throws Exception {
        String[] tables = new String[] { "documents", "document_tags", "revisions", "tags", "users", "votes" };

        Statement statement = Database.getInstance().getStatement();
        for(String table : tables) {
            ResultSet rs = statement.executeQuery("SHOW TABLES LIKE '" + table + "'");
            Assert.assertEquals("Checking " + table + " exists", true, rs.next());
        }
    }

    @org.junit.Test
    public void testClose() throws Exception {
        Assert.assertEquals("DB Connection start off valid", false, Database.getInstance().getConnection().isClosed());
        Database.getInstance().close();
        Assert.assertEquals("DB Connection should have closed", true, Database.getInstance().getConnection().isClosed());
    }
}