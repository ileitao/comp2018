package compilador;

import java.util.HashMap;

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

	public static int nextTokenId = 1;
	
    private HashMap<String, RegTablaSimbolos> tablaDeSimbolos;
    
    public TablaDeSimbolos() {
        this.tablaDeSimbolos = new HashMap<String, RegTablaSimbolos>();
        cargarPalabrasReservadas();
        nextTokenId = 257;
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
    	
//    	reg = crearRegTabla("if", TipoToken.PALABRA_RESERVADA, 0, 0);
//    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = crearRegTabla("usinteger", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = crearRegTabla("single", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = crearRegTabla("for", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = crearRegTabla("void", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = crearRegTabla("fun", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = crearRegTabla("return	", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    }
    
    public RegTablaSimbolos crearRegTabla(String lexemaToken, TipoToken tipoToken, int linea, int posicion) {
    	
    	Token token = new Token(lexemaToken, getNextTokenId());
    	
    	return new RegTablaSimbolos(token, tipoToken, linea, posicion);
    }
    
    private int getNextTokenId() {
    	return nextTokenId++;
    }
}
