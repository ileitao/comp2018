package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
import compilador.log.EventoLog;

public class ASValidarEnteroSinSigno implements Validable {

	//(2^16)-1
	private final int MAX_ENTERO_SIN_SIGNO = 65535;
	private final int MIN_ENTERO_SIN_SIGNO = 0;
	
	public ASValidarEnteroSinSigno() {
	}

	@Override
	public boolean evaluar(AnalizadorLexico aLexico, TipoToken tipoToken) {
		
		String lexema = aLexico.getLexemaParcial().toString();
		lexema = lexema.substring(0, lexema.length()-2);
		int numero = Integer.parseInt(lexema);
		
		if ((numero >= MIN_ENTERO_SIN_SIGNO) && (numero <= MAX_ENTERO_SIN_SIGNO)){
			aLexico.setLexemaParcial(lexema);
			return true;
		}
		else
			return false;
	}

	@Override
	public void procesar(AnalizadorLexico aLexico) {
		//En caso de estar fuera del rango se utiliza la tecnica de reemplazo con el maximo valor permitido.
		//Se setea un warning.
		String numero = aLexico.getLexemaParcial().toString();
		int linea = aLexico.getLineaActual();
		
		aLexico.getLogger().log(new EventoLog("Se ha truncado la constante " + numero
				+ " al maximo valor permitido " + MAX_ENTERO_SIN_SIGNO, EventoLog.WARNING, linea));
		
		aLexico.setLexemaParcial(String.valueOf(MAX_ENTERO_SIN_SIGNO));
	}

}
