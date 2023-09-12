package com.hluther.interpreter.ast.instruction;

import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.table.symbolTable.Symbol;
import com.hluther.interpreter.ast.table.symbolTable.SymbolCategory;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.util.Stack;
/**
 *
 * @author helmuth
 */
public class Return extends Node implements Instruction{
    
    private Instruction value;

    public Return(Instruction value, int row, int column) {
        super(row, column);
        this.value = value;
    }
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        SymbolType tempType = (SymbolType)value.analyze(typeTable, symbolTable, scope, errors);
        Symbol sym = symbolTable.getLastMethod(scope);
        
        //Analizar si se se esta dentro de un procedimiento o dentro de una funcion 
        if(sym.getCategory() == SymbolCategory.PROCEDURE){
          errors.addSemanticError(new MError("Retorno no valido.", "Retorno en un procedimiento.", super.getRow(), super.getColumn()));
        } else{
            //Analizar valor de retorno
            if(tempType != SymbolType.NOT_FOUND){
                if(tempType == SymbolType.VOID){
                    errors.addSemanticError(new MError("Retorno no valido.", "Retorno  de tipo Void", super.getRow(),super.getColumn()));
                } else{
                    //Validacion de tipos
                    if(!typeTable.hasImplicitConversion(sym.getType(), tempType)){
                        errors.addSemanticError(new MError(
                            "Error de tipos. Funcion de tipo " + typeTable.get(sym.getType()).getName() +" con retorno de tipo "+ typeTable.get(tempType).getName(),
                            "Retorno",
                            super.getRow(), 
                            super.getColumn())
                        );
                    }
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
