<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="javax.servlet.*,java.sql.*, java.util.ArrayList, uk.ac.cam.grpproj.lima.flashmoblearning.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.*, uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*" %>

<!DOCTYPE html>
<html>
   <head>
		<%	//get attributes/parameters
			if(session.getAttribute(Attribute.USERID)==null){
				response.sendRedirect("login.jsp");
				return;
			}
						
			long uid = (Long) session.getAttribute(Attribute.USERID);
			DocumentType dt = DocumentType.ALL;
		
			if (request.getParameter("doctype") != null)
			{
				String doctypeString = request.getParameter("doctype");
				if (doctypeString.equals("skulpt")) dt = DocumentType.SKULPT;
				else dt = DocumentType.PLAINTEXT;
				session.setAttribute(Attribute.DOCTYPE, dt);
			}
			if (session.getAttribute(Attribute.DOCTYPE) == null) {
				response.sendRedirect("landing.jsp");
				return;
			}
			dt = (DocumentType) session.getAttribute(Attribute.DOCTYPE);
		%>
      	<title>Hub - Flash Mob Learning</title>
      	<meta charset="utf-8" />
      	<meta name="viewport" content="width=device-width, initial-scale=1">
      <link type="text/css" href="css/demo.css" rel="stylesheet" />

      <!-- Include jQuery.mmenu .css files -->
      <link type="text/css" href="css/jquery.mmenu.all.css" rel="stylesheet" />

      <!-- Include jQuery and the jQuery.mmenu .js files -->
      <script type="text/javascript" src="jquery-2.1.3.min.js"></script>
      <script type="text/javascript" src="jquery.mmenu.min.all.js"></script>
      

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
      <link rel="stylesheet" type="text/css" href="css/HubStyle.css">

   </head>
   <body>

      <!-- The page -->
      <div class="page">
         <div class="header">
            <a href="#menu"></a>
            <%=dt==DocumentType.SKULPT?"Skulpt - ":"Text - " %>Community Hub
         </div>
         <div class="content" style="padding-top:10px;">
<%! //Outputs a single entry
	String entry(PublishedDocument d, int page, String sort, ArrayList<Long> upvoted, long uid){
		String ageString;
		int ageInHours = (int) ((System.currentTimeMillis() - d.creationTime)/3600000);
		if (ageInHours < 1) ageString = "Less than an hour ago";
		else if (ageInHours < 2) ageString = "An hour ago";
		else if (ageInHours < 24) ageString = ageInHours + " hours ago";
		else
		{
			int ageInDays = ageInHours / 24;
			if (ageInDays == 1) ageString = "yesterday";
			else ageString = ageInDays + " days ago";
		}		
		String upvoteLink = "<a href='hub.jsp?page=" + page + "&sort=" + sort + "&upvote=" + Long.toString(d.getID()) + "'>";
		String upvoteEndLink = "</a>";
		String upvoteImage = "UpvoteNormal.png";
		if (upvoted.contains(d.getID())) {
			upvoteImage = "UpvoteEngaged.png";
			upvoteLink = "";
			upvoteEndLink = "";
		}
		String entry = 
		"<tr class='upperRow'>" + 
		"<td class='upvote'>" + upvoteLink + " <img src='" + upvoteImage + "'>"+upvoteEndLink+"</td>" + //upvote
		//TODO: Replace with upvote sprite
		//TODO: JavaScript to change upvote sprite and increment score locally on upvote.
		"<td class='title'> <a href="+(d.docType==DocumentType.SKULPT?"'editor.jsp":"'preview.jsp")+"?docID=" + Long.toString(d.getID())+(uid==d.owner.getID()?"&myDoc=1":"")+ "'>" + HTMLEncoder.encode(d.getTitle()) + "</a></td>" + //title
		"<td class='age'>" + ageString + "</td>" + //age
		"</tr>" + 
		"<tr class='lowerRow'>" +
		"<td id='score" + Long.toString(d.getID()) + "' class='votes'>" + d.getVotes()	+ "</td>" + //score
		"<td class='submitter'> <a href='profile.jsp?id=" + Long.toString(d.owner.getID()) + "'>" + (uid==d.owner.getID()?"me":HTMLEncoder.encode(d.owner.getName()))+ "</a></td>" + //submitter
		"<td></td>" +
		"</tr>"; 
		return entry;		
	}
