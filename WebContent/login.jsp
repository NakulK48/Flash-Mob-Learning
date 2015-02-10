<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login - Flash Mob Learning</title>

<link rel="stylesheet" href="LoginStyle.css" media="screen"
	type="text/css" />

</head>
<body>
	<%!
		//init db on login page??
		
		public void jspInit(){
			try{
				Database.init();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	%>
	<%
		//Check if user already logged in.
		if(session.getAttribute("uid")!=null){
			response.sendRedirect("home.jsp");
		}
	%>
	<div class="login-card">
	<center><%=LoginManager.getInstance().getLoginBanner() %></center>
		<form method="post" action="loginCheck.jsp">
			<input type="text" name="username" placeholder="Username" required> <input
				type="password" name="pwd" placeholder="Password" required> <input
				type="submit" class="login login-submit" value="Login">
		</form>

		<div class="login-help">
			<a href="reg.jsp">Register</a>
		</div>
	</div>

	<!-- <div id="error"><img src="https://dl.dropboxusercontent.com/u/23299152/Delete-icon.png" /> Your caps-lock is on.</div> -->

	<script
		src='http://codepen.io/assets/libs/fullpage/jquery_and_jqueryui.js'></script>
</body>
</html>