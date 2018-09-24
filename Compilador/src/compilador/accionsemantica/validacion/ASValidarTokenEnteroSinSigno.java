package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
import compilador.log.EventoLog;

public class ASValidarTokenEnteroSinSigno extends ASValidarToken {

	//(2^16)-1
	private final int MAX_ENTERO_SIN_SIGNO = 65535;
	private final int MIN_ENTERO_SIN_SIGNO = 0;
	
	public ASValidarTokenEnteroSinSigno(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico, TipoToken.CONSTANTE_ENTERO_SIN_SIGNO);
	}

	@Override
	public boolean validar() {
		
		int numero = Integer.parseInt(this.analizadorLexico.getLexemaParcial().toString());
		
		return (numero >= MIN_ENTERO_SIN_SIGNO) && (numero <= MAX_ENTERO_SIN_SIGNO);
	}

	@Override
	public void hacer() {
		//En caso de estar fuera del rango se utiliza la tecnica de reemplazo con el maximo valor permitido.
		//Se setea un warning.
		String numero = this.analizadorLexico.getLexemaParcial().toString();
		int linea = this.analizadorLexico.getLineaActual();
		
		this.analizadorLexico.getLogger().log(new EventoLog("Se ha truncado la constante " + numero
				+ " al maximo valor permitido " + MAX_ENTERO_SIN_SIGNO, EventoLog.WARNING, linea));
		
		this.analizadorLexico.setLexemaParcial(String.valueOf(MAX_ENTERO_SIN_SIGNO));
	}

}
