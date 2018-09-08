package compilador;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase AnalizadorLexico
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public class AnalizadorLexico {

	private static final int ESTADO_INICIAL = 0;

	private static final int ESTADO_FINAL = -1;

	private int estadoActual = ESTADO_INICIAL;

	private int lineaActual = 1;
        
	// Representa la matriz de estados y simbolos.
	// Almacena el indice del estado al que se avanza.
        // Filas: clase del simbolo - Columnas: nro de estado (0-16)
	private int[][] matEstados =
        {
            {10,  1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, -1, -1, -1, -1}, // [a-z] - [A-Z]
            {10,  1, -1, -1, -1,  6, -1, -1,  6, -1, 10, 11, 12, -1, -1, -1, -1}, // "F"
            {10,  1, -1,  4, -1, -1, -1, -1, -1, -1, 10, 11, 12, -1, -1, -1, -1}, // "u"
            {10,  1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, -1, -1, -1, -1}, // "i"
            { 2,  1,  2, -1, -1,  8,  9,  9,  8,  9, -1, 11, 12, -1, -1, -1, -1}, // [0-9]
            { 5, -1,  5, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "."
            { 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, -1, -1, -1, -1}, // "_"
            {-1, -1, -1, -1, -1, -1,  7, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "+"
            {-1, -1,  3, -1, -1, -1,  7, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "-"
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "*"
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "/"
            {16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // ":"
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "="
            {13, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "<"
            {14, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // ">"
            {15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "!"
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "("
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // ")"
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "{"
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "}"
            {11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, 12, -1, -1, -1, -1}, // "#"
            {12, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1, -1}, // "`"
            { 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // " "
            { 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1, -1}, // "/n"
            { 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "/t"
            { 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "otro"
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}, // $
            
        };

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
