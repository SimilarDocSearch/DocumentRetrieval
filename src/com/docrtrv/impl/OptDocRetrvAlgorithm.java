package com.docrtrv.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
public class OptDocRetrvAlgorithm {

	public static String PATH;
	//Main method just for testing purpose
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//int no_of_Records = 7;
	/*	//String file[] = {"map1.txt"};//, "map2.txt", "map3.txt", "map4.txt" };
		String myfile[] = {"DB_1.txt", "DB_2.txt", "DB_3.txt", "DB_4.txt", "DB_5.txt"};//, "DB_6.txt" };
		init(no_of_Records, myfile);
	*/
		/*File file = new File(".\\WebContent\\datasets\\DB_Set\\D1\\DB_1.txt");  
		readfile(file);*/
	}
	public static void Rank_DB(String choice) throws IOException
	{	
		double sum = 0.0;
		Map<Integer,Double> newMap = new HashMap<Integer,Double>();
		Map<Integer,Double> map = new HashMap<Integer,Double>();
		ArrayList<Map<Integer,Double>> dblist = new ArrayList<Map<Integer,Double>>(); 
		String myfiles[] = new String[5];
		if(choice.equals("Cosine")||choice.equals("SubRangeSim"))
		{	for(int p= 0;p<5;p++)
			myfiles[p]="DB_"+(p+1)+".txt";
			//String myfiles[] = {"DB_1.txt","DB_2.txt","DB_3.txt","DB_4.txt","DB_5.txt" };
		}
		
		if(choice.equals("FastSim"))
		{
			for(int p= 0;p<5;p++)
				myfiles[p]="fastSim_"+(p+1)+".txt";
				
			//String myfiles1[] = {"fastSim__1.txt","fastSim__2.txt","fastSim__3.txt","fastSim__4.txt","fastSim__5.txt"};
		}
		File mynewFiles[] = new File[5];
		File folder = new File("./WebContent/datasets/DB_Set/");
		
        File[] listOfSubDirectories = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        ArrayList<File> filesList = new ArrayList<File>();
        for (File dir : listOfSubDirectories) {
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isFile();
                }
            });
            
            for(int f = 0; f< files.length; f++)
            {	//System.out.println("fils[]f"+files[f]);
            	filesList.add(files[f]);
            }
            
        }
        for(int i = 0; i<myfiles.length;i++)
		{
			if(filesList.toString().contains(myfiles[i]))
			{ 	
				int index = SubRangeEstimation.getIndex(myfiles[i],filesList);
				//System.out.println("index: "+index);
				//System.out.println(Arr.get(index));
				mynewFiles[i] =filesList.get(index);
			}
		}
		
        // now access those files and get norm weights
        for(int k= 0;k<5; k++)
        {	//System.out.println(mynewFiles[k]);
        	map =readfile(mynewFiles[k]);
        	
        	dblist.add(map);
        	
        	
        }
        int i = 0;
      //access the db's list
      		for(Map mp:dblist){
      			i++;int count = 5;
      			Iterator<Entry<Integer, Double>> it = mp.entrySet().iterator();
      			    while (it.hasNext()) {
      			    	if(count == 0) break;
      			    	//System.out.println("--"+it.next());
      			        Map.Entry<Integer, Double> pair = (Map.Entry)it.next();
      			        sum = sum +pair.getValue();
      			       	count--;
      			         
      			    }
      			    // store in map
      			    newMap.put(i, sum/5.0);
      			    sum=0.0;
      				it.remove();
      		}
      		// sort in order of rank ..
      		Map<Integer, Double> SortedDB = sortByComparator(newMap);
      		System.out.println("Sorted DB rank:"+SortedDB);
      	//store the ranking order in Rank File..
      		
      		 FileWriter fstream;
			 BufferedWriter out;
			 fstream = new FileWriter("./WebContent/datasets/DB_Set/Rank.txt");
			 out = new BufferedWriter(fstream);
			 Iterator<Entry<Integer, Double>> it = SortedDB.entrySet().iterator();
			    while (it.hasNext()) {
			    	Map.Entry<Integer, Double> pair = (Map.Entry)it.next();
			        String DB = Integer.toString(pair.getKey())+".txt";
			        String val = Double.toString(pair.getValue());
			        out.write(DB+"  "+val);
			        out.newLine();
			    }
			    out.close();
      		        
	}
	
	public static void init(int no_of_Records, String choice, File[] myfile, String path) throws IOException
	{	
		PATH = path;
		Rank_DB(choice);
		
		
		if(myfile == null)
		{   //getFiles();
			ArrayList<File> Arr = new ArrayList<File>();
			String myfiles[] = {"DB_1.txt", "DB_2.txt", "DB_3.txt", "DB_4.txt", "DB_5.txt" };
			/*String FastSimfile[] = {"fastSim_1.txt", "fastSim_2.txt", "fastSim_3.txt", "fastSim_4.txt", "fastSim_5.txt"};
			String SubSim[] = {"SubSim_1.txt", "SubSim_2.txt", "SubSim_3.txt", "SubSim_4.txt", "SubSim_5.txt"};*/
			File mynewFiles[] = new File[5];
			Arr =	SubRangeEstimation.abc("DB");
			for(int i = 0; i<myfiles.length;i++)
			{
				if(Arr.toString().contains(myfiles[i]))
				{ 	
					int index = SubRangeEstimation.getIndex(myfiles[i],Arr);
					//System.out.println("index: "+index);
					//System.out.println(Arr.get(index));
					mynewFiles[i] =Arr.get(index);
				}
			}
			for(int i = 0; i<myfiles.length;i++)
			{
				//System.out.println("mynewFiles[i]: "+mynewFiles[i]);
			}
			
			if (choice.equals("Cosine"))
				{	
					processOptDocRetrv(no_of_Records, mynewFiles);
				}
				
		}
		
		if (choice.equals("FastSim"))
			{
			processOptDocRetrv(no_of_Records,myfile);
			}
		
		if(choice.equals("SubRangeSim"))
			{
			processOptDocRetrv(no_of_Records, myfile);
			}
	}
	public static void processOptDocRetrv(int recordsRequired, File file[]) throws IOException
	{
		Map<Integer,Double> merger = new HashMap<Integer,Double>();
		int no_of_Records = recordsRequired;
		double m_asim = 0;
		int ptr = 0;
		Double min = 2.0;
		Double this_min;

		ArrayList<Map<Integer,Double>> dblist = new ArrayList<Map<Integer,Double>>(); 
		for(File f: file){
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
			        { 	System.out.println("no_of_Records: "+no_of_Records);
			        	if(no_of_Records == 0) 
		        		break;
			        	System.out.println(pair.getKey() + " = " + pair.getValue());
			        	merger.put(pair.getKey(), pair.getValue() );
			        	no_of_Records--;
			        	
			        	
			        }
	
			        it.remove(); 
			    }
		}
		System.out.println("Unsorted Map:" +merger);
		Map<Integer, Double> Sortedmerger = sortByComparator(merger); 
		System.out.println("**************************");
		System.out.println("No of Documents Given: "+recordsRequired);
		System.out.println("Sorted Merger: "+ Sortedmerger);
		
		// give the file locations in a text file
		
			 FileWriter fstream;
			 BufferedWriter out;
			 fstream = new FileWriter(PATH+"/FileLocs.txt");
			 out = new BufferedWriter(fstream);
			  File folder = new File(PATH+"/DB_Set/");
		        File[] listOfSubDirectories = folder.listFiles(new FileFilter() {
		            @Override
		            public boolean accept(File file) {
		                return file.isDirectory();
		            }
		        });
		        
	        ArrayList<File> filesList = new ArrayList<File>();
	        for (File dir : listOfSubDirectories) {
	            File[] files = dir.listFiles(new FileFilter() {
	                @Override
	                public boolean accept(File file) {
	                    return file.isFile();
	                }
	            });

	            for (File f : files) {
	                filesList.add(f);       
	            }
	        }
	        //loop the merger
	        
	        Iterator<Entry<Integer, Double>> it = Sortedmerger.entrySet().iterator();
		    while (it.hasNext()) {
		    	//System.out.println("--"+it.next());
		        Map.Entry<Integer, Double> pair = (Map.Entry)it.next();
		        String aa = Integer.toString(pair.getKey())+".txt";
		        String val = Double.toString(pair.getValue());
		    
		        if(filesList.toString().contains(aa))
				{ 	
					int index = SubRangeEstimation.getIndex(aa,filesList);
					//System.out.println("index: "+index);
					//System.out.println(filesList.get(index)+" sim="+val);
					out.write(filesList.get(index)+" sim="+val);
					out.newLine();
					//myrepFiles[i] =Arr.get(index);
				}
		    }
		    out.close();
		    
	}
	
	
	static Map<Integer,Double> readfile(File f)
	{
	
		//System.out.println(f);
		BufferedReader inputStream = null;
		Map<Integer,Double> map = new HashMap<Integer,Double>();
		Map<Integer, Double> newMap = new TreeMap(Collections.reverseOrder());
		try {
			//System.out.println(f);
		    inputStream = new BufferedReader(new FileReader(f));
			//System.out.println(f);
		    while (true) {
		        String line = inputStream.readLine();
		        if (line == null) {
		            break;
		        }
		        //System.out.println("line:"+line);
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

