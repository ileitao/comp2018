//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package compilador.analizadorsintactico;



//#line 3 "Gramatica.y"
import java.io.IOException;
import compilador.AnalizadorLexico;
import compilador.RegTablaSimbolos;
import compilador.TablaDeSimbolos;
import compilador.TipoToken;
import compilador.Token;
import compilador.log.Logger;
import compilador.log.EventoLog;
import static java.lang.Math.toIntExact;
//#line 27 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short _IF=257;
public final static short _ELSE=258;
public final static short _ENDIF=259;
public final static short _PRINT=260;
public final static short _USINTEGER=261;
public final static short _SINGLE=262;
public final static short _FOR=263;
public final static short _VOID=264;
public final static short _FUN=265;
public final static short _RETURN=266;
public final static short _PLUS=267;
public final static short _MINUS=268;
public final static short _MULT=269;
public final static short _DIV=270;
public final static short _ASSIGN=271;
public final static short _EQUAL=272;
public final static short _LESSER=273;
public final static short _LESSER_OR_EQUAL=274;
public final static short _GREATER=275;
public final static short _GREATER_OR_EQUAL=276;
public final static short _UNEQUAL=277;
public final static short _LPAREN=278;
public final static short _RPAREN=279;
public final static short _LCBRACE=280;
public final static short _RCBRACE=281;
public final static short _COMMA=282;
public final static short _SEMICOLON=283;
public final static short _QUOTE=284;
public final static short _IDENTIFIER=285;
public final static short _CONSTANT_UNSIGNED_INTEGER=286;
public final static short _CONSTANT_SINGLE=287;
public final static short _CONSTANT_STRING=288;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    1,    2,    4,    4,    4,    5,    5,
    6,    6,    6,    7,    7,    7,    7,    7,    8,    8,
    9,   10,   10,    3,    3,    3,    3,    3,   11,   11,
   11,   11,   17,   17,   16,   13,   13,   18,   18,   14,
   14,   12,   12,   12,   21,   15,   19,   19,   19,   22,
   22,   22,   23,   23,   23,   23,   20,   20,   20,   20,
   20,   20,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    1,    3,    3,    1,    1,    1,
    1,    3,    3,    8,    7,    8,    7,    8,    1,    2,
    5,    3,    1,    1,    1,    1,    1,    1,    6,    8,
    6,    6,    1,    3,    3,    4,    3,    1,    2,    5,
    5,    6,    6,    6,    9,    4,    3,    3,    1,    3,
    3,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    9,   10,    0,    0,    0,    0,    0,    1,
    3,    4,    5,    0,    8,   24,   25,   26,   27,   28,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    2,
    0,    0,    0,    0,   56,   53,   54,   55,    0,    0,
    0,   52,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   37,    0,    0,    7,    0,    0,    6,    0,
    0,    0,    0,   61,   57,   59,   58,   60,   62,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   36,   46,   13,   12,    0,   33,    0,    0,    0,
    0,    0,    0,   50,   51,   41,   40,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   31,   32,    0,
   29,   43,    0,   44,   42,    0,    0,    0,    0,    0,
   39,   34,    0,    0,   20,   17,   15,    0,    0,    0,
    0,   30,    0,    0,   16,   18,   14,    0,    0,   23,
    0,    0,    0,    0,   45,    0,   21,
};
final static short yydgoto[] = {                          9,
  116,   11,   12,   13,   14,   33,   15,  117,  129,  141,
   16,   17,   18,   19,   20,   39,   88,  107,   40,   70,
   47,   41,   42,
};
final static short yysindex[] = {                      -199,
 -272, -264,    0,    0, -259, -246, -245, -222, -199,    0,
    0,    0,    0, -244,    0,    0,    0,    0,    0,    0,
 -252, -251, -243, -253, -247, -223, -198, -189, -192,    0,
 -238, -235, -182, -186,    0,    0,    0,    0, -153, -119,
 -180,    0, -140, -129, -113, -100, -107, -106, -105, -104,
 -103, -101,    0, -239, -102,    0,  -99,  -99,    0, -179,
 -233, -118, -118,    0,    0,    0,    0,    0,    0, -118,
 -118, -118,  -98,  -97, -179, -108, -203,  -93,  -92,  -91,
  -90,    0,    0,    0,    0, -172,    0,  -80,  -78, -154,
 -180, -180, -149,    0,    0,    0,    0,  -89,  -88,  -86,
  -85, -199, -199, -199, -199, -172,  -87,    0,    0, -179,
    0,    0,  -94,    0,    0, -199,  -83,  -82,  -84, -234,
    0,    0,  -76, -112,    0,    0,    0,  -77,  -81,  -79,
  -75,    0,  -74, -190,    0,    0,    0,  -73, -195,    0,
  -72,  -71,  -70,  -69,    0, -102,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -68,    0,    0,    0,    0,    0,    0,    0,    0,
 -165,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -123,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -152, -139,  -63,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -64,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -187,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -61,    0,
};
final static short yygindex[] = {                         0,
   59,    0,  -60,    0,    0,   74,    0,  -96,   72,    0,
    0,    0,    0,    0,  175,    0,  -59,   98,  -25,   81,
    0,   79,   80,
};
final static int YYTABLESIZE=218;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         87,
   87,   90,   54,   34,   43,   21,  118,  119,  120,   24,
   26,   31,   45,   22,   87,   98,   87,  101,   23,  125,
   57,  130,   89,    1,   48,  106,    2,   62,   63,    5,
   49,  128,   35,   36,   37,   38,   44,  140,   25,   27,
   32,   46,   82,   56,   93,  106,   86,   58,   28,   87,
  123,    8,  100,    1,   50,   29,    2,    1,   10,    5,
    2,    3,    4,    5,    6,    7,    1,   30,   19,    2,
    3,    4,    5,    6,    7,   28,   86,    1,   19,   51,
    2,    8,  143,    5,    1,    8,   55,    2,   71,   72,
    5,   19,   60,   19,  139,   52,   36,   37,   38,   59,
   86,   49,   49,  110,  111,    8,   49,   49,   49,   49,
   49,   49,    8,   49,   47,   47,   49,   62,   63,   47,
   47,   47,   47,   47,   47,   61,   47,   48,   48,   47,
   84,   85,   48,   48,   48,   48,   48,   48,   73,   48,
   91,   92,   48,   56,   56,   56,   56,   62,   63,   74,
   94,   95,   64,   65,   66,   67,   68,   69,   56,   64,
   65,   66,   67,   68,   69,   75,   35,   36,   37,   38,
   76,   77,   78,   79,   80,   81,   29,   99,  108,   83,
  109,  128,  132,   96,   97,   32,  102,  103,  104,  105,
  124,  131,  112,  122,  113,  114,  115,  126,  127,  135,
  134,  136,   53,  121,  133,  137,  144,    0,  146,  142,
    0,  138,  147,   11,  145,   35,   38,   22,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         60,
   61,   61,   28,  256,  256,  278,  103,  104,  105,  256,
  256,  256,  256,  278,   75,   75,   77,   77,  278,  116,
  256,  256,  256,  257,  278,   86,  260,  267,  268,  263,
  278,  266,  285,  286,  287,  288,  288,  134,  285,  285,
  285,  285,  282,  282,   70,  106,  280,  283,  271,  110,
  110,  285,  256,  257,  278,  278,  260,  257,    0,  263,
  260,  261,  262,  263,  264,  265,  257,    9,  256,  260,
  261,  262,  263,  264,  265,  271,  280,  257,  266,  278,
  260,  285,  278,  263,  257,  285,  279,  260,  269,  270,
  263,  279,  279,  281,  285,  285,  286,  287,  288,  282,
  280,  267,  268,  258,  259,  285,  272,  273,  274,  275,
  276,  277,  285,  279,  267,  268,  282,  267,  268,  272,
  273,  274,  275,  276,  277,  279,  279,  267,  268,  282,
   57,   58,  272,  273,  274,  275,  276,  277,  279,  279,
   62,   63,  282,  267,  268,  269,  270,  267,  268,  279,
   71,   72,  272,  273,  274,  275,  276,  277,  282,  272,
  273,  274,  275,  276,  277,  279,  285,  286,  287,  288,
  271,  279,  279,  279,  279,  279,  278,  286,  259,  282,
  259,  266,  259,  282,  282,  285,  280,  280,  280,  280,
  285,  120,  282,  281,  283,  282,  282,  281,  281,  281,
  278,  281,   28,  106,  124,  281,  279,   -1,  279,  283,
   -1,  286,  282,  282,  286,  279,  281,  279,
};
}
final static short YYFINAL=9;
final static short YYMAXTOKEN=288;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"_IF","_ELSE","_ENDIF","_PRINT","_USINTEGER","_SINGLE","_FOR",
"_VOID","_FUN","_RETURN","_PLUS","_MINUS","_MULT","_DIV","_ASSIGN","_EQUAL",
"_LESSER","_LESSER_OR_EQUAL","_GREATER","_GREATER_OR_EQUAL","_UNEQUAL",
"_LPAREN","_RPAREN","_LCBRACE","_RCBRACE","_COMMA","_SEMICOLON","_QUOTE",
"_IDENTIFIER","_CONSTANT_UNSIGNED_INTEGER","_CONSTANT_SINGLE",
"_CONSTANT_STRING",
};
final static String yyrule[] = {
"$accept : programa",
"programa : sentencia",
"programa : programa sentencia",
"sentencia : bloque_declarativo",
"sentencia : bloque_ejecutable",
"bloque_declarativo : sentencias_de_declaracion_de_variables",
"sentencias_de_declaracion_de_variables : tipo lista_de_variables _COMMA",
"sentencias_de_declaracion_de_variables : tipo error _COMMA",
"sentencias_de_declaracion_de_variables : declaracion_de_funcion",
"tipo : _USINTEGER",
"tipo : _SINGLE",
"lista_de_variables : _IDENTIFIER",
"lista_de_variables : _IDENTIFIER _SEMICOLON lista_de_variables",
"lista_de_variables : _IDENTIFIER error lista_de_variables",
"declaracion_de_funcion : _FUN _IDENTIFIER _LPAREN _RPAREN _LCBRACE cuerpo_de_funcion retorno_de_funcion _RCBRACE",
"declaracion_de_funcion : _VOID _IDENTIFIER _LPAREN _RPAREN _LCBRACE cuerpo_de_funcion _RCBRACE",
"declaracion_de_funcion : _FUN error _LPAREN _RPAREN _LCBRACE cuerpo_de_funcion retorno_de_funcion _RCBRACE",
"declaracion_de_funcion : _VOID error _LPAREN _RPAREN _LCBRACE cuerpo_de_funcion _RCBRACE",
"declaracion_de_funcion : _FUN _IDENTIFIER _LPAREN _RPAREN _LCBRACE cuerpo_de_funcion error _RCBRACE",
"cuerpo_de_funcion : sentencia",
"cuerpo_de_funcion : sentencia cuerpo_de_funcion",
"retorno_de_funcion : _RETURN _LPAREN retorno _RPAREN _COMMA",
"retorno : _IDENTIFIER _LPAREN _RPAREN",
"retorno : cuerpo_de_funcion",
"bloque_ejecutable : seleccion",
"bloque_ejecutable : iteracion",
"bloque_ejecutable : asignacion",
"bloque_ejecutable : impresion",
"bloque_ejecutable : invocacion_de_funcion",
"seleccion : _IF _LPAREN condicion _RPAREN bloque_de_sentencias _ENDIF",
"seleccion : _IF _LPAREN condicion _RPAREN bloque_de_sentencias _ELSE bloque_de_sentencias _ENDIF",
"seleccion : _IF _LPAREN error _RPAREN bloque_de_sentencias _ENDIF",
"seleccion : _IF _LPAREN condicion _RPAREN error _ENDIF",
"bloque_de_sentencias : bloque_ejecutable",
"bloque_de_sentencias : _LCBRACE asignacion_compuesta _RCBRACE",
"condicion : expresion comparador expresion",
"asignacion : _IDENTIFIER _ASSIGN expresion _COMMA",
"asignacion : _IDENTIFIER _ASSIGN invocacion_de_funcion",
"asignacion_compuesta : bloque_ejecutable",
"asignacion_compuesta : bloque_ejecutable asignacion_compuesta",
"impresion : _PRINT _LPAREN _CONSTANT_STRING _RPAREN _COMMA",
"impresion : _PRINT _LPAREN error _RPAREN _COMMA",
"iteracion : _FOR _LPAREN condiciones_de_iteracion _RPAREN bloque_de_sentencias _COMMA",
"iteracion : _FOR _LPAREN error _RPAREN bloque_de_sentencias _COMMA",
"iteracion : _FOR _LPAREN condiciones_de_iteracion _RPAREN error _COMMA",
"condiciones_de_iteracion : _IDENTIFIER _ASSIGN _CONSTANT_UNSIGNED_INTEGER _SEMICOLON _IDENTIFIER comparador _CONSTANT_UNSIGNED_INTEGER _SEMICOLON _CONSTANT_UNSIGNED_INTEGER",
"invocacion_de_funcion : _IDENTIFIER _LPAREN _RPAREN _COMMA",
"expresion : expresion _PLUS termino",
"expresion : expresion _MINUS termino",
"expresion : termino",
"termino : termino _MULT factor",
"termino : termino _DIV factor",
"termino : factor",
"factor : _CONSTANT_UNSIGNED_INTEGER",
"factor : _CONSTANT_SINGLE",
"factor : _CONSTANT_STRING",
"factor : _IDENTIFIER",
"comparador : _LESSER",
"comparador : _GREATER",
"comparador : _LESSER_OR_EQUAL",
"comparador : _GREATER_OR_EQUAL",
"comparador : _EQUAL",
"comparador : _UNEQUAL",
};

