<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>
<%@ page import="javax.servlet.*" %>
<%@ page import="java.io.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Registered</title>
</head>
	<%
		// get request parameters
		String name = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		String rpwd = request.getParameter("rpwd");
		
		if (pwd.length()<8||pwd.length()>22){
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/reg.jsp");
			response.getWriter().println("<center><font color=red>Password length must be between 8 and 22 characters.</font></center>");
			rd.include(request, response);
		}else if (!pwd.equals(rpwd)){
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/reg.jsp");
			response.getWriter().println("<center><font color=red>Password does not match.</font></center>");
			rd.include(request, response);
		}
		
		//Data base connection 
		try{
			LoginManager l = LoginManager.getInstance();
			User u = l.createUser(name, pwd, false);
			session.setAttribute("uid",String.valueOf(u.getID()));
			response.sendRedirect("landing.jsp");			
		}catch(DuplicateEntryException e){
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/reg.jsp");
			response.getWriter().println("<center><font color=red>Username already exists.</font></center>");
			rd.include(request, response);
		}
	%>
</html>