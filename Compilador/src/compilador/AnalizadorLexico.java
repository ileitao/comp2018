package compilador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import compilador.accionsemantica.AccionSemantica;
import compilador.accionsemantica.general.ASConcatenarLexema;
import compilador.accionsemantica.general.ASDescartarToken;
import compilador.accionsemantica.general.ASError;
import compilador.accionsemantica.general.ASInicializarLexema;
import compilador.accionsemantica.general.ASNoAccion;
import compilador.accionsemantica.general.ASRetroceder;
import compilador.accionsemantica.validacion.ASCadenaCaracteres;
import compilador.accionsemantica.validacion.ASComparador;
import compilador.accionsemantica.validacion.ASOperador;
import compilador.accionsemantica.validacion.ASReconocerToken;
import compilador.accionsemantica.validacion.ASTokenOtro;
import compilador.accionsemantica.validacion.ASValidarEnteroSinSigno;
import compilador.accionsemantica.validacion.ASValidarFlotante;
import compilador.accionsemantica.validacion.ASValidarIdentificador;
import compilador.accionsemantica.validacion.ASValidarPalabraReservada;
import compilador.analizadorsintactico.Parser;
import compilador.log.Logger;

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

	private static final int CANTIDAD_ESTADOS = 17;

	private static final int CANTIDAD_SIMBOLOS = 27;

	private int estadoActual = ESTADO_INICIAL;
	
	private int estadoSiguiente = ESTADO_INICIAL;

	// Usar StringBuffer solo por una cuestion de performance, ya que String es
	// inmutable y genera un nuevo objeto String
	// por cada nuevo elemento, almacenandolo en la Pool de Strings.
	private StringBuffer lexemaParcial;

	private Character charActual;

	private TipoToken tipoToken = TipoToken.CADENA_CARACTERES;

	// Representa la matriz de estados y simbolos.
	// Almacena el indice del estado al que se avanza.
	// Filas: clase del simbolo - Columnas: nro de estado (0-16)
	private int[][] matEstados = {
		/*
		 * Estados
		 * 0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16
		 */

		{ 10,  1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, -1, -1, -1, -1 },//  0| [a-z] - [A-Z]
		{ -1,  1, -1, -1, -1,  6, -1, -1,  6, -1, 10, 11, 12, -1, -1, -1, -1 },//  1| "F"
		{ 10,  1, -1,  4, -1, -1, -1, -1, -1, -1, 10, 11, 12, -1, -1, -1, -1 },//  2| "u"
		{ 10,  1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, -1, -1, -1, -1 },//  3| "i"
		{  2,  1,  2, -1, -1,  8,  9,  9,  8,  9, -1, 11, 12, -1, -1, -1, -1 },//  4| [0-9]
		{  5, -1,  5, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },//  5| "."
		{  1, -1,  3, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },//  6| "_"
		{ -1, -1, -1, -1, -1, -1,  7, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },//  7| "+"matEstados
		{ -1, -1, -1, -1, -1, -1,  7, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },//  8| "-"
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },//  9| "*"
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },// 10| "/"
		{ 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },// 11| ":"
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },// 12| "="
		{ 13, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },// 13| "<"
		{ 14, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },// 14| ">"
		{ 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },// 15| "!"
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },// 16| "("
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },// 17| ")"
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },// 18| "{"
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },// 19| "}"
		{ 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, 12, -1, -1, -1, -1 },// 20| "#"
		{ 12, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1, -1 },// 21| "'"
		{  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },// 22| " "
		{  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1, -1 },// 23| "/n"
		{  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },// 24| "/t"
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1 },// 25| "otro"
		{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },// 26| $

	};

	// Acciones semánticas
	private Hashtable<Integer, AccionSemantica> accionesSemanticas;

	// Representa la accion semantica asociada a cada estado.
	private AccionSemantica[][] matAccSem;

	private TablaDeSimbolos tablaSimbolos;
	
	private List<Token> tiraTokens = new ArrayList<>();

	// Lector de caracteres
	private LectorDeArchivo lector;

	private Logger logger;

	public AnalizadorLexico(LectorDeArchivo lector) {
		this.lector = lector;
		this.tablaSimbolos = new TablaDeSimbolos();
		logger = new Logger();

		InicializarAccionesSemanticas();
		mapearMatAccSemanticas();
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

			RegTablaSimbolos reg = tablaSimbolos.getRegistro(this.lexemaParcial.toString());
			// Si no existe en la tabla de simbolos cargo el registro, sino devuelvo el
			// token existente.
			if (reg == null) {
				reg = this.tablaSimbolos.createRegTabla(this.lexemaParcial.toString(), this.tipoToken, lineaToken, posicionToken);
				this.tablaSimbolos.agregarSimbolo(reg);
			}
			
			//guardo el token reconocido en la tira de tokens.
			tiraTokens.add(reg.getToken());
			//retorno el token
			return reg.getToken();
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

	/**
	 * Carga la hash table con acciones semanticas para reutilizar los objetos en lugar de crear uno nuevo
	 * por cada estado.
	 */
	private void InicializarAccionesSemanticas() {
		this.accionesSemanticas = new Hashtable<>();
		this.accionesSemanticas.put(AccionSemantica.AS_NO_ACCION, new ASNoAccion(this));
		this.accionesSemanticas.put(AccionSemantica.AS_INICIALIZAR_LEXEMA, new ASInicializarLexema(this));
		this.accionesSemanticas.put(AccionSemantica.AS_CONCATENAR_LEXEMA, new ASConcatenarLexema(this));
		this.accionesSemanticas.put(AccionSemantica.AS_ERROR, new ASError(this));
		this.accionesSemanticas.put(AccionSemantica.AS_DESCARTAR_TOKEN, new ASDescartarToken(this));
		this.accionesSemanticas.put(AccionSemantica.AS_RETROCEDER, new ASRetroceder(this));
		
		//Acciones semanticas de reconocimiento
		this.accionesSemanticas.put(AccionSemantica.AS_TOKEN_IDENTIFICADOR, new ASReconocerToken(this, TipoToken.IDENTIFICADOR, new ASValidarIdentificador()));
		this.accionesSemanticas.put(AccionSemantica.AS_TOKEN_ENTERO_SIN_SIGNO, new ASReconocerToken(this, TipoToken.CONSTANTE_ENTERO_SIN_SIGNO, new ASValidarEnteroSinSigno()));
		this.accionesSemanticas.put(AccionSemantica.AS_TOKEN_FLOTANTE, new ASReconocerToken(this, TipoToken.CONSTANTE_FLOTANTE, new ASValidarFlotante()));
		this.accionesSemanticas.put(AccionSemantica.AS_TOKEN_OPERADOR_ARITMETICO, new ASReconocerToken(this, TipoToken.OPERADOR_ARITMETICO, new ASOperador()));
		this.accionesSemanticas.put(AccionSemantica.AS_TOKEN_OPERADOR_ASIGNACION, new ASReconocerToken(this, TipoToken.OPERADOR_ASIGNACION, new ASOperador(true)));
		this.accionesSemanticas.put(AccionSemantica.AS_TOKEN_COMPARADOR_SIMPLE, new ASReconocerToken(this, TipoToken.COMPARADOR, new ASComparador()));
		this.accionesSemanticas.put(AccionSemantica.AS_TOKEN_COMPARADOR_COMPUESTO, new ASReconocerToken(this, TipoToken.COMPARADOR, new ASComparador(true)));
		this.accionesSemanticas.put(AccionSemantica.AS_TOKEN_CADENA_CARACTERES, new ASReconocerToken(this, TipoToken.CADENA_CARACTERES, new ASCadenaCaracteres()));
		this.accionesSemanticas.put(AccionSemantica.AS_TOKEN_PALABRA_RESERVADA, new ASReconocerToken(this, TipoToken.PALABRA_RESERVADA, new ASValidarPalabraReservada()));
		this.accionesSemanticas.put(AccionSemantica.AS_TOKEN_OTRO, new ASReconocerToken(this, TipoToken.OTRO, new ASTokenOtro()));
		
	}
	
	/*
	 * Asigna una accion semantica a cada estado segun corresponda
	 */
	private void mapearMatAccSemanticas() {
		this.matAccSem = new AccionSemantica[CANTIDAD_SIMBOLOS][CANTIDAD_ESTADOS];
		
		//El simbolo $ se carga individualmente.
		//Sera un error para cualquier estado menos para el 0
		List<Integer> listaFilas = IntStream.range(0, CANTIDAD_SIMBOLOS-1).boxed().collect(Collectors.toList());
		
		//ESTADO 0
		for (int fila : listaFilas) {
			switch (fila) {
			case 1:
				matAccSem[fila][0] = accionesSemanticas.get(AccionSemantica.AS_ERROR);
				break;
			case 7:
			case 8:
			case 9:
			case 10:
				matAccSem[fila][0] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_OPERADOR_ARITMETICO);
				break;
			case 12:
			case 13:
			case 14:
				matAccSem[fila][0] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_COMPARADOR_SIMPLE);
				break;
			case 15:
				matAccSem[fila][0] = accionesSemanticas.get(AccionSemantica.AS_INICIALIZAR_LEXEMA);
				break;
			case 22:
			case 23:
			case 24:
				matAccSem[fila][0] = accionesSemanticas.get(AccionSemantica.AS_DESCARTAR_TOKEN);
				break;
			case 25:
				matAccSem[fila][0] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_OTRO);
				break;
			default:
				matAccSem[fila][0] = accionesSemanticas.get(AccionSemantica.AS_INICIALIZAR_LEXEMA);
				break;
			}
		}
		
		//ESTADO 1
		for (int fila : listaFilas) {
			if (Arrays.asList(0,1,2,3,4).contains(fila))
				matAccSem[fila][1] = accionesSemanticas.get(AccionSemantica.AS_CONCATENAR_LEXEMA);
			else
				//Se reconoce un token de tipo IDENTIFICADOR
				matAccSem[fila][1] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_IDENTIFICADOR);
		}
		
		//ESTADO 2
		for (int fila : listaFilas) {
			if (Arrays.asList(4,5,6).contains(fila))
				matAccSem[fila][2] = accionesSemanticas.get(AccionSemantica.AS_CONCATENAR_LEXEMA);
			else
				//para cualquier otro simbolo es accion semantica de error (reset)
				matAccSem[fila][2] = accionesSemanticas.get(AccionSemantica.AS_ERROR);
		}
		
		//ESTADO 3
		for (int fila : listaFilas) {
			if (fila == 2)
				matAccSem[fila][3] = accionesSemanticas.get(AccionSemantica.AS_CONCATENAR_LEXEMA);
			else
				//para cualquier otro simbolo es accion semantica de error (reset)
				matAccSem[fila][3] = accionesSemanticas.get(AccionSemantica.AS_ERROR);
		}
		
		//ESTADO 4
		for (int fila : listaFilas) {
			if (fila == 3)
				matAccSem[fila][4] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_ENTERO_SIN_SIGNO);
			else
				//para cualquier otro simbolo accion semantica de error
				matAccSem[fila][4] = accionesSemanticas.get(AccionSemantica.AS_ERROR);
		}
		
		//ESTADO 5
		for (int fila : listaFilas) {
			if(Arrays.asList(1,4).contains(fila))
				matAccSem[fila][5] = accionesSemanticas.get(AccionSemantica.AS_CONCATENAR_LEXEMA);
			else
				//Se reconoce un token de tipo FLOTANTE
				matAccSem[fila][5] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_FLOTANTE);
		}
		
		//ESTADO 6
		for (int fila : listaFilas) {
			if (Arrays.asList(4,7,8).contains(fila))
				matAccSem[fila][6] = accionesSemanticas.get(AccionSemantica.AS_CONCATENAR_LEXEMA);
			else
				//con cualquier otra cosa accion semantica de error
				matAccSem[fila][6] = accionesSemanticas.get(AccionSemantica.AS_ERROR);
		}
		
		//ESTADO 7
		for (int fila : listaFilas) {
			if (fila == 4)
				matAccSem[fila][7] = accionesSemanticas.get(AccionSemantica.AS_CONCATENAR_LEXEMA);
			else
				//con cualquier otro simbolo accion semantica de error
				matAccSem[fila][7] = accionesSemanticas.get(AccionSemantica.AS_ERROR);
		}
		
		//ESTADO 8
		for (int fila : listaFilas) {
			if(Arrays.asList(1,4).contains(fila))
				matAccSem[fila][8] = accionesSemanticas.get(AccionSemantica.AS_CONCATENAR_LEXEMA);
			else
				//Se reconoce un token de tipo FLOTANTE
				matAccSem[fila][8] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_FLOTANTE);
		}
		
		//ESTADO 9
		for (int fila : listaFilas) {
			if (fila == 4)
				matAccSem[fila][9] = accionesSemanticas.get(AccionSemantica.AS_CONCATENAR_LEXEMA);
			else
				//Con cualquier otra cosa accion semantica validar flotante
				matAccSem[fila][9] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_FLOTANTE);
		}
		
		//ESTADO 10
		//Se asume que se llega a traves de una letra minuscula por la ASInicializarLexema
		for (int fila : listaFilas) {
			if (Arrays.asList(0,1,2,3).contains(fila))
				matAccSem[fila][10] = accionesSemanticas.get(AccionSemantica.AS_CONCATENAR_LEXEMA);
			else
				//Se detecta palabra reservada
				matAccSem[fila][10] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_PALABRA_RESERVADA);
		}
		
		//ESTADO 11
		for (int fila : listaFilas) {
			if (fila == 20) 
				matAccSem[fila][11] = accionesSemanticas.get(AccionSemantica.AS_DESCARTAR_TOKEN);
			else
				matAccSem[fila][11] = accionesSemanticas.get(AccionSemantica.AS_CONCATENAR_LEXEMA);
		}
		
		//ESTADO 12
		for (int fila : listaFilas) {
			switch(fila) {
			case 21:
				matAccSem[fila][12] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_CADENA_CARACTERES);
				break;
			case 23:
				matAccSem[fila][12] = accionesSemanticas.get(AccionSemantica.AS_ERROR);
				break;
			default:
				matAccSem[fila][12] = accionesSemanticas.get(AccionSemantica.AS_CONCATENAR_LEXEMA);
				break;
			}
		}
		
		//ESTADO 13
		for (int fila : listaFilas) {
			if (fila == 12) 
				matAccSem[fila][13] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_COMPARADOR_COMPUESTO);
			else
				matAccSem[fila][13] = accionesSemanticas.get(AccionSemantica.AS_RETROCEDER);
		}
		
		//ESTADO 14
		for (int fila : listaFilas) {
			if (fila == 12) 
				matAccSem[fila][14] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_COMPARADOR_COMPUESTO);
			else
				matAccSem[fila][14] = accionesSemanticas.get(AccionSemantica.AS_RETROCEDER);
		}
		
		//ESTADO 15
		for (int fila : listaFilas) {
			if (fila == 12) 
				matAccSem[fila][15] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_COMPARADOR_COMPUESTO);
			else
				matAccSem[fila][15] = accionesSemanticas.get(AccionSemantica.AS_ERROR);
		}
		
		//ESTADO 16
		for (int fila : listaFilas) {
			if (fila == 12) 
				matAccSem[fila][16] = accionesSemanticas.get(AccionSemantica.AS_TOKEN_OPERADOR_ASIGNACION);
			else
				matAccSem[fila][16] = accionesSemanticas.get(AccionSemantica.AS_ERROR);
		}

		//Fila del simbolo $
		matAccSem[26][0] = accionesSemanticas.get(AccionSemantica.AS_NO_ACCION);
		for (int col = 1; col < CANTIDAD_ESTADOS; col++) {
			matAccSem[26][col] = accionesSemanticas.get(AccionSemantica.AS_ERROR);
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
	}

	public Hashtable<Integer, AccionSemantica> getAccionesSemanticas() {
		return accionesSemanticas;
	}

}
