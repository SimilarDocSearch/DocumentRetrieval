package com.docrtrv.searchengine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BM25Implementation 
{
	double k1;
	double k2;
	double b;
	HashMap<String,Double> DocumentsAndAverages = new HashMap<String,Double>();
	HashMap<String,HashMap<String,Integer>> WordsDocumentsAndFrequencies = new HashMap<String,HashMap<String,Integer>>();
	BufferedReader reader;
	static String word;
	Integer TotalDocuments;
	HashMap<String,Double> DocumentScores = new HashMap<String,Double>();
	String[] DocumentIds;
	static Integer MaximumResults;
	static Integer queryId = 1;
	//PrintWriter resultsFile = null;
	
	public BM25Implementation()
	{
		k1 = 1.2;
		k2 = 100;
		b = 0.75;
	}
	
	public void ComputeAverageForDocuments(String fileName)
	{
		try
		{
			reader = new BufferedReader(new FileReader(fileName));
			String line = null;
			while((line = reader.readLine()) != null)
			{
				String[] tokens = line.split(":");
				String documentId = tokens[0];
				Double average = Double.parseDouble(tokens[1]);
				DocumentsAndAverages.put(documentId, average);
			}
			Object[] objectArray = DocumentsAndAverages.keySet().toArray();
			DocumentIds = Arrays.copyOf(objectArray, objectArray.length, String[].class);
			TotalDocuments = DocumentsAndAverages.keySet().toArray().length; //total number of documents
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}

		}
		
	}
	
	public void ComputeDocumentsAndTheWordFrequencies(String fileName)
	{
		try
		{
			reader = new BufferedReader(new FileReader(fileName));

			String line = null;
			HashMap<String,Integer> documentAndFreq = null;
			while((line = reader.readLine()) != null)
			{
				String[] tokens = line.split("\\s+"); //split on space

				if(tokens[0].equals("#"))
				{
					word = tokens[1];
					documentAndFreq = new HashMap<String,Integer>();
				}
				else if(tokens[0] != "#")
				{
					HashMap<String,Integer> currentEntryForWord = WordsDocumentsAndFrequencies.get(word);
					String documentId = tokens[0];
					Integer frequency = Integer.parseInt(tokens[1]);
					if(currentEntryForWord == null)
					{
						documentAndFreq.put(documentId, frequency);
					}
					else
					{
						Integer getfreq = documentAndFreq.get(documentId);
						if(getfreq == null)
						{
							documentAndFreq.put(documentId, frequency);
						}
					}
					WordsDocumentsAndFrequencies.put(word, documentAndFreq);
				}

			}
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}

		}

		
	}
	
	public void ComputeScoreForDocuments(String fileName)
	{
		try
		{
			reader = new BufferedReader(new FileReader(fileName));
		   // resultsFile = new PrintWriter(new BufferedWriter(new FileWriter("results.txt", true)));
			String line = null;
			while((line = reader.readLine()) != null)
			{
				ProcessQuery(line);
			}
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}

		}
		
	}
	
	public void ProcessQuery(String query)
	{
		String[] tokens = query.split("\\s+");
		for(String token : tokens)
		{
			HashMap<String,Integer> docsAndfreq = WordsDocumentsAndFrequencies.get(token);
			Object[] objectArray = docsAndfreq.keySet().toArray();
			String[] documents = Arrays.copyOf(objectArray, objectArray.length, String[].class);
			Integer n1 = documents.length;
		//	System.out.println("Current word is " + token + "Present in " + n1 + "documents");
			Integer N = TotalDocuments;
			for(String doc : DocumentIds)
			{
				if(Arrays.asList(documents).contains(doc))
				{
					Integer f1 = docsAndfreq.get(doc);
					Double averageDoc = DocumentsAndAverages.get(doc);
					
			//		System.out.println("Current Document is "+ doc + "Having frequency of token " + token + " as " + f1 + " average freq "+ averageDoc);
					
					Double k = k1 * ((1-b) + b * averageDoc);	
			//		System.out.println("K is "+ k);
					double upper = (0.5 / 0.5);
					double lower = (n1 + 0.5) / (N - n1 + 0.5) ;
					double firstPart = upper / lower;
					double firstPartLog = Math.log(firstPart);
			
					Double SecondPart = ((k1 + 1) * f1) / (k + f1);
					
					Double ThirdPart = (k2 + 1) / (k2 + 1);
					
			//		System.out.println("Having log as "+ firstPartLog + " second part as "+ SecondPart + " Third part as" + ThirdPart);
					Double score = DocumentScores.get(doc);
					if(score == null)
						score = 0.0;
					
					score = score + (firstPartLog * SecondPart * ThirdPart);
			//		System.out.println("Current Score for document =" + doc +"is " + score);
					DocumentScores.put(doc, score);
				}
			}
		}
		
		PrintResults();
	}
	
	public void PrintResults()
	{
		ValueComparator comp = new ValueComparator(DocumentScores);
		TreeMap<String,Double> sorted_scores = new TreeMap<String,Double>(comp);
		sorted_scores.putAll(DocumentScores);
		Integer Rank = 1;
		String CurrentQueryLiteral = "Q0";
		//String systemName = "system_name";
		String hostname = "";
		try 
		{
			hostname = InetAddress.getLocalHost().getHostName();
		} 
		catch (UnknownHostException e) 
		{
			System.out.println("Not able to Get the Host Name i.e. System Name, Please refer stack trace , might be that the current user does not have premission (Not an Admin)");
			e.printStackTrace();
		}
	//	System.out.println("QueryID | Literal| Doc ID|Rank   |Bm25_Score | ");
		for(Map.Entry<String,Double> entry : sorted_scores.entrySet())
		{
				String documentId = entry.getKey();
				Double score = entry.getValue();
				System.out.println(queryId +"\t|"+ CurrentQueryLiteral +"\t|"+ documentId + "\t|" + Rank +"\t|" + score + "\t|" + hostname);
				//resultsFile.println(queryId +"\t|"+ CurrentQueryLiteral +"\t|"+ documentId + "\t|" + Rank +"\t|" + score + "\t|" + hostname);
				Rank++;
				if(Rank > MaximumResults)
				break;
		}
		queryId++;
		System.out.println("\nQuery Number " + queryId + " Results Below==================================================");
		//resultsFile.println("\nQuery Number " + queryId + " Results Below==================================================");
		DocumentScores.clear(); //clear the Map for next iteration
	}
	
	public void PrintDocumentsAndAverages()
	{
		for(Map.Entry<String,Double> entry : DocumentsAndAverages.entrySet())
		{
			String documentId = entry.getKey();
			Double average = entry.getValue();
			System.out.println("Document = " + documentId + " having Average = " + average);
		}
	}
	
	
	public void PrintIndexedValues()
	{
		try
		{	
			for(Map.Entry<String,HashMap<String,Integer>> entry : WordsDocumentsAndFrequencies.entrySet())
			{
				String word = entry.getKey();
				HashMap<String,Integer> documentIdAndFreq = entry.getValue();
				System.out.println(word);
				for(Map.Entry<String,Integer> docNFreq : documentIdAndFreq.entrySet())
				{
					String documentId = docNFreq.getKey();
					Integer frequency = docNFreq.getValue();
					System.out.println(documentId +" "+ frequency);
				}
				System.out.println("\n");
			}
//			HashMap<String,Integer> map = WordToDocumentAndFreq.get("preliminari");
//			for(Map.Entry<String,Integer> entry : map.entrySet())
//			{
//				String documentId = entry.getKey();
//				Integer frequency = entry.getValue();
//				System.out.println("Document Id is " + documentId + "Frequency is = " + frequency);
//				
//				System.out.println("\n===========================================================");
//			}
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
		}
		
}
	
	public static void main(String[] args)
	{
		String indexerFile = "";
		String QueryFile = "";
		BM25Implementation bm25 = new BM25Implementation();
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		String averagesFile = s + "/AverageLength.txt";
		try
		{
		     //file = new File("/Users/sukhdeepsaini/Documents/workspace/MyIndexer/src/tccorpus.txt");
			indexerFile = args[0]; //input file for processing	
			QueryFile = args[1];
			MaximumResults = Integer.parseInt(args[2]);
		}
		catch(Exception ex)
		{
			System.out.println("There is some issue with the Input file , please refer exception details" + ex.getMessage());
		}
		
		bm25.ComputeAverageForDocuments(averagesFile);
		//bm25.PrintDocumentsAndAverages();
		bm25.ComputeDocumentsAndTheWordFrequencies(indexerFile);
		//bm25.PrintIndexedValues();
		bm25.ComputeScoreForDocuments(QueryFile);
		//bm25.PrintResults();
		//bm25.resultsFile.close();
	}
	
	
}


class ValueComparator implements Comparator<String> 
{

    Map<String, Double> base;
    public ValueComparator(Map<String, Double> base) 
    {
        this.base = base;
    }    
    public int compare(String a, String b)
    {
        if (base.get(a) >= base.get(b)) 
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }
}
