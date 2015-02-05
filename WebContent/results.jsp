<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="uk.ac.cam.grpproj.lima.flashmoblearning.*,uk.ac.cam.grpproj.lima.flashmoblearning.database.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Results - Flash Mob Learning</title>
<%!
	public void jspInit()
	{
		try
		{
			Database.init();
		}
		catch (Exception e)
		{
			System.out.println("Something went wrong! Try reloading the page.");
			return;
		}
	}
%>
</head>
<body>
<%
	String searchQuery = request.getParameter("query");
	if (searchQuery == null) searchQuery = "";
	String searchDomain = request.getParameter("domain"); //tag, document or user.
	if (searchDomain == null) searchDomain = "documents";
	if (!searchDomain.equals("documents") && !searchDomain.equals("tags") && !searchDomain.equals("users") ) searchDomain = "documents";
%>

	<div id="searchTypesHolder">
		<a href='<%="results.jsp?query=" + searchQuery + "&domain=documents"%>'><div class="searchType">Documents</div></a>
		<a href='<%="results.jsp?query=" + searchQuery + "&domain=users"%>'><div class="searchType">Users</div></a>
		<a href='<%="results.jsp?query=" + searchQuery + "&domain=tags"%>'><div class="searchType">Tags</div></a>
	</div>
	<br>
	
<%
	if (searchQuery == null || searchQuery.length() == 0) out.println("<p class='error'>Whoops! We need something to search for.</p>");
	
	else
	{
		out.println("<p id='query'>Searching " + searchDomain + " for '" + searchQuery + "'</p>");
	}

	if (searchDomain.equals("documents"))
	{
		//TODO: query Document database for matching titles
	}
	
	else if (searchDomain.equals("users"))
	{
		//TODO: query User database for matching names
	}
	
	else if (searchDomain.equals("tags"))
	{
		//TODO: query Tag database for matching names
	}
%>



</body>
</html>