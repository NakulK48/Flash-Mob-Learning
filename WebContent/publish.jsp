<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, java.util.Date"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*, java.sql.*, javax.servlet.http.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Publishing...</title>
</head>
<body>
	    <h1>Publishing your document, please wait</h1>
<script>
	<%
		if(session.getAttribute(Attribute.USERID)==null){
			response.sendRedirect("landing.jsp");
			return;
		}
	%>

	<%
		LoginManager l = LoginManager.getInstance();
		User u = l.getUser((String) session.getAttribute(Attribute.USERNAME));
		Long docID = Long.parseLong(request.getParameter("docID"));
		Document d = DocumentManager.getInstance().getDocumentById(docID);
		WIPDocument wipdoc1 = (WIPDocument) d;
		PublishedDocument pubdoc1 = wipdoc1.publish();
		Long pubdocID = pubdoc1.getID();
	    String redirectURL = "successfulpublish.jsp";
	    response.sendRedirect(redirectURL);
	%>

</script>
</body>
</html>