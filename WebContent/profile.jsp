<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="uk.ac.cam.grpproj.lima.flashmoblearning.*,uk.ac.cam.grpproj.lima.flashmoblearning.database.*,java.util.LinkedList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile - Flash Mob Learning</title>
<%!
	public void jspInit()
	{
		try
		{
			Database.init();
		}
		catch (Exception e)
		{
			System.out.println("<p class='error'>Something went wrong! Try reloading the page.</p>");
			return;
		}
	}
%>
</head>
<body>
<%
	long userID = 0;
	String userIDString = request.getParameter("id");
	
	String sortType = request.getParameter("sort");
	if (sortType == null ) sortType = "new";
	if (!sortType.equals("new") && !sortType.equals("top")) sortType = "new";
	String capitalisedSortType = sortType.substring(0, 1).toUpperCase() + sortType.substring(1);
	
	try
	{
		userID = Long.parseLong(userIDString);	
	}
	catch (NumberFormatException e)
	{
		out.println("<p class='error'>This user doesn't seem to exist!</p>");
		return;
	}
	
	LinkedList<PublishedDocument> thisUserDocuments = null;
	User profileUser;

	try
	{
		profileUser = LoginManager.getInstance().getUser(userID);
		QueryParam p;
		if (sortType == "new")
		{
			p = new QueryParam(25, 0, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
		}
		
		else
		{
			p = new QueryParam(25, 0, QueryParam.SortField.VOTES, QueryParam.SortOrder.DESCENDING);
		}

		thisUserDocuments = (LinkedList<PublishedDocument>) DocumentManager.getInstance().getPublishedByUser(profileUser, p);
	}
	
	catch (Exception e)
	{
		out.println("<p class='error'>This user doesn't seem to exist!</p>");
		return;
	}
	
	
%>

	<div id="orderHolder">
		<a href='<%="profile.jsp?id=" + userIDString + "&sort=top"%>'><div class="order">Top</div></a>
		<a href='<%="profile.jsp?id=" + userIDString + "&sort=new"%>'><div class="order">New</div></a>
	</div>

<h1><%= profileUser.name %></h1>
<h2><%= capitalisedSortType %> Documents</h2>
<%
	//TODO: output list of documents: upvote button, votes, title, age
	for (PublishedDocument pd : thisUserDocuments)
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
		"<td></td>" +
		"</tr>"; 
		
		out.println(entry);
	}
	
%>


</body>
</html>