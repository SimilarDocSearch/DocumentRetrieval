package com.docrtrv.scoring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;


public class TF_IDF {
	
	
	public static LinkedHashMap<String, Double> terms_present_in_query_count = new LinkedHashMap<String, Double>();
	public static LinkedHashMap<String, LinkedHashMap<String,Double>> final_scores_d = new LinkedHashMap<>();
	
	public static void calculateTF_IDF() throws NumberFormatException, IOException{
		BufferedReader br = null;
		String sLine = "";
		

		/*---------------Calculating di for TF-IDF ----------------------------------*/
		double totalnumberofDocuments = Query.len_d.size();
		//System.out.println(totalnumberofDocuments);
		LinkedHashMap<String, Info> innerdocumentmap = new LinkedHashMap<>();
		for(Entry<String, LinkedHashMap<String, Info>> e : Query.documentmap.entrySet() ){
			innerdocumentmap = e.getValue();
			for(Entry e1 : innerdocumentmap.entrySet()){
				Info i = (Info) e1.getValue();
				double di = i.getOktf()* Math.log(totalnumberofDocuments/Query.term_info.get(e1.getKey()))/Math.log(2);
				i.setTf_idf(di);
			}
		}
		
		//System.out.println(OkapiTF.documentmap.entrySet() );
		
		/*---------------Calculating qi for TF-IDF ----------------------------------*/
		LinkedHashMap<String, Info> innerquerymap = new LinkedHashMap<>();
		HashSet<String> hs = new HashSet<>();
		for (Entry e : Query.querymap.entrySet()){
			innerquerymap = (LinkedHashMap<String, Info>) e.getValue();
			for (Entry e1 : innerquerymap.entrySet()){
				hs.add(e1.getKey().toString());
			}
		}
		double count = 0;
		for(String s : hs){
			for (Entry e : Query.querymap.entrySet()){
				innerquerymap = (LinkedHashMap<String, Info>) e.getValue();
				if (null!=innerquerymap.get(s)){
					count = count + 1;
				}
			}
			terms_present_in_query_count.put(s, count);
			count = 0;
		}
		
		//System.out.println(terms_present_in_query_count.entrySet());
		
		int totalnumberofQueries = Query.len_d_query.size();
		//System.out.println(totalnumberofQueries);
		innerquerymap = new LinkedHashMap<>();
		for (Entry e : Query.querymap.entrySet()){
			innerquerymap = (LinkedHashMap<String, Info>) e.getValue();
			for (Entry e1 : innerquerymap.entrySet()){
				Info i = (Info) e1.getValue();
				double qi = i.getOktf()*Math.log(totalnumberofQueries/terms_present_in_query_count.get(e1.getKey()))/Math.log(2);
				i.setTf_idf(qi);
			}
		}
		
		//System.out.println(OkapiTF.querymap.entrySet());
	}
	
	public static void calculateTF_IDFScore(){
		/*----------------------Calculating score(d)---------------*/
		double square_di_sum = 0;
		double square_qi_sum = 0;
		LinkedHashMap<String,Double> inner_final_score = new LinkedHashMap<>();
		for (Entry query_e : Query.querymap.entrySet()){

			LinkedHashMap<String,Info> innerquerymaptp = (LinkedHashMap<String, Info>) query_e.getValue();
			for(Entry query_e1 : innerquerymaptp.entrySet()){
				Info i = (Info) query_e1.getValue();
				square_qi_sum = square_qi_sum + i.getTf_idf()*i.getTf_idf();
			}
			double norm_qi = Math.sqrt(square_qi_sum);
			double norm_di = 0;
			for (Entry doc_e : Query.documentmap.entrySet()){
				LinkedHashMap<String,Info> innerdocmaptp = (LinkedHashMap<String, Info>) doc_e.getValue();
				for(Entry doc_e1 : innerdocmaptp.entrySet()){
					Info i = (Info) doc_e1.getValue();
					square_di_sum = square_di_sum + i.getTf_idf()*i.getTf_idf();
				}
				norm_di = Math.sqrt(square_di_sum);
				
				//Take dot product
				double numerator = 0.0;
				for(Entry doc_e1 : innerdocmaptp.entrySet()){
					if (null!=innerquerymaptp.get(doc_e1.getKey())){
						Info qi = (Info) doc_e1.getValue();
						Info di = innerquerymaptp.get(doc_e1.getKey());
						numerator = numerator + qi.getTf_idf() * di.getTf_idf(); 
					}
				}
				double score_d = numerator / (norm_di*norm_qi);
				//System.out.println(query_e.getKey()+" "+doc_e.getKey()+" "+score_d);
				inner_final_score.put(doc_e.getKey().toString(), score_d);
				square_di_sum = 0;
			}
			final_scores_d.put(query_e.getKey().toString(), inner_final_score);
			inner_final_score = new LinkedHashMap<>();
			square_qi_sum = 0;
		}
		
		
		for (Entry e : final_scores_d.entrySet()){
			inner_final_score = (LinkedHashMap<String, Double>) (e.getValue());
			inner_final_score = (LinkedHashMap<String, Double>) Query.sortByValues(inner_final_score);
			//System.out.println(inner_final_score.entrySet());
			final_scores_d.put(e.getKey().toString(), inner_final_score);
		}
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
