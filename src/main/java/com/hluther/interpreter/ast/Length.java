package com.hluther.interpreter.ast;
/**
 *
 * @author helmuth
 */
public class Length extends Node implements Instruction {
    
    private Instruction content;

    public Length(Instruction content, int row, int column) {
        super(row, column);
        this.content = content;
    }
    
}
