package compilador;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase AnalizadorLexico
 * 
 * @author ileitao
 * @author gmaiola
 */
public class AnalizadorLexico {

	private static final int ESTADO_INICIAL = 0;

	private static final int ESTADO_FINAL = -1;

	private int estadoActual = ESTADO_INICIAL;

	private int lineaActual = 1;

	// Representa la matriz de estados y simbolos.
	// Almacena el indice del estado al que se avanza.
	private int[][] matEstados;

	// Representa la accion semantica asociada a cada estado.
	private AccionSemantica[][] matAccSem;

	private TablaDeSimbolos tablaSimbolos;

	// Tira de tokens
	private List<Token> tokens;

	// Lector de caracteres
	private LectorDeArchivo lector;
	
	private Logger logger;

	public AnalizadorLexico(LectorDeArchivo lector) {
		// this.matEstados = new int[cantEstados][cantSimbolos];
		// this.matAccSem = new AccionSemantica[cantEstados][cantSimbolos];
		this.lector = lector;
		this.tokens = new ArrayList<>();
		logger = new Logger();
	}

	public void setEstadoActual(int estadoActual) {
		this.estadoActual = estadoActual;
	}

	public int getEstadoActual() {
		return this.estadoActual;
	}

	public void setLineaActual(int lineaActual) {
		this.lineaActual = lineaActual;
	}

	public int getLineaActual() {
		return this.lineaActual;
	}

	public TablaDeSimbolos getTablaSimbolos() {
		return tablaSimbolos;
	}

	public List<Token> getTokens() {
		return tokens;
	}
	
	public Token getToken() {
		//TODO realizar la logica de avance de estados hasta obtener un token.
		//Se debe invocar al lector de archivos mientras los estados de la matriz no lleguen al estado final
		//En caso de error, se debera loguear.
		return null;
	}

}
