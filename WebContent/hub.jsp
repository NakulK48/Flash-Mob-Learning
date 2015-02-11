<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*, java.util.ArrayList, uk.ac.cam.grpproj.lima.flashmoblearning.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>



</style>
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
		
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	} 
%>
</head>
<body>
<%

	

	/* LoginManager lm = LoginManager.getInstance();
	DocumentManager dm = DocumentManager.getInstance();
	
	User u = lm.createUser("Bob", "password1", false);
	
	PublishedDocument pd1 = new PublishedDocument(-1, DocumentType.SKULPT, u, "ABC", System.currentTimeMillis(), 0);
	PublishedDocument pd2 = new PublishedDocument(-1, DocumentType.SKULPT, u, "DEF", System.currentTimeMillis() - 36000000, 5);
	PublishedDocument pd3 = new PublishedDocument(-1, DocumentType.SKULPT, u, "GHI", System.currentTimeMillis() - 72000000, 10);
	
	Tag t = Tag.create("Synergy");
	
	dm.createDocument(pd1);
	dm.createDocument(pd2);
	dm.createDocument(pd3);
	
	
	dm.addTag(pd1, t);
	dm.addTag(pd2, t);
	dm.addTag(pd3, t);
 */	


	String upvoted = request.getParameter("upvote"); //specifies which document to upvote
	String doctype = request.getParameter("doctype"); //browsing text or skulpt?
	String sortType = request.getParameter("sort");
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
	
	if (sortType == null) sortType = "hot";
	if (!sortType.equals("new") && !sortType.equals("top") && !sortType.equals("hot")) sortType = "hot";
	
	QueryParam p;
	
	if (sortType.equals("new"))
	{
		p = new QueryParam(25, offset, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
	}
	
	else if (sortType.equals("top"))
	{
		p = new QueryParam(25, offset, QueryParam.SortField.VOTES, QueryParam.SortOrder.DESCENDING);
	}
	
	else // "hot"
	{
		p = new QueryParam(25, offset, QueryParam.SortField.POPULARITY, QueryParam.SortOrder.DESCENDING);
	}

	ArrayList<PublishedDocument> subs = (ArrayList<PublishedDocument>) DocumentManager.getInstance().getPublished(p);

	if (upvoted != null)
	{
		long documentToUpvote = Long.parseLong(upvoted);
		//TODO: query DB to find the document with this ID
		//TODO: increase its votes by 1
	}
	
%>
	<div id="orderHolder">
		<a href='<%="hub.jsp?sort=hot"%>'><div class="order" id="left">Hot</div></a>
		<a href='<%="hub.jsp?sort=top"%>'><div class="order" id="centre">Top</div></a>
		<a href='<%="hub.jsp?sort=new"%>'><div class="order" id="right">New</div></a>
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
		
		//dm.deleteAllDocumentsByUser(jimmy);
		//lm.deleteUser(jimmy);
		
		String previousURL = "hub.jsp?sort=" + sortType + "&page=" + previousPage;
		String nextURL = "hub.jsp?sort=" + sortType + "&page=" + nextPage;
		
	%>
	
	<tr id="pageHolder">	
	<td id="previous"><a href='<%=previousURL %>'>Previous</a></td>
	<td id="current">Page <%=pageNumber %></td>
	<td id="next"><a href='<%=nextURL %>'>Next</a></td>
	</tr>	
</table>
</body>
</html>