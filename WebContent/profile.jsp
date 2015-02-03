<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="uk.ac.cam.grpproj.lima.flashmoblearning.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile - Flash Mob Learning</title>
</head>
<body>
<%
	String userIDString = request.getParameter("id");
	try
	{
		long userID = Long.parseLong(userIDString);	
	}
	catch (NumberFormatException e)
	{
		out.println("<p class='error'>This user doesn't seem to exist!</p>");
		return;
	}
	
	//TODO: Get user from User database. Print error if not found.
	
	String sortType = request.getParameter("sort");
	if (sortType == null ) sortType = "new";
	if (!sortType.equals("new") && !sortType.equals("top")) sortType = "new";
	String capitalisedSortType = sortType.substring(0, 1).toUpperCase() + sortType.substring(1);
	
	//TODO: get documents from DocumentDatabase where user ID is as specified
	
%>

	<div id="orderHolder">
		<a href='<%="profile.jsp?id=" + userIDString + "&sort=top"%>'><div class="order">Top</div></a>
		<a href='<%="profile.jsp?id=" + userIDString + "&sort=new"%>'><div class="order">New</div></a>
	</div>

<h1><% //username %></h1>
<h2><%= capitalisedSortType %> Documents</h2>
<%
	//TODO: output list of documents: upvote button, votes, title, age
%>


</body>
</html>