<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="uk.ac.cam.grpproj.lima.flashmoblearning.*,uk.ac.cam.grpproj.lima.flashmoblearning.database.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<%
		if(session.getAttribute(Attribute.USERID)==null){
			//session invalid
			response.sendRedirect("login.jsp");
		}else if(session.getAttribute(Attribute.PRIVILEGE)==null||
				!session.getAttribute(Attribute.PRIVILEGE).equals("admin")){
			response.sendRedirect("landing.jsp");
		}
		String uid = request.getParameter("uid");
		Long userID = 0L;
		
		if(uid==null){
			response.sendRedirect("userList.jsp");
		}
		else{
			userID = Long.parseLong(uid);
			User u = LoginManager.getInstance().getUser(userID);
			%>
			<title><%=u.getName() %></title>
			<%
		}
	%>
</head>
<body>

</body>
</html>