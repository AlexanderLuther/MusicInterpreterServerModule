package com.hluther.interpreter.ast.track;

import com.hluther.entity.AnalysisError;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.table.symbolTable.Symbol;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author helmuth
 */
public class Track extends Node implements Instruction{
    
    private String id;
    private LinkedList<Instruction> instructions;

    public Track(String id, LinkedList<Instruction> instructions, int row, int column) {
        super(row, column);
        this.id = id;
        this.instructions = instructions;
    }

    public Track(String id, int row, int column) {
        super(row, column);
        this.id = id;
        this.instructions = new LinkedList<>();
    }
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        //Apilar el ambito global 
        scope.push("global");
        
        //Analizar cada una de las instrucciones de la pista
        for(Instruction instruction :instructions){
            instruction.analyze(typeTable, symbolTable, scope, errors);
        }
        
        //Desapilar el ambito global
        scope.pop();
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable){
        return null;
    }
    
}
