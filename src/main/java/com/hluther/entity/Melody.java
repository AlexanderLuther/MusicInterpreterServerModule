package com.hluther.entity;

import java.io.Serializable;
import java.util.LinkedList;
import org.jfugue.player.Player;

/**
 *
 * @author helmuth
 */
public class Melody implements Play, Serializable{
    
    private String id;
    private LinkedList<Channel> channels;
    private String code;

    public Melody() {
        channels = new LinkedList<>();
    }
    
    public Channel getChannel(int channelId){
        for(Channel channel:channels){
            if(channel.getChannelId() == channelId){
                return channel;
            }
        }
        return null;
    }
    
    public Channel addChannel(int channelId){
        Channel channel = new Channel(channelId);
        channels.add(channel);
        return channel;
    }
    
    public boolean isEmpty(){
        return channels.isEmpty();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LinkedList<Channel> getChannels() {
        return channels;
    }

    public void setChannels(LinkedList<Channel> channels) {
        this.channels = channels;
    }
    
    public void setExit(boolean exit){
        for(Channel channel:channels){
            channel.setExit(exit);
        }
    }
    
    public double getDuration(){
        double duration = 0;
        for(Channel channel:channels){
            if(channel.getDuration() > duration){
                duration = channel.getDuration();
            }
        }
        return duration/1000;
    }

    @Override
    public Object play(Thread thread, Player player) {
        channels.stream().map(channel -> new Thread(){
            @Override 
            public  void run(){
                try {
                    channel.play(this, player);
                }    
                catch (Exception e) {
                    System.out.println("Error en Track: " + e.getMessage());
                }
            }}).forEachOrdered(thread1 -> {
                thread1.start();
                
        });
        return null;
    }
    
}
