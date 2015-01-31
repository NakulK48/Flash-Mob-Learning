package uk.ac.cam.grpproj.lima.flashmoblearning.database.memory;

import java.util.List;
import java.util.Set;

import uk.ac.cam.grpproj.lima.flashmoblearning.Document;
import uk.ac.cam.grpproj.lima.flashmoblearning.PublishedDocument;
import uk.ac.cam.grpproj.lima.flashmoblearning.Revision;
import uk.ac.cam.grpproj.lima.flashmoblearning.Tag;
import uk.ac.cam.grpproj.lima.flashmoblearning.User;
import uk.ac.cam.grpproj.lima.flashmoblearning.WIPDocument;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.DocumentManager;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.QueryParam;

public class MemoryDocumentManager extends DocumentManager {

	@Override
	public List<PublishedDocument> getPublishedByUser(User u, QueryParam param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WIPDocument> getWorkInProgressByUser(User u, QueryParam param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublishedDocument> getPublishedByTag(Tag t, QueryParam param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublishedDocument> getFeatured(QueryParam param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublishedDocument> getFeaturedByTag(Tag t, QueryParam param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PublishedDocument> getPublished(QueryParam param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(User u, QueryParam param) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Revision> getRevisions(WIPDocument d, QueryParam param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Revision getFinalRevision(PublishedDocument d, QueryParam param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDocument(WIPDocument d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteDocument(PublishedDocument d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRevision(WIPDocument d, Revision r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createDocument(WIPDocument d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createDocument(PublishedDocument d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDocument(Document d) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Tag> getTags() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tag getTag(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateTags(Document d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTag(Tag tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteTag(Tag tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addVote(User u, PublishedDocument d) {
		// TODO Auto-generated method stub

	}

}
