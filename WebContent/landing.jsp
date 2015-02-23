<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.util.Date,uk.ac.cam.grpproj.lima.flashmoblearning.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
   <meta name="viewport" content="width=device-width, initial-scale=1">
   <link rel="stylesheet" href="jquery.mobile-1.4.5/jquery.mobile-1.4.5.min.css">
   <link rel="stylesheet" href="static/homeattempt.css">
       <link type="text/css" href="jquery.mmenu.all.css" rel="stylesheet" />
    <!--  change the jquery.js to a jquery.min.js before distribution -->
    <script type="text/javascript" src="jquery.mobile-1.4.5/jquery-2.1.3.min.js"></script> 
    <script type="text/javascript" src="jquery.mobile-1.4.5/jquery.mobile-1.4.5.min.js"></script>

          <link type="text/css" href="css/demo.css" rel="stylesheet" />

      <!-- Include jQuery.mmenu .css files -->
      <link type="text/css" href="css/jquery.mmenu.all.css" rel="stylesheet" />

      <!-- Include jQuery and the jQuery.mmenu .js files -->
      <script type="text/javascript" src="jquery-2.1.3.min.js"></script>
      <script type="text/javascript" src="jquery.mmenu.min.all.js"></script>
    
<title>Home - Flash Mob Learning</title>
</head>
<body>
	<%
		
		//Session check
		if(session.getAttribute(Attribute.USERID)==null){
			//session invalid
			response.sendRedirect("login.jsp");
		}
	%>
<div data-role="page">
   <div data-role="header">
      <h1>Welcome, <%=session.getAttribute(Attribute.USERNAME) %>! <a href="logout.jsp">Logout</a></h1>
   </div> 
   <div class="button_skulpt" width="device-width">
      <a href="library.jsp?doctype=skulpt">
      <img src="static/skulptbutton.png" id="Skulpt" width="100%"></a>
   </div>
   <div class="button_text" width="device-width">
      <a href="library.jsp?doctype=plaintext">
      <img src="static/textbutton.png" id="Text" width="100%"></a>
   </div>
</div>

</body>
</html>
