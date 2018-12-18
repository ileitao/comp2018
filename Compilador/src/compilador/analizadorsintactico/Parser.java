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
//#line 36 "Parser.java"




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
    0,    0,    1,    1,    2,    4,    4,    4,    4,    5,
    5,    6,    6,    6,    8,    8,    9,    9,    7,    7,
    7,   10,   10,   11,   12,   12,    3,    3,    3,    3,
    3,   13,   20,   13,   13,   13,   19,   19,   18,   15,
   15,   21,   21,   16,   16,   24,   14,   14,   25,   26,
   26,   27,   27,   27,   28,   28,   29,   17,   22,   22,
   22,   30,   30,   30,   31,   31,   31,   31,   31,   23,
   23,   23,   23,   23,   23,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    1,    3,    3,    1,    3,    1,
    1,    1,    3,    3,    2,    2,    2,    2,    7,    6,
    7,    1,    2,    5,    3,    1,    1,    1,    1,    1,
    1,    6,    0,    9,    6,    6,    1,    3,    3,    4,
    3,    1,    2,    5,    5,    1,    6,    6,    3,    4,
    4,    4,    4,    4,    1,    1,    1,    4,    3,    3,
    1,    3,    3,    1,    1,    1,    2,    1,    1,    1,
    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,   10,   11,   46,    0,    0,    0,    0,    1,
    3,    4,    5,    0,    8,    0,    0,   27,   28,   29,
   30,   31,    0,    0,    0,   18,   17,   16,    0,    0,
    0,    0,    2,    0,    0,    0,    0,    0,    0,    0,
    0,   68,   65,   66,    0,    0,    0,   64,    0,    0,
    0,    0,    9,   69,    0,   41,    0,    0,    7,    6,
    0,    0,    0,    0,    0,    0,   67,    0,    0,    0,
   74,   70,   72,   71,   73,   75,    0,    0,    0,    0,
    0,   14,   13,   40,   58,    0,    0,    0,    0,    0,
   57,    0,    0,    0,   37,    0,    0,    0,    0,    0,
    0,   62,   63,   45,   44,    0,    0,    0,    0,    0,
    0,    0,    0,   56,   55,   49,    0,    0,    0,    0,
   35,   36,   33,   32,   23,    0,    0,    0,   20,   51,
   50,   48,   47,    0,    0,    0,   43,   38,    0,   21,
    0,   19,   53,   54,   52,    0,    0,   26,    0,   34,
    0,    0,    0,   24,
};
final static short yydgoto[] = {                          9,
  106,   11,   12,   13,   14,   30,   15,   16,   17,  107,
  128,  149,   18,   19,   20,   21,   22,   45,   96,  139,
  120,   46,   77,   23,   64,   65,   92,  116,   93,   47,
   48,
};
final static short yysindex[] = {                      -185,
 -262, -225,    0,    0,    0, -249, -221, -259, -185,    0,
    0,    0,    0, -201,    0, -220, -155,    0,    0,    0,
    0,    0, -153, -247, -251,    0,    0,    0, -175, -172,
 -242, -196,    0, -146, -175, -138, -142, -129, -128, -116,
 -117,    0,    0,    0,  -89,  -60, -237,    0,  -86,  -85,
  -96,  -96,    0,    0,  -78,    0,  -87,  -73,    0,    0,
  -88,  -70,  -53,  -68, -197, -159,    0, -229, -238, -238,
    0,    0,    0,    0,    0,    0, -238, -238, -238,  -63,
  -62,    0,    0,    0,    0, -185, -185, -236, -195,  -90,
    0, -234, -145, -194,    0,  -38,  -37, -188, -237, -237,
 -161,    0,    0,    0,    0, -185, -199,  -58,  -59,  -57,
  -55,  -54, -238,    0,    0,    0, -238, -238, -194,  -56,
    0,    0,    0,    0,    0,  -52,  -48,  -50,    0,    0,
    0,    0,    0, -165, -148,  -95,    0,    0, -159,    0,
 -168,    0,    0,    0,    0,  -27, -166,    0,  -46,    0,
  -45,  -47,  -73,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -267,    0,
    0,    0,    0,    0,  -44,    0,    0,    0,    0,  -71,
    0,    0,    0,    0,    0,    0, -134,    0,    0,    0,
    0,    0,    0,    0,  -91,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -121, -108,
  -43,    0,    0,    0,    0, -157,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -42,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -39,    0,
};
final static short yygindex[] = {                         0,
    8,    0,  -65,    0,    0,  -10,    0,    0,    0,  -81,
    0,    0,    0,    0,    0,    0,  206,    0,  -66,    0,
  122,  -31,  -80,    0,    0,    0,    0,    0,    0,   44,
   37,
};
final static int YYTABLESIZE=241;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         57,
   95,   98,   95,   36,   49,  108,   26,   10,   40,  113,
   15,   31,  118,   54,   12,   24,   33,   54,   32,  109,
   41,  114,  112,   95,  125,   41,   97,    1,  119,   41,
    2,   78,   79,    5,   28,   27,   50,   42,   43,   44,
   82,   83,   55,   43,   44,  101,   42,   43,   44,  110,
   94,  115,   25,  119,   34,    8,  126,   37,   90,  148,
  111,    1,    1,   29,    2,    2,  127,    5,    5,  123,
  124,    1,  146,   95,    2,    3,    4,    5,    6,    7,
   51,  134,   58,   35,   94,  135,  136,   91,    1,    8,
    8,    2,    3,    4,    5,    6,    7,    1,   22,    8,
    2,   69,   70,    5,   31,   69,   70,   52,   22,   53,
  117,  151,   99,  100,  102,  103,  147,  143,   69,   70,
   94,   22,   38,   22,   39,    8,   71,   72,   73,   74,
   75,   76,   61,   61,  144,   59,   61,   61,   61,   61,
   61,   61,   61,   60,   61,   59,   59,   61,   61,   62,
   59,   59,   59,   59,   59,   59,   63,   59,   60,   60,
   59,   59,   66,   60,   60,   60,   60,   60,   60,   67,
   60,   69,   70,   60,   60,   68,   68,   68,   68,   69,
   70,   71,   72,   73,   74,   75,   76,  145,   35,   68,
   68,   86,   80,   81,   84,   69,   69,   69,   69,   32,
   69,   69,   69,   69,   69,   69,   69,   70,   85,   87,
   89,   71,   72,   73,   74,   75,   76,   88,  104,  105,
  121,  122,  129,  130,  138,  131,  132,  133,  140,  141,
  142,  150,  152,  153,  154,   39,   56,   12,   42,   25,
  137,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         31,
   66,   68,   68,   14,  256,   87,  256,    0,  256,   90,
  278,  271,   93,  256,  282,  278,    9,  256,  278,  256,
  268,  256,   89,   89,  106,  268,  256,  257,   94,  268,
  260,  269,  270,  263,  256,  285,  288,  285,  286,  287,
   51,   52,  285,  286,  287,   77,  285,  286,  287,  286,
  280,  286,  278,  119,  256,  285,  256,  278,  256,  141,
  256,  257,  257,  285,  260,  260,  266,  263,  263,  258,
  259,  257,  139,  139,  260,  261,  262,  263,  264,  265,
  256,  113,  279,  285,  280,  117,  118,  285,  257,  285,
  285,  260,  261,  262,  263,  264,  265,  257,  256,  285,
  260,  267,  268,  263,  271,  267,  268,  283,  266,  282,
  256,  278,   69,   70,   78,   79,  285,  283,  267,  268,
  280,  279,  278,  281,  278,  285,  272,  273,  274,  275,
  276,  277,  267,  268,  283,  282,  279,  272,  273,  274,
  275,  276,  277,  282,  279,  267,  268,  282,  283,  279,
  272,  273,  274,  275,  276,  277,  285,  279,  267,  268,
  282,  283,  279,  272,  273,  274,  275,  276,  277,  287,
  279,  267,  268,  282,  283,  267,  268,  269,  270,  267,
  268,  272,  273,  274,  275,  276,  277,  283,  285,  279,
  282,  280,  279,  279,  282,  267,  268,  269,  270,  278,
  272,  273,  274,  275,  276,  277,  267,  268,  282,  280,
  279,  272,  273,  274,  275,  276,  277,  271,  282,  282,
  259,  259,  281,  283,  281,  283,  282,  282,  281,  278,
  281,  259,  279,  279,  282,  279,   31,  282,  281,  279,
  119,
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
"sentencias_de_declaracion_de_variables : _FUN lista_de_variables _COMMA",
"tipo : _USINTEGER",
"tipo : _SINGLE",
"lista_de_variables : _IDENTIFIER",
"lista_de_variables : _IDENTIFIER _SEMICOLON lista_de_variables",
"lista_de_variables : _IDENTIFIER error lista_de_variables",
"inicio_funcion : _FUN _IDENTIFIER",
"inicio_funcion : _FUN error",
"inicio_closure : _VOID _IDENTIFIER",
"inicio_closure : _VOID error",
"declaracion_de_funcion : inicio_funcion _LPAREN _RPAREN _LCBRACE cuerpo_de_funcion retorno_de_funcion _RCBRACE",
"declaracion_de_funcion : inicio_closure _LPAREN _RPAREN _LCBRACE cuerpo_de_funcion _RCBRACE",
"declaracion_de_funcion : inicio_funcion _LPAREN _RPAREN _LCBRACE cuerpo_de_funcion error _RCBRACE",
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
"inicio_iteracion : _FOR",
"iteracion : inicio_iteracion _LPAREN condiciones_de_iteracion _RPAREN bloque_de_sentencias _COMMA",
"iteracion : inicio_iteracion _LPAREN condiciones_de_iteracion _RPAREN error _COMMA",
"condiciones_de_iteracion : inicializacion_iteracion condicion_iteracion incremento_iteracion",
"inicializacion_iteracion : _IDENTIFIER _ASSIGN _CONSTANT_UNSIGNED_INTEGER _SEMICOLON",
"inicializacion_iteracion : _IDENTIFIER _ASSIGN error _SEMICOLON",
"condicion_iteracion : condicion_iteracion_inicio comparador expresion _SEMICOLON",
"condicion_iteracion : error comparador expresion _SEMICOLON",
"condicion_iteracion : condicion_iteracion_inicio error expresion _SEMICOLON",
"incremento_iteracion : _CONSTANT_UNSIGNED_INTEGER",
"incremento_iteracion : error",
"condicion_iteracion_inicio : _IDENTIFIER",
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

