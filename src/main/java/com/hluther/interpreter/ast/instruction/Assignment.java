package com.hluther.interpreter.ast.instruction;

import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.table.symbolTable.Symbol;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import static com.hluther.interpreter.ast.table.typeTable.SymbolType.NOT_FOUND;
import static com.hluther.interpreter.ast.table.typeTable.SymbolType.VOID;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.util.Stack;
/**
 *
 * @author helmuth
 */
public class Assignment extends Node implements Instruction {
    
    private String id;
    private Instruction value;

    public Assignment(String id, Instruction value, int row, int column) {
        super(row, column);
        this.id = id;
        this.value = value;
    }
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        //Verificar que exista la variable
        Symbol symbol =  symbolTable.get(id, scope);
        if(symbol != null){
            
            //Validacion de tipos
            SymbolType valueType = (SymbolType)value.analyze(typeTable, symbolTable, scope, errors);
            switch(valueType){
                case NOT_FOUND -> {}
                
                case VOID -> { 
                    errors.addSemanticError(new MError(
                            "Error de tipos. Variable de tipo " + typeTable.get(symbol.getType()).getName() +" con asignacion tipo VOID ",
                            id,
                            super.getRow(), 
                            super.getColumn())
                        );
                }
                
                default -> {
                    //Validacion de tipos
                    if(!typeTable.hasImplicitConversion(symbol.getType(), valueType)){
                        errors.addSemanticError(new MError(
                            "Error de tipos. Variable de tipo " + typeTable.get(symbol.getType()).getName() +" con asignacion de tipo "+ typeTable.get(valueType).getName(),
                            id,
                            super.getRow(), 
                            super.getColumn())
                        );
                    }
                }    
            }
            
            symbol.setInitialized(true);
            return null;
        }
        
        errors.addSemanticError(new MError("La varaible no existe.", id, super.getRow(), super.getColumn()));
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable){
        return null;
    }
}
