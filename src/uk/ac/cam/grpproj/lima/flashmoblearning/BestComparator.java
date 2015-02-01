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
		
		long t1 = o1.creationTime;
		long t2 = o2.creationTime;
		
		long a1 = System.currentTimeMillis() - t1;
		long a2 = System.currentTimeMillis() - t2;
		
		int s1 = o1.getScore();
		int s2 = o2.getScore();
		
		double r1 = calculateRanking(a1, s1);
		double r2 = calculateRanking(a2, s2);
		
		return (int) (r2-r1);
	}
	
	public static double calculateRanking(long age, int score)
	{
		age /= 3600000;
		return (score * Math.exp(-8 * age * age));
	}

}
