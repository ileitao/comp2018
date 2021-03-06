package compilador.accionsemantica;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
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
		aLexico.setTipoToken(TipoToken.CADENA_CARACTERES);
		
		String lexema = aLexico.getLexemaParcial().toString();
		lexema = lexema.substring(1, lexema.length());
		
		aLexico.setLexemaParcial(lexema);
		
	}


}
