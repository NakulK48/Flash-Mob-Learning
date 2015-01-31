package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.Comparator;
import java.util.Date;
import uk.ac.cam.grpproj.lima.flashmoblearning.PublishedDocument;

public class NewComparator<PublishedDocument> implements Comparator<PublishedDocument>{

	@Override
	public int compare(PublishedDocument o1, PublishedDocument o2) {
		Date d1 = o1.getDateSubmitted();
		Date d2 = o2.getDateSubmitted();
		
		if (d1.before(d2))
		{
			return 1;
		}
		
		else if (d1.after(d2))
		{
			return -1;
		}
		// TODO Auto-generated method stub
		return 0;
		// TODO Auto-generated method stub
		return 0;
	} 
	

}
