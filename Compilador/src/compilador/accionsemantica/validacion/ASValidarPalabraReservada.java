package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.RegTablaSimbolos;
import compilador.TipoToken;
import compilador.log.EventoLog;

public class ASValidarPalabraReservada implements Validable {

	public ASValidarPalabraReservada() {
	}

	/**
	 * Retorna true si la palabra existe en la tabla de simbolos y ademas es palabra reservada.
	 */
	@Override
	public boolean evaluar(AnalizadorLexico aLexico, TipoToken tipoToken) {
		
		//Se retrocede el lector para volver a leer el ultimo caracter leido.
		aLexico.retrocederLectura();
		
		String lexema = aLexico.getLexemaParcial().toString();
		RegTablaSimbolos reg = aLexico.getTablaSimbolos().getRegistro(lexema);
		
		boolean valido = (reg != null) && (reg.getTipo().equals(tipoToken));
		
		if (valido)
			aLexico.setEstadoSiguiente(AnalizadorLexico.ESTADO_FINAL);
		
		return valido;
	}

	/**
	 * Se almacena el error y se reinicia el analizador lexico.
	 */
	@Override
	public void procesar(AnalizadorLexico aLexico) {
		String lexema = aLexico.getLexemaParcial().toString();
		int linea = aLexico.getLineaActual();
		
		aLexico.getLogger().log(new EventoLog(lexema + " no se reconoce como una palabra reservada.", EventoLog.ERROR, linea));
		aLexico.reiniciar();
	}

}
