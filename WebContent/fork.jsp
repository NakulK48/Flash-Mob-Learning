<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, java.util.Date"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*, java.sql.*, javax.servlet.http.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Forking...</title>
</head>
<body>
	    <h1>Cloning the document, please wait</h1>
<script>
	<%
		if(session.getAttribute(Attribute.USERID)==null){
			response.sendRedirect("landing.jsp");
			return;
		}
	%>

    <%
		LoginManager l = LoginManager.getInstance();
		User u = LoginManager.getInstance().getUser((Long) session.getAttribute(Attribute.USERID));
		Long docID = Long.parseLong(request.getParameter("docID"));
		Document doc = DocumentManager.getInstance().getDocumentById(docID);
		DocumentType dt = doc.docType;
		PublishedDocument pubdoc = (PublishedDocument) DocumentManager.getInstance().getDocumentById(docID);
		WIPDocument wipdoc = pubdoc.fork(u);
		Long wipdocid = wipdoc.getID();
		
	    String redirectURL = (dt==DocumentType.PLAINTEXT?"plaintexteditor.jsp":"editor.jsp")+"?docID="+Long.toString(wipdocid)+"&newDoc=0&myDoc=1&wipdoc=1" ;
	    response.sendRedirect(redirectURL);
	%>
</script>
</body>
</html>