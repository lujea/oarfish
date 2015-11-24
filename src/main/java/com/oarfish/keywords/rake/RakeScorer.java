package com.oarfish.keywords.rake;

import com.google.common.collect.Lists;
import com.oarfish.keywords.RelevantTerm;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RakeScorer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RakeScorer() {}

    public ArrayList<RelevantTerm> getKeywords(CandidateKeyword[] candidateKeywordList, int topN) {
        Comparator<RelevantTerm> comparator = new Comparator<RelevantTerm>() {

            @Override
            public int compare(RelevantTerm o1, RelevantTerm o2) {
                return (o1.score > o2.score) ? -1 : 1;
            }
        };

        PriorityQueue<RelevantTerm> keywordHeap = new PriorityQueue<>(topN, comparator);

        RakeMatrix matrix = new RakeMatrix(candidateKeywordList);

        ArrayList<RelevantTerm> word_score_map = Lists.newArrayList();

        for (CandidateKeyword candidate : candidateKeywordList) {            
            //compute the relevancy score as the
            double score = matrix.getScore(candidate);
            keywordHeap.offer(new RelevantTerm(candidate.getLabel(), score));
        }
        
        int count = 0;
        if (topN != -1) {
            while (keywordHeap.size() > 0 && count != topN) {
                word_score_map.add(keywordHeap.poll());
                count += 1;
            }
        }else{
            word_score_map = Lists.newArrayList(keywordHeap.toArray(new RelevantTerm[0]));
        }

        return word_score_map;
    }

}
