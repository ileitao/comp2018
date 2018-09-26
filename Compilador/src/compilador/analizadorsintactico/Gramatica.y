/*** 1-DECLARATION SECTION ***/
%{
import java.io.IOException;
import compilador.AnalizadorLexico;
import compilador.TablaDeSimbolos;
import compilador.Token;
import compilador.log.Logger;
import compilador.log.EventoLog;
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
		notify("usinteger");
	}
	|
	_SINGLE
	;

/**
 * Lista de variables
 * Las variables se separan con ";"
 */
lista_de_variables:
  asignacion
	|
	lista_de_variables _SEMICOLON asignacion
	;

asignacion:
  _IDENTIFIER _ASSIGN expresion
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
	_ID
	{
		$$ = $1;
	}
	|
	constante
	{
		$$ = $1;
	}

/**
 * Constante
 */
constante :
	_UINT
	{
		this.currentType = SymbolItem.symbolType.UINT;
	}
	|
	_DOUBLE
	{
		this.currentType = SymbolItem.symbolType.DOUBLE;
	}
	;

%%

/*** 4-CODE ***/
AnalizadorLexico analizadorLexico;
TablaDeSimbolos tablaDeSimbolos;
Logger logger;
Token tokenActual;
//SymbolItem.symbolType currentType;
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
	// yylval = new SymbolItem(this.tokenActual);
	return 0;
	//return this.tokenActual.getCode();
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