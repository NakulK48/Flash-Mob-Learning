<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="uk.ac.cam.grpproj.lima.flashmoblearning.*, java.util.LinkedList"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hub - Flash Mob Learning</title>
<link rel="stylesheet" type="text/css" href="HubStyle.css">

</head>
<body>
<%
/* 	Hub hub = new Hub();
	SortType st = SortType.BEST;
	hub.sort(st);
	
	LinkedList<PublishedDocument> subs = hub.submissions; */
	
%>
<table>
	<tr>
		<td class='heading' id='upvoteScoreHeading'></td>
		<td class='heading' id='titleSubmitterHeading'></td>
		<td class='heading' id='ageHeading'></td>
	</tr>
	<%
 /* 	for (PublishedDocument pd : subs)
		{
			String ageString;
			int ageInHours = (int) ((System.currentTimeMillis() - pd.creationTime)/3600000);
			if (ageInHours < 1) ageString = "Less than an hour ago";
			else if (ageInHours < 24) ageString = ageInHours + " hours ago";
			else
			{
				int ageInDays = ageInHours / 24;
				ageString = ageInDays + " days ago";
			}
			
			String entry = 
			"<tr class='upperRow'>" + 
			"<td class='upvote'> UV </td>" + //upvote
			//TODO: Replace with upvote sprite
			//TODO: JavaScript to change upvote sprite and increment score locally on upvote.
			"<td class='title'>" + pd.getTitle() 	+ "</td>" + //title
			"<td class='age'>" + ageString + "</td>" + //age
			"</tr>" + 
			"<tr class='lowerRow'>" +
			"<td class='score'>" + pd.getScore()	+ "</td>" + //score
			"<td class='submitter'> <a href='../userpage.jsp?id=" + Long.toString(pd.owner.getID()) + "'>" + pd.owner.name 		+ "</a></td>" + //submitter
			"<td></td>" +
			"</tr>"; 
			
			out.println(entry);
		} */
		
		String entry = 
		"<tr class='upperRow'>" + 
		"<td class='upvote'>O</td>" + //upvote
		"<td class='title'>" + "Spider climbing up screen" 	+ "</td>" + //title
		"<td class='age'>" + "4" + " hours ago" + "</td>" + //age
		"</tr>" + 
		"<tr class='lowerRow'>" +
		"<td class='score'>" + "11"	+ "</td>" + //score
		"<td class='submitter'>" + "George Bush" 		+ "</td>" + //submitter
		"<td></td>" +
		"</tr>"; 
		
		out.println(entry);
		
		String entry2 = 
		"<tr class='upperRow'>" + 
		"<td class='upvote'>O</td>" + //upvote
		"<td class='title'>" + "Mosquito flying around screen" 	+ "</td>" + //title
		"<td class='age'>" + "Less than an hour ago" + "</td>" + //age
		"</tr>" + 
		"<tr class='lowerRow'>" +
		"<td class='score'>" + "18"	+ "</td>" + //score
		"<td class='submitter'>" + "Bill Clinton" 		+ "</td>" + //submitter
		"<td></td>" +
		"</tr>"; 
		
		out.println(entry2);
		
		String entry3 = 
		"<tr class='upperRow'>" + 
		"<td class='upvote'>O</td>" + //upvote
		"<td class='title'>" + "Cicada receiving tax cuts" 	+ "</td>" + //title
		"<td class='age'>" + "2 days ago" + "</td>" + //age
		"</tr>" + 
		"<tr class='lowerRow'>" +
		"<td class='score'>" + "48"	+ "</td>" + //score
		"<td class='submitter'>" + "Ronald Reagan" 		+ "</td>" + //submitter
		"<td></td>" +
		"</tr>"; 
		
		out.println(entry3);
	%>
	
	
</table>
</body>
</html>