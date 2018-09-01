/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.BufferedReader;
import java.io.File;
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
    
    private String rutaDelArchivo;
    
    public LectorDeArchivo(String archivo) {
        try {
            this.rutaDelArchivo = archivo;
            this.in = new BufferedReader(new FileReader(new File(this.rutaDelArchivo)));
            System.out.println("Archivo abierto exitosamente.");
            this.puntero = 0;
            this.lineaActual = in.readLine();
            this.lineaAnterior = "";
        }
        catch(IOException exc) {
            System.out.println("Archivo o ruta de archivo incorrecta.");
        }
    }
    
    /**
     * leerLinea obtiene una linea entera dada un archivo
     * y la setea en lineaActual, guardando la linea anterior
     * en lineaAnterior e incrementando el indice nroLinea
     */
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
    
    public String leerChar() throws IOException {
        String character = "";
        //// Tengo una linea para leer
        if (this.lineaActual != null) {
            //// El puntero superó el largo de la linea leída
            if (this.puntero > this.lineaActual.length()) {
                //// Leo una linea nueva
                this.leerLinea();
                //// Reinicio el puntero a la primer posición
                this.puntero = 0;
                //// Pido el primer caracter recursivamente
                return this.leerChar();
            } else if (this.puntero == this.lineaActual.length()) {
                character = "/n";
            } else {
                char characterLeido = (this.lineaActual.charAt(puntero));
                character = "" + characterLeido;
                switch (characterLeido){
                  //case '\t': result="/t"; break;
                    case ' ': character = " "; break;
                }  
                System.out.println("Caracter Parcial: " + character + " en nro linea: " + this.nroLinea + " puntero en posicion " + this.puntero);
            }

            this.puntero++;
        } else {
            //// Fin del archivo = devolver caracter simbolico
            //// para avanzar al ultimo estado (x ej "$")
            character = "";
        }

        return character;    
    }
    
    /**
     * retrocederPuntero mueve puntero en una posición 
     * se considera el caso en que puntero está en la 
     * posición 0 y es necesario volver a la linea anterior
     */
    public void retrocederPuntero() {
        if (this.puntero <= 0) {
            this.puntero = 0;
            if (this.nroLinea > 0) {
                this.nroLinea--;    
            }
            
            this.lineaActual = this.lineaAnterior;            
        } else {
            this.puntero--;
        }
    }
    
    /**
     * Resetea el lector al inicio del archivo almacenado en rutaDelArchivo
     * @throws IOException 
     */
    public void resetearLector() throws IOException {
        try {
            this.in.close();
            this.in = new BufferedReader(new FileReader(this.rutaDelArchivo));
            this.puntero = 0;
            this.nroLinea = 0;
            this.lineaActual = in.readLine();
            this.lineaAnterior = "";   
        }
        catch (IOException exc) {
            System.out.println("No se pudo resetear el archivo.");
        }
    }
    
    public BufferedReader getIn() {
        return this.in;
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
    
    public String getRutaDelArchivo() {
        return this.rutaDelArchivo;
    }
    
    public void setRutaDelArchivo(String rutaDelArchivo) {
        this.rutaDelArchivo = rutaDelArchivo;
    }
}
