<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>-Register - Flash Mob Learning</title>
</head>
<body>
	<form method="post" action="registration.jsp">
		<center>
			<table border="1" width="30%" cellpadding="3">
				<thead>
					<tr>
						<th colspan="2">Login Here</th>
					</tr>
				</thead>
				<tbody>
					<tbody>
					<tr>
						<td>First Name</td>
						<td><input type="text" name="fn" placeholder="First Name"
							required /></td>
					</tr>
					<tr>
						<td>Last Name</td>
						<td><input type="text" name="ln" placeholder="Last Name"
							required /></td>
					</tr>
					<tr>
						<td>Password</td>
						<td><input type="password" name="pwd" placeholder="Password" required/></td>
					</tr>
					<tr>
						<td><input type="submit" value="Register" /></td>
					</tr>
				</tbody>
			</table>
		</center>
	</form>

</body>
</html>