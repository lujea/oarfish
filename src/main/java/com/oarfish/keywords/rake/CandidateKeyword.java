/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oarfish.keywords.rake;

import java.util.Objects;

/**
 *
 * @author ludovic
 */
public class CandidateKeyword {

    private String label;
    private boolean isAdjoin;
    private String adjoinStopWord;

    public CandidateKeyword(String label) {
        this(label, false, "");
    }

    public CandidateKeyword(String label, boolean isAdjoin, String adjoinStopWord) {
        this.label = label;
        this.isAdjoin = isAdjoin;
        this.adjoinStopWord = adjoinStopWord;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isIsAdjoin() {
        return isAdjoin;
    }

    public void setIsAdjoin(boolean isAdjoin) {
        this.isAdjoin = isAdjoin;
    }

    public String getAdjoinStopWord() {
        return adjoinStopWord;
    }

    public void setAdjoinStopWord(String adjoinStopWord) {
        this.adjoinStopWord = adjoinStopWord;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.label.toLowerCase());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CandidateKeyword other = (CandidateKeyword) obj;
        if (!Objects.equals(this.label.toLowerCase(), other.label.toLowerCase())) {
            return false;
        }
        return true;
    }
    
    

}