//#line 281 "Gramatica.y"

/*** 4-CODE ***/
AnalizadorLexico analizadorLexico;
TablaDeSimbolos tablaDeSimbolos;
Logger logger;
Token tokenActual;
TipoToken tipoActual;
int lineaActual;

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
	this.lineaActual = analizadorLexico.getLineaActual();
	//RegTablaSimbolos reg = this.tablaDeSimbolos.getRegistro(this.tokenActual.toString());
	//yylval = reg.getTipo();
	//tokenfy(this.tokenActual.toString(), this.tokenActual.getLine());
	//yylval = this.tablaDeSimbolos.createRegTabla(this.tokenActual.toString(), this.tipoToken, lineaToken, posicionToken);
	if (this.tokenActual != null)
	{
		if (this.tokenActual.getId() == -1)
		{
			return 0;
		}

		return toIntExact(this.tokenActual.getId());
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
    System.out.println("************************************************");
    System.out.println("Errores sintácticos encontrados:");
    this.logger.imprimir();
    System.out.println("************************************************");
    System.out.println("Resultados del Analizador Léxico:");
    System.out.println(analizadorLexico.getTiraTokens());
    System.out.println("Cant. Tokens detectados: " + analizadorLexico.getTiraTokens().size());
    System.out.println("************************************************");
    System.out.println("Tabla de Simbolos:");
    this.tablaDeSimbolos.imprimirTablaDeSimbolos();
    System.out.println("************************************************");
}
//#line 438 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
throws IOException
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    //if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      //if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        //if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          //if (yydebug)
          //  yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        //if (yydebug)
          //debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      //if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            //if (yydebug)
              //debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            //if (yydebug)
              //debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        //if (yydebug)
          //{
          //yys = null;
          //if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          //if (yys == null) yys = "illegal-symbol";
          //debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          //}
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    //if (yydebug)
      //debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 6:
