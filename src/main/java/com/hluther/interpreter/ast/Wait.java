package com.hluther.interpreter.ast;
/**
 *
 * @author helmuth
 */
public class Wait extends Node implements Instruction{
    
    private Instruction time;
    private Instruction channel;

    public Wait(Instruction time, Instruction channel, int row, int column) {
        super(row, column);
        this.time = time;
        this.channel = channel;
    }

}
