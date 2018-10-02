package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;

public class ASValidarCadenaCarateres implements Validable {

	public ASValidarCadenaCarateres() {
	}

	/**
	 * No requiere validacion, se realiza la accion necesaria.
	 */
	@Override
	public boolean validar(AnalizadorLexico aLexico, TipoToken tipoToken) {
		return false;
	}

	/**
	 * Descarta las comillas que determinan la cadena de texto.
	 */
	@Override
	public void finalizar(AnalizadorLexico aLexico) {
		
		String lexema = aLexico.getLexemaParcial().toString();
		lexema = lexema.substring(1, lexema.length()-1);
		
		aLexico.setLexemaParcial(lexema);
	}


}
