package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** A document that has been published for everyone to see. Note that the content of the document
 * cannot be modified once it has been published, but the tags and title can, by the owner. */
public class PublishedDocument extends Document{
	
	/** Has the "featured" flag been set by the administrator? */
	private boolean isFeatured;
	private int score;
	private int votes;

	public int getVotes() {
		return votes;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	/** Only called by WIPDocument.publish() */
	PublishedDocument(WIPDocument original) {
		super(original.docType, original.owner, original.parentDoc, original.getTitle(), System.currentTimeMillis());
		isFeatured = false;
	}
	
	/** Only called by DocumentManager */
	public PublishedDocument(DocumentType docType, User owner, Document parentDoc,
			String title, long time, int votes, int score) {
		super(docType, owner, parentDoc, title, time);
		this.votes = votes;
		this.score = score;
	}
	
	/** Get the one and only revision 
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public Revision getContentRevision() throws NotInitializedException, SQLException, NoSuchObjectException {
		return getLastRevision();
	}

	/** Copy a document so we can edit it 
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public WIPDocument fork(User newOwner) throws NotInitializedException, SQLException, NoSuchObjectException {
		WIPDocument d = new WIPDocument(this, newOwner);
		String content = this.getLastRevision().getContent();
		Revision firstRev = getContentRevision().copy(d);
		DocumentManager.getInstance().createDocument(d);
		DocumentManager.getInstance().addRevision(d, firstRev, content);
		return d;
	}
	
	/** Is this document Featured? */
	public synchronized boolean getFeatured() {
		return isFeatured;
	}
	
	/** Set the Featured flag 
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public void setFeatured(boolean set) throws NotInitializedException, SQLException, NoSuchObjectException {
		synchronized(this) {
			if(set == isFeatured) return;
			isFeatured = set;
		}
		DocumentManager.getInstance().updateDocument(this);
	}
}
