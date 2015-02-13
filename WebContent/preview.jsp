<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*"%>
    
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


</head >

<body>
<%		
		//Session check
	if(session.getAttribute("uid")==null){
		//session invalid
		response.sendRedirect("login.jsp");
	}
%>

function cloneit(){

}

function editit(){

}

function publishit(){

}

function upvoteit(){

}
	
          <div class="header">
          <a href="#menu"></a>
          Viewer
        </div>


<div>
    <textarea class="textbox" id="text" ></textarea><br /> 
</div>
<div id="buttons" style="padding-left: 40%; padding-right: 30%;">
	<button class="fml_buttons" type="button" onclick=<%if(session.getAttribute("myDoc")=="1"){%>"editit()"<%}else{%>"cloneit()"<%}%>
		style="border-style: none; width:10%; min-width:50px;"><%if(session.getAttribute("myDoc")=="1"){%>Edit<%}else{%>Clone<%}%></button>
	<%if(session.getAttribute("myDoc")=="1" && session.getAttribute("WIPDoc")=="1"){%>
	<button class="fml_buttons" type="button" onclick="publishit()"
		style="border-style: none; width:10%; min-width:50px;">Publish</button>
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