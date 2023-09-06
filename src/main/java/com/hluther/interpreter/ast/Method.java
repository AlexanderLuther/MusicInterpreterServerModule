package com.hluther.interpreter.ast;

import com.hluther.interpreter.ast.table.SymbolType;
import java.util.LinkedList;
/**
 *
 * @author helmuth
 */
public abstract class Method extends Node{
    
    private SymbolType symbolType;
    private String id;
    private LinkedList<Instruction> params;
    private LinkedList<Instruction> instructions;
    private boolean keep;

    public Method(SymbolType symbolType, String id, LinkedList<Instruction> params, LinkedList<Instruction> instructions, int row, int column) {
        super(row, column);
        this.symbolType = symbolType;
        this.id = id;
        this.params = params;
        this.instructions = instructions;
    }
    
    public Method(SymbolType symbolType, String id, LinkedList<Instruction> instructions, int row, int column) {
        super(row, column);
        this.symbolType = symbolType;
        this.id = id;
        this.params = new LinkedList<>();
        this.instructions = instructions;
    }

    public boolean isKeep() {
        return keep;
    }

    public void setKeep(boolean keep) {
        this.keep = keep;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public void setSymbolType(SymbolType symbolType) {
        this.symbolType = symbolType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinkedList<Instruction> getParams() {
        return params;
    }

    public void setParams(LinkedList<Instruction> params) {
        this.params = params;
    }

    public LinkedList<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(LinkedList<Instruction> instructions) {
        this.instructions = instructions;
    }
    
}
