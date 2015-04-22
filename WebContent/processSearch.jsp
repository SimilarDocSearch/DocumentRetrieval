
<%@page import="com.docrtrv.models.Document"%>
<%@page import="java.util.*"%>
<%@page import="com.docrtrv.scoring.Runme"%>



<%
	int n = Integer.parseInt(request.getParameter("number"));
String query = request.getParameter("query");
String method = request.getParameter("method");

Runme.processSearch(query, n, method, getServletContext().getRealPath("\\datasets\\DB_Set"));
	List<Document> docList = new ArrayList<Document>();
	//TODO Get list of documents from optDocRetrv
	
	Document d;
	StringBuilder strB = new StringBuilder();
	String prefix = "";
	strB.append("{ \"docList\":[");
	for(int i=1;i<=n;i++){
	d = new Document();	
	d.setName("document"+i);
	d.setNormWeight((0.57 - (double)(i*2)/10));
	d.setPath("//path-to-file//document"+i);
	d.setSimPerc((80.00 - (double)(i*2)/10));
	
	strB.append(prefix);
	prefix = ",";
	strB.append(d.toString());
	}
	strB.append("] }");
	System.out.println(strB.toString());
	response.setContentType("text/html");
	response.getWriter().write("done");
%>