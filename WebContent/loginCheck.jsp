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
		//TODO: data base connection
		/*
		//TODO: test database.
		try{
			LoginManager l = LoginManager.getInstance();
			User u = l.getUser(username);
			if(u.checkPassword(pwd)){
				session.setAttribute("uid",u.getID());
				response.sendRedirect("home.jsp");
			}else{
				%>
				<jsp:forward page="login.jsp">
				<jsp:param value="fail" name="status"/>
				</jsp:forward>
				<%
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		*/
		
		
		
				
		if (username.equals("Jamie") && pwd.equals("pwd")) {
			session.setAttribute("uid", "1");
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