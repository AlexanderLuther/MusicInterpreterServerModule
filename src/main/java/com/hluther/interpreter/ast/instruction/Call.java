package com.hluther.interpreter.ast.instruction;

import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.operation.Operation;
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
public class Call extends Node implements Instruction{
    
    private String id;
    private LinkedList<Instruction> paramsValue;

    public Call(String id, LinkedList<Instruction> params, int row, int colum) {
        super(row, colum);
        this.id = id;
        this.paramsValue = params;
    }

    public Call(String id, int row, int column) {
        super(row, column);
        this.id = id;
        this.paramsValue = new LinkedList<>();
    }
    
    public String getCalledMehodId(LinkedList<SymbolType> types){
        String identifier = id;   
        for (SymbolType  type : types) {
            identifier+= "_" + type.toString().toLowerCase();
        }
        return identifier;
    }
    
   @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        LinkedList paramsTypes = new LinkedList<>();
        Symbol symbol;
        
       //Obtener el tipo de cada uno de los parametros y agregarlos a un listado.
        SymbolType tempType;
        for(Instruction value : paramsValue){
            tempType = (SymbolType)value.analyze(typeTable, symbolTable, scope, errors);
            paramsTypes.add(tempType);
            if(tempType == SymbolType.VOID){
                Call tempCall = ((Call)((Operation)value).getLeftOperator());
                 errors.addSemanticError(new MError("Llamada hacia un procedimiento, que no retorna ningun valor, como parametro.", tempCall.id, tempCall.getRow(), tempCall.getColumn()));
            }
        }
              
        //Si dentro del listado no hay algun tipo VOID o NOTFOUND entonces continuar con el analisis.
        if(!paramsTypes.contains(SymbolType.VOID) || !paramsTypes.contains(SymbolType.NOT_FOUND)){
            symbol = symbolTable.get(this.getCalledMehodId(paramsTypes));
            //Si se encontro un procedimiento o una funcion con el id
            if(symbol != null){
                return symbol.getType();
            }
            errors.addSemanticError(new MError("No existe ningun procedimiento o funcion con ese nombre y parametros.", id, super.getRow(), super.getColumn()));
            return SymbolType.NOT_FOUND;
        }
        return SymbolType.NOT_FOUND;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable){
        return null;
    }
    
}
