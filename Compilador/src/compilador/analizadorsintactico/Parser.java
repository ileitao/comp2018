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
import compilador.UsoToken;
import compilador.Token;
import compilador.log.Logger;
import compilador.log.EventoLog;
import static java.lang.Math.toIntExact;
import java.util.ArrayList;
import java.util.List;
import compilador.accionsemantica.ASValidarFlotante;
import compilador.codigointermedio.PolacaInversa;
import compilador.codigointermedio.ElementoPI;
//#line 33 "Parser.java"




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
    9,   10,   10,    3,    3,    3,    3,    3,   11,   18,
   11,   11,   11,   17,   17,   16,   13,   13,   19,   19,
   14,   14,   12,   12,   12,   22,   15,   20,   20,   20,
   23,   23,   23,   24,   24,   24,   24,   24,   21,   21,
   21,   21,   21,   21,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    1,    3,    3,    1,    1,    1,
    1,    3,    3,    8,    7,    8,    7,    8,    1,    2,
    5,    3,    1,    1,    1,    1,    1,    1,    6,    0,
    9,    6,    6,    1,    3,    3,    4,    3,    1,    2,
    5,    5,    6,    6,    6,    9,    4,    3,    3,    1,
    3,    3,    1,    1,    1,    2,    1,    1,    1,    1,
    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    9,   10,    0,    0,    0,    0,    0,    1,
    3,    4,    5,    0,    8,   24,   25,   26,   27,   28,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    2,
    0,    0,    0,    0,    0,   57,   54,   55,    0,    0,
    0,   53,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   58,    0,   38,    0,    0,    7,    0,    0,    6,
    0,   56,    0,    0,    0,   63,   59,   61,   60,   62,
   64,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   37,   47,   13,   12,    0,   34,    0,
    0,    0,    0,    0,    0,   51,   52,   42,   41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   32,
   33,   30,   29,   44,    0,   45,   43,    0,    0,    0,
    0,    0,   40,   35,    0,    0,   20,   17,   15,    0,
    0,    0,    0,    0,    0,    0,   16,   18,   14,   31,
    0,    0,   23,    0,    0,    0,    0,   46,    0,   21,
};
final static short yydgoto[] = {                          9,
  118,   11,   12,   13,   14,   33,   15,  119,  131,  144,
   16,   17,   18,   19,   20,   39,   90,  125,  109,   40,
   72,   47,   41,   42,
};
final static short yysindex[] = {                      -192,
 -270, -258,    0,    0, -254, -230, -228, -175, -192,    0,
    0,    0,    0, -225,    0,    0,    0,    0,    0,    0,
 -249, -253, -224, -248, -220, -176, -164, -246, -157,    0,
 -207, -182, -153, -145, -233,    0,    0,    0, -144,  -91,
 -203,    0, -137, -132, -131, -238, -124, -122, -121, -111,
 -101,    0,  -99,    0, -170, -102,    0,  -92,  -92,    0,
 -169,    0, -251, -243, -243,    0,    0,    0,    0,    0,
    0, -243, -243, -243,  -88,  -87, -169,  -90, -204,  -83,
  -82,  -81,  -80,    0,    0,    0,    0, -168,    0,  -58,
  -57, -213, -203, -203, -190,    0,    0,    0,    0,  -79,
 -173,  -78,  -77, -192, -192, -192, -192, -168,  -75,    0,
    0,    0,    0,    0,  -76,    0,    0, -192,  -74,  -73,
  -55, -245,    0,    0, -169,  -85,    0,    0,    0,  -68,
  -69,  -67,  -66,  -46,  -70, -178,    0,    0,    0,    0,
  -65, -172,    0,  -60,  -64,  -59,  -54,    0, -102,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -53,    0, -103,    0,    0,    0,    0,    0,    0,
 -149,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -107,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -136, -123,  -52,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -51,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -166,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -48,    0,
};
final static short yygindex[] = {                         0,
   14,    0,  -61,    0,    0,   46,    0,  -56,   95,    0,
    0,    0,    0,    0,  193,    0,  -62,    0,  115,  -24,
   98,    0,   44,   47,
};
final static int YYTABLESIZE=231;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         89,
   92,   89,   43,   55,   91,    1,   34,   21,    2,   52,
  132,    5,   52,   10,  100,   89,  103,   89,   35,   22,
  130,   35,   30,   23,   35,   24,  108,   26,   88,   48,
   31,   45,   78,    8,   44,   36,   37,   38,   53,   37,
   38,   36,   37,   38,  112,  113,  108,   95,  120,  121,
  122,  102,    1,   62,   25,    2,   27,   49,    5,   32,
   46,  127,  134,   89,    1,   73,   74,    2,    3,    4,
    5,    6,    7,   58,   57,   88,   64,   65,    1,  143,
    8,    2,    3,    4,    5,    6,    7,    1,    1,   19,
    2,    2,    8,    5,    5,   28,   64,   65,   28,   19,
   59,   50,   29,   86,   87,  146,  142,   93,   94,  115,
   88,   84,   19,   51,   19,    8,    8,   50,   50,   96,
   97,   56,   50,   50,   50,   50,   50,   50,   60,   50,
   48,   48,   50,   61,   63,   48,   48,   48,   48,   48,
   48,   75,   48,   49,   49,   48,   76,   77,   49,   49,
   49,   49,   49,   49,   79,   49,   80,   81,   49,   57,
   57,   57,   57,   58,   58,   58,   58,   82,   58,   58,
   58,   58,   58,   58,   57,   64,   65,   83,   29,   85,
   66,   67,   68,   69,   70,   71,   66,   67,   68,   69,
   70,   71,   32,   98,   99,  101,  104,  105,  106,  107,
  110,  111,  114,  116,  117,  124,  128,  129,  126,  136,
  130,  137,  140,  138,  139,  141,  133,  145,  147,  149,
   54,  148,  123,  135,    0,    0,   36,  150,   11,   39,
   22,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         61,
   63,   63,  256,   28,  256,  257,  256,  278,  260,  256,
  256,  263,  256,    0,   77,   77,   79,   79,  268,  278,
  266,  268,    9,  278,  268,  256,   88,  256,  280,  278,
  256,  256,  271,  285,  288,  285,  286,  287,  285,  286,
  287,  285,  286,  287,  258,  259,  108,   72,  105,  106,
  107,  256,  257,  287,  285,  260,  285,  278,  263,  285,
  285,  118,  125,  125,  257,  269,  270,  260,  261,  262,
  263,  264,  265,  256,  282,  280,  267,  268,  257,  136,
  285,  260,  261,  262,  263,  264,  265,  257,  257,  256,
  260,  260,  285,  263,  263,  271,  267,  268,  271,  266,
  283,  278,  278,   58,   59,  278,  285,   64,   65,  283,
  280,  282,  279,  278,  281,  285,  285,  267,  268,   73,
   74,  279,  272,  273,  274,  275,  276,  277,  282,  279,
  267,  268,  282,  279,  279,  272,  273,  274,  275,  276,
  277,  279,  279,  267,  268,  282,  279,  279,  272,  273,
  274,  275,  276,  277,  279,  279,  279,  279,  282,  267,
  268,  269,  270,  267,  268,  269,  270,  279,  272,  273,
  274,  275,  276,  277,  282,  267,  268,  279,  278,  282,
  272,  273,  274,  275,  276,  277,  272,  273,  274,  275,
  276,  277,  285,  282,  282,  286,  280,  280,  280,  280,
  259,  259,  282,  282,  282,  281,  281,  281,  285,  278,
  266,  281,  259,  281,  281,  286,  122,  283,  279,  279,
   28,  286,  108,  126,   -1,   -1,  279,  282,  282,  281,
  279,
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
"$$1 :",
"seleccion : _IF _LPAREN condicion _RPAREN bloque_de_sentencias _ELSE $$1 bloque_de_sentencias _ENDIF",
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
"factor : _MINUS _CONSTANT_SINGLE",
"factor : _IDENTIFIER",
"factor : error",
"comparador : _LESSER",
"comparador : _GREATER",
"comparador : _LESSER_OR_EQUAL",
"comparador : _GREATER_OR_EQUAL",
"comparador : _EQUAL",
"comparador : _UNEQUAL",
};

