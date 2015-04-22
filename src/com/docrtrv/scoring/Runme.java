package com.docrtrv.scoring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

public class Runme {
	
	public static void main(String args[]) throws IOException, ParseException
	{
		String maps[] = new String[6];
			
		// Read the query
		 File Query = new File("F:/Query");
		// read the db sets
		 File folder = new File("F:/DocumentSimilarity/DocumentRetrieval/datasets/DB_Set");
		 int no_of_Records = 5 ;
		 
		 System.out.println("Calculating cosine similarities..");
		 Runme.readFolders(no_of_Records, folder, Query, 0, maps);
		 
		 System.out.println("Calculating Fast similarities..");
		 FastSimilairty.fastSimilarityInit(no_of_Records,  folder, Query, 0);
		 FastSimilairty.FastSimilarityprocess(no_of_Records);
		 OptDocRetrvAlgorithm.init(no_of_Records,"FastSim");
		 
		 System.out.println("Calculating Sub Range Based similarities..");
		 
		 
	}
	
	public static void readFolders(int no_of_Records, File folder, File query, int set, String maps[]) throws IOException {
		TfIDF tf_idf = new TfIDF();
		List <HashMap<Integer,Double>> dblist = new ArrayList <HashMap<Integer,Double>>(); 
		HashMap<Integer, Double> DB = new HashMap<Integer, Double>();
		HashMap<Integer, Double> SortedDB = new HashMap<Integer, Double>(); 
		int i = 0;
		 
		for ( File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	set++;
	        	readFolders(no_of_Records, fileEntry,query,set, maps);
	        	 	
	        	//System.out.println("********* Dataset Scanned***********");
	        try{
	        	if(set == 6)
	   		 {	 System.out.println("Opt doc calling..");
	   		 	 //set = 1;
	   			 OptDocRetrvAlgorithm.init(no_of_Records,"Cosine");
	   		 }
	   		 }
	   		 catch(NoSuchElementException e)
	   		 {
	   			 System.out.print("");
	   			 
	   		 }
	        	
	        } else {
	        	
	        	for (File q:query.listFiles())
	    		{ 
	        		        	
	        	i++;
	            String Doc = file_to_String(fileEntry);
	            String Query =file_to_String(q);
	            Double sim = tf_idf.cosineSimilarity (true, Doc,Query);
	            sim = Math.round( sim * 100.0 ) / 100.0;
	       		//System.out.println("Similarity performed..");
	            DB.put(Integer.parseInt(fileEntry.getName().replaceAll(".txt","")), sim);
	            
	           // System.out.println(i+" "+fileEntry.getName().replaceAll(".txt","")+" "+sim);
	         // sort the Map in descending order of similarity..
	        	 SortedDB = Arrange_similar_Values(DB); 
	        	//System.out.println("Sorted.."+SortedDB);
	    		}
	        	
	        		        	
	        }
	        	        
	   	}
		
		
		 maps[set-1] = "DB_"+set+".txt" ; 
		
		 put_sims_in_file(SortedDB, "DB_"+Integer.toString(set));
		 
		 
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
	public static void put_sims_in_file(HashMap<Integer,Double> map, String fileName)
	{		//System.out.println(fileName);
		 	try {
			 FileWriter fstream;
			 BufferedWriter out;
			 fstream = new FileWriter(fileName+".txt");
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
