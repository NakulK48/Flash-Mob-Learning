<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="uk.ac.cam.grpproj.lima.flashmoblearning.*,uk.ac.cam.grpproj.lima.flashmoblearning.database.*,java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Tag - Flash Mob Learning</title>
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
			System.out.println("<p class='error'>Something went wrong! Try reloading the page.</p>");
			return;
		}
	}
%>
<style>
table {top:200px;}
</style>
</head>
<body>
<%
	String tagName = request.getParameter("name");
	
	String sortType = request.getParameter("sort");
	
	
	if (sortType == null ) sortType = "new";
	if (!sortType.equals("new") && !sortType.equals("top")) sortType = "new";
	String capitalisedSortType = sortType.substring(0, 1).toUpperCase() + sortType.substring(1);
	ArrayList<PublishedDocument> thisTagDocuments = null;
	
	String pageNumberString = request.getParameter("page");
	if (pageNumberString == null) pageNumberString = "1";
	int pageNumber = Integer.parseInt(pageNumberString);
	int previousPage = pageNumber - 1;
	int nextPage = pageNumber + 1;
	if (pageNumber <= 1)
	{
		pageNumber = 1;
		previousPage = 1;
	}

	int offset = (pageNumber - 1) * 25;

	try
	{
		Tag tag = DocumentManager.getInstance().getTag(tagName);
		QueryParam p;
		if (sortType.equals("new"))
		{
			p = new QueryParam(25, offset, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
		}
		
		else
		{
			p = new QueryParam(25, offset, QueryParam.SortField.VOTES, QueryParam.SortOrder.DESCENDING);
		}

		thisTagDocuments = (ArrayList<PublishedDocument>) DocumentManager.getInstance().getPublishedByTag(tag, p);
	}
	
	catch (Exception e)
	{
		out.println("<p class='error'>This tag doesn't seem to exist!</p>");
		return;
	}
	
	
%>

	<div id="orderHolder">
		<a href='<%="tag.jsp?name=" + tagName + "&sort=top"%>'><div class="order" id="left">Top</div></a>
		<a href='<%="tag.jsp?name=" + tagName + "&sort=new"%>'><div class="order" id="right">New</div></a>
	</div>

<h1>Tag: <%= tagName %></h1>
<h2><%= capitalisedSortType %> Documents</h2>
<table>
<%
	for (PublishedDocument pd : thisTagDocuments)
	{
		String ageString;
		int ageInHours = (int) ((System.currentTimeMillis() - pd.creationTime)/3600000);
		if (ageInHours < 1) ageString = "Less than an hour ago";
		else if (ageInHours < 2) ageString = "An hour ago";
		else if (ageInHours < 24) ageString = ageInHours + " hours ago";
		else
		{
			int ageInDays = ageInHours / 24;
			if (ageInDays == 1) ageString = "yesterday";
			else ageString = ageInDays + " days ago";
		}
		
		String entry = 
		"<tr class='upperRow'>" + 
		"<td class='upvote'><button name='upvote" + Long.toString(pd.getID()) + "' >UP</button></td>" + //upvote
		//TODO: Replace with upvote sprite
		//TODO: JavaScript to change upvote sprite and increment score locally on upvote.
		"<td class='title'> <a href='preview.jsp?docID=" + Long.toString(pd.getID()) + "'>" + pd.getTitle() 		+ "</a></td>" + //title
		"<td class='age'>" + ageString + "</td>" + //age
		"</tr>" + 
		"<tr class='lowerRow'>" +
		"<td id='score" + Long.toString(pd.getID()) + "' class='votes'>" + pd.getVotes()	+ "</td>" + //score
		"<td class='submitter'> <a href='profile.jsp?id=" + Long.toString(pd.owner.getID()) + "'>" + pd.owner.getName() 		+ "</a></td>" + //submitter
		"<td></td>" +
		"</tr>"; 
		
		out.println(entry);
	}
	
		String previousURL = "tag.jsp?sort=" + sortType + "&name=" + tagName + "&page=" + previousPage;
		String nextURL = "tag.jsp?sort=" + sortType + "&name=" + tagName + "&page=" + nextPage;
		
	%>
	
	<tr id="pageHolder">	
	<td id="previous"><a href='<%=previousURL %>'>Previous</a></td>
	<td id="current">Page <%=pageNumber %></td>
	<td id="next"><a href='<%=nextURL %>'>Next</a></td>
	</tr>	
</table>

</body>
</html>