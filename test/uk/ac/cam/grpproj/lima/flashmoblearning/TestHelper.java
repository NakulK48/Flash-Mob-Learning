package uk.ac.cam.grpproj.lima.flashmoblearning;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.Database;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Spencer on 8/2/2015.
 */
public class TestHelper {

    public static void databaseInit() throws ClassNotFoundException, SQLException {
        if(Boolean.getBoolean("useHsqlDb")) {
            Class.forName("org.hsqldb.jdbcDriver");
            try {
                File f = null;
                if(Boolean.getBoolean("unitTests")) {
                    f = File.createTempFile("flashmoblearning", ".test.db");
                    f.deleteOnExit();
                } else {
                    f = new File(System.getProperty("user.home"),".flashmoblearning.hdb");
                }
                Database.init("jdbc:hsqldb:" + f + ";sql.syntax_mys=true", "SA", "");
            } catch (IOException e) {
                throw new RuntimeException("Failed to create temporary JDBC database file.");
            }
        } else {
            Database.init();
        }
    }

    public static void databaseCleanTablesAndClose() throws SQLException {
        if(!Database.getInstance().getConnection().isClosed()) {
            List<String> tables = Arrays.asList(new String[]{"users", "documents", "document_parents", "document_tags", "revisions", "tags", "votes"});

            for (String table : tables) {
                Database.getInstance().getStatement().executeUpdate("DELETE FROM " + table);
            }

            Database.getInstance().close();
        }
    }

}
