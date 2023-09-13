package com.hluther.controller;

import com.hluther.entity.Melody;
import com.hluther.gui.MusicInterpreterFrame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
/**
 *
 * @author helmuth
 */

public class MelodyFileController {
   
    private File file;
    private ObjectOutputStream objectOutputStream;
    private FileOutputStream fileOutputStream;
    private ObjectInputStream objectInputStream;
    private FileInputStream fileInputStream;
    private MusicInterpreterFrame musicInterpreterFrame; 
    private final String TRACKS_FILE_PATH = "Tracks.bin";
    
    public MelodyFileController(MusicInterpreterFrame musicInterpreterFrame){
        this.musicInterpreterFrame = musicInterpreterFrame;
        createFile(TRACKS_FILE_PATH);
    }
    
    public void createFile(String path){
        try {
            file = new File(path);
            if(!file.exists()){
                file.createNewFile();
                saveMelodies(new LinkedList<>());
            }
        } catch (IOException ex) {
            System.out.println("Error creando el archivo"+path);
        }
    }
        
    public void saveMelodies(LinkedList<Melody> melodies){
        try{
            fileOutputStream = new FileOutputStream(file = new File(TRACKS_FILE_PATH));
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(melodies);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch(IOException e){
            System.out.println("Error al guardar en archivo: " + TRACKS_FILE_PATH);
        }
    }    
    
    public LinkedList<Melody> getMelodies(){
        LinkedList<Melody> tracks = null;
        try {
            fileInputStream = new FileInputStream(new File(TRACKS_FILE_PATH));
            objectInputStream = new ObjectInputStream(fileInputStream);
            while(fileInputStream.available() > 0){
                tracks =(LinkedList<Melody>)objectInputStream.readObject();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error al recuperar en archivo: " + TRACKS_FILE_PATH + " " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error al recuperar en archivo: " + TRACKS_FILE_PATH + " " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al recuperar en archivo: " + TRACKS_FILE_PATH + " " + ex.getMessage());
        } 
        return tracks;
    }
    
}
