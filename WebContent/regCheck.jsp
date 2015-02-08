<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Registered</title>
</head>
	<%
		// get request parameters
		String fname = request.getParameter("fn");
		String lname = request.getParameter("ln");
		String pwd = request.getParameter("pwd");
		String rpwd = request.getParameter("rpwd");
		
		if (pwd.length()<8||pwd.length()>22){
			%>
			<jsp:forward page="reg.jsp">
			<jsp:param value="length" name="pwstatus"/>
			</jsp:forward>
			<%
		}else if (!pwd.equals(rpwd)){
			%>
			<jsp:forward page="reg.jsp">
			<jsp:param value="match" name="pwstatus"/>
			</jsp:forward>
			<%
		}
		boolean success = false;
		
		//Data base connection 
		try{
			
			LoginManager l = LoginManager.getInstance();
			System.out.println("successfully invoked login manager");
			User u = l.createUser(fname+" "+lname, pwd, false);
			session.setAttribute("uid",String.valueOf(u.getID()));
			response.sendRedirect("home.jsp");			
		}catch(DuplicateNameException e){
			response.sendRedirect("reg.jsp");
			e.printStackTrace();
		}
	%>
</html>