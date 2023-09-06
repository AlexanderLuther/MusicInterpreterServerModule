package com.hluther.controller;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author helmuth
 */
public class FileController {
    
    private BufferedReader bufferReader;
    private String data;
    private StringBuffer text = new StringBuffer();

    /*
    LEER ARCHIVO
    Metodo que recibe como parametro un path, el cual utiliza para la apertura de 
    un archivo y su posterior lectura. Devuelve un String con todos los datos 
    contenidos dentro del archivo.
    */
    public String readFile(String path){
	try {
            bufferReader = new BufferedReader(new FileReader(path));
            while ((data = bufferReader.readLine()) != null){    
                text.append(data);
                text.append("\n");
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
            } catch(NullPointerException e){
                System.out.println("ERROR: No se encontro el archivo de entrada. \nSi no existe cree el archivo input.txt y ubiquelo en la carpeta donde se encuentra el ejecutable.");
            }
	}
        return text.toString();
    }
    
}
