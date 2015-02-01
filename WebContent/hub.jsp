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
/* 		for (PublishedDocument pd : subs)
		{
			long ageInHours = (System.currentTimeMillis() - pd.creationTime)/3600000;
			String entry = 
			"<tr class='upperRow'>" + 
			"<td class='upvote'> UV </td>" + //upvote
			"<td class='title'>" + pd.getTitle() 	+ "</td>" + //title
			"<td class='age'>" + ageInHours + " hours ago" + "</td>" + //age
			"</tr>" + 
			"<tr class='lowerRow'>" +
			"<td class='score'>" + pd.getScore()	+ "</td>" + //score
			"<td class='submitter'>" + pd.owner 		+ "</td>" + //submitter
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
		"<td class='submitter'>" + "Mike Smith" 		+ "</td>" + //submitter
		"<td></td>" +
		"</tr>"; 
		
		out.println(entry);
	%>
	
	
</table>
</body>
</html>