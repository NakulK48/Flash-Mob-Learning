<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Manage - Flash Mob Learning</title>
</head>
<body>
	<%
	//Session check
	if(session.getAttribute("uid")==null){
		//session invalid
		response.sendRedirect("login.jsp");
	}else if(session.getAttribute("privaledge")==null||
			!session.getAttribute("privaledge").equals("admin")){
		response.sendRedirect("landing.jsp");
	}
	%>
	<h1>Manage:</h1>
	<h2></h2><a href="logout.jsp">Users</a></h2>
	<h2></h2><a href="banner.jsp">Login banner</a></h2>
	<h2></h2><a href=>Documents</a></h2>
	
	<h2></h2><a href=>Logout</a></h2>
</body>
</html>