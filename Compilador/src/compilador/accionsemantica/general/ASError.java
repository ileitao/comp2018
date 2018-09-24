package compilador.accionsemantica.general;

import compilador.AnalizadorLexico;
import compilador.accionsemantica.AccionSemantica;
import compilador.log.EventoLog;

public class ASError extends AccionSemantica {
	
	public ASError(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}
	
	@Override
	public void execute() {
		Character simbolo = this.analizadorLexico.getCharActual();
		int linea = this.analizadorLexico.getLineaActual();
		
		this.analizadorLexico.getLogger().log(new EventoLog("Simbolo inesperado: " + simbolo, EventoLog.ERROR, linea));
		this.analizadorLexico.reiniciar();
	}

}
