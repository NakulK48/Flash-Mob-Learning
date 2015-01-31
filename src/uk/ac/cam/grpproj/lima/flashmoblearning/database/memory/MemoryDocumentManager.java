package uk.ac.cam.grpproj.lima.flashmoblearning.database.memory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.DuplicateNameException;
import uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.NoSuchObjectException;

public class MemoryDocumentManager extends DocumentManager {
	
	private final HashMap<User, Set<PublishedDocument>> publishedByUser = new HashMap<User, Set<PublishedDocument>>();
	private final HashMap<User, Set<WIPDocument>> wipByUser = new HashMap<User, Set<WIPDocument>>();
	private final HashMap<Tag, Set<PublishedDocument>> publishedByTag = new HashMap<Tag, Set<PublishedDocument>>();
	private final HashSet<PublishedDocument> featured = new HashSet<PublishedDocument>();
	private final HashSet<PublishedDocument> published = new HashSet<PublishedDocument>();
	private final HashMap<PublishedDocument,Revision> publishedRevisions = new HashMap<PublishedDocument,Revision>();
	private final HashMap<WIPDocument,LinkedList<Revision>> wipRevisions = new HashMap<WIPDocument,LinkedList<Revision>>();
	private final HashMap<PublishedDocument, Long> likes = new HashMap<PublishedDocument, Long>();
	
	@Override
	public synchronized List<PublishedDocument> getPublishedByUser(User u, QueryParam param) {
		return sortDocs(publishedByUser.get(u), param);
	}

	private <T extends Document> List<T> sortDocs(Set<T> set, final QueryParam param) {
		if(set == null) return null;
		ArrayList<T> data = new ArrayList<T>(set);
		if(param.sortField != QueryParam.SortField.NONE) {
			Collections.sort(data, new Comparator<T>() {

				@Override
				public int compare(T arg0, T arg1) {
					int ret;
					switch(param.sortField) {
					case TIME:
						if(arg0.creationTime > arg1.creationTime)
							ret = 1;
						else if(arg0.creationTime < arg1.creationTime)
							ret = -1;
						else ret = 0;
						break;
					case POPULARITY:
						int pop0 = getPopularity(arg0);
						int pop1 = getPopularity(arg1);
						if(pop0 > pop1) ret=1;
						else if(pop0 < pop1) ret=-1;
						else ret=0;
						break;
					default:
					throw new UnsupportedOperationException();
					}
					if(param.sortOrder == QueryParam.SortOrder.DESCENDING) ret = -ret;
					return ret;
				}
			});
		}
		if(param.limit > data.size()) {
			data = new ArrayList<T> (data.subList(0, param.limit));
		}
		return data;
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

	@Override
	public void deleteAllDocumentsByUser(User u, QueryParam param)
			throws SQLException, NoSuchObjectException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LinkedList<Revision> getRevisions(Document d, QueryParam param)
			throws SQLException, NoSuchObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDocument(Document d) throws SQLException,
			NoSuchObjectException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRevision(Document d, Revision r) throws SQLException,
			NoSuchObjectException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRevision(List<Revision> r) throws SQLException,
			NoSuchObjectException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDocument(Document d) throws SQLException,
			NoSuchObjectException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Tag> getTagsNotEmpty() throws SQLException,
			NoSuchObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Tag> getTagsNotBanned() throws SQLException,
			NoSuchObjectException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tag createTag(Tag tag) throws SQLException, NoSuchObjectException,
			DuplicateNameException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteTagReferences(Tag tag) throws SQLException,
			NoSuchObjectException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getRevisionContent(Revision revision) throws SQLException,
			NoSuchObjectException {
		// TODO Auto-generated method stub
		return null;
	}

}
