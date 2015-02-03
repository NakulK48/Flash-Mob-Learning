<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login - Flash Mob Learning</title>
</head>
<body>
	<form name="loginForm" action="loginCheck.jsp" method="post">
		<input type ="text" size ="30" name ="id" placeholder="UserID" required=""><br>
		<input type ="password" size = "30" name ="pwd" placeholder="Password" required=""><br>
		<input type ="submit" name="login" value ="login"><br>
		<input type ="submit" name="register" value ="register"><br>
	</form>
</body>
</html>