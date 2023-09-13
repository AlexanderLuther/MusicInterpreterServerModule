package com.hluther.controller;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author helmuth
 */
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
/**
 *
 * @author alexa
 */
public class FileController {
    
    private BufferedReader bufferReader;
    private FileOutputStream fileOutputStream;
    private File file;
    private String data;
    private String text;
    
    public String readFile(String path){
        text = "";
	try {
            bufferReader = new BufferedReader(new FileReader(path));
            while ((data = bufferReader.readLine()) != null){    
                text = text + data + "\n";
            } 
	}
        catch (EOFException ex) {
            System.out.println("ERROR: Lectura finalizada");
	}
        catch (IOException ex) {
            System.out.println("ERROR: No se puede leer archivo");
	}
        finally{
            try {
		bufferReader.close();
            } 
            catch (IOException ex) {
		System.out.println("ERROR: No se pudo cerrar el archivo");
            }
	}
        return text.substring(0, text.length()-1);
    }
    
    public boolean createFile(String path, String data){
        try {
            fileOutputStream = null;
            fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(data.getBytes());
            return true;
        } 
        catch (FileNotFoundException ex) {
            System.out.println("Error al crear el archivo");
            return false;
        } catch (IOException ex) {
            System.out.println("Error al crear el archivo");
            return false;
        }
    }
    
}
