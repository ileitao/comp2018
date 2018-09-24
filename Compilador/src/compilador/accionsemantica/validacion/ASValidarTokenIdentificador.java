package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
import compilador.log.EventoLog;

public class ASValidarTokenIdentificador extends ASValidarToken {

	private final int LARGO_MAXIMO = 25;
	
	public ASValidarTokenIdentificador(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico, TipoToken.IDENTIFICADOR);
	}

	@Override
	public boolean validar() {
		
		//Devuelve false en caso de superar el largo permitido
		return (this.analizadorLexico.getLexemaParcial().length() <= LARGO_MAXIMO);
		
	}

	@Override
	public void hacer() {
		
		String lexema = this.analizadorLexico.getLexemaParcial().toString();
		int linea = this.analizadorLexico.getLineaActual();
		
		this.analizadorLexico.getLogger().log(new EventoLog("El identificador" + lexema 
				+ " supera el largo maximo permitido " + LARGO_MAXIMO + " caracteres.", EventoLog.ERROR, linea));
		
		this.analizadorLexico.reiniciar();
	}

}
