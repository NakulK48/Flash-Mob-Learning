<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, java.util.Date"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*, java.sql.*, javax.servlet.http.*"%>

<%!
String processRequest() {
	if(request.getParameter("funct")=="save"){ // TODO : Error handling
		String docTitle = request.getParameter("titleBox");
		Date date = new Date();
		User u = LoginManager.getInstance().getUser(Long.parseLong((String)session.getAttribute("uid")));
		Long docID = Long.parseLong((String)request.getParameter("docID"));
		Document doc = DocumentManager.getInstance().getDocumentById(docID);
		User docOwner = doc.owner; 
		if(u.getID() == docOwner.getID()){ //Working on my own WIPDocument or a fork
			if(doc instanceof WIPDocument){
				WIPDocument wipdoc = (WIPDocument) doc;
				String oldContent = wipdoc.getLastRevision().getContent();
				if(!oldContent.equals(request.getParameter("text"))){
					wipdoc.addRevision(date, request.getParameter("text"));
					return "OK";
				}
				else{
					return "Already saved";
				}
			}
			else{
				return "Invalid document type";
			}
		} else {
			return "Invalid permissions";
		}
	}



}
%>
<% processRequest(); %>