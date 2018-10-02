package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;

public class ASTokenOtro implements Validable {

	@Override
	public boolean validar(AnalizadorLexico aLexico, TipoToken tipoToken) {
		return false;
	}

	@Override
	public void finalizar(AnalizadorLexico aLexico) {
		aLexico.setLexemaParcial(aLexico.getCharActual().toString());
	}

}
