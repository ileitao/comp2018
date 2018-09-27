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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    2,    2,    4,    5,    7,    7,    7,
    6,    3,   11,   11,   12,   12,    8,    9,    9,    9,
   13,   13,   13,   14,   15,   15,   10,   10,   10,   10,
   10,   10,
};
final static short yylen[] = {                            2,
    2,    1,    1,    2,    1,    1,    5,    0,    1,    1,
    3,    3,    1,    1,    1,    3,    4,    3,    3,    1,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,
};
final static short yydefred[] = {                         0,
   13,   14,    0,    0,    3,    0,    0,    0,    1,    5,
    6,    0,    0,    0,    4,    0,   12,   25,   26,    0,
    0,    0,   23,   24,   16,    0,    0,    0,   31,   27,
   29,   28,   30,   32,    0,    0,    0,    0,   10,    7,
    9,    0,    0,    0,   21,   22,    0,    0,   17,
};
final static short yydgoto[] = {                          3,
    8,    9,    5,   10,   11,   20,   40,   41,   21,   35,
    6,   13,   22,   23,   24,
};
final static short yysindex[] = {                      -205,
    0,    0,    0, -202,    0, -272, -249, -202,    0,    0,
    0, -248, -256, -200,    0, -272,    0,    0,    0, -237,
 -223, -206,    0,    0,    0, -257, -200, -200,    0,    0,
    0,    0,    0,    0, -200, -200, -200, -224,    0,    0,
    0, -206, -206, -201,    0,    0, -200, -234,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,   58,    0,    0,    0,    0,    0,    0,
    0, -217,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -265,    0,    0,    0,   72,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -252, -236, -204,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   73,   -7,    0,    0,    0,    0,    0,    0,  -29,    0,
    0,   60,   41,   34,    0,
};
final static int YYTABLESIZE=76;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                          7,
   15,   20,   20,    1,    2,   44,   20,   20,   20,   20,
   20,   20,   12,   20,   18,   18,   20,   48,   39,   18,
   18,   18,   18,   18,   18,   17,   18,   38,   14,   18,
   19,   19,   27,   28,   16,   19,   19,   19,   19,   19,
   19,   26,   19,   27,   28,   19,   47,   49,   29,   30,
   31,   32,   33,   34,    7,    1,    2,    2,    1,    2,
   18,   19,   36,   37,   15,   27,   28,   42,   43,   45,
   46,    8,    4,    0,   11,   25,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                        257,
    8,  267,  268,  261,  262,   35,  272,  273,  274,  275,
  276,  277,  285,  279,  267,  268,  282,   47,   26,  272,
  273,  274,  275,  276,  277,  282,  279,  285,  278,  282,
  267,  268,  267,  268,  283,  272,  273,  274,  275,  276,
  277,  279,  279,  267,  268,  282,  271,  282,  272,  273,
  274,  275,  276,  277,  257,  261,  262,    0,  261,  262,
  261,  262,  269,  270,  282,  267,  268,   27,   28,   36,
   37,    0,    0,   -1,  279,   16,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=285;
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
"_IDENTIFIER",
};
final static String yyrule[] = {
"$accept : programa",
"programa : bloque_declarativo bloque",
"programa : bloque_declarativo",
"bloque_declarativo : sentencias_de_declaracion_de_variables",
"bloque : bloque_declarativo bloque",
"bloque : sentencia",
"sentencia : seleccion",
"seleccion : _IF _LPAREN condicion _RPAREN bloque_de_seleccion",
"bloque_de_seleccion :",
"bloque_de_seleccion : asignacion",
"bloque_de_seleccion : bloque",
"condicion : expresion comparador expresion",
"sentencias_de_declaracion_de_variables : tipo lista_de_variables _COMMA",
"tipo : _USINTEGER",
"tipo : _SINGLE",
"lista_de_variables : _IDENTIFIER",
"lista_de_variables : _IDENTIFIER _SEMICOLON lista_de_variables",
"asignacion : _IDENTIFIER _ASSIGN expresion _COMMA",
"expresion : expresion _PLUS termino",
"expresion : expresion _MINUS termino",
"expresion : termino",
"termino : termino _MULT factor",
"termino : termino _DIV factor",
"termino : factor",
"factor : constante",
"constante : _USINTEGER",
"constante : _SINGLE",
"comparador : _LESSER",
"comparador : _GREATER",
"comparador : _LESSER_OR_EQUAL",
"comparador : _GREATER_OR_EQUAL",
"comparador : _EQUAL",
"comparador : _UNEQUAL",
};

//#line 211 "Gramatica.y"

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
  yyparse();
}
//#line 325 "Parser.java"
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
case 1:
//#line 45 "Gramatica.y"
{ notify("Compilación terminada.");	}
break;
case 2:
//#line 46 "Gramatica.y"
{ notify("Compilación terminada.");	}
break;
case 6:
//#line 71 "Gramatica.y"
{	notify("Sentencia IF " + this.lineaActual + ".");	}
break;
case 12:
//#line 105 "Gramatica.y"
{ notify("Sentencia de declaración de variables en línea " + this.lineaActual + "."); }
break;
case 13:
//#line 113 "Gramatica.y"
{ this.tipoActual = TipoToken.CONSTANTE_ENTERO_SIN_SIGNO; }
break;
case 14:
//#line 114 "Gramatica.y"
{ this.tipoActual = TipoToken.CONSTANTE_FLOTANTE;	}
break;
case 15:
//#line 123 "Gramatica.y"
{
		notify("Identificador " + yyval.sval + ".");
		/*this.tipoActual = TipoToken.IDENTIFICADOR;
		RegTablaSimbolos reg = this.tablaDeSimbolos.getRegistro($1.toString());
		if (reg == null) {
			reg = this.tablaDeSimbolos.createRegTabla($1.toString(), this.tipoActual, this.analizadorLexico.getLineaActual(), this.analizadorLexico.getPunteroActual());
			this.tablaDeSimbolos.agregarSimbolo(reg);
		}*/
	}
break;
case 16:
//#line 133 "Gramatica.y"
{
		notify("Identificador " + yyval.sval + ".");
		/*this.tipoActual = TipoToken.IDENTIFICADOR;
		RegTablaSimbolos reg = this.tablaDeSimbolos.getRegistro($3.toString());
		if (reg == null) {
			reg = this.tablaDeSimbolos.createRegTabla($3.toString(), this.tipoActual, this.analizadorLexico.getLineaActual(), this.analizadorLexico.getPunteroActual());
			this.tablaDeSimbolos.agregarSimbolo(reg);
		}*/
	}
break;
case 17:
//#line 150 "Gramatica.y"
{
		notify(val_peek(3).toString());
		notify(val_peek(2).toString());
		notify(val_peek(1).toString());
	}
break;
case 24:
//#line 183 "Gramatica.y"
{
		yyval = val_peek(0);
		notify(val_peek(0).toString());
	}
break;
//#line 537 "Parser.java"
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
