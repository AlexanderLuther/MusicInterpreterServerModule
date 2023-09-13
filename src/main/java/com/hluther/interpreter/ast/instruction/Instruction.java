package com.hluther.interpreter.ast.instruction;

import com.hluther.entity.AnalysisError;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import com.hluther.interpreter.ast.track.Track;
import java.util.Stack;

/**
 *
 * @author helmuth
 */
public interface Instruction {
    
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors);
    public Object execute(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, Track track);
}
