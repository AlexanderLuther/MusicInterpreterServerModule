package com.hluther.interpreter.ast.method;

import com.hluther.interpreter.ast.method.Method;
import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.interpreter.ast.instruction.Return;
import com.hluther.interpreter.ast.table.symbolTable.SymbolCategory;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.util.LinkedList;
import java.util.Stack;
/**
 *
 * @author helmuth
 */
public class Procedure extends Method implements Instruction {
    
    public Procedure(String id, LinkedList<Instruction> params, LinkedList<Instruction> instructions, int row, int column) {
        super(SymbolType.VOID, id, params, instructions, row, column);
    }
    
    public Procedure(String id, LinkedList<Instruction> instructions, int row, int column) {
        super(SymbolType.VOID, id, instructions, row, column);
    }
    

    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        boolean success;

        //Obtener el identidicador unico del procedimiento.
        String functionId = super.getIdentifier();
        
        //Apilar ambito de la funcion.
        scope.push(functionId);

        //Agregar el procedimiento a la tabla de simbolos
        success = symbolTable.addMethod(functionId, super.getSymbolType(), SymbolCategory.PROCEDURE, super.getParams().size(), super.getParamsType());
        if(!success) errors.addSemanticError(new MError("Ya existe un procedimiento con ese nombre y parametros.", super.getId(), super.getRow(), super.getColumn()));
        
        //Agregar cada uno de los parametros a la tabla de simbolos
        for(Instruction instruction: super.getParams()){
            instruction.analyze(typeTable, symbolTable, scope, errors);
        }
        
        //Analizar cada una de las instrucciones dentro del procedimiento
        for(Instruction instruction : super.getInstructions()){
            instruction.analyze(typeTable, symbolTable, scope, errors);
        }
        
        //Desapilar el ambito del procedimiento
        scope.pop();
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable){
        return null;
    }
    
}
