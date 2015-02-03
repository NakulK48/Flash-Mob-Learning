<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Results - Flash Mob Learning</title>
</head>
<body>
<%
	String searchQuery = request.getParameter("query");
	String searchDomain = request.getParameter("domain"); //tag, document or user.
	if (searchDomain == null) searchDomain = "documents";
%>

	<div id="searchTypesHolder">
		<a href='<%="results.jsp?query=" + searchQuery + "&domain=documents"%>'><div class="searchType">Documents</div></a>
		<a href='<%="results.jsp?query=" + searchQuery + "&domain=users"%>'><div class="searchType">Users</div></a>
		<a href='<%="results.jsp?query=" + searchQuery + "&domain=tags"%>'><div class="searchType">Tags</div></a>
	</div>
	<br>
	
<%
	if (searchQuery == null || searchQuery == "") out.println("Please enter a search query.");
	
	else
	{
		out.println("<p id='query'>Searching " + searchDomain + " for '" + searchQuery + "'</p>");
	}

%>

</body>
</html>