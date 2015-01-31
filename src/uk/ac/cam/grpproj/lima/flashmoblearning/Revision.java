package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.lang.ref.SoftReference;
import java.sql.SQLException;
import java.util.Date;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** Represents a single version of a document. The actual content is fetched lazily. */
public class Revision {
	
	/** This is a revision of a specific document. It will be deleted when the 
	 * document is deleted. This makes housekeeping considerably easier. */
	public final Document document;
	/** Owned by a specific user */
	public final User owner;
	/** Creation time */
	public final Date creationTime;
	/** Content. May be relatively large, but no more than a few pages. We only 
	 * fetch the content when we need it. Immutable in the database. */
	private SoftReference<String> content;
	
	/** Create a Revision with a given content and store it to the database. */
	public static Revision createRevision(User u, Date d, Document doc, String payload) throws NotInitializedException, SQLException, NoSuchObjectException {
		Revision r = new Revision(u, d, doc, payload);
		// Store the revision record AND the content.
		// IMPORTANT: The payload is still reachable at this point!
		DocumentManager.getInstance().addRevision(doc, r);
		return r;
	}
	
	/** To be used carefully! */
	private Revision(User u, Date d, Document doc, String initialPayload) {
		document = doc;
		owner = u;
		creationTime = d;
		content = new SoftReference<String>(initialPayload);
	}
	
	public String getContent() throws SQLException, NoSuchObjectException {
		synchronized(this) {
			if(content != null) {
				String s = content.get();
				if(s != null) return s;
				content = null;
			}
		}
		String fetched = DocumentManager.getInstance().getRevisionContent(this);
		synchronized(this) {
			if(content != null) {
				String s = content.get();
				if(s != null) return s;
			}
			content = new SoftReference<String>(fetched);
			return fetched;
		}
	}

	/** Create a new copy of this revision with the same content but a different owner.
	 * Will be stored to the database.
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public Revision copy(Document doc) throws NotInitializedException, SQLException, NoSuchObjectException {
		String fetched = getContent();
		return createRevision(doc.owner, creationTime, doc, fetched);
	}

}
