package com.oarfish.keywords.rake;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CandidateGenerator {

    private HashSet<String> disWords = new HashSet<String>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    HashSet<String> stopWords;

    public HashSet<String> getdistinctWords() {
        if (disWords.isEmpty()) {
            throw new IllegalStateException("Call generate before calling this method");
        } else {
            return disWords;
        }
    }

    public CandidateGenerator(String stopListFile) {
        stopWords = getStopWordsfromFile(stopListFile);
    }

    public CandidateGenerator(InputStream stopListStream) {
        stopWords = getStopWordsfromFile(stopListStream);
    }

    /**
     * Method to generate candidateList keywords from a text adjoin keywords are
     * keywords that contain interior stop words for instance minister of
     * defense. These keywords should follow two conditions: 
     * 1- appear in consecutive order 
     * 2- appear at least twice in the document
     *
     * @param text
     * @return
     */
    public CandidateKeyword[] generateCandidates(String text) {
        ArrayList<CandidateKeyword> candidateList = Lists.newArrayList();        
        ArrayList<CandidateKeyword> adjoinCandidateList = Lists.newArrayList();        

        ArrayList<String> sentences = genSent(text);
        ArrayList<String> temp = new ArrayList<>(); //temp list for storing candkey words
        for (String sent : sentences) {
            //convert to lowercase
            sent = sent.toLowerCase();
            String prevCandidate = "";
            String previousStopWord = "";
            for (String single_word : sent.split("\\s+|\\s*,\\s*")) {

                //Removing numbers from generated keyword list
                if (single_word.matches("[0-9]+") || single_word.matches("\\(.*\\)")) {
                    //System.out.println(single_word);
                    continue;
                }
                //System.out.println(single_word);
                if (stopWords.contains(single_word)) {
                    //logger.info("stopword: {}, previous {}", single_word, prevCandidate);
                    if (temp.isEmpty()) {
                        continue;
                    } else {

                        //Check number of words in the arraylist
                        if (temp.size() <= 3) {
                            //System.out.println(temp);
                            String candidateStr = StringUtils.join(temp, " ");
                            if (candidateStr.trim().length() > 0) {
                                candidateList.add(new CandidateKeyword(candidateStr));
                                //add keyword pair to adjoin list
                                String pair = prevCandidate + " " + previousStopWord + " " + candidateStr;
                                if (previousStopWord.trim().length() > 0 && prevCandidate.trim().length() > 0) {
                                    adjoinCandidateList.add(new CandidateKeyword(pair, true, previousStopWord));
                                }
                                prevCandidate = candidateStr;
                            }
                        }

                        temp.clear();
                    }
                    previousStopWord = single_word;
                } else {
                    //temp.add(checkLastChar(single_word));
                    temp.add(single_word);
                }
            }
            //for the last words in temp
            if (!temp.isEmpty()) {
                if (temp.size() <= 3) {
                    String candidateStr = StringUtils.join(temp, " ");
                    if (candidateStr.trim().length() > 0) {
                        candidateList.add(new CandidateKeyword(candidateStr));
                        //add keyword pair to adjoin list
                        String pair = prevCandidate + " " + previousStopWord + " " + candidateStr;
                        if (previousStopWord.trim().length() > 0 && prevCandidate.trim().length() > 0) {
                            adjoinCandidateList.add(new CandidateKeyword(pair, true, previousStopWord));
                        }
                        prevCandidate = candidateStr;
                    }
                }
            }
            temp.clear();
        }        
        //process the adjoin list and only retrieve those that appear at least twice
        HashSet<CandidateKeyword> adjoinShortList = Sets.newHashSet();
        for (CandidateKeyword adjoinCandidate : adjoinCandidateList){
            if(Collections.frequency(adjoinCandidateList, adjoinCandidate) >= 2){
                adjoinShortList.add(adjoinCandidate);
            }
        }
        //add the list of adjoin keywords to the candidates
        candidateList.addAll(adjoinShortList);

        return candidateList.toArray(new CandidateKeyword[0]);
    }

    //generates sentences from given text
    private ArrayList<String> genSent(String text) {

        ArrayList<String> sent_list = new ArrayList<String>();
        String delimiters = "\\.\\s*|\\?\\s*|\\s*,\\s*|\\s*!\\s*|\n";

        for (String a : text.split(delimiters)) {

            sent_list.add(a);
        }

        return sent_list;

    }

    private HashSet<String> getStopWordsfromFile(String filename) {
        HashSet<String> list_stop_words = new HashSet<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                list_stop_words.add(sCurrentLine);
            }

        } catch (IOException e) {
            logger.error("IOException: could not load stop-list. You must provide a list of stop-words", e);
        }

        return list_stop_words;

    }

    private HashSet<String> getStopWordsfromFile(InputStream fileStream) {
        HashSet<String> list_stop_words = new HashSet<String>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fileStream))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                list_stop_words.add(sCurrentLine);
            }

        } catch (IOException e) {
            logger.error("IOException: could not load stop-list. You must provide a list of stop-words", e);
        }

        return list_stop_words;

    }

    public static String checkLastChar(String word) {
        HashSet<String> trimmer = new HashSet<String>();
        trimmer.add("s");
        trimmer.add("?");
        trimmer.add(".");
        //add while loop for multiple last char checking
        //see how substirng ke index works
        if (trimmer.contains(word.substring(word.length() - 1, word.length()))) {
            return word.substring(0, word.length() - 1);
        } else {
            return word;
        }
    }
}
