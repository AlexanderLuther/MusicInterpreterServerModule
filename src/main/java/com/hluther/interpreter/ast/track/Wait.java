package com.hluther.interpreter.ast.track;

import com.hluther.interpreter.ast.instruction.Node;
import com.hluther.interpreter.ast.instruction.Instruction;
import com.hluther.entity.AnalysisError;
import com.hluther.entity.Channel;
import com.hluther.entity.MError;
import com.hluther.entity.Melody;
import com.hluther.entity.Pause;
import com.hluther.interpreter.ast.table.symbolTable.SymbolTable;
import com.hluther.interpreter.ast.table.typeTable.SymbolType;
import com.hluther.interpreter.ast.table.typeTable.TypeTable;
import java.util.Stack;

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

    @Override
    public Object analyze(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, AnalysisError errors){
        SymbolType tempType;
        
        tempType = (SymbolType)time.analyze(typeTable, symbolTable, scope, errors);
        if(tempType != SymbolType.INTEGER && tempType != SymbolType.NOT_FOUND) 
            errors.addSemanticError(new MError("Tiempo no valido.", "El valor debe ser de tipo Entero", super.getRow(), super.getColumn()));
        
        tempType = (SymbolType)channel.analyze(typeTable, symbolTable, scope, errors);
        if(tempType != SymbolType.INTEGER && tempType != SymbolType.NOT_FOUND) 
            errors.addSemanticError(new MError("Canal no valido.", "El valor debe ser de tipo Entero", super.getRow(), super.getColumn()));
        
        return null;
    }
    
    @Override
    public Object execute(TypeTable typeTable, SymbolTable symbolTable, Stack<String> scope, Track track){
        int timeVal= (int)time.execute(typeTable, symbolTable, scope, track);
        int channelVal= (int)channel.execute(typeTable, symbolTable, scope, track);
        
          if(timeVal < 0|| channelVal < 0){
            throw  new NumberFormatException();
        }
        
        //Obtener la melodia que se esta creando
        Melody melody = track.getMelody();

        //Verificar si ya existe el canal, de lo contrario agregarlo y agregar la nota sobre el canal.
        Channel channel = melody.getChannel(channelVal);
        if(channel == null){
            channel = melody.addChannel(channelVal);
        }
        channel.addAction(new Pause(timeVal), timeVal);
        return timeVal;

    }
}
