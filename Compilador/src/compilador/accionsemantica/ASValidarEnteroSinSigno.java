package compilador.accionsemantica;

import compilador.AnalizadorLexico;
import compilador.analizadorsintactico.Parser;
import compilador.log.EventoLog;

/**
 * 
 * @author leandro
 *
 */
public class ASValidarEnteroSinSigno extends AccionSemantica {

	//(2^16)-1
	private final int MAX_ENTERO_SIN_SIGNO = 65535;
	private final int MIN_ENTERO_SIN_SIGNO = 0;
	
	public ASValidarEnteroSinSigno(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);

	}

	@Override
	public void execute() {
		
		//ESTADO FINAL: Se finaliza el reconocimiento del token realizando las validaciones
		//y correcciones que sean necesarias acorde al tipo de Token.
		
		//Seteo el codigo de token que se va a reconocer
		this.aLexico.setCodigoTokenReconocido(Parser._CONSTANT_UNSIGNED_INTEGER);
		
		//Descarto el postifjo "_ui" para quedarme con el numero.
		String lexema = aLexico.getLexemaParcial().toString().split("_")[0];
		int numero = Integer.parseInt(lexema);
		
		//En caso de estar fuera del rango se utiliza la tecnica de reemplazo con el maximo valor permitido.
		if ((numero < MIN_ENTERO_SIN_SIGNO) || (numero > MAX_ENTERO_SIN_SIGNO)) {

			lexema = String.valueOf(MAX_ENTERO_SIN_SIGNO);
			
			//Se setea un warning.
			int linea = aLexico.getLineaActual();
			int posicion = aLexico.getPunteroActual();
			
			aLexico.getLogger().log(new EventoLog("Se ha truncado la constante " + numero
													+ " al maximo valor permitido " + MAX_ENTERO_SIN_SIGNO,
													EventoLog.WARNING,
													linea,
													posicion));	
		}
		
		aLexico.setLexemaParcial(lexema);
			
	}
}
