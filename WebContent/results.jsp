<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="uk.ac.cam.grpproj.lima.flashmoblearning.*,uk.ac.cam.grpproj.lima.flashmoblearning.database.*,java.util.ArrayList,java.util.TreeSet"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Results - Flash Mob Learning</title>
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
	String searchQuery = request.getParameter("query");
	if (searchQuery == null) searchQuery = "";
	String searchDomain = request.getParameter("domain"); //tag, document or user.
	if (searchDomain == null) searchDomain = "documents";
	if (!searchDomain.equals("documents") && !searchDomain.equals("tags") && !searchDomain.equals("users") ) searchDomain = "documents";
%>

	<div id="searchTypesHolder">
		<a href='<%="results.jsp?query=" + searchQuery + "&domain=documents"%>'><div class="searchType">Documents</div></a>
		<a href='<%="results.jsp?query=" + searchQuery + "&domain=users"%>'><div class="searchType">Users</div></a>
		<a href='<%="results.jsp?query=" + searchQuery + "&domain=tags"%>'><div class="searchType">Tags</div></a>
	</div>
	<br>
	
<%
	if (searchQuery == null || searchQuery.length() == 0) out.println("<p class='error'>Whoops! We need something to search for.</p>");
	
	else
	{
		out.println("<p id='query'>Searching " + searchDomain + " for '" + searchQuery + "'</p>");
	}
%>
<table>
<%

	if (searchDomain.equals("documents"))
	{
		//TODO: query Document database for matching titles
		QueryParam p = new QueryParam(25, 0, QueryParam.SortField.VOTES, QueryParam.SortOrder.DESCENDING);
		ArrayList<PublishedDocument> matchingDocs = (ArrayList<PublishedDocument>) DocumentManager.getInstance().getPublishedByTitle(searchQuery, p);
		for (PublishedDocument pd : matchingDocs)
		{

			String ageString;
			int ageInHours = (int) ((System.currentTimeMillis() - pd.creationTime)/3600000);
			if (ageInHours < 1) ageString = "Less than an hour ago";
			else if (ageInHours < 2) ageString = "An hour ago";
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
			"<td class='title'> <a href='preview.jsp?id=" + Long.toString(pd.getID()) + "'>" + pd.getTitle() 		+ "</a></td>" + //title
			"<td class='age'>" + ageString + "</td>" + //age
			"</tr>" + 
			"<tr class='lowerRow'>" +
			"<td id='score" + Long.toString(pd.getID()) + "' class='votes'>" + pd.getVotes()	+ "</td>" + //score
			"<td class='submitter'> <a href='profile.jsp?id=" + Long.toString(pd.owner.getID()) + "'>" + pd.owner.getName() 		+ "</a></td>" + //submitter
			"<td></td>" +
			"</tr>"; 
			
			out.println(entry);
		} 
		
	}
	
	else if (searchDomain.equals("users"))
	{
		//TODO: maybe implement a fuzzier search here?
			try
			{
				User u = LoginManager.getInstance().getUser(searchQuery);
				String userID = Long.toString(u.getID());
				out.println(userID);
				out.println("<a class='searchResult' href='profile.jsp?id=" + userID + "'>" + u.getName() + "</a>");		
			}
			catch (Exception e)
			{
				out.println("No such user exists.");
			}

	
	}
	
	else if (searchDomain.equals("tags"))
	{
		//TODO: query Tag database for matching names
			try
			{
				Tag tag = DocumentManager.getInstance().getTag(searchQuery);
				String tagID = tag.name;
				out.println("<a class='searchResult' href='tag.jsp?name=" + tagID + "'>" + tag.name + "</a>");		
			}
			catch (Exception e)
			{
				out.println("No such tag exists.");
			}

	}
%>
</table>


</body>
</html>