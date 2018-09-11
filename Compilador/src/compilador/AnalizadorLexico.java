package compilador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import compilador.accionsemantica.AccionSemantica;
import compilador.accionsemantica.AccionSemantica01DescartarSimbolo;
import compilador.accionsemantica.AccionSemantica03ValidarFlotante;
import compilador.log.Logger;

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

    private static final int CANTIDAD_ESTADOS = 17;

    private static final int CANTIDAD_SIMBOLOS = 27;

    private int lineaActual = 1;

    //Usar StringBuffer solo por una cuestion de performance, ya que String es inmutable y genera un nuevo objeto String
    //por cada nuevo elemento, almacenandolo en la Pool de Strings.
    private StringBuffer lexemaParcial;

    private Character charActual;

    // Representa la matriz de estados y simbolos.
    // Almacena el indice del estado al que se avanza.
    // Filas: clase del simbolo - Columnas: nro de estado (0-16)
    private int[][] matEstados =
    {
    	/*EstadosestadoActual
    	{0,   1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16 */
    	
        {10,  1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, -1, -1, -1, -1}, // [a-z] - [A-Z]
        {10,  1, -1, -1, -1,  6, -1, -1,  6, -1, 10, 11, 12, -1, -1, -1, -1}, // "F"
        {10,  1, -1,  4, -1, -1, -1, -1, -1, -1, 10, 11, 12, -1, -1, -1, -1}, // "u"
        {10,  1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, -1, -1, -1, -1}, // "i"
        { 2,  1,  2, -1, -1,  8,  9,  9,  8,  9, -1, 11, 12, -1, -1, -1, -1}, // [0-9]
        { 5, -1,  5, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "."
        { 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, -1, -1, -1, -1}, // "_"
        {-1, -1, -1, -1, -1, -1,  7, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "+"matEstados
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
        {12, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1, -1}, // "'"
        { 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // " "
        { 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1, -1}, // "/n"
        { 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "/t"
        { 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, 12, -1, -1, -1, -1}, // "otro"
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}, // $

    };

    // Acciones semánticas
    //private AccionSemantica AS_1 = new AccionSemantica01HacerNada(this);
    //private AccionSemantica AS_2 = new AccionSemantica02ValidarEnteroSinSigno(this);
    //private AccionSemantica AS_3 = new AccionSemantica03ValidarFlotante(this);
    //private AccionSemantica AS_4 = new AccionSemantica04ValidarIdentificador(this);
    //private AccionSemantica AS_5 = new AccionSemantica05ValidarPalabraReservada(this);
    //private AccionSemantica AS_6 = new ...;
    //private AccionSemantica AS_7 = new ...;
    //private AccionSemantica AS_8 = new ...;

    // Representa la accion semantica asociada a cada estado.
    private AccionSemantica[][] matAccSem;

    private TablaDeSimbolos tablaSimbolos;

    // Tira de tokens
    private List<Token> tokens;

    // Lector de caracteres
    private LectorDeArchivo lector;

    private Logger logger;

    public AnalizadorLexico(LectorDeArchivo lector) {
        this.lector = lector;
        this.tablaSimbolos = new TablaDeSimbolos();
        this.tokens = new ArrayList<>();
        logger = new Logger();
        
        cargarMatAccSemanticas();
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
    
    public String getLexemaParcial() {
        return this.lexemaParcial.toString();
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
     * Obtiene el siguiente token pidiendole uno a uno los simbolos al lector de archivo.
     * @return El token reconocido.
     * @throws IOException En caso de que llegue al final del archivo.
     */
    public Token getToken() {
        //TODO realizar la logica de avance de estados hasta obtener un token.
        //Se debe invocar al lector de archivos mientras los estados de la matriz no lleguen al estado final
        //En caso de error, se debera loguear.
        this.lexemaParcial = new StringBuffer();
        this.estadoActual = AnalizadorLexico.ESTADO_INICIAL;
        
        //Se itera hasta llegar al estado final
        while (this.estadoActual != AnalizadorLexico.ESTADO_FINAL) {
            
        	//Leer un char
            this.charActual = this.lector.leerChar();
            
            //Obtengo el nuevo estado
            int fila = getFila(charActual);
            int estadoAnterior = this.estadoActual;
            
            estadoActual = matEstados[fila][estadoActual];

            if (this.estadoActual != AnalizadorLexico.ESTADO_FINAL)
	            //Ir armando el lexema parcial
	            this.lexemaParcial.append(this.charActual);
            else
            	matAccSem[fila][estadoAnterior].execute();
        }
        
        //Retrocedo el lector para no volver a procesar el ultimo caracter leido en la siguiente ejecucion.
        retrocederLectura();
        
        //Solo devuelve el token si no se recibio el caracter simbolico de ultimo estado.
        if (!this.charActual.equals('$')) {
	       
        	//FIXME Estos datos deben ser provistos al reconocer el token para poder almacenarlos en la tabla de simbolos.
        	//Ver como identificar que tipo de token es.
	        TipoSimbolo tipoSimbolo = TipoSimbolo.CADENA;
	        int posicionToken = lector.getPuntero();
	        int lineaToken = lector.getNroLinea();
	        
	        //Token obtenido
	        Token token = new Token(this.lexemaParcial.toString());
	        
	        RegTablaSimbolos reg = tablaSimbolos.getRegistro(token);
	        //Si no existe en la tabla de simbolos cargo el registro, sino devuelvo el token existente.
	        if(reg == null) {
	        	token.setId();
	        	this.tablaSimbolos.agregarSimbolo(new RegTablaSimbolos(token, tipoSimbolo, posicionToken));
	        }
	        return token;
        }
        else
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
            //Como ya se valido antes si era "F", "u", o "i" se valida que sea cualquier otra letra mayuscula o minuscula.
        } else if (Character.isLowerCase(caracter) || Character.isUpperCase(caracter)){
            return 0;
        }else if (Character.isDigit(caracter)) {
            return 4;
        } else if(caracter == '.'){
            return 5;
        } else if (caracter == '_') {
            return 6;
        } else if (caracter == '+') {
            return 7;
        } else if(caracter == '-') {
            return 8;
        } else if (caracter == '*') {
            return 9;
        } else if (caracter=='/') {
            return 10;
        } else if (caracter == ':') {
            return 11;
        } else if  (caracter == '=') {
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
        } else if (caracter == ' ') {
            return 21;
        } else if (caracter == '\'') {
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
    
    public void cargarMatAccSemanticas() {
    	this.matAccSem = new AccionSemantica[AnalizadorLexico.CANTIDAD_SIMBOLOS][AnalizadorLexico.CANTIDAD_ESTADOS];
    	
    	for(int i = 0; i < AnalizadorLexico.CANTIDAD_SIMBOLOS; i++)
    		for(int j = 0; j < AnalizadorLexico.CANTIDAD_ESTADOS; j++)
    			matAccSem[i][j] = new AccionSemantica03ValidarFlotante(this);
    	
    	//A MODO DE TEST
    	for(int i = 22; i < 26; i++)
    		matAccSem[i][0] = new AccionSemantica01DescartarSimbolo(this);
    }

	public Logger getLogger() {
		return logger;
	}
    
    
}
