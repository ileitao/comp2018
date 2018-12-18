/*** 1-DECLARATION SECTION ***/
%{
import java.io.IOException;
import compilador.AnalizadorLexico;
import compilador.RegTablaSimbolos;
import compilador.Ambito;
import compilador.TablaDeSimbolos;
import compilador.TipoToken;
import compilador.UsoToken;
import compilador.Token;
import compilador.log.Logger;
import compilador.log.EventoLog;
import static java.lang.Math.toIntExact;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import compilador.accionsemantica.ASValidarFlotante;
import compilador.codigointermedio.PolacaInversa;
import compilador.codigointermedio.ElementoPI;
%}

/*** 2-YACC DECLARATIONS ***/

/* RESERVED KEYS [if else endif print usinteger single for void fun return ]*/
%token _IF _ELSE _ENDIF _PRINT _USINTEGER _SINGLE _FOR _VOID _FUN _RETURN

/* ARITHMETIC OPERATORS [ + - * / ] */
%token _PLUS _MINUS _MULT _DIV

/* ASIGNATION OPERATORS [ := ] */
%token _ASSIGN

/* COMPARATORS [ = < <= > >= != ] */
%token _EQUAL _LESSER _LESSER_OR_EQUAL _GREATER _GREATER_OR_EQUAL _UNEQUAL

/* OTHERS [ ( ) { } , ; ' ] */
%token _LPAREN _RPAREN _LCBRACE _RCBRACE _COMMA _SEMICOLON _QUOTE _IDENTIFIER _CONSTANT_UNSIGNED_INTEGER _CONSTANT_SINGLE _CONSTANT_STRING

%right _PLUS _MINUS
%right _MULT _DIV
%right _ELSE

%start programa
%%

/*** 3-GRAMMAR FOLLOWS ***/

/**
 * Programa
 * Conjunto de sentencias sin delimitador
 */
programa :
	sentencia
  | programa sentencia
	;

/**
 * Sentencia
 * Declarativas o ejecutables
 */
sentencia :
	bloque_declarativo
  | bloque_ejecutable
;

/**
 * Bloque_declarativo
 * Tira de sentencias declarativas
 */
bloque_declarativo :
  sentencias_de_declaracion_de_variables
  ;

/**
 * Sentencias de declaracion de variables
 * <tipo> <lista_de_variables> ","
 */
sentencias_de_declaracion_de_variables :
	tipo lista_de_variables _COMMA { notify("Sentencia de declaración de variables en línea " + this.lineaActual + ".");
									 declararIdentificadores((Token)$2.obj, UsoToken.VARIABLE);
									 }
	| tipo error _COMMA { yyerror("ERROR: No se definió ninguna variable en sentencia de declaración de variables", this.lineaActual); }
	| declaracion_de_funcion
	/* Se agrega declaracion de variables de tipo FUN */
	| _FUN lista_de_variables _COMMA
		{
		this.tipoActual = TipoToken.FUN;
		declararIdentificadores((Token)$2.obj, UsoToken.VARIABLE);
		}
	;

/**
 * Tipo
 * Tipos _USINTEGER Y _SINGLE
 */
tipo :
	_USINTEGER { this.tipoActual = TipoToken.USINTEGER; }
	| _SINGLE { this.tipoActual = TipoToken.SINGLE;	}
	;

/**
 * Lista de variables
 * Las variables se separan con ";"
 */
lista_de_variables:
  _IDENTIFIER
	|	_IDENTIFIER _SEMICOLON lista_de_variables { addIdentifier( (Token) $3.obj ); }
	| _IDENTIFIER error lista_de_variables { yyerror("ERROR: Falta ; para separar variables en la sentencia de declaración de variables", this.lineaActual); }
	;

inicio_funcion :
	_FUN _IDENTIFIER {
		this.tipoActual = TipoToken.FUN;
		
		/*Configura tipo y uso de identificador*/
		declararIdentificadores((Token) $2.obj, UsoToken.FUNCION);

		//Apilo y obtengo nuevo ambito con el nombre de la funcion actual
		Ambito.nuevoAmbito( (Token) $1.obj, (Token) $2.obj );

		//apilo contador de closures para poder controlar cantidad en caso de que anide funciones
		//(lo cual seria un error, pero aun asi necesito llevar el control)
		pilaClosures.push(this.numClosuresEnFuncion);
		this.numClosuresEnFuncion = 0;
	}
	| _FUN error { yyerror("ERROR: No se definió nombre para la función", this.lineaActual); }
	;

