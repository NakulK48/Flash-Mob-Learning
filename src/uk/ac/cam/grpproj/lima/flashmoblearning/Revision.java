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
	/** Creation time. Not included in equals/hashCode because SQL truncates it. */
	public final Date creationTime;
	/** The revision ID (primary key) */
	private final long id;
	
	/** Create a Revision with a given content and store it to the database. */
	public static Revision createRevision(Document doc, Date d, String payload) throws NotInitializedException, SQLException, NoSuchObjectException {
		// Store the revision record AND the content.
		return DocumentManager.getInstance().addRevision(doc, d, payload);
	}
	
	/** To be used by the database code only! */
	public Revision(long id, Date d, Document doc) {
		this.id = id;
		this.document = doc;
		this.creationTime = d;
	}
	
	/** Fetch the content of the revision. This may be large, so is fetched 
	 * on-demand from the database table rather than being loaded with the 
	 * Revision metadata.
	 * @return The content of a revision as a String. TODO OPT We could maybe
	 * use some other representation to speed things up?
	 * @throws SQLException If an error occurred.
	 * @throws NoSuchObjectException If the revision has been deleted from the
	 * database.
	 */
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
		return createRevision(doc, creationTime, fetched);
	}

	/** Get the User who owns the document that this revision belongs to */
	public User getOwner() {
		return document.owner;
	}

	/** Get the Revision ID (an automatically generated unique number for this 
	 * revision used as a primary key). */
	public long getID() { return id; }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ document.hashCode();
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Revision other = (Revision) obj;
		if (id != other.id)
			return false;
		return document.equals(other.document);
	}

}