//#line 498 "Gramatica.y"

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

//#line 588 "Parser.java"
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
//#line 80 "Gramatica.y"
{ notify("Sentencia de declaración de variables en línea " + this.lineaActual + ".");
									 declararIdentificadores((Token)val_peek(1).obj, UsoToken.VARIABLE);
									 }
break;
case 7:
//#line 83 "Gramatica.y"
{ yyerror("ERROR: No se definió ninguna variable en sentencia de declaración de variables", this.lineaActual); }
break;
case 9:
//#line 87 "Gramatica.y"
{
		this.tipoActual = TipoToken.FUN;
		declararIdentificadores((Token)val_peek(1).obj, UsoToken.VARIABLE);
		}
break;
case 10:
//#line 98 "Gramatica.y"
{ this.tipoActual = TipoToken.USINTEGER; }
break;
case 11:
//#line 99 "Gramatica.y"
{ this.tipoActual = TipoToken.SINGLE;	}
break;
case 13:
//#line 108 "Gramatica.y"
{ addIdentifier( (Token) val_peek(0).obj ); }
break;
case 14:
//#line 109 "Gramatica.y"
{ yyerror("ERROR: Falta ; para separar variables en la sentencia de declaración de variables", this.lineaActual); }
break;
case 15:
//#line 113 "Gramatica.y"
{
		this.tipoActual = TipoToken.FUN;
		
		/*Configura tipo y uso de identificador*/
		declararIdentificadores((Token) val_peek(0).obj, UsoToken.FUNCION);

		/*Apilo y obtengo nuevo ambito con el nombre de la funcion actual*/
		Ambito.nuevoAmbito( (Token) val_peek(1).obj, (Token) val_peek(0).obj );

		/*apilo contador de closures para poder controlar cantidad en caso de que anide funciones*/
		/*(lo cual seria un error, pero aun asi necesito llevar el control)*/
		pilaClosures.push(this.numClosuresEnFuncion);
		this.numClosuresEnFuncion = 0;
	}
