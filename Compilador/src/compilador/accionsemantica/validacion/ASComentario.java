package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;

public class ASComentario implements Validable {

	public ASComentario() {
	}

	@Override
	public boolean validar(AnalizadorLexico aLexico, TipoToken tipoToken) {
		return false;
	}

	@Override
	public void finalizar(AnalizadorLexico aLexico) {
		aLexico.reiniciar();
	}

}
