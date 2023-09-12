package com.hluther.interpreter.ast.track;

import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.entity.AnalysisError;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.util.Stack;

/**
 *
 * @author helmuth
 */
public class Message extends Node implements Instruction {
    
    private Instruction content;

    public Message(Instruction content, int row, int column) {
        super(row, column);
        this.content = content;
    }
        
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        content.analyze(typeTable, symbolTable, scope, errors);
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable){
        return null;
    }
}
