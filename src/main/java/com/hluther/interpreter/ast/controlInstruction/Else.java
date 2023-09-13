package com.hluther.interpreter.ast.controlInstruction;

import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.entity.AnalysisError;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import com.hluther.interpreter.ast.track.Track;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author helmuth
 */
public class Else extends Node implements Instruction{
    
    private LinkedList<Instruction> instructions;

    public Else(LinkedList<Instruction> instructions, int row, int column) {
        super(row, column);
        this.instructions = instructions;
    }
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        //Apilar ambito
        scope.push(scope.peek() + "_sino");
        
        //Analizar instrucciones
        for(Instruction instruction : instructions){
            instruction.analyze(typeTable, symbolTable, scope, errors);
        }
        
        //Desapilar ambito
        scope.pop();
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, Track track){
        //Apilar ambito
        scope.push(scope.peek() + "_sino");
        
        for(Instruction instruction : instructions){
            instruction.execute(typeTable, symbolTable, scope, track);
        }
        
        //Desapilar ambito
        scope.pop();
        return null;
    }
}
