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

	public static int nextTokenId = 300;
	
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
    	
    	reg = registrarNuevoToken(Parser._IF, "if", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._ELSE, "else", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._ENDIF, "endif", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._PRINT, "print", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._USINTEGER, "usinteger", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._SINGLE, "single", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._FOR, "for", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._VOID, "void", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._FUN, "fun", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._RETURN, "return", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._PLUS, "+", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._MINUS, "-", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._MULT, "*", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._DIV, "/", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._ASSIGN, ":=", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._GREATER_OR_EQUAL, ">=", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._LESSER_OR_EQUAL, "<=", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._GREATER, ">", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);	
    	
    	reg = registrarNuevoToken(Parser._LESSER, "<", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._EQUAL, "=", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._UNEQUAL, "!=", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._LPAREN, "(", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._RPAREN, ")", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._LCBRACE, "{", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._RCBRACE, "}", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._COMMA, ",", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._SEMICOLON, ";", TipoToken.PALABRA_RESERVADA, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    }
    
    /**
     * Metodo publico para registrar nuevos tokens con el ID consecutivo privado.
     */
    public RegTablaSimbolos createRegTabla(String lexemaToken, TipoToken tipoToken, int linea, int posicion) {
    	
    	return registrarNuevoToken(getNextTokenId(), lexemaToken, tipoToken, linea, posicion);
    }
    
    private RegTablaSimbolos registrarNuevoToken(long idToken, String lexemaToken, TipoToken tipoToken, int linea, int posicion) {
    	
    	Token token = new Token(lexemaToken, getNextTokenId());
    	return new RegTablaSimbolos(token, tipoToken, linea, posicion);
    }
    
    private int getNextTokenId() {
    	return nextTokenId++;
    }
}
