<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, java.util.Date"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*, java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link type="text/css" href="textpages.css" rel="stylesheet" />

    <!-- Include jQuery.mmenu .css files -->
    <link type="text/css" href="jquery.mmenu.all.css" rel="stylesheet" />

    <!-- Include jQuery and the jQuery.mmenu .js files -->
    <script type="text/javascript" src="jquery.mobile-1.4.5/jquery-2.1.3.min.js"></script>
    <script type="text/javascript" src="jQuery.mmenu-master/src/js/jquery.mmenu.min.all.js"></script>

    <!-- Fire the plugin onDocumentReady -->
    <script type="text/javascript">
       $(document).ready(function() {
          $("#menu").mmenu({
             "slidingSubmenus": false,
             "classes": "mm-white",
             "searchfield": true
          });
       });
    </script>
<%		
		//Session check
	if(session.getAttribute(Attribute.USERID)==null){
		response.sendRedirect("login.jsp");
		return;
	}

	long uid = (Long) session.getAttribute(Attribute.USERID);
	
	if (session.getAttribute(Attribute.DOCTYPE) == null) {
		response.sendRedirect("landing.jsp");
		return;
	}
	DocumentType dt = (DocumentType) session.getAttribute(Attribute.DOCTYPE);

	String documentID = (String) request.getParameter("docID");
	LoginManager l = LoginManager.getInstance();
	User u = l.getUser((String) session.getAttribute(Attribute.USERNAME));
	Long docID = Long.parseLong(documentID);
	Document doc = DocumentManager.getInstance().getDocumentById(docID);
	String title = doc.getTitle();
	String pageType = "Preview";
	if (doc instanceof PublishedDocument){
		pageType = "Published";
	} 	
%>
<title><%=title%> - <%=pageType %></title>
</head >

<body>

<script type="text/javascript"> 

function cloneit(){
	window.location = "fork.jsp?docID=<%=documentID%>";
}

function editit(){
	window.location = "plaintexteditor.jsp?docID=<%=documentID%>&newDoc=0&wipdoc=1";
}

function publishit(){
	window.location ="publish.jsp?docID=<%=documentID%>";
}

//function upvoteit(){
 //<!-- TODO -->
//}

</script>
          <div class="header">
          <a href="#menu"></a>
          Viewer
        </div>

<div>

<h1 id="titlearea">
     <%=doc.getTitle()+" - "+pageType %>
</h1>
<%	boolean hasParent = false;
try{
	String parentTitle=doc.getParentDocument().getTitle();
	hasParent=true;
}catch(Exception e){}%>
<h2 id="parentdoctitle"> <%if(hasParent){%><%="Based on"+ doc.getParentDocument().getTitle() + "."%><%}else%><%=""%></h2>
<p id="bodyarea">
	<%= DocumentManager.getInstance().getRevisionContent(doc.getLastRevision()) %>
</p>
<p id="tagarea">
	Tags : <%= doc.getTags() %>
</p>
<!-- TODO : upvote button qnd upvote count -->
</div>

<div id="buttons" style="padding-left: 40%; padding-right: 30%;">
	<%if(pageType.equals("Preview")){%>
		<button class="fml_buttons" type="button" onclick="editit()"
			style="border-style: none; width:10%; min-width:50px;">Edit</button>
	
		<button class="fml_buttons" type="button" onclick="publishit()"
			style="border-style: none; width:10%; min-width:50px;">Publish</button>
	<%
	}else{
		%><button class="fml_buttons" type="button" onclick="cloneit()"
				style="border-style: none; width:10%; min-width:50px;">Clone</button><%
		String myDoc = request.getParameter("myDoc");
		if(!(myDoc!= null && ((String) myDoc).equals("1"))){
			//Not my document
			%>
			<button class="fml_buttons" type="button" onclick="upvoteit()"
					style="border-style: none; width:10%; min-width:50px;">Upvote</button><%
		}
	}
	%>
</div>



      <!-- The menu -->
      <nav id="menu">
         <ul>
            <li><a href="landing.jsp">Home</a></li>
            <li><a href="CreateNew.jsp?doctype=<%=(dt==DocumentType.SKULPT?"skulpt":"plaintext")%>">New Document</a></li>
            <li><a href="library.jsp">Library</a></li>
            <li><a href="profile.jsp?id=<%=uid%>">My Published Docs</a></li>
            <li><a href="hub.jsp">Community Hub</a></li>
            <li><a href="results.jsp">Search</a></li>
            <li style="padding-top: 140%;"></li>
            <li><a href="logout.jsp">Logout</a></li>
         </ul>
      </nav>

</body> 
 
</html> 