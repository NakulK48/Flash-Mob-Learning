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
	/** Looked up by the database on creation. Not updated in updateDocument(). */
	private int votes;
	/** Will be set by database */
	private double score;
	
	/** Get the total number of votes ever cast for this document, including
	 * those cast by users who have now been deleted. */
	public int getVotes() {
		return votes;
	}

	/** Set the number of votes cast. */
	public void setVotes(int votes) {
		this.votes = votes;
	}
	
	/** Get the document's age-adjusted popularity score. */
	public double getScore() { return score; }

	/** Set the document's age-adjusted popularity score. */
	public void setScore(double score) {
		this.score = score;
	}

	/** Only called by WIPDocument.publish() */
	PublishedDocument(WIPDocument original) {
		super(-1, original.docType, original.owner, original.getTitle(), System.currentTimeMillis());
		isFeatured = false;
		// a new document cannot have an upvote or score.
		votes = 0;
		score = 0;
	}
	

	/** Only called by DocumentManager.
	 * @param votes The number of votes for the document when it was fetched from the database.
	 * @param score The score stored in the database for the document. Might be updated later. */
	public PublishedDocument(long id, DocumentType docType, User owner,
			String title, long time, int votes, double score) {
		super(id, docType, owner, title, time);
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
		copyTo(d, this);
		return d;
	}
	
	/** Is this document Featured? */
	public boolean getFeatured() {
		return isFeatured;
	}
	
	/** Set the Featured flag 
	 * @throws NoSuchObjectException 
	 * @throws SQLException 
	 * @throws NotInitializedException */
	public void setFeatured(boolean set) throws NotInitializedException, SQLException, NoSuchObjectException {
		if(set == isFeatured) return;
		isFeatured = set;
		DocumentManager.getInstance().updateDocument(this);
	}
	
	public static double calculateScore(long age, int votes)
	{
		age /= 3600000; //in hours
		return (votes * Math.exp(age * age / 50000));
	}
	
	/** Calculate the document's age-adjusted popularity score. This uses an 
	 * algorithm similar to that used by Reddit's "Hot" ranking. */
	public double calculateScore()
	{
		double age = (System.currentTimeMillis() - creationTime)/3600000;
		return (votes * Math.exp(-1 * age * age / 50000));
	}
}
