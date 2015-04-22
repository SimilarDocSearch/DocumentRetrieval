package com.docrtrv.scoring;

import java.util.ArrayList;
import java.util.List;
public class TfIDF {

	 public double cosineSimilarity (boolean print, String str1, String str2) {
        List<String> terms = new ArrayList<>();
        List<Double> doc1 = new ArrayList<>();
        List<Double> doc2 = new ArrayList<>();

        String[] docs = new String[2];
        docs[0] = str1;
        docs[1] = str2;

        for (int i = 0; i < docs.length; i++) {
            for (String s : docs[i].split(" ")) {
                if (!terms.contains(s)) {
                    terms.add(s);
                }
            }
        }

        for(String s : terms) {
            double tempCount = 0.0;
            for (int i = 0; i < docs.length; i++) {
                for (String str : docs[i].split(" ")) {
                    if(str.equals(s)) {
                        tempCount++;
                    }
                }
                if (i == 0) {
                    doc1.add(tempCount);
                } else if (i == 1) {
                    doc2.add(tempCount);
                }
                tempCount = 0.0;
            }
        }

        if(print) {
            for (String s : terms) {
                //System.out.print(s + " ");
            }

            //System.out.println();

            //System.out.print("A = [ ");
            for (Double i : doc1) {
                //System.out.print(i + " ");
            }
            //System.out.print("];");

            //System.out.println();

            //System.out.print("B = [ ");
            for (Double i : doc2) {
                //System.out.print(i + " ");
            }
            //System.out.print("];");
        }

        double[] d1 = new double[doc1.size()];
        for (int i = 0; i < d1.length; i++) {
            d1[i] = doc1.get(i);
        }

        double[] d2 = new double[doc2.size()];
        for (int i = 0; i < d2.length; i++) {
            d2[i] = doc2.get(i);
        }

        if(!print) {
            //System.out.println(cosine(d1, d2));
        } else if (print) {
           // System.out.println("\n" + cosine(d1, d2));
        }
        return cosine(d1, d2);
    }

    public double cosine(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
	
}