break;
case 16:
//#line 127 "Gramatica.y"
{ yyerror("ERROR: No se definió nombre para la función", this.lineaActual); }
break;
case 17:
//#line 131 "Gramatica.y"
{
		this.tipoActual = TipoToken.VOID;
		/*Incremento para control de closures declarados en funcion*/
		this.numClosuresEnFuncion++;

		if (this.numClosuresEnFuncion > 1)
			yyerror("ERROR: Solo se permite la declaracion de 1 unica funcion sin retorno dentro de una funcion FUN (" + Ambito.getAmbitoActual() +")", this.lineaActual);

		/*Pido el token que genero el ambito actual para ver si es tipo FUN*/
		Token tokenAmbito = Ambito.getAmbitoActual().getTokenIdentificador();
		
		/*Con esto se controla que la funcion sin retorno solo sea declarada dentro del ambito de una funcion con retorno*/
		if (Ambito.getAmbitoActual().isMain() || !TipoToken.FUN.equals(tokenAmbito.getRegTabSimbolos().getTipoToken()) )
			yyerror("ERROR: Declaración de función sin retorno ("+((Token) val_peek(0).obj).getLexema()+") en un ambito que no es una funcion FUN (" + Ambito.getAmbitoActual() +")", this.lineaActual);

		/*Configura tipo y uso de identificadores*/
		declararIdentificadores((Token) val_peek(0).obj, UsoToken.FUNCION);
	
		/*Apilo y obtengo nuevo ambito con el nombre del closure actual*/
		Ambito.nuevoAmbito( (Token) val_peek(1).obj, (Token) val_peek(0).obj );
	}
