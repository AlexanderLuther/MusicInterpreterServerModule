package com.hluther.interpreter.ast;

import com.hluther.interpreter.ast.table.SymbolType;
/**
 *
 * @author helmuth
 */
public class Param extends Node implements Instruction{
    
    private String id;
    private SymbolType symbolType;

    public Param(String id, SymbolType symbolType, int row, int column) {
        super(row, column);
        this.id = id;
        this.symbolType = symbolType;
    }
    
    
    
}
