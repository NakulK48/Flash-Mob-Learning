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
	
          <div class="header">
          <a href="#menu"></a>
          Viewer
        </div>


<div>
    <textarea class="textbox" id="text" ></textarea><br /> 
</div>
<div>
    <!-- complete these buttons-->
    <button type="button" onclick="cloneit()">Clone</button> 
    <button type="button"></button>
 </div>
      <!-- The page -->



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