package uk.ac.cam.grpproj.lima.flashmoblearning.database;

/**
 * The QueryParam class provides the framework for adding parameters to any database
 * query, including result limits, offsets, sort field and order.
 */
public class QueryParam {

	// A limit of 0 or less implies no limit.
	public final int limit;
	public final int offset;
	public final SortField sortField;
	public final SortOrder sortOrder;
	public static QueryParam UNSORTED = new QueryParam(0);

	/**
	 * Constructs a new query parameter with limit.
	 * @param limit limits the number of rows to return. 0 or less means no limit.
	 */
	public QueryParam(int limit) {
		this.limit = limit;
		this.offset = 0;
		this.sortField = SortField.NONE;
		this.sortOrder = SortOrder.ASCENDING;
	}

	/**
	 * Constructs a new query parameter with limit and offset.
	 * @param limit limits the number of rows to return. 0 or less means no limit.
	 * @param offset the offset/start of the results to return.
	 */
	public QueryParam(int limit, int offset) {
		this.limit = limit;
		this.offset = offset;
		this.sortField = SortField.NONE;
		this.sortOrder = SortOrder.ASCENDING;
	}


	/**
	 * Constructs a new query parameter with limit, offset and sort.
	 * @param limit limits the number of rows to return. 0 or less means no limit.
	 * @param offset the offset/start of the results to return.
	 * @param sortField the field to sort by, TIME for all documents and POPULARITY for published documents.
	 * @param sortOrder ascending or descending, ignored if sort is irrelevant.
	 */
	public QueryParam(int limit, int offset, SortField sortField, SortOrder sortOrder) {
		this.limit = limit;
		this.offset = offset;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}

	/**
	 * Updates a given SQL query with the limits/offset/sort specified by this query parameter.
	 * @param sql the SQL query to modify
	 * @return The updated SQL query with limits/offset/sort specified.
	 */
	public String updateQuery(String sql) {
		if(sortField == SortField.TIME) {
			sql += " ORDER BY update_time " + (sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
		} else if(sortField == SortField.VOTES) {
			sql += " ORDER BY vote_count " + (sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
		} else if(sortField == SortField.POPULARITY) {
			sql += " ORDER BY score " + (sortOrder == SortOrder.ASCENDING ? "ASC" : "DESC");
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

