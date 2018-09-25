package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
import compilador.accionsemantica.validacion.ASReconocerToken;

public class ASTokenCadenaCarateres extends ASReconocerToken {

	public ASTokenCadenaCarateres(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico, TipoToken.CADENA_CARACTERES);
	}

	/**
	 * No requiere validacion, se realiza la accion necesaria.
	 */
	@Override
	protected boolean validar() {
		return false;
	}

	/**
	 * Descarta las comillas que determinan la cadena de texto.
	 */
	@Override
	protected void hacer() {
		
		String lexema = this.analizadorLexico.getLexemaParcial().toString();
		lexema = lexema.substring(1, lexema.length()-1);
		
		this.analizadorLexico.setLexemaParcial(lexema);
	}


}
