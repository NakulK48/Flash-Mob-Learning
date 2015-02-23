<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, java.util.Date"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*, java.sql.*, javax.servlet.http.*"%>

<%!
String processRequest() {
	if(request.getParameter("funct")=="save"){ 
		String docTitle = request.getParameter("title");
		Date date = new Date();
		try{
		User u = LoginManager.getInstance().getUser(Long.parseLong((String)session.getAttribute("uid")));
		Long docID = Long.parseLong((String)request.getParameter("docID"));
		Document doc = DocumentManager.getInstance().getDocumentById(docID);
		User docOwner = doc.owner; 
		if(u.getID() == docOwner.getID()){ //Working on my own WIPDocument or a fork
			if(doc instanceof WIPDocument){
				WIPDocument wipdoc = (WIPDocument) doc;
				wipdoc.setTitle(docTitle);
				String oldContent = wipdoc.getLastRevision().getContent();
				if(!oldContent.equals(request.getParameter("text"))){
					wipdoc.addRevision(date, request.getParameter("text"));
					return "Save successful";
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
		}catch(Exception e){
			return "Something went wrong ! Please try again later";
		}
	}



}
%>
<% processRequest(); %>