/*** 1-DECLARATION SECTION ***/
%{
import java.io.IOException;
import compilador.AnalizadorLexico;
import compilador.RegTablaSimbolos;
import compilador.TablaDeSimbolos;
import compilador.TipoToken;
import compilador.Token;
import compilador.log.Logger;
import compilador.log.EventoLog;
import static java.lang.Math.toIntExact;
%}

/*** 2-YACC DECLARATIONS ***/

/* RESERVED KEYS [if else endif print usinteger single for void fun return ]*/
%token _IF _ELSE _ENDIF _PRINT _USINTEGER _SINGLE _FOR _VOID _FUN _RETURN _IDENTIFIER

/* ARITHMETIC OPERATORS [ + - * / ] */
%token _PLUS _MINUS _MULT _DIV

/* ASIGNATION OPERATORS [ := ] */
%token _ASSIGN

/* COMPARATORS [ = < <= > >= != ] */
%token _EQUAL _LESSER _LESSER_OR_EQUAL _GREATER _GREATER_OR_EQUAL _UNEQUAL

/* OTHERS [ ( ) { } , ; ' ] */
%token _LPAREN _RPAREN _LCBRACE _RCBRACE _COMMA _SEMICOLON _QUOTE

%right _PLUS _MINUS
%right _MULT _DIV
%right _ELSE

%start programa
%%

/*** 3-GRAMMAR FOLLOWS ***/

programa :
	sentencias_de_declaracion_de_variables
	{
		notify("programa válido");
	}
	;

/**
 * Sentencias de declaracion de variables
 * <tipo> <lista_de_variables> ","
 */
sentencias_de_declaracion_de_variables :
	tipo lista_de_variables _COMMA
	;

/**
 * Tipo
 * Tipos _USINTEGER Y _SINGLE
 */
tipo :
	_USINTEGER
	{
		this.tipoActual = TipoToken.CONSTANTE_ENTERO_SIN_SIGNO;
		notify("usinteger");

	}
	|
	_SINGLE
	{
		this.tipoActual = TipoToken.CONSTANTE_FLOTANTE;
		notify("single");
	}
	;

/**
 * Lista de variables
 * Las variables se separan con ";"
 */
lista_de_variables:
  _IDENTIFIER
  {
		notify("asignacion");
		this.tipoActual = TipoToken.IDENTIFICADOR;
		RegTablaSimbolos reg = this.tablaDeSimbolos.getRegistro($1.toString());
		if (reg == null) {
			reg = this.tablaDeSimbolos.createRegTabla($1.toString(), this.tipoActual, this.analizadorLexico.getLineaActual(), this.analizadorLexico.getPunteroActual());
			this.tablaDeSimbolos.agregarSimbolo(reg);
		}
	}
	|
	lista_de_variables _SEMICOLON _IDENTIFIER
	{
		notify("asignacion");
		this.tipoActual = TipoToken.IDENTIFICADOR;
		RegTablaSimbolos reg = this.tablaDeSimbolos.getRegistro($3.toString());
		if (reg == null) {
			reg = this.tablaDeSimbolos.createRegTabla($3.toString(), this.tipoActual, this.analizadorLexico.getLineaActual(), this.analizadorLexico.getPunteroActual());
			this.tablaDeSimbolos.agregarSimbolo(reg);
		}
	}
	;

asignacion:
  _IDENTIFIER _ASSIGN expresion
  {
		notify($1.toString());
		notify($2.toString());
		notify($3.toString());
	}
 ;

/**
 * Expresión
 * Aritmética, variable o constante
 */
expresion :
	expresion _PLUS termino
	|
	expresion _MINUS termino
	|
	termino
	;

/**
 * Término
 * Aritmética, variable o constante
 */
termino :
	termino _MULT factor
	|
	termino _DIV factor
	|
	factor
	;

/**
 * Factor
 * Aritmética, variable o constante
 */
factor :
	constante
	{
		$$ = $1;
		notify($1.toString());
	}

/**
 * Constante
 */
constante :
	_USINTEGER
	|
	_SINGLE
	;

%%

/*** 4-CODE ***/
AnalizadorLexico analizadorLexico;
TablaDeSimbolos tablaDeSimbolos;
Logger logger;
Token tokenActual;
TipoToken tipoActual;
//int currentLine;

public void notify(String msg)
{
	System.out.println(msg);
	//this.syntaxLog.addLog(msg, lexAnalyzer.getLineNumber());
}

public void notify(String msg, int line)
{
	//this.syntaxLog.addLog(msg, line);
}

public void tokenfy(String msg, int line)
{
	//this.tokensLog.addLog(msg, line);
}

public void yyerror(String error)
{
	this.logger.log(new EventoLog(error, "Error", this.analizadorLexico.getLineaActual()));
}

public void yyerror(String error, int line)
{
	this.logger.log(new EventoLog(error, "Error", line));
}

public int yylex() throws IOException
{
	this.tokenActual = analizadorLexico.getToken();
	//tokenfy(this.tokenActual.toString(), this.tokenActual.getLine());
	//yylval = this.tablaDeSimbolos.createRegTabla(this.tokenActual.toString(), this.tipoToken, lineaToken, posicionToken);
	if (this.tokenActual.getId() == -1)
	{
		return 0;
	}

	return toIntExact(this.tokenActual.getId());
}

public Parser(AnalizadorLexico analizadorLexico, TablaDeSimbolos tablaDeSimbolos)
{
	this.analizadorLexico = analizadorLexico;
	this.logger = new Logger();
	this.tablaDeSimbolos = tablaDeSimbolos;
}

public void Run() throws IOException
{
  yyparse();
}