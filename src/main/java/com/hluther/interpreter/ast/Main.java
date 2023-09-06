package com.hluther.interpreter.ast;

import com.hluther.interpreter.ast.table.SymbolType;
import java.util.LinkedList;
/**
 *
 * @author helmuth
 */
public class Main extends Method implements Instruction{
        
    public Main(String id, LinkedList<Instruction> instructions, int row, int column) {
        super(SymbolType.VOID, id, instructions, row, column);
    }
    
}
