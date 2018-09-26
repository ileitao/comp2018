package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;

public class ASTokenOtro implements Validable {

	@Override
	public boolean evaluar(AnalizadorLexico aLexico, TipoToken tipoToken) {
		return false;
	}

	@Override
	public void procesar(AnalizadorLexico aLexico) {
		aLexico.setLexemaParcial(aLexico.getCharActual().toString());
	}

}
