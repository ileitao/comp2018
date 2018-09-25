package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.RegTablaSimbolos;
import compilador.TipoToken;
import compilador.log.EventoLog;

public class ASTokenPalabraReservada extends ASReconocerToken {

	public ASTokenPalabraReservada(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico, TipoToken.PALABRA_RESERVADA);
	}

	/**
	 * Retorna true si la palabra existe en la tabla de simbolos y ademas es palabra reservada.
	 */
	@Override
	public boolean validar() {
		String lexema = this.analizadorLexico.getLexemaParcial().toString();
		RegTablaSimbolos reg = this.analizadorLexico.getTablaSimbolos().getRegistro(lexema);
		
		return (reg != null) && (reg.getTipo().equals(this.tipotoken));
	}

	/**
	 * Se almacena el error y se reinicia el analizador lexico.
	 */
	@Override
	public void hacer() {
		String lexema = this.analizadorLexico.getLexemaParcial().toString();
		int linea = this.analizadorLexico.getLineaActual();
		
		this.analizadorLexico.getLogger().log(new EventoLog(lexema + " no se reconoce como una palabra reservada.", EventoLog.ERROR, linea));
		this.analizadorLexico.reiniciar();
	}

}
