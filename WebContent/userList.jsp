<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*" %>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User List</title>
</head>
<body>
	<%
		if(session.getAttribute("uid")==null){
			response.sendRedirect("home.jsp");
		}
		if(!((String) session.getAttribute("uid")).equals("1")){
			response.sendRedirect("error.jsp");
		}
		int offset = 0;
		for(int i=offset; i<offset+10; i++){
		}

	%>
</body>
</html>