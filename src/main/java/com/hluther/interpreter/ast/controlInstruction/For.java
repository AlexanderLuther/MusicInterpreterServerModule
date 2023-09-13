package com.hluther.interpreter.ast.controlInstruction;

import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import com.hluther.interpreter.ast.track.Track;
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
        
        //Apilar ambito
        scope.push(scope.peek() + "_para");
                 
        //Analizar asignacion
        assignment.analyze(typeTable, symbolTable, scope, errors);
           
        //Analizar condicional
        tempType = (SymbolType)condition.analyze(typeTable, symbolTable, scope, errors);
        if(tempType != SymbolType.BOOLEAN && tempType != SymbolType.NOT_FOUND){
            errors.addSemanticError(new MError("La condicion debe de ser un tipo booleano.", "Para", super.getRow(), super.getColumn()));
        }
        
        //Analizar accion
        action.analyze(typeTable, symbolTable, scope, errors);
        
        //Analizar instrucciones for
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
        scope.push(scope.peek() + "_para");
        
        assignment.execute(typeTable, symbolTable, scope, track);
        
        while((boolean)condition.execute(typeTable, symbolTable, scope, track)){
            
             for(Instruction instruction : instructions){
                if(instruction instanceof Break){
                    return null;
                }
                else if(instruction instanceof Continue){
                    break;
                }
                else{
                    instruction.execute(typeTable, symbolTable, scope, track);
                }
            }
            
            action.execute(typeTable, symbolTable, scope, track);
        }
        
        //Desapilar ambito
        scope.pop();
        return null;
    }
}
