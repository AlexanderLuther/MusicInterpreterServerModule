package com.hluther.interpreter.ast.instruction;

import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.table.symbolTable.Symbol;
import com.hluther.interpreter.ast.table.symbolTable.SymbolCategory;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import static com.hluther.interpreter.ast.table.typeTable.SymbolType.INTEGER;
import static com.hluther.interpreter.ast.table.typeTable.SymbolType.NOT_FOUND;
import static com.hluther.interpreter.ast.table.typeTable.SymbolType.VOID;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author helmuth
 */
public class ArrayAssignment extends Node implements Instruction{
    
    private String id;
    private LinkedList<Instruction> dimensions;
    private Instruction value;

    public ArrayAssignment(String id, LinkedList<Instruction> dimensions, Instruction value, int row, int column) {
        super(row, column);
        this.id = id;
        this.dimensions = dimensions;
        this.value = value;
    }
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        //Verificar que exista la variable
        Symbol symbol =  symbolTable.get(id, scope);
        if(symbol != null){
            //Validar que sea un arreglo
            if(symbol.getCategory() != SymbolCategory.ARRAY){
                errors.addSemanticError(new MError("La variable no es un arreglo.", id, super.getRow(), super.getColumn()));
            } else{
                //Validar cantidad de indices del arreglo con indices proporcionados.
                if(dimensions.size() != symbol.getDimensionsAmount()){
                    errors.addSemanticError(new MError("La cantidad de dimensiones proporcionada no coincide con la cantidad de dimensiones del arreglo.", id, super.getRow(), super.getColumn()));
                }
            }
            
            //Validacion de tipos
            SymbolType valueType = (SymbolType)value.analyze(typeTable, symbolTable, scope, errors);
            switch(valueType){
                case NOT_FOUND -> {}
                
                case VOID -> { 
                    errors.addSemanticError(new MError(
                            "Error de tipos. Arreglo de tipo " + typeTable.get(symbol.getType()).getName() +" con asignacion de tipo VOID ",
                            id,
                            super.getRow(), 
                            super.getColumn())
                        );
                }
                
                default -> {
                    //Validacion de tipos
                    if(!typeTable.hasImplicitConversion(symbol.getType(), valueType)){
                        errors.addSemanticError(new MError(
                            "Error de tipos. Arreglo de tipo " + typeTable.get(symbol.getType()).getName() +" con asignacion de tipo "+ typeTable.get(valueType).getName(),
                            id,
                            super.getRow(), 
                            super.getColumn())
                        );
                    }
                }    
            }
            
            symbol.setInitialized(true);
        }
        
        //Validar que los indices sean de tipo entero
        SymbolType dimensionType;
        for(Instruction dimension : dimensions){
            dimensionType = (SymbolType)dimension.analyze(typeTable, symbolTable, scope, errors);
            switch (dimensionType) {
                case NOT_FOUND, INTEGER ->{}

                default ->{
                    errors.addSemanticError(new MError("Error en indice. Todos los indices de un arreglo deben de ser de tipo entero.", id, super.getRow(), super.getColumn()));
                }
            }
        }
        
        if(symbol == null){
            errors.addSemanticError(new MError("El arreglo no existe.", id, super.getRow(), super.getColumn()));
        }
        
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable){
        return null;
    }
    
}
