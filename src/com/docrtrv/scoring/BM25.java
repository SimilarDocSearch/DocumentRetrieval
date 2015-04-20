package com.docrtrv.scoring;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;


public class BM25 {
	static double k1 = 1.2;
	static double b = 0.75;
	static double k2 = 100;
	public static LinkedHashMap<String, LinkedHashMap<String,Double>> final_scores_d = new LinkedHashMap<>();
	
	public static void calculateBM25(){
		int totalnumberofdocs = Query.len_d.size();
		// System.out.println(totalnumberofdocs);

		// tf(d,i) -> i.getTermFreq()
		// df(i) -> Query.term_info.get(e1.getKey())
		LinkedHashMap<String,Double> inner_final_score = new LinkedHashMap<>();
		for (Entry query_e : Query.querymap.entrySet()) {
			LinkedHashMap<String, Info> innerquerymaptp = (LinkedHashMap<String, Info>) query_e.getValue();

			double K = 0;
			for (Entry doc_e : Query.documentmap.entrySet()) {
				LinkedHashMap<String,Info> innerdocmaptp = (LinkedHashMap<String, Info>) doc_e.getValue();
				K = k1
						* ((1 - b) + b
								* (Query.len_d.get(doc_e.getKey()) / Query.avg_len_d));
				double score = 0.0;
				for (Entry query_e1 : innerquerymaptp.entrySet()) {
					if (null!= innerdocmaptp.get(query_e1.getKey())){
						double part1 = (totalnumberofdocs + 0.5) / (Query.term_info.get(query_e1.getKey())+0.5);
						part1 = Math.log(part1)/Math.log(2);
						Info i = innerdocmaptp.get(query_e1.getKey());
						double part2 = ((1+k1)*i.getTermFreq())/(K+i.getTermFreq());
						i = innerquerymaptp.get(query_e1.getKey());
						double part3 = ((1+k2)*i.getTermFreq())/(k2+i.getTermFreq());
						score = score + part1*part2*part3;
					}
				}
				inner_final_score.put(doc_e.getKey().toString(), score);
				K = 0;
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
		//System.out.println(final_scores_d);
		
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
