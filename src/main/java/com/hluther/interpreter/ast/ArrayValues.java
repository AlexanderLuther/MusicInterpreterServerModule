package com.hluther.interpreter.ast;

import java.util.LinkedList;
/**
 *
 * @author helmuth
 */
public class ArrayValues implements Instruction{
    
    private LinkedList<LinkedList<Instruction>> values;

    public ArrayValues(LinkedList<LinkedList<Instruction>> values) {
        this.values = values;
    }

    
}
