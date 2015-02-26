<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="uk.ac.cam.grpproj.lima.flashmoblearning.*,uk.ac.cam.grpproj.lima.flashmoblearning.database.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Featuring</title>
</head>
<body>
	<%
	if(session.getAttribute(Attribute.USERID)==null){
		//session invalid
		response.sendRedirect("login.jsp");
		return;
	}
	if(session.getAttribute(Attribute.PRIVILEGE)==null||
			!session.getAttribute(Attribute.PRIVILEGE).equals("admin")){
		response.sendRedirect("hub.jsp");
		return;
	}
	String docID = request.getParameter("docID");
	if(docID == null){
		response.sendRedirect("error.jsp");
		return;
	}
	String feature = request.getParameter("feature");
	if(feature == null){
		response.sendRedirect("error.jsp");
		return;
	}
	
	DocumentManager dm = DocumentManager.getInstance();
	Document d = dm.getDocumentById(Long.parseLong(docID));
	if (!(d instanceof PublishedDocument)){
		response.sendRedirect("error.jsp");
		return;
	}
	if(feature.equals("true")){
		((PublishedDocument) d).setFeatured(true);
	} else if (feature.equals("false")) { 
		((PublishedDocument) d).setFeatured(false);
	} else {
		response.sendRedirect("error.jsp");
		return;
	}
	
	response.sendRedirect("hub.jsp");
		
	%>
</body>
</html>