inicio_closure :
	_VOID _IDENTIFIER {
		this.tipoActual = TipoToken.VOID;
		//Incremento para control de closures declarados en funcion
		this.numClosuresEnFuncion++;

		if (this.numClosuresEnFuncion > 1)
			yyerror("ERROR: Solo se permite la declaracion de 1 unica funcion sin retorno dentro de una funcion FUN (" + Ambito.getAmbitoActual() +")", this.lineaActual);

		//Pido el token que genero el ambito actual para ver si es tipo FUN
		Token tokenAmbito = Ambito.getAmbitoActual().getTokenIdentificador();
		
		//Con esto se controla que la funcion sin retorno solo sea declarada dentro del ambito de una funcion con retorno
		if (Ambito.getAmbitoActual().isMain() || !TipoToken.FUN.equals(tokenAmbito.getRegTabSimbolos().getTipoToken()) )
			yyerror("ERROR: Declaración de función sin retorno ("+((Token) $2.obj).getLexema()+") en un ambito que no es una funcion FUN (" + Ambito.getAmbitoActual() +")", this.lineaActual);

		/*Configura tipo y uso de identificadores*/
		declararIdentificadores((Token) $2.obj, UsoToken.FUNCION);
	
		//Apilo y obtengo nuevo ambito con el nombre del closure actual
		Ambito.nuevoAmbito( (Token) $1.obj, (Token) $2.obj );
	}
	|_VOID error { yyerror("ERROR: No se definió nombre para la función", this.lineaActual); }
	;

/**
 * Declaración de función
 * fun ID () {
 *   <cuerpo_de_funcion> // conjunto de sentencias declarativas y ejecutables
 *   return ( <retorno> ) ","
 * }
 *
 * void ID () {
 *   <cuerpo_de_funcion> // conjunto de sentencias declarativas y ejecutables
 * }
 */
declaracion_de_funcion :
	inicio_funcion _LPAREN _RPAREN _LCBRACE cuerpo_de_funcion retorno_de_funcion _RCBRACE
		{	
			notify("Sentencia de declaración de función con retorno " + this.lineaActual + ".");

			// Restauro ambito anterior
			Ambito.finalizarAmbito();
			this.numClosuresEnFuncion = pilaClosures.pop();
		}
	| inicio_closure _LPAREN _RPAREN _LCBRACE cuerpo_de_funcion _RCBRACE
		{	notify("Sentencia de declaración de función sin retorno " + this.lineaActual + ".");

			// Restauro ambito anterior
			Ambito.finalizarAmbito();
		}
	| inicio_funcion _LPAREN _RPAREN _LCBRACE cuerpo_de_funcion error _RCBRACE { yyerror("ERROR: Falta retorno de la función", this.lineaActual); }
	;

/**
 * Cuerpo de función
 * Conjunto de sentencias declarativas y ejecutables
 */
cuerpo_de_funcion :
	sentencia
	| sentencia cuerpo_de_funcion
  ;

/**
 * Retorno de función
 * return ( <retorno> ) ","
 */
retorno_de_funcion :
  _RETURN _LPAREN retorno _RPAREN _COMMA
  ;

/**
 * Retorno
 * Un identificador seguido de "()" o el cuerpo de una función
 */
retorno :
  _IDENTIFIER _LPAREN _RPAREN
		{
		Token tokenIdentificador = (Token)$1.obj;
		validarDeclaracionIdentificador(tokenIdentificador);
		// Valido el tipo de retorno
		if (!TipoToken.VOID.equals( tokenIdentificador.getRegTabSimbolos().getTipoToken() ))
			yyerror("ERROR: La funcion retornada ("+ tokenIdentificador.getLexema() +") debe ser de tipo VOID", this.lineaActual);
		}
  | cuerpo_de_funcion
  ;

/**
 * Bloque ejecutable
 * Sentencias ejecutables
 */
bloque_ejecutable :
	seleccion
	| iteracion
	| asignacion
	| impresion
	| invocacion_de_funcion
	;

/**
 * Selección
 * if ( <condicion> ) <bloque_de_sentencias> else <bloque_de_sentencias>
 */
