<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, java.util.Date"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*, java.sql.*, javax.servlet.http.*, java.net.URLDecoder, java.util.*"%>

<%!
String processRequest(String text, String docID, Long uid, String title, String[] tags) {
	//if("${funct}".equals("save")){ 
	try{
		String docTitle = URLDecoder.decode(title, "UTF-8");
		Date date = new Date();
		User u = LoginManager.getInstance().getUser(uid);
		Long documentID = Long.parseLong(docID);
		Document doc = DocumentManager.getInstance().getDocumentById(documentID);
		User docOwner = doc.owner; 
		if(u.getID() == docOwner.getID()){ //Working on my own WIPDocument or a fork
			if(doc instanceof WIPDocument){
				WIPDocument wipdoc = (WIPDocument) doc;
				wipdoc.setTitle(docTitle);
				String oldContent = wipdoc.getLastRevision().getContent();
				String newContent = URLDecoder.decode(text, "UTF-8");
				Set<Tag> oldTags = wipdoc.getTags();
				
				for(Tag t:oldTags){ //erasing all the tags
					wipdoc.deleteTag(t);
				}
			
 				for (String tag: tags){
 					if(tag.equals("")) continue;
					try{
						wipdoc.addTag(Tag.makeUnbanned(tag));
					}catch(BannedTagException e){
						// Ignore.
					}
				} 
				wipdoc.addRevision(date, newContent);
				return("Save successful");
			}
			else{
				System.out.println("Invalid document type");
				return("Invalid document type");
			}
		} else {
			System.out.println("Invalid permissions");
			return("Invalid permissions");
		}
	}catch(Exception e){
		System.out.println("Error saving: "+e);
		e.printStackTrace();
		return("Something went wrong ! Please try again later");
	}
	//}
	//else return("Unknown function call");
}
%>
<% 
String str = URLDecoder.decode(request.getParameter("tags"), "UTF-8"); 
String [] arr = str.split(",");				
processRequest(URLDecoder.decode(request.getParameter("text"), "UTF-8"), (String) request.getParameter("docID"), (Long) session.getAttribute("uid"), (String) request.getParameter("title"), arr); %>