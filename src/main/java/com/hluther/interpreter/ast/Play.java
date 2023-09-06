package com.hluther.interpreter.ast;
/**
 *
 * @author helmuth
 */
public class Play extends Node implements Instruction {
    
    private String note;
    private Instruction eighth;
    private Instruction time;
    private Instruction channel;

    public Play(String note, Instruction eighth, Instruction time, Instruction channel, int row, int column) {
        super(row, column);
        this.note = note;
        this.eighth = eighth;
        this.time = time;
        this.channel = channel;
    }
    
    
    
}
