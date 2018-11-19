package compilador.accionsemantica;

import compilador.AnalizadorLexico;
import compilador.analizadorsintactico.Parser;

/**
 * 
 * @author leandro
 *
 */
public class ASValidarCadenaCarateres extends AccionSemantica {

	public ASValidarCadenaCarateres(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}

	/**
	 * Descarta las comillas que determinan la cadena de texto.
	 */
	@Override
	public void execute() {
		
		aLexico.setCodigoTokenReconocido(Parser._CONSTANT_STRING);
		
		String lexema = aLexico.getLexemaParcial().toString();
		lexema = lexema.substring(1, lexema.length()-1);
		
		aLexico.setLexemaParcial(lexema);
		
	}


}
