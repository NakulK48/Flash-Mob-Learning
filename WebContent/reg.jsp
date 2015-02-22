<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*,uk.ac.cam.grpproj.lima.flashmoblearning.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>-Register - Flash Mob Learning</title>
<link rel="stylesheet" href="LoginStyle.css" media="screen"
	type="text/css" />
</head>
<body>
	<%
		//Check if user already logged in.
		if(session.getAttribute(Attribute.USERID)!=null){
			response.sendRedirect("home.jsp");
		}
	%>
	<div class="login-card">
	<center><%=LoginManager.getInstance().getLoginBanner() %></center></br>
		<form method="post" action="regCheck.jsp">
			<input type="text" name="username" placeholder="Username" required> 
			<input type="password" name="pwd" placeholder="Password" required> 
			<input type="password" name="rpwd" placeholder="Repeat Password" required>
			<input type="submit" class="login login-submit" value="Register">
		</form>

		<div class="login-help">
			<a href="login.jsp">Login</a>
		</div>
	</div>

	<script
		src='http://codepen.io/assets/libs/fullpage/jquery_and_jqueryui.js'></script>
	
</body>
</html>
