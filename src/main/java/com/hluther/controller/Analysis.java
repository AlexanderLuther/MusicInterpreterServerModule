package com.hluther.controller;

import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.lexer.TrackLexer;
import com.hluther.interpreter.parser.TrackParser;
import java.io.StringReader;
import java.util.Arrays;
/**
 *
 * @author helmuth
 */
public class Analysis {
    
    private AnalysisError analysisError;

    public Analysis() {
        this.analysisError = new AnalysisError();
    }
    
    public void analyzeTrackInput(StringReader inputData){
        try {
            TrackLexer lexer = new TrackLexer(inputData);
            TrackParser parser = new TrackParser(lexer);
            parser.parse();
            
            analysisError.addLexicalErrors(lexer.getLexicalErrors());
            analysisError.addSemanticErrors(lexer.getSemanticErrors());
            
            System.out.println("ERRORES LEXICOS");
             for(Object error : analysisError.getLexicalErrors()){
                System.out.println("Columna:"+((MError)error).getColumn() +" Linea:"+((MError)error).getLine()+" "+((MError)error).getError() + " " + ((MError)error).getLexeme());
            }
          
            System.out.println("ERRORES SEMANTICOS");
            for(Object error : analysisError.getSemanticErrors()){
                System.out.println(((MError)error).getColumn() +" "+((MError)error).getLine()+" "+((MError)error).getError());
            }
            
        } catch (Exception ex) {
            System.out.println("Error ejecutando el analisis.");
        }
    }

    public AnalysisError getAnalysisError() {
        return analysisError;
    }
    
}

