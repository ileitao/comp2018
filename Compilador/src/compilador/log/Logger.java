/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.log;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Logger
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public class Logger {
    
	private List<EventoLog> eventos;
	
	public Logger() {
		this.eventos = new ArrayList<>();
	}
	
	public void log(EventoLog evento) {
		eventos.add(evento);
	}

	public void imprimir() {
		System.out.println(eventos);
	}
}
