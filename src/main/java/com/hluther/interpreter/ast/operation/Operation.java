package com.hluther.interpreter.ast.operation;

import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.entity.AnalysisError;
import com.hluther.entity.MError;
import com.hluther.interpreter.ast.track.Message;
import com.hluther.interpreter.ast.track.Order;
import com.hluther.interpreter.ast.track.Wait;
import com.hluther.interpreter.ast.track.Play;
import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.table.symbolTable.Symbol;
import com.hluther.interpreter.ast.table.symbolTable.SymbolCategory;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import com.hluther.interpreter.ast.track.Length;
import com.hluther.interpreter.ast.track.Summarize;
import com.hluther.interpreter.ast.track.Track;
import java.util.LinkedList;
import java.util.Stack;
/**
 *
 * @author helmuth
 */
public class Operation extends Node implements Instruction {
    
    private OperationType operationType;
    private Instruction leftOperator;
    private Instruction rightOperator;
    private LinkedList<Instruction> dimensions;
    private Object value;
     
    /**
     * Operaciones Binarias
     * @param leftOperator
     * @param rightOperator
     * @param operationType
     * @param row
     * @param column
     */
    public Operation(Instruction leftOperator, Instruction rightOperator, OperationType operationType, int row, int column) {
        super(row, column);
        this.leftOperator = leftOperator;
        this.rightOperator = rightOperator;
        this.operationType = operationType;
    }
    
    /**
     * Operaciones Unarias
     * @param operator
     * @param operationType
     * @param row
     * @param column 
     */
    public Operation(Instruction operator, OperationType operationType, int row, int column) {
        super(row, column);
        this.leftOperator = operator;
        this.operationType = operationType;
    }
    
    /**
     * INTEGER
     * @param value
     * @param row
     * @param column 
     */
    public Operation(Integer value, int row, int column){
        super(row, column);
        this.value = value;
        this.operationType = OperationType.INTEGER;
    }
    
    /**
     * DOUBLE
     * @param value
     * @param row
     * @param column 
     */
    public Operation(Double value, int row, int column){
        super(row, column);
        this.value = value;
        this.operationType = OperationType.DOUBLE;
    }
    
    /**
     * CHARACTER
     * @param value
     * @param row
     * @param column 
     */
    public Operation(Character value, int row, int column){
        super(row, column);
        this.value = value;
        this.operationType = OperationType.CHAR;
    }
    
    /**
     * BOOLEAN
     * @param value
     * @param row
     * @param column 
     */
    public Operation(Boolean value, int row, int column){
        super(row, column);
        this.value = value;
        this.operationType = OperationType.BOOLEAN;
    }
    
    /**
     * STRING | ID | CALL | ARRAY_ALL | NULL_VAR
     * @param value
     * @param operationType
     * @param row
     * @param column 
     */
    public Operation(String value, OperationType operationType, int row, int column){
        super(row, column);
        this.value = value;
        this.operationType = operationType;
        System.out.println(operationType.toString().toLowerCase());
    }
    
    /**
     * NULL_VAR
     * @param value 
     * @param dimensions 
     * @param row
     * @param column 
     */
    public Operation(String value, LinkedList<Instruction> dimensions, OperationType operationType, int row, int column){
        super(row, column);
        this.value = value;
        this.dimensions = dimensions;
        this.operationType = operationType;
    }
    
    /**
     * ARRAY
     * @param value 
     * @param dimensions 
     * @param row
     * @param column 
     */
    public Operation(String value, LinkedList<Instruction> dimensions,  int row, int column){
        super(row, column);
        this.value = value;
        this.dimensions = dimensions;
        this.operationType = OperationType.ARRAY;
    }
    

    public OperationType getType() {
        return operationType;
    }

    public Object getValue() {
        return value;
    }

    public Instruction getLeftOperator() {
        return leftOperator;
    }

    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        Symbol temp;
        SymbolType tempType;
        SymbolType tempType2;
        
