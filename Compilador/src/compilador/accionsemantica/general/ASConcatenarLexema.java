package compilador.accionsemantica.general;

import compilador.AnalizadorLexico;
import compilador.accionsemantica.AccionSemantica;

public class ASConcatenarLexema extends AccionSemantica {

	public ASConcatenarLexema(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}
	
	@Override
	public void execute() {
		this.analizadorLexico.getLexemaParcial().append(this.analizadorLexico.getCharActual());
	}

}
