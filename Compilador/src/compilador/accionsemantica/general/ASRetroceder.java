package compilador.accionsemantica.general;

import compilador.AnalizadorLexico;
import compilador.accionsemantica.AccionSemantica;

public class ASRetroceder extends AccionSemantica {

	public ASRetroceder(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}

	@Override
	public void execute() {
		this.analizadorLexico.retrocederLectura();
	}

}
