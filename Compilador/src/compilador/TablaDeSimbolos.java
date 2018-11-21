package compilador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

	public short nextTokenId = 256;
	
    private List<RegTablaSimbolos> tablaSimbolos = new ArrayList<>();
    
    public TablaDeSimbolos() {
        cargarPalabrasReservadas();
    }

    public void agregarSimbolo(RegTablaSimbolos regSimbolo) {
        this.tablaSimbolos.add(regSimbolo);
    }
    
    public List<RegTablaSimbolos> getTablaSimbolos() {
		return tablaSimbolos;
	}

	public RegTablaSimbolos getRegistro(String lexemaToken) {
        
		Optional<RegTablaSimbolos> regTabla = this.tablaSimbolos.stream()
											.filter(reg -> reg.getToken().getLexema()
															.equals(lexemaToken))
											.findFirst();
		return regTabla.isPresent() ? regTabla.get() : null;
    }
    
    /**
     * Imprime la tabla de simbolos
     */
    public void imprimirTablaDeSimbolos() {
    	tablaSimbolos.stream()
    		.sorted( (reg1, reg2) -> Integer.compare(reg1.getRegId(), reg2.getRegId()))
    		.forEach(reg -> System.out.println(reg));
    }
    
    private void cargarPalabrasReservadas() {

    	Collections.addAll(this.tablaSimbolos,
				    	    		registrarNuevoToken(Parser._IF, "if", 0, 0),    	    	
					    	    	registrarNuevoToken(Parser._ELSE, "else", 0, 0),
					    	    	registrarNuevoToken(Parser._ENDIF, "endif", 0, 0),
					    	    	registrarNuevoToken(Parser._PRINT, "print", 0, 0),
					    	    	registrarNuevoToken(Parser._USINTEGER, "usinteger", 0, 0),
					    	    	registrarNuevoToken(Parser._SINGLE, "single", 0, 0),
					    	    	registrarNuevoToken(Parser._FOR, "for", 0, 0),
					    	    	registrarNuevoToken(Parser._VOID, "void", 0, 0),
					    	    	registrarNuevoToken(Parser._FUN, "fun", 0, 0),
					    	    	registrarNuevoToken(Parser._RETURN, "return", 0, 0));
    }
    
    /**
     * Metodo publico para registrar nuevos tokens con el ID consecutivo privado.
     * Solo para aquellos tokens que necesiten guardarse en la tabla de simbolos.
     * En caso de querer registrar un token que no corresponde se devuevle NULL.
     */
    public RegTablaSimbolos createRegTabla(String lexemaToken, short codigoToken, int linea, int posicion) {

    	switch (codigoToken) {
		
    	case Parser._IDENTIFIER:
		case Parser._CONSTANT_UNSIGNED_INTEGER:
		case Parser._CONSTANT_SINGLE:
		case Parser._CONSTANT_STRING:
			return registrarNuevoToken(codigoToken, lexemaToken, linea, posicion);
			
		default:
			return null;

		}
    }
    
    private RegTablaSimbolos registrarNuevoToken(short idToken, String lexemaToken, int linea, int posicion) {
    	
    	Token token = new Token(lexemaToken, idToken);
    	return new RegTablaSimbolos(getNextTokenId(), token, linea, posicion);
    }
    
    public short getNextTokenId() {
    	return ++nextTokenId;
    }
}
