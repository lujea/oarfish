/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oarfish.keywords;

/**
 *
 * @author ludovic
 */
public interface IKeywordExtraction {
    
    public RelevantTerm[] extract(String text, int topK);
    
}
