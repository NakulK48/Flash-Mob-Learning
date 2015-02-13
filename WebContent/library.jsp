<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.sql.*, java.util.ArrayList, uk.ac.cam.grpproj.lima.flashmoblearning.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="HubStyle.css">
<%!
 	public void jspInit()
	{
		try
		{
			Database.init();
		}
		
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	} 
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Library - Flash Mob Learning</title>
</head>
<body>
<h1>My Library</h1>
<%
	/*if(session.getAttribute("uid")==null){
		//session invalid
		response.sendRedirect("login.jsp");
	}*/

	
	long uid = 1;//(Long) session.getAttribute("uid");
	//TODO: Remove above placeholder
	LoginManager lm = LoginManager.getInstance();
	DocumentManager dm = DocumentManager.getInstance();
	
	String pageNumberString = request.getParameter("page");
	if (pageNumberString == null) pageNumberString = "1";
	int pageNumber = Integer.parseInt(pageNumberString);
	int previousPage = pageNumber - 1;
	int nextPage = pageNumber + 1;
	if (pageNumber <= 1)
	{
		pageNumber = 1;
		previousPage = 1;
	}

	int offset = (pageNumber - 1) * 25;
	
	User u = lm.getUser(uid);

	QueryParam q = new QueryParam(25, offset, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
	
	ArrayList<WIPDocument> docs = (ArrayList<WIPDocument>) dm.getWorkInProgressByUser(u, q);
	
%>

<table>
	<tr>
		<td class='heading' id='upvoteScoreHeading'></td>
		<td class='heading' id='titleSubmitterHeading'>Title</td>
		<td class='heading' id='ageHeading'>Last edited</td>
	</tr>
	<%
	
		for (WIPDocument doc : docs)
		{

			String ageString;
			int ageInHours = (int) ((System.currentTimeMillis() - doc.creationTime)/3600000);
			if (ageInHours < 1) ageString = "Less than an hour ago";
			else if (ageInHours < 2) ageString = "An hour ago";
			else if (ageInHours < 24) ageString = ageInHours + " hours ago";
			else
			{
				int ageInDays = ageInHours / 24;
				if (ageInDays == 1) ageString = "yesterday";
				else ageString = ageInDays + " days ago";
			}
			
			String entry = 
			"<tr class='lowerRow'>" + 
			"<td></td>" + 
			"<td class='title'> <a href='editor.jsp?id=" + Long.toString(doc.getID()) + "'>" + doc.getTitle() 		+ "</a></td>" + //title
			"<td class='age'>" + ageString + "</td>" + //age
			"</tr>"; 
			
			out.println(entry);
		} 
		
		//dm.deleteAllDocumentsByUser(jimmy);
		//lm.deleteUser(jimmy);
		
		String previousURL = "hub.jsp?page=" + previousPage;
		String nextURL = "hub.jsp?page=" + nextPage;
		
	%>
	
	<tr id="pageHolder">	
	<td id="previous"><a href='<%=previousURL %>'>Previous</a></td>
	<td id="current">Page <%=pageNumber %></td>
	<td id="next"><a href='<%=nextURL %>'>Next</a></td>
	</tr>	
</table>

</body>
</html>