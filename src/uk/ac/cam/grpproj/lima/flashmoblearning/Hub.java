package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.Collections;
import java.util.LinkedList;

public class Hub {
	public LinkedList<PublishedDocument> submissions;
	
	public Hub()
	{
		//obtain submissions from database in any order
	}
	
	public void sort (SortType t)
	{
		switch(t)
		{
		case BEST:
			Collections.sort(submissions, new BestComparator());
		case TOP:
			//get from DB, ordered by score
		case NEW:
			//get from DB, ordered in reverse by creation time
			
		}
	}

}
