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
	
	private short codigoTokenReconocido = -1;

	// Representa la matriz de estados y simbolos.
	// Almacena el indice del estado al que se avanza.
	// Filas: clase del simbolo - Columnas: nro de estado (0-17)
	private int[][] matEstados = {
		/*
		 * Estados
		 * 0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17
		 */
		{ 10,  1,  0,  0,  0, -1,  0,  0, -1, -1, 10, 11, 12, -1, -1,  0,  0,  0},//  0| [a-z] - [A-Z]
		{  0,  1,  0,  0,  0,  6,  0,  0,  6, -1,  0, 11, 12, -1, -1,  0,  0,  0},//  1| "F"
		{ 10,  1,  0,  4,  0, -1,  0,  0, -1, -1, 10, 11, 12, -1, -1,  0,  0,  0},//  2| "u"
		{ 10,  1,  0,  0, -1, -1,  0,  0, -1, -1, 10, 11, 12, -1, -1,  0,  0,  0},//  3| "i"
		{  2,  1,  2,  0,  0,  8,  9,  9,  8,  9, -1, 11, 12, -1, -1,  0,  0,  8},//  4| [0-9]
		{ 17, -1,  5,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},//  5| "."
		{  1, -1,  3,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},//  6| "_"
		{ -1, -1,  0,  0,  0, -1,  7,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},//  7| "+"
		{ -1, -1,  0,  0,  0, -1,  7,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},//  8| "-"
		{ -1, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},//  9| "*"
		{ -1, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},// 10| "/"
		{ 16, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},// 11| ":"
		{ -1, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1, -1, -1,  0},// 12| "="
		{ 13, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},// 13| "<"
		{ 14, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},// 14| ">"
		{ 15, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},// 15| "!"
		{ -1, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},// 16| "("
		{ -1, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},// 17| ")"
		{ -1, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},// 18| "{"
		{ -1, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},// 19| "}"
		{ 11, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1,  0, 12, -1, -1,  0,  0,  0},// 20| "#"
		{ 12, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, -1, -1, -1,  0,  0,  0},// 21| " ' "
		{  0, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},// 22| " "
		{  0, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11,  0, -1, -1,  0,  0,  0},// 23| "/n"
		{  0, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},// 24| "/t"
		{ -1, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1, 11, 12, -1, -1,  0,  0,  0},// 25| otro
		{ -1, -1,  0,  0,  0, -1,  0,  0, -1, -1, -1,  0,  0, -1, -1,  0,  0,  0},// 26| "$"
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
		 *		0		1			2  			3  		  4  		5  			6  			7  		  8  		9			10		  11		  12		13			14			15		 16			17
		 */
		{ asReconoc, asReconoc,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asReconoc, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},//  0| [a-z] - [A-Z]
		{  asErrMay, asReconoc,  asErrSim,  asErrSim,  asErrSim, asReconoc,  asErrSim,  asErrSim, asReconoc, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},//  1| "F"
		{ asReconoc, asReconoc,  asErrSim, asReconoc,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asReconoc, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},//  2| "u"
		{ asReconoc, asReconoc,  asErrSim,  asErrSim, asFinUInt, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asReconoc, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},//  3| "i"
		{ asReconoc, asReconoc, asReconoc,  asErrSim,  asErrSim, asReconoc, asReconoc, asReconoc, asReconoc, asReconoc, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim, asReconoc},//  4| [0-9]
		{ asReconoc,   asFinID, asReconoc,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},//  5| "."
		{ asReconoc,   asFinID, asReconoc,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},//  6| "_"
		{ asFinOper,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot, asReconoc,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},//  7| "+"
		{ asFinOper,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot, asReconoc,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},//  8| "-"
		{ asFinOper,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},//  9| "*"
		{ asFinOper,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 10| "/"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 11| ":"
		{ asFinOper,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper, asFinOper, asFinOper,  asErrSim},// 12| "="
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 13| "<"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 14| ">"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 15| "!"
		{ asFinOper,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 16| "("
		{ asFinOper,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 17| ")"
		{ asFinOper,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 18| "{"
		{ asFinOper,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 19| "}"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes,  asReinic, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 20| "#"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc,  asFinCad, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 21| " ' "
		{  asReinic,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 22| " "
		{  asReinic,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc,  asErrSim, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 23| "/n"
		{  asReinic,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 24| "/t"
		{ asFinOper,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes, asReconoc, asReconoc, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 25| "otro"
		{ asReconoc,   asFinID,  asErrSim,  asErrSim,  asErrSim, asFinFlot,  asErrSim,  asErrSim, asFinFlot, asFinFlot, asFinPRes,  asErrSim,  asErrSim, asFinOper, asFinOper,  asErrSim,  asErrSim,  asErrSim},// 26| $

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

			//System.out.println(this.getLexemaParcial());
			
			int posicionToken = lector.getPuntero() - this.lexemaParcial.length();
			int lineaToken = lector.getNroLinea();

			Token token;
			
			switch(this.codigoTokenReconocido) {
				//Almacenar token
				case Parser._IDENTIFIER:
				case Parser._CONSTANT_STRING:
				case Parser._CONSTANT_UNSIGNED_INTEGER:
				case Parser._CONSTANT_SINGLE:
					RegTablaSimbolos reg = this.tablaSimbolos.createRegTabla(this.lexemaParcial.toString(), this.codigoTokenReconocido, lineaToken, posicionToken);
					this.tablaSimbolos.agregarSimbolo(reg);
					
					token = reg.getToken();
					
					break;
				default:
					//No almacenar token
					token = new Token(this.lexemaParcial.toString(), this.codigoTokenReconocido);
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
			return 17;
		} else if (caracter == '{') {
			return 18;
		} else if (caracter == '}') {
			return 19;
		} else if (caracter == '#') {
			return 20;
		} else if (caracter == '\'') {
			return 21;
		} else if (caracter == ' ') {
			return 22;
		} else if (caracter == '\n') {
			return 23;
		} else if (caracter == '\t') {
			return 24;
		} else if (caracter == '$') {
			return 26;
		} else {
			return 25;
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
	}

	public short getCodigoTokenReconocido() {
		return codigoTokenReconocido;
	}

	public void setCodigoTokenReconocido(short codigoTokenReconocido) {
		this.codigoTokenReconocido = codigoTokenReconocido;
	}
}
