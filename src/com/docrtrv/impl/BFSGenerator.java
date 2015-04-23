package com.docrtrv.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class BFSGenerator {

	//Main method just for testing purpose
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n=4;
		//File file[] = {"map1.txt", "map2.txt", "map3.txt", "map4.txt" };
		String q ="my query";
		Map<Integer,Double> resultMap = new HashMap<Integer,Double>();
		
		ArrayList<Map<Integer,Double>> dblist = new ArrayList<Map<Integer,Double>>(); 
		File file = new File("temp.txt");
		//for(File f: file){
			Map<Integer,Double> map = new HashMap<Integer,Double>();
			//map = OptDocRetrvAlgorithm.readfile(f);
			//System.out.println("map: " + map);
			dblist.add(map);
		//}
				
		resultMap = processBFSGenerator(n,q,dblist);
		System.out.println("Final BFS Generated List: "+ resultMap);
		
	}
	static Map<Integer, Double> processBFSGenerator(int n,String q,ArrayList<Map<Integer,Double>> dblist)
	{	Map<Integer,Double> resultMap = new HashMap<Integer,Double>();
		Map<Integer,Double> returnMap = new HashMap<Integer,Double>();
		Map<Integer,Double> returnMap_1 = new HashMap<Integer,Double>();
		returnMap.clear();
		// Step 1:
		double min_sim = 1;
		// Step 2:
		// Get the result like sorted map lists such as Map1, Map2, Map3, Map4, Map5
		for(Map<Integer, Double> m:dblist){
		//System.out.println("m: "+m);
		Iterator<Entry<Integer, Double>> it = m.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Integer, Double> pair = (Map.Entry<Integer, Double>)it.next();
	        resultMap.put(pair.getKey(), pair.getValue() );
	        
		}
	    it.remove();
		}
		// Step 3:
		// Arrange all the retrieved documents from database set in order. (According to Global Sim)
		returnMap = OptDocRetrvAlgorithm.sortByComparator(resultMap); 
		
		// Step 4:
		// give the required number of documents
		Iterator<Entry<Integer, Double>> it = resultMap.entrySet().iterator();
		while(n >0 && it.hasNext())
		{   
			Map.Entry<Integer, Double> pair = (Map.Entry<Integer, Double>)it.next();
	        returnMap_1.put(pair.getKey(), pair.getValue() );
	        n--;
	       if(n==0) 
	    	   break;
		}
		it.remove();	
		returnMap_1 = OptDocRetrvAlgorithm.sortByComparator(returnMap_1); 
		return returnMap_1;
	}
}