break;
case 18:
//#line 152 "Gramatica.y"
{ yyerror("ERROR: No se definió nombre para la función", this.lineaActual); }
break;
case 19:
//#line 168 "Gramatica.y"
{	
			notify("Sentencia de declaración de función con retorno " + this.lineaActual + ".");

			/* Restauro ambito anterior*/
			Ambito.finalizarAmbito();
			this.numClosuresEnFuncion = pilaClosures.pop();
		}
break;
case 20:
//#line 176 "Gramatica.y"
{	notify("Sentencia de declaración de función sin retorno " + this.lineaActual + ".");

			/* Restauro ambito anterior*/
			Ambito.finalizarAmbito();
		}
break;
case 21:
//#line 181 "Gramatica.y"
{ yyerror("ERROR: Falta retorno de la función", this.lineaActual); }
break;
case 25:
//#line 207 "Gramatica.y"
{
		Token tokenIdentificador = (Token)val_peek(2).obj;
		validarDeclaracionIdentificador(tokenIdentificador);
		/* Valido el tipo de retorno*/
		if (!TipoToken.VOID.equals( tokenIdentificador.getRegTabSimbolos().getTipoToken() ))
			yyerror("ERROR: La funcion retornada ("+ tokenIdentificador.getLexema() +") debe ser de tipo VOID", this.lineaActual);
		}
break;
case 32:
//#line 235 "Gramatica.y"
{	notify("Sentencia IF sin ELSE en línea " + this.lineaActual + ".");
			polaca.completarPasoIncompleto(0);
		}
break;
case 33:
//#line 238 "Gramatica.y"
{ polaca.generarElse(); }
break;
case 34:
//#line 239 "Gramatica.y"
{	notify("Sentencia IF con ELSE en línea " + this.lineaActual + ".");
		polaca.completarPasoIncompleto(0);
	}
