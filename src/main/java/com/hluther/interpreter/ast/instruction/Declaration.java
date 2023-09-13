package com.hluther.interpreter.ast.instruction;

import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
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
public class Declaration extends Node implements Instruction{
    
    private SymbolType varType;
    private String id;
    private Instruction value;
    private boolean keep;

    /**
     * Declaracion sin inicializacion
     * @param symbolType
     * @param id
     * @param row
     * @param column 
     */
    public Declaration(SymbolType symbolType, String id, int row, int column) {
        super(row, column);
        this.varType = symbolType;
        this.id = id;
        this.value = null;
        this.keep = false;
    }

    /**
     * Declaracion con inicializacion
     * @param symbolType
     * @param id
     * @param value
     * @param row
     * @param column 
     */
    public Declaration(SymbolType symbolType, String id, Instruction value, int row, int column) {
        super(row, column);
        this.varType = symbolType;
        this.id = id;
        this.value = value;
        this.keep = false;
    }

    public Declaration(String id, int row, int column) {
        super(row, column);
        this.id = id;
        this.value = null;
        this.keep = false;
    }

    public void setSymbolType(SymbolType symbolType) {
        this.varType = symbolType;
    }

    public void setKeep(boolean keep) {
        this.keep = keep;
    }

    public void setValue(Instruction value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SymbolType getVarType() {
        return varType;
    }
    
    /**
     * Ejecuta analisis semantico.
     * @param typeTable
     * @param symbolTable
     * @param scope
     * @param errors
     * @return 
     */
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        boolean success;

        //Declaracion sin inicializacion
        if(value == null) success = symbolTable.add(varType, id, SymbolCategory.VAR, scope.peek(), false);
        
        
        //Declaracion con inicializacion
        else{
            //Validacion de tipos
            SymbolType valueType = (SymbolType)value.analyze(typeTable, symbolTable, scope, errors);
            switch(valueType){
                case NOT_FOUND -> {}
                
                case VOID -> { 
                    errors.addSemanticError(new MError(
                            "Error de tipos. Variable de tipo " + typeTable.get(varType).getName() +" con asignacion de tipo VOID ",
                            id,
                            super.getRow(), 
                            super.getColumn())
                        );
                }
                
                default -> {
                    //Validacion de tipos
                    if(!typeTable.hasImplicitConversion(varType, valueType)){
                        errors.addSemanticError(new MError(
                            "Error de tipos. Variable de tipo " + typeTable.get(varType).getName() +" con asignacion de tipo "+ typeTable.get(valueType).getName(),
                            id,
                            super.getRow(), 
                            super.getColumn())
                        );
                    }
                }    
            }
            
            success = symbolTable.add(varType, id, SymbolCategory.VAR, scope.peek(), true);
        }
        
        //Verificar que se realizo la declaracion.
        if(!success){
            errors.addSemanticError(new MError("Ya existe una variable con ese nombre.", id, super.getRow(), super.getColumn()));
        } 
    
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, Track track){
        if(value == null){
            symbolTable.add(varType, id, scope.peek());
        } else{
            Object val = value.execute(typeTable, symbolTable, scope, track);
            symbolTable.add(varType, id, val, SymbolCategory.VAR, scope.peek());
        }
        return null;
    }
    
}
