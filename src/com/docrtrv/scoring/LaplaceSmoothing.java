package com.docrtrv.scoring;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;


public class LaplaceSmoothing {
	public static LinkedHashMap<String, LinkedHashMap<String,Double>> final_scores_d = new LinkedHashMap<>();
	
	public static void calculateLaplaceSmoothing(){
		//V -> Unique terms in corpus -> termids.size
		//len(d) -> we have it -> number of terms in the document
		//tfdi -> null!= innerdocmaptp.get(query_e1.getKey()
		int V = Query.termids.size();
		LinkedHashMap<String,Double> inner_final_score = new LinkedHashMap<>();
		for (Entry query_e : Query.querymap.entrySet()) {
			LinkedHashMap<String, Info> innerquerymaptp = (LinkedHashMap<String, Info>) query_e.getValue();
			
			for (Entry doc_e : Query.documentmap.entrySet()) {
				LinkedHashMap<String,Info> innerdocmaptp = (LinkedHashMap<String, Info>) doc_e.getValue();
				double score = 0.0;
				for (Entry query_e1 : innerquerymaptp.entrySet()) {
					Info i = innerdocmaptp.get(query_e1.getKey()) == null ? null : innerdocmaptp.get(query_e1.getKey());
					double numerator = i == null ? 1.0 : (i.getTermFreq() + 1);
					double denominator = Query.len_d.get(doc_e.getKey())+V;
					score = score + Math.log(numerator/denominator)/Math.log(2);
				}
				inner_final_score.put(doc_e.getKey().toString(), score);
			}
			final_scores_d.put(query_e.getKey().toString(), inner_final_score);
			inner_final_score = new LinkedHashMap<>();
		}
		
		for (Entry e : final_scores_d.entrySet()){
			inner_final_score = (LinkedHashMap<String, Double>) (e.getValue());
			inner_final_score = (LinkedHashMap<String, Double>) Query.sortByValues(inner_final_score);
			//System.out.println(inner_final_score.entrySet());
			final_scores_d.put(e.getKey().toString(), inner_final_score);
		}
		
		//System.out.println(final_scores_d.entrySet());
		
	}
	
	public static void write_score_d() throws IOException{
		/*Writing termids to the file*/
		File file = new File("run.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		int rank = 0;
		LinkedHashMap<String, Double>inner_final_score = new LinkedHashMap<>();
		for (Entry e: final_scores_d.entrySet()){
			inner_final_score = (LinkedHashMap<String, Double>) e.getValue();
			for (Entry e1:inner_final_score.entrySet()){
				rank = rank+1;
				bw.write(e.getKey().toString()+"\t"+"0\t"+Query.docids.get(e1.getKey())+"\t\t"+rank +"\t"+e1.getValue()+"\t\trun1\n");
			}
			rank = 0;
		}
		bw.close();
	}

}
