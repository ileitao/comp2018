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
    
    public void agregarSimbolo(String lexema, TipoToken tipoToken, int lineaToken, int posicionToken) {
		RegTablaSimbolos reg = this.createRegTabla(lexema, tipoToken, lineaToken, posicionToken);
        this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
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
    	
    	reg = registrarNuevoToken(Parser._PLUS, "+", TipoToken.OPERADOR_ARITMETICO, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._MINUS, "-", TipoToken.OPERADOR_ARITMETICO, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._MULT, "*", TipoToken.OPERADOR_ARITMETICO, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._DIV, "/", TipoToken.OPERADOR_ARITMETICO, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._ASSIGN, ":=", TipoToken.OPERADOR_ASIGNACION, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._GREATER_OR_EQUAL, ">=", TipoToken.COMPARADOR, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._LESSER_OR_EQUAL, "<=", TipoToken.COMPARADOR, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._GREATER, ">", TipoToken.COMPARADOR, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);	
    	
    	reg = registrarNuevoToken(Parser._LESSER, "<", TipoToken.COMPARADOR, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._EQUAL, "=", TipoToken.COMPARADOR, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._UNEQUAL, "!=", TipoToken.COMPARADOR, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._LPAREN, "(", TipoToken.DELIMITADOR, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._RPAREN, ")", TipoToken.DELIMITADOR, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._LCBRACE, "{", TipoToken.DELIMITADOR, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._RCBRACE, "}", TipoToken.DELIMITADOR, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._COMMA, ",", TipoToken.DELIMITADOR, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    	
    	reg = registrarNuevoToken(Parser._SEMICOLON, ";", TipoToken.DELIMITADOR, 0, 0);
    	this.tablaDeSimbolos.put(reg.getToken().getLexema(), reg);
    }
    
    /**
     * Metodo privado para registrar nuevos tokens con el ID consecutivo privado.
     */
    private RegTablaSimbolos createRegTabla(String lexemaToken, TipoToken tipoToken, int linea, int posicion) {
//    	if (tipoToken.equals(TipoToken.IDENTIFICADOR)) {
//            return registrarNuevoToken(Parser._IDENTIFIER, lexemaToken, tipoToken, linea, posicion); 
//        } else if (tipoToken == TipoToken.CONSTANTE_ENTERO_SIN_SIGNO)
//        {
//            return registrarNuevoToken(Parser._CONSTANT, lexemaToken, tipoToken, linea, posicion);
//        }
    	switch (tipoToken) {
		
    	case IDENTIFICADOR:
			return registrarNuevoToken(Parser._IDENTIFIER, lexemaToken, tipoToken, linea, posicion);
		
		case CONSTANTE_ENTERO_SIN_SIGNO:
			return registrarNuevoToken(Parser._CONSTANT_UNSIGNED_INTEGER, lexemaToken, tipoToken, linea, posicion);
		
		case CONSTANTE_FLOTANTE:
			return registrarNuevoToken(Parser._CONSTANT_SINGLE, lexemaToken, tipoToken, linea, posicion);
		
		case CADENA_CARACTERES:
			return registrarNuevoToken(Parser._CONSTANT_STRING, lexemaToken, tipoToken, linea, posicion);
			
		default:
			return registrarNuevoToken(getNextTokenId(), lexemaToken, tipoToken, linea, posicion);

		}
    }
    
    private RegTablaSimbolos registrarNuevoToken(long idToken, String lexemaToken, TipoToken tipoToken, int linea, int posicion) {
    	
    	Token token = new Token(lexemaToken, idToken);
    	return new RegTablaSimbolos(token, tipoToken, linea, posicion);
    }
    
    private int getNextTokenId() {
    	return nextTokenId++;
    }
}
