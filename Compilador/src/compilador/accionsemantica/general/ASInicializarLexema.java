package compilador.accionsemantica.general;

import compilador.AnalizadorLexico;
import compilador.accionsemantica.AccionSemantica;

public class ASInicializarLexema extends AccionSemantica {

	public ASInicializarLexema(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}
	
	@Override
	public void execute() {
		if(!Character.isUpperCase(this.analizadorLexico.getCharActual()))
			this.analizadorLexico.setLexemaParcial(this.analizadorLexico.getCharActual().toString());
		else
			new ASError(this.analizadorLexico).execute();
	}
	

}
