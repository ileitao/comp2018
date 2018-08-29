/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase LectorDeArchivo
 * 
 * @author ileitao
 * @author gmaiola
 */
public class LectorDeArchivo {
   
    private BufferedReader in;
    
    private int nroLinea = 1;
    
    private int puntero;
    
    private String lineaActual;

    private String lineaAnterior;
    
    public LectorDeArchivo(String archivo) {
        try {
            this.in = new BufferedReader(new FileReader(new File(archivo)));
            System.out.println("Archivo abierto exitosamente.");
            this.puntero = 0;
            this.lineaActual = in.readLine();
            this.lineaAnterior = "";
        }
        catch(IOException exc) {
            System.out.println("Archivo o ruta de archivo incorrecta.");
        }
    }
    
    public void leerLinea() {
        try {
            this.lineaAnterior = this.lineaActual;
            this.lineaActual = this.in.readLine();
            this.nroLinea++;
        }
        catch(IOException exc) {
            System.out.println("No se pudo leer la linea del archivo.");
            System.out.println("Ultima linea :" + this.lineaActual + " en linea nro: " + this.nroLinea);
        }
    }
    
    public String getLineaActual() {
        return this.lineaActual;
    }
    
    public String getLineaAnterior() {
        return this.lineaAnterior;
    }
    
    public int getNroLinea() {
        return this.nroLinea;
    }
}
