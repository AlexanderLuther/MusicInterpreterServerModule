package com.hluther.interpreter.ast.track;

import com.hluther.entity.AnalysisError;
import com.hluther.entity.Channel;
import com.hluther.entity.Melody;
import com.hluther.interpreter.ast.instruction.ArrayDeclaration;
import com.hluther.interpreter.ast.instruction.Declaration;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.method.Main;
import com.hluther.interpreter.ast.method.Method;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.JTextPane;

/**
 *
 * @author helmuth
 */
public class Track extends Node implements Instruction{
    
    private String id;
    private LinkedList<Instruction> instructions;
    private JTextPane console;
    private Melody melody;

    public Track(String id, LinkedList<Instruction> instructions, int row, int column) {
        super(row, column);
        this.id = id;
        this.instructions = instructions;
        this.melody = new Melody();
    }

    public Track(String id, int row, int column) {
        super(row, column);
        this.id = id;
        this.instructions = new LinkedList<>();
        this.melody = new Melody();
    }

    public void setConsole(JTextPane console) {
        this.console = console;
    }
    
    public Instruction getMethod(String id){
        for(Instruction instruction : instructions){
            if(instruction instanceof Method){
                if(((Method)instruction).getIdentifier().equals(id)){
                    return instruction;
                }
            }
        }
        return null;
    }

    public JTextPane getConsole() {
        return console;
    }

    public Melody getMelody() {
        return melody;
    }
    
    
    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        //Apilar el ambito global 
        scope.push("global");
        
        //Analizar cada una de las instrucciones de la pista
        for(Instruction instruction :instructions){
            instruction.analyze(typeTable, symbolTable, scope, errors);
        }
        
        //Desapilar el ambito global
        scope.pop();
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, Track track){
        melody.setId(id);
        
        //Apilar el ambito global
        scope.push("global");
        
        //Ejecutar declaraciones y declaraciones con asignacion a nivel global
        for(Instruction instruction : instructions){
            if(instruction instanceof Declaration || instruction instanceof ArrayDeclaration ){
                instruction.execute(typeTable, symbolTable, scope, track);
            }
        }
        
        //Ejecutar el procedimiento principal
         for(Instruction instruction : instructions){
            if(instruction instanceof Main ){
                instruction.execute(typeTable, symbolTable, scope, track);
            }
        }
         
        //Desapilar el ambito global
        scope.pop();
        return null;
    }
    
}
