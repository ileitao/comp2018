package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;

public interface Validable {

	/**
	 * Realiza una evaluacion para un determinado tipo de token en caso de requerirse
	 * @return true si supera todas las validaciones, sino false.
	 */
	public boolean evaluar(AnalizadorLexico aLexico, TipoToken tipoToken);
	
	/**
	 * Realiza acciones segun el tipo de token.
	 */
	public void procesar(AnalizadorLexico aLexico);
	
}
