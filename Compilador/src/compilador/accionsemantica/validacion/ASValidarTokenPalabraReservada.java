package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.RegTablaSimbolos;
import compilador.TipoToken;
import compilador.log.EventoLog;

public class ASValidarTokenPalabraReservada extends ASValidarToken {

	public ASValidarTokenPalabraReservada(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico, TipoToken.PALABRA_RESERVADA);
	}

	/**
	 * Retorna true si la palabra existe en la tabla de simbolos.
	 * Se sobreentiende que es palabra reservada porque es un lexema completamente en minusculas.
	 * Aun asi, se realiza la verificacion.
	 */
	@Override
	public boolean validar() {
		String lexema = this.analizadorLexico.getLexemaParcial().toString();
		RegTablaSimbolos reg = this.analizadorLexico.getTablaSimbolos().getRegistro(lexema);
		
		//Supera la validacion en caso de existir el lexema en la tabla de simbolos
		//y ademas, ser de tipo PALABRA_RESERVADA.
		return (reg != null) && (reg.getTipo().equals(this.tipotoken));
	}

	/**
	 * Se
	 */
	@Override
	public void hacer() {
		String lexema = this.analizadorLexico.getLexemaParcial().toString();
		int linea = this.analizadorLexico.getLineaActual();
		
		this.analizadorLexico.getLogger().log(new EventoLog(lexema + " no se reconoce como una palabra reservada.", EventoLog.ERROR, linea));
		this.analizadorLexico.reiniciar();
	}

}
