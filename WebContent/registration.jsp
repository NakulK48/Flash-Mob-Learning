<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
		
		if (pwd!=rpwd){
			%>
			<jsp:forward page="reg.jsp">
			<jsp:param value="pwfail" name="status"/>
			</jsp:forward>
			<%
		}
		
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