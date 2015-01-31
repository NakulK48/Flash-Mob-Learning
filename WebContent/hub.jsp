<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="uk.ac.cam.grpproj.lima.flashmoblearning.*, java.util.LinkedList"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hub - Flash Mob Learning</title>
<style>

td
{
	text-align:center;
}
.heading
{
	font-size:14pt;
	color:navy;
	text-align:center;
}

#upvoteHeading
{
	width:10%
}

#scoreHeading
{
	width:10%;
}

#titleHeading
{
	width:40%;
}

#submitterHeading
{
	width:15%;
}

#dateHeading
{
	width:15%;
}
table
{
	width:100%;
	border:1px solid black;
}

td
{
	border:1px solid black;
}
</style>
</head>
<body>
<%
/*
	Hub hub = new Hub();
	SortType st = SortType.BEST;
	hub.sort(st);
	
	LinkedList<PublishedDocument> subs = hub.submissions;
*/
%>
<table>
	<tr>
		<td class='heading' id='upvoteHeading'></td>
		<td class='heading' id='scoreHeading'>Score</td>
		<td class='heading' id='titleHeading'>Title</td>
		<td class='heading' id='submitterHeading'>Submitter</td>
		<td class='heading' id='dateHeading'>Submitted</td>
	</tr>
	
	<tr>
		<td class='upvote'>UV</td>
		<td class='score'>14</td>
		<td class='title'>Spider crawling up wall</td>
		<td class='owner'>Bill Smith</td>
		<td class='age'>"7 hours ago</td>
	</tr>   
		
		
	<%
		/*
		for (PublishedDocument pd : subs)
		{
			long ageInHours = (System.currentTimeMillis() - pd.creationTime)/3600000;
			String entry = 
			"<tr>" + 
			"<td class='upvote'> UV </td>" 						//upvote button
			"<td class='score'>" + pd.getScore()	+ "</td>" + //score
			"<td class='title'>" + pd.getTitle() 	+ "</td>" + //title
			"<td class='owner'>" + pd.owner 		+ "</td>" + //submitter
			"<td class='age'>" + ageInHours + " hours ago" + "</td>" + //age
			"</tr>"; 
			out.println(entry);
		}
		*/
	%>
</table>
</body>
</html>