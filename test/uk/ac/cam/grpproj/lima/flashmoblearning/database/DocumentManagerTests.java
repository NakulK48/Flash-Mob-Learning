package uk.ac.cam.grpproj.lima.flashmoblearning.database;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.cam.grpproj.lima.flashmoblearning.*;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.Database;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.LoginManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.QueryParam;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class DocumentManagerTests {

    private Statement m_Statement;
    private Connection m_Connection;
    private User m_TestUser;
    private User m_TestUser2;
    private WIPDocument m_WIP_Document;
    private PublishedDocument m_Published_Document;
    private Tag m_UsedTag;
    private Tag m_BannedTag;

    private static final String c_TestDocumentTitle = "Test Document";
    private static final String c_TestRevisionContent = "Test Revision";
    private static final String c_TestTagTitle = "Test Tag";

    @Before
    public void setUp() throws Exception {
    	Database.init();
        m_Statement = Database.getInstance().getStatement();
        m_Connection = Database.getInstance().getConnection();

        m_TestUser = LoginManager.getInstance().createUser("test_user", "test_password", false);

        // Each document is duplicated and assigned to test user 2.
        // The idea is that these documents should NOT be returned when asking for testuser1's documents.
        // This ensures a more comprehensive test.
        m_TestUser2 = LoginManager.getInstance().createUser("test_user2", "test_password2", false);

        m_WIP_Document = new WIPDocument(-1, DocumentType.PLAINTEXT, m_TestUser, c_TestDocumentTitle + " (WIP)", 0);
        m_Published_Document = new PublishedDocument(-1, DocumentType.PLAINTEXT, m_TestUser, c_TestDocumentTitle + " (PUBLISHED)", 0, 0);

        /**
         * DOCUMENTS
         */

        // Insert WIP document
        PreparedStatement ps = m_Connection.prepareStatement("INSERT INTO " +
                "documents (user_id, type, title, published_flag, featured_flag, update_time, vote_count) VALUES (?, ?, ?, ?, ?, NOW(), ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, m_TestUser.getID());
        ps.setInt(2, 0);
        ps.setString(3, c_TestDocumentTitle + " (WIP)");
        ps.setInt(4, 0);
        ps.setInt(5, 0);
        ps.setInt(6, 0);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys(); rs.next();
        m_WIP_Document.setID(rs.getInt(1));

        ps.setLong(1, m_TestUser2.getID());
        ps.executeUpdate();

        // Insert published document
        ps.setLong(1, m_TestUser.getID());
        ps.setString(3, c_TestDocumentTitle + " (PUBLISHED)");
        ps.setInt(4, 1);
        ps.executeUpdate();
        rs = ps.getGeneratedKeys(); rs.next();
        m_Published_Document.setID(rs.getInt(1));

        ps.setLong(1, m_TestUser2.getID());
        ps.executeUpdate();

        // Insert featured published document
        ps.setLong(1, m_TestUser.getID());
        ps.setString(3, c_TestDocumentTitle + " (FEATURED)");
        ps.setInt(5, 1);
        ps.executeUpdate();
        rs = ps.getGeneratedKeys(); rs.next();
        int featured_ID = rs.getInt(1);

        ps.setLong(1, m_TestUser2.getID());
        ps.executeUpdate();

        /**
         * PARENTS
         */

        ps = m_Connection.prepareStatement("INSERT INTO document_parents (document_id, parent_document_id) VALUES (?, ?)");
        ps.setLong(1, m_WIP_Document.getID());
        ps.setLong(2, m_Published_Document.getID());
        ps.executeUpdate();

        /**
         * REVISIONS
         */

        // Insert WIP revisions
        ps = m_Connection.prepareStatement("INSERT INTO revisions (document_id, update_time, content) VALUES (?, NOW(), ?)");
        ps.setLong(1, m_WIP_Document.getID());
        ps.setString(2, c_TestRevisionContent + " (WIP)");
        ps.executeUpdate(); // revision 1
        ps.executeUpdate(); // revision 2
        ps.executeUpdate(); // revision 3

        // Insert published revisions
        ps.setLong(1, m_Published_Document.getID());
        ps.setString(2, c_TestRevisionContent + " (PUBLISHED)");
        ps.executeUpdate();

        // Insert featured revisions
        ps.setLong(1, featured_ID);
        ps.setString(2, c_TestRevisionContent + " (FEATURED)");
        ps.executeUpdate();

        /**
         * TAGS
         */

        // Insert unused tag
        ps = m_Connection.prepareStatement("INSERT INTO tags (name, banned_flag) VALUES (?, ?)",  Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, c_TestTagTitle + " (UNUSED)");
        ps.setInt(2, 0);
        ps.executeUpdate();

        // Insert used tag (2 references, 1 by published and featured respectively.
        ps.setString(1, c_TestTagTitle + " (USED)");
        ps.executeUpdate();
        rs = ps.getGeneratedKeys(); rs.next();
        int usedtag_ID = rs.getInt(1);
        m_UsedTag = new Tag(usedtag_ID, c_TestTagTitle + " (USED)", false);

        // Insert banned tag
        ps.setString(1, c_TestTagTitle + " (BANNED)");
        ps.setInt(2, 1);
        ps.executeUpdate();
        rs = ps.getGeneratedKeys(); rs.next();
        int bannedtag_ID = rs.getInt(1);
        m_BannedTag = new Tag(bannedtag_ID, c_TestTagTitle + " (BANNED)", true);

        // Insert tag <-> document pair for published
        ps = m_Connection.prepareStatement("INSERT INTO document_tags (tag_id, document_id) VALUES (?, ?)");
        ps.setInt(1, usedtag_ID);
        ps.setLong(2, m_Published_Document.getID());
        ps.executeUpdate();

        // Insert tag <-> document pair for featured
        ps.setInt(2, featured_ID);
        ps.executeUpdate();

        /**
         * VOTES
         */

        // Add a vote for our published document
        ps = m_Connection.prepareStatement("INSERT INTO votes (user_id, document_id) VALUES (?, ?)");
        ps.setLong(1, m_TestUser.getID());
        ps.setLong(2, m_Published_Document.getID());
    }

    @After
    public void tearDown() throws Exception {
        Database.getInstance().getStatement().executeUpdate("DELETE FROM users WHERE id = '" + m_TestUser.getID() + "'");
        Database.getInstance().getStatement().executeUpdate("DELETE FROM users WHERE id = '" + m_TestUser2.getID() + "'");
        Database.getInstance().getStatement().executeUpdate("DELETE FROM tags WHERE name LIKE '" + c_TestTagTitle + "%'");
        Database.getInstance().close();
    }

    @Test
    public void testGetPublishedByUser() throws Exception {
        List<PublishedDocument> published = DocumentManager.getInstance().getPublishedByUser(m_TestUser, QueryParam.UNSORTED);
        Assert.assertEquals("Expect 2 published documents", 2, published.size());
        List<String> titles = Arrays.asList(new String[]{ published.get(0).getTitle(), published.get(1).getTitle() });

        Assert.assertEquals("Expect a published title", true, titles.contains(c_TestDocumentTitle + " (PUBLISHED)"));
        Assert.assertEquals("Expect a featured title", true, titles.contains(c_TestDocumentTitle + " (FEATURED)"));
    }

    @Test
    public void testGetWorkInProgressByUser() throws Exception {
        List<WIPDocument> wip = DocumentManager.getInstance().getWorkInProgressByUser(m_TestUser, QueryParam.UNSORTED);
        Assert.assertEquals("Expect 1 WIP document", 1, wip.size());
        Assert.assertEquals("Expect correct WIP title", c_TestDocumentTitle + " (WIP)", wip.get(0).getTitle());
    }

    @Test
    public void testCanGetSortedWorkInProgressByUser() throws Exception {
        List<WIPDocument> wip = DocumentManager.getInstance().getWorkInProgressByUser(m_TestUser, new QueryParam(0, 0, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING));
        Assert.assertEquals("Expect 1 WIP document", 1, wip.size());
        Assert.assertEquals("Expect correct WIP title", c_TestDocumentTitle + " (WIP)", wip.get(0).getTitle());
    }

    @Test
    public void testGetPublishedByTag() throws Exception {
        Tag tag = DocumentManager.getInstance().getTag(c_TestTagTitle + " (USED)");
        List<PublishedDocument> published = DocumentManager.getInstance().getPublishedByTag(tag, QueryParam.UNSORTED);
        Assert.assertEquals("Expect 2 published documents", 2, published.size());
        List<String> titles = Arrays.asList(new String[]{ published.get(0).getTitle(), published.get(1).getTitle() });

        Assert.assertEquals("Expect a published title", true, titles.contains(c_TestDocumentTitle + " (PUBLISHED)"));
        Assert.assertEquals("Expect a featured title", true, titles.contains(c_TestDocumentTitle + " (FEATURED)"));

    }

    @Test
    public void testGetFeatured() throws Exception {
        List<PublishedDocument> published = DocumentManager.getInstance().getFeatured(QueryParam.UNSORTED);
        Assert.assertEquals("Expect 2 featured documents", 2, published.size());
        Assert.assertEquals("Expect correct featured title", c_TestDocumentTitle + " (FEATURED)", published.get(0).getTitle());
    }
    
    @Test
    public void testSearchPublished() throws Exception {
        List<PublishedDocument> published = DocumentManager.getInstance().getPublishedByTitle("FEATURED", QueryParam.UNSORTED);
        Assert.assertEquals("Expect 2 documents with FEATURED in title", 2, published.size());
        Assert.assertEquals("Expect correct featured title", c_TestDocumentTitle + " (FEATURED)", published.get(0).getTitle());
    }

    @Test
    public void testGetFeaturedByTag() throws Exception {
        Tag tag = DocumentManager.getInstance().getTag(c_TestTagTitle + " (USED)");
        List<PublishedDocument> published = DocumentManager.getInstance().getFeaturedByTag(tag, QueryParam.UNSORTED);
        Assert.assertEquals("Expect 1 featured document", 1, published.size());
        Assert.assertEquals("Expect correct featured title", c_TestDocumentTitle + " (FEATURED)", published.get(0).getTitle());
    }

    @Test
    public void testGetPublished() throws Exception {
        List<PublishedDocument> published = DocumentManager.getInstance().getPublished(QueryParam.UNSORTED);
        Assert.assertEquals("Expect 4 published documents", 4, published.size());
    }

    @Test
    public void testDeleteAllDocumentsByUser() throws Exception {
        DocumentManager.getInstance().deleteAllDocumentsByUser(m_TestUser2);
        Assert.assertEquals("Expect no documents for user after deletion", 0, DocumentManager.getInstance().getPublishedByUser(m_TestUser2, QueryParam.UNSORTED).size());
    }

    @Test
    public void testGetRevisions() throws Exception {
        List<Revision> revisions = DocumentManager.getInstance().getRevisions(m_WIP_Document, QueryParam.UNSORTED);
        Assert.assertEquals("Expect 3 revisions", 3, revisions.size());
    }

    @Test
    public void testDeleteDocument() throws Exception {
        DocumentManager.getInstance().deleteDocument(m_WIP_Document);
        List<WIPDocument> wip = DocumentManager.getInstance().getWorkInProgressByUser(m_TestUser, QueryParam.UNSORTED);
        Assert.assertEquals("Expect no WIP documents", 0, wip.size());
    }

    @Test
    public void testAddAndGetRevision() throws Exception {
        // We know that createRevision calls our addRevision, since Revision's constructor is hidden.
        Revision revision = Revision.createRevision(m_WIP_Document, new Date(), c_TestRevisionContent + " (DYNAMIC)");
        List<Revision> revisions = DocumentManager.getInstance().getRevisions(m_WIP_Document, QueryParam.UNSORTED);
        Assert.assertEquals("Expect 4th added revision", 4, revisions.size());
        Assert.assertEquals("Expect correct revision content", c_TestRevisionContent + " (DYNAMIC)", DocumentManager.getInstance().getRevisionContent(revisions.get(3)));
    }

    @Test
    public void testDeleteRevision() throws Exception {
        Revision revision = Revision.createRevision(m_WIP_Document, new Date(), c_TestRevisionContent + " (DYNAMIC)");
        DocumentManager.getInstance().deleteRevision(Arrays.asList(new Revision[] { revision }));
        List<Revision> revisions = DocumentManager.getInstance().getRevisions(m_WIP_Document, QueryParam.UNSORTED);
        Assert.assertEquals("Expect unchanged number of revisions", 3, revisions.size());
    }

    @Test
    public void testCreateDocument() throws Exception {
        Document newDocument = new WIPDocument(-1, DocumentType.PLAINTEXT, m_TestUser, c_TestDocumentTitle + " (WIP2)", 0);
        DocumentManager.getInstance().createDocument(newDocument);
        Assert.assertNotSame("ID should be set", -1, newDocument.getID());
        List<WIPDocument> wip = DocumentManager.getInstance().getWorkInProgressByUser(m_TestUser, QueryParam.UNSORTED);
        Assert.assertEquals("Expect new WIP document (total: 2)", 2, wip.size());
    }

    @Test
     public void testUpdateDocument() throws Exception {
        m_WIP_Document.setTitle(c_TestDocumentTitle + " (DYNAMIC)");
        DocumentManager.getInstance().updateDocument(m_WIP_Document);

        List<WIPDocument> retrieved = DocumentManager.getInstance().getWorkInProgressByUser(m_TestUser, QueryParam.UNSORTED);
        Assert.assertEquals("Expected retrieved document with amended title", c_TestDocumentTitle + " (DYNAMIC)", retrieved.get(0).getTitle());
    }

    @Test
    public void testGetParentDoc() throws Exception {
        Document parentDoc = DocumentManager.getInstance().getParentDocument(m_WIP_Document);
        Assert.assertEquals("Parent should be as defined in test", m_Published_Document.getID(), parentDoc.getID());
    }

    @Test
    public void testSetParentDoc() throws Exception {
        Document published = DocumentManager.getInstance().getDocumentById(m_Published_Document.getID());
        DocumentManager.getInstance().setParentDocument(published, m_WIP_Document);
        Document parentDoc = DocumentManager.getInstance().getParentDocument(published);
        Assert.assertEquals("Parent should be as defined in test", m_WIP_Document.getID(), parentDoc.getID());
    }

    @Test
    public void testGetTags() throws Exception {
        Set<Tag> tags = DocumentManager.getInstance().getTags();
        Assert.assertEquals("Expecting 3 tags", 3, tags.size());
    }

    @Test
    public void testGetTagsNotEmpty() throws Exception {
        Set<Tag> tagSet = DocumentManager.getInstance().getTagsNotEmpty();
        Tag[] tags = tagSet.toArray(new Tag[tagSet.size()]);
        Assert.assertEquals("Expecting 1 tag", 1, tags.length);
        Assert.assertEquals("Expecting used tag", c_TestTagTitle + " (USED)", tags[0].name);
    }

    @Test
    public void testGetTagsNotBanned() throws Exception {
        Set<Tag> tagSet = DocumentManager.getInstance().getTagsNotBanned();
        Tag[] tags = tagSet.toArray(new Tag[tagSet.size()]);
        Assert.assertEquals("Expecting 2 tags", 2, tags.length);
    }

    @Test
    public void testGetTagsForDocument() throws Exception {
        Set<Tag> tagSet = DocumentManager.getInstance().getTags(m_Published_Document);
        Assert.assertEquals("Expecting 1 tag", 1, tagSet.size());
    }

    @Test
    public void testGetTag() throws Exception {
        Tag tag = DocumentManager.getInstance().getTag(c_TestTagTitle + " (USED)");
        Assert.assertEquals("Expecting used tag", c_TestTagTitle + " (USED)", tag.name);
    }

    @Test
    public void testCreateTag() throws Exception {
        Tag tag = new Tag(-1, c_TestTagTitle + " (DYNAMIC)", false);
        DocumentManager.getInstance().createTag(tag);

        Tag retrieved = DocumentManager.getInstance().getTag(c_TestTagTitle + " (DYNAMIC)");
        Assert.assertEquals("Expecting dynamic tag in database", c_TestTagTitle + " (DYNAMIC)", retrieved.name);
    }

    @Test
    public void testAddTagToDocument() throws Exception {
        DocumentManager.getInstance().addTag(m_WIP_Document, m_BannedTag);

        Set<Tag> tagSet = DocumentManager.getInstance().getTagsNotEmpty();
        Tag[] tags = tagSet.toArray(new Tag[tagSet.size()]);
        Assert.assertEquals("Expecting 2 used tags", 2,  tags.length);

        List<WIPDocument> retrieved = DocumentManager.getInstance().getWorkInProgressByUser(m_TestUser, QueryParam.UNSORTED);
        Assert.assertEquals("Expected retrieved document with tag", 1, retrieved.get(0).getTags().size());
    }

    @Test
    public void testDeleteTagFromDocument() throws Exception {
        DocumentManager.getInstance().deleteTag(m_Published_Document, m_UsedTag);

        List<PublishedDocument> retrieved = DocumentManager.getInstance().getPublishedByTitle(m_Published_Document.getTitle(), QueryParam.UNSORTED);
        Assert.assertEquals("Expected retrieved document with no tags", 0, retrieved.get(0).getTags().size());
    }

    @Test
    public void testUpdateTag() throws Exception {
        Tag banned = new Tag(m_BannedTag.getID(), m_BannedTag.name, false);
        DocumentManager.getInstance().updateTag(banned);

        Tag retrieved = DocumentManager.getInstance().getTag(c_TestTagTitle + " (BANNED)");
        Assert.assertEquals("Expecting unbanned tag in database", false, retrieved.getBanned());
    }

    @Test
    public void testDeleteTag() throws Exception {
        DocumentManager.getInstance().deleteTag(m_UsedTag);

        Set<Tag> tags = DocumentManager.getInstance().getTags();
        Assert.assertEquals("Expecting 2 tags", 2, tags.size());

        Set<Tag> tagSet = DocumentManager.getInstance().getTagsNotEmpty();
        Tag[] nonEmptyTags = tagSet.toArray(new Tag[tagSet.size()]);
        Assert.assertEquals("Expecting no used tags", 0, nonEmptyTags.length);

        PublishedDocument featured = DocumentManager.getInstance().getFeatured(QueryParam.UNSORTED).get(0);
        Assert.assertEquals("Featured document should no longer have tag", 0, featured.getTags().size());
    }

    @Test
    public void testDeleteTagReferences() throws Exception {
        DocumentManager.getInstance().deleteTagReferences(m_UsedTag);

        Set<Tag> tags = DocumentManager.getInstance().getTags();
        Assert.assertEquals("Expecting 3 tags", 3, tags.size());

        Set<Tag> tagSet = DocumentManager.getInstance().getTagsNotEmpty();
        Tag[] nonEmptyTags = tagSet.toArray(new Tag[tagSet.size()]);
        Assert.assertEquals("Expecting no used tags", 0, nonEmptyTags.length);

        PublishedDocument featured = DocumentManager.getInstance().getFeatured(QueryParam.UNSORTED).get(0);
        Assert.assertEquals("Featured document should no longer have tag", 0, featured.getTags().size());
    }

    @Test
    public void testAddVote() throws Exception {
        DocumentManager.getInstance().addVote(m_TestUser, DocumentManager.getInstance().getFeatured(QueryParam.UNSORTED).get(0));

        PublishedDocument featured = DocumentManager.getInstance().getFeatured(QueryParam.UNSORTED).get(0);
        Assert.assertEquals("Featured document has 1 vote", 1, featured.getVotes());
    }

    @Test(expected=SQLException.class)
    public void testAddTwoVotes() throws Exception {
        DocumentManager.getInstance().addVote(m_TestUser, DocumentManager.getInstance().getFeatured(QueryParam.UNSORTED).get(0));
        DocumentManager.getInstance().addVote(m_TestUser, DocumentManager.getInstance().getFeatured(QueryParam.UNSORTED).get(0));
    }
}