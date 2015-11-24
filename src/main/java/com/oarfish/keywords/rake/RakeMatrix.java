/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oarfish.keywords.rake;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author ludovic
 */
public class RakeMatrix {

    //frequency matrix contains the frequency of each term
    private final HashMap<String, Integer> freqMap = Maps.newHashMap();
    //degree matrix contains the degree of each term. 
    //degree takes into account the number of candidates a given term appeared on
    private final HashMap<String, Integer> degreeMap = Maps.newHashMap();

    public RakeMatrix(CandidateKeyword[] candidateKeyword) {
        initMatrix(candidateKeyword);
    }

    public HashMap<String, Integer> getFreqMap() {
        return freqMap;
    }

    public HashMap<String, Integer> getDegreeMap() {
        return degreeMap;
    }

    /**
     * method to initialize the frequency and degree matrix
     *
     * @param candidateKeyword
     */
    private void initMatrix(CandidateKeyword[] candidateKeyword) {
        ListMultimap<String, String> coocurrenceMatrix = ArrayListMultimap.create();
        for (CandidateKeyword candidate : candidateKeyword) {
            //do not include adjoin keywords in the matrix
            if (candidate.isIsAdjoin() == true) {
                continue;
            }

            String[] tokens = candidate.getLabel().split(" ");
            HashSet<String> tokenSet = Sets.newHashSet(tokens);

            for (String token : tokens) {
                int currentFreq = getOrDefault(freqMap, token, 0);
                
                //update frequency map
                freqMap.put(token, currentFreq + 1);

                //update co-occurence matrix
                for (String neighbor : tokenSet) {
                    if (!neighbor.equals(token)) {
                        coocurrenceMatrix.put(token, neighbor);
                    }
                }
            }
        }
        for (String token : freqMap.keySet()) {
            //degree is define as: freq(t) + size of co-occurrence_vector(t)
            int degree = freqMap.get(token) + coocurrenceMatrix.get(token).size();
            degreeMap.put(token, degree);
        }
    }

    /**
     * Method to compute the score of a keyword The score is computed as
     * score(t) = sum(deg(wi) / freq(wi)) for wi in t
     *
     * @param candidate
     * @return
     */
    public double getScore(CandidateKeyword candidate) {
        double score = 0;
        String candidateText = candidate.getLabel();
        //if the keyword is not adjoin compute score based on term. Otherwise compute for each sub-part of the keyword
        if (candidate.isIsAdjoin() == false) {
            score = computeScore(candidateText);
        } else {
            //split the candidate based on the stop word and compute the score for each subpart
            String stopWordToSplit = candidate.getAdjoinStopWord();
            String[] subParts = candidateText.split(stopWordToSplit);
            for (String part : subParts){
                score += computeScore(part);
            }
        }

        return score;
    }

    private double computeScore(String candidate) {
        double score = 0;
        for (String w : candidate.split(" ")) {
            score += getOrDefault(degreeMap, w, 0) / (double) getOrDefault(freqMap, w, 1);
        }

        return score;
    }
    
    private int getOrDefault(HashMap<String, Integer> map, String key, int defaultValue){
        int out = defaultValue;
        if(map.containsKey(key)){
            out = map.get(key);
        }
        return out;
    }

}
