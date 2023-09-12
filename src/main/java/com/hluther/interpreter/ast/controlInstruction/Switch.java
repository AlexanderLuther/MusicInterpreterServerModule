package com.hluther.interpreter.ast.controlInstruction;

import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.entity.AnalysisError;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author helmuth
 */
public class Switch extends Node implements Instruction{
    
    private Instruction id;
    private LinkedList<Instruction> cases;
    private Instruction defaultCase;

    public Switch(Instruction id, LinkedList<Instruction> cases, Instruction defaultCase, int row, int column) {
        super(row, column);
        this.id = id;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }
    
        public Switch(Instruction id, LinkedList<Instruction> cases, int row, int column) {
        super(row, column);
        this.id = id;
        this.cases = cases;
        this.defaultCase = null;
    }

        @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        SymbolType tempType; 
        int caseId = 0;
        
        //Apilar ambito 
        scope.push(scope.peek() + "_switch");
        
        //Analizar variable
        tempType = (SymbolType) id.analyze(typeTable, symbolTable, scope, errors);
         
        //Analizar cases y establecer su tipo
        for(Instruction instruction : cases){
            ((Case)instruction).setSymbolType(tempType);
            ((Case)instruction).setCaseId(caseId);
            caseId++;
            instruction.analyze(typeTable, symbolTable, scope, errors);
        }
        
        //Analizar defaultCase
        if(defaultCase != null) defaultCase.analyze(typeTable, symbolTable, scope, errors);
        
        //Desapilar ambito
        scope.pop();
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable){
        return null;
    }

    
}
