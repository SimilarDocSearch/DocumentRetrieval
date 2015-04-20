package com.docrtrv.scoring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;


public class Query {
	
	public static LinkedHashMap<String, LinkedHashMap<String,Info>> documentmap = new LinkedHashMap<>();
	//hasmap (hashmap) => (documentid -> (term -> Info(freq, di)))
	public static LinkedHashMap<String, Integer> len_d = new LinkedHashMap<>();
	// Storing the len(d) for each document -> The number of terms in document d.
	public static LinkedHashMap<String, LinkedHashMap<String,Info>> querymap = new LinkedHashMap<>();
	//hasmap (hashmap) => (queryid -> (term -> Info(freq,qi)))
	public static LinkedHashMap<String, Integer> len_d_query = new LinkedHashMap<>();
	// Storing the len(d) for each document -> The number of terms in document d.
	public static LinkedHashMap<String, String> termids = new LinkedHashMap<>();
	//term ids for Query terms
	public static LinkedHashMap<String, List> queryid_stemmedlist = new LinkedHashMap<>();
	//Final scores for the document based on each query
	public static LinkedHashMap<String, String> docids = new LinkedHashMap<>();
	//used for tf-idf and bm25
	public static HashMap<String, Double> term_info = new HashMap<String, Double>();
	public static HashMap<String, Double> term_info_term_corpus = new HashMap<String, Double>();
	