seleccion :
	_IF _LPAREN condicion _RPAREN bloque_de_sentencias _ENDIF
		{	notify("Sentencia IF sin ELSE en línea " + this.lineaActual + ".");
			polaca.completarPasoIncompleto(0);
		}
	| _IF _LPAREN condicion _RPAREN bloque_de_sentencias _ELSE { polaca.generarElse(); } bloque_de_sentencias _ENDIF
	{	notify("Sentencia IF con ELSE en línea " + this.lineaActual + ".");
		polaca.completarPasoIncompleto(0);
	}

	| _IF _LPAREN error _RPAREN bloque_de_sentencias _ENDIF {	yyerror("ERROR: Faltó condición en IF", this.lineaActual);	}
	| _IF _LPAREN condicion _RPAREN error _ENDIF {	yyerror("ERROR: Faltó bloque de sentencias en IF", this.lineaActual);	}
	;

/**
 * Bloque de selección
 * Asignaciones, selecciones y sentencias de control
 * @TODO Agregar while
 */
bloque_de_sentencias :
	bloque_ejecutable
	| _LCBRACE asignacion_compuesta _RCBRACE
	;

/**
 * Condicion
 * Comparación entre expresiones aritméticas, variables o constantes
 */
condicion :
	expresion comparador expresion { polaca.addElemento( new ElementoPI( ((Token)$2.obj).getLexema(), (Token)$1.obj)); polaca.generarBifurcacion("BF"); }
  ;

/**
 * Asignación
 * <_IDENTIFIER> := <expresion>
 */
asignacion :
	_IDENTIFIER _ASSIGN expresion _COMMA
		{
		notify("Sentencia de asignación en línea " + this.lineaActual + ".");

		validarDeclaracionIdentificador((Token)$1.obj);

		//Valido semantica para el caso de asignacion de variables de tipo FUN
		if ( TipoToken.FUN.equals( ((Token)$1.obj).getRegTabSimbolos().getTipoToken()) ){
			if (cantFactores > 1)
				yyerror("ERROR: No se permiten asignacion de expresiones para identificadores de tipo FUN", this.lineaActual);
			
			//$3 tiene el ultimo token reconocido de la expresion
			else if (!TipoToken.FUN.equals( ((Token)$3.obj).getRegTabSimbolos().getTipoToken()) )
				yyerror("ERROR: Se esperaba un identificador de tipo FUN despues del simbolo de asignacion :=", this.lineaActual);
		}
		cantFactores = 0;

		//Genero codigo intermedio para asignacion
		polaca.addElemento( new ElementoPI( ((Token)$1.obj).getLexema(), (Token)$1.obj));
		polaca.addElemento( new ElementoPI( ((Token)$2.obj).getLexema(), (Token)$2.obj));
		}
	| _IDENTIFIER _ASSIGN invocacion_de_funcion
		{
		notify("Sentencia de asignación en línea " + this.lineaActual + ".");
		validarDeclaracionIdentificador( (Token)$1.obj );
		}
 ;

/**
 * Asignación
 * <_IDENTIFIER_1> := <expresion>
 * <_IDENTIFIER_2> := <expresion>
 * ...
 */
asignacion_compuesta :
	bloque_ejecutable
	| bloque_ejecutable asignacion_compuesta
	;

/**
 * Impresión
 * print <(cadena)> ,
 */
impresion :
  _PRINT _LPAREN _CONSTANT_STRING _RPAREN _COMMA
		{	notify("Sentencia PRINT en línea " + this.lineaActual + ".");
			polaca.addElemento( new ElementoPI( ((Token)$3.obj).getLexema(), (Token)$3.obj));
			polaca.addElemento( new ElementoPI( ((Token)$1.obj).getLexema(), (Token)$1.obj));
		}
  | _PRINT _LPAREN error _RPAREN	_COMMA { yyerror("ERROR: No se especificó ninguna cadena en sentencia PRINT", this.lineaActual); }
  ;

/**
* Fue necesario agregar esta regla para evitar conflictos al generar codigo intermedio para apilar el paso inicial 
* del FOR.
*/
inicio_iteracion :
	_FOR { polaca.generarInicioCondicionFOR(); }
	;

/**
 * Iteración
 *  for ( <condiciones_de_iteracion> ) <bloque_de_sentencias> ,
 */
iteracion :
	inicio_iteracion _LPAREN condiciones_de_iteracion _RPAREN bloque_de_sentencias _COMMA
		{	notify("Sentencia FOR en línea " + this.lineaActual + ".");
			polaca.generarBloqueFOR(pilaAcumulador.pop(), pilaAcumulador.pop());
		}

	| inicio_iteracion _LPAREN condiciones_de_iteracion _RPAREN error _COMMA {	yyerror("ERROR: No se especificó  ningún bloque de sentencias en sentencia FOR", this.lineaActual);	}
	;

