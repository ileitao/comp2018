package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;

public interface Validable {

	/**
	 * Ejecuta acciones y evalua errores para un determinado tipo de token en caso de requerirse
	 * @return true si supera todas las validaciones, sino false.
	 */
	public boolean validar(AnalizadorLexico aLexico, TipoToken tipoToken);
	
	/**
	 * Realiza acciones segun el tipo de token.
	 */
	public void finalizar(AnalizadorLexico aLexico);
	
}
