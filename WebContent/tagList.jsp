<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*, java.sql.*,uk.ac.cam.grpproj.lima.flashmoblearning.*, java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>All Tags</title>
</head>
<%!
String entry(String name, boolean banned){
	String ban = banned?"unban":"ban";
	return ("<tr><td>"+name+"</td><td>"+(banned?"<font color=\"red\">Banned</font>":"")+"</td><td><a href=\"tagManager.jsp?tagName="+name+"&action="+ban+"\">"+ban+"</a></td>"
			+"<td><a href=\"tagManager.jsp?tagName="+name+"&action=del\">delete</a></td>"
			+"</tr>");
}
%>
<%
	if (session.getAttribute(Attribute.USERID) == null) {
		//session invalid
		response.sendRedirect("login.jsp");
		return;
	}
	if (session.getAttribute(Attribute.PRIVILEGE) == null
			|| !session.getAttribute(Attribute.PRIVILEGE).equals(
					"admin")) {
		response.sendRedirect("landing.jsp");
		return;
	}
	DocumentManager dm;
	try {
		dm = DocumentManager.getInstance();
	} catch (NotInitializedException e) {
		Database.init();
		RequestDispatcher rd = request.getRequestDispatcher("hub.jsp");
		rd.include(request, response);
		return;
	}
	Set<Tag> tl = dm.getTags();
%>
<body>
<a href="mng.jsp">Back</a>
	<div>
	<center><table border="1" style="width:100%">
	<tr>
		<th>Tag</th>
		<th>Status</th>
		<th></th>
		<th></th>
	</tr>
	
<%
	for(Tag t: tl){
		out.println(entry(t.name,t.getBanned()));
	}
%>
	</table></center>
	</div>


</body>
</html>