package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;
import java.util.List;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.QueryParam;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

public class WIPDocumentTest extends DocumentTestBase {

	@Override
	Document create(long id, DocumentType docType, User owner, String title,
			long time) {
		return new WIPDocument(id, docType, owner, title, time);
	}

	@Override
	List<? extends Document> getByTitle(String title) throws NotInitializedException, SQLException, NoSuchObjectException {
		return DocumentManager.getInstance().getWIPByExactTitle(title, DocumentType.ALL, QueryParam.UNSORTED);
	}

}
