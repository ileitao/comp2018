package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;

public class ASTokenOperador extends ASReconocerToken {

	public ASTokenOperador(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico, TipoToken.COMPARADOR);
	}

	@Override
	protected boolean validar() {
		return false;
	}

	@Override
	protected void hacer() {
	}

}
