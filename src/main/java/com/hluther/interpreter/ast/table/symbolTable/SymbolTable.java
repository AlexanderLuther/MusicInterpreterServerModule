package com.hluther.interpreter.ast.table.symbolTable;

import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
/**
 *
 * @author helmuth
 */
public class SymbolTable extends HashMap<String, Symbol> {
       
    /**
     * Inserta un simbolo, cuya categoria es VAR, en la tabla de simbolos y establece si
     * esta inicializado o no.Metodo ejecutado al momento del analisis semantico.
     * @param type
     * @param id
     * @param category
     * @param scope
     * @param initialized
     * @return 
     */
    public boolean add(SymbolType type, String id, SymbolCategory category,  String scope, boolean initialized){
        if(!containsKey(id+"_"+scope)){
            put(id+"_"+scope, new Symbol(id, type, category, scope, initialized));
            return true;
        }
        return false;
        
    }
    
    /**
     * Inserta un simbolo, cuya categoria es ARRAY, en la tabla de simbolos y establece si
     * esta inicializado o no. Metodo ejecutado al momento del analisis semantico.
     * @param type
     * @param id
     * @param category
     * @param dimensionsAmount
     * @param scope
     * @param initialized
     * @return 
     */
    public boolean add(SymbolType type, String id, SymbolCategory category, int dimensionsAmount, String scope, boolean initialized){
        if(!containsKey(id+"_"+scope)){
            put(id+"_"+scope, new Symbol(id, type, dimensionsAmount, category, scope, initialized));
            return true;
        }
        return false;
        
    }
    
    /**
     * Inserta un simbolo, cuya categoria es PROCEDURE O FUNCTIONA,  en la tabla de simbolos.
     * @param id
     * @param type
     * @param category
     * @param paramsAmount
     * @param paramsType
     * @return 
     */
    public boolean addMethod(String id, SymbolType type, SymbolCategory category , int paramsAmount, LinkedList<SymbolType> paramsType){
        if(!containsKey(id)){
            put(id, new Symbol(id, type, category, id, paramsAmount, paramsType));
            return true;
        }
        return false;
    }
    
    /**
     * Busca un simbolo en la tabla de simbolos. Itera desde el ambito actual hasta el 
     * global. 
     * @param id
     * @param scope
     * @return 
     */
    public Symbol get(String id, Stack<String> scope){        
        Stack<String> tempStack = new Stack<>();
        tempStack.addAll(scope); 
        Symbol temp;
        
        while(!tempStack.isEmpty()){
            temp = get(id+"_"+tempStack.pop());
            if(temp != null) return temp;
        }
        return null;
    }
    
    
        public Symbol getLastMethod(Stack<String> scope){        
        Stack<String> tempStack = new Stack<>();
        tempStack.addAll(scope); 
        Symbol temp;
        
        while(!tempStack.isEmpty()){
            temp = get(tempStack.pop());
            if(temp != null){
                if(temp.getCategory() == SymbolCategory.FUNCTION || temp.getCategory() == SymbolCategory.PROCEDURE){
                    return temp;
                }
            }
        }
        return null;
    }
    
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Inserta un simbolo sin inicializar en la tabla de simbolos.
     * @param type
     * @param id
     * @param scope
     * @return 
     */
    public boolean add(SymbolType type, String id, String scope){
        if(!containsKey(id+"_"+scope)){
            put(id+"_"+scope, new Symbol(id, type, SymbolCategory.VAR, scope));
            return true;
        }
        return false;
    }
    
    /**
     * Inserta un simbolo, inicilizando su valor, en la tabla de simbolos.
     * @param typpe
     * @param id
     * @param value
     * @param scope
     * @return 
     */
    public boolean add(SymbolType typpe, String id, Object value, String scope){
        if(!containsKey(id+"_"+scope)){
            put(id+"_"+scope, new Symbol(id, typpe, SymbolCategory.VAR, value, scope));
            return true;
        }
        return false;
    }

    /**
     * Modificar el valor de un simbolo.
     * @param id
     * @param value 
     */
    public void setValue(String id, Object value){
        Symbol symbol = this.get(id);
        symbol.setValue(value);
        this.replace(id, symbol);
    }
    
}
