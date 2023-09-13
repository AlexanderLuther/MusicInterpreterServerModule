package com.hluther.entity;

import java.io.Serializable;
import org.jfugue.player.Player;

/**
 *
 * @author helmuth
 */
public class Note implements Play, Serializable{

    private String note;
    private int eighth;
    private int duration;
    private double frecuency;

    public Note(String note, int eighth, int duration) {
        this.note = note;
        this.eighth = eighth;
        this.duration = duration;
        switch(note){
            case "C" -> {
                switch(eighth){
                    case 1 -> frecuency = 32.70;
                    case 2 -> frecuency = 65.41;
                    case 3 -> frecuency = 130.81;
                    case 4 -> frecuency = 261.63;
                    case 5 -> frecuency = 523.25;
                    case 6 -> frecuency = 1046.50;
                    case 7 -> frecuency = 2093.0;
                    case 8 -> frecuency = 4186.01;
                }
            }

            case "C#" -> {
                switch(eighth){
                    case 1 -> frecuency = 34.6;
                    case 2 -> frecuency = 69.30;
                    case 3 -> frecuency = 138.59;
                    case 4 -> frecuency = 277.17;
                    case 5 -> frecuency = 554.37;
                    case 6 -> frecuency = 1108.73;
                    case 7 -> frecuency = 2217.45;
                }
            }

            case "D" -> {
                switch(eighth){
                    case 1 -> frecuency = 36.71;
                    case 2 -> frecuency = 73.42;
                    case 3 -> frecuency = 146.83;
                    case 4 -> frecuency = 293.66;
                    case 5 -> frecuency = 587.33;
                    case 6 -> frecuency = 1174.66;
                    case 7 -> frecuency = 2349.32;
                }
            }

            case "D#" -> {
                switch(eighth){
                    case 1 -> frecuency = 38.89;
                    case 2 -> frecuency = 77.78;
                    case 3 -> frecuency = 155.56;
                    case 4 -> frecuency = 311.12;
                    case 5 -> frecuency = 622.25;
                    case 6 -> frecuency = 1244.51;
                    case 7 -> frecuency = 2489.02;
                }
            }

            case "E" -> {
                switch(eighth){
                    case 1 -> frecuency = 41.20;
                    case 2 -> frecuency = 82.41;
                    case 3 -> frecuency = 164.81;
                    case 4 -> frecuency = 329.63;
                    case 5 -> frecuency = 659.26;
                    case 6 -> frecuency = 1318.51;
                    case 7 -> frecuency = 2637.02;
                }
            }

            case "F" -> {
                switch(eighth){
                    case 1 -> frecuency = 43.65;
                    case 2 -> frecuency = 87.31;
                    case 3 -> frecuency = 174.61;
                    case 4 -> frecuency = 349.23;
                    case 5 -> frecuency = 698.46;
                    case 6 -> frecuency = 1396.91;
                    case 7 -> frecuency = 2793.83;
                }
            }

            case "F#" -> {
                switch(eighth){
                    case 1 -> frecuency = 46.25;
                    case 2 -> frecuency = 92.50;
                    case 3 -> frecuency = 185;
                    case 4 -> frecuency = 369.99;
                    case 5 -> frecuency = 739.99;
                    case 6 -> frecuency = 1479.98;
                    case 7 -> frecuency = 2959.96;
                }
            }

            case "G" -> {
                switch(eighth){
                    case 1 -> frecuency = 49;
                    case 2 -> frecuency = 98;
                    case 3 -> frecuency = 196;
                    case 4 -> frecuency = 392;
                    case 5 -> frecuency = 783.99;
                    case 6 -> frecuency = 1567.98;
                    case 7 -> frecuency = 3135.96;
                }
            }

            case "G#" -> {
                switch(eighth){
                    case 1 -> frecuency = 51.91;
                    case 2 -> frecuency = 103.83;
                    case 3 -> frecuency = 207.65;
                    case 4 -> frecuency = 415.30;
                    case 5 -> frecuency = 830.61;
                    case 6 -> frecuency = 1661.22;
                    case 7 -> frecuency = 3322.44;
                }
            }

            case "A" -> {
                switch(eighth){
                    case 0 -> frecuency = 27.50;
                    case 1 -> frecuency = 55;
                    case 2 -> frecuency = 110;
                    case 3 -> frecuency = 220;
                    case 4 -> frecuency = 440;
                    case 5 -> frecuency = 880;
                    case 6 -> frecuency = 1760;
                    case 7 -> frecuency = 3520;
                }
            }

            case "A#" -> {
                switch(eighth){
                    case 0 -> frecuency = 29.17;
                    case 1 -> frecuency = 58.27;
                    case 2 -> frecuency = 116.54;
                    case 3 -> frecuency = 233.08;
                    case 4 -> frecuency = 466.16;
                    case 5 -> frecuency = 932.33;
                    case 6 -> frecuency = 1864.66;
                    case 7 -> frecuency = 3729.31;
                }
            }

            case "B" -> {
                switch(eighth){
                    case 0 -> frecuency = 30.87;
                    case 1 -> frecuency = 61.74;
                    case 2 -> frecuency = 123.47;
                    case 3 -> frecuency = 246.94;
                    case 4 -> frecuency = 493.88;
                    case 5 -> frecuency = 987.77;
                    case 6 -> frecuency = 1975.53;
                    case 7 -> frecuency = 3951.07;
                }
            }    
    
        }
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getEighth() {
        return eighth;
    }

    public void setEighth(int eighth) {
        this.eighth = eighth;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getFrecuency() {
        return frecuency;
    }
    
    @Override
    public Object play(Thread thread, Player player){
        player.play(note+eighth + "/" + (duration/1000));
        return null;
    }
    
}