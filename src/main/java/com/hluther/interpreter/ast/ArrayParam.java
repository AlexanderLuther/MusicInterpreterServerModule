package com.hluther.interpreter.ast;

import com.hluther.interpreter.ast.table.SymbolType;
import java.util.LinkedList;
/**
 *
 * @author helmuth
 */
public class ArrayParam extends Node implements Instruction{
    
    private String id;
    private SymbolType symbolType;
    private LinkedList<Instruction> dimensions;

    public ArrayParam(String id, SymbolType symbolType, LinkedList<Instruction> dimensions, int row, int column) {
        super(row, column);
        this.id = id;
        this.symbolType = symbolType;
        this.dimensions = dimensions;
    }
    
    
    
}
