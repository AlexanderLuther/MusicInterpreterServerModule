package com.hluther.interpreter.ast;

import com.hluther.interpreter.ast.table.SymbolType;
import java.util.LinkedList;

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
    
}
