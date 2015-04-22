package com.docrtrv.impl;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import com.docrtrv.scoring.Runme;
import com.docrtrv.scoring.TfIDF;

public class FastSimilairty {
	
	//Main method just for testing purpose
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		//int no_of_Records; 
		//File folder;
		//File query;
		//performFastSimilarity(no_of_Records,folder,query);
		FastSimilarityprocess(5);
		System.out.println("Fast Sim done..");
		OptDocRetrvAlgorithm.init(5,"FastSim");
		
		
	}
	public static void fastSimilarityInit(int no_of_Records, File folder, String query, int set) throws IOException
	{  	//System.out.println("performFastSimilarity called..");
		double mnwi=0.0;
		double avg_nwi=0.0;
			
		TfIDF tf_idf = new TfIDF();
		List <HashMap<Integer,Double>> dblist = new ArrayList <HashMap<Integer,Double>>(); 
		HashMap<Integer, Double> DB = new HashMap<Integer, Double>();
		HashMap<Integer, Double> SortedDB = new HashMap<Integer, Double>(); 
		int i = 0;
		 
		for ( File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	set++;
	        	fastSimilarityInit(no_of_Records, fileEntry,query,set);
	        	 	
	        
	        } else {
	        	
	        	//for (File q:query.listFiles())
	    		//{ 
	        		        	
	        	i++;
	            String Doc = Runme.file_to_String(fileEntry);
//	            String Query =Runme.file_to_String(q);
	            String Query = query;
	            Double sim = tf_idf.cosineSimilarity (true, Doc,Query);
	            sim = Math.round( sim * 100.0 ) / 100.0;
	       		DB.put(Integer.parseInt(fileEntry.getName().replaceAll(".txt","")), sim);
	       		SortedDB = Runme.Arrange_similar_Values(DB); 
	        	// Get the maximum normalized weight
	        	 mnwi = getMaxNormalizedWeight(SortedDB);
	        	 // get the average normalized weight
	        	 avg_nwi = getAvgNormalizedWeight(SortedDB);
	    		//}
	        	
	        		        	
	        }
	        	        
	   	}
		
		//System.out.println("max mnwi:"+mnwi);
		//System.out.println("avg_nwi:"+avg_nwi);
		
		// put gathered values into givne equation..
		SortedDB.put(1000, mnwi);
		SortedDB.put(1001, avg_nwi);
		
		 Runme.put_sims_in_file(SortedDB, "Fast_Temp_DB_"+Integer.toString(set));
		 
	
		
		
	}
	
	public static void FastSimilarityprocess(int no_of_Records ) throws ParseException
	{
		//System.out.println("FastSimilarityprocess called..");
		// aply the given eqaution on gathered values..
		String myfile[] = {"Fast_Temp_DB_1.txt", "Fast_Temp_DB_2.txt", "Fast_Temp_DB_3.txt", "Fast_Temp_DB_4.txt", "Fast_Temp_DB_5.txt"};
		FastSim(no_of_Records, myfile);
		
		
	}
	public static void FastSim(int recordsRequired, String file[]) throws ParseException
	{	// Main implementation of fast Sim..
		double max_norm =  0.0;
		double avg_norm = 0.0 ;
		double sim = 0.0;
		int i = 0;
		Map<Integer,Double> myData = new HashMap<Integer,Double>();
		HashMap<Integer,Double> tempMap = new HashMap<Integer,Double>();
		for(String f: file){
			i++;
			//Map<Integer,Double> myData = new HashMap<Integer,Double>();
			myData = OptDocRetrvAlgorithm.readfile(f);
			//System.out.println("myData: " + myData);
			for(Map.Entry<Integer, Double> entry : myData.entrySet()) {
				  Integer key = entry.getKey();
				  Double value = entry.getValue();
				  if(key==1000)
					  max_norm = entry.getValue();
				  if(key == 1001)
					  avg_norm = entry.getValue();
				}
			for(Map.Entry<Integer, Double> entry : myData.entrySet()) {
				  Integer key = entry.getKey();
				  Double value = entry.getValue();
				  
				  sim = value * max_norm / avg_norm;
				  DecimalFormat df=new DecimalFormat("0.00");
				  String formate = df.format(sim); 
				  double finalValue = (Double)df.parse(formate) ;
				  //System.out.println(key +"::"+ finalValue);
				  tempMap.put(key, finalValue);
				  
				}
			tempMap = Runme.Arrange_similar_Values(tempMap);
			tempMap.remove(1000);tempMap.remove(1001);
			Runme.put_sims_in_file(tempMap,"fastSim_"+i);
			tempMap.clear();
			myData.clear();
		}
		
		
		
	}
	public static Double getMaxNormalizedWeight(HashMap<Integer, Double> SortedDB)
	{	double max_val = 0.0;
		TreeMap<Integer, Double> myMap = new TreeMap<Integer, Double>();
		myMap.putAll(SortedDB);
		
		for(Map.Entry<Integer, Double> entry : myMap.entrySet()) {
			  Integer key = entry.getKey();
			  Double value = entry.getValue();
			  if(value > max_val)
				  max_val =value;
			}
		
		//System.out.println("Mymap:"+myMap);
		//System.out.println("max val:"+max_val);
		return max_val;
	}
	public static Double getAvgNormalizedWeight(HashMap<Integer, Double> SortedDB)
	{	
		double avg =0.0;
		double sum =0.0;
		TreeMap<Integer, Double> myMap = new TreeMap<Integer, Double>();
		myMap.putAll(SortedDB);
		for(Map.Entry<Integer, Double> entry : myMap.entrySet()) {
			  Integer key = entry.getKey();
			  Double value = entry.getValue();
			  sum = sum + value; 
			  //System.out.println(key + " => " + value);
			}
		avg =sum/myMap.size();
		return avg;
	}
}
