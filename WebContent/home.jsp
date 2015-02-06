<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home - Flash Mob Learning</title>
</head>
<body>
	<%
		//TODO: Database query
		
		//Session check
		if(session.getAttribute("uid")==null){
			//session invalid
			response.sendRedirect("login.jsp");
		}
	%>
	<p2>Welcome, <%=session.getAttribute("uid") %>!</p2>
	<p2><a href="logout.jsp">logout</a></p2>
</body>
</html>