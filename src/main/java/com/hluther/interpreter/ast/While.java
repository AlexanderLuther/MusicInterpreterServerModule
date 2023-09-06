package com.hluther.interpreter.ast;

import java.util.LinkedList;

/**
 *
 * @author helmuth
 */
public class While extends Node implements Instruction{
    
    private Instruction condition;
    private LinkedList<Instruction> instructions;

    public While(Instruction condition, LinkedList<Instruction> instructions, int row, int column) {
        super(row, column);
        this.condition = condition;
        this.instructions = instructions;
    }
    
    
    
}
