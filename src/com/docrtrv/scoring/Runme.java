package com.docrtrv.scoring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.docrtrv.impl.FastSimilairty;
import com.docrtrv.impl.OptDocRetrvAlgorithm;
import com.docrtrv.impl.SubRangeEstimation;

public class Runme {
	
	public static String PATH;
	
	public static void processSearch(String query, int numberOfDocs, String method, String contextPath) throws IOException, ParseException
	{
		String maps[] = new String[6];
			
		// Read the query
		// File Query = new File("F:/Query");
		// read the db sets
		PATH = contextPath;
		 File folder = new File(contextPath);
		 int no_of_Records = numberOfDocs ;
		 
		 if("Cosine".equals(method)) {
		 System.out.println("********  Calculating Cosine similarities  ********");
		 Runme.readFolders(no_of_Records, folder, query, 0, maps);
		 //System.out.println("Calculating opt doc mechnaism..");
		 //OptDocRetrvAlgorithm.init(no_of_Records,"Cosine");
		 }
		 else if ("FastSim".equals(method)){
		 System.out.println("********  Calculating Fast similarities  ********");
		 FastSimilairty.fastSimilarityInit(no_of_Records,  folder, query, 0);
		 File f1[] = new File[5];
		 f1 = FastSimilairty.FastSimilarityprocess(no_of_Records);
		 System.out.println("Calculating opt doc mechanism for fast similarities..");
		 OptDocRetrvAlgorithm.init(no_of_Records,"FastSim", f1, PATH);
		 }
		 else {
		 System.out.println("********  Calculating Sub Range Based similarities  ********");
		 SubRangeEstimation.SubRangeSimilarityInit(no_of_Records, folder, query, 0);
		 File f[] = new File[5];
		 f =SubRangeEstimation.SubRangeSimilarityProcess(no_of_Records);
		 System.out.println("Calculating opt doc mechanism for subrange similarities..");
		 OptDocRetrvAlgorithm.init(no_of_Records,"SubRangeSim",f, PATH);
		 }
	}
	
	public static void main(String args[]) throws IOException, ParseException
	{
		String maps[] = new String[6];
			
		// Read the query
		 //File Query = new File("F:/Query");
			String Query = "This is a grad student and a good student";
		// read the db sets
		 File folder = new File("F:/DocumentSimilarity/DocumentRetrieval/datasets/DB_Set");
		 int no_of_Records = 3 ;
		 
		 System.out.println("Calculating Cosine similarities..");
		 //Runme.readFolders(no_of_Records, folder, Query, 0, maps);
		 
		 		 
		 System.out.println("Calculating Fast similarities..");
		 FastSimilairty.fastSimilarityInit(no_of_Records,  folder, Query, 0);
		 File f1[] = new File[5];
		 f1 = FastSimilairty.FastSimilarityprocess(no_of_Records);
		 System.out.println("Calculating opt doc mechanism for fast similarities..");
		 OptDocRetrvAlgorithm.init(no_of_Records,"FastSim", f1, PATH);
		 
		 /*System.out.println("Calculating Sub Range Based similarities..");
		 //SubRangeEstimation.SubRangeSimilarityInit(no_of_Records, folder, Query, 0);
		 File f[] = new File[5];
		 f =SubRangeEstimation.SubRangeSimilarityProcess(no_of_Records);
		 System.out.println("Calculating opt doc mechanism for subrange similarities..");
		 OptDocRetrvAlgorithm.init(no_of_Records,"SubRangeSim",f);
		 */
		 System.out.println("All Done!!..");
	}
	
	public static void readFolders(int no_of_Records, File folder, String query, int set, String maps[]) throws IOException, ParseException {
		TfIDF tf_idf = new TfIDF();
		List <HashMap<Integer,Double>> dblist = new ArrayList <HashMap<Integer,Double>>(); 
		HashMap<Integer, Double> DB = new HashMap<Integer, Double>();
		HashMap<Integer, Double> SortedDB = new HashMap<Integer, Double>(); 
		int i = 0;
		//File f[] = new File[5];
		ArrayList<String> file_names = new ArrayList<String>();
		for(int p = 1; p<=6;p++){
			file_names.add(new String("DB_"+p));
			file_names.add(new String("fastSim_"+p));
			file_names.add(new String("Fast_Temp_DB_"+p));
		}
		
		for ( File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	set++;
	        	readFolders(no_of_Records, fileEntry,query,set, maps);
	        	 	
	        	//System.out.println("********* Dataset Scanned***********");
	        	try{
	        		if(set == 6)
	        		{	 System.out.println("Opt doc calling..");
	        		
	        			//f= getFiles();
	   		 	 		OptDocRetrvAlgorithm.init(no_of_Records,"Cosine", null, PATH);
	        		}
		   		 }
		   		 catch(NoSuchElementException e)
		   		 {
		   			 System.out.print("");
		   			 
		   		 }
	        	
	        } else {
	        	
	        	if(!file_names.contains(fileEntry.getName()) )
	        	{	        	
	        	i++;
	            String Doc = file_to_String(fileEntry);
	            String Query = query;
	            Double sim = tf_idf.cosineSimilarity (true, Doc,Query);
	            sim = Math.round( sim * 100.0 ) / 100.0;
	       		//System.out.println("Similarity performed..");
	            DB.put(Integer.parseInt(fileEntry.getName().replaceAll(".txt","")), sim);
	            SortedDB = Arrange_similar_Values(DB); 
	        	
	        	
	        	}       	
	        }
	        	        
	   	}
		
		
		 maps[set-1] = "DB_"+set+".txt" ; 
		
		 put_sims_in_file(SortedDB, "DB_"+Integer.toString(set),set);
		 
		 // get the average sims for db for ranking..
		 //rankDb(SortedDB, set);
		 
		 
		 
		 
		 
	}
	
	public static String file_to_String(File file) throws IOException
	{	String Document = "";
		 BufferedReader in = new BufferedReader(new FileReader(file));
         try {
             StringBuilder sb = new StringBuilder();
             String line = in.readLine();

             while (line != null) {
                 sb.append(line);
                 sb.append("\n");
                 line = in.readLine();
             }
            Document = sb.toString();
            return Document;
         } finally {
             in.close();
         }

	}
	public static void put_sims_in_file(HashMap<Integer,Double> map, String fileName, int set)
	{		//System.out.println("put_sims_in_file: "+fileName);
		 	try {
			 FileWriter fstream;
			 BufferedWriter out;
			 fstream = new FileWriter(PATH+"/D"+set+"/"+fileName+".txt");
			 out = new BufferedWriter(fstream);
			 Iterator<Entry<Integer,Double>> it = map.entrySet().iterator();
			 while (it.hasNext()) {	
			        Map.Entry<Integer,Double> pairs = it.next();
		            out.write(pairs.getKey()+" "+pairs.getValue());
		        	out.newLine();
			        
			       
			    }
			 
			    out.close();
			// System.out.println("New similarities to "+fileName +" written.. ");

		    } catch (IOException ioException) {
		        System.out.println("\n\n Cannot write to file \"" + fileName + "\"");
		    }
	}
	
	public static HashMap<Integer, Double> Arrange_similar_Values(HashMap<Integer, Double> unsortMap) {
		 
		// Convert Map to List
		List<Map.Entry<Integer, Double>> list = 
			new LinkedList<Map.Entry<Integer, Double>>(unsortMap.entrySet());
 
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1,
                                           Map.Entry<Integer, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
 
		// Convert sorted map back to a Map
		HashMap<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
		for (Iterator<Map.Entry<Integer, Double>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
}
