<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*" %>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User List</title>
</head>
<body>
	<%
		if(session.getAttribute("uid")==null){
			//session invalid
			response.sendRedirect("login.jsp");
		}else if(session.getAttribute("privilege")==null||
				!session.getAttribute("privilege").equals("admin")){
			response.sendRedirect("landing.jsp");
		}
		int offset = request.getAttribute("page")==null?0:Integer.parseInt((String) request.getAttribute("page"));
		int limit = 10;
		
		QueryParam qp = new QueryParam(limit, offset);
		
		List<User> users = LoginManager.getInstance().getAllUsers(qp);
		
		
		for(User u:users){
			%>
			
			<%
		}

	%>
</body>
</html>