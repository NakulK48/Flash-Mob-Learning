package uk.ac.cam.grpproj.lima.flashmoblearning.tests.database;

import junit.framework.Assert;
import org.junit.Test;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.QueryParam;

import javax.management.Query;

import static org.junit.Assert.*;

public class QueryParamTest {

    private final String c_StandardQuery = "SELECT * FROM table WHERE field = value";

    @Test
    public void testLimit() throws Exception {
        QueryParam param = new QueryParam(100);
        Assert.assertEquals("Expect limit", c_StandardQuery + " LIMIT 100", param.updateQuery(c_StandardQuery));
    }

    @Test
    public void testLimitAndOffset() throws Exception {
        QueryParam param = new QueryParam(100, 50);
        Assert.assertEquals("Expect limit and offset", c_StandardQuery + " LIMIT 100 OFFSET 50", param.updateQuery(c_StandardQuery));
    }

    @Test
    public void testSort() throws Exception {
        QueryParam param = new QueryParam(0, 0, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
        Assert.assertEquals("Expect sort", c_StandardQuery + " ORDER BY update_time DESCENDING", param.updateQuery(c_StandardQuery));
    }

    @Test
    public void testSortWithLimitAndOffset() throws Exception {
        QueryParam param = new QueryParam(30, 50, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
        Assert.assertEquals("Expect sort", c_StandardQuery + " ORDER BY update_time DESCENDING LIMIT 30 OFFSET 50", param.updateQuery(c_StandardQuery));
    }
}