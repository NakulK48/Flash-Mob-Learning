package uk.ac.cam.grpproj.lima.flashmoblearning;

import java.util.Comparator;

public class BestComparator implements Comparator<PublishedDocument> 
{

	@Override
	public int compare(PublishedDocument o1, PublishedDocument o2) {
		long t1 = o1.creationTime;
		long t2 = o2.creationTime;
		
		long a1 = System.currentTimeMillis() - t1;
		long a2 = System.currentTimeMillis() - t2;
		
		int s1 = o1.getScore();
		int s2 = o2.getScore();
		
		int r1 = calculateRanking(a1, s1);
		int r2 = calculateRanking(a2, s2);
		
		return (r1-r2);
	}
	
	public static int calculateRanking(long age, int score)
	{
		return (int) (age * Math.exp(-8 * age * age));
	}

}
