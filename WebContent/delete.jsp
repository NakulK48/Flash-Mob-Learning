<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, java.util.Date"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*, java.sql.*, javax.servlet.http.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Deleting...</title>
</head>
<body>
	    <h1>Deleting the document, please wait</h1>
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
		String priv = (String) session.getAttribute(Attribute.PRIVILEGE);
		Long docID = Long.parseLong(request.getParameter("docID"));
		Document doc = DocumentManager.getInstance().getDocumentById(docID);
		DocumentType d = doc.docType;
		if(priv.equals("admin")||doc.owner.getID()==((Long) session.getAttribute(Attribute.USERID))){
			//allow admin to delete as well
			DocumentManager.getInstance().deleteDocument(DocumentManager.getInstance().getDocumentById(docID));
		}
	    String redirectURL = "landing.jsp" ;
	    if (d==DocumentType.PLAINTEXT){
	    	redirectURL = "hub.jsp?doctype=plaintext";
	    }else{
	    	redirectURL = "hub.jsp?doctype=skulpt";
	    }
	    response.sendRedirect(redirectURL);
	%>
</script>
</body>
</html>