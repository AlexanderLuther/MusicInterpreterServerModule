package com.hluther.entity;

import java.io.Serializable;
import java.util.LinkedList;
import org.jfugue.player.Player;

/**
 *
 * @author helmuth
 */
public class Channel implements Play, Serializable{
    
    private int id;
    private LinkedList<Play> actions;
    private int duration;
    private boolean exit;

    public Channel(int id) {
        this.id = id;
        this.duration = 0;
        this.exit = false;
        this.actions = new LinkedList<>();
    }

    public int getChannelId() {
        return id;
    }
    
    public void addAction(Play action, int duration){
        actions.addLast(action);
        this.duration += duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public LinkedList<Play> getActions() {
        return actions;
    }
        
    @Override
    public Object play(Thread thread, Player  player) {
        int counter = 0;
        while(!exit && !(counter == actions.size())){
            actions.get(counter).play(thread, player);
            counter++;
        }
        
        return null;
    }
}
