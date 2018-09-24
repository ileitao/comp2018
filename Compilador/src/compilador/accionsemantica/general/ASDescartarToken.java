package compilador.accionsemantica.general;

import compilador.AnalizadorLexico;
import compilador.accionsemantica.AccionSemantica;

public class ASDescartarToken extends AccionSemantica {

	public ASDescartarToken(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}

	@Override
	public void execute() {
		this.analizadorLexico.reiniciar();
	}

}
