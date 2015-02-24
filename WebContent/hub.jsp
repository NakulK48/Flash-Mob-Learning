<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*, java.util.ArrayList, uk.ac.cam.grpproj.lima.flashmoblearning.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*" %>

<!DOCTYPE html>
<html>
   <head>

      <title>Flash Mob Learning</title>
      <meta charset="utf-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <link type="text/css" href="css/demo.css" rel="stylesheet" />

      <!-- Include jQuery.mmenu .css files -->
      <link type="text/css" href="css/jquery.mmenu.all.css" rel="stylesheet" />

      <!-- Include jQuery and the jQuery.mmenu .js files -->
      <script type="text/javascript" src="jquery-2.1.3.min.js"></script>
      <script type="text/javascript" src="jquery.mmenu.min.all.js"></script>

      <!-- Fire the plugin onDocumentReady -->
      <script type="text/javascript">
         $(document).ready(function() {
            $("#menu").mmenu({
               "slidingSubmenus": false,
               "classes": "mm-white",
               "searchfield": true
            });
         });
      </script>
      <link rel="stylesheet" type="text/css" href="css/HubStyle.css">
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

      <!-- The page -->
      <div class="page">
         <div class="header">
            <a href="#menu"></a>
            Community Hub
         </div>
         <div class="content" style="padding-top:10px;">
