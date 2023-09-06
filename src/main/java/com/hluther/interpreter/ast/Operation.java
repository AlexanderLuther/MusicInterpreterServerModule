package com.hluther.interpreter.ast;

import java.util.LinkedList;
/**
 *
 * @author helmuth
 */
public class Operation implements Instruction {
    
    private OperationType operationType;
    private Instruction leftOperator;
    private Instruction rightOperator;
    private LinkedList<Instruction> dimensions;
    private Object value;
    private int lRow;
    private int lColumn;
    private int rRow;
    private int rColumn;
     
    /**
     * Operaciones Binarias
     * @param leftOperator
     * @param rightOperator
     * @param operationType
     * @param lRow
     * @param lColumn
     * @param rRow
     * @param rColumn 
     */
    public Operation(Instruction leftOperator, Instruction rightOperator, OperationType operationType, int lRow, int lColumn, int rRow, int rColumn) {
        this.leftOperator = leftOperator;
        this.rightOperator = rightOperator;
        this.operationType = operationType;
        this.lRow = lRow;
        this.lColumn = lColumn;
        this.rRow = rRow;
        this.rColumn = rColumn;
    }
    
    /**
     * Operaciones Unarias
     * @param operator
     * @param operationType
     * @param row
     * @param column 
     */
    public Operation(Instruction operator, OperationType operationType, int row, int column) {
        this.leftOperator = operator;
        this.operationType = operationType;
        this.lRow = row;
        this.lColumn = column;
    }
    
    /**
     * INTEGER
     * @param value
     * @param row
     * @param column 
     */
    public Operation(Integer value, int row, int column){
        this.value = value;
        this.lRow = row;
        this.lColumn = column;
        this.operationType = OperationType.INTEGER;
    }
    
    /**
     * DOUBLE
     * @param value
     * @param row
     * @param column 
     */
    public Operation(Double value, int row, int column){
        this.value = value;
        this.lRow = row;
        this.lColumn = column;
        this.operationType = OperationType.DOUBLE;
    }
    
    /**
     * CHARACTER
     * @param value
     * @param row
     * @param column 
     */
    public Operation(Character value, int row, int column){
        this.value = value;
        this.lRow = row;
        this.lColumn = column;
        this.operationType = OperationType.CHAR;
    }
    
    /**
     * BOOLEAN
     * @param value
     * @param row
     * @param column 
     */
    public Operation(Boolean value, int row, int column){
        this.value = value;
        this.lRow = row;
        this.lColumn = column;
        this.operationType = OperationType.BOOLEAN;
    }
    
    /**
     * STRING | ID | CALL
     * @param value
     * @param operationType
     * @param row
     * @param column 
     */
    public Operation(String value, OperationType operationType, int row, int column){
        this.value = value;
        this.lRow = row;
        this.lColumn = column;
        this.operationType = operationType;
        System.out.println(operationType.toString().toLowerCase());
    }
    
    /**
     * ARRAY
     * @param value 
     * @param dimensions 
     * @param row
     * @param column 
     */
    public Operation(String value, LinkedList<Instruction> dimensions,  int row, int column){
        this.value = value;
        this.dimensions = dimensions;
        this.lRow = row;
        this.lColumn = column;
        this.operationType = OperationType.ARRAY;
    }
    

    public OperationType getType() {
        return operationType;
    }

    public Object getValue() {
        return value;
    }

    
    
}
