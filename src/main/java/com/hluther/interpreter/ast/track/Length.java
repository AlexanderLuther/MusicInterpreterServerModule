package com.hluther.interpreter.ast.track;

import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import static com.hluther.interpreter.ast.table.typeTable.SymbolType.NOT_FOUND;
import static com.hluther.interpreter.ast.table.typeTable.SymbolType.VOID;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.util.LinkedList;
import java.util.Stack;
/**
 *
 * @author helmuth
 */
public class Length extends Node implements Instruction {
    
    private Instruction content;
    private LinkedList<LinkedList<Instruction>> arrayContent;

    public Length(Instruction content, int row, int column) {
        super(row, column);
        this.content = content;
        this.arrayContent = null;
    }

    public Length(LinkedList<LinkedList<Instruction>> arrayContent, int row, int column) {
        super(row, column);
        this.arrayContent = arrayContent;
        this.content = null;
    }
    
    
 
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        SymbolType tempType = null;
        if(content != null){
            tempType = (SymbolType)content.analyze(typeTable, symbolTable, scope, errors);
            if(tempType != SymbolType.STRING  && tempType != SymbolType.NOT_FOUND){
                 errors.addSemanticError(new MError("Longitud no valida.", "Solo se permiten variables de tipo Cadena o Cadenas", super.getRow(), super.getColumn()));
            }
        } else{
            if(arrayContent.size() > 1){
                errors.addSemanticError(new MError("Longitud no valido.", "Solo se permiten arreglos de una dimension.", super.getRow(), super.getColumn()));
            }
            
            int counter = 0;
            boolean uniform = true;
            int tempSize = arrayContent.getFirst().size();
            //Iterar por cada uno de los elementos en cada una de las dimensiones del arreglo.
            for(LinkedList<Instruction> dimension : arrayContent){
             
                for(Instruction value : dimension){
                
                    //Validacion de tipos
                    SymbolType valueType = (SymbolType)value.analyze(typeTable, symbolTable, scope, errors);
                    if(counter == 0){
                        tempType = valueType;
                        counter++;
                    }
                    
                    switch(valueType){
                        case NOT_FOUND -> {}

                        case VOID -> { 
                            errors.addSemanticError(new MError(
                                    "Error de tipos. Arreglo de tipo " + typeTable.get(tempType).getName() +" con asignacion de tipo VOID ",
                                    "Longitud",
                                    super.getRow(), 
                                    super.getColumn())
                                );
                        }

                        default -> {
                            //Validacion de tipos
                            if(!typeTable.hasImplicitConversion(tempType, valueType)){
                                errors.addSemanticError(new MError(
                                    "Error de tipos. Arreglo de tipo " + typeTable.get(tempType).getName() +" con asignacion de tipo "+ typeTable.get(valueType).getName(),
                                    "Longitud",
                                    super.getRow(), 
                                    super.getColumn())
                                );
                            }
                        }    
                    }
                }
                //Validacion de uniformidad entre dimensiones
                if(uniform && tempSize != dimension.size()){
                    uniform = false;
                    errors.addSemanticError(new MError("Todas las dimensiones deben de tener la misma cantidad de elementos.", "Longitud", super.getRow(), super.getColumn()));
                }
            }
        }
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable){
        return null;
    }
    
}