break;
case 35:
//#line 243 "Gramatica.y"
{	yyerror("ERROR: Faltó condición en IF", this.lineaActual);	}
break;
case 36:
//#line 244 "Gramatica.y"
{	yyerror("ERROR: Faltó bloque de sentencias en IF", this.lineaActual);	}
break;
case 39:
//#line 262 "Gramatica.y"
{ polaca.addElemento( new ElementoPI( ((Token)val_peek(1).obj).getLexema(), (Token)val_peek(2).obj)); polaca.generarBifurcacion("BF"); }
break;
case 40:
//#line 271 "Gramatica.y"
{
		notify("Sentencia de asignación en línea " + this.lineaActual + ".");

		validarDeclaracionIdentificador((Token)val_peek(3).obj);

		/*Valido semantica para el caso de asignacion de variables de tipo FUN*/
		if ( TipoToken.FUN.equals( ((Token)val_peek(3).obj).getRegTabSimbolos().getTipoToken()) ){
			if (cantFactores > 1)
				yyerror("ERROR: No se permiten asignacion de expresiones para identificadores de tipo FUN", this.lineaActual);
			
			/*$3 tiene el ultimo token reconocido de la expresion*/
			else if (!TipoToken.FUN.equals( ((Token)val_peek(1).obj).getRegTabSimbolos().getTipoToken()) )
				yyerror("ERROR: Se esperaba un identificador de tipo FUN despues del simbolo de asignacion :=", this.lineaActual);
		}
		cantFactores = 0;

		/*Genero codigo intermedio para asignacion*/
		polaca.addElemento( new ElementoPI( ((Token)val_peek(3).obj).getLexema(), (Token)val_peek(3).obj));
		polaca.addElemento( new ElementoPI( ((Token)val_peek(2).obj).getLexema(), (Token)val_peek(2).obj));
		}
break;
case 41:
//#line 292 "Gramatica.y"
{
		notify("Sentencia de asignación en línea " + this.lineaActual + ".");
		validarDeclaracionIdentificador( (Token)val_peek(2).obj );
		}
break;
case 44:
//#line 315 "Gramatica.y"
{	notify("Sentencia PRINT en línea " + this.lineaActual + ".");
			polaca.addElemento( new ElementoPI( ((Token)val_peek(2).obj).getLexema(), (Token)val_peek(2).obj));
			polaca.addElemento( new ElementoPI( ((Token)val_peek(4).obj).getLexema(), (Token)val_peek(4).obj));
		}
break;
case 45:
//#line 319 "Gramatica.y"
{ yyerror("ERROR: No se especificó ninguna cadena en sentencia PRINT", this.lineaActual); }
break;
case 46:
//#line 327 "Gramatica.y"
{ polaca.generarInicioCondicionFOR(); }
break;
case 47:
//#line 336 "Gramatica.y"
{	notify("Sentencia FOR en línea " + this.lineaActual + ".");
			polaca.generarBloqueFOR(pilaAcumulador.pop(), pilaAcumulador.pop());
		}
break;
case 48:
//#line 340 "Gramatica.y"
{	yyerror("ERROR: No se especificó  ningún bloque de sentencias en sentencia FOR", this.lineaActual);	}
break;
case 50:
//#line 358 "Gramatica.y"
{
		Token tokenIdentificador = ((Token)val_peek(3).obj);

		/*Valido semantica de uso para la variable del iterador*/
		RegTablaSimbolos regDeclaracion = validarDeclaracionIdentificador(tokenIdentificador);	
		if ( (regDeclaracion != null) && (!TipoToken.USINTEGER.equals(regDeclaracion.getTipoToken()) ))
			yyerror("ERROR: La variable usada en el iterador de la sentencia de iteracion FOR, debe ser de tipo entero");	

		/*Realiza acciones necesarias para generar inicio de iteracion para polaca inversa*/
		inicializarIteracion((Token)val_peek(3).obj, (Token)val_peek(2).obj, (Token)val_peek(1).obj);
		}
break;
case 51:
//#line 371 "Gramatica.y"
{
		/*Realiza acciones necesarias para generar inicio de iteracion para polaca inversa*/
		inicializarIteracion((Token)val_peek(3).obj, (Token)val_peek(2).obj, (Token)val_peek(1).obj);
		yyerror("ERROR: Solo se permite el uso de constantes enteras para inicializar la variable de iteracion", this.lineaActual);
		}