//#line 71 "Gramatica.y"
{ notify("Sentencia de declaración de variables en línea " + this.lineaActual + "."); }
break;
case 7:
//#line 72 "Gramatica.y"
{ yyerror("ERROR: No se definió ninguna variable en sentencia de declaración de variables", this.lineaActual); }
break;
case 9:
//#line 81 "Gramatica.y"
{ /*this.tipoActual = TipoToken.CONSTANTE_ENTERO_SIN_SIGNO;*/ }
break;
case 10:
//#line 82 "Gramatica.y"
{ /*this.tipoActual = TipoToken.CONSTANTE_FLOTANTE;*/	}
break;
case 13:
//#line 92 "Gramatica.y"
{ yyerror("ERROR: Falta ; para separar variables en la sentencia de declaración de variables", this.lineaActual); }
break;
case 14:
//#line 107 "Gramatica.y"
{ notify("Sentencia de declaración de función con retorno " + this.lineaActual + "."); }
break;
case 15:
//#line 108 "Gramatica.y"
{ notify("Sentencia de declaración de función sin retorno " + this.lineaActual + "."); }
break;
case 16:
//#line 109 "Gramatica.y"
{ yyerror("ERROR: No se definió nombre para la función", this.lineaActual); }
break;
case 17:
//#line 110 "Gramatica.y"
{ yyerror("ERROR: No se definió nombre para la función", this.lineaActual); }
break;
case 18:
//#line 111 "Gramatica.y"
{ yyerror("ERROR: Falta retorno de la función", this.lineaActual); }
break;
case 29:
//#line 157 "Gramatica.y"
{	notify("Sentencia IF en línea " + this.lineaActual + ".");	}
break;
case 30:
//#line 158 "Gramatica.y"
{	notify("Sentencia IF en línea " + this.lineaActual + ".");	}
break;
case 31:
//#line 159 "Gramatica.y"
{	yyerror("ERROR: Faltó condición en IF", this.lineaActual);	}
break;
case 32:
//#line 160 "Gramatica.y"
{	yyerror("ERROR: Faltó bloque de sentencias en IF", this.lineaActual);	}
break;
case 36:
//#line 186 "Gramatica.y"
{	notify("Sentencia de asignación en línea " + this.lineaActual + ".");	}
break;
case 37:
//#line 187 "Gramatica.y"
{	notify("Sentencia de asignación en línea " + this.lineaActual + ".");	}
break;
case 40:
//#line 206 "Gramatica.y"
{	notify("Sentencia PRINT en línea " + this.lineaActual + ".");	}
break;
case 41:
//#line 207 "Gramatica.y"
{ yyerror("ERROR: No se especificó ninguna cadena en sentencia PRINT", this.lineaActual); }
break;
case 42:
//#line 215 "Gramatica.y"
{	notify("Sentencia FOR en línea " + this.lineaActual + ".");	}
break;
case 43:
//#line 216 "Gramatica.y"
{	yyerror("ERROR: No se especificó  ninguna condición en sentencia FOR", this.lineaActual);	}
break;
case 44:
//#line 217 "Gramatica.y"
{	yyerror("ERROR: No se especificó  ningún bloque de condiciones en sentencia FOR", this.lineaActual);	}
break;
//#line 671 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    //if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      //if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        //if (yydebug)
          //yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      //if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
