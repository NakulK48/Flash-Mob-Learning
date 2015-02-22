<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.io.PrintWriter"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<%
	//Session check
	if(session.getAttribute("uid")==null){
		//session invalid
		response.sendRedirect("login.jsp");
	}else if(session.getAttribute("privaledge")==null||
			!session.getAttribute("privaledge").equals("admin")){
		response.sendRedirect("landing.jsp");
	}
	%>
	
	<%
		String newBanner = request.getParameter("banner");
		LoginManager.getInstance().setLoginBanner(newBanner);
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/mng.jsp");
		response.getWriter().println("<center><font color=green>Banner successfully updated</font></center>");
		rd.include(request, response);			
	%>
</body>
</html>