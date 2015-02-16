<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*, java.util.ArrayList, uk.ac.cam.grpproj.lima.flashmoblearning.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
   <head>

      <title>Library - Flash Mob Learning</title>
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
            My Docs
         </div>
         <div class="content">
            <h1>My Library</h1>
<%
	
	if(session.getAttribute("uid")==null){
		//session invalid
		response.sendRedirect("login.jsp");
	}

	//TODO: Check whether viewing Skulpt or Text
	
	long uid = 1;//(Long) session.getAttribute("uid");
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

	int offset = (pageNumber - 1) * 25;
	
	User u = lm.getUser(uid);

	QueryParam q = new QueryParam(25, offset, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
	
	ArrayList<WIPDocument> docs = (ArrayList<WIPDocument>) dm.getWorkInProgressByUser(u, q);
	
%>

<table>
	<tr>
		<td class='heading' id='upvoteScoreHeading'></td>
		<td class='heading' id='titleSubmitterHeading'>Title</td>
		<td class='heading' id='ageHeading'>Last edited</td>
	</tr>
	<%
	
		for (WIPDocument doc : docs)
		{

			String ageString;
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
			
			String entry = 
			"<tr class='lowerRow'>" + 
			"<td></td>" + 
			"<td class='title'> <a href='editor.jsp?id=" + Long.toString(doc.getID()) + "'>" + doc.getTitle() 		+ "</a></td>" + //title
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
	<td id="previous"><a href='<%=previousURL %>'>Previous</a></td>
	<td id="current">Page <%=pageNumber %></td>
	<td id="next"><a href='<%=nextURL %>'>Next</a></td>
	</tr>	
</table>
         </div>
      </div>

      <!-- The menu -->
      <nav id="menu">
         <ul>
            <li><a href="landing.jsp">Home</a></li>
            <li><a href="#">My Docs</a>
               <ul>
                  <li><a href="/about/history">History</a></li>
                  <li><a href="/about/team">The team</a></li>
                  <li><a href="/about/address">Our address</a></li>
               </ul>
            </li>
            <li><a href="hub.jsp">Community Hub</a></li>
          <div style="padding-top:60%;"><a href="logout.jsp">Logout</a></div>  
         </ul>

      </nav>

   </body>
</html>