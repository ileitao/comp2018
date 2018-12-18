package compilador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    	RegTablaSimbolos reg = new RegTablaSimbolos(getNextTokenId(), token, linea, posicion);
    	token.setRegTabSimbolos(reg);
    	return reg;
    }
    
    public short getNextTokenId() {
    	return ++nextTokenId;
    }
    
    public Optional<RegTablaSimbolos> obtenerDeclaracion(Token tokenIdentificador) {
    	
    	Optional<RegTablaSimbolos> regEncontrado = this.tablaSimbolos.stream()
    			//Descarto el token recibido por parametro.filter( reg -> reg.getToken().getLexema().equals(tokenID.getLexema()))
    			.filter( reg -> !reg.equals(tokenIdentificador.getRegTabSimbolos()))
    			// Busco los identificadores con el mismo lexema
    			.filter( reg -> reg.getToken().getLexema().equals(tokenIdentificador.getLexema()))
    			// Me quedo con los que son alcanzables desde el ambito actual
    			.filter( reg -> esVisible(reg.getToken()))
    			// De los declarados tomo el mas nuevo (mayor regId)
    			.max( (reg1, reg2) -> Integer.compare(reg1.getRegId(), reg2.getRegId()) )
				;
  
    	return regEncontrado;
    }
    
    /**
	 * Verifica si un identificador es visible para el ambito actual
	 * El alcance para un identificador segun su declaracion se define como sigue (mayor a menor):
	 *	1) main
	 *	2) Algun miembro dentro del anidamiento de ambitos (padre, abuelo, etc)
	 *	3) Ambito actual
	 * Para el caso de que no tenga ambito, significa que es un token de una sentencia ejecutable anterior
	 * la cual no fue declarada (entonces no tiene ambito).
	 * Por lo tanto se considera como que no esta al alcance.
	 * @param token
	 * @return
	 */
	private boolean esVisible(Token token){
		
		Ambito ambitoToken = token.getRegTabSimbolos().getAmbito(); 
		
		if (ambitoToken == null)
			return false;
		
		if (ambitoToken.isMain())
			return true;
		
		if (ambitoToken.equals(Ambito.getAmbitoActual()))
			return true;
		
		return Ambito.esAncestro(ambitoToken); 
	}
	
	/**
	 * Enlaza un token a su correspondiente declaracion.
	 * Estos tokens corresponden al uso de un identificador en las sentencias ejecutables
	 * @param reg Registro que contiene la declaracion correspondiente del identificador
	 * @param token Token a enlazar
	 */
	public void enlazarDeclaracion(RegTablaSimbolos reg, Token token) {
		//Elimino registro de la tabla de simbolos antes de reenlazar el token al nuevo registro
		this.tablaSimbolos.remove(token.getRegTabSimbolos());
		token.setRegTabSimbolos(reg);
	}
}
