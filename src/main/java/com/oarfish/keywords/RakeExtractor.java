/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oarfish.keywords;

import com.oarfish.keywords.rake.CandidateGenerator;
import com.oarfish.keywords.rake.CandidateKeyword;
import com.oarfish.keywords.rake.RakeScorer;
import java.io.InputStream;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ludovic
 */
public class RakeExtractor implements IKeywordExtraction {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CandidateGenerator candidateGenerator;
    private final RakeScorer scorer;

    public RakeExtractor() {
        /*InputStream propertiesStream = this.getClass().getResourceAsStream("config.properties");
        Properties props = new Properties();
        try {
            props.load(propertiesStream);
        } catch (IOException ex) {
           logger.error("IOException: failed to load properties from 'config.properties'", ex);
           System.exit(0);
        }*/
        InputStream stopFileStream = this.getClass().getClassLoader().getResourceAsStream("stoplist.txt");        
        candidateGenerator = new CandidateGenerator(stopFileStream);
        scorer = new RakeScorer();
    }

    /**
     * Returns the topK keywords sorted by relevance score. If -1 is specified all the keywords are returned
     * @param text
     * @param topK
     * @return 
     */
    @Override
    public RelevantTerm[] extract(String text, int topK) {        
        //generate a list of candidate keywords
        CandidateKeyword[] candidates = candidateGenerator.generateCandidates(text);
        
        //compute the score for the candidates
        ArrayList<RelevantTerm> keywords = scorer.getKeywords(candidates, topK);
        return keywords.toArray(new RelevantTerm[0]);
    }



    public CandidateKeyword[] getCandidateKeywords(String text) {
        return candidateGenerator.generateCandidates(text);
    }

}
