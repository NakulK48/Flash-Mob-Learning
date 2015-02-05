package uk.ac.cam.grpproj.lima.flashmoblearning.database;

public class QueryParam {

	// A limit of 0 or less implies no limit.
	public final int limit;
	public final int offset;
	public final SortField sortField;
	public final SortOrder sortOrder;
	public static QueryParam UNSORTED = new QueryParam(0);

	/**
	 * @param limit Limits the number of rows to return. 0 or less means no limit.
	 */
	public QueryParam(int limit) {
		this.limit = limit;
		this.offset = 0;
		this.sortField = SortField.NONE;
		this.sortOrder = SortOrder.ASCENDING;
	}

	/**
	 * @param limit Limits the number of rows to return. 0 or less means no limit.
	 * @param offset Retrieves records from offset to offset + limit.
	 */
	public QueryParam(int limit, int offset) {
		this.limit = limit;
		this.offset = offset;
		this.sortField = SortField.NONE;
		this.sortOrder = SortOrder.ASCENDING;
	}


	/**
	 * @param limit Limits the number of rows to return. 0 or less means no limit.
	 * @param sortField The field to sort by, TIME for all documents and POPULARITY for published documents.
	 * @param sortOrder Ascending or Descending, ignored if sort is irrelevant.
	 */
	public QueryParam(int limit, int offset, SortField sortField, SortOrder sortOrder) {
		this.limit = limit;
		this.offset = offset;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	public String updateQuery(String sql) {
		if(sortField == SortField.TIME) {
			sql += " ORDER BY update_time " + (sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
		} else if(sortField == SortField.VOTES) {
			sql += " ORDER BY vote_count " + (sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
		}
		if(limit > 0) sql += " LIMIT " + limit;
		if(offset > 0) sql += " OFFSET " + offset;
		return sql;
	}

	public static enum SortField {
		
		// No sorting, defaults to sort by insertion time.
		NONE,	
		// Sort by creation time - limited to Documents.
		TIME,	
		// Sorts by popularity - limited to PublishedDocuments.
		POPULARITY,
		// Sorts by votes - limited to PublishedDocuments.
		VOTES
		
	}

	public static enum SortOrder {

		ASCENDING,
		DESCENDING
		
	}


}

