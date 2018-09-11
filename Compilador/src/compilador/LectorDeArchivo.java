/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase LectorDeArchivo
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
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
			inicializar();
			System.out.println("Archivo abierto exitosamente.");
		} catch (IOException exc) {
			System.out.println("Archivo o ruta de archivo incorrecta.");
		}
	}

	/**
	 * leerLinea obtiene una linea entera dada un archivo y la setea en lineaActual,
	 * guardando la linea anterior en lineaAnterior e incrementando el indice
	 * nroLinea
	 */
	public void leerLinea() {
		try {
			this.lineaAnterior = this.lineaActual;
			this.lineaActual = this.in.readLine();
			this.nroLinea++;
		} catch (IOException exc) {
			System.out.println("No se pudo leer la linea del archivo.");
			System.out.println("Ultima linea :" + this.lineaActual + " en linea nro: " + this.nroLinea);
		}
	}

	/**
	 * Se lee el siguiente char de la linea actual, se avanza de linea en caso de
	 * llegar al final de la misma.
	 * 
	 * @return El char leido.
	 * @throws IOException
	 */
	public Character leerChar() {
		Character charLeido = ' ';
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
				// En caso de llegar al final de la linea se debe retornan el caracter de salto
				// de linea.
				charLeido = Character.DIRECTIONALITY_PARAGRAPH_SEPARATOR;
			} else {
				// Capturo el siguiente char.
				charLeido = this.lineaActual.charAt(puntero);
//				System.out.println("Caracter Parcial: " + charLeido + " en nro linea: " + this.nroLinea
//						+ " puntero en posicion " + this.puntero);
			}

			this.puntero++;
		} else {
			//// Fin del archivo = devolver caracter simbolico para avanzar al ultimo estado
			return '$';
		}

		return charLeido;
	}

	/**
	 * retrocederPuntero mueve puntero en una posición se considera el caso en que
	 * puntero está en la posición 0 y es necesario volver a la linea anterior
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
	 * 
	 * @throws IOException
	 */
	public void inicializar() throws IOException {
		try {
			// Si el buffer ya se encuentra abierto, lo cierra antes de reabrirlo.
			if (this.in != null)
				this.in.close();

			this.in = new BufferedReader(new FileReader(this.rutaDelArchivo));
		} catch (IOException exc) {
			System.out.println("No se pudo resetear el archivo.");
		}
		this.puntero = 0;
		this.nroLinea = 0;
		this.lineaActual = in.readLine();
		this.lineaAnterior = "";

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

	public int getPuntero() {
		return puntero;
	}

}
