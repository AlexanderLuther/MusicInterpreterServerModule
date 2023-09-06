package com.hluther.interpreter.ast;
/**
 *
 * @author helmuth
 */
public class Order extends Node implements Instruction{
    
    private Instruction content;
    private OrderType orderType;

    public Order(Instruction content, OrderType orderType, int row, int column) {
        super(row, column);
        this.content = content;
        this.orderType = orderType;
    }
    
}
