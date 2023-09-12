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
public class For extends Node implements Instruction{
    
    private Instruction assignment;
    private Instruction condition;
    private Instruction action;
    private LinkedList<Instruction> instructions;

    public For(Instruction assignment, Instruction condition, Instruction action, LinkedList<Instruction> instructions, int row, int column) {
        super(row, column);
        this.assignment = assignment;
        this.condition = condition;
        this.action = action;
        this.instructions = instructions;
    }
      
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){        
        SymbolType tempType;
                 
        //Analizar asignacion
        assignment.analyze(typeTable, symbolTable, scope, errors);
           
        //Analizar condicional
        tempType = (SymbolType)condition.analyze(typeTable, symbolTable, scope, errors);
        if(tempType != SymbolType.BOOLEAN && tempType != SymbolType.NOT_FOUND){
            errors.addSemanticError(new MError("La condicion debe de ser un tipo booleano.", "Para", super.getRow(), super.getColumn()));
        }
        
        //Analizar accion
        action.analyze(typeTable, symbolTable, scope, errors);
        
        //Apilar ambito
        scope.push(scope.peek() + "_para");
        
        //Analizar instrucciones for
        for(Instruction instruction : instructions){
            instruction.analyze(typeTable, symbolTable, scope, errors);
        }
        
        //Desapilar ambito
        scope.pop();
        
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable){
        return null;
    }
}
