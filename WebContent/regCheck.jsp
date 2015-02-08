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
		
		if (pwd.length()<10||pwd.length()>22){
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
		/*
		//Data base connection 
		try{
			LoginManager l = LoginManager.getInstance();
			User u = l.createUser(fname+" "+lname, pwd, false);
			session.setAttribute("uid",u.getID());
			response.sendRedirect("home.jsp");
		}catch(DuplicateNameException e){
			%>
			<jsp:forward page="reg.jsp">
			<jsp:param value="dname" name="reg"/>
			</jsp:forward>
			<%
		}catch(Exception e){
			%>
			<jsp:forward page="reg.jsp">
			<jsp:param value="regfail" name="reg"/>
			</jsp:forward>
			<%
			e.printStackTrace();
		}
		
		*/
		//TODO: data base connection
		boolean reg = true;
		String userID = "2";
		if (reg) {
			session.setAttribute("uid", userID);
			response.sendRedirect("home.jsp");
		} else {
			%>
			<jsp:forward page="reg.jsp">
			<jsp:param value="regfail" name="status"/>
			</jsp:forward>
			<%
		}
	%>
</html>