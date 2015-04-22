
<%@page import="com.docrtrv.models.Database"%>
<%@page import="java.util.*"%>



<%
	//int n = Integer.parseInt(request.getParameter("number"));
	//TODO Get list of documents from optDocRetrv
	
	Database d;
	StringBuilder strB = new StringBuilder();
	String prefix = "";
	strB.append("{ \"rankedDBs\":[");
	for(int i=1;i<=6;i++){
	d = new Database();	
	d.setName("DB"+i);
	d.setRank(i);
	d.setPath("//path-to-file//DB"+i);
	d.setRepresentative("{p = "+ (0.9 - (double)(i/95))+", anw = "+(0.4 - (double)(i/100))+", mnw = "+(0.7 - (double)(i/100)) +", sd = "+(0.16  - (double)(i/100))+" }");
	
	strB.append(prefix);
	prefix = ",";
	strB.append(d.toString());
	}
	strB.append("] }");
	System.out.println(strB.toString());
	response.setContentType("application/json");
	response.getWriter().write(strB.toString());
%>