<%
	DocumentManager dm = DocumentManager.getInstance();

	String upvoted = request.getParameter("upvote"); //specifies which document to upvote
	if (upvoted != null)
	{
		long thisDocumentID = Long.parseLong(upvoted);
		long thisUserID = (Long) session.getAttribute(Attribute.USERID);
		User thisUser = LoginManager.getInstance().getUser(thisUserID);
		

		PublishedDocument thisDocument = (PublishedDocument) dm.getDocumentById(thisDocumentID);
		
		try
		{
			dm.addVote(thisUser, thisDocument);
		}
		catch (DuplicateEntryException e)
		{
			thisDocument.setVotes(thisDocument.getVotes() - 1);
		}

		thisDocument.setVotes(thisDocument.getVotes() + 1);
	}
	
	String showFeatured = request.getParameter("showFeatured");
	if (showFeatured == null) showFeatured = "true";
	
	DocumentType dt = DocumentType.ALL;
	if (session.getAttribute("doctype") == null) response.sendRedirect("landing.jsp");
	else dt = (DocumentType) session.getAttribute("doctype"); //browsing text or skulpt?
			
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
	QueryParam pf;
	
	int featuredCount = 5;
	if (showFeatured.equals("only")) featuredCount = 25;
	
	if (sortType.equals("new"))
	{
		p = new QueryParam(25, offset, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
		pf = new QueryParam(featuredCount, 0, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
	}
	
	else if (sortType.equals("top"))
	{
		p = new QueryParam(25, offset, QueryParam.SortField.VOTES, QueryParam.SortOrder.DESCENDING);
		pf = new QueryParam(featuredCount, 0, QueryParam.SortField.VOTES, QueryParam.SortOrder.DESCENDING);
	}
	
	else // "hot"
	{
		p = new QueryParam(25, offset, QueryParam.SortField.POPULARITY, QueryParam.SortOrder.DESCENDING);
		pf = new QueryParam(featuredCount, 0, QueryParam.SortField.POPULARITY, QueryParam.SortOrder.DESCENDING);
	}
	
	ArrayList<PublishedDocument> featuredSubs = new ArrayList<PublishedDocument>();
	ArrayList<PublishedDocument> subs = new ArrayList<PublishedDocument>();

	if (pageNumber == 1 && (showFeatured.equals("true") || showFeatured.equals("only"))) 
	{
		featuredSubs = (ArrayList<PublishedDocument>) DocumentManager.getInstance().getFeatured(dt, p);
	}
	
	if (!showFeatured.equals("only"))
	{
		subs = (ArrayList<PublishedDocument>) DocumentManager.getInstance().getPublished(dt, p);
	}


	if (upvoted != null)
	{
		long documentToUpvote = Long.parseLong(upvoted);
		//TODO: query DB to find the document with this ID
		//TODO: increase its votes by 1
	}
	
%> 
	<div id="orderHolder" data-type="controlgroup" data-style="horizontal">
		<div class="order" id="left"><a href='<%="hub.jsp?sort=hot"%>'>Hot</a></div>
		<div class="order" id="centre"><a href='<%="hub.jsp?sort=top"%>'>Top</a></div>
		<div class="order" id="right"><a href='<%="hub.jsp?sort=new"%>'>New</a></div>
	</div>

<table>
	<tr>
		<td class='heading' id='upvoteScoreHeading'></td>
		<td class='heading' id='titleSubmitterHeading'></td>
		<td class='heading' id='ageHeading'></td>
	</tr>
	<%
		if (pageNumber == 1) 
		{
			out.println("<tr><th>Featured</th>");
			if (showFeatured.equals("true"))
			{
				out.println("<th><a href = 'hub.jsp?sort=" + sortType + "&showFeatured=false'>(Hide) </a> ");
				out.println(" <a href = 'hub.jsp?sort=" + sortType + "&showFeatured=only'> (View More)</a></th></tr>");
				out.println("<tr><td>&nbsp;</td></tr>");
			}
			else if (showFeatured.equals("false"))
			{
				out.println("<th><a href = 'hub.jsp?sort=" + sortType + "&showFeatured=true'>(Show)</a></th></tr>");
				out.println("<tr></tr>");
				out.println("<tr><td>&nbsp;</td></tr>");
			}
			else //only
			{
				out.println("<th><a href = 'hub.jsp?sort=" + sortType + "&showFeatured=false'>(Hide) </a></th></tr>");
				out.println("<tr><td>&nbsp;</td></tr>");
			}

		}
		//TODO list featured documents
		for (PublishedDocument pd : featuredSubs)
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
			
			String upvoteLink = "<a href='hub.jsp?page=" + pageNumber + "&sort=" + sortType + "&upvote=" + Long.toString(pd.getID()) + "'>";
			String entry = 
			"<tr class='upperRow'>" + 
			"<td class='upvote'>" + upvoteLink + " <button name='upvote'>UP</button></a></td>" + //upvote
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
		

		if (pageNumber == 1 && !showFeatured.equals("only"))
		{
			out.println("<tr><th>The Rest</th></tr>");
			out.println("<tr><td>&nbsp;</td></tr>");
		}
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
			
			String upvoteLink = "<a href='hub.jsp?page=" + pageNumber + "&sort=" + sortType + "&upvote=" + Long.toString(pd.getID()) + "'>";
			String entry = 
			"<tr class='upperRow'>" + 
			"<td class='upvote'>" + upvoteLink + " <button name='upvote'>UP</button></a></td>" + //upvote
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
		
		//dm.deleteAllDocumentsByUser(jimmy);
		//lm.deleteUser(jimmy);
		
		String previousURL = "hub.jsp?sort=" + sortType + "&page=" + previousPage;
		String nextURL = "hub.jsp?sort=" + sortType + "&page=" + nextPage;
		
	%>


</table>
	<div data-role="footer" class="ui-bar" data-position="fixed">	
	
	<div data-style="controlgroup" data-type="horizontal">
		<div><a href='<%=previousURL %>'>Previous</a></div>
		<div>Page <%=pageNumber %></div>
		<div><a href='<%=nextURL %>'>Next</a></div>
	</div>

	</div>	
         </div>
      </div>

      <!-- The menu -->
      <nav id="menu">
         <ul>
            <li><a href="landing.jsp">Home</a></li>
            <li><a href="library.jsp">My Docs</a></li>
            <li><a href="hub.jsp">Community Hub</a></li>
            <li><a href="logout.jsp">Logout</a></li>
         </ul>
      </nav>

   </body>
</html>