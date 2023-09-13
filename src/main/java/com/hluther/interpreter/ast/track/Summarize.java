package com.hluther.interpreter.ast.track;

import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.table.symbolTable.Symbol;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author helmuth
 */
public class Summarize extends Node implements Instruction {
    
    private Instruction content;
    private LinkedList<LinkedList<Instruction>> arrayContent;

    public Summarize(Instruction content, int row, int column) {
        super(row, column);
        this.content = content;
        this.arrayContent = null;
    }
    
    public Summarize(LinkedList<LinkedList<Instruction>> arrayContent, int row, int column) {
        super(row, column);
        this.arrayContent = arrayContent;
        this.content = null;
    }
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        SymbolType tempType = null;
        if(content != null){
            content.analyze(typeTable, symbolTable, scope, errors);
        } else{
            if(arrayContent.size() > 1){
                errors.addSemanticError(new MError("Sumarizacion no valida.", "Solo se permiten arreglos de una dimension.", super.getRow(), super.getColumn()));
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
                                    "Sumarizar",
                                    super.getRow(), 
                                    super.getColumn())
                                );
                        }

                        default -> {
                            //Validacion de tipos
                            if(!typeTable.hasImplicitConversion(tempType, valueType)){
                                errors.addSemanticError(new MError(
                                    "Error de tipos. Arreglo de tipo " + typeTable.get(tempType).getName() +" con asignacion de tipo "+ typeTable.get(valueType).getName(),
                                    "Sumarizar",
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
                    errors.addSemanticError(new MError("Todas las dimensiones deben de tener la misma cantidad de elementos.", "Sumarizar", super.getRow(), super.getColumn()));
                }
            }
        }
        
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, Track track){
        if(arrayContent == null){
            Object[] array = (Object[])content.execute(typeTable, symbolTable, scope, track);

            if(array[0]  instanceof Double){
                double value= 0; 
                for(int i=0; i< array.length; i++){
                    value = value + (double)array[i];
                }
                return String.valueOf(value);
            }
            
            if(array[0] instanceof Integer){
                int value= 0; 
                for(int i=0; i<array.length; i++){
                    value = value + (int)array[i];
                }
                 return String.valueOf(value);
            }
            
            if(array[0] instanceof Character){
                int value = 0; 
                for(int i=0; i<array.length; i++){
                    value = value + (char)array[i];
                }
                 return String.valueOf(value);
            }
            
            if(array[0] instanceof Boolean){
                int value = 0; 
                for(int i=0; i<array.length; i++){
                    int temp = (boolean)(boolean)array[i]? 1 : 0;
                    value = value + temp;
                }
                return String.valueOf(value);
            }
            
            if(array[0] instanceof String){
                String value="";
                 for(int i=0; i<array.length; i++){
                    value = value + array[i].toString();
                }
                 return  value;
            }
            
            
        } else{
            if(arrayContent.getFirst().getFirst().execute(typeTable, symbolTable, scope, track) instanceof Double){
                double value= 0; 
                for(int i=0; i<arrayContent.getFirst().size(); i++){
                    value = value + (double)arrayContent.getFirst().get(i).execute(typeTable, symbolTable, scope, track);
                }
                return String.valueOf(value);
            }
            
            if(arrayContent.getFirst().getFirst().execute(typeTable, symbolTable, scope, track) instanceof Integer){
                int value= 0; 
                for(int i=0; i<arrayContent.getFirst().size(); i++){
                    value = value + (int)arrayContent.getFirst().get(i).execute(typeTable, symbolTable, scope, track);
                }
                 return String.valueOf(value);
            }
            
            if(arrayContent.getFirst().getFirst().execute(typeTable, symbolTable, scope, track) instanceof Character){
                int value = 0; 
                for(int i=0; i<arrayContent.getFirst().size(); i++){
                    value = value + (char)arrayContent.getFirst().get(i).execute(typeTable, symbolTable, scope, track);
                }
                 return String.valueOf(value);
            }
            
            if(arrayContent.getFirst().getFirst().execute(typeTable, symbolTable, scope, track) instanceof Boolean){
                int value = 0; 
                for(int i=0; i<arrayContent.getFirst().size(); i++){
                    int temp = (boolean)(boolean)arrayContent.getFirst().get(i).execute(typeTable, symbolTable, scope, track) ? 1 : 0;
                    value = value + temp;
                }
                return String.valueOf(value);
            }
            
            if(arrayContent.getFirst().getFirst().execute(typeTable, symbolTable, scope, track) instanceof String){
                String value="";
                 for(int i=0; i<arrayContent.getFirst().size(); i++){
                    value = value + arrayContent.getFirst().get(i).execute(typeTable, symbolTable, scope, track).toString();
                }
                 return  value;
            }
        
        }
        return null;
    }
}