/**
 * Condiciones_de_iteracion
 * (i := n ; <condicion> ; j )
 *
 * Es necesario guardar el token del iterador y el acumulador para poder realizar la asignacion de incremento del iterador al final del
 * while (antes del salto atras BI).
 * Para esto se utiliza una pila de token exclusiva para poder mantener juntos el iterador y el acumulador para cuando se aniden
 * sentencias de control.
 */
condiciones_de_iteracion :
	inicializacion_iteracion condicion_iteracion incremento_iteracion
	;

inicializacion_iteracion :
	_IDENTIFIER _ASSIGN _CONSTANT_UNSIGNED_INTEGER _SEMICOLON
		{
		Token tokenIdentificador = ((Token)$1.obj);

		//Valido semantica de uso para la variable del iterador
		RegTablaSimbolos regDeclaracion = validarDeclaracionIdentificador(tokenIdentificador);	
		if ( (regDeclaracion != null) && (!TipoToken.USINTEGER.equals(regDeclaracion.getTipoToken()) ))
			yyerror("ERROR: La variable usada en el iterador de la sentencia de iteracion FOR, debe ser de tipo entero");	

		//Realiza acciones necesarias para generar inicio de iteracion para polaca inversa
		inicializarIteracion((Token)$1.obj, (Token)$2.obj, (Token)$3.obj);
		}
	
	| _IDENTIFIER _ASSIGN error _SEMICOLON
		{
		//Realiza acciones necesarias para generar inicio de iteracion para polaca inversa
		inicializarIteracion((Token)$1.obj, (Token)$2.obj, (Token)$3.obj);
		yyerror("ERROR: Solo se permite el uso de constantes enteras para inicializar la variable de iteracion", this.lineaActual);
		}
	;

condicion_iteracion :
	/*_IDENTIFIER comparador _CONSTANT_UNSIGNED_INTEGER _SEMICOLON*/
	condicion_iteracion_inicio comparador expresion _SEMICOLON
		{	
		polaca.addElemento( new ElementoPI( ((Token)$2.obj).getLexema(), (Token)$2.obj));
		}
	
	| error comparador expresion _SEMICOLON
		{	
		yyerror("ERROR: Falta variable en lado izquierdo del comparador en la condicion de iteracion FOR.");
		}
	
	| condicion_iteracion_inicio error expresion _SEMICOLON
		{	
		yyerror("ERROR: Falta comparador en la condicion de iteracion FOR.");
		}
	;

incremento_iteracion :
	_CONSTANT_UNSIGNED_INTEGER
			{
			pilaAcumulador.push( (Token)$1.obj );
			polaca.generarBifurcacion("BF");
			}

	| error
		{
		//Semantica para la constante de incremento del iterador
		yyerror("ERROR: Solo se permite el uso de constantes enteras para incrementar la variable de iteracion FOR");
		pilaAcumulador.push( (Token)$1.obj );
		polaca.generarBifurcacion("BF");		
		}
	;

/**
* Fue necesario incluir este no terminal para poder generar el codigo intermedio del identificador antes que la expresion
* para la condicion de la sentencia de iteracion (sino se generaba primero la expresion)
*/
condicion_iteracion_inicio :
	_IDENTIFIER {polaca.addElemento( new ElementoPI( ((Token)$1.obj).getLexema(), (Token)$1.obj));}
	;

/**
 * Invocación de función
 * fun () ,
 */
invocacion_de_funcion :
	_IDENTIFIER _LPAREN _RPAREN _COMMA
		{
		Token tokenIdentificador = (Token)$1.obj;
		
		//Valido semantica de uso de funciones
		validarDeclaracionIdentificador(tokenIdentificador);
		if (!TipoToken.FUN.equals(tokenIdentificador.getRegTabSimbolos().getTipoToken()))
			yyerror("ERROR: El identificador " + ((Token) $1.obj).getLexema() + " no es de tipo fun.");
		}
	;

/**
 * Expresión
 * Aritmética, variable o constante
 */
expresion :
	expresion _PLUS termino { polaca.addElemento( new ElementoPI( ((Token)$2.obj).getLexema(), (Token)$2.obj));}
	|	expresion _MINUS termino { polaca.addElemento( new ElementoPI( ((Token)$2.obj).getLexema(), (Token)$2.obj));}
	|	termino
	;

