
<%@page import="com.docrtrv.models.Document"%>
<%@page import="java.util.*"%>
<%@page import="com.docrtrv.scoring.Runme"%>



<%
	int n = Integer.parseInt(request.getParameter("number"));
String query = request.getParameter("query");
String method = request.getParameter("method");

Runme.processSearch(query, n, method, getServletContext().getRealPath("\\datasets\\DB_Set"));
	
	response.setContentType("text/html");
	response.getWriter().write("done");
%>