<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JSP Page</title>
</head>
<body>
	<%
		// get request parameters
		String userID = request.getParameter("uid");
		String pwd = request.getParameter("pwd");
		//TODO: data base connection
		if (userID.equals("1") && pwd.equals("pwd")) {
			session.setAttribute("uid", userID);
			response.sendRedirect("home.jsp");
		} else {
			%>
			<jsp:forward page="login.jsp">
			<jsp:param value="fail" name="status"/>
			</jsp:forward>
			<%
		}
	%>
</body>
</html>