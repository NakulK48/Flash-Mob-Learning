<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.*"%>
<%@ page import="uk.ac.cam.grpproj.lima.flashmoblearning.database.exception.*"%>
<%@ page import="javax.servlet.*" %>
<%@ page import="java.io.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Modify User</title>
</head>
<body>
	<%
		if(session.getAttribute(Attribute.USERID)==null){
			//session invalid
			response.sendRedirect("login.jsp");
			return;
		}
		if(session.getAttribute(Attribute.PRIVILEGE)==null||
				!session.getAttribute(Attribute.PRIVILEGE).equals("admin")){
			response.sendRedirect("landing.jsp");
			return;
		}
	%>
	<%
		// get request parameters
		String modp = request.getParameter("modifyP");
		String modu = request.getParameter("modifyU");	
		String del = request.getParameter("delete");
		String userID = request.getParameter("uid");
		String name = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		String rpwd = request.getParameter("rpwd");
		if (userID==null){
			response.sendRedirect("error.jsp");
		}
		if ((modu!=null&&modp!=null&&del!=null)||(modu==null&&modp==null&&del==null)){
			response.sendRedirect("error.jsp");
			return;
		}
		if (modp!=null){
			if (pwd.length()<8||pwd.length()>22){
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/userPage.jsp");
				response.getWriter().println("<center><font color=red>Password length must be between 8 and 22 characters.</font></center>");
				rd.include(request, response);
			}else if (!pwd.equals(rpwd)){
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/userPage.jsp");
				response.getWriter().println("<center><font color=red>Password does not match.</font></center>");
				rd.include(request, response);
			}else{
				try{
					
					LoginManager l = LoginManager.getInstance();
					User u = l.getUser(Long.parseLong(userID));
					u.setPassword(pwd);
					if (Long.parseLong(userID)!=((Long)session.getAttribute(Attribute.USERID))){
						RequestDispatcher rd = getServletContext().getRequestDispatcher("/userList.jsp");
						response.getWriter().println("<center><font color=green>User detail successfully updated</font></center>");
						rd.include(request, response);			
						return;
					}else{
						session.setAttribute(Attribute.DOCTYPE, null);
						session.setAttribute(Attribute.USERID, null);
						session.setAttribute(Attribute.USERNAME, null);
						session.setAttribute(Attribute.PRIVILEGE, null);
						RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
						response.getWriter().println("<center><font color=green>User detail successfully updated, please login again</font></center>");
						rd.include(request, response);			
						return;
					}
				}catch(DuplicateEntryException e){
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/userPage.jsp");
					response.getWriter().println("<center><font color=red>Username already exists.</font></center>");
					rd.include(request, response);
					return;
				}
			}
		}else if (modu!=null){
			LoginManager l = LoginManager.getInstance();
			User u = l.getUser(Long.parseLong(userID));
			u.setName(name);
			if (Long.parseLong(userID)!=((Long)session.getAttribute(Attribute.USERID))){
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/userList.jsp");
				response.getWriter().println("<center><font color=green>User detail successfully updated</font></center>");
				rd.include(request, response);			
				return;
			}else{
				session.setAttribute(Attribute.DOCTYPE, null);
				session.setAttribute(Attribute.USERID, null);
				session.setAttribute(Attribute.USERNAME, null);
				session.setAttribute(Attribute.PRIVILEGE, null);
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
				response.getWriter().println("<center><font color=green>User detail successfully updated, please login again</font></center>");
				rd.include(request, response);			
				return;
			}

		}else if (del!=null){
			if (Long.parseLong(userID)!=((Long)session.getAttribute(Attribute.USERID))){
				LoginManager l = LoginManager.getInstance();
				User u = l.getUser(Long.parseLong(userID));
				try{
					l.deleteUser(u);
				}catch(NoSuchObjectException e){
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/userPage.jsp");
					response.getWriter().println("<center><font color=red>User does not exist!</font></center>");
					rd.include(request, response);
					return;
				}
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/userList.jsp");
				response.getWriter().println("<center><font color=green>User: "+u.getName()+" successfully removed!</font></center>");
				rd.include(request, response);			
				return;
			}else{
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/userPage.jsp");
				response.getWriter().println("<center><font color=red> Unable to delete admin account! </font></center>");
				rd.include(request, response);			
			}
		}
	%>	
</body>
</html>