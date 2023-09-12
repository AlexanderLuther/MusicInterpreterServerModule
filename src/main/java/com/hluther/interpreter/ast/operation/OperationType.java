package com.hluther.interpreter.ast.operation;
/**
 *
 * @author helmuth
 */
public enum OperationType {
    INTEGER,
    DOUBLE,
    CHAR,
    BOOLEAN,
    STRING,
    ID,
    ARRAY,
    ALL_ARRAY,
    CALL,
    PLUS,
    MINUS,
    SUM,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION,
    MOD,
    POTENTIATION, 
    ISEQUAL,
    NOTEQUAL,
    GREATERTHAN,
    LESSTHAN,
    GREATERTHANOREQUALTO,
    LESSTHANOREQUALTO,
    ISNULL,
    AND,
    NAND,
    OR,
    NOR,
    XOR,
    NOT
}
