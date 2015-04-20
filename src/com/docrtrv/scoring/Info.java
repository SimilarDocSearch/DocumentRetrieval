package com.docrtrv.scoring;


public class Info {
	
	private Integer termFreq; // TF w.r.t document/query
	private Double oktf; // the component of the document/query vector for term i
	private Double tf_idf;
	
	public Integer getTermFreq() {
		return termFreq;
	}
	public void setTermFreq(int termFreq) {
		this.termFreq = termFreq;
	}
	public Double getOktf() {
		return oktf;
	}
	public void setOktf(Double oktf) {
		this.oktf = oktf;
	}
	
	public Double getTf_idf() {
		return tf_idf;
	}
	public void setTf_idf(Double tf_idf) {
		this.tf_idf = tf_idf;
	}
	
	@Override
	public String toString(){
		return "("+getTermFreq()+","+getOktf()+","+getTf_idf()+")" ;
	}
	
}
