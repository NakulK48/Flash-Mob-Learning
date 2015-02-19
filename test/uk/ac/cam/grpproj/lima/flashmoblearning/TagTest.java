package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.Database;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.LoginManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.QueryParam;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateNameException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** Test Tag and Tag-related methods on Document */
public class TagTest {

	@org.junit.Before
	public void setUp() throws Exception {
		TestHelper.databaseInit();
    	owner = LoginManager.getInstance().getUser(Database.DEFAULT_TEACHER_USERNAME);
	}

	@org.junit.After
	public void tearDown() throws Exception {
		TestHelper.databaseCleanTablesAndClose();
	}

	final String tagName1 = "Test";
	final String tagName2 = "Testing";
	final DocumentType docType = DocumentType.PLAINTEXT;
	private User owner;
	final String titleSimple = "Test document";
	final String payloadSimple = "Test payload";

	@Test
	public void testCreateAndFetch() throws NotInitializedException, SQLException, NoSuchObjectException, DuplicateNameException, IDAlreadySetException {
		Tag t = Tag.create(tagName1);
		Assert.assertNotSame(-1, t.getID());
		WIPDocument doc = new WIPDocument(-1, docType, owner, titleSimple, 
    			System.currentTimeMillis());
		DocumentManager.getInstance().createDocument(doc);
		doc.addRevision(new Date(), payloadSimple);
		doc.addTag(t);
		Set<Tag> tags = doc.getTags();
		Assert.assertEquals(1, tags.size());
		Assert.assertTrue(tags.contains(t));
		PublishedDocument published = doc.publish();
		tags = doc.getTags();
		Assert.assertEquals(1, tags.size());
		Assert.assertTrue(tags.contains(t));
		List<PublishedDocument> docs = 
				DocumentManager.getInstance().getPublishedByTag(t, DocumentType.ALL, QueryParam.UNSORTED);
		Assert.assertEquals(1, docs.size());
		Assert.assertEquals(published, docs.get(0));
	}

	@Test
	public void testBanTag() throws NotInitializedException, SQLException, NoSuchObjectException, DuplicateNameException, IDAlreadySetException {
		Tag t = Tag.create(tagName1);
		Assert.assertNotSame(-1, t.getID());
		WIPDocument doc = new WIPDocument(-1, docType, owner, titleSimple, 
    			System.currentTimeMillis());
		DocumentManager.getInstance().createDocument(doc);
		doc.addRevision(new Date(), payloadSimple);
		doc.addTag(t);
		Set<Tag> tags = doc.getTags();
		Assert.assertEquals(1, tags.size());
		Assert.assertTrue(tags.contains(t));
		PublishedDocument published = doc.publish();
		tags = doc.getTags();
		Assert.assertEquals(1, tags.size());
		Assert.assertTrue(tags.contains(t));
		List<PublishedDocument> docs = 
				DocumentManager.getInstance().getPublishedByTag(t, DocumentType.ALL, QueryParam.UNSORTED);
		Assert.assertEquals(1, docs.size());
		Assert.assertEquals(published, docs.get(0));
		t.setBanned(true);
		Assert.assertTrue(t.getBanned());
		Assert.assertEquals(t, DocumentManager.getInstance().getTag(tagName1));
		Assert.assertTrue(DocumentManager.getInstance().getTag(tagName1).getBanned());
		// Deletes references.
		docs = 
				DocumentManager.getInstance().getPublishedByTag(t, DocumentType.ALL, QueryParam.UNSORTED);
		Assert.assertEquals(0, docs.size());
		published = (PublishedDocument) DocumentManager.getInstance().getDocumentById(published.getID());
		Assert.assertEquals(0, published.getTags().size());
		doc = (WIPDocument) DocumentManager.getInstance().getDocumentById(doc.getID());
		Assert.assertEquals(0, doc.getTags().size());
	}

	@Test
	public void testDeleteTag() throws NotInitializedException, SQLException, NoSuchObjectException, DuplicateNameException, IDAlreadySetException {
		Tag t = Tag.create(tagName1);
		Assert.assertNotSame(-1, t.getID());
		WIPDocument doc = new WIPDocument(-1, docType, owner, titleSimple, 
    			System.currentTimeMillis());
		DocumentManager.getInstance().createDocument(doc);
		doc.addRevision(new Date(), payloadSimple);
		doc.addTag(t);
		Set<Tag> tags = doc.getTags();
		Assert.assertEquals(1, tags.size());
		Assert.assertTrue(tags.contains(t));
		PublishedDocument published = doc.publish();
		tags = doc.getTags();
		Assert.assertEquals(1, tags.size());
		Assert.assertTrue(tags.contains(t));
		List<PublishedDocument> docs = 
				DocumentManager.getInstance().getPublishedByTag(t, DocumentType.ALL, QueryParam.UNSORTED);
		Assert.assertEquals(1, docs.size());
		Assert.assertEquals(published, docs.get(0));
		DocumentManager.getInstance().deleteTag(t);
		try {
			DocumentManager.getInstance().getTag(tagName1);
			Assert.fail();
		} catch (NoSuchObjectException e) {
			// OK.
		}
		// Deletes references.
		docs = 
				DocumentManager.getInstance().getPublishedByTag(t, DocumentType.ALL, QueryParam.UNSORTED);
		Assert.assertEquals(0, docs.size());
		published = (PublishedDocument) DocumentManager.getInstance().getDocumentById(published.getID());
		Assert.assertEquals(0, published.getTags().size());
		doc = (WIPDocument) DocumentManager.getInstance().getDocumentById(doc.getID());
		Assert.assertEquals(0, doc.getTags().size());
	}
	
	@Test
	public void testDeleteTagFromDocument() throws NotInitializedException, SQLException, NoSuchObjectException, DuplicateNameException, IDAlreadySetException {
		Tag t = Tag.create(tagName1);
		Assert.assertNotSame(-1, t.getID());
		WIPDocument doc = new WIPDocument(-1, docType, owner, titleSimple, 
    			System.currentTimeMillis());
		DocumentManager.getInstance().createDocument(doc);
		doc.addRevision(new Date(), payloadSimple);
		doc.addTag(t);
		Set<Tag> tags = doc.getTags();
		Assert.assertEquals(1, tags.size());
		Assert.assertTrue(tags.contains(t));
		// Add another tag.
		Tag t2 = Tag.create(tagName2);
		Assert.assertNotSame(-1, t.getID());
		doc.addTag(t2);
		tags = doc.getTags();
		Assert.assertEquals(2, tags.size());
		Assert.assertTrue(tags.contains(t));
		Assert.assertTrue(tags.contains(t2));
		// Delete first tag.
		doc.deleteTag(t);
		tags = doc.getTags();
		Assert.assertEquals(1, tags.size());
		Assert.assertFalse(tags.contains(t));
		Assert.assertTrue(tags.contains(t2));
	}
}
