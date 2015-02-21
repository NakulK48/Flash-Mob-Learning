package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.Database;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.LoginManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateEntryException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** Base for tests on WIPDocument's and PublishedDocument's. Tests Document
 * methods that need a concrete, storable instance. */
public abstract class DocumentTestBase {
	
	final DocumentType docType = DocumentType.PLAINTEXT;
	protected User owner;
	final String titleSimple = "Test document";
	final String titleSimple2 = "Different test document";
	
    @org.junit.Before
    public void setUp() throws Exception {
    	TestHelper.databaseInit();
    	owner = LoginManager.getInstance().getUser(Database.DEFAULT_TEACHER_USERNAME);
    }

    @org.junit.After
    public void tearDown() throws Exception {
		TestHelper.databaseCleanTablesAndClose();
    }

	abstract Document create(long id, DocumentType docType, User owner,
			String title, long time);
	
	abstract List<? extends Document> getByTitle(String title) throws NotInitializedException, SQLException, NoSuchObjectException;
	
    @Test
    public void testSetIDPersistent() throws NotInitializedException, SQLException, IDAlreadySetException, NoSuchObjectException {
    	Document doc = create(-1, docType, owner, titleSimple, 
    			System.currentTimeMillis());
    	Assert.assertEquals(doc.getID(), -1);
    	DocumentManager.getInstance().createDocument(doc);
    	long id = doc.getID();
    	Assert.assertNotSame(id, -1);
    	Document doc2 = DocumentManager.getInstance().getDocumentById(id);
    	Assert.assertNotNull(doc2);
    	Assert.assertEquals(doc, doc2);
    	List<? extends Document> doc3 = getByTitle(titleSimple);
    	Assert.assertEquals(1, doc3.size());
    	Assert.assertEquals(doc3.get(0), doc);
    }
    
    @Test
    public void testSetTitlePersistent() throws NotInitializedException, SQLException, IDAlreadySetException, NoSuchObjectException {
    	Document doc = create(-1, docType, owner, titleSimple, 
    			System.currentTimeMillis());
    	Assert.assertEquals(titleSimple, doc.getTitle());
    	DocumentManager.getInstance().createDocument(doc);
    	// Check that it can be fetched by title.
    	List<? extends Document> doc3 = getByTitle(titleSimple);
    	Assert.assertEquals(1, doc3.size());
    	Assert.assertEquals(doc3.get(0), doc);
    	// Change the title.
    	doc.setTitle(titleSimple2);
    	Assert.assertEquals(titleSimple2, doc.getTitle());
    	// Can it be fetched by the old title?
    	Assert.assertEquals(0, getByTitle(titleSimple).size());
    	// Can it be fetched by the new title?
    	doc3 = getByTitle(titleSimple2);
    	Assert.assertEquals(1, doc3.size());
    	Assert.assertEquals(doc3.get(0), doc);
    	Assert.assertEquals(titleSimple2, doc3.get(0).getTitle());
    }

    
    @Test
    public void testSetTitlePersistentEmpty() throws NotInitializedException, SQLException, IDAlreadySetException, NoSuchObjectException {
    	Document doc = create(-1, docType, owner, titleSimple, 
    			System.currentTimeMillis());
    	Assert.assertEquals(titleSimple, doc.getTitle());
    	String titleSimple2 = "";
    	DocumentManager.getInstance().createDocument(doc);
    	// Check that it can be fetched by title.
    	List<? extends Document> doc3 = getByTitle(titleSimple);
    	Assert.assertEquals(1, doc3.size());
    	Assert.assertEquals(doc3.get(0), doc);
    	// Change the title.
    	doc.setTitle(titleSimple2);
    	Assert.assertEquals(titleSimple2, doc.getTitle());
    	// Can it be fetched by the old title?
    	Assert.assertEquals(0, getByTitle(titleSimple).size());
    	// Can it be fetched by the new title?
    	doc3 = getByTitle(titleSimple2);
    	Assert.assertEquals(1, doc3.size());
    	Assert.assertEquals(doc3.get(0), doc);
    	Assert.assertEquals(titleSimple2, doc3.get(0).getTitle());
    }
    