//#line 305 "Gramatica.y"

/*** 4-CODE ***/
AnalizadorLexico analizadorLexico;
TablaDeSimbolos tablaDeSimbolos;
Logger logger;
Token tokenActual;
TipoToken tipoActual;
int lineaActual;

List<Token> tokensIDENTIFIER = new ArrayList<>();

PolacaInversa polaca = new PolacaInversa();

public void notify(String msg)
{
	System.out.println(msg);
	//this.syntaxLog.addLog(msg, lexAnalyzer.getLineNumber());
}

public void actualizarTipoID(String msg, int line)
{
	//this.syntaxLog.addLog(msg, line);
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
	//RegTablaSimbolos reg = this.tablaDeSimbolos.getRegistro(this.tokenActual.toString());
	
	//Se almacena el token actual
	yylval = new ParserVal(tokenActual);
	
	//tokenfy(this.tokenActual.toString(), this.tokenActual.getLine());
	//yylval = this.tablaDeSimbolos.createRegTabla(this.tokenActual.toString(), this.tipoToken, lineaToken, posicionToken);
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

public void addIdentifier(Token token){
	tokensIDENTIFIER.add(token);
}

public void setTipoIdentificador(){
	tokensIDENTIFIER.forEach( token -> token.getRegTabSimbolos().setTipoToken(this.tipoActual));
	this.tipoActual = null;
}

public void setUsoIdentificador(UsoToken usoToken) {
	tokensIDENTIFIER.forEach( token -> token.getRegTabSimbolos().setUsoToken(usoToken) );
}

/**
* Configura tipo y uso de identificadores.
* Para funciones se utiliza la misma lista para reuso de la funcionalidad.
*/
public void configurarIdentificadores(Token tokenIdentificador, UsoToken usoToken) {
	addIdentifier(tokenIdentificador);
	setTipoIdentificador();
	setUsoIdentificador(usoToken);
	tokensIDENTIFIER.clear();
}

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
//#line 503 "Parser.java"
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
//#line 77 "Gramatica.y"
{ notify("Sentencia de declaración de variables en línea " + this.lineaActual + ".");
									 configurarIdentificadores((Token)val_peek(1).obj, UsoToken.VARIABLE);
									 }
break;
case 7:
//#line 80 "Gramatica.y"
{ yyerror("ERROR: No se definió ninguna variable en sentencia de declaración de variables", this.lineaActual); }
break;
case 9:
//#line 89 "Gramatica.y"
{ this.tipoActual = TipoToken.USINTEGER; }
break;
case 10:
//#line 90 "Gramatica.y"
{ this.tipoActual = TipoToken.SINGLE;	}
break;
case 12:
//#line 99 "Gramatica.y"
{ addIdentifier( (Token) val_peek(0).obj); }
break;
case 13:
//#line 100 "Gramatica.y"
{ yyerror("ERROR: Falta ; para separar variables en la sentencia de declaración de variables", this.lineaActual); }
break;
case 14:
//#line 115 "Gramatica.y"
{ notify("Sentencia de declaración de función con retorno " + this.lineaActual + ".");
																							  /*Configura tipo y uso de identificadores*/
																							  this.tipoActual = TipoToken.FUN;
																							  configurarIdentificadores((Token) val_peek(6).obj, UsoToken.FUNCION);
																							  }
break;
case 15:
//#line 120 "Gramatica.y"
{ notify("Sentencia de declaración de función sin retorno " + this.lineaActual + ".");
																				this.tipoActual = TipoToken.VOID;
																				configurarIdentificadores((Token)val_peek(5).obj, UsoToken.FUNCION);
																			}
break;
case 16:
//#line 124 "Gramatica.y"
{ yyerror("ERROR: No se definió nombre para la función", this.lineaActual); }
break;
case 17:
//#line 125 "Gramatica.y"
{ yyerror("ERROR: No se definió nombre para la función", this.lineaActual); }
break;
case 18:
//#line 126 "Gramatica.y"
{ yyerror("ERROR: Falta retorno de la función", this.lineaActual); }
break;
case 29:
//#line 172 "Gramatica.y"
{ notify("Sentencia IF sin ELSE en línea " + this.lineaActual + ".");
																polaca.completarPasoIncompleto(0); }
break;
case 30:
//#line 174 "Gramatica.y"
{ polaca.generarElse(); }
break;
case 31:
//#line 174 "Gramatica.y"
{	notify("Sentencia IF con ELSE en línea " + this.lineaActual + "."); polaca.completarPasoIncompleto(0); }
break;
case 32:
//#line 176 "Gramatica.y"
{	yyerror("ERROR: Faltó condición en IF", this.lineaActual);	}
break;
case 33:
//#line 177 "Gramatica.y"
{	yyerror("ERROR: Faltó bloque de sentencias en IF", this.lineaActual);	}
break;
case 36:
//#line 195 "Gramatica.y"
{ polaca.addElemento( new ElementoPI( ((Token)val_peek(1).obj).getLexema(), (Token)val_peek(2).obj)); polaca.generarBifurcacion("BF"); }
break;
case 37:
//#line 203 "Gramatica.y"
{ notify("Sentencia de asignación en línea " + this.lineaActual + ".");
					 polaca.addElemento( new ElementoPI( ((Token)val_peek(3).obj).getLexema(), (Token)val_peek(3).obj));
					 polaca.addElemento( new ElementoPI( ((Token)val_peek(2).obj).getLexema(), (Token)val_peek(2).obj));}
break;
case 38:
//#line 206 "Gramatica.y"
{	notify("Sentencia de asignación en línea " + this.lineaActual + ".");	}
break;
case 41:
//#line 225 "Gramatica.y"
{	notify("Sentencia PRINT en línea " + this.lineaActual + ".");	}
break;
case 42:
//#line 226 "Gramatica.y"
{ yyerror("ERROR: No se especificó ninguna cadena en sentencia PRINT", this.lineaActual); }
break;
case 43:
//#line 234 "Gramatica.y"
{	notify("Sentencia FOR en línea " + this.lineaActual + ".");	}
break;
case 44:
//#line 235 "Gramatica.y"
{	yyerror("ERROR: No se especificó  ninguna condición en sentencia FOR", this.lineaActual);	}
break;
case 45:
//#line 236 "Gramatica.y"
{	yyerror("ERROR: No se especificó  ningún bloque de sentencias en sentencia FOR", this.lineaActual);	}
break;
case 48:
//#line 260 "Gramatica.y"
{ polaca.addElemento( new ElementoPI( ((Token)val_peek(1).obj).getLexema(), (Token)val_peek(1).obj));}
break;
case 49:
//#line 261 "Gramatica.y"
{ polaca.addElemento( new ElementoPI( ((Token)val_peek(1).obj).getLexema(), (Token)val_peek(1).obj));}
break;
case 51:
//#line 270 "Gramatica.y"
{ polaca.addElemento( new ElementoPI( ((Token)val_peek(1).obj).getLexema(), (Token)val_peek(1).obj));}
break;
case 52:
//#line 271 "Gramatica.y"
{ polaca.addElemento( new ElementoPI( ((Token)val_peek(1).obj).getLexema(), (Token)val_peek(1).obj));}
break;
case 54:
//#line 280 "Gramatica.y"
{ polaca.addElemento( new ElementoPI( ((Token)val_peek(0).obj).getLexema(), (Token)val_peek(0).obj));}
break;
case 55:
//#line 281 "Gramatica.y"
{ polaca.addElemento( new ElementoPI( ((Token)val_peek(0).obj).getLexema(), (Token)val_peek(0).obj));}
break;
case 56:
//#line 282 "Gramatica.y"
{ validarFlotante((Token) val_peek(0).obj);
								polaca.addElemento( new ElementoPI( ((Token)val_peek(1).obj).getLexema(), (Token)val_peek(1).obj));}
break;
case 57:
//#line 287 "Gramatica.y"
{ polaca.addElemento( new ElementoPI( ((Token)val_peek(0).obj).getLexema(), (Token)val_peek(0).obj));}
break;
case 58:
//#line 288 "Gramatica.y"
{ yyerror("ERROR: Se esperaba un factor en lugar del token: " + ((Token) val_peek(0).obj).getLexema()); }
break;
//#line 797 "Parser.java"
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
