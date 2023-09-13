package com.hluther.interpreter.ast.instruction;

import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.table.symbolTable.SymbolCategory;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import static com.hluther.interpreter.ast.table.typeTable.SymbolType.INTEGER;
import static com.hluther.interpreter.ast.table.typeTable.SymbolType.NOT_FOUND;
import static com.hluther.interpreter.ast.table.typeTable.SymbolType.VOID;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import com.hluther.interpreter.ast.track.Track;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author helmuth
 */
public class ArrayDeclaration extends Declaration implements Instruction{
    
    private LinkedList<Instruction> dimensions;
    private LinkedList<LinkedList<Instruction>> values;
    private int dimensionsAmount;
    
    /**
     * Declaraciones
     * @param declaration
     * @param symbolType
     * @param dimensions 
     */
    public ArrayDeclaration(Declaration declaration, SymbolType symbolType, LinkedList<Instruction> dimensions){
        super(symbolType, declaration.getId(), declaration.getRow(), declaration.getColumn());
        this.dimensions = dimensions;
        this.dimensionsAmount = dimensions.size();
        this.values = null;
    }
    
    /**
     * Declaracion y asignacion
     * @param declaration
     * @param symbolType
     * @param dimensionsAmount
     * @param values 
     */
    public ArrayDeclaration(Declaration declaration, SymbolType symbolType, int dimensionsAmount, LinkedList<LinkedList<Instruction>> values){
        super(symbolType, declaration.getId(), declaration.getRow(), declaration.getColumn());
        this.values = values;
        this.dimensionsAmount = dimensionsAmount;
    }
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        boolean success;
        SymbolType tempType;
        boolean uniform = true;
         
        //Declaracion sin inicializacion
        if(values == null){
            //Validar que los indices sean de tipo entero
            for(Instruction dimension : dimensions){
                tempType = (SymbolType)dimension.analyze(typeTable, symbolTable, scope, errors);
                switch (tempType) {
                    case NOT_FOUND, INTEGER ->{}

                    default ->{
                        errors.addSemanticError(new MError("Error en indice. Todos los indices de un arreglo deben de ser de tipo entero.", super.getId(), super.getRow(), super.getColumn()));
                    }
                }
            }
            success = symbolTable.add(super.getVarType(), super.getId(), SymbolCategory.ARRAY, dimensions.size(), scope.peek(), true);
        }

        //Declaracion con inicializacion
        else{
           int tempSize = values.getFirst().size();
            
            //Validar que las dimensiones declaradas y asignadas coincidan.
            if(dimensionsAmount != values.size()){
                errors.addSemanticError(new MError("La cantidad de dimensiones declaradas no coincide con las asignadas.", super.getId(), super.getRow(), super.getColumn()));
            }
            
            //Iterar por cada uno de los elementos en cada una de las dimensiones del arreglo.
            for(LinkedList<Instruction> dimension : values){
             
                for(Instruction value : dimension){
                
                    //Validacion de tipos
                    SymbolType valueType = (SymbolType)value.analyze(typeTable, symbolTable, scope, errors);
                    switch(valueType){
                        case NOT_FOUND -> {}

                        case VOID -> { 
                            errors.addSemanticError(new MError(
                                    "Error de tipos. Arreglo de tipo " + typeTable.get(super.getVarType()).getName() +" con asignacion de tipo VOID ",
                                    super.getId(),
                                    super.getRow(), 
                                    super.getColumn())
                                );
                        }

                        default -> {
                            //Validacion de tipos
                            if(!typeTable.hasImplicitConversion(super.getVarType(), valueType)){
                                errors.addSemanticError(new MError(
                                    "Error de tipos. Arreglo de tipo " + typeTable.get(super.getVarType()).getName() +" con asignacion de tipo "+ typeTable.get(valueType).getName(),
                                    super.getId(),
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
                    errors.addSemanticError(new MError("Todas las dimensiones deben de tener la misma cantidad de elementos.", super.getId(), super.getRow(), super.getColumn()));
                }
            }

            success = symbolTable.add(super.getVarType(), super.getId(), SymbolCategory.ARRAY, values.size() , scope.peek(), true);
        }
          
        //Verificar que se realizo la declaracion.
        if(!success){
            errors.addSemanticError(new MError("Ya existe una variable con ese nombre.", super.getId(), super.getRow(), super.getColumn()));
        } 
        
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, Track track){
        LinkedList dimensionsSize = new LinkedList();
        int length = 1;
        int tempSize;

        //Declaracion
        if(values == null){
            //Obtener longitud total del arreglo
            for(Instruction instruction : dimensions){
                tempSize = (int)instruction.execute(typeTable, symbolTable, scope, track);
                length = length * tempSize;
                dimensionsSize.add(tempSize);
            }
            symbolTable.add(super.getVarType(), super.getId(), new Object[length], dimensionsSize, SymbolCategory.ARRAY, dimensionsAmount, scope.peek(), false);
        } 
        
        //Declaracion y asignacion
        else{
            //Obtener longitud total del arreglo
            length = values.size() * values.getFirst().size();
            
            int counter = 0;
            Object array[] = new Object[length];
      
            //Insetar valores en el arreglo plano
            for(LinkedList<Instruction> dimension : values){
                for(Instruction instruction : dimension){
                    array[counter] = instruction.execute(typeTable, symbolTable, scope, track);
                    counter++;
                }
            }
            
            //Insertar dimensiones en la lista de dimensiones
            for (LinkedList<Instruction> value : values) {
                dimensionsSize.add(values.getFirst().size());
            }
            
            symbolTable.add(super.getVarType(), super.getId(), array, dimensionsSize, SymbolCategory.ARRAY, dimensionsAmount, scope.peek(), true);
        }
        
        return null;
    }
    
}
