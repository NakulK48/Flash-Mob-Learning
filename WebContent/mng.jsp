<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Manage - Flash Mob Learning</title>
</head>
<body>
	<%
	//Session check
	if(session.getAttribute(Attribute.USERID)==null){
		//session invalid
		response.sendRedirect("login.jsp");
		return;
	}
	if(session.getAttribute(Attribute.PRIVILEGE)==null||
			!session.getAttribute(Attribute.PRIVILEGE).equals("admin")){
		response.sendRedirect("landing.jsp");
		return;
	}
	%>
	<h1>Manage:</h1>
	<h2></h2><a href="userList.jsp">Users</a></h2>
	<h2></h2><a href="tagList.jsp">Tags</a></h2>
	<h2></h2><a href="banner.jsp">Login banner</a></h2>
	
	
	</div>
	      <nav id="menu">
         <ul>
            <li><a href="landing.jsp">Home</a></li>
            <li><a href="logout.jsp">Logout</a></li>
         </ul>
      </nav>
</body>
</html>