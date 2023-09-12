package com.hluther.interpreter.ast.controlInstruction;

import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author helmuth
 */
public class If extends Node implements Instruction{
   
    private Instruction condition;
    private LinkedList<Instruction> instructions;
    private LinkedList<Instruction> elseIfs;
    private Instruction elseInstruction;

    public If(Instruction condition, LinkedList<Instruction> instructions, LinkedList<Instruction> elseIfs, Instruction elseInstruction, int row, int column) {
        super(row, column);
        this.condition = condition;
        this.instructions = instructions;
        this.elseIfs = elseIfs;
        this.elseInstruction = elseInstruction;
    }
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        SymbolType tempType;
        
        //Apilar ambito
        scope.push(scope.peek() + "_si");
          
        //Analizar condicional
        tempType = (SymbolType)condition.analyze(typeTable, symbolTable, scope, errors);
        if(tempType != SymbolType.BOOLEAN && tempType != SymbolType.NOT_FOUND){
            errors.addSemanticError(new MError("La condicion debe de ser un tipo booleano.", "Si", super.getRow(), super.getColumn()));
        }
        
        //Analizar instrucciones if
        for(Instruction instruction : instructions){
            instruction.analyze(typeTable, symbolTable, scope, errors);
        }
        
        //Desapilar ambito
        scope.pop();

        //Analizar elseif
        for(Instruction instruction : elseIfs){
            instruction.analyze(typeTable, symbolTable, scope, errors);
        }
        
        //Analizar else
        if(elseInstruction != null) elseInstruction.analyze(typeTable, symbolTable, scope, errors);
        
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable){
        return null;
    }
    
    
    
}
