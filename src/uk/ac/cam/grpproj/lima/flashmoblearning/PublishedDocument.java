package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.sql.SQLException;
import java.util.Date;

import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NotInitializedException;

/** A document that has been published for everyone to see. Note that the content of the document
 * cannot be modified once it has been published, but the tags and title can, by the owner. */
public class PublishedDocument extends Document{
	
	/** Has the "featured" flag been set by the administrator? */
	private boolean isFeatured;
	/** Looked up by the database on creation. Not updated in updateDocument(). */
	private int votes;
	/** FIXME will be cached by the database and passed in, updated on a vote 
	 * being cast and periodically. For now it's computed in the constructor. */
	private double score;
	
	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
	
	public double getScore() {
		return score;
	}

	/** Only called by WIPDocument.publish() */
	PublishedDocument(WIPDocument original) {
		super(-1, original.docType, original.owner, original.getTitle(), System.currentTimeMillis());
		isFeatured = false;
		// FIXME
		score = calculateScore();
	}
	

	/** Only called by DocumentManager.
	 * @param votes The number of votes for the document when it was fetched from the database.
	 * @param score The score stored in the database for the document. Might be updated later. */
	public PublishedDocument(long id, DocumentType docType, User owner,
			String title, long time, int votes) {
		super(id, docType, owner, title, time);
		this.votes = votes;
		// FIXME
		score = calculateScore();
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
		try {
			DocumentManager.getInstance().createDocument(d);
			DocumentManager.getInstance().setParentDoc(d, this);
		} catch (IDAlreadySetException e) {
			throw new IllegalStateException("ID already set but just created?!");
		}
		Revision.createRevision(d, new Date(), content);
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
	
	public static double calculateScore(long age, int votes)
	{
		age /= 3600000;
		return (votes * Math.exp(-8 * age * age));
	}
	
	/** Calculate the document's score */
	public double calculateScore()
	{
		double age = (System.currentTimeMillis() - creationTime)/3600000;
		return (votes * Math.exp(-8 * age * age));
	}
}
