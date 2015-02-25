<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, java.util.Date"%>
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
	User u = l.getUser((long) session.getAttribute(Attribute.USERID));

//String body = DocumentManager.getInstance().getRevisionContent(doc.getLastRevision());
%>
	
<script type="text/javascript"> 
// output functions are configurable.  This one just appends some text
// to a pre element.
var mycodemirror;
function loadCodeMirror(){
  mycodemirror = CodeMirror.fromTextArea(document.getElementById("text"), {lineNumbers: false});
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

// Here's everything you need to run a python program in skulpt
// grab the code from your textarea
// get a reference to your pre element for output
// configure the output function
// call Sk.importMainWithBody()


function saveit() {//DOES NOT DO TAGS YET. DOES NOT DO TAGS YET. DOES NOT DO TAGS YET.
	   mycodemirror.save();
	   var mytext = encodeURIComponent(document.getElementById("text").value); 
        jQuery.ajax({
            type: "POST",
            url: "plaintextfunctions.jsp",
            data: {
            	
            	title: encodeURIComponent(document.getElementById('titleBox').value),
      			funct: "save",
                docID: <%=request.getParameter("docID")%>,
        		text: mytext,
        		newDoc: <%=request.getParameter("newDoc")%>
        		
            },
            dataType: "script"
        }).done(function( response ) {
			alert(response);
        }).fail(function(response) { alert("Error")   ; });
}
function previewit() {
	saveit();
	window.location="preview.jsp?docID=<%=request.getParameter("docID")%>&WIPDoc=1&myDoc=1";
}
        

  </script>
  <div class="header">
   <a href="#menu"></a>
          Text Editor
  </div>

        <form action="demo_form.asp" id="tagtitlebox">
        <input type="text" value=<%
    Document document = DocumentManager.getInstance().getDocumentById(Long.parseLong(request.getParameter("docID")));%>"<%=document.getTitle()%>"
    id="titleBox" maxlength="30" placeholder="Title" required><br>
        <input type="text" placeholder="Tags" required><br>
        </form>



    <textarea class="textbox" id="text" ><%if(Integer.parseInt(request.getParameter("newDoc"))!=1){%><%=DocumentManager.getInstance().getRevisionContent(document.getLastRevision())%><%}%></textarea><br /> 

    <!-- complete these buttons-->
			<div id="buttons" style="padding-left: 40%; padding-right: 30%;">
				<button class="fml_buttons" type="button" onclick="saveit()"
					style="border-style: none; background: #00CC66; color: #ff7865; width:10%; min-width:50px;">Save</button>
				<button class="fml_buttons" type="button" onclick="previewit()"
					style="border-style: none; background: #ffdfe0; color: #000000; width:10%; min-width:50px;">Preview</button>
			</div>



      <!-- The menu -->
      <nav id="menu">
         <ul>
            <li><a href="landing.jsp">Home</a></li>
            <li><a href="library.jsp">My Docs</a></li>
            <li><a href="hub.jsp">Community Hub</a></li>
          <div style="padding-top:60%;"><a href="logout.jsp">Logout</a></div>  
         </ul>

      </nav>

</body> 
 
</html> 