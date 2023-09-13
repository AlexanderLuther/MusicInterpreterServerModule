package com.hluther.entity;

import java.io.Serializable;
import org.jfugue.player.Player;

/**
 *
 * @author helmuth
 */
public class Pause implements Play, Serializable{

    private int duration;

    public Pause(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public Object play(Thread thread, Player  player) {
        try {
            thread.sleep(duration);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
   
}