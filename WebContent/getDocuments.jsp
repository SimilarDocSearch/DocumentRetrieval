
<%@page import="com.docrtrv.models.Document"%>
<%@page import="java.util.*"%>



<%
	int n = Integer.parseInt(request.getParameter("number"));
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
	response.setContentType("application/json");
	response.getWriter().write(strB.toString());
%>