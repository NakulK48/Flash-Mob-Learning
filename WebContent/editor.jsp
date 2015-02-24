<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*, java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html> 
<head> 

    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="skulpt.min.js" type="text/javascript"></script>
    <script src="skulpt-stdlib.js" type="text/javascript"></script> 
    <script src="codemirror.js"></script>
    <link rel="stylesheet" href="css/codemirror.css">
    <script src="python.js"></script>
    <link  href="css/demo.css" rel="stylesheet" />

    <!-- Include jQuery.mmenu .css files -->
    <link  href="css/jquery.mmenu.all.css" rel="stylesheet" />

    <!-- Include jQuery and the jQuery.mmenu .js files -->
    <script type="text/javascript" src="jquery-2.1.3.min.js"></script>
    <script type="text/javascript" src="jquery.mmenu.min.all.js"></script>
    
    <script src="jquery.tagsinput.js"></script>
    <script src="jquery-ui.js"></script>
    
	<script src="jquery.taghandler.js"></script>
	<link rel="stylesheet" type="text/css" href="jquery.taghandler.css"/>
    <!-- Fire the plugin onDocumentReady -->
    <script type="text/javascript">
	$(function(){
		$('.tagInputField').autocomplete();
	})
	$(document).ready(function(){

	$("#array_tag_handler").tagHandler({
    assignedTags: [ 'C', 'Perl', 'PHP' ],
    availableTags: [ 'C', 'C++', 'C#', 'Java', 'Perl', 'PHP', 'Python' ],
    autocomplete: true
	});


	})
       $(document).ready(function() {
          $("#menu").mmenu({
             "slidingSubmenus": false,
             "classes": "mm-white",
             "searchfield": true
          });
       });
    </script>
    
    <%!
 	public void jspInit()
	{
		try
		{
			Database.init();
		}
		
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	} 
%>
</head >

<body onload="loadCodeMirror()">
	<script type="text/javascript">
		// output functions are configurable.  This one just appends some text
		// to a pre element.
		
		$(function(){
			$('#tagbox').tagsInput({
				'width':'auto',
				'height':'auto',
				'autocomplete':true
			});
			
		});
		
		var mycodemirror;
		function loadCodeMirror() {
			mycodemirror = CodeMirror.fromTextArea(document
					.getElementById("code"), {
				lineNumbers : true,
				mode : "python"
			});

		}
		setTimeout(function() {
			$('.textbox').css({
				'height' : 'auto'
			});
		}, 50);

		function outf(text) {
			var mypre = document.getElementById("output");
			mypre.innerHTML = mypre.innerHTML + text;
		}
		function builtinRead(x) {
			if (Sk.builtinFiles === undefined
					|| Sk.builtinFiles["files"][x] === undefined)
				throw "File not found: '" + x + "'";
			return Sk.builtinFiles["files"][x];
		}

		// Here's everything you need to run a python program in skulpt
		// grab the code from your textarea
		// get a reference to your pre element for output
		// configure the output function
		// call Sk.importMainWithBody()

		function runit() {
			mycodemirror.save();
			var prog = document.getElementById("code").value;
			var mypre = document.getElementById("output");
			mypre.innerHTML = '';
			Sk.canvas = "mycanvas";
			Sk.pre = "output";
			Sk.configure({
				output : outf,
				read : builtinRead
			});
			try {
				eval(Sk.importMainWithBody("<stdin>", false, prog));
			} catch (e) {
				alert(e.toString())
			}
		}

		function saveit() {

		}
		
		function addTag(){
			var newTag = document.getElementById('tags').value;
			console.log(newTag);
			var selectedTags = document.getElementById('selectedTags');
			var removeTagList = document.getElementById('removeTagList');
			if(selectedTags.innerText.indexOf(newTag) == -1){ // to prevent duplicate Tags
				selectedTags.innerText += " "+newTag;
				removeTagList.options[removeTagList.options.length] = new Option(newTag, newTag);
			}
			
		}
		
		function removeTag(){
			var option = document.getElementById('removeTagList').value;
			$("#removeTagList option[value="+option+"]").remove();
			var selectedTagString = document.getElementById('selectedTags').innerText;
			selectedTagString = selectedTagString.replace(option,'');
			document.getElementById('selectedTags').innerText = selectedTagString;
			
		}
		
	</script>
	<!-- The page -->
	<div class="page">
		<div class="header">
			<a href="#menu"></a> Code Editor
		</div>
			<div style="padding-left: 10%;">
			<label>Title</label>
			<input>
			</br>
<!-- 			<label>Select Tag</label>
			<input type="text" id="tags" list="tagOptions" />
			<datalist id="tagOptions">
			   <select onchange="$('#tags').val(this.value);">
			    <option label="United States" value="USA"></option>
			    <option label="United Kingdom" value="UK"></option>
			    <option label="Uruguay" value="URU"></option>
			    <option label="Brazil" value="BRA"></option>
			    <option label="Russia" value="RUS"></option>
			   </select>
			</datalist>
			<button type="button" onclick="addTag()">Add</button>
			
			</br> -->
			

		<div class="codeEditor">


	<textarea class="textbox" id="code">print "hello world"</textarea>
			<br />



			
			<div id="buttons" style="padding-left: 40%; padding-right: 30%;">
				<button class="fml_buttons" type="button" onclick="runit()"
					style="border-style: none; background: #00CC66; color: #ff7865; width:10%; min-width:50px;">Run</button>
				<button class="fml_buttons" type="button" onclick="saveit()"
					style="border-style: none; background: #7AA3CC;width:10%; min-width:50px;">Save</button>
			</div>
			
			<ul id="array_tag_handler" style="list-style-type:none;"></ul>
			<pre id="output"></pre>
			<!-- If you want turtle graphics include a canvas -->
			<canvas id="mycanvas">
			</mycanvas>
		</div>
	</div>

	<!-- The menu -->
	<nav id="menu">
	<ul>
		<li><a href="landing.jsp">Home</a></li>
		<li><a href="library.jsp">My Docs</a></li>
		<li><a href="hub.jsp">Community Hub</a></li>
		<li style="padding-top: 140%;"></li>
		<li><a href="logout.jsp">Logout</a></li>
	</ul>

	</nav>

</body> 
 
</html> 
