package com.hluther.interpreter.ast.operation;
/**
 *
 * @author helmuth
 */
public class SemanticErrorMessage {
    
    static String LEFT_VOID_MATH_OP = "Operador matematico sobre operando izquierdo de tipo Void.";
    static String RIGHT_VOID_MATH_OP = "Operador matematico sobre operando derecho de tipo Void.";
    static String BOTH_VOID_MATH_OP = "Operador matematico sobre dos operandos de tipo Void.";
    static String BOTH_STRING_MATH_OP = "Operador matematico sobre dos operandos de tipo STRING.";
    static String LEFT_STRING_MATH_OP = "Operador matematico sobre operando izquierdo de tipo STRING.";
    static String RIGHT_STRING_MATH_OP = "Operador matematico sobre operando derecho de tipo STRING.";
    static String LEFT_MOD_OP = "Modulo sobre operando izquierdo de tipo ";
    static String RIGHT_MOD_OP = "Modulo sobre operando derecho de tipo ";
    static String BOTH_MOD_OP = "Modulo sobre operandos que no son de tipo entero.";
    static String BOTH_VOID_RELATIONAL_OP = "Operador relacional sobre dos operandos de tipo Void.";
    static String LEFT_VOID_RELATIONA_OP = "Operador relacional sobre operando izquierdo de tipo Void.";
    static String RIGHT_VOID_RELATIONAL_OP = "Operador relacional sobre operando derecho de tipo Void.";
    
    static String LEFT_VOID_LOGICAL_OP = "Operador logico sobre operando izquierdo de tipo Void.";
    static String RIGHT_VOID_LOGICAL_OP = "Operador logico sobre operando derecho de tipo Void.";
    
    
    static String INVALID_OPERATION = "Operacion no valida.";
    
    
}
