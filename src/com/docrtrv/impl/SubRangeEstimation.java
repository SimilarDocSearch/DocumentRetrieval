package com.docrtrv.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import com.docrtrv.scoring.Runme;
import com.docrtrv.scoring.TfIDF;

public class SubRangeEstimation {
	//Main method just for testing purpose
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 
		ArrayList<String> Arr = new ArrayList<String>();
	}
	public static void SubRangeSimilarityInit(int no_of_Records, File folder, String query, int set) throws IOException, ParseException
	{  	////System.out.println("SubRangeSimilarityInit called..");

		ArrayList<String> file_names = new ArrayList<String>();
		for(int i = 1; i<=6;i++){
			file_names.add(new String("DB_"+i));
			file_names.add(new String("fastSim_"+i));
			file_names.add(new String("Fast_Temp_DB_"+i));
		}
		
	double mnwi=0.0;
	double avg_nwi=0.0;
	double probablity_mnwi = 0.0;	
	double probablity_avg_nwi = 0.0;
	double sd = 0.0;
	int total_number_of_docs = 0;
	int temp = 0;
	int ptr = 0;
	TfIDF tf_idf = new TfIDF();
	List <HashMap<Integer,Double>> dblist = new ArrayList <HashMap<Integer,Double>>(); 
	HashMap<Integer, Double> DB = new HashMap<Integer, Double>();
	HashMap<Integer, Double> SortedDB = new HashMap<Integer, Double>(); 
	ArrayList<String> RepresentativeList = new ArrayList<String>();
	int i = 0;
	 
	for ( File fileEntry : folder.listFiles()) {
        if (fileEntry.isDirectory()) {
        	set++; ptr++;
        	
        	SubRangeSimilarityInit(no_of_Records, fileEntry,query,set);
        		
       
        } else {
        	
        		        	
        	
        	if(!file_names.contains(fileEntry.getName()) )
        	{
        		i++;
        	String Doc = Runme.file_to_String(fileEntry);
//          String Query =Runme.file_to_String(q);
            String Query = query;
            Double sim = tf_idf.cosineSimilarity (true, Doc,Query);
            sim = Math.round( sim * 100.0 ) / 100.0;
       		DB.put(Integer.parseInt(fileEntry.getName().replaceAll(".txt","")), sim);
       		SortedDB = Runme.Arrange_similar_Values(DB); 
       		
       		// Get the maximum normalized weight
       		mnwi = FastSimilairty.getMaxNormalizedWeight(SortedDB);
       		
       		
       		// get the average normalized weight
       		avg_nwi = FastSimilairty.getAvgNormalizedWeight(SortedDB);
       		
       		
       		// get the total number of documents
       		total_number_of_docs = folder.listFiles().length;
       		temp = folder.listFiles().length;
       		////System.out.println("total_number_of_docs"+total_number_of_docs);
       		
       		////System.out.println("SortedDB: "+SortedDB);
       		
       		
        	
    		//}
        	
        	
        	// Generate Database representative for each document in the database
        	String doc_id = fileEntry.getName().replace(".txt","");
        	////System.out.println("file Name: "+fileEntry.getName().replace(".txt",""));
        	
       		probablity_mnwi = count(mnwi, SortedDB) / total_number_of_docs;
       		DecimalFormat df=new DecimalFormat("0.0000");
       		String formate = df.format(probablity_mnwi); 
       		double final_probablity_mnwi = (Double)df.parse(formate) ;
       		////System.out.println("probablity_mnwi: "+final_probablity_mnwi);
       		
       		probablity_avg_nwi = count(avg_nwi, SortedDB) / total_number_of_docs;
       		df=new DecimalFormat("0.000");
       		formate = df.format(probablity_mnwi); 
       		double final_probablity_avg_nwi = (Double)df.parse(formate)*10 ;
       		////System.out.println("final_probablity_avg_nwi: "+final_probablity_avg_nwi);
       		
       		//Calculate standard deviation
       		sd = StandardDevation(SortedDB, avg_nwi);
       		
       		////System.out.println("StandardDevation: "+sd);

        	////System.out.println("mnwi: "+mnwi+"avg_nwi: "+avg_nwi+ " probablity_mnwi: "+final_probablity_mnwi+" final_probablity_avg_nwi: "+final_probablity_avg_nwi+" tandardDevation: "+sd );
       		
       		RepresentativeList.add(doc_id+" mnwi: "+Double.toString(mnwi)
       				              +" avg_nwi: "+Double.toString(avg_nwi)+ " probablity_mnwi: "+Double.toString(final_probablity_mnwi)+
       				              " final_probablity_avg_nwi: "+Double.toString(final_probablity_avg_nwi)+" StandardDevation: "+Double.toString(sd));
        }	        	
        }
        
      
        
        	        
   	}
	
	////System.out.println("--Set: "+set);
	storeDbRepresentative(RepresentativeList, "DB_Representatives_"+Integer.toString(set));
	
	
	}
	static void storeDbRepresentative(ArrayList<String> RepresentativeList, String filename)
	{
		////System.out.println(fileName);
	 	try {
		 FileWriter fstream;
		 BufferedWriter out;
		 fstream = new FileWriter("./WebContent/datasets/DB_Set/"+filename+".txt");
		 out = new BufferedWriter(fstream);
		
		 for (int s = 0; s< RepresentativeList.size(); s++) {	

	        	////System.out.println(RepresentativeList.get(s));
	            out.write(RepresentativeList.get(s));
	            out.newLine();
		        
		       
		    }
		 
		    out.close();
		

	    } catch (IOException ioException) {
	        //System.out.println("\n\n Cannot write to file \"" + filename + "\"");
	    }
	}
	static Double StandardDevation(HashMap<Integer, Double> entries, double average) {
		// TODO Auto-generated method stub
		double sum = 0.0;
		double sd = 0.0;
		
		for(Map.Entry<Integer, Double> entry : entries.entrySet()) 
			{ Integer key = entry.getKey();
			  Double value = entry.getValue();
			  sum += value;
			} 
        
		double mean = sum/entries.size();
        double temp = 0;
        for(Map.Entry<Integer, Double> entry1 : entries.entrySet()) 
			{ Integer key = entry1.getKey();
			  Double value = entry1.getValue();
              temp += (mean-value)*(mean-value);
			}
        return Math.sqrt(temp/entries.size());
		
	}
	static Double count(Double mnwi, HashMap<Integer, Double> DB )
	{  double count = 0.0;
		for(Map.Entry<Integer, Double> entry : DB.entrySet()) {
			  Integer key = entry.getKey();
			  Double value = entry.getValue();
			  if(value.equals(mnwi))
				  count++;
			  
			}
		
		return count;
	}
	public static File[] SubRangeSimilarityProcess(int no_of_Records ) throws ParseException, IOException
		{	ArrayList file_names = new ArrayList<String>();
			ArrayList<File> Arr = new ArrayList<File>();
			for(int p = 1; p<=6;p++){
				file_names.add(new String("DB_"+p));
				file_names.add(new String("fastSim_"+p));
				file_names.add(new String("Fast_Temp_DB_"+p));
			}
			String myfile[] = {"DB_1.txt", "DB_2.txt", "DB_3.txt", "DB_4.txt", "DB_5.txt" };
			String dbRepfile[] = {"DB_Representatives_1.txt", "DB_Representatives_2.txt", "DB_Representatives_3.txt", "DB_Representatives_4.txt", "DB_Representatives_5.txt"};
			File mynewFiles[] = new File[5];
			Arr =	abc("DB");
			for(int i = 0; i<myfile.length;i++)
			{
				if(Arr.toString().contains(myfile[i]))
				{ 	
					int index = getIndex(myfile[i],Arr);
					//System.out.println("index: "+index);
					//System.out.println(Arr.get(index));
					mynewFiles[i] =Arr.get(index);
				}
			}
			for(int i = 0; i<myfile.length;i++)
			{
				//System.out.println("mynewFiles[i]: "+mynewFiles[i]);
			}
			// get rep file locs
			File myrepFiles[] = new File[5];
			Arr =	abc("dbrep");
			for(int i = 0; i<dbRepfile.length;i++)
			{
				if(Arr.toString().contains(dbRepfile[i]))
				{ 	
					int index = getIndex(dbRepfile[i],Arr);
					//System.out.println("index: "+index);
					//System.out.println(Arr.get(index));
					myrepFiles[i] =Arr.get(index);
				}
			}
			for(int i = 0; i<dbRepfile.length;i++)
			{
				//System.out.println("myrepFiles[i]: "+myrepFiles[i]);
			}

		SubRangeSim(no_of_Records, mynewFiles, myrepFiles);
		return mynewFiles;
		
	}
	
	public static int getIndex(String itemName, ArrayList<File> Arr)
	{
	    for (int i = 0; i < Arr.size(); i++)
	    {
	        File a = Arr.get(i);
	        if (itemName.equals(a.getName()))
	        {
	            return i;
	        }
	    } 

	    return -1;
	}
	private static void SubRangeSim(int no_of_Records, File[] myfile,	File[] dbRepfile) throws IOException, ParseException {
		// TODO Auto-generated method stub
		////System.out.println("here");
		String pair[] = new String[2];
		HashMap<Integer,Double> subrangeMap = new HashMap<Integer,Double>();
		// the sim value for each doc
		int i = 0;
		for ( int k = 0; k < myfile.length; k++){  
		File f = myfile[k];
		File db= dbRepfile[k];
		i++;
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			       // process the line.
			    	////System.out.println(line);
			    	pair = line.split(" ");
			    	int doc_no = Integer.parseInt(pair[0]);
			    	double sim_val = Double.parseDouble(pair[1]); 
			    		
			    			try (BufferedReader b = new BufferedReader(new FileReader(db))) {
			    			    String l;
			    			    while ((l = b.readLine()) != null) {
			    			    	String id = l.substring(0,l.indexOf(" ")); 
			    			    	if(id.equals(String.valueOf(pair[0])))
			    			    	{
			    			    		////System.out.println(pair[0]+" present in: "+l);
			    			    		Double mnwi = Double.parseDouble(l.substring(l.indexOf("mnwi:")+6, l.indexOf("avg_nwi")));
			    			    		Double avg_mnwi = Double.parseDouble(l.substring(l.indexOf("avg_nwi:")+9, l.indexOf("probablity_mnwi")));
			    			    		
			    			    		DecimalFormat df=new DecimalFormat("0.00");
			    						String formate = df.format(avg_mnwi); 
			    						 avg_mnwi = (Double)df.parse(formate) ;
			    			    		
			    			    		Double probability = Double.parseDouble(l.substring(l.indexOf("probablity_mnwi:")+17, l.indexOf("final_probablity_avg_nwi:")));
			    			    		Double sim = 1 *((sim_val)* avg_mnwi )/ (mnwi* Math.pow(probability,2));
			    			    		// normalizing similarity between 0 - 1
			    			    		sim = (double)Math.round(sim * 100) / 1000000;
			    			    		 df=new DecimalFormat("0.00");
			    						 formate = df.format(sim); 
			    						sim = (Double)df.parse(formate) ;
			    			    		////System.out.println(sim);
			    			    		subrangeMap.put(doc_no, sim);
			    			    	}
			    			    	
			    			    }
			    			}
			  
			    }
			}
			subrangeMap = Runme.Arrange_similar_Values(subrangeMap);
			Runme.put_sims_in_file(subrangeMap,"SubSim_"+i,i);
			subrangeMap.clear();
		}
		// get the rep for the corresponding doc
		
		
		
	}
	public static ArrayList<String> listFilesForFolder( File folder) {
		ArrayList file_names = new ArrayList<String>();
		ArrayList Arr = new ArrayList<String>();
		for(int p = 1; p<=6;p++){
			file_names.add(new String("DB_"+p));
			file_names.add(new String("fastSim_"+p));
			file_names.add(new String("Fast_Temp_DB_"+p));
		}
		
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            //System.out.println(fileEntry.getName());
	            if(file_names.contains(fileEntry.getName()))
	            {
	            	Arr.add(fileEntry.getName());
	            }
	        }
	    }
		return Arr;
	}
	public static ArrayList<File> abc(String choice)
	{ 
		File folder = null;
		if(choice.equals("dbrep"))
		 folder = new File("./WebContent/datasets/");
		if(choice.equals("DB"))
		 folder = new File("./WebContent/datasets/DB_Set/");
		
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
        //System.out.println(filesList);
        return filesList;
        
	}
	
	
}
