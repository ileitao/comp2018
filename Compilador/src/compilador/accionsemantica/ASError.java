package compilador.accionsemantica;

import compilador.AnalizadorLexico;
import compilador.log.EventoLog;

/**
 * 
 * @author leandro
 *
 */
public class ASError extends AccionSemantica {

	private String mensaje;
	
	public ASError(AnalizadorLexico aLexico, String mensaje) {
		super(aLexico);
		this.mensaje = mensaje;
	}

	@Override
	public void execute() {
		
		EventoLog eventoError = new EventoLog(this.mensaje,
												EventoLog.ERROR,
												aLexico.getLineaActual(),
												aLexico.getPunteroActual());
		aLexico.getLogger().log(eventoError);
		
		aLexico.reiniciar();
		
	}

}
