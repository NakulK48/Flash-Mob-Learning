package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.Comparator;

public class BestComparator implements Comparator<PublishedDocument> 
{

	@Override
	public int compare(PublishedDocument o1, PublishedDocument o2) {
		if (o1.getFeatured() && !o2.getFeatured())
		{
			return -1;
		}
		
		else if (!o1.getFeatured() && o2.getFeatured())
		{
			return 1;
		}
		
		double s1 = o1.getScore();
		double s2 = o2.getScore();
		
		if(s1 > s2) return 1;
		else if(s1 < s2) return -1;
		else return 0;
	}
	
}
