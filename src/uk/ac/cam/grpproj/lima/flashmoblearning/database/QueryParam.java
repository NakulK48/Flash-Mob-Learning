package uk.ac.cam.grpproj.lima.flashmoblearning.database;

public class QueryParam {

	// A limit of 0 or less implies
	public final int limit;
	public final SortField sortField;
	public final SortOrder sortOrder;
	public static QueryParam UNSORTED = new QueryParam(0);

	/**
	 * @param limit Limits the number of rows to return. 0 or less means no limit.
	 */
	public QueryParam(int limit) {
		this.limit = 0;
		this.sortField = SortField.NONE;
		this.sortOrder = SortOrder.ASCENDING;
	}
	
	/**
	 * @param limit Limits the number of rows to return. 0 or less means no limit.
	 * @param sortField The field to sort by, TIME for all documents and POPULARITY for published documents.
	 * @param sortOrder Ascending or Descending, ignored if sort is irrelevant.
	 */
	public QueryParam(int limit, SortField sortField, SortOrder sortOrder) {
		this.limit = limit;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
	}
	
	public static enum SortField {
		
		// No sorting, defaults to sort by insertion time.
		NONE,	
		// Sort by creation time - limited to Documents.
		TIME,	
		// Sorts by popularity - limited to PublishedDocuments.
		POPULARITY
		
	}

	public static enum SortOrder {

		ASCENDING,
		DESCENDING
		
	}


}

