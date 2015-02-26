<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, java.util.Date"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*, java.sql.*, javax.servlet.http.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Creating...</title>
</head>
<body>
	    <h1>Creating a new document, please wait</h1>
	<%
		System.out.println("Creating plain text");
		if(session.getAttribute(Attribute.USERID)==null){
			response.sendRedirect("login.jsp");
			return;
		}
	%>

    <%
    try{
		LoginManager l = LoginManager.getInstance();
		User u = LoginManager.getInstance().getUser((Long) session.getAttribute(Attribute.USERID));
		System.out.println(u);
		String type = request.getParameter("doctype");
		Date date = new Date();
		if(type.equals("skulpt")){
			WIPDocument doc = WIPDocument.createDocument(DocumentType.getValue(1), u, "New Document", date.getTime());
			Long docid = doc.getID();
			doc.addRevision(date, "");
		    String redirectURL = "editor.jsp?docID="+Long.toString(docid)+"&newDoc=1&myDoc=1&wipdoc=1" ;
		    response.sendRedirect(redirectURL);
		}
		else if(type.equals("plaintext")){
			WIPDocument doc = WIPDocument.createDocument(DocumentType.getValue(0), u, "New Document", date.getTime());
			Long docid = doc.getID();
			doc.addRevision(date, "");
		    String redirectURL = "plaintexteditor.jsp?docID="+Long.toString(docid)+"&newDoc=1&myDoc=1&wipdoc=1" ;
		    response.sendRedirect(redirectURL);
		}
		else{ 
			out.println("Unknown doctype, please go to a menu and try again");
		}
    }catch(Exception e){
    	e.printStackTrace(); //TODO : Make more useful
    }
	%>
</body>
</html>
