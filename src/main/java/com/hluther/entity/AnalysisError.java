package com.hluther.entity;

import java.util.LinkedList;

/**
 *
 * @author helmuth
 */
public class AnalysisError {
    
    private LinkedList<MError> lexicalErrors;
    private LinkedList<MError> sintacticErrors;
    private LinkedList<MError> semanticErrors;

    public AnalysisError() {
        this.lexicalErrors = new LinkedList();
        this.sintacticErrors = new LinkedList();
        this.semanticErrors = new LinkedList();
    }

    public LinkedList getLexicalErrors() {
        return lexicalErrors;
    }

    public void addLexicalErrors(LinkedList<MError> lexicalErrors) {
        this.lexicalErrors.addAll(lexicalErrors);
    }

    public LinkedList getSintacticErrors() {
        return sintacticErrors;
    }

    public void addSintacticErrors(LinkedList<MError> sintacticErrors) {
        this.sintacticErrors.addAll(sintacticErrors);
    }

    public LinkedList getSemanticErrors() {
        return semanticErrors;
    }

    public void addSemanticErrors(LinkedList<MError> semanticErrors) {
        this.semanticErrors.addAll(semanticErrors);
    }
    
}
