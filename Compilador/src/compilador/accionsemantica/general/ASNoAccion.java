package compilador.accionsemantica.general;

import compilador.AnalizadorLexico;
import compilador.accionsemantica.AccionSemantica;

public class ASNoAccion extends AccionSemantica {

	public ASNoAccion(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}

	@Override
	public void execute() {
		//No hacer nada
	}

}
