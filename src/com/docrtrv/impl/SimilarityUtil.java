package com.docrtrv.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
* @author Xiao Ma
* mail : 409791952@qq.com
*`enter code here`
*/
  public class SimilarityUtil {

public static double consineTextSimilarity(String[] left, String[] right) {
    Map<String, Integer> leftWordCountMap = new HashMap<String, Integer>();
    Map<String, Integer> rightWordCountMap = new HashMap<String, Integer>();
    Set<String> uniqueSet = new HashSet<String>();
    Integer temp = null;
    for (String leftWord : left) {
        temp = leftWordCountMap.get(leftWord);
        if (temp == null) {
            leftWordCountMap.put(leftWord, 1);
            uniqueSet.add(leftWord);
        } else {
            leftWordCountMap.put(leftWord, temp + 1);
        }
    }
    for (String rightWord : right) {
        temp = rightWordCountMap.get(rightWord);
        if (temp == null) {
            rightWordCountMap.put(rightWord, 1);
            uniqueSet.add(rightWord);
        } else {
            rightWordCountMap.put(rightWord, temp + 1);
        }
    }
    int[] leftVector = new int[uniqueSet.size()];
    int[] rightVector = new int[uniqueSet.size()];
    int index = 0;
    Integer tempCount = 0;
    for (String uniqueWord : uniqueSet) {
        tempCount = leftWordCountMap.get(uniqueWord);
        leftVector[index] = tempCount == null ? 0 : tempCount;
        tempCount = rightWordCountMap.get(uniqueWord);
        rightVector[index] = tempCount == null ? 0 : tempCount;
        index++;
    }
    return consineVectorSimilarity(leftVector, rightVector);
}

/**
 * The resulting similarity ranges from −1 meaning exactly opposite, to 1
 * meaning exactly the same, with 0 usually indicating independence, and
 * in-between values indicating intermediate similarity or dissimilarity.
 * 
 * For text matching, the attribute vectors A and B are usually the term
 * frequency vectors of the documents. The cosine similarity can be seen as
 * a method of normalizing document length during comparison.
 * 
 * In the case of information retrieval, the cosine similarity of two
 * documents will range from 0 to 1, since the term frequencies (tf-idf
 * weights) cannot be negative. The angle between two term frequency vectors
 * cannot be greater than 90°.
 * 
 * @param leftVector
 * @param rightVector
 * @return
 */
private static double consineVectorSimilarity(int[] leftVector,
        int[] rightVector) {
    if (leftVector.length != rightVector.length)
        return 1;
    double dotProduct = 0;
    double leftNorm = 0;
    double rightNorm = 0;
    for (int i = 0; i < leftVector.length; i++) {
        dotProduct += leftVector[i] * rightVector[i];
        leftNorm += leftVector[i] * leftVector[i];
        rightNorm += rightVector[i] * rightVector[i];
    }

    double result = dotProduct
            / (Math.sqrt(leftNorm) * Math.sqrt(rightNorm));
    return result;
}

public static void main(String[] args) {
    String left[] = "This is a foo bar sentence .".split(" ");
    String right[] = "This sentence is similar to a foo bar sentence .".split(" ");
    System.out.println(consineTextSimilarity(left,right));
}
}
