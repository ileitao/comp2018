package compilador;

import java.util.HashMap;

import compilador.analizadorsintactico.Parser;

/**
 * Clase TablaDeSimbolos
 * 
 * Estructura de datos que contiene un registro para
 * cada identificador utilizado en el código fuente.
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public class TablaDeSimbolos {

	public static short nextTokenId = 300;
	
    private HashMap<String, RegTablaSimbolos> tablaDeSimbolos;
    
    public TablaDeSimbolos() {
        this.tablaDeSimbolos = new HashMap<String, RegTablaSimbolos>();
        cargarPalabrasReservadas();
    }
    
    public HashMap<String, RegTablaSimbolos> getTablaDeSimbolos() {
        return this.tablaDeSimbolos;
    }
    
    public void agregarSimbolo(RegTablaSimbolos regSimbolo) {
        this.tablaDeSimbolos.put(regSimbolo.getToken().getLexema(), regSimbolo);
    }
    
    public RegTablaSimbolos getRegistro(String lexemaToken) {
        return this.tablaDeSimbolos.get(lexemaToken);
    }
    
    /**
     * Imprime la tabla de simbolos
     */
    public void imprimirTablaDeSimbolos() {
    	this.tablaDeSimbolos.keySet().forEach(id -> System.out.println(this.tablaDeSimbolos.get(id)));
    }
    
    private void cargarPalabrasReservadas() {
    	RegTablaSimbolos reg;
    	
    	reg = registrarNuevoToken(Parser._IF, "if", 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._ELSE, "else", 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._ENDIF, "endif", 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._PRINT, "print", 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._USINTEGER, "usinteger", 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._SINGLE, "single", 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._FOR, "for", 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._VOID, "void", 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._FUN, "fun", 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._RETURN, "return", 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);

    }
    
    /**
     * Metodo publico para registrar nuevos tokens con el ID consecutivo privado.
     */
    public RegTablaSimbolos createRegTabla(String lexemaToken, short codigoToken, int linea, int posicion) {

    	switch (codigoToken) {
		
    	case Parser._IDENTIFIER:
		case Parser._CONSTANT_UNSIGNED_INTEGER:
		case Parser._CONSTANT_SINGLE:
		case Parser._CONSTANT_STRING:
			return registrarNuevoToken(codigoToken, lexemaToken, linea, posicion);
			
		default:
			return registrarNuevoToken(getNextTokenId(), lexemaToken, linea, posicion);

		}
    }
    
    private RegTablaSimbolos registrarNuevoToken(short idToken, String lexemaToken, int linea, int posicion) {
    	
    	Token token = new Token(lexemaToken, idToken);
    	return new RegTablaSimbolos(token, linea, posicion);
    }
    
    private short getNextTokenId() {
    	return nextTokenId++;
    }
}
