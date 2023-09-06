package com.hluther.interpreter.ast;
/**
 *
 * @author helmuth
 */
public class Message extends Node implements Instruction {
    
    private Instruction content;

    public Message(Instruction content, int row, int column) {
        super(row, column);
        this.content = content;
    }
        
}
