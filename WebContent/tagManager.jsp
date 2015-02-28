<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*, java.sql.*,uk.ac.cam.grpproj.lima.flashmoblearning.*, java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- Include jQuery and the jQuery.mmenu .js files -->
<title>Changing Tags...</title>
</head>
<body>
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
		if (request.getParameter("action") == null) {
			response.sendRedirect("error.jsp");
			return;
		}
		DocumentManager dm;
		try{
			dm = DocumentManager.getInstance();
		}catch(NotInitializedException e){
			Database.init();
			RequestDispatcher rd = request.getRequestDispatcher("hub.jsp");
			rd.include(request, response);
			return;
		}

	%>
	<%
		String tagName = request.getParameter("tagName");
		if (tagName == null) {
			response.sendRedirect("error.jsp");
			return;
		}
		boolean ban = request.getParameter("action").equals("ban") ? true: false;
		boolean unban = request.getParameter("action").equals("unban") ? true: false;
		boolean del = request.getParameter("action").equals("del") ? true: false;
		boolean add = request.getParameter("action").equals("add") ? true: false;
		if (add) {
			try{
				Tag t = dm.createTag(tagName, false);
			}catch(DuplicateEntryException e){
				RequestDispatcher rd = getServletContext().getRequestDispatcher("tagList.jsp");
				response.getWriter().println("<center><font color=red>Add failed: Tag already exists!</font></center>");
				rd.include(request, response);			
				return;
			}catch(NoSuchObjectException e){
				response.sendRedirect("error.jsp");
				return;
			}catch(SQLException e){
				response.sendRedirect("error.jsp");
				return;
			}
		} else if (ban){
			try{
				Tag t = dm.getTag(tagName);
				t.setBanned(true);
			}catch(NoSuchObjectException e){
				RequestDispatcher rd = getServletContext().getRequestDispatcher("tagList.jsp");
				response.getWriter().println("<center><font color=red>Ban failed: Tag does not exist!</font></center>");
				rd.include(request, response);			
				return;
			}catch(SQLException e){
				response.sendRedirect("error.jsp");
				return;
			}
		} else if (del){
			try{
				Tag t = dm.getTag(tagName);
				dm.deleteTagReferences(t);
				dm.deleteTag(t);
			}catch(NoSuchObjectException e){
				RequestDispatcher rd = getServletContext().getRequestDispatcher("tagList.jsp");
				response.getWriter().println("<center><font color=red>Delete failed: Tag does not exist!</font></center>");
				rd.include(request, response);			
				return;
			}catch(SQLException e){
				response.sendRedirect("error.jsp");
				return;
			}
		} else if (unban){
			try{
				Tag t = dm.getTag(tagName);
				t.setBanned(false);
			}catch(NoSuchObjectException e){
				RequestDispatcher rd = getServletContext().getRequestDispatcher("tagList.jsp");
				response.getWriter().println("<center><font color=red>Unban failed: Tag does not exist!</font></center>");
				rd.include(request, response);			
				return;
			}catch(SQLException e){
				response.sendRedirect("error.jsp");
				return;
			}
		}
		response.sendRedirect("tagList.jsp");
	%>
</body>
</html>