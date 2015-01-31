<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hub - Flash Mob Learning</title>
<style>
.heading
{
	width:100px;
	font-size:14pt;
	color:navy;
	text-align:center;
}

#score
{
	width:50px;
}
</style>
</head>
<body>
<table>
	<tr>
		<td class='heading' id='score'>Score</td>
		<td class='heading' id='title'>Title</td>
		<td class='heading' id='submitter'>Submitter</td>
		<td class='heading' id='date'>Age</td>
	</tr>
	<%
		for (int i = 0; i < 5; i++)
		{
			String entry = 
			"<tr>" + 
			"<td>"+i+"</td>" + //score
			"<td>Bob</td>" + //title
			"<td>Bob</td>" + //submitter
			"<td>Bob</td>" + //age
			"</tr>";
			out.println(entry);
		}
	%>
</table>
</body>
</html>