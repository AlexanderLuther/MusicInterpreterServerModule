package com.hluther.controller;

import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.lexer.TrackLexer;
import com.hluther.interpreter.parser.TrackParser;
import com.hluther.interpreter.ast.track.Track;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.io.StringReader;
import java.util.Stack;
/**
 *
 * @author helmuth
 */
public class AnalysisController {
    
    private AnalysisError analysisError;

    public AnalysisController() {
        this.analysisError = new AnalysisError();
    }
    
    public void analyzeTrackInput(String inputData){
        try {
            TrackLexer lexer = new TrackLexer(new StringReader(inputData));
            TrackParser parser = new TrackParser(lexer);
            parser.parse();
            
            analysisError.addLexicalErrors(lexer.getLexicalErrors());
            analysisError.addSemanticErrors(lexer.getSemanticErrors());
            analysisError.addSintacticErrors(parser.getSyntacticErrors());
            
            
            
            Track track = parser.getTrack();
            
            track.analyze(new TypeTable(), new SymbolTable(), new Stack<>(),  analysisError);
            
            System.out.println("ERRORES LEXICOS");
             for(Object error : analysisError.getLexicalErrors()){
                System.out.println("Columna:"+((MError)error).getColumn() +" Linea:"+((MError)error).getLine()+" "+((MError)error).getError() + " " + ((MError)error).getLexeme());
            }
             
            System.out.println("ERRORES SINCTACTICOS");
             for(Object error : analysisError.getSintacticErrors()){
                System.out.println("Columna:"+((MError)error).getColumn() +" Linea:"+((MError)error).getLine()+" "+((MError)error).getError() + " " + ((MError)error).getLexeme());
            }
          
            System.out.println("ERRORES SEMANTICOS");
            for(Object error : analysisError.getSemanticErrors()){
                System.out.println(((MError)error).getColumn() +" "+((MError)error).getLine()+" "+((MError)error).getError() +" "+ ((MError)error).getLexeme());
            }
           
        } catch (Exception ex) {
            System.out.println("Error ejecutando el analisis." + ex.getMessage());
        }
    }

    public AnalysisError getAnalysisError() {
        return analysisError;
    }
    
}