break;
case 52:
//#line 381 "Gramatica.y"
{	
		polaca.addElemento( new ElementoPI( ((Token)val_peek(2).obj).getLexema(), (Token)val_peek(2).obj));
		}
break;
case 53:
//#line 386 "Gramatica.y"
{	
		yyerror("ERROR: Falta variable en lado izquierdo del comparador en la condicion de iteracion FOR.");
		}
break;
case 54:
//#line 391 "Gramatica.y"
{	
		yyerror("ERROR: Falta comparador en la condicion de iteracion FOR.");
		}
break;
case 55:
//#line 398 "Gramatica.y"
{
			pilaAcumulador.push( (Token)val_peek(0).obj );
			polaca.generarBifurcacion("BF");
			}
break;
case 56:
//#line 404 "Gramatica.y"
{
		/*Semantica para la constante de incremento del iterador*/
		yyerror("ERROR: Solo se permite el uso de constantes enteras para incrementar la variable de iteracion FOR");
		pilaAcumulador.push( (Token)val_peek(0).obj );
		polaca.generarBifurcacion("BF");		
		}
break;
case 57:
//#line 417 "Gramatica.y"
{polaca.addElemento( new ElementoPI( ((Token)val_peek(0).obj).getLexema(), (Token)val_peek(0).obj));}
break;
case 58:
//#line 426 "Gramatica.y"
{
		Token tokenIdentificador = (Token)val_peek(3).obj;
		
		/*Valido semantica de uso de funciones*/
		validarDeclaracionIdentificador(tokenIdentificador);
		if (!TipoToken.FUN.equals(tokenIdentificador.getRegTabSimbolos().getTipoToken()))
			yyerror("ERROR: El identificador " + ((Token) val_peek(3).obj).getLexema() + " no es de tipo fun.");
		}
break;
case 59:
//#line 441 "Gramatica.y"
{ polaca.addElemento( new ElementoPI( ((Token)val_peek(1).obj).getLexema(), (Token)val_peek(1).obj));}
break;
case 60:
//#line 442 "Gramatica.y"
{ polaca.addElemento( new ElementoPI( ((Token)val_peek(1).obj).getLexema(), (Token)val_peek(1).obj));}
break;
case 62:
//#line 452 "Gramatica.y"
{
		polaca.addElemento( new ElementoPI( ((Token)val_peek(1).obj).getLexema(), (Token)val_peek(1).obj));
		cantFactores++;
		}
break;
case 63:
//#line 457 "Gramatica.y"
{
		polaca.addElemento( new ElementoPI( ((Token)val_peek(1).obj).getLexema(), (Token)val_peek(1).obj));
		cantFactores++;
		}
break;
case 64:
//#line 461 "Gramatica.y"
{cantFactores++;}
break;
case 65:
//#line 469 "Gramatica.y"
{ polaca.addElemento( new ElementoPI( ((Token)val_peek(0).obj).getLexema(), (Token)val_peek(0).obj));}
break;
case 66:
//#line 470 "Gramatica.y"
{ polaca.addElemento( new ElementoPI( ((Token)val_peek(0).obj).getLexema(), (Token)val_peek(0).obj));}
break;
case 67:
//#line 471 "Gramatica.y"
{ validarFlotante((Token) val_peek(0).obj);
								polaca.addElemento( new ElementoPI( ((Token)val_peek(0).obj).getLexema(), (Token)val_peek(0).obj));}
break;
case 68:
//#line 477 "Gramatica.y"
{
		polaca.addElemento( new ElementoPI( ((Token)val_peek(0).obj).getLexema(), (Token)val_peek(0).obj));
		validarDeclaracionIdentificador((Token)val_peek(0).obj);
		}
break;
case 69:
//#line 481 "Gramatica.y"
{ yyerror("ERROR: Se esperaba un factor en lugar del token: " + ((Token) val_peek(0).obj).getLexema()); }
break;
//#line 1055 "Parser.java"
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
