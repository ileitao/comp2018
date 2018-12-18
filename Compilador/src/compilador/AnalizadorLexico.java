package compilador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import compilador.accionsemantica.ASError;
import compilador.accionsemantica.ASOperador;
import compilador.accionsemantica.ASReconocerToken;
import compilador.accionsemantica.ASReiniciar;
import compilador.accionsemantica.ASValidarCadenaCarateres;
import compilador.accionsemantica.ASValidarEnteroSinSigno;
import compilador.accionsemantica.ASValidarFlotante;
import compilador.accionsemantica.ASValidarIdentificador;
import compilador.accionsemantica.ASValidarPalabraReservada;
import compilador.accionsemantica.AccionSemantica;
import compilador.analizadorsintactico.Parser;
import compilador.log.Logger;
import compilador.log.MensajesEventos;

/**
 * Clase AnalizadorLexico
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public class AnalizadorLexico {

	public static final int ESTADO_INICIAL = 0;

	public static final int ESTADO_FINAL = -1;

	private int estadoActual = ESTADO_INICIAL;
	
	private int estadoSiguiente = ESTADO_INICIAL;

	private StringBuffer lexemaParcial;

	private Character charActual;

	private TipoToken tipoToken = null;
	
//	private UsoToken usoToken = null;
	
	private short codigoTokenReconocido = -1;

	// Representa la matriz de estados y simbolos.
	// Almacena el indice del estado al que se avanza.
	// Filas: clase del simbolo - Columnas: nro de estado (0-17)
	private int[][] matEstados = {
		/*
		 * Estados
		 * 0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17
		 */
		{ 10,  1,  0,  0,  0, -1,  0,  0, -1, -1, 10, 11, 12, -1, -1,  0,  0},//  0| [a-z] - [A-Z]
		{  0,  1,  0,  0,  0,  6,  0,  0,  6, -1,  0, 11, 12, -1, -1,  0,  0},//  1| "F"
		{ 10,  1,  0,  4,  0, -1,  0,  0, -1, -1, 10, 11, 12, -1, -1,  0,  0},//  2| "u"
		{ 10,  1,  0,  0, -1, -1,  0,  0, -1, -1, 10, 11, 12, -1, -1,  0,  0},//  3| "i"
		{  2,  1,  2,  0,  0,  8,  9,  9,  8,  9, -1, 11, 12, -1, -1,  0,  8},//  4| [0-9]
		{ 17, -1,  5,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},//  5| "."
		{  1, -1,  3,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},//  6| "_"
		{ 14, -1,  0,  0,  0, -1,  7,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},//  7| "+"
		{ 14, -1,  0,  0,  0, -1,  7,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},//  8| "-"
		{ 14, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},//  9| "*"
		{ 14, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},// 10| "/"
		{ 15, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},// 11| ":"
		{ 14, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, 14, -1, 14,  0},// 12| "="
		{ 13, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},// 13| "<"
		{ 13, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},// 14| ">"
		{ 15, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},// 15| "!"
		{ 14, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},// 16| "( ) { } , ;"
		{ 11, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1,  0, 12, -1, -1,  0,  0},// 20| "#"	17
		{ 12, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, -1, -1, -1,  0,  0},// 21| " ' "	18
		{  0, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},// 22| " "	19
		{  0, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11,  0, -1, -1,  0,  0},// 23| "/n"	20
		{  0, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},// 24| "/t"	21
		{ -1, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0},// 25| otro	22
		{ -1, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1,  0,  0, -1, -1,  0,  0},// 26| "$"	23
	};

	private AccionSemantica asReconoc 	= new ASReconocerToken(this);
	private AccionSemantica asFinID 	= new ASValidarIdentificador(this);
	private AccionSemantica asFinUInt	= new ASValidarEnteroSinSigno(this);
	private AccionSemantica asFinFlot	= new ASValidarFlotante(this);
	private AccionSemantica asFinPRes	= new ASValidarPalabraReservada(this);
	private AccionSemantica asFinCad	= new ASValidarCadenaCarateres(this);
	private AccionSemantica asFinOper 	= new ASOperador(this);
	private AccionSemantica asReinic	= new ASReiniciar(this);
	private AccionSemantica asErrSim	= new ASError(this, MensajesEventos.SIMBOLO_INESPERADO);
	private AccionSemantica asErrMay	= new ASError(this, MensajesEventos.ERROR_MAYUSCULA + "F");

	// Representa la accion semantica asociada a cada estado.
	private AccionSemantica[][] matAccSem = {
		/*
		 * Estados
		 *		0		1			2  			3  		  4  		5  			6  			7  		  8  		9			10		  11		  12		13			14			15		 16			Simbolo
		 */
		{ asReconoc, asReconoc,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asReconoc, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},//  0| [a-z] - [A-Z]
		{  asErrMay, asReconoc,  asErrSim,  asErrSim,  asErrSim, asReconoc,  asErrSim,  asErrSim, asReconoc, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},//  1| "F"
		{ asReconoc, asReconoc,  asErrSim, asReconoc,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asReconoc, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},//  2| "u"
		{ asReconoc, asReconoc,  asErrSim,  asErrSim, asFinUInt, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asReconoc, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},//  3| "i"
		{ asReconoc, asReconoc, asReconoc,  asErrSim,  asErrSim, asReconoc, asReconoc, asReconoc, asReconoc, asReconoc, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim, asReconoc},//  4| [0-9]
		{ asReconoc,   asFinID, asReconoc,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},//  5| "."
		{ asReconoc,   asFinID, asReconoc,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},//  6| "_"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot, asReconoc,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},//  7| "+"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot, asReconoc,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},//  8| "-"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},//  9| "*"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},// 10| "/"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},// 11| ":"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asReconoc, asFinOper, asReconoc,  asErrSim},// 12| "="
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},// 13| "<"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},// 14| ">"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},// 15| "!"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},// 16| "( ) { } , ;"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes,  asReinic, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},// 20| "#"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc,  asFinCad, asFinOper, asFinOper,  asErrSim,  asErrSim},// 21| " ' "
		{  asReinic,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},// 22| " "
		{  asReinic,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc,  asErrSim, asFinOper, asFinOper,  asErrSim,  asErrSim},// 23| "/n"
		{  asReinic,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},// 24| "/t"
		{ asFinOper,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim},// 25| "otro"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes,  asErrSim,  asErrSim, asFinOper, asFinOper,  asErrSim,  asErrSim},// 26| $

	};

	private TablaDeSimbolos tablaSimbolos;
	
	private List<Token> tiraTokens = new ArrayList<>();

	// Lector de caracteres
	private LectorDeArchivo lector;

	private Logger logger;

	public AnalizadorLexico(LectorDeArchivo lector) {
		this.lector = lector;
		this.tablaSimbolos = new TablaDeSimbolos();
		logger = new Logger();
	}

	public void setEstadoActual(int estadoActual) {
		this.estadoActual = estadoActual;
	}

	public int getEstadoActual() {
		return this.estadoActual;
	}

	public int getEstadoSiguiente() {
		return estadoSiguiente;
	}

	public void setEstadoSiguiente(int estadoSiguiente) {
		this.estadoSiguiente = estadoSiguiente;
	}

	/**
	 * Inciso C: La numeración de las líneas de código debe comenzar en 1
	 * @return El numero de linea del lector de archivo mas uno.
	 */
	public int getLineaActual() {
		return this.lector.getNroLinea() + 1;
	}
        
        public int getPunteroActual() {
                return this.lector.getPuntero();
        }

	public TablaDeSimbolos getTablaSimbolos() {
		return tablaSimbolos;
	}

	public StringBuffer getLexemaParcial() {
		return this.lexemaParcial;
	}

	public void setLexemaParcial(String nuevoLexema) {
		this.lexemaParcial = new StringBuffer(nuevoLexema);
	}

	public Character getCharActual() {
		return this.charActual;
	}

	public void setCharActual(Character caracter) {
		this.charActual = caracter;
	}

	/**
	 * Obtiene el siguiente token pidiendole uno a uno los simbolos al lector de
	 * archivo.
	 * 
	 * @return El token reconocido.
	 * @throws IOException
	 *             En caso de que llegue al final del archivo.
	 */
	public Token getToken() {

		// Seteo estado inicial del automata
		reiniciar();

		// Se itera hasta llegar al estado final
		while (this.estadoActual != ESTADO_FINAL) {

			// Leer siguiente simbolo
			this.charActual = this.lector.leerChar();

			int filaSimbolo = getFila(charActual);
			
			estadoSiguiente = matEstados[filaSimbolo][estadoActual];
			
			// Ejecuto accion semantica asociada
			matAccSem[filaSimbolo][this.estadoActual].execute();
			
			// Avanzo de estado
			estadoActual = estadoSiguiente;
		}

		// Solo devuelve el token si no se recibio el caracter simbolico de ultimo
		// estado.
		if (!this.charActual.equals('$')) {
			
			int posicionToken = lector.getPuntero() - this.lexemaParcial.length();
			int lineaToken = lector.getNroLinea();

			Token token;
			RegTablaSimbolos reg = this.tablaSimbolos.getRegistro(this.lexemaParcial.toString());
			
			//Solo se almacenan los tokens detallados en el switch
			switch(this.codigoTokenReconocido) {
				case Parser._IDENTIFIER:
				case Parser._CONSTANT_STRING:
				case Parser._CONSTANT_UNSIGNED_INTEGER:
				case Parser._CONSTANT_SINGLE:
					//Se genera el registro para insertar en la tabla de simbolos
					reg = this.tablaSimbolos.createRegTabla(this.lexemaParcial.toString(), this.codigoTokenReconocido, lineaToken, posicionToken);
					
					switch (this.codigoTokenReconocido) {
					
					//Se va a setear en el analizador sintactico
					//En caso de ser identificador seteo el tipo de uso
//					case Parser._IDENTIFIER:
//						reg.setUsoToken(this.usoToken);
//						break;
					
					//Se setea tipo de token para constantes
					case Parser._CONSTANT_UNSIGNED_INTEGER:
					case Parser._CONSTANT_SINGLE:
					case Parser._CONSTANT_STRING:
						//El tipo de token de los identificadores lo setea el A. Sintactico
						reg.setTipoToken(this.tipoToken);
						break;
					default:
						break;
					}
					
					this.tablaSimbolos.agregarSimbolo(reg);
					
					token = reg.getToken();
//					token.setRegTabSimbolos(reg);
					
					break;
				default:
					//No almacenar token
					if (reg == null)
						token = new Token(this.lexemaParcial.toString(), this.codigoTokenReconocido);
					else
						token = reg.getToken();
					break;
			}
			
			//guardo el token reconocido en la tira de tokens.
			tiraTokens.add(token);
			//retorno el token
			return token;
		} else
			return null;
	}

	public void retrocederLectura() {
		this.lector.retrocederPuntero();
	}

	/**
	 * Retorna el número de fila a partir del caracter recibido
	 * 
	 * @param simboloActual
	 * @return
	 */
	private int getFila(Character caracter) {

		if (caracter == 'F') {
			return 1;
		} else if (caracter == 'u') {
			return 2;
		} else if (caracter == 'i') {
			return 3;
			// Como ya se valido antes si era "F", "u", o "i" se valida que sea cualquier
			// otra letra mayuscula o minuscula.
		} else if (Character.isLowerCase(caracter) || Character.isUpperCase(caracter)) {
			return 0;
		} else if (Character.isDigit(caracter)) {
			return 4;
		} else if (caracter == '.') {
			return 5;
		} else if (caracter == '_') {
			return 6;
		} else if (caracter == '+') {
			return 7;
		} else if (caracter == '-') {
			return 8;
		} else if (caracter == '*') {
			return 9;
		} else if (caracter == '/') {
			return 10;
		} else if (caracter == ':') {
			return 11;
		} else if (caracter == '=') {
			return 12;
		} else if (caracter == '<') {
			return 13;
		} else if (caracter == '>') {
			return 14;
		} else if (caracter == '!') {
			return 15;
		} else if (caracter == '(') {
			return 16;
		} else if (caracter == ')') {
			return 16;
		} else if (caracter == '{') {
			return 16;
		} else if (caracter == '}') {
			return 16;
		} else if (caracter == ',') {
			return 16;
		} else if (caracter == ';') {
			return 16;
		} else if (caracter == '#') {
			return 17;
		} else if (caracter == '\'') {
			return 18;
		} else if (caracter == ' ') {
			return 19;
		} else if (caracter == '\n') {
			return 20;
		} else if (caracter == '\t') {
			return 21;
		} else if (caracter == '$') {
			return 23;
		} else {
			return 22;
		}
	}

	public Logger getLogger() {
		return logger;
	}

	public TipoToken getTipoToken() {
		return tipoToken;
	}

	public void setTipoToken(TipoToken tipoToken) {
		this.tipoToken = tipoToken;
	}

	public List<Token> getTiraTokens() {
		return tiraTokens;
	}

	/**
	 * Configura el analizador lexico en el estado inicial y setea un nuevo lexema vacio.
	 */
	public void reiniciar() {
		setEstadoActual(ESTADO_INICIAL);
		setEstadoSiguiente(ESTADO_INICIAL);
		setLexemaParcial("");
		setCodigoTokenReconocido((short) -1);
		setTipoToken(null);
	}

	public short getCodigoTokenReconocido() {
		return codigoTokenReconocido;
	}

	public void setCodigoTokenReconocido(short codigoTokenReconocido) {
		this.codigoTokenReconocido = codigoTokenReconocido;
	}
}
