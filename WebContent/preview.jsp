<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, java.util.Date"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*, java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link type="text/css" href="textpages.css" rel="stylesheet" />

    <!-- Include jQuery.mmenu .css files -->
    <link type="text/css" href="jquery.mmenu.all.css" rel="stylesheet" />

    <!-- Include jQuery and the jQuery.mmenu .js files -->
    <script type="text/javascript" src="jquery.mobile-1.4.5/jquery-2.1.3.min.js"></script>
    <script type="text/javascript" src="jQuery.mmenu-master/src/js/jquery.mmenu.min.all.js"></script>

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
<title>Preview</title>
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

</head >

<body>
<%		
		//Session check
	if(session.getAttribute("uid")==null){
		//session invalid
		response.sendRedirect("login.jsp");
	}
%>

<script type="text/javascript"> 

	<%
	LoginManager l = LoginManager.getInstance();
	User u = l.getUser((String) session.getAttribute(Attribute.USERNAME));
	Long docID = Long.parseLong(request.getParameter("docID"));
	Document doc = DocumentManager.getInstance().getDocumentById(docID);
	%>

function cloneit(){
	document.location.href = "fork.jsp?docid="+request.getParameter("docid")
}

function editit(){
	document.location.href = "plaintexteditor.jsp?docID="+request.getParameter("docID")+"&newdoc=0&wipdoc=1&mydoc=1"
}

function publishit(){
	document.location.href = "publish.jsp?docID="+request.getParameter("docID");
}

function upvoteit(){
 <!-- TODO -->
}

</script>
          <div class="header">
          <a href="#menu"></a>
          Viewer
        </div>

<div>
<h1 id="titlearea">
     <%=doc.getTitle() %>
</h1>
<%	boolean hasParent = false;
try{
	String parentTitle=doc.getParentDocument().getTitle();
	hasParent=true;
	}catch(Exception e){}%>
<h2 id="parentdoctitle"> <%if(hasParent){%><%="Based on"+ doc.getParentDocument().getTitle() + "."%><%}else%><%=""%></h2>
<p id="bodyarea">
	<%= DocumentManager.getInstance().getRevisionContent(doc.getLastRevision()) %>
</p>
<p id="tagarea">
	Tags : <%= doc.getTags() %>
</p>
<!-- TODO : upvote button qnd upvote count -->
</div>

<div id="buttons" style="padding-left: 40%; padding-right: 30%;">
	<%if(session.getAttribute("myDoc")=="1"){%>
		<button class="fml_buttons" type="button" onclick="editit()"
				style="border-style: none; width:10%; min-width:50px;">Edit</button>
		<%if(session.getAttribute("WIPDoc")=="1"){%>
			<button class="fml_buttons" type="button" onclick="publishit()"
				style="border-style: none; width:10%; min-width:50px;">Publish</button>
	
	<%}}else{%>
		<button class="fml_buttons" type="button" onclick="cloneit()"
				style="border-style: none; width:10%; min-width:50px;">Clone</button>
		<button class="fml_buttons" type="button" onclick="upvoteit()"
				style="border-style: none; width:10%; min-width:50px;">Upvote</button>
	<%}%>
</div>



      <!-- The menu -->
      <nav id="menu">
         <ul>
            <li><a href="home.jsp">Home</a></li>
            <li><a href="library.jsp">My Docs</a></li>
            <li><a href="hub.jsp">Community Hub</a></li>
          <div style="padding-top:60%;"><a href="logout.jsp">Logout</a></div>  
         </ul>

      </nav>

</body> 
 
</html> 