/**
 * Término
 * Aritmética, variable o constante
 */
termino :
	termino _MULT factor
		{
		polaca.addElemento( new ElementoPI( ((Token)$2.obj).getLexema(), (Token)$2.obj));
		cantFactores++;
		}
	|	termino _DIV factor
		{
		polaca.addElemento( new ElementoPI( ((Token)$2.obj).getLexema(), (Token)$2.obj));
		cantFactores++;
		}
	|	factor {cantFactores++;}
	;

/**
 * Factor
 * Constantes unsigned integer, single, string o identificador
 */
factor :
	_CONSTANT_UNSIGNED_INTEGER { polaca.addElemento( new ElementoPI( ((Token)$1.obj).getLexema(), (Token)$1.obj));}
	| _CONSTANT_SINGLE { polaca.addElemento( new ElementoPI( ((Token)$1.obj).getLexema(), (Token)$1.obj));}
	| _MINUS _CONSTANT_SINGLE { validarFlotante((Token) $2.obj);
								polaca.addElemento( new ElementoPI( ((Token)$2.obj).getLexema(), (Token)$2.obj));}
	/*NO SE ACEPTA CONSTANTE STRING PORQUE ESTA SOLO SERA USADA EN PRINT
	| _CONSTANT_STRING
	*/
	| _IDENTIFIER
		{
		polaca.addElemento( new ElementoPI( ((Token)$1.obj).getLexema(), (Token)$1.obj));
		validarDeclaracionIdentificador((Token)$1.obj);
		}
	| error { yyerror("ERROR: Se esperaba un factor en lugar del token: " + ((Token) $1.obj).getLexema()); }
	;

/**
 * Comparador
 * <, >, <=, >=, ==, !=
 */
comparador :
	_LESSER
	|	_GREATER
	|	_LESSER_OR_EQUAL
	|	_GREATER_OR_EQUAL
	|	_EQUAL
	|	_UNEQUAL
	;

%%

/*** 4-CODE ***/
AnalizadorLexico analizadorLexico;
TablaDeSimbolos tablaDeSimbolos;
Logger logger;
Token tokenActual;
TipoToken tipoActual;
int lineaActual;

List<Token> tokensIDENTIFIER = new ArrayList<>();

PolacaInversa polaca = new PolacaInversa();

//Se usa para ir apilando el valor del acumulador del FOR.
//Es necesario usar una pila, ya que en caso de anidarse varios FOR
//debo tener en el tope el del ultimo FOR
Stack<Token> pilaAcumulador = new Stack<>();

String ambitoFuncionPadre = "";

// Contador y pila para controlar semantica de closures
int numClosuresEnFuncion = 0;
Stack<Integer> pilaClosures = new Stack<>();

int cantFactores = 0;

public void notify(String msg)
{
	System.out.println(msg);
}

public void yyerror(String error)
{
	this.logger.log(new EventoLog(error, "Error", this.analizadorLexico.getLineaActual(), this.analizadorLexico.getPunteroActual()));
}

public void yyerror(String error, int line)
{
	this.logger.log(new EventoLog(error, "Error", line, this.analizadorLexico.getPunteroActual()));
}

public int yylex() throws IOException
{
	this.tokenActual = analizadorLexico.getToken();
	this.lineaActual = analizadorLexico.getLineaActual();
	
	//Se almacena el token actual
	yylval = new ParserVal(tokenActual);

	if (this.tokenActual != null)
	{
		if (this.tokenActual.getCodigo() == -1)
		{
			return 0;
		}

		return toIntExact(this.tokenActual.getCodigo());
	}

	return 0;
}

public Parser(AnalizadorLexico analizadorLexico, TablaDeSimbolos tablaDeSimbolos)
{
	this.analizadorLexico = analizadorLexico;
	this.logger = new Logger();
	this.tablaDeSimbolos = tablaDeSimbolos;
}

