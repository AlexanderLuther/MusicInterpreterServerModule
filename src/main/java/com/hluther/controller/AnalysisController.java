package com.hluther.controller;

import com.hluther.entity.AnalysisError;
import com.hluther.interpreter.lexer.TrackLexer;
import com.hluther.interpreter.parser.TrackParser;
import com.hluther.interpreter.ast.track.Track;
import java.io.StringReader;
/**
 *
 * @author helmuth
 */
public class AnalysisController {
    
    private AnalysisError analysisError;
    private Track track;

    public AnalysisController() {}
    
    public void analyzeTrack(String inputData){
        try {
            analysisError = new AnalysisError();
            TrackLexer lexer = new TrackLexer(new StringReader(inputData));
            TrackParser parser = new TrackParser(lexer);
            parser.parse();
            
            //Obtener errores Lexicos, Semanticos y Sintacticos encontrados.
            analysisError.addLexicalErrors(lexer.getLexicalErrors());
            analysisError.addSemanticErrors(lexer.getSemanticErrors());
            analysisError.addSintacticErrors(parser.getSyntacticErrors());
            
            //retornar  el ast creador por la traduccion dirigida por la sintaxis.
            track = parser.getTrack();
                      
        } catch (Exception ex) {
            System.out.println("Error ejecutando el analisis. No se pudo generar el AST." + ex.getMessage());
        }
    }

    public AnalysisError getAnalysisError() {
        return analysisError;
    }

    public Track getTrack() {
        return track;
    }
    
    
}

