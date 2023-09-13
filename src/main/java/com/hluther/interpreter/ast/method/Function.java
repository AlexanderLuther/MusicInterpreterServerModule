package com.hluther.interpreter.ast.method;

import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.instruction.Return;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.interpreter.ast.table.symbolTable.SymbolCategory;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import com.hluther.interpreter.ast.track.Track;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author helmuth
 */
public class Function extends Method implements Instruction {
    
    public Function(SymbolType symbolType, String id, LinkedList<Instruction> params, LinkedList<Instruction> instructions, int row, int column) {
        super(symbolType, id, params, instructions, row, column);
    }
    
    public Function(SymbolType symbolType, String id, LinkedList<Instruction> instructions, int row, int column) {
        super(symbolType, id, instructions, row, column);
    }
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        boolean success;
        int returnCounter = 0;
        
        //Obtener el identidicador unico de la funcion.
        String functionId = super.getIdentifier();
        
        //Apilar ambito de la funcion.
        scope.push(functionId);

        //Agregar la funcion a la tabla de simbolos
        success = symbolTable.addMethod(functionId, super.getSymbolType(), SymbolCategory.FUNCTION, super.getParams().size(), super.getParamsType());
        if(!success) errors.addSemanticError(new MError("Ya existe una funcion con ese nombre y parametros.", super.getId(), super.getRow(), super.getColumn()));
        
        //Agregar cada uno de los parametros a la tabla de simbolos
        for(Instruction instruction: super.getParams()){
            instruction.analyze(typeTable, symbolTable, scope, errors);
        }
        
        //Analizar cada una de las instrucciones dentro de la funcion
        for(Instruction instruction : super.getInstructions()){
            if(instruction instanceof Return) returnCounter++;
            instruction.analyze(typeTable, symbolTable, scope, errors);
        }
        
        //Validar que exista un valor de retorno
        if(returnCounter == 0){
            errors.addSemanticError(new MError("Funcion sin retorno. Se debe proporcionar un valor de retorno al final de la funcion.", super.getId(), super.getRow(), super.getColumn()));
        }
        
        //Validar que el valor de retorno este al final
        if(!super.getInstructions().isEmpty()){
            if(returnCounter == 1 && !(super.getInstructions().getLast() instanceof Return)){
                errors.addSemanticError(new MError("Instrucciones depues del valor de retorno. Las instucciones declaradas despues del valor de retorno no seran ejecutadas.", super.getId(), super.getRow(), super.getColumn()));
            }
        }
        
        if(returnCounter > 1){
            errors.addSemanticError(new MError("Instrucciones depues del valor de retorno. Las instucciones declaradas despues del valor de retorno no seran ejecutadas.", super.getId(), super.getRow(), super.getColumn()));
        }
        
        //Desapilar el ambito de la funcion
        scope.pop();
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, Track track){
       Object returnValue = null;
        
        //Apilar ambito
        scope.push(super.getIdentifier());
        
        //Establecer el valor de cada uno de los parametros
        for(int i=0; i < super.getParams().size(); i++){
            ((Param)super.getParams().get(i)).setValue(super.getParamsValues().get(i));
        }
        
        //Ingresar parametros a la tabla de simbolos
        for(Instruction param : super.getParams()){
            param.execute(typeTable, symbolTable, scope, track);
        }
        
        //Ejecutar instrucciones
        for(Instruction instruction : super.getInstructions()){
            //Capturar el valor de retorno
            if(instruction instanceof Return){
                returnValue = instruction.execute(typeTable, symbolTable, scope, track);
            }else{
                 instruction.execute(typeTable, symbolTable, scope, track);
            }
        }
        
        //Desapilar ambito
        scope.pop();
        return returnValue;
    }
}
