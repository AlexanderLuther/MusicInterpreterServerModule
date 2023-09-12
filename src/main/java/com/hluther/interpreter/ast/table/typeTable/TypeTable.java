package com.hluther.interpreter.ast.table.typeTable;

import static com.hluther.interpreter.ast.table.typeTable.SymbolType.DOUBLE;
import java.util.HashMap;

/**
 *
 * @author helmuth
 */
public class TypeTable extends HashMap<SymbolType, Type> {
   
    public TypeTable() {
        this.put(SymbolType.DOUBLE, new Type("Doble", null));
        this.put(SymbolType.INTEGER, new Type("Entero", SymbolType.DOUBLE));
        this.put(SymbolType.CHARACTER, new Type("Caracter", SymbolType.INTEGER));
        this.put(SymbolType.BOOLEAN, new Type("Boolean", SymbolType.CHARACTER));
        this.put(SymbolType.STRING, new Type("Cadena", null));
        this.put(SymbolType.VOID, new Type("Void", null));
    }
    
    /**
     * Comprueba si los tipos son iguales, de lo contrario verifica si existe una
     * conversion implicita entre tipos.
     * @param varType
     * @param valueType
     * @return 
     */
    public boolean hasImplicitConversion(SymbolType varType, SymbolType valueType){
        if(varType == valueType){
            return true;
        }
        
        switch(varType){
            case DOUBLE -> { 
                return valueType == SymbolType.INTEGER;
            }

            case INTEGER -> { 
                return valueType != SymbolType.STRING;
            }

            case CHARACTER -> { 
                return valueType == SymbolType.INTEGER;
            }

            case BOOLEAN -> { 
                return false;
            }
            case STRING -> { 
                return valueType != SymbolType.VOID;
            }
            default ->{
                return false;
            }
        }   
    }
    
    /**
     * Analiza si los tipos son iguales o si tienen una conversion implicita en base al
     * tipo padre valor que se desea asignar.
     * @param leftType
     * @param rightType
     * @return 
     */
    public SymbolType getOperationType(SymbolType leftType, SymbolType rightType){    
        Type temp;
        //Si ambos tipos son iguales
        if(leftType == rightType){
            return leftType;
        }
        //Si los tipos no son iguales buscar el tipo padre del valor actual.
        else{
            temp = this.get(rightType);
            if(temp != null){
                return getOperationType(leftType, temp.getParentType());
            }  
        }
        return null;
    }
    
}
