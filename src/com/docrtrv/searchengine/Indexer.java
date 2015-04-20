package com.docrtrv.searchengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Indexer 
{
	HashMap<String,HashMap<String,Integer>> WordToDocumentAndFreq = new HashMap<String,HashMap<String,Integer>>();
	HashMap<String,Integer> DocumentToTokenCount = new HashMap<String,Integer>();
	HashMap<String,Double> DocumentAverageLength = new HashMap<String,Double>();
	BufferedReader reader;
	static String DocumentId;
	
	public void ComputerIndex(String fileName)
	{
		try
		{
			String line = null;
		    reader = new BufferedReader(new FileReader(fileName));
			while((line = reader.readLine()) != null)
			{
				String[] tokens = line.split("\\s+"); //split on space

				if(tokens[0].equals("#"))
				{
					DocumentId = tokens[1];
				}
				else if(tokens[0] != "#")
				{
					for(String token : tokens)
					{
						HashMap<String,Integer> DocumentIdAndFrequency =  WordToDocumentAndFreq.get(token);
						HashMap<String,Integer> newEntryInMap = new HashMap<String,Integer>();
						if(DocumentIdAndFrequency == null)
						{
							newEntryInMap.put(DocumentId, 1);
							WordToDocumentAndFreq.put(token,newEntryInMap);
						}
						else
						{
							newEntryInMap = WordToDocumentAndFreq.get(token);
							Integer CurrentCount = newEntryInMap.get(DocumentId);
							if(CurrentCount != null)
							{
								CurrentCount = CurrentCount + 1;
								newEntryInMap.put(DocumentId, CurrentCount);
								WordToDocumentAndFreq.put(token, newEntryInMap);
							}
							else
							{
								newEntryInMap.put(DocumentId, 1);
								WordToDocumentAndFreq.put(token,newEntryInMap);
							}
						}
					}
				}
			}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch(Exception ex)
			{
				System.out.println("File not properly closed due to some error , please refer exception for details" + ex.getMessage());
			}
		}
		

	}
	
	public void GetTokenCountInDocuments(String fileName)
	{
		try
		{
			String line = null;
		    reader = new BufferedReader(new FileReader(fileName));
			while((line = reader.readLine()) != null)
			{
				String[] tokens = line.split("\\s+"); //split on space
				if(tokens[0].equals("#"))
				{
					DocumentId = tokens[1];
				}
				else if(tokens[0] != "#")
				{
					for(String token : tokens)
					{
						Integer tokenCount = DocumentToTokenCount.get(DocumentId);
						
						if(tokenCount == null)
						{
							tokenCount = 1;
						}
						else
						{
							tokenCount = tokenCount + 1;
						}
						DocumentToTokenCount.put(DocumentId, tokenCount);
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch(Exception ex)
			{
				System.out.println("File not properly closed due to some error , please refer exception for details" + ex.getMessage());
			}
		}
		
	}
		
	public void ComputeAverageDocumentLength()
	{
		Object[] documents = DocumentToTokenCount.keySet().toArray();
		Integer TotalNumberOfDocuments = documents.length;
		Integer count = 0;
		for(Object document : documents)
		{
			Integer documentlength = DocumentToTokenCount.get(document);
			count = count + documentlength;
		}
		Double corpusAverage = (double) count / TotalNumberOfDocuments;
		System.out.println("Average of corpus is "+ corpusAverage);
		
		for(Object document : documents)
		{
			Integer documentlength = DocumentToTokenCount.get(document);
			Double AverageDocumentLength = (double) documentlength / corpusAverage;
			DocumentAverageLength.put(document.toString(), AverageDocumentLength);
		}
	}
	
	
	public void PrintAverageDocumentLength() throws IOException
	{
		PrintWriter output = new PrintWriter(new FileWriter("AverageLength.txt"));
		try
		{
			for(Map.Entry<String,Double> entry : DocumentAverageLength.entrySet())
			{
				String document = entry.getKey();
				Double average = entry.getValue();
				output.println(document + ":" + average);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			output.close();
		}


	}
	
	
	public void PrintIndexedValues() throws IOException
	{
		//Object[] keys = WordToDocumentAndFreq.keySet().toArray();
		PrintWriter output = new PrintWriter(new FileWriter("index.out"));
		try
		{	
			for(Map.Entry<String,HashMap<String,Integer>> entry : WordToDocumentAndFreq.entrySet())
			{
				String word = entry.getKey();
				HashMap<String,Integer> documentIdAndFreq = entry.getValue();
				output.println("# "+ word);
				for(Map.Entry<String,Integer> docNFreq : documentIdAndFreq.entrySet())
				{
					String documentId = docNFreq.getKey();
					Integer frequency = docNFreq.getValue();
					output.println(documentId +" "+ frequency);
				}
				//output.println("\n");
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
			ex.printStackTrace();
		}
		finally
		{
			output.close();
		}
		
}
	
	public void PrintDocumentAndItsTokenCount()
	{
		int count = 0;
		for(Map.Entry<String,Integer> entry : DocumentToTokenCount.entrySet())
		{
			String documentId = entry.getKey();
			Integer frequency = entry.getValue();
			System.out.println("Document Id is " + documentId + "Count of Tokens is = " + frequency);
			System.out.println("\n===========================================================");
			count = count + 1;
		}
		System.out.println("Total Documents are " + count);
	}
	
	
	public static void main(String[] args)
	{
		String inputFile = "";
		Indexer newIndexer = new Indexer();
		//File file = null;
		try
		{
		     //file = new File("/Users/sukhdeepsaini/Documents/workspace/MyIndexer/src/tccorpus.txt");
			 inputFile = args[0]; //input file for processing	
		}
		catch(Exception ex)
		{
			System.out.println("There is some issue with the Input file , please refer exception details" + ex.getMessage());
		}
		newIndexer.ComputerIndex(inputFile);
		try
		{
			newIndexer.PrintIndexedValues();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		newIndexer.GetTokenCountInDocuments(inputFile);
		//newIndexer.PrintDocumentAndItsTokenCount();
		newIndexer.ComputeAverageDocumentLength();
		try 
		{
			newIndexer.PrintAverageDocumentLength();
		} 
		catch (IOException e)
		{

			e.printStackTrace();
		}
		
	}
}


