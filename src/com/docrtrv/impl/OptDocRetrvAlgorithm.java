package com.docrtrv.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.Map.Entry;
public class OptDocRetrvAlgorithm {

	//Main method just for testing purpose
	@SuppressWarnings({ "rawtypes", "unchecked" })
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		//int no_of_Records = 7;
		//String file[] = {"map1.txt"};//, "map2.txt", "map3.txt", "map4.txt" };
		String myfile[] = {"DB_1.txt", "DB_2.txt", "DB_3.txt", "DB_4.txt", "DB_5.txt"};//, "DB_6.txt" };
		init(no_of_Records, myfile);
			
	}*/
	
	public static void init(int no_of_Records, String choice)
	{	
		String myfile[] = {"DB_1.txt", "DB_2.txt", "DB_3.txt", "DB_4.txt", "DB_5.txt", "DB_6.txt" };
		String FastSimfile[] = {"fastSim_1.txt", "fastSim_2.txt", "fastSim_3.txt", "fastSim_4.txt", "fastSim_5.txt"};
		String SubSim[] = {"SubSim_1.txt", "SubSim_2.txt", "SubSim_3.txt", "SubSim_4.txt", "SubSim_5.txt"};
		
		if (choice.equals("Cosine"))
			processOptDocRetrv(no_of_Records, myfile);
		
		if (choice.equals("FastSim"))
			processOptDocRetrv(no_of_Records, FastSimfile);
		
		if(choice.equals("SubRangeSim"))
			processOptDocRetrv(no_of_Records, SubSim);
	}
	public static void processOptDocRetrv(int recordsRequired, String file[])
	{
		Map<Integer,Double> merger = new HashMap<Integer,Double>();
		int no_of_Records = recordsRequired;
		double m_asim = 0;
		int ptr = 0;
		Double min = 2.0;
		Double this_min;

		ArrayList<Map<Integer,Double>> dblist = new ArrayList<Map<Integer,Double>>(); 
		for(String f: file){
			Map<Integer,Double> map = new HashMap<Integer,Double>();
			map = OptDocRetrvAlgorithm.readfile(f);
			//System.out.println("map: " + map);
			dblist.add(map);
		}
		//System.out.println("dblist: "+dblist);
		//get m_asim val
		for(Map mp:dblist){
			//System.out.println("m: "+mp);
			ptr++;
			this_min = Collections.max(mp.values());
			
			if(this_min < min && ptr <= 2)
				{
					m_asim = this_min; 
					min =this_min;
				}
		}
		
		//set less similarity value to m_asim
		//System.out.println("final m_asim:"+m_asim);
		
		//access the db's list
		for(Map mp:dblist){
			Iterator<Entry<Integer, Double>> it = mp.entrySet().iterator();
			    while (it.hasNext()) {
			    	//System.out.println("--"+it.next());
			        Map.Entry<Integer, Double> pair = (Map.Entry)it.next();
			        if(pair.getValue() >= (m_asim))
			        { 	//System.out.println("no_of_Records: "+no_of_Records);
			        	if(no_of_Records == 0) 
		        		break;
			        	//System.out.println(pair.getKey() + " = " + pair.getValue());
			        	merger.put(pair.getKey(), pair.getValue() );
			        	no_of_Records--;
			        	
			        	
			        }
	
			        it.remove(); 
			    }
		}
		//System.out.println("Unsorted Map:" +merger);
		Map<Integer, Double> Sortedmerger = sortByComparator(merger); 
		System.out.println("No of Documents Given: "+recordsRequired);
		System.out.println("Sorted Merger: "+ Sortedmerger);

	}
	static Map<Integer,Double> readfile(String file)
	{
		BufferedReader inputStream = null;
		Map<Integer,Double> map = new HashMap<Integer,Double>();
		Map<Integer, Double> newMap = new TreeMap(Collections.reverseOrder());
		try {
		    inputStream = new BufferedReader(new FileReader(file));
		    while (true) {
		        String line = inputStream.readLine();
		        if (line == null) {
		            break;
		        }
		        String pair[]= line.split(" ");
		        map.put(Integer.parseInt(pair[0]),Double.valueOf(pair[1]));
		    
		    }
		    inputStream.close();
		    newMap.putAll(map);
		    return newMap;
    	}
		catch(Exception e){
			System.out.print("No file found");
			}
		return newMap;
		
	}
	
	public static Map<Integer, Double> sortByComparator(Map<Integer, Double> unsortMap) {
		 
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
		Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
		for (Iterator<Map.Entry<Integer, Double>> it = list.iterator(); it.hasNext();) {
			Map.Entry<Integer, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
 
	
	
}