%>
<%
	DocumentManager dm;
	LoginManager lm;
	try{
		dm = DocumentManager.getInstance();
		lm = LoginManager.getInstance();
	}catch(NotInitializedException e){
		Database.init();
		RequestDispatcher rd = request.getRequestDispatcher("hub.jsp");
		rd.include(request, response);
		return;
	}
	User thisUser = lm.getUser(uid);
	String upvoted = request.getParameter("upvote"); //specifies which document to upvote
	if (upvoted != null) {
		long thisDocumentID = Long.parseLong(upvoted);
		PublishedDocument thisDocument = (PublishedDocument) dm.getDocumentById(thisDocumentID);
		try {
			dm.addVote(thisUser, thisDocument);
		}
		catch (DuplicateEntryException e) {
			// Already voted
		}
	}
	
	String showFeatured = request.getParameter("showFeatured");
	if (showFeatured == null) showFeatured = "true";
			
	String sortType = request.getParameter("sort");
	String pageNumberString = request.getParameter("page");
	if (pageNumberString == null) pageNumberString = "1";
	int pageNumber = Integer.parseInt(pageNumberString);
	int previousPage = pageNumber - 1;
	int nextPage = pageNumber + 1;
	if (pageNumber <= 1)
	{
		pageNumber = 1;
		previousPage = 1;
	}
	
	int limit = 25;
	int offset = (pageNumber - 1) * limit;
	
	if (sortType == null) sortType = "hot";
	if (!sortType.equals("new") && !sortType.equals("top") && !sortType.equals("hot")) sortType = "hot";
	
	QueryParam p;
	QueryParam pf;
	
	int featuredCount = 5;
	if (showFeatured.equals("only")) featuredCount = limit;
	
	if (sortType.equals("new"))
	{
		p = new QueryParam(limit+1, offset, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
		pf = new QueryParam(featuredCount+1, offset, QueryParam.SortField.TIME, QueryParam.SortOrder.DESCENDING);
	}
	
	else if (sortType.equals("top"))
	{
		p = new QueryParam(limit+1, offset, QueryParam.SortField.VOTES, QueryParam.SortOrder.DESCENDING);
		pf = new QueryParam(featuredCount+1, offset, QueryParam.SortField.VOTES, QueryParam.SortOrder.DESCENDING);
	}
	
	else // "hot"
	{
		sortType = "hot"; // XSS: In case somebody is messing with it...
		p = new QueryParam(limit+1, offset, QueryParam.SortField.POPULARITY, QueryParam.SortOrder.DESCENDING);
		pf = new QueryParam(featuredCount+1, offset, QueryParam.SortField.POPULARITY, QueryParam.SortOrder.DESCENDING);
	}
	
	ArrayList<PublishedDocument> featuredSubs = new ArrayList<PublishedDocument>();
	ArrayList<PublishedDocument> subs = new ArrayList<PublishedDocument>();

	if (pageNumber == 1 && (showFeatured.equals("true") || showFeatured.equals("only"))) 
	{
		featuredSubs = (ArrayList<PublishedDocument>) DocumentManager.getInstance().getFeatured(dt, p);
	}
	else if(showFeatured.equals("only")){
		featuredSubs = (ArrayList<PublishedDocument>) DocumentManager.getInstance().getFeatured(dt, p);
	}
	
	if (!showFeatured.equals("only"))
	{
		subs = (ArrayList<PublishedDocument>) DocumentManager.getInstance().getPublished(dt, p);
	}

%> 
	<div id="orderHolder"  >
		<div id="inner">
			<div class="order" ><a href='<%="hub.jsp?sort=hot"%>'>Hot</a></div>	
			<div class="order" ><a href='<%="hub.jsp?sort=top"%>'>Top</a></div>
			<div class="order" ><a href='<%="hub.jsp?sort=new"%>'>New</a></div>
			
		</div>

	</div>
	<style>
		#inner{width:200px; display:block; margin:0 auto;}
		.order{float:left; padding-right:6%; width:50px}		
	</style>


	<table>
	<tr>
		<td class='heading' id='upvoteScoreHeading'></td>
		<td class='heading' id='titleSubmitterHeading'></td>
		<td class='heading' id='ageHeading'></td>
	</tr>
	<%
		if (pageNumber == 1) 
		{
			out.println("<tr><th>Featured</th></tr>");
			if (showFeatured.equals("true"))
			{
				%>
				<tr>
				<td><a href = 'hub.jsp?sort=<%=sortType%>&showFeatured=false'>(Hide) </a></td>
				<td><a href = 'hub.jsp?sort=<%=sortType%>&showFeatured=only'> (View More)</a></td>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<%
			}
			else if (showFeatured.equals("false"))
			{
				%>
				<tr><td><a href = 'hub.jsp?sort=<%=sortType%>&showFeatured=true'>(Show) </a></td></tr>
				<tr><td>&nbsp;</td></tr>
				<%
			}
			else //only.
			{
				%>
				<tr><td><a href = 'hub.jsp?sort=<%=sortType%>&showFeatured=false'>(Hide) </a></td></tr>
				<tr><td>&nbsp;</td></tr>
				<%
			}

		}
		//TODO list featured documents
		
		ArrayList<Long> upvotedDocuments = (ArrayList<Long>) dm.hasUpvoted(thisUser, subs);
		
		for (int i = 0; i < featuredSubs.size() && i < featuredCount; i++) {
			PublishedDocument pd  = featuredSubs.get(i);
			out.println(entry(pd, pageNumber, sortType, upvotedDocuments, uid));
		} 
		

		if (pageNumber == 1 && !showFeatured.equals("only")){
			%>
			<tr><th>The Rest</th></tr>
			<tr><td>&nbsp;</td></tr>
			<%
		}
		
		for (int i = 0; i < subs.size() && i < limit; i++){
			PublishedDocument pd = subs.get(i);
			out.println(entry(pd, pageNumber, sortType, upvotedDocuments, uid));
		} 
		boolean hasNext = subs.size() > limit||(showFeatured.equals("only")&&featuredSubs.size()>featuredCount);
		boolean hasPre = pageNumber!=1;
		
		String previousURL = "hub.jsp?sort=" + sortType + "&page=" + previousPage + "&showFeatured=" + showFeatured;
		String nextURL = "hub.jsp?sort=" + sortType + "&page=" + nextPage + "&showFeatured=" + showFeatured;
		
	%>


</table>
	<script>
	$(window).bind("load", function() { 
	    
	    var footerHeight = 0,
	        footerTop = 0,
	        $footer = $("#footer");
	        
	    positionFooter();
	    
	    function positionFooter() {
	    
	             footerHeight = $footer.height();
	             footerTop = ($(window).scrollTop()+$(window).height()-footerHeight)+"px";
	    
	            if ( ($(document.body).height()+footerHeight) < $(window).height()) {
	                $footer.css({
	                     position: "absolute"
	                }).animate({
	                     top: footerTop
	                })
	            } else {
	                $footer.css({
	                     position: "static"
	                })
	            }
	            
	    }
	
	    $(window)
	            .scroll(positionFooter)
	            .resize(positionFooter)
	            
	});
	</script>
	<div class="footer fixed"  >	
		<div id="inner">
			<div id = "previousLink" class="footerElem">
			<%
			if (hasPre){
			%><a href='<%=previousURL %>'>Previous</a><%
			}else{
			%>Previous<%	
			}
			%>
			</div>
			<div id = "pageNumber" class="footerElem">Page <%=pageNumber %></div>
			<div id = "nextLink" class="footerElem">
			<%
			if (hasNext){
			%><a href='<%=nextURL %>'>Next</a><%
			}else{
			%>Next<%	
			}
			%>
			</a></div>	
		</div>
	</div>	

	
	<style>
		
		#footer { height: 100px}	
		#inner{width:200px; display:block; margin:0 auto;}
		.footerElem{float:left; padding-right:3%}
	</style>
         </div>
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