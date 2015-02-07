<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*, java.util.ArrayList, uk.ac.cam.grpproj.lima.flashmoblearning.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*" %>

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
	LoginManager lm = LoginManager.getInstance();
	DocumentManager dm = DocumentManager.getInstance();
	User jimmy = lm.createUser("Jimmy Fallon", "password1", false);
	PublishedDocument da = new PublishedDocument(-1, DocumentType.SKULPT, jimmy, "Cicada tax cuts", System.currentTimeMillis(), 0);
	PublishedDocument db = new PublishedDocument(-1, DocumentType.SKULPT, jimmy, "Grasshopper tax increases", System.currentTimeMillis(), 1);
	PublishedDocument dc = new PublishedDocument(-1, DocumentType.SKULPT, jimmy, "Preying mantis spending cuts", System.currentTimeMillis(), 2);

	dm.createDocument(da);
	dm.createDocument(db);
	dm.createDocument(dc);
	
	
	

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

	ArrayList<PublishedDocument> subs = (ArrayList<PublishedDocument>) DocumentManager.getInstance().getPublished(p);

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
		
		dm.deleteAllDocumentsByUser(jimmy);
		lm.deleteUser(jimmy);
	%>
	
	
</table>
</body>
</html>