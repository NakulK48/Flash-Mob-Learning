<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	// Redirect user depending on whether they are logged in.
	if(!(session.getAttribute(Attribute.USERID)==null)) {
		response.sendRedirect("landing.jsp");
		return;
	} else {
		response.sendRedirect("login.jsp");
		return;
	}
%>