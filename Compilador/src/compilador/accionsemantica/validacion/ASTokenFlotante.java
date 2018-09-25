package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;

public class ASTokenFlotante extends ASReconocerToken {

	public ASTokenFlotante(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico, TipoToken.CONSTANTE_FLOTANTE);
	}

	@Override
	public boolean validar() {
		return false;
	}

	@Override
	public void hacer() {
		System.out.println("Validacion flotante");
	}

}
