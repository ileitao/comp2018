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
	public boolean validar(AnalizadorLexico aLexico, TipoToken tipoToken) {

		//Concatena la "i" con la que se va a estado final
		aLexico.getLexemaParcial().append(aLexico.getCharActual().toString());
		String lexema = aLexico.getLexemaParcial().toString();
		
		if (!lexema.endsWith("_ui"))
			return false;
		
		//Descarto el postifjo "_ui" para quedarme con el numero.
		lexema = lexema.split("_")[0];
		int numero = Integer.parseInt(lexema);
		
		if ((numero >= MIN_ENTERO_SIN_SIGNO) && (numero <= MAX_ENTERO_SIN_SIGNO)){
			aLexico.setLexemaParcial(lexema);
			return true;
		}
		else
			return false;
	}

	@Override
	public void finalizar(AnalizadorLexico aLexico) {
		
		//En caso de estar fuera del rango se utiliza la tecnica de reemplazo con el maximo valor permitido.
		//Se setea un warning.
		String numero = aLexico.getLexemaParcial().toString();
		numero = numero.substring(0, numero.length()-2);
		int linea = aLexico.getLineaActual();
		
		aLexico.getLogger().log(new EventoLog("Se ha truncado la constante " + numero
				+ " al maximo valor permitido " + MAX_ENTERO_SIN_SIGNO, EventoLog.WARNING, linea));
		
		aLexico.setLexemaParcial(String.valueOf(MAX_ENTERO_SIN_SIGNO));
		
		
	}

}
