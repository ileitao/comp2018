package compilador.accionsemantica;

import compilador.AnalizadorLexico;

/**
 * Clase abstracta AccionSemantica
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public abstract class AccionSemantica {
	
	public static final int AS_ERROR = 20;
	
	protected AnalizadorLexico aLexico;

	public AccionSemantica(AnalizadorLexico analizadorLexico) {
		this.aLexico = analizadorLexico;
	}

	/**
	 * Realiza la verificacion correspondiente de la accion semantica y ejecuta la
	 * el metodo de correccion para la misma en caso de ser necesario.
	 */
	public abstract void execute();

}