	public static double avg_len_d; 
	
	
	public static void main (String[] args) throws IOException {
		LinkedHashMap<String,Info> innerdocmap = new LinkedHashMap<>();
		BufferedReader br = null;
		String sLine = "";
		
		/*Need term ids for Query terms*/
		br = new BufferedReader(new FileReader("term_info.txt"));
		while ((sLine = br.readLine()) != null) {
			String[] parts = sLine.split("\t");
			term_info.put(parts[0], Double.parseDouble(parts[3]));
			term_info_term_corpus.put(parts[0], Double.parseDouble(parts[2]));
		}
		
		/*Need term ids for Query terms*/
		br = new BufferedReader(new FileReader("termids.txt"));
		while ((sLine = br.readLine()) != null) {
			String[] parts = sLine.split("\t");
			termids.put(parts[1], parts[0]);
		}
		
		br = new BufferedReader(new FileReader("docids.txt"));
		
		while ((sLine = br.readLine()) != null) {
			String[] parts = sLine.split("\t");
			docids.put(parts[0], parts[1]);
		}
		
		
		/*take all queries and apply stopping and stemming with terms.*/
		br = new BufferedReader(new FileReader("topics.xml")); //-- change
		StringBuilder html = new StringBuilder();
		while ((sLine = br.readLine()) != null) {
			html.append(sLine);
		}
		
		HashSet <String> hs = new HashSet<String>();
		br = new BufferedReader(new FileReader("stoplist.txt"));
		while ((sLine = br.readLine()) != null) {
			hs.add(sLine);
		}
	
		/*Tokenization -> Stopping -> Stemming*/
		List stemmedlist = new ArrayList<String>();
		SnowballStemmer stemmer = (SnowballStemmer) new englishStemmer();
		List stoppedlist = new ArrayList<String>();
		String regex = "\\w+(\\.?\\w+)*";
		Document doc = Jsoup.parse(html.toString(), "", Parser.xmlParser());
	    for (Element e : doc.select("topic")) {
	        //System.out.println(e.attr("number")+" "+e.child(0).text());
	        List tokenizedlist = texttotokens(e.child(0).text(), regex);
	        for (Object s : tokenizedlist){
	        	if(!hs.contains(s.toString())){
	        		stoppedlist.add(s.toString());
	        		stemmer.setCurrent(s.toString());
					stemmer.stem();
					String stemmedstring = stemmer.getCurrent().toString();
					stemmedlist.add(termids.get(stemmedstring));
					//System.out.println(stemmedstring+" :: "+termids.get(stemmedstring));
	        	}
	        }
	        queryid_stemmedlist.put(e.attr("number"), stemmedlist);
	        stemmedlist = new ArrayList<String>();
	    }
		
	    //System.out.println(queryid_stemmedlist.entrySet());
	    /*Now populate QueryMap*/
	    LinkedHashMap<String,Info> innerquerymap = new LinkedHashMap<>();
	    int len_d_sum_query = 0;
	    for (Entry<String, List> e : queryid_stemmedlist.entrySet()){
	    	HashSet<String> hsdupe = new HashSet<String>();
	    	List l = e.getValue();
	    	for (Object o : l){
	    		boolean b = hsdupe.add(o.toString());
	    		if(b){
	    			Info info = new Info();
	    			info.setTermFreq(1);
	    			innerquerymap.put(o.toString(), info);
	    			len_d_sum_query = len_d_sum_query +1;
	    			
	    		}else{
		    			//innerquerymap = querymap.get(e.getKey());
		    			Info info = innerquerymap.get(o.toString());
		    			info.setTermFreq(info.getTermFreq() + 1);
		    			innerquerymap.put(o.toString(), info);
		    			len_d_sum_query = len_d_sum_query +1;
	    		}
	    		
	    	}
	    	querymap.put(e.getKey(), innerquerymap);
	    	len_d_query.put(e.getKey(), len_d_sum_query);
	    	len_d_sum_query = 0;
	    	innerquerymap = new LinkedHashMap<>();
	    }
	    
	    //System.out.println(querymap.entrySet());
		/* --------------------------------Done Creating a Querymap qi --------------------------*/
		
		/*take all documents (read doc_index.txt) create documentmap using doc_index.txt*/
		br = new BufferedReader(new FileReader("doc_index.txt"));
		String prevdocid = "";
		int len_d_sum = 0;
		while ((sLine = br.readLine()) != null) {
			String[] parts = sLine.split("\t");
			String docid = parts[0];
			if(!docid.equalsIgnoreCase(prevdocid)){
				len_d.put(prevdocid, len_d_sum);
				documentmap.put(prevdocid, innerdocmap);
				innerdocmap = new LinkedHashMap<>();
				len_d_sum = 0;
			}
			
			String termid = parts[1];
			Integer termFreq = parts.length - 2;
			len_d_sum = len_d_sum + termFreq;
			Info info = new Info();
			info.setTermFreq(termFreq);
			innerdocmap.put(termid,info);
			prevdocid = docid;
		}
		//Adding the last docid
		documentmap.put(prevdocid, innerdocmap);
		len_d.put(prevdocid, len_d_sum);
		
		//System.out.println(len_d.size());
		
		/*-----------------------------Done creating Documentmap di ------------------------*/
		
		
		/*------------------------Calculating total terms in corpus------------*/
		float totdocs = len_d.size() - 1;
		/*Create a calculation for avg(len(d))*/
		float totaltemsincorpus = 0.0f;
		for (Entry e : documentmap.entrySet()){
			LinkedHashMap<String,Info> innerdocmaptp = (LinkedHashMap<String, Info>) e.getValue();
			for(Entry e1 : innerdocmaptp.entrySet()){
				Info i = (Info) e1.getValue();
				totaltemsincorpus = totaltemsincorpus + i.getTermFreq();
				//System.out.print(i.getTermFreq()+"\n");
			}
		}
		
		/*------------------------Calculating average len(d)------------*/
		//System.out.println(totaltemsincorpus);
		float average_len_d = totaltemsincorpus/ totdocs;
		avg_len_d = average_len_d;
		//System.out.println(totaltemsincorpus);
		//System.out.println(average_len_d);
		
		/*di calculation starts*/
		for (Entry e : documentmap.entrySet()){
			LinkedHashMap<String,Info> innerdocmaptp = (LinkedHashMap<String, Info>) e.getValue();
			for(Entry e1 : innerdocmaptp.entrySet()){
				Info i = (Info) e1.getValue();
				double numerator = i.getTermFreq();
				double denominator = i.getTermFreq() + 0.5+ 1.5 * (len_d.get(e.getKey())/average_len_d);
				i.setOktf(numerator/denominator);
			}
		}
		
		//System.out.println(documentmap.entrySet());
		
		/*------------------------Calculating total terms in queries------------*/
		float totqueries = len_d_query.size();
		
		/*Create a calculation for avg(len(d))*/
		float totaltemsinqueries = 0.0f;
		for (Entry e : querymap.entrySet()){
			LinkedHashMap<String,Info> innerdocmaptp = (LinkedHashMap<String, Info>) e.getValue();
			for(Entry e1 : innerdocmaptp.entrySet()){
				Info i = (Info) e1.getValue();
				totaltemsinqueries = totaltemsinqueries + i.getTermFreq();
				//System.out.print(i.getTermFreq()+"\n");
			}
		}
		//System.out.println(totaltemsinqueries);
		float average_len_d_query = totaltemsinqueries/ totqueries;
		//System.out.println(average_len_d_query);
		/*qi calculation starts*/
		for (Entry e : querymap.entrySet()){
			LinkedHashMap<String,Info> innerdocmaptp = (LinkedHashMap<String, Info>) e.getValue();
			for(Entry e1 : innerdocmaptp.entrySet()){
				Info i = (Info) e1.getValue();
				double numerator = i.getTermFreq();
				double denominator = i.getTermFreq() + 0.5+ 1.5 * (len_d_query.get(e.getKey())/average_len_d_query);
				i.setOktf(numerator/denominator);
			}
		}
		
		//System.out.println(querymap.entrySet());
		
		/*score(d) calculation starts*/
		documentmap.remove(""); //Removing null key from hashmap
		len_d.remove("");
		/*-----------Calculating norm of vector di and qi-------------*/
		//System.out.println(documentmap.entrySet());
		
		if (args.length == 2 && args[0].equalsIgnoreCase("--score") && args[1].equalsIgnoreCase("TF-IDF")){
			System.out.println("TF-IDF");
			TF_IDF.calculateTF_IDF();
			TF_IDF.calculateTF_IDFScore();
			TF_IDF.write_score_d();
		}else if (args.length == 2 && args[0].equalsIgnoreCase("--score") && args[1].equalsIgnoreCase("BM25")){
			System.out.println("BM25");
			BM25.calculateBM25();
			BM25.write_score_d();
		}else if (args.length == 2 && args[0].equalsIgnoreCase("--score") && args[1].equalsIgnoreCase("Laplace")){
			System.out.println("Laplace");
			LaplaceSmoothing.calculateLaplaceSmoothing();
			LaplaceSmoothing.write_score_d();
		}else{
			System.out.println("check arguments");
		}
				
	}
	

	
	public static List<String> texttotokens(String text, String regex) {
		// TODO Auto-generated method stub
		
		Pattern p = Pattern.compile(regex);
		StringTokenizer st = new StringTokenizer(text.toLowerCase());
		List l = new ArrayList<String>();
		String s1 = "";
		//System.out.println(text);
		while (st.hasMoreTokens()) {
			s1 = "";
			s1 = st.nextToken();
//			/System.out.println(s1);

			Matcher m = p.matcher(s1);
			while (m.find()) {
				String s2 = m.group().toString();
				l.add(s2);
			}
		}
		return l;
	}
	
	   public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
	        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
	     
	        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

	            @Override
	            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
	                return o2.getValue().compareTo(o1.getValue());
	            }
	        });
	     
	        //LinkedHashMap will keep the keys in the order they are inserted
	        //which is currently sorted on natural ordering
	        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
	     
	        for(Map.Entry<K,V> entry: entries){
	            sortedMap.put(entry.getKey(), entry.getValue());
	        }
	     
	        return sortedMap;
	    }
	   



}
