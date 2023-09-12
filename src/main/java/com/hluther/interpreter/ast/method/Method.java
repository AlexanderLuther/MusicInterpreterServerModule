package com.hluther.interpreter.ast.method;

import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
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

    public String getId() {
        return id;
    }

    public LinkedList<Instruction> getParams() {
        return params;
    }

    public LinkedList<Instruction> getInstructions() {
        return instructions;
    }

    /**
     * Crea el nombre del metodo. El nombre esta basado en el id del metodo y el 
     * tipo de cada uno de sus parametros.
     * @return 
     */
    public String getIdentifier(){
        String identifier = id;   
        for (Instruction instruction : params) {
            identifier+= "_" + ((Param)instruction).getSymbolType().toString().toLowerCase();
        }
        return identifier;
    }
    
    /**
     * Devuelve un listado ordenado con los tipos de cada uno de los parametros.
     * @return 
     */
    public LinkedList<SymbolType> getParamsType(){
        LinkedList<SymbolType> list = new LinkedList();   
        for (Instruction instruction : params) {
            list.add(((Param)instruction).getSymbolType());  
        }
        return list;
    }
    
}