    @Test
    public void testSetParentDocument() throws NotInitializedException, SQLException, IDAlreadySetException, NoSuchObjectException, DuplicateEntryException {
    	Document doc = create(-1, docType, owner, titleSimple, 
    			System.currentTimeMillis());
    	DocumentManager.getInstance().createDocument(doc);
    	Assert.assertNull(DocumentManager.getInstance().getParentDocument(doc));
    	Document doc2 = create(-1, docType, owner, titleSimple2, 
    			System.currentTimeMillis());
    	DocumentManager.getInstance().createDocument(doc2);
    	Assert.assertNull(DocumentManager.getInstance().getParentDocument(doc2));
    	DocumentManager.getInstance().setParentDocument(doc2, doc);
    	Assert.assertNull(DocumentManager.getInstance().getParentDocument(doc));
    	Assert.assertEquals(DocumentManager.getInstance().getParentDocument(doc2), doc);
    }
    
    @Test
    public void testNoResetNullParentDocument() throws NotInitializedException, SQLException, NoSuchObjectException, IDAlreadySetException, DuplicateEntryException {
    	Document doc = create(-1, docType, owner, titleSimple, 
    			System.currentTimeMillis());
    	DocumentManager.getInstance().createDocument(doc);
    	Assert.assertNull(DocumentManager.getInstance().getParentDocument(doc));
    	Document doc2 = create(-1, docType, owner, titleSimple2, 
    			System.currentTimeMillis());
    	DocumentManager.getInstance().createDocument(doc2);
    	Assert.assertNull(DocumentManager.getInstance().getParentDocument(doc2));
    	DocumentManager.getInstance().setParentDocument(doc2, doc);
    	Assert.assertNull(DocumentManager.getInstance().getParentDocument(doc));
    	Assert.assertEquals(DocumentManager.getInstance().getParentDocument(doc2), doc);
    	try {
    		DocumentManager.getInstance().setParentDocument(doc2, null);
    		Assert.fail("Should throw here");
    	} catch (NullPointerException e) {
    		// FIXME this should be an SQL error.
    	} catch (DuplicateEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void testNoResetParentDocument() throws NotInitializedException, SQLException, NoSuchObjectException, IDAlreadySetException, DuplicateEntryException {
    	Document doc = create(-1, docType, owner, titleSimple, 
    			System.currentTimeMillis());
    	DocumentManager.getInstance().createDocument(doc);
    	Assert.assertNull(DocumentManager.getInstance().getParentDocument(doc));
    	Document doc2 = create(-1, docType, owner, titleSimple2, 
    			System.currentTimeMillis());
    	DocumentManager.getInstance().createDocument(doc2);
    	Assert.assertNull(DocumentManager.getInstance().getParentDocument(doc2));
    	DocumentManager.getInstance().setParentDocument(doc2, doc);
    	Assert.assertNull(DocumentManager.getInstance().getParentDocument(doc));
    	Assert.assertEquals(DocumentManager.getInstance().getParentDocument(doc2), doc);
    	Document doc3 = create(-1, docType, owner, titleSimple2, 
    			System.currentTimeMillis());
    	DocumentManager.getInstance().createDocument(doc3);
    	try {
    		DocumentManager.getInstance().setParentDocument(doc2, doc3);
    		Assert.fail("Should throw here");
    	} catch (SQLException e) {
    		// OK.
    	} catch (DuplicateEntryException e) {
    		// OK.
		}
    }
    
    @Test
    public void testDeleteParentDocument() throws NotInitializedException, SQLException, IDAlreadySetException, NoSuchObjectException, DuplicateEntryException {
    	Document doc = create(-1, docType, owner, titleSimple, 
    			System.currentTimeMillis());
    	DocumentManager.getInstance().createDocument(doc);
    	Assert.assertNull(DocumentManager.getInstance().getParentDocument(doc));
    	Document doc2 = create(-1, docType, owner, titleSimple2, 
    			System.currentTimeMillis());
    	DocumentManager.getInstance().createDocument(doc2);
    	Assert.assertNull(DocumentManager.getInstance().getParentDocument(doc2));
    	DocumentManager.getInstance().setParentDocument(doc2, doc);
    	Assert.assertNull(DocumentManager.getInstance().getParentDocument(doc));
    	Assert.assertEquals(DocumentManager.getInstance().getParentDocument(doc2), doc);
    	DocumentManager.getInstance().deleteDocument(doc);
    	Assert.assertNull(DocumentManager.getInstance().getParentDocument(doc2));
    }
}
