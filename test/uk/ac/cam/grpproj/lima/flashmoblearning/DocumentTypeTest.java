package uk.ac.cam.grpproj.lima.flashmoblearning;

import org.junit.Test;

import junit.framework.Assert;

public class DocumentTypeTest {
	
	@Test
	public void testValid() {
		for(DocumentType t : DocumentType.values()) {
			Assert.assertEquals(t, DocumentType.getValue(t.id));
		}
	}
	
	@Test
	public void testInvalid() {
		Assert.assertEquals(DocumentType.PLAINTEXT, DocumentType.getValue(-2));
	}

}
