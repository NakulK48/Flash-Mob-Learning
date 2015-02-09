<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*"%>
<%@ page import="javax.servlet.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JSP Page</title>
</head>
<body>
	<%
		// get request parameters
		//String userID = request.getParameter("uid");
		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		
		//TODO: test database.
		try{
			LoginManager l = LoginManager.getInstance();
			User u = l.getUser(username);
			if(u.checkPassword(pwd)){
				session.setAttribute("uid",String.valueOf(u.getID()));
				response.sendRedirect("home.jsp");
			}else{
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
				response.getWriter().println("<center><font color=red>Password/Username does not match.</font></center>");
				rd.include(request, response);			}
		}catch(Exception e){
			response.sendRedirect("err.jsp");
			e.printStackTrace();
		}
	%>
</body>
</html>