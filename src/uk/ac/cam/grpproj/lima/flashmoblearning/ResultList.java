package uk.ac.cam.grpproj.lima.flashmoblearning;

/** A collection of documents to be displayed. The result of a search.
 * Can then be sorted by various filters. May actually wrap a query, i.e. be lazily evaluated. */
public interface ResultList<T extends Document> {

	/** How many results? */
	public int size();
	
	/** Render using the current sorting mode */
	public String render(int maxCount);
	
}
