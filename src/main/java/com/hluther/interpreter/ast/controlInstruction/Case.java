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
public class Case extends Node implements Instruction {
    
    private SymbolType symbolType;
    private Instruction value;
    private LinkedList<Instruction> instructions;
    private int caseId;
    private Object varValue;

    public Case(Instruction value, LinkedList<Instruction> instructions, int row, int column) {
        super(row, column);
        this.value = value;
        this.instructions = instructions;
    }

    public void setSymbolType(SymbolType symbolType) {
        this.symbolType = symbolType;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public void setVarValue(Object varValue) {
        this.varValue = varValue;
    }
   
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        SymbolType tempType;
        tempType = (SymbolType)value.analyze(typeTable, symbolTable, scope, errors);
        
        //Analizar el caso
        if(symbolType != SymbolType.NOT_FOUND){
            if(symbolType != tempType){
                errors.addSemanticError(new MError("Caso no valido.", "Se esperaba un caso de tipo "+typeTable.get(symbolType).getName()+". Se recibio un tipo "+typeTable.get(tempType).getName()+".", super.getRow(), super.getColumn()));
            }
        }
       
        //Apilar ambito
        scope.push(scope.peek()+" _caso"+caseId);
        
        //Analizar instrucciones del caso
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
        scope.push(scope.peek()+" _caso"+caseId);
        
        boolean condition = false;
        
        if(varValue instanceof Double) condition = (double)varValue == (double)value.execute(typeTable, symbolTable, scope, track);
        if(varValue instanceof Integer) condition = (int)varValue == (int)value.execute(typeTable, symbolTable, scope, track);
        if(varValue instanceof Character) condition = (char)varValue == (char)value.execute(typeTable, symbolTable, scope, track);
        if(varValue instanceof Boolean) condition = (boolean)varValue == (boolean)value.execute(typeTable, symbolTable, scope, track);
        if(varValue instanceof String) condition = ((String)varValue).equals((String)value.execute(typeTable, symbolTable, scope, track));
     
            
        if(condition){
            for(Instruction instruction : instructions){
                if(instruction instanceof Break){
                    return instruction;
                }
                instruction.execute(typeTable, symbolTable, scope, track);        
            }   
        }
            
        //Desapilar ambito
        scope.pop();
        return null;
    }
    
    
    
}
