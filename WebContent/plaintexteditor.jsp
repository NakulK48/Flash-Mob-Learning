<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, java.util.Date, java.util.Set"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> 
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="codemirror/lib/codemirror.js"></script>
    <link rel="stylesheet" href="codemirror/lib/codemirror.css">
    <link type="text/css" href="textpages.css" rel="stylesheet" />

    <!-- Include jQuery.mmenu .css files -->
    <link type="text/css" href="jquery.mmenu.all.css" rel="stylesheet" />

    <!-- Include jQuery and the jQuery.mmenu .js files -->
    <script type="text/javascript" src="jquery.mobile-1.4.5/jquery-2.1.3.min.js"></script>
    <script type="text/javascript" src="jQuery.mmenu-master/src/js/jquery.mmenu.min.all.js"></script>
    
    <script src="jquery-ui.js"></script>
    
	<script src="jquery.taghandler.js"></script>
	<link rel="stylesheet" type="text/css" href="jquery.taghandler.css"/>
	
    <!-- Fire the plugin onDocumentReady -->
    <script type="text/javascript">

	$(function(){
		$('.tagInputField').autocomplete();
	})
	$(document).ready(function(){
		var availableTags = [];
	<%
	DocumentManager dm = DocumentManager.getInstance();
	Set<Tag> tagList = dm.getTagsNotBanned();
	for(Tag t:tagList){
		out.println("availableTags.push('"+t.name+"');");
	}
	
	String docID = request.getParameter("docID");
    Document document = DocumentManager.getInstance().getDocumentById(Long.parseLong(docID));
    Set<Tag> thisTags = document.getTags();
    
	%>
       $(document).ready(function() {
          $("#menu").mmenu({
             "slidingSubmenus": false,
             "classes": "mm-white",
             "searchfield": true
          });
       });
       
       var currentTags = [];
   	<%
   	for(Tag t : thisTags) {
   		out.println("currentTags.push('"+t.name+"')");
   	}
   	%>
   	$("#array_tag_handler").tagHandler({
   	assignedTags:currentTags,
       availableTags: availableTags,
       autocomplete: true
   	});


   	})

       
       
    </script>
</head >

<body onload="loadCodeMirror()">

<%		
		//Session check
	if(session.getAttribute(Attribute.USERID)==null){
		//session invalid
		response.sendRedirect("login.jsp");
		return;
	}
	LoginManager l = LoginManager.getInstance();
	User u = l.getUser((Long) session.getAttribute(Attribute.USERID));
	
%>
<%
	if(docID==null) {
		response.sendRedirect("error.jsp");
		return;
	}

%>
<script type="text/javascript"> 
// output functions are configurable.  This one just appends some text
// to a pre element.
var mycodemirror;
function loadCodeMirror(){
  mycodemirror = CodeMirror.fromTextArea(document.getElementById("plaintext"), {
	  lineNumbers: true,
	  lineWrapping: true
	});
}
setTimeout(function () {
    $('.textbox').css({
        'height': 'auto'
    });
}, 100);

function builtinRead(x) {
    if (Sk.builtinFiles === undefined || Sk.builtinFiles["files"][x] === undefined)
            throw "File not found: '" + x + "'";
    return Sk.builtinFiles["files"][x];
}

function saveit() {//DOES NOT DO TAGS YET. DOES NOT DO TAGS YET. DOES NOT DO TAGS YET.
	   mycodemirror.save();
	   var mytext = encodeURIComponent(document.getElementById("plaintext").value); 
	   var tags = $('#array_tag_handler').tagHandler("getTags");
        jQuery.ajax({
            type: "POST",
            url: "plaintextfunctions.jsp",
            data: {
            	
            	title: encodeURIComponent(document.getElementById('titleBox').value),
      			funct: "save",
                docID: <%=docID%>,
        		text: mytext,
        		tags:encodeURIComponent(tags)
        		
            },
            dataType: "script"
        }).done(function( response ) {
			//alert(response);
			alert("Save successful");
        }).fail(function(response) { alert("Error")   ; });
}

function previewit() {

	   mycodemirror.save();
	   var mytext = encodeURIComponent(document.getElementById("plaintext").value); 
	   var tags = $('#array_tag_handler').tagHandler("getTags");
        jQuery.ajax({
            type: "POST",
            url: "plaintextfunctions.jsp",
            async: false,
            data: {
            	
            	title: encodeURIComponent(document.getElementById('titleBox').value),
      			funct: "save",
                docID: <%=docID%>,
        		text: mytext,
        		tags:encodeURIComponent(tags)
        		
            },
            dataType: "script"
        }).done(function( response ) {
        	window.location="preview.jsp?WIPDoc=1&myDoc=1&docID=<%=docID%>";
        }).fail(function(response) { alert("Error")   ; });

}


        

  </script>
  <div class="header">
   <a href="#menu"></a>
          Text Editor
  </div>
<div>
        <form id="tagtitlebox">
        <input type="text" value="<%=document.getTitle()%>"
    id="titleBox" maxlength="28" placeholder="Title" style="margin-bottom:10px; font-size:130%" required><br>
        </form>


	
    <textarea class="textbox" id="plaintext" ><%=DocumentManager.getInstance().getRevisionContent(document.getLastRevision())%></textarea><br /> 
	
	<div>
		<ul id="array_tag_handler" style="list-style-type:none; margin-top:10px; margin-bottom:10px"></ul>
	</div>
</div>
	
    <!-- complete these buttons-->
			<div id="buttons" style="padding-left: 40%; padding-right: 30%; min-width:60px;">
				<button class="fml_buttons" type="button" onclick="saveit()"
					style="border-style: none; background: #00CC66; color: #ff7865; width:10%; min-width:50px;">Save</button>
				<button class="fml_buttons" type="button" onclick="previewit()"
					style="border-style: none; background: #ffdfe0; color: #000000; width:10%; min-width:50px;">Preview</button>
			</div>

      <!-- The menu -->
      <nav id="menu">
         <ul>
            <li><a href="landing.jsp">Home</a></li>
            <li><a href="library.jsp">My Documents</a></li>
            <li><a href="hub.jsp">Community Hub</a></li>
          <div style="padding-top:60%;"><a href="logout.jsp">Logout</a></div>  
         </ul>

      </nav>

</body> 
 
</html> 