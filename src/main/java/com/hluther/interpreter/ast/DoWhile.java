package com.hluther.interpreter.ast;

import java.util.LinkedList;

/**
 *
 * @author helmuth
 */
public class DoWhile extends Node implements Instruction{
    
    private Instruction condition;
    private LinkedList<Instruction> instructions;

    public DoWhile(Instruction conditional, LinkedList<Instruction> instructions, int row, int column) {
        super(row, column);
        this.condition = conditional;
        this.instructions = instructions;
    }
        
}
