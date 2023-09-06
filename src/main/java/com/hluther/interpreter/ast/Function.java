package com.hluther.interpreter.ast;

import com.hluther.interpreter.ast.table.SymbolType;
import java.util.LinkedList;

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
    
}
