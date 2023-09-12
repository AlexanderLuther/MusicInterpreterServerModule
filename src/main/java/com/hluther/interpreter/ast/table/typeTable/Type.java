package com.hluther.interpreter.ast.table.typeTable;
/**
 *
 * @author helmuth
 */
public class Type {
    
    private String name;
    private SymbolType parentType;

    public Type(String name, SymbolType parentType) {
        this.name = name;
        this.parentType = parentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SymbolType getParentType() {
        return parentType;
    }

    public void setParentType(SymbolType parentType) {
        this.parentType = parentType;
    }

   
}
