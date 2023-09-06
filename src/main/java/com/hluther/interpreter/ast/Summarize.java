package com.hluther.interpreter.ast;

/**
 *
 * @author helmuth
 */
public class Summarize extends Node implements Instruction {
    
    private Instruction content;

    public Summarize(Instruction content, int row, int column) {
        super(row, column);
        this.content = content;
    }
    
}
