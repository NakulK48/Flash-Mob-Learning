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
	private int votes;
	
	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	/** Only called by WIPDocument.publish() */
	PublishedDocument(WIPDocument original) {
		super(original.docType, original.owner, original.parentDoc, original.getTitle(), System.currentTimeMillis());
		isFeatured = false;
	}
	

	/** Only called by DocumentManager.
	 * @param votes The number of votes for the document when it was fetched from the database.
	 * @param score The score stored in the database for the document. Might be updated later. */
	public PublishedDocument(long id, DocumentType docType, User owner, Document parentDoc,
			String title, long time, int votes) {
		super(docType, owner, parentDoc, title, time);
		this.votes = votes;
		this.id = id;
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
	public WIPDocument fork(User newOwner) throws NotInitializedException, SQLException, NoSuchObjectException, IDAlreadySetException {
		WIPDocument d = new WIPDocument(this, newOwner);
		String content = this.getLastRevision().getContent();
		DocumentManager.getInstance().createDocument(d);
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
