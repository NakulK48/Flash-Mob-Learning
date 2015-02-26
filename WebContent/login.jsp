<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*,uk.ac.cam.grpproj.lima.flashmoblearning.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Login - Flash Mob Learning</title>

<link rel="stylesheet" href="LoginStyle.css" media="screen"
	type="text/css" />

</head>
<body >
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
		if(!(session.getAttribute(Attribute.USERID)==null)){
			response.sendRedirect("landing.jsp");
			return;
		}
	%>
	<img alt="logo" src="fml_logo.png" style="width:100%">
	<div class="login-card" >
	<center><%=LoginManager.getInstance().getLoginBanner()%></center><br>
		<form method="post" action="loginCheck.jsp">
			<input type="text" name="username" placeholder="Username" required> <input
				type="password" name="pwd" placeholder="Password" required> <input
				type="submit" class="login login-submit" value="Login">
		</form>

		<div class="login-help">
			<a href="reg.jsp">Register</a>
		</div>
	</div>

</body>
</html>
