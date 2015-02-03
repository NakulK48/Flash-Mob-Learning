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
		String userID = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		String login = request.getParameter("login");
		String register = request.getParameter("register");
		//TODO: data base connection
		if (login != null) {
			//TODO: search data base for password
			if (userID.equals("1") && pwd.equals("pwd")) {
				response.sendRedirect("userpage.jsp");
			} else {
				out.println("<font color=red>Either user name or password is wrong.</font>");
			}
		} else if (register != null) {
			//TODO: add password&userID to database
			response.sendRedirect("userpage.jsp");
		} else {
			out.println("<font color=red>Oops something is wrong, blame Matthew.");
		}
	%>
</body>
</html>