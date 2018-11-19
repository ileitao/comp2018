package compilador.accionsemantica;

import java.util.Arrays;

import compilador.AnalizadorLexico;
import compilador.RegTablaSimbolos;
import compilador.analizadorsintactico.Parser;

/**
 * 
 * @author leandro
 *
 */
public class ASValidarPalabraReservada extends AccionSemantica {


	public ASValidarPalabraReservada(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}

	@Override
	public void execute() {
		
		//Se retrocede el lector para volver a leer el ultimo caracter leido.
		aLexico.retrocederLectura();
		
		String lexema = aLexico.getLexemaParcial().toString();
		RegTablaSimbolos reg = aLexico.getTablaSimbolos().getRegistro(lexema);
		
		ASError error = new ASError(this.aLexico, "Palabra reservada invalida: '" + lexema +"'");
		
		//Si la palabra no existe en la tabla de simbolos es ERROR.
		if (reg == null)
			error.execute();
		else {
			short codigo = (short) reg.getToken().getCodigo();
			//Si la palabra existe pero no esta dentro de las reservadas es ERROR
			if (! Arrays.asList(	Parser._IF,
									Parser._ELSE,
									Parser._ENDIF,
									Parser._PRINT,
									Parser._USINTEGER,
									Parser._SINGLE,
									Parser._FOR,
									Parser._VOID,
									Parser._FUN,
									Parser._RETURN)
							.contains(codigo)
				)
				error.execute();
		}
	}

}
