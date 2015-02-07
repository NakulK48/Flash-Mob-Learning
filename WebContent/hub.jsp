<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.util.LinkedList, uk.ac.cam.grpproj.lima.flashmoblearning.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hub - Flash Mob Learning</title>
<link rel="stylesheet" type="text/css" href="HubStyle.css">
<%!
 	public void jspInit()
	{
		try
		{
			Database.init();
		}
		catch (Exception e)
		{
			System.out.println("Something went wrong! Try reloading the page.");
			return;
		}
	} 
%>
</head>
<body>
<%
	String upvoted = request.getParameter("upvote"); //specifies which document to upvote
	String doctype = request.getParameter("doctype"); //browsing text or skulpt?
	String sortType = request.getParameter("sort");
	
	if (sortType == null) sortType = "hot";
	if (!sortType.equals("new") && !sortType.equals("top") && !sortType.equals("hot")) sortType = "hot";
	
	QueryParam p;
	
	if (sortType.equals("new"))
	{
		p = new QueryParam(25, 0, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
	}
	
	else if (sortType.equals("top"))
	{
		p = new QueryParam(25, 0, QueryParam.SortField.VOTES, QueryParam.SortOrder.DESCENDING);
	}
	
	else // "hot"
	{
		p = new QueryParam(25, 0, QueryParam.SortField.POPULARITY, QueryParam.SortOrder.DESCENDING);
	}

	LinkedList<PublishedDocument> subs = new LinkedList<PublishedDocument>(); //(LinkedList<PublishedDocument>) DocumentManager.getInstance().getPublished(p);
	

	if (upvoted != null)
	{
		long documentToUpvote = Long.parseLong(upvoted);
		//TODO: query DB to find the document with this ID
		//TODO: increase its votes by 1
	}
	
%>
	<div id="orderHolder">
		<a href='<%="hub.jsp?sort=hot"%>'><div class="order">Hot</div></a>
		<a href='<%="hub.jsp?sort=top"%>'><div class="order">Top</div></a>
		<a href='<%="hub.jsp?sort=new"%>'><div class="order">New</div></a>
	</div>

<table>
	<tr>
		<td class='heading' id='upvoteScoreHeading'></td>
		<td class='heading' id='titleSubmitterHeading'></td>
		<td class='heading' id='ageHeading'></td>
	</tr>
	<%
	
		for (PublishedDocument pd : subs)
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
			"<td class='upvote'><button name='upvote" + Long.toString(pd.getID()) + "' >Upvote</button></td>" + //upvote
			//TODO: Replace with upvote sprite
			//TODO: JavaScript to change upvote sprite and increment score locally on upvote.
			"<td class='title'> <a href='../preview.jsp?id=" + Long.toString(pd.getID()) + "'>" + pd.getTitle() 		+ "</a></td>" + //title
			"<td class='age'>" + ageString + "</td>" + //age
			"</tr>" + 
			"<tr class='lowerRow'>" +
			"<td id='score" + Long.toString(pd.getID()) + "' class='votes'>" + pd.getVotes()	+ "</td>" + //score
			"<td class='submitter'> <a href='../userpage.jsp?id=" + Long.toString(pd.owner.getID()) + "'>" + pd.owner.getName() 		+ "</a></td>" + //submitter
			"<td></td>" +
			"</tr>"; 
			
			out.println(entry);
		} 
		
		 /*
		String entry = 
		"<tr class='upperRow'>" + 
		"<td class='upvote'><form method='post' action='hub.jsp'><button name='upvote12345'>Upvote</button></form></td>" + //upvote
		"<td class='title'>" + "Spider climbing up screen" 	+ "</td>" + //title
		"<td class='age'>" + "4" + " hours ago" + "</td>" + //age
		"</tr>" + 
		"<tr class='lowerRow'>" +
		"<td class='votes'>" + "11"	+ "</td>" + //score
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
		"<td class='votes'>" + "18"	+ "</td>" + //score
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
		"<td class='votes'>" + "48"	+ "</td>" + //score
		"<td class='submitter'>" + "Ronald Reagan" 		+ "</td>" + //submitter
		"<td></td>" +
		"</tr>"; 
		
		out.println(entry3);
		*/
	%>
	
	
</table>
</body>
</html>