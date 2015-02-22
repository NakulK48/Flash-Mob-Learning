<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="uk.ac.cam.grpproj.lima.flashmoblearning.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="refresh" content="3;URL=login.jsp">
<title>Logout</title>
</head>
<body>
	<%
		if(session.getAttribute(Attribute.USERID)!=null)
			session.removeAttribute(Attribute.USERID);
		if(session.getAttribute(Attribute.PRIVILEGE)!=null)
				session.removeAttribute(Attribute.PRIVILEGE);
		if(session.getAttribute(Attribute.USERNAME)!=null)
			session.removeAttribute(Attribute.USERNAME);
		out.println("Logout successful, redirecting to login page...");
	%>
</body>
</html>