<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, java.util.Date"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*"%>
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

</head >

<body onload="initialisepage()">
<%		
		//Session check
	if(session.getAttribute("uid")==null){
		//session invalid
		response.sendRedirect("login.jsp");
	}
%>

<script type="text/javascript"> 

function initialisepage(){
	<%
	LoginManager l = LoginManager.getInstance();
	User u = l.getUser((String) session.getAttribute("username"));
	Long docID = Long.parseLong((String)session.getAttribute("docID"));
	%>
}

function cloneit(){
	<%
	PublishedDocument pubdoc = (PublishedDocument) DocumentManager.getInstance().getDocumentById(docID);
	WIPDocument wipdoc0 = pubdoc.fork(u);
	session.setAttribute("docID", wipdoc0.getID());
	session.setAttribute("WIPDoc", "1");
	session.setAttribute("myDoc", "1");
	session.setAttribute("newDoc","0");
	%>
	document.location.href = "plaintexteditor.jsp" 
}

function editit(){
	<%
	session.setAttribute("docID", docID);
	session.setAttribute("WIPDoc", "1");
	session.setAttribute("myDoc", "1");
	session.setAttribute("newDoc","0");
	%>
	document.location.href = "plaintexteditor.jsp" 
}

function publishit(){
	<%
	WIPDocument wipdoc1 = (WIPDocument) DocumentManager.getInstance().getDocumentById(docID);
	PublishedDocument pubdoc1 = wipdoc1.publish();
	session.setAttribute("docID", pubdoc1.getID());
	session.setAttribute("WIPDoc", "0");
	session.setAttribute("myDoc", "0");
	session.setAttribute("newDoc","0");
	%>
	document.location.href = "successfulpublish.jsp"
}

function upvoteit(){

}

</script>
          <div class="header">
          <a href="#menu"></a>
          Viewer
        </div>


<div>
    <textarea class="textbox" id="text" ></textarea><br /> 
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
	<%}%>
</div>



      <!-- The menu -->
      <nav id="menu">
         <ul>
            <li><a href="home.html">Home</a></li>
            <li><a href="#">My Docs</a></li>
            <li><a href="communityHub.html">Community Hub</a></li>
          <div style="padding-top:60%;"><a href="logout.html">Logout</a></div>  
         </ul>

      </nav>

</body> 
 
</html> 