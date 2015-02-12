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
	if(session.getAttribute("uid")==null){
		//session invalid
		response.sendRedirect("login.jsp");
	}
%>
	
<script type="text/javascript"> 
// output functions are configurable.  This one just appends some text
// to a pre element.
var mycodemirror;
function loadCodeMirror(){
  mycodemirror = CodeMirror.fromTextArea(document.getElementById("text"), {
    lineNumbers: false
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

// Here's everything you need to run a python program in skulpt
// grab the code from your textarea
// get a reference to your pre element for output
// configure the output function
// call Sk.importMainWithBody()
function saveit() { 
   mycodemirror.save();
   var mytext = document.getElementById("text").value; 
   <% 
	LoginManager l = LoginManager.getInstance();
	User u = l.getUser((String) session.getAttribute("username"));
	String docTitle = request.getParameter("titleBox");
	Date date = new Date();
	
	
   DocumentManager.getInstance().createDocument(new Document(-1L, DocumentType.getValue(0), u, docTitle, date.getTime()));
   %>
} 

function publishit() {
	
}


  </script>
  <div class="header">
   <a href="#menu"></a>
          Text Editor
  </div>

        <form action="demo_form.asp" id="tagtitlebox">
        <input type="text" value="Title" id="titleBox" placeholder="Title" required><br>
        <input type="text" value="Tags" placeholder="Tags" required><br>
        <input type="submit" value="Save">
        </form>



    <textarea class="textbox" id="text" ></textarea><br /> 

    <!-- complete these buttons-->
			<div id="buttons" style="padding-left: 40%; padding-right: 30%;">
				<button class="fml_buttons" type="button" onclick="saveit()"
					style="border-style: none; background: #00CC66; color: #ff7865; width:10%; min-width:50px;">Save</button>
				<button class="fml_buttons" type="button" onclick="publishit()"
					style="border-style: none; background: #7AA3CC;width:10%; min-width:50px;">Publish</button>
			</div>



      <!-- The menu -->
      <nav id="menu">
         <ul>
            <li><a href="home.html">Home</a></li>
            <li><a href="#">My Docs</a></li>
            <li><a href="communityHub.html">Community Hub</a></li>
          <div style="padding-top:60%;"><a href="logout.html">Logout</a></div>  
         </ul>

      </nav>

</body> 
 
</html> 