public void Run() throws IOException
{
    System.out.println("************************************************");
    System.out.println("Resultados del Analizador Sintáctico:");
    yyparse();
    
    System.out.println();
    System.out.println("************************************************");
    System.out.println("Errores sintácticos encontrados:");
    this.logger.imprimir();
    
    System.out.println();
    System.out.println("************************************************");
    System.out.println("Resultados del Analizador Léxico:");
    System.out.println(analizadorLexico.getTiraTokens());
    System.out.println("Cant. Tokens detectados: " + analizadorLexico.getTiraTokens().size());
    
    System.out.println();
    System.out.println("************************************************");
    System.out.println("Errores léxicos encontrados:");
    this.analizadorLexico.getLogger().imprimir();
    
    System.out.println();
    System.out.println("************************************************");
    System.out.println("Tabla de Simbolos:");
    this.tablaDeSimbolos.imprimirTablaDeSimbolos();
    System.out.println("************************************************");

	System.out.println();
    System.out.println("************************************************");
	polaca.imprimir();
}

/**
* Agrega un token identificador a la lista
*/
public void addIdentifier(Token token){
	tokensIDENTIFIER.add(token);
}

/**
* Configura tipo y uso de identificadores.
* Para funciones se utiliza la misma lista para reuso de la funcionalidad.
*/
public void declararIdentificadores(Token tokenID, UsoToken usoToken) {
	
	//Agrego ultimo identificador reconocido a la lista	
	addIdentifier(tokenID);

	//Setea el tipo reconocido, tipo de uso y ambito a los identificadores declarados.	
	tokensIDENTIFIER.forEach( token -> {
		token.getRegTabSimbolos().setTipoToken(this.tipoActual);
		token.getRegTabSimbolos().setUsoToken(usoToken);
		token.getRegTabSimbolos().setAmbito(Ambito.getAmbitoActual());
		
		Optional<RegTablaSimbolos> reg = tablaDeSimbolos.obtenerDeclaracion(token);
		if (reg.isPresent())
			errorIdentificadorRedeclarado(reg.get().getToken());
		
		else if (TipoToken.FUN.equals(this.tipoActual))
			//Chequeo que se declara en ambito main
			if(!Ambito.getAmbitoActual().isMain())
				yyerror("ERROR: Declaración de función con retorno ("+token.getLexema()+") en un ambito que no es main (" + Ambito.getAmbitoActual() +")", this.lineaActual);
	});
	
	//Restablezco variables
	this.tipoActual = null;
	tokensIDENTIFIER.clear();
}

/**
* Valida flotante para el caso en que haya tenido signo
*/
public void validarFlotante(Token tokenFlotante) {
	
	String lexema = "-" + tokenFlotante.getLexema();
	if (! ASValidarFlotante.validar(lexema)){
		lexema = ASValidarFlotante.truncar(this.analizadorLexico.getLogger(),
											lexema,
											this.analizadorLexico.getLineaActual(),
											this.analizadorLexico.getPunteroActual());
	}
	tokenFlotante.setLexema(lexema);
}

/**
* loguea error de identificador no declarado
*/
public void errorIdentificadorNoDeclarado(Token token){
	yyerror("ERROR: Identificador " + token.getLexema() + " no declarado dentro del alcance del ambito ("+Ambito.getAmbitoActual().getNombre()+")", this.lineaActual);
}

/**
* loguea error de identificador REdeclarado
*/
public void errorIdentificadorRedeclarado(Token token){
	yyerror("ERROR: Redeclaracion del identificador " + token.getLexema() + " (" + token.getRegTabSimbolos().getAmbito().getNombre() + ") en el ambito " + Ambito.getAmbitoActual().getNombre(), this.lineaActual);
}

/**
* Valida si una variable fue declarada antes de su uso.
* En caso de haber sido declarada enlaza el token al registro correspondiente
* en la tabla de simbolos y lo retorna.
* En caso contrario se informa error de identificador no declarado y retorna null.
*/
public RegTablaSimbolos validarDeclaracionIdentificador(Token token){
	Optional<RegTablaSimbolos> regDeclaracion = tablaDeSimbolos.obtenerDeclaracion(token);
	if (regDeclaracion.isPresent()){
		tablaDeSimbolos.enlazarDeclaracion(regDeclaracion.get(), token);
	}	
	else
		errorIdentificadorNoDeclarado(token);
	return regDeclaracion.isPresent() ? regDeclaracion.get() : null;
}

/**
* Realiza acciones necesarias para generar inicio de iteracion para polaca inversa
*/
private void inicializarIteracion(Token identifier, Token assign, Token value){

	pilaAcumulador.push( identifier );
	polaca.addElemento( new ElementoPI( value.getLexema(), value));
	polaca.addElemento( new ElementoPI( identifier.getLexema(), identifier));
	polaca.addElemento( new ElementoPI( assign.getLexema(), assign));
}

