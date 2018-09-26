package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;

public class ASCadenaCaracteres implements Validable {

	public ASCadenaCaracteres() {
	}
	
	@Override
	public boolean evaluar(AnalizadorLexico aLexico, TipoToken tipoToken) {
		return false;
	}

	@Override
	public void procesar(AnalizadorLexico aLexico) {
		
		String lexema = aLexico.getLexemaParcial().toString();
		
		//Descarto comilla de comienzo de cadena de caracteres
		aLexico.setLexemaParcial(lexema.substring(1,lexema.length()));
	}

}
