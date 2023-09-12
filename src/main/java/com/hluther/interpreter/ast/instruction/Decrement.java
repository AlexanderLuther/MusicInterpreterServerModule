package com.hluther.interpreter.ast.instruction;

import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.util.Stack;

/**
 *
 * @author helmuth
 */
public class Decrement extends Node implements Instruction{
    
    private Instruction id;

    public Decrement(Instruction id, int row, int column) {
        super(row, column);
        this.id = id;
    }
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        SymbolType tempType = (SymbolType)id.analyze(typeTable, symbolTable, scope, errors);
       
        if(tempType != SymbolType.NOT_FOUND && (tempType == SymbolType.STRING || tempType == SymbolType.BOOLEAN)){
            errors.addSemanticError(new MError("Decremento sobre una variable de tipo "+typeTable.get(tempType).getName(), "Decremento", super.getRow(), super.getColumn()));
        }
        
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable){
        return null;
    }
    
}