        switch(operationType){
            case INTEGER -> {
                return SymbolType.INTEGER; 
            }
                
            case DOUBLE -> {
                return SymbolType.DOUBLE;
            }
            
            case CHAR -> {
                return SymbolType.CHARACTER;
            }
                
            case BOOLEAN -> {
                return SymbolType.BOOLEAN; 
            }
                
            case STRING -> {
                return SymbolType.STRING; 
            }
            
            case ALL_ARRAY ->{
                temp = symbolTable.get(value.toString(), scope);
                if(temp != null){
                    if(temp.getCategory() != SymbolCategory.ARRAY){
                        errors.addSemanticError(new MError("El identificador proporcionado no pertenece a un arreglo.", value.toString(), super.getRow(), super.getColumn()));
                        return SymbolType.NOT_FOUND; 
                    }
                    return temp.getType();
                }
                errors.addSemanticError(new MError("Arreglo no existe", value.toString(), super.getRow(), super.getColumn()));
                return SymbolType.NOT_FOUND;
            }
                
            case ID -> { 
                temp = symbolTable.get(value.toString(), scope);
                if(temp != null){
                    if(temp.getCategory() == SymbolCategory.ARRAY){
                        errors.addSemanticError(new MError("No se puede acceder a los valores de un arreglo sin proporcionar algun indice.", value.toString(), super.getRow(), super.getColumn()));
                    }
                    if(!temp.isInitialized()){
                        errors.addSemanticError(new MError("Variable no inicializada.", value.toString(), super.getRow(), super.getColumn()));
                    } 
                    return temp.getType();
                }
                errors.addSemanticError(new MError("Variable no existe", value.toString(), super.getRow(), super.getColumn()));
                return SymbolType.NOT_FOUND;
            }
            
            case NULL_VAR ->{
                temp = symbolTable.get(value.toString(), scope);
                if(temp != null){
                    if(temp.getCategory() == SymbolCategory.ARRAY){
                        //Validar cantidad de indices del arreglo con indices proporcionados.
                        if(dimensions.size() != temp.getDimensionsAmount()){
                            errors.addSemanticError(new MError("La cantidad de dimensiones proporcionada no coincide con la cantidad de dimensiones del arreglo.", value.toString(), super.getRow(), super.getColumn()));
                        }
                    }
                    
                    return SymbolType.BOOLEAN;
                }
                errors.addSemanticError(new MError("Variable no existe", value.toString(), super.getRow(), super.getColumn()));
                return SymbolType.NOT_FOUND;
            }
            
            case CALL ->{
                if(leftOperator instanceof Play || leftOperator instanceof Wait || leftOperator instanceof Order || leftOperator instanceof Length) {
                    leftOperator.analyze(typeTable, symbolTable, scope, errors);
                    return SymbolType.INTEGER;
                }
                
                if(leftOperator instanceof Summarize) {
                    leftOperator.analyze(typeTable, symbolTable, scope, errors);
                    return SymbolType.STRING;
                }
                
                if(leftOperator instanceof Message) {
                    leftOperator.analyze(typeTable, symbolTable, scope, errors);
                    return SymbolType.VOID;
                }
                                
                return leftOperator.analyze(typeTable, symbolTable, scope, errors);
            }
            
            case ARRAY ->{
                temp = symbolTable.get(value.toString(), scope);
                //Validar que el arreglo exista
                if(temp != null){
                    //Validar que sea un arreglo
                    if(temp.getCategory() != SymbolCategory.ARRAY){
                        errors.addSemanticError(new MError("La variable no es un arreglo.", value.toString(), super.getRow(), super.getColumn()));
                    } else{
                        //Validar cantidad de indices del arreglo con indices proporcionados.
                        if(dimensions.size() != temp.getDimensionsAmount()){
                            errors.addSemanticError(new MError("La cantidad de dimensiones proporcionada no coincide con la cantidad de dimensiones del arreglo.", value.toString(), super.getRow(), super.getColumn()));
                        }
                    }
                }
                
                //Validar que los indices sean de tipo entero
                SymbolType dimensionType;
                for(Instruction dimension : dimensions){
                    dimensionType = (SymbolType)dimension.analyze(typeTable, symbolTable, scope, errors);
                    switch (dimensionType) {
                        case NOT_FOUND, INTEGER ->{}

                        default ->{
                            errors.addSemanticError(new MError("Error en indice. Todos los indices de un arreglo deben de ser de tipo entero.", value.toString(), super.getRow(), super.getColumn()));
                        }


                    }
                }
                
                if(temp == null){
                    errors.addSemanticError(new MError("Arreglo no existe", value.toString(), super.getRow(), super.getColumn()));
                    return SymbolType.NOT_FOUND;
                }
                return temp.getType();
            }
            
            case PLUS, MINUS ->{
                tempType = (SymbolType)leftOperator.analyze(typeTable, symbolTable, scope, errors);
                switch(tempType){
                    case BOOLEAN, CHARACTER, NOT_FOUND -> {
                        return SymbolType.INTEGER;
                    }
                    
                    case VOID, STRING -> {
                        errors.addSemanticError(new MError("Operacion no valida.", "Operador matematicO sobre un valor de tipo " + typeTable.get(tempType).getName(), super.getRow(), super.getColumn()));
                    }
                    
                    default -> { 
                        return  tempType;
                    }
                }
            }
            
            case SUM ->{
                tempType = (SymbolType)leftOperator.analyze(typeTable, symbolTable, scope, errors);
                tempType2 = (SymbolType)rightOperator.analyze(typeTable, symbolTable, scope, errors);
                
                switch(tempType){  
                    
                    case NOT_FOUND -> {
                        //Ambos tipos son NOT_FOUND
                        if(tempType2 == SymbolType.NOT_FOUND) return SymbolType.NOT_FOUND;      
                        //Tipo derecho es VOID
                        if(tempType2 == SymbolType.VOID){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_VOID_MATH_OP, super.getRow(), super.getColumn()));
                            return SymbolType.VOID;
                        }
                        //Retornar el tipo de dato del operando derecho.
                        return tempType2;
                    }
               
                    case VOID ->{     
                        //Ambos tipos son VOID
                        if(tempType2 == SymbolType.VOID){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.BOTH_VOID_MATH_OP, super.getRow(), super.getColumn()));
                            return SymbolType.VOID;
                        }
                        //Tipo derecho NOT_FOUND
                        if(tempType2 == SymbolType.NOT_FOUND){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_VOID_MATH_OP, super.getRow(), super.getColumn()));
                            return SymbolType.VOID;
                        }
                        //Retornar el tipo de dato del operando derecho
                        errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_VOID_MATH_OP, super.getRow(), super.getColumn()));
                        return tempType2;    
                    }
                    
                    default ->{
                        //Tipo derecho VOID
                        if(tempType2 == SymbolType.VOID){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_VOID_MATH_OP, super.getRow(), super.getColumn()));
                            return tempType;
                        }
                        //Tipo derecho NOT_FOUND
                        if(tempType2 == SymbolType.NOT_FOUND) return tempType;
                        //Ambos tipos son STRING
                        if(tempType == SymbolType.STRING || tempType2 == SymbolType.STRING) return SymbolType.STRING;
                        //Validacion de tipos
                        SymbolType opType = typeTable.getOperationType(tempType, tempType2);
                        if(opType == null){
                            opType = typeTable.getOperationType(tempType2, tempType);
                            return opType;
                        } else{
                            return  opType;
                        }   
                    }
                }
            }
            
            case SUBTRACTION, MULTIPLICATION, DIVISION -> {
                tempType = (SymbolType)leftOperator.analyze(typeTable, symbolTable, scope, errors);
                tempType2 = (SymbolType)rightOperator.analyze(typeTable, symbolTable, scope, errors);
                
                switch(tempType){
                    case STRING ->{
                        //Tipo derecho STRING
                        if(tempType2 == SymbolType.STRING){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.BOTH_STRING_MATH_OP, super.getRow(), super.getColumn()));
                            return SymbolType.STRING;
                        }
                        //Tipo derecho VOID
                        if(tempType2 == SymbolType.VOID){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_VOID_MATH_OP, super.getRow(), super.getColumn()));
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_STRING_MATH_OP, super.getRow(), super.getColumn()));
                            return SymbolType.STRING;
                        }
                        //Tipo derecho NOT_FOUND
                        if(tempType2 == SymbolType.NOT_FOUND){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_STRING_MATH_OP, super.getRow(), super.getColumn()));
                            return SymbolType.STRING;
                        }
                        //Retornar tipo de dato derecho
                        errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_STRING_MATH_OP, super.getRow(), super.getColumn()));
                        return tempType2;
                    }
                    
                    case VOID ->{
                        //Tipo derecho STRING
                        if(tempType2 == SymbolType.STRING){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_VOID_MATH_OP, super.getRow(), super.getColumn()));
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_STRING_MATH_OP, super.getRow(), super.getColumn()));
                            return SymbolType.STRING;
                        }
                        //Tipo derecho VOID
                        if(tempType2 == SymbolType.VOID){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.BOTH_VOID_MATH_OP, super.getRow(), super.getColumn()));
                            return SymbolType.VOID;
                        }
                        //Tipo derecho NOT_FOUND
                        if(tempType2 == SymbolType.NOT_FOUND){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_VOID_MATH_OP, super.getRow(), super.getColumn()));
                            return SymbolType.VOID;
                        }
                        //Retornar el tipo del operando derecho
                        errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_VOID_MATH_OP, super.getRow(), super.getColumn()));
                        return tempType2;
                    }
                    
                    case NOT_FOUND ->{
                        //Tipo derecho STRING
                        if(tempType2 == SymbolType.STRING){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_STRING_MATH_OP, super.getRow(), super.getColumn()));
                            return SymbolType.STRING;
                        }
                        //Tipo derecho VOID
                        if(tempType2 == SymbolType.VOID){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_VOID_MATH_OP, super.getRow(), super.getColumn()));
                            return SymbolType.VOID;
                        }
                        //Tipo derecho NOT_FOUND
                        if(tempType2 == SymbolType.NOT_FOUND) return SymbolType.NOT_FOUND;
                        //Retornar el tipo del operando derecho
                        return tempType2;
                    }
                    
                    default ->{
                        //Tipo derecho STRING
                        if(tempType2 == SymbolType.STRING){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_STRING_MATH_OP, super.getRow(), super.getColumn()));
                            return tempType;
                        }
                        //Tipo derecho VOID
                        if(tempType2 == SymbolType.VOID){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_VOID_MATH_OP, super.getRow(), super.getColumn()));
                            return tempType;
                        }
                        //Tipod derecho NOT_FOUND
                        if(tempType2 == SymbolType.NOT_FOUND){
                            return tempType;
                        }
                        //Validacion de tipos
                        SymbolType opType = typeTable.getOperationType(tempType, tempType2);
                        if(opType == null){
                            opType = typeTable.getOperationType(tempType2, tempType);
                            return opType;
                        } else{
                            return  opType;
                        }
                    }
                }    
            }
            
            case MOD ->{
                tempType = (SymbolType)leftOperator.analyze(typeTable, symbolTable, scope, errors);
                tempType2 = (SymbolType)rightOperator.analyze(typeTable, symbolTable, scope, errors);
                
                switch(tempType){
                
                    case VOID ->{
                        //Tipo derecho VOID
                        if(tempType2 == SymbolType.VOID){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.BOTH_VOID_MATH_OP, super.getRow(), super.getColumn())); 
                            return SymbolType.INTEGER;
                        }
                        //Tipo derecho NOT_FOUND
                        if(tempType2 == SymbolType.NOT_FOUND){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_VOID_MATH_OP, super.getRow(), super.getColumn()));
                            return SymbolType.INTEGER;
                        }
                        //Tipo derecho distinto a INTEGER
                        if(tempType2 != SymbolType.INTEGER){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_VOID_MATH_OP, super.getRow(), super.getColumn()));
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_MOD_OP + typeTable.get(tempType2).getName()+".", super.getRow(), super.getColumn()));
                            return SymbolType.INTEGER;
                        }
                    }
                    
                    case NOT_FOUND ->{
                        //Tipo derecho VOID
                        if(tempType2 == SymbolType.VOID){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_VOID_MATH_OP, super.getRow(), super.getColumn()));
                            return SymbolType.INTEGER;
                        }
                        //Tipo derecho NOT_FOUND
                        if(tempType2 == SymbolType.NOT_FOUND)return SymbolType.INTEGER;
                        //Tipo derecho distinto a INTEGER
                        if(tempType2 != SymbolType.INTEGER){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_MOD_OP + typeTable.get(tempType2).getName()+".", super.getRow(), super.getColumn()));
                            return SymbolType.INTEGER;
                        }
                    }
                    
                    default ->{
                        if(tempType != SymbolType.INTEGER && tempType2 != SymbolType.INTEGER){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.BOTH_MOD_OP + typeTable.get(tempType2).getName(), super.getRow(), super.getColumn()));
                            return SymbolType.INTEGER;
                        }
                        if(tempType != SymbolType.INTEGER) errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_MOD_OP + typeTable.get(tempType2).getName()+".", super.getRow(), super.getColumn()));
                        if(tempType2 != SymbolType.INTEGER) errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_MOD_OP + typeTable.get(tempType2).getName()+".", super.getRow(), super.getColumn()));
                        return SymbolType.INTEGER;
                    }   
                }
                return SymbolType.INTEGER;
            }
            
            case POTENTIATION->{
                tempType = (SymbolType)leftOperator.analyze(typeTable, symbolTable, scope, errors);
                tempType2 = (SymbolType)rightOperator.analyze(typeTable, symbolTable, scope, errors);
                
                if(tempType2 != SymbolType.INTEGER && tempType2 != SymbolType.NOT_FOUND){
                    errors.addSemanticError(new MError("Operacion no valida.", "Operador matematico Potencia, solo se permiten potencias de tipo entero.", super.getRow(), super.getColumn()));   
                }
                
                if(tempType == SymbolType.VOID || tempType == SymbolType.STRING){
                    errors.addSemanticError(new MError("Operacion no valida.", "Operador matematico sobre operando izquierdo de tipo "+typeTable.get(tempType).getName() +".", super.getRow(), super.getColumn()));
                }
                return SymbolType.DOUBLE;     
            } 
            
            case ISEQUAL, NOTEQUAL, GREATERTHAN, LESSTHAN, GREATERTHANOREQUALTO, LESSTHANOREQUALTO ->{
                tempType = (SymbolType)leftOperator.analyze(typeTable, symbolTable, scope, errors);
                tempType2 = (SymbolType)rightOperator.analyze(typeTable, symbolTable, scope, errors);
               
                switch(tempType){
                    case VOID ->{
                        //Tipo derecho VOID
                        if(tempType2 == SymbolType.VOID){
                             errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.BOTH_VOID_RELATIONAL_OP, super.getRow(), super.getColumn()));
                            return SymbolType.BOOLEAN;
                        }
                        //Tipo derecho NOT_FOUND
                        if(tempType2 == SymbolType.NOT_FOUND){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_VOID_RELATIONA_OP, super.getRow(), super.getColumn()));
                            return SymbolType.BOOLEAN;
                        }

                        errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_VOID_RELATIONA_OP, super.getRow(), super.getColumn()));
                        return SymbolType.BOOLEAN;  
                    }

                    case NOT_FOUND ->{
                        //Tipo derecho VOID
                        if(tempType2 == SymbolType.VOID){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_VOID_RELATIONAL_OP, super.getRow(), super.getColumn()));
                            return SymbolType.BOOLEAN;
                        }
                        //Tipo derecho NOT_FOUND
                        if(tempType2 == SymbolType.NOT_FOUND) return SymbolType.BOOLEAN;

                         return SymbolType.BOOLEAN;
                    }

                    default ->{
                        if(tempType2 != SymbolType.NOT_FOUND && tempType2 != SymbolType.VOID){
                             //Validacion de tipos
                            SymbolType opType = typeTable.getOperationType(tempType, tempType2);
                            if(opType == null){
                                opType = typeTable.getOperationType(tempType2, tempType);
                                if(opType == null){
                                    errors.addSemanticError(new MError("Operacion no valida.", "Operador relacional sobre operando de tipo "+typeTable.get(tempType).getName()+" con operando de tipo "+typeTable.get(tempType2).getName()+".", super.getRow(), super.getColumn()));
                                }
                            }
                        }

                        if(tempType2 == SymbolType.VOID){
                            errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_VOID_RELATIONAL_OP, super.getRow(), super.getColumn()));
                        }
                        return SymbolType.BOOLEAN;
                    }
                }                                   
            }
            
            case ISNULL ->{
                leftOperator.analyze(typeTable, symbolTable, scope, errors);
                if(((Operation)leftOperator).getType() != OperationType.NULL_VAR){
                    errors.addSemanticError(new MError("Operacion no valida.", "Operador relacional aplicable unicamente sobre variables.", super.getRow(), super.getColumn()));
                }
                return SymbolType.BOOLEAN;
            }
            
            case AND, NAND, OR, NOR, XOR ->{
                tempType = (SymbolType)leftOperator.analyze(typeTable, symbolTable, scope, errors);
                tempType2 = (SymbolType)rightOperator.analyze(typeTable, symbolTable, scope, errors);
                
                //Evaluar operando izquierdo
                if(tempType == SymbolType.VOID)  errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.LEFT_VOID_LOGICAL_OP, super.getRow(), super.getColumn()));
                     
                if(tempType != SymbolType.BOOLEAN && tempType != SymbolType.VOID && tempType != SymbolType.NOT_FOUND ){
                    errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, "Operador logico sobre operando izquierdo de tipo "+typeTable.get(tempType).getName(), super.getRow(), super.getColumn())); 
                }
                
                //Evaluar operando derecho
                 if(tempType2 == SymbolType.VOID) errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_VOID_LOGICAL_OP, super.getRow(), super.getColumn()));
                
                if(tempType2 != SymbolType.BOOLEAN && tempType2 != SymbolType.VOID && tempType2 != SymbolType.NOT_FOUND ){
                    errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, "Operador logico sobre operando derecho de tipo "+typeTable.get(tempType2).getName(), super.getRow(), super.getColumn())); 
                }
                return  SymbolType.BOOLEAN;
            }
            
            case NOT ->{
                tempType = (SymbolType)leftOperator.analyze(typeTable, symbolTable, scope, errors);
                
                if(tempType == SymbolType.VOID) errors.addSemanticError(new MError(SemanticErrorMessage.INVALID_OPERATION, SemanticErrorMessage.RIGHT_VOID_LOGICAL_OP, super.getRow(), super.getColumn()));;
                
                if(tempType != SymbolType.BOOLEAN && tempType != SymbolType.VOID && tempType != SymbolType.NOT_FOUND){
                    errors.addSemanticError(new MError("Operacion no valida.", "Operador logico con operando derecho de tipo " + typeTable.get(tempType).getName()+".", super.getRow(), super.getColumn()));
                }
                return SymbolType.BOOLEAN;   
            }
        }
        
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, Track track){
        Object leftValue;
        Object rightValue;
         switch(operationType){
             
            case INTEGER, DOUBLE, CHAR, BOOLEAN -> {
                return value; 
            }
            
            case STRING -> {
                return ((String)value).replaceAll("\"", ""); 
            }
            
            case ALL_ARRAY ->{
                return symbolTable.get(value.toString(), scope).getValue();
            }
                
            case ID -> { 
                return symbolTable.get(value.toString(), scope).getValue();
            }
            
            case NULL_VAR -> {
                //Variable
                if(dimensions == null){
                    Symbol sym = symbolTable.get(value.toString(), scope);
                    return !sym.isInitialized();   
                }
                //Arreglo
                else{
                    //Obtener indices de acceso al arreglo
                    LinkedList<Integer> arrayIndices = new LinkedList();
                    for(Instruction instruction : dimensions){
                        arrayIndices.add((int)instruction.execute(typeTable, symbolTable, scope, track));  
                    }
                    
                    //Obtener indice plano del arreglo
                    int arrayIndex = symbolTable.getArrayIndex(new LinkedList<>(symbolTable.get(value.toString(), scope).getArrayDimensions()), arrayIndices);                    
                   
                    return ((Object[])symbolTable.get(value.toString(), scope).getValue())[arrayIndex] == null;
                }
            }
            
            case CALL ->{     
                return leftOperator.execute(typeTable, symbolTable, scope, track);
            }
            
            case ARRAY ->{
                //Obtener indices de acceso al arreglo
                LinkedList<Integer> arrayIndices = new LinkedList();
                for(Instruction instruction : dimensions){
                    arrayIndices.add((int)instruction.execute(typeTable, symbolTable, scope, track));  
                }

                //Obtener indice plano del arreglo
                int arrayIndex = symbolTable.getArrayIndex(new LinkedList<>(symbolTable.get(value.toString(), scope).getArrayDimensions()), arrayIndices);     
                Object tempValue = ((Object[])symbolTable.get(value.toString(), scope).getValue())[arrayIndex];
                
                //Si el valor del arreglo en los indices indicados es null, enviar un valor por defecto.
                if(tempValue == null){
                    switch (symbolTable.get(value.toString(), scope).getType()) {
                        case DOUBLE -> {
                            return 0.0;
                        }
                        case INTEGER -> {
                            return 0;
                        }
                        case CHARACTER -> {
                            return ' ';
                        }
                        case BOOLEAN -> {
                            return true;
                        }
                        case STRING -> {
                            return "";
                        }
                    }
                }
                
                return tempValue; 
            }
            
            case PLUS ->{
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                if(leftValue instanceof Double) return (double)leftValue;
                if(leftValue instanceof Integer) return (int)leftValue;
                if(leftValue instanceof Character) return (char)leftValue;
                if(leftValue instanceof Boolean){
                     int temp = (boolean)leftValue ? 1 : 0;
                       return temp;
                }
            }
            
             case MINUS ->{
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                if(leftValue instanceof Double) return -(double)leftValue;
                if(leftValue instanceof Integer) return -(int)leftValue;
                if(leftValue instanceof Character) return -(char)leftValue;
                if(leftValue instanceof Boolean){
                     int temp = (boolean)leftValue ? 1 : 0;
                       return -temp;
                }
            }
            
            case SUM ->{
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                rightValue = rightOperator.execute(typeTable, symbolTable, scope, track);
                   
                //Double to
                if(leftValue instanceof Double){
                    if(rightValue instanceof Double) return (double)leftValue + (double)rightValue;
                    if(rightValue instanceof Integer) return (double)leftValue + (int)rightValue;
                    if(rightValue instanceof Character) return (double)leftValue + (char)rightValue;  
                    if(rightValue instanceof Boolean){
                       int temp = (boolean)rightValue ? 1 : 0;
                       return (double)leftValue + temp;
                    }
                    if(rightValue instanceof String) return leftValue.toString() + rightValue.toString();    
                    return (double)leftValue + (double)rightValue;
                }
                //Integer to
                if(leftValue instanceof Integer){
                    if(rightValue instanceof Double) return (int)leftValue + (double)rightValue;
                    if(rightValue instanceof Integer) return (int)leftValue + (int)rightValue;
                    if(rightValue instanceof Character) return (int)leftValue + (char)rightValue;  
                    if(rightValue instanceof Boolean){
                       int temp = (boolean)rightValue ? 1 : 0;
                       return (int)leftValue + temp;
                    }
                    if(rightValue instanceof String) return leftValue.toString() + rightValue.toString(); 
                }
                //Character to
                if(leftValue instanceof Character){
                    if(rightValue instanceof Double) return (char)leftValue + (double)rightValue;
                    if(rightValue instanceof Integer) return (char)leftValue + (int)rightValue;
                    if(rightValue instanceof Character) return (char)leftValue + (char)rightValue;  
                    if(rightValue instanceof Boolean){
                       int temp = (boolean)rightValue ? 1 : 0;
                       return (char)leftValue + temp;
                    }
                    if(rightValue instanceof String) return leftValue.toString() + rightValue.toString(); 
                }
                //Boolean to
                if(leftValue instanceof Boolean){
                    int temp = (boolean)leftValue ? 1 : 0;
                    if(rightValue instanceof Double) return temp + (double)rightValue;
                    if(rightValue instanceof Integer) return temp + (int)rightValue;
                    if(rightValue instanceof Character) return temp + (char)rightValue;  
                    if(rightValue instanceof Boolean) {
                        int temp2 = (boolean)rightValue ? 1 : 0;
                        return temp + temp2;
                    }    
                    if(rightValue instanceof String) return leftValue.toString() + rightValue.toString(); 
                }
                //String to
                if(leftValue instanceof String){
                    return leftValue.toString() + rightValue.toString();
                }
            }
            
            case SUBTRACTION -> {
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                rightValue = rightOperator.execute(typeTable, symbolTable, scope, track);
                //Double to
                if(leftValue instanceof Double){
                    if(rightValue instanceof Double) return (double)leftValue - (double)rightValue;
                    if(rightValue instanceof Integer) return (double)leftValue - (int)rightValue;  
                    if(rightValue instanceof Character) return (double)leftValue - (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (double)leftValue - temp;
                    }
                }
                //Integer to
                if(leftValue instanceof Integer){
                    if(rightValue instanceof Double) return (int)leftValue - (double)rightValue;
                    if(rightValue instanceof Integer) return (int)leftValue - (int)rightValue;  
                    if(rightValue instanceof Character) return (int)leftValue - (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (int)leftValue - temp;
                    }
                }
                //Character to
                if(leftValue instanceof Character){
                    if(rightValue instanceof Double) return (char)leftValue - (double)rightValue;
                    if(rightValue instanceof Integer) return (char)leftValue - (int)rightValue;  
                    if(rightValue instanceof Character) return (char)leftValue - (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (char)leftValue - temp;
                    }
                }
                //Boolean to
                if(leftValue instanceof Boolean){
                    int temp = (boolean)leftValue ? 1 : 0;
                    if(rightValue instanceof Double) return temp - (double)rightValue;
                    if(rightValue instanceof Integer) return temp - (int)rightValue;
                    if(rightValue instanceof Character) return temp - (char)rightValue;  
                    if(rightValue instanceof Boolean) {
                        int temp2 = (boolean)rightValue ? 1 : 0;
                        return temp - temp2;
                    }    
                }
            }
            
            case MULTIPLICATION -> {
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                rightValue = rightOperator.execute(typeTable, symbolTable, scope, track);
                //Double to
                if(leftValue instanceof Double){
                    if(rightValue instanceof Double) return (double)leftValue * (double)rightValue;
                    if(rightValue instanceof Integer) return (double)leftValue * (int)rightValue;  
                    if(rightValue instanceof Character) return (double)leftValue * (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (double)leftValue * temp;
                    }
                }
                //Integer to
                if(leftValue instanceof Integer){
                    if(rightValue instanceof Double) return (int)leftValue * (double)rightValue;
                    if(rightValue instanceof Integer) return (int)leftValue * (int)rightValue;  
                    if(rightValue instanceof Character) return (int)leftValue * (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (int)leftValue * temp;
                    }
                }
                //Character to
                if(leftValue instanceof Character){
                    if(rightValue instanceof Double) return (char)leftValue * (double)rightValue;
                    if(rightValue instanceof Integer) return (char)leftValue * (int)rightValue;  
                    if(rightValue instanceof Character) return (char)leftValue * (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (char)leftValue * temp;
                    }
                }
                //Boolean to
                if(leftValue instanceof Boolean){
                    int temp = (boolean)leftValue ? 1 : 0;
                    if(rightValue instanceof Double) return temp * (double)rightValue;
                    if(rightValue instanceof Integer) return temp * (int)rightValue;
                    if(rightValue instanceof Character) return temp * (char)rightValue;  
                    if(rightValue instanceof Boolean) {
                        int temp2 = (boolean)rightValue ? 1 : 0;
                        return temp * temp2;
                    }    
                }
            }
            
            case DIVISION -> {
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                rightValue = rightOperator.execute(typeTable, symbolTable, scope, track);
                //Double to
                if(leftValue instanceof Double){
                    if(rightValue instanceof Double) return (double)leftValue / (double)rightValue;
                    if(rightValue instanceof Integer) return (double)leftValue / (int)rightValue;  
                    if(rightValue instanceof Character) return (double)leftValue / (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (double)leftValue / temp;
                    }
                }
                //Integer to
                if(leftValue instanceof Integer){
                    if(rightValue instanceof Double) return (int)leftValue / (double)rightValue;
                    if(rightValue instanceof Integer) return (int)leftValue / (int)rightValue;  
                    if(rightValue instanceof Character) return (int)leftValue / (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (int)leftValue / temp;
                    }
                }
                //Character to
                if(leftValue instanceof Character){
                    if(rightValue instanceof Double) return (char)leftValue / (double)rightValue;
                    if(rightValue instanceof Integer) return (char)leftValue / (int)rightValue;  
                    if(rightValue instanceof Character) return (char)leftValue / (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (char)leftValue / temp;
                    }
                }
                //Boolean to
                if(leftValue instanceof Boolean){
                    int temp = (boolean)leftValue ? 1 : 0;
                    if(rightValue instanceof Double) return temp / (double)rightValue;
                    if(rightValue instanceof Integer) return temp / (int)rightValue;
                    if(rightValue instanceof Character) return temp / (char)rightValue;  
                    if(rightValue instanceof Boolean) {
                        int temp2 = (boolean)rightValue ? 1 : 0;
                        return temp / temp2;
                    }    
                }
            }
            
            case MOD ->{
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                rightValue = rightOperator.execute(typeTable, symbolTable, scope, track);
                return (int)leftValue % (int)rightValue; 
            }
            
            case POTENTIATION->{
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                rightValue = rightOperator.execute(typeTable, symbolTable, scope, track);
                if(leftValue instanceof Double) return Math.pow((double)leftValue, (int)rightValue);
                if(leftValue instanceof Integer)return Math.pow((int)leftValue, (int)rightValue);
                if(leftValue instanceof Character) return Math.pow((char)leftValue, (int)rightValue);
                if(leftValue instanceof Boolean){
                    int temp = (boolean)leftValue ? 1 : 0;
                    return (int)Math.pow(temp, (int)rightValue);
                }
            }
            
            case ISEQUAL ->{
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                rightValue = rightOperator.execute(typeTable, symbolTable, scope, track);
                //Double to
                if(leftValue instanceof Double){
                    if(rightValue instanceof Double) return (double)leftValue == (double)rightValue;
                    if(rightValue instanceof Integer) return (double)leftValue == (int)rightValue;  
                    if(rightValue instanceof Character) return (double)leftValue == (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (double)leftValue == temp;
                    }
                }
                //Integer to
                if(leftValue instanceof Integer){
                    if(rightValue instanceof Double) return (int)leftValue == (double)rightValue;
                    if(rightValue instanceof Integer) return (int)leftValue == (int)rightValue;  
                    if(rightValue instanceof Character) return (int)leftValue == (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (int)leftValue == temp;
                    }
                }
                //Character to
                if(leftValue instanceof Character){
                    if(rightValue instanceof Double) return (char)leftValue == (double)rightValue;
                    if(rightValue instanceof Integer) return (char)leftValue == (int)rightValue;  
                    if(rightValue instanceof Character) return (char)leftValue == (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (char)leftValue == temp;
                    }
                }
                //Boolean to
                if(leftValue instanceof Boolean){
                    int temp = (boolean)leftValue ? 1 : 0;
                    if(rightValue instanceof Double) return temp == (double)rightValue;
                    if(rightValue instanceof Integer) return temp == (int)rightValue;
                    if(rightValue instanceof Character) return temp == (char)rightValue;  
                    if(rightValue instanceof Boolean) {
                        int temp2 = (boolean)rightValue ? 1 : 0;
                        return temp == temp2;
                    }    
                }
                //String to
                if(leftValue instanceof String){
                    return leftValue.toString().equals(rightValue.toString());
                }
            }
            
            case NOTEQUAL ->{
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                rightValue = rightOperator.execute(typeTable, symbolTable, scope, track);
                //Double to
                if(leftValue instanceof Double){
                    if(rightValue instanceof Double) return (double)leftValue != (double)rightValue;
                    if(rightValue instanceof Integer) return (double)leftValue != (int)rightValue;  
                    if(rightValue instanceof Character) return (double)leftValue != (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (double)leftValue != temp;
                    }
                }
                //Integer to
                if(leftValue instanceof Integer){
                    if(rightValue instanceof Double) return (int)leftValue != (double)rightValue;
                    if(rightValue instanceof Integer) return (int)leftValue != (int)rightValue;  
                    if(rightValue instanceof Character) return (int)leftValue != (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (int)leftValue != temp;
                    }
                }
                //Character to
                if(leftValue instanceof Character){
                    if(rightValue instanceof Double) return (char)leftValue != (double)rightValue;
                    if(rightValue instanceof Integer) return (char)leftValue != (int)rightValue;  
                    if(rightValue instanceof Character) return (char)leftValue != (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (char)leftValue != temp;
                    }
                }
                //Boolean to
                if(leftValue instanceof Boolean){
                    int temp = (boolean)leftValue ? 1 : 0;
                    if(rightValue instanceof Double) return temp != (double)rightValue;
                    if(rightValue instanceof Integer) return temp != (int)rightValue;
                    if(rightValue instanceof Character) return temp != (char)rightValue;  
                    if(rightValue instanceof Boolean) {
                        int temp2 = (boolean)rightValue ? 1 : 0;
                        return temp != temp2;
                    }    
                }
                //String to
                if(leftValue instanceof String){
                    return !leftValue.toString().equals(rightValue.toString());
                }
            }
            
            case GREATERTHAN ->{
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                rightValue = rightOperator.execute(typeTable, symbolTable, scope, track);
                //Double to
                if(leftValue instanceof Double){
                    if(rightValue instanceof Double) return (double)leftValue > (double)rightValue;
                    if(rightValue instanceof Integer) return (double)leftValue > (int)rightValue;  
                    if(rightValue instanceof Character) return (double)leftValue > (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (double)leftValue > temp;
                    }
                }
                //Integer to
                if(leftValue instanceof Integer){
                    if(rightValue instanceof Double) return (int)leftValue > (double)rightValue;
                    if(rightValue instanceof Integer) return (int)leftValue > (int)rightValue;  
                    if(rightValue instanceof Character) return (int)leftValue > (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (int)leftValue > temp;
                    }
                }
                //Character to
                if(leftValue instanceof Character){
                    if(rightValue instanceof Double) return (char)leftValue > (double)rightValue;
                    if(rightValue instanceof Integer) return (char)leftValue > (int)rightValue;  
                    if(rightValue instanceof Character) return (char)leftValue > (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (char)leftValue > temp;
                    }
                }
                //Boolean to
                if(leftValue instanceof Boolean){
                    int temp = (boolean)leftValue ? 1 : 0;
                    if(rightValue instanceof Double) return temp > (double)rightValue;
                    if(rightValue instanceof Integer) return temp > (int)rightValue;
                    if(rightValue instanceof Character) return temp > (char)rightValue;  
                    if(rightValue instanceof Boolean) {
                        int temp2 = (boolean)rightValue ? 1 : 0;
                        return temp > temp2;
                    }    
                }
                //String to
                if(leftValue instanceof String){
                    return leftValue.toString().length() > rightValue.toString().length();
                }
            }
            
            case LESSTHAN ->{
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                rightValue = rightOperator.execute(typeTable, symbolTable, scope, track);
                //Double to
                if(leftValue instanceof Double){
                    if(rightValue instanceof Double) return (double)leftValue < (double)rightValue;
                    if(rightValue instanceof Integer) return (double)leftValue < (int)rightValue;  
                    if(rightValue instanceof Character) return (double)leftValue < (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (double)leftValue < temp;
                    }
                }
                //Integer to
                if(leftValue instanceof Integer){
                    if(rightValue instanceof Double) return (int)leftValue < (double)rightValue;
                    if(rightValue instanceof Integer) return (int)leftValue < (int)rightValue;  
                    if(rightValue instanceof Character) return (int)leftValue < (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (int)leftValue < temp;
                    }
                }
                //Character to
                if(leftValue instanceof Character){
                    if(rightValue instanceof Double) return (char)leftValue < (double)rightValue;
                    if(rightValue instanceof Integer) return (char)leftValue < (int)rightValue;  
                    if(rightValue instanceof Character) return (char)leftValue < (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (char)leftValue < temp;
                    }
                }
                //Boolean to
                if(leftValue instanceof Boolean){
                    int temp = (boolean)leftValue ? 1 : 0;
                    if(rightValue instanceof Double) return temp < (double)rightValue;
                    if(rightValue instanceof Integer) return temp < (int)rightValue;
                    if(rightValue instanceof Character) return temp < (char)rightValue;  
                    if(rightValue instanceof Boolean) {
                        int temp2 = (boolean)rightValue ? 1 : 0;
                        return temp < temp2;
                    }    
                }
                //String to
                if(leftValue instanceof String){
                    return leftValue.toString().length() < rightValue.toString().length();
                }
            }
            
            case  GREATERTHANOREQUALTO ->{
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                rightValue = rightOperator.execute(typeTable, symbolTable, scope, track);
                //Double to
                if(leftValue instanceof Double){
                    if(rightValue instanceof Double) return (double)leftValue >= (double)rightValue;
                    if(rightValue instanceof Integer) return (double)leftValue >= (int)rightValue;  
                    if(rightValue instanceof Character) return (double)leftValue >= (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (double)leftValue >= temp;
                    }
                }
                //Integer to
                if(leftValue instanceof Integer){
                    if(rightValue instanceof Double) return (int)leftValue >= (double)rightValue;
                    if(rightValue instanceof Integer) return (int)leftValue >= (int)rightValue;  
                    if(rightValue instanceof Character) return (int)leftValue >= (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (int)leftValue >= temp;
                    }
                }
                //Character to
                if(leftValue instanceof Character){
                    if(rightValue instanceof Double) return (char)leftValue >= (double)rightValue;
                    if(rightValue instanceof Integer) return (char)leftValue >= (int)rightValue;  
                    if(rightValue instanceof Character) return (char)leftValue >= (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (char)leftValue >= temp;
                    }
                }
                //Boolean to
                if(leftValue instanceof Boolean){
                    int temp = (boolean)leftValue ? 1 : 0;
                    if(rightValue instanceof Double) return temp >= (double)rightValue;
                    if(rightValue instanceof Integer) return temp >= (int)rightValue;
                    if(rightValue instanceof Character) return temp >= (char)rightValue;  
                    if(rightValue instanceof Boolean) {
                        int temp2 = (boolean)rightValue ? 1 : 0;
                        return temp >= temp2;
                    }    
                }
                //String to
                if(leftValue instanceof String){
                    return leftValue.toString().length() >= rightValue.toString().length();
                }   
            }
            
            case LESSTHANOREQUALTO ->{
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                rightValue = rightOperator.execute(typeTable, symbolTable, scope, track);
                //Double to
                if(leftValue instanceof Double){
                    if(rightValue instanceof Double) return (double)leftValue <= (double)rightValue;
                    if(rightValue instanceof Integer) return (double)leftValue <= (int)rightValue;  
                    if(rightValue instanceof Character) return (double)leftValue <= (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (double)leftValue <= temp;
                    }
                }
                //Integer to
                if(leftValue instanceof Integer){
                    if(rightValue instanceof Double) return (int)leftValue <= (double)rightValue;
                    if(rightValue instanceof Integer) return (int)leftValue <= (int)rightValue;  
                    if(rightValue instanceof Character) return (int)leftValue <= (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (int)leftValue <= temp;
                    }
                }
                //Character to
                if(leftValue instanceof Character){
                    if(rightValue instanceof Double) return (char)leftValue <= (double)rightValue;
                    if(rightValue instanceof Integer) return (char)leftValue <= (int)rightValue;  
                    if(rightValue instanceof Character) return (char)leftValue <= (char)rightValue;
                    if(rightValue instanceof Boolean){
                         int temp = (boolean)rightValue ? 1 : 0;
                         return (char)leftValue <= temp;
                    }
                }
                //Boolean to
                if(leftValue instanceof Boolean){
                    int temp = (boolean)leftValue ? 1 : 0;
                    if(rightValue instanceof Double) return temp <= (double)rightValue;
                    if(rightValue instanceof Integer) return temp <= (int)rightValue;
                    if(rightValue instanceof Character) return temp <= (char)rightValue;  
                    if(rightValue instanceof Boolean) {
                        int temp2 = (boolean)rightValue ? 1 : 0;
                        return temp <= temp2;
                    }    
                }
                //String to
                if(leftValue instanceof String){
                    return leftValue.toString().length() <= rightValue.toString().length();
                }
            }
            
            case ISNULL ->{
                return (boolean)leftOperator.execute(typeTable, symbolTable, scope, track);
            }
            
            case AND ->{
                return  (boolean)leftOperator.execute(typeTable, symbolTable, scope, track) && (boolean)rightOperator.execute(typeTable, symbolTable, scope, track);
            }
            
            case NAND->{
                return  !((boolean)leftOperator.execute(typeTable, symbolTable, scope, track) && (boolean)rightOperator.execute(typeTable, symbolTable, scope, track));
            }
            
            case OR ->{
                return  (boolean)leftOperator.execute(typeTable, symbolTable, scope, track) || (boolean)rightOperator.execute(typeTable, symbolTable, scope, track);
            }
            
            case NOR ->{
                return  !((boolean)leftOperator.execute(typeTable, symbolTable, scope, track) || (boolean)rightOperator.execute(typeTable, symbolTable, scope, track));
            }
            
            case XOR ->{
                leftValue = leftOperator.execute(typeTable, symbolTable, scope, track);
                rightValue = rightOperator.execute(typeTable, symbolTable, scope, track);
                return ((boolean)leftValue || (boolean)rightValue) & !((boolean)leftValue && (boolean)rightValue);
            }
            
            case NOT ->{
               return !(boolean)leftOperator.execute(typeTable, symbolTable, scope, track);
            }
        
        }
        return null;
    }
 
    
}
