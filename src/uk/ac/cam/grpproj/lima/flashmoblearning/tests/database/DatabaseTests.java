package uk.ac.cam.grpproj.lima.flashmoblearning.tests.database;

import junit.framework.Assert;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.Database;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTests {

    @org.junit.Before
    public void setUp() throws Exception {
    	if(Boolean.getBoolean("useMysql")) {
    		Database.init();
    	} else {
    		Database.initTemp();
    	}
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
        String[] tables = new String[] { "documents", "document_parents", "document_tags", "revisions", "tags", "users", "votes" };
        List<String> database_tables = new ArrayList<String>();

        DatabaseMetaData databaseMetaData = Database.getInstance().getConnection().getMetaData();
        ResultSet rs = databaseMetaData.getTables(null, null, "%", null);
        while(rs.next()) {
            database_tables.add(rs.getString("TABLE_NAME"));
        }

        for(String table : tables) {
            Assert.assertEquals(table + " table should exist", true, database_tables.contains(table) || database_tables.contains(table.toUpperCase()));
        }
    }

    @org.junit.Test
    public void testClose() throws Exception {
        Assert.assertEquals("DB Connection start off valid", false, Database.getInstance().getConnection().isClosed());
        Database.getInstance().close();
        Assert.assertEquals("DB Connection should have closed", true, Database.getInstance().getConnection().isClosed());
    }
}