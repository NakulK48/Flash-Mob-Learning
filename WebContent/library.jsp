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
<%	
	if(session.getAttribute(Attribute.USERID)==null){
		//session invalid
		response.sendRedirect("login.jsp");
		return;
	}

	String doctypeString = request.getParameter("doctype");
	if (doctypeString != null) {
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
%>

      <!-- The page -->
      <div class="page">
         <div class="header">
            <a href="#menu"></a>
             <%=dt==DocumentType.SKULPT?"Skulpt - ":"Text - " %>Library
         </div>
         <div class="content">
<%
	long uid = (Long) session.getAttribute(Attribute.USERID);
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
		<th class='heading' id='upvoteScoreHeading'></th>
		<th class='heading' id='titleSubmitterHeading'>Title</th>
		<th class='heading' id='ageHeading'>Last edited</th>
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
</table>

	<script>
	$(window).bind("load", function() { 
	    
	    var footerHeight = 0,
	        footerTop = 0,
	        $footer = $("#footer");
	        
	    positionFooter();
	    
	    function positionFooter() {
	    
	             footerHeight = $footer.height();
	             footerTop = ($(window).scrollTop()+$(window).height()-footerHeight)+"px";
	    
	            if ( ($(document.body).height()+footerHeight) < $(window).height()) {
	                $footer.css({
	                     position: "absolute"
	                }).animate({
	                     top: footerTop
	                })
	            } else {
	                $footer.css({
	                     position: "static"
	                })
	            }
	            
	    }
	
	    $(window)
	            .scroll(positionFooter)
	            .resize(positionFooter)
	            
	});
	</script>
		
	<div class="footer fixed"  >	
		<div id="inner">
		<%
			if (pageNumber != 1){
				%><div id="previousLink" class="footerElem"><a href='<%=previousURL %>'>Previous</a></div><%
			}else{
				%><div id="previousLink" class="footerElem">Previous</div><%;
			}
			%><div id="pageNumber" class="footerElem">Page <%=pageNumber %></div><%
			if (docs.size()==limit+1){
				%><div id="nextLink" class="footerElem"><a href='<%=nextURL %>'>Next</a></div><%
			}else{
				%><div id="nextLink" class="footerElem">Next</div><%;
			}%>
		</div>
	</div>	
	
	<style>
		
		#footer { height: 100px}	
		#inner{width:200px; display:block; margin:0 auto;}
		.footerElem{float:left; padding-right:3%}
	</style>
         </div>
      </div>

      <!-- The menu -->
      <nav id="menu">
         <ul>
            <li><a href="landing.jsp">Home</a></li>
            <li><a href="CreateNew.jsp?doctype=<%=(dt==DocumentType.SKULPT?"skulpt":"plaintext")%>">New Document</a></li>
            <li><a href="library.jsp">Library</a></li>
            <li><a href="profile.jsp?id=<%=uid%>">My Published Docs</a></li>
            <li><a href="hub.jsp">Community Hub</a></li>
            <li><a href="results.jsp">Search</a></li>
            <li style="padding-top: 140%;"></li>
            <li><a href="logout.jsp">Logout</a></li>
         </ul>
      </nav>

   </body>
</html>