<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*, java.util.ArrayList, uk.ac.cam.grpproj.lima.flashmoblearning.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
   <head>

      <title>Library</title>
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
            Library
         </div>
         <div class="content">
<%
	
	if(session.getAttribute(Attribute.USERID)==null){
		//session invalid
		response.sendRedirect("login.jsp");
		return;
	}

	if (request.getParameter("doctype") != null)
	{
		String doctypeString = request.getParameter("doctype");
		DocumentType dt = null;
		if (doctypeString.equals("skulpt")) dt = DocumentType.SKULPT;
		else dt = DocumentType.PLAINTEXT;
		session.setAttribute(Attribute.DOCTYPE, dt);
	}

	DocumentType dt = DocumentType.ALL;
	if (session.getAttribute(Attribute.DOCTYPE) == null) {
		response.sendRedirect("landing.jsp");
		return;
	}
	dt = (DocumentType) session.getAttribute(Attribute.DOCTYPE); //browsing text or skulpt?
//TODO: Check whether viewing Skulpt or Text
	DocumentType doctype = (DocumentType) session.getAttribute(Attribute.DOCTYPE);
	
	long uid = (Long) session.getAttribute(Attribute.USERID);
	//TODO: Remove above placeholder
	LoginManager lm = LoginManager.getInstance();
	DocumentManager dm = DocumentManager.getInstance();
	
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
	//number of entries displayed per page
	int limit = 25;
	int offset = (pageNumber - 1) * limit;
	
	User u = lm.getUser(uid);

	QueryParam q = new QueryParam(limit+1, offset, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
	
	//TODO: Change the below to take doctype into account
	ArrayList<WIPDocument> docs = (ArrayList<WIPDocument>) dm.getWorkInProgressByUser(u, dt, q);
	
%>

<table>
	<tr>
		<td class='heading' id='upvoteScoreHeading'></td>
		<td class='heading' id='titleSubmitterHeading'>Title</td>
		<td class='heading' id='ageHeading'>Last edited</td>
	</tr>
	<%
		for (int i=0; i<Math.min(docs.size(),limit);i++){
			String ageString;
			WIPDocument doc = docs.get(i);
			int ageInHours = (int) ((System.currentTimeMillis() - doc.creationTime)/3600000);
			if (ageInHours < 1) ageString = "Less than an hour ago";
			else if (ageInHours < 2) ageString = "An hour ago";
			else if (ageInHours < 24) ageString = ageInHours + " hours ago";
			else
			{
				int ageInDays = ageInHours / 24;
				if (ageInDays == 1) ageString = "yesterday";
				else ageString = ageInDays + " days ago";
			}
			String editor = dt==DocumentType.PLAINTEXT?"'plaintexteditor.jsp":"'editor.jsp";
			String entry = 
			"<tr class='lowerRow'>" + 
			"<td></td>" + 
			"<td class='title'> <a href="+editor+"?docID=" + Long.toString(doc.getID())+"&newDoc="+0+ "'>" + doc.getTitle() + "</a></td>" + //title
			"<td class='age'>" + ageString + "</td>" + //age
			"</tr>"; 
			
			out.println(entry);
		}
		
		//dm.deleteAllDocumentsByUser(jimmy);
		//lm.deleteUser(jimmy);
		
		String previousURL = "library.jsp?page=" + previousPage;
		String nextURL = "library.jsp?page=" + nextPage;
		
	%>
	
	<tr id="pageHolder">	
	<% 
		if (pageNumber != 1){
			%><td id="previous"><a href='<%=previousURL %>'>Previous</a></td><%
		}else{
			%> <td id="previous">Previous</td><%;
		}
		%><td id="current">Page <%=pageNumber %></td><%
		if (docs.size()==limit+1){
			%><td id="next"><a href='<%=nextURL %>'>Next</a></td><%
		}else{
			%> <td id="next">Next</td><%;
		}
	%>
	</tr>	
</table>
         </div>
      </div>

      <!-- The menu -->
      <nav id="menu">
         <ul>
            <li><a href="landing.jsp">Home</a></li>
            <li><a href="hub.jsp">Community Hub</a></li>
            <li><a href="CreateNew.jsp?doctype=skulpt">New Skulpt Document</a></li>
           	<li><a href="CreateNew.jsp?doctype=plaintext">New Text Document</a></li>
          <div style="padding-top:60%;"><a href="logout.jsp">Logout</a></div>  
         </ul>

      </nav>

   </body>
</html>