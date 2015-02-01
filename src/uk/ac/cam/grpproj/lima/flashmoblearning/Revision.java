package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;
import java.util.Date;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** Represents a single version of a document. The actual content is fetched on demand. */
public class Revision {
	
	/** This is a revision of a specific document. It will be deleted when the 
	 * document is deleted. This makes housekeeping considerably easier. */
	public final Document document;
	/** Creation time */
	public final Date creationTime;
	private final long id;
	
	/** Create a Revision with a given content and store it to the database. */
	public static Revision createRevision(Date d, Document doc, String payload) throws NotInitializedException, SQLException, NoSuchObjectException {
		// Store the revision record AND the content.
		return DocumentManager.getInstance().addRevision(doc, payload);
	}
	
	/** To be used carefully! */
	public Revision(long id, Date d, Document doc) {
		this.id = id;
		this.document = doc;
		this.creationTime = d;
	}
	
	public String getContent() throws SQLException, NoSuchObjectException {
		return DocumentManager.getInstance().getRevisionContent(this);
	}

	/** Create a new copy of this revision with the same content but a different owner.
	 * Will be stored to the database.
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public Revision copy(Document doc) throws NotInitializedException, SQLException, NoSuchObjectException {
		String fetched = getContent();
		return createRevision(creationTime, doc, fetched);
	}

	public User getOwner() {
		return document.owner;
	}

	public long getID() { return id; }

}
