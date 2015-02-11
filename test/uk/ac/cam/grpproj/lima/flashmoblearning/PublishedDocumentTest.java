package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.QueryParam;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

public class PublishedDocumentTest extends DocumentTestBase {

	@Override
	PublishedDocument create(long id, DocumentType docType, User owner, String title,
			long time) {
		return new PublishedDocument(id, docType, owner, title, time, 0, 0);
	}

	@Override
	List<? extends Document> getByTitle(String title)
			throws NotInitializedException, SQLException, NoSuchObjectException {
		return DocumentManager.getInstance().getPublishedByExactTitle(title, QueryParam.UNSORTED);
	}

	@Test
	public void testFeatured() throws NotInitializedException, SQLException, IDAlreadySetException, NoSuchObjectException {
    	PublishedDocument doc = create(-1, docType, owner, titleSimple, 
    			System.currentTimeMillis());
    	DocumentManager.getInstance().createDocument(doc);
    	Assert.assertEquals(0, DocumentManager.getInstance().getFeatured(QueryParam.UNSORTED).size());
    	doc.setFeatured(true);
    	List<PublishedDocument> docsFeatured = 
    			DocumentManager.getInstance().getFeatured(QueryParam.UNSORTED);
    	Assert.assertEquals(1, docsFeatured.size());
    	Assert.assertEquals(doc, docsFeatured.get(0));
    	doc.setFeatured(false);
    	Assert.assertEquals(0, DocumentManager.getInstance().getFeatured(QueryParam.UNSORTED).size());
	}
}
