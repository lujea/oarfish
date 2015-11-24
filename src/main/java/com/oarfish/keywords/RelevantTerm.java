/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oarfish.keywords;

import java.util.Objects;

/**
 *
 * @author ludovic
 */
public class RelevantTerm {
    public String term;
    public double score;
    
    public RelevantTerm(String term, double score){
        this.term = term;
        this.score = score;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.term.toLowerCase());
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
        final RelevantTerm other = (RelevantTerm) obj;
        if (!Objects.equals(this.term.toLowerCase(), other.term.toLowerCase())) {
            return false;
        }
        return true;
    }
    
    
}
