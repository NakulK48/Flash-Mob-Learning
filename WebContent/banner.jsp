<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*,uk.ac.cam.grpproj.lima.flashmoblearning.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Banner - Flash Mob Learning</title>
</head>
<body>
	<%
	//Session check
	if(session.getAttribute(Attribute.USERID)==null){
		//session invalid
		response.sendRedirect("login.jsp");
	}else if(session.getAttribute(Attribute.PRIVILEGE)==null||
			!session.getAttribute(Attribute.PRIVILEGE).equals("admin")){
		response.sendRedirect("landing.jsp");
	}
	%>

	<h2>Change your banner here:</h2>
	<form method="post" action="setBanner.jsp">
		<textarea name = "banner" rows = "8" required><%=LoginManager.getInstance().getLoginBanner() %></textarea>
		<input type="submit" value="Submit">
	</form>
	

</body>
</html>