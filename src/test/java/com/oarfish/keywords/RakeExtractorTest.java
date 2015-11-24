/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oarfish.keywords;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.oarfish.keywords.rake.CandidateKeyword;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ludovic
 */
public class RakeExtractorTest {

    static RakeExtractor rakeExtractor;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String abstractTxt = "Compatibility of systems of linear constraints over the set of natural numbers\n"
            + "\n"
            + "Criteria of compatibility of a system of linear Diophantine equations, strict inequations, and nonstrict inequations are considered. "
            + "Upper bounds for components of a minimal set of solutions and algorithms of construction of minimal generating sets of solutions for all types of systems are given. "
            + "These criteria and the corresponding algorithms for constructing a minimal supporting set of solutions can be used in solving all the considered types of systems and systems of mixed types.";

    public RakeExtractorTest() {
    }

    /**
     * Test of extract method, of class RakeExtractor.
     */
    @Test
    public void testExtract() {
        logger.info("Testing extraction of keywords");
        int topK = 5;
        RakeExtractor instance = new RakeExtractor();

        ArrayList<RelevantTerm> expected = Lists.newArrayList();
        expected.add(new RelevantTerm("minimal generating sets", 8.7));
        expected.add(new RelevantTerm("linear diophantine equations", 8.5));
        expected.add(new RelevantTerm("minimal supporting set", 7.7));
        expected.add(new RelevantTerm("minimal set", 4.7));
        expected.add(new RelevantTerm("linear constraints", 4.5));
        RelevantTerm[] expResult = expected.toArray(new RelevantTerm[5]);
        RelevantTerm[] result = instance.extract(abstractTxt, topK);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getCandidateKeywords method, of class RakeExtractor.
     */
    @Test
    public void testGetCandidateKeywords() {
        logger.info("Testing generation of keywords");
        String[] expected = {"compatibility", "systems", "linear constraints", "set", "natural numbers",
            "criteria", "compatibility", "system", "linear diophantine equations", "strict inequations",
            "nonstrict inequations", "upper bounds", "components", "minimal set", "solutions", "algorithms", "construction", "minimal generating sets",
            "solutions", "systems", "criteria", "corresponding algorithms", "constructing", "minimal supporting set", "solving", "systems", "types", "types of systems"};
        RakeExtractor instance = new RakeExtractor();
        HashSet<String> expectedSet = Sets.newHashSet(expected);
        CandidateKeyword[] candidates = instance.getCandidateKeywords(abstractTxt);
        HashSet<String> extractedSet = Sets.newHashSet();
        for (CandidateKeyword candidate : candidates) {
            extractedSet.add(candidate.getLabel());
        }
        logger.info("Missing {}", Sets.difference(extractedSet, expectedSet));
        assertEquals(extractedSet, expectedSet);
    }

}
