<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*" %>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>All Users</title>
</head>
<body>
	<center><table border="1" style="width:100%">
	<thead>All Users</thead>
	<tr>
		<th>Name</th>
		<th>ID</th>
	</tr>
	<%
		if(session.getAttribute(Attribute.USERID)==null){
			//session invalid
			response.sendRedirect("login.jsp");
		}else if(session.getAttribute(Attribute.PRIVILEGE)==null||
				!session.getAttribute(Attribute.PRIVILEGE).equals("admin")){
			response.sendRedirect("landing.jsp");
		}
		//TODO: set new limit
		int limit = 5;
		int pageno = request.getParameter("page")==null?0:Integer.parseInt((String) request.getParameter("page"));
		int offset = pageno * limit;
		
		QueryParam qp = new QueryParam(limit+1, offset);
		
		List<User> users = LoginManager.getInstance().getAllUsers(qp);
		
		
		for(int i=0;i<Math.min(users.size(),limit);i++){
			User u = users.get(i);
			Long uid = u.getID();
			%>
				<tr><td><a href="userPage.jsp?uid=<%=uid%>"><%=u.getName() %></a></td><td><%=u.getID()%></td></tr>
			<%
		}

	%>
	</table></center>
	<br>
	<div id="footer">
	<%
		if (pageno != 0) {
			%><div style="bottom:0;float:left;"><a href="userList.jsp?page=<%=pageno-1%>">Previous</a></div><%
		}
	
		if (users.size()==limit+1){
			%><div style="bottom:0;float:right;"><a href="userList.jsp?page=<%=pageno+1%>">Next</a></div><%
		}
	%>
	<div style="bottom:0;"><center>Page <%=pageno+1 %></center></div>
	</div>
	      <nav id="menu">
         <ul>
         	<li><a href="mng.jsp">Back</a>
            <li><a href="landing.jsp">Home</a></li>
            <li><a href="logout.jsp">Logout</a></li>
         </ul>
      </nav>
	
</body>
</html>