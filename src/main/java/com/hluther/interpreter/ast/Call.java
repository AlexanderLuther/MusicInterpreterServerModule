package com.hluther.interpreter.ast;

import java.util.LinkedList;
/**
 *
 * @author helmuth
 */
public class Call extends Node implements Instruction{
    
    private String id;
    private LinkedList<Instruction> params;

    public Call(String id, LinkedList<Instruction> params, int row, int colum) {
        super(row, colum);
        this.id = id;
        this.params = params;
    }

    public Call(String id, int row, int column) {
        super(row, column);
        this.id = id;
        this.params = new LinkedList<>();
    }
    
   
    
}
