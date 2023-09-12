package com.hluther.interpreter.ast.table.symbolTable;

import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import java.util.LinkedList;
/**
 *
 * @author helmuth
 */
public class Symbol {
    
    private String id;
    private SymbolType type;
    private SymbolCategory category;
    private Object value;
    private String scope;
    private boolean initialized;
    private int paramnsAmount;
    private LinkedList<SymbolType> paramsType;
    
    //Arrays
    private int dimensionsAmount;
  
    /**
     * Variables
     * Constuctor utilizado unicamente durante el analisis semantico.
     * @param id
     * @param type
     * @param category
     * @param scope
     * @param initialized 
     */
    public Symbol(String id, SymbolType type, SymbolCategory category, String scope, boolean initialized) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.scope = scope;
        this.initialized = initialized;
    }
   
    public Symbol(String id, SymbolType type, int dimensionsAmount, SymbolCategory category, String scope, boolean initialized) {
        this.id = id;
        this.type = type;
        this.dimensionsAmount = dimensionsAmount;
        this.category = category;
        this.scope = scope;
        this.initialized = initialized;
    }

    public int getDimensionsAmount() {
        return dimensionsAmount;
    }
    
    /**
     * Variables no inicializadas
     * @param id
     * @param type
     * @param category
     * @param scope 
     */
    public Symbol(String id, SymbolType type, SymbolCategory category, String scope) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.scope = scope;
        this.initialized = false;
    }

    /**
     * Variable inicializada
     * @param id
     * @param type
     * @param category
     * @param value
     * @param scope 
     */
    public Symbol(String id, SymbolType type, SymbolCategory category, Object value, String scope) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.value = value;
        this.scope = scope;
        this.initialized = true;
    }

    /**
     * Funcion o Procedimiento
     * @param id
     * @param type
     * @param category
     * @param scope
     * @param paramnsAmount
     * @param paramsType 
     */
    public Symbol(String id, SymbolType type, SymbolCategory category, String scope, int paramnsAmount, LinkedList<SymbolType> paramsType) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.scope = scope;
        this.paramnsAmount = paramnsAmount;
        this.paramsType = paramsType;
    }

    public String getId() {
        return id;
    }

    public SymbolType getType() {
        return type;
    }

    public SymbolCategory getCategory() {
        return category;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
        this.initialized = true;
    }

    public String getScope() {
        return scope;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public int getParamnsAmount() {
        return paramnsAmount;
    }

    public LinkedList<SymbolType> getParamsType() {
        return paramsType;
    }    

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
