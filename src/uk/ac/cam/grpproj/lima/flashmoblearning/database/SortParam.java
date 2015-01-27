package uk.ac.cam.grpproj.lima.flashmoblearning.database;

public class SortParam {
	
	public final SortField SortField;
	public final SortOrder SortOrder;
	
	public SortParam(SortField sortField, SortOrder sortOrder) {
		SortField = sortField;
		SortOrder = sortOrder;
	}
	
	public enum SortField {
		
		// No sorting, defaults to sort by insertion time.
		NONE,	
		// Sort by creation time - limited to Documents.
		TIME,	
		// Sorts by popularity - limited to PublishedDocuments.
		POPULARITY
	}

	public enum SortOrder {

		ASCENDING,
		DESCENDING
		
	}


}

