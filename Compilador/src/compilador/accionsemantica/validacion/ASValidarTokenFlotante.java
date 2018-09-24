package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;

public class ASValidarTokenFlotante extends ASValidarToken {

	public ASValidarTokenFlotante(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico, TipoToken.CONSTANTE_FLOTANTE);
	}

	@Override
	public boolean validar() {
		return false;
	}

	@Override
	public void hacer() {
		this.analizadorLexico.setTipoToken(this.tipotoken);
		System.out.println("Validacion flotante");
	}

}
