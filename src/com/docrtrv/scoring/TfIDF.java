package com.docrtrv.scoring;
// we reffered some of the related TF-IDf codes for it's implementation and reimplemented it 
//but we  were unable to get the exact file location for the code we referred..
import java.util.ArrayList;
import java.util.List;
public class TfIDF {

	 public double cosineSimilarity (boolean print, String st1, String st2) {
        List<String> words = new ArrayList<>();
        List<Double> word_i = new ArrayList<>();
        List<Double> word_j = new ArrayList<>();

        String[] strings = new String[2];
        strings[0] = st1;
        strings[1] = st2;
    
        for (int i = 0; i < strings.length; i++) {
            for (String s : strings[i].split(" ")) {
                if (!words.contains(s)) {
                    words.add(s);
                }
            }
        }

        for(String s : words) {
            double count = 0.0;
            for (int i = 0; i < strings.length; i++) {
                for (String str : strings[i].split(" ")) {
                    if(str.equals(s)) {
                        count++;
                    }
                }
                if (i == 0) {
                    word_i.add(count);
                } else if (i == 1) {
                    word_j.add(count);
                }
                count = 0.0;
            }
        }

        

        double[] d1 = new double[word_i.size()];
        for (int i = 0; i < d1.length; i++) {
            d1[i] = word_i.get(i);
        }

        double[] d2 = new double[word_j.size()];
        for (int i = 0; i < d2.length; i++) {
            d2[i] = word_j.get(i);
        }

        return cos_sim(d1, d2);
    }

    public double cos_sim(double[] word_1, double[] word_2) {
        double result = 0.0;
        double var_a = 0.0;
        double var_b = 0.0;
        for (int i = 0; i < word_1.length; i++) {
            result += word_1[i] * word_2[i];
            var_a += Math.pow(word_1[i], 2);
            var_b += Math.pow(word_2[i], 2);
        }
        
        return result / (Math.sqrt(var_a) * Math.sqrt(var_b));
    }
	
}
