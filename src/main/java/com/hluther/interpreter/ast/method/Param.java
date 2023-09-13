package com.hluther.interpreter.ast.method;

import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.table.symbolTable.Symbol;
import com.hluther.interpreter.ast.table.symbolTable.SymbolCategory;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import com.hluther.interpreter.ast.track.Track;
import java.util.Stack;
/**
 *
 * @author helmuth
 */
public class Param extends Node implements Instruction{
    
    private String id;
    private SymbolType symbolType;
    private Instruction value;

    public Param(String id, SymbolType symbolType, int row, int column) {
        super(row, column);
        this.id = id;
        this.symbolType = symbolType;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public void setValue(Instruction value) {
        this.value = value;
    }
    
    
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        boolean success;
        success = symbolTable.add(symbolType, id, SymbolCategory.PARAM, scope.peek(), true);
       
        //Verificar que se inserto el parametro en la tabla de simbolos.
        if(!success){
            errors.addSemanticError(new MError("Ya existe un parametro con ese nombre.", id, super.getRow(), super.getColumn()));
        } 
        
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, Track track){
        Object val = value.execute(typeTable, symbolTable, scope, track);
        
        //Si el parametro ya esta en la tabla de simbolos solo modificar su valor
        Symbol sym = symbolTable.get(id, scope);
        if(sym != null && sym.getCategory() == SymbolCategory.PARAM){
            sym.setValue(val);
        }else{
            symbolTable.add(symbolType, id, val, SymbolCategory.PARAM, scope.peek());
        }
          
        return null;
    }
    
}
