<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="refresh" content="3;URL=login.jsp">
<title>Logout</title>
</head>
<body>
	<%
		String username = (String)session.getAttribute("uid");
		if(username!=null)
			session.removeAttribute("uid");
		out.println("You are being logged out!");
	%>
</body>
</html>