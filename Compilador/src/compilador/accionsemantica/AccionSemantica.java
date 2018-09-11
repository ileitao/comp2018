package compilador.accionsemantica;

import compilador.AnalizadorLexico;
import compilador.log.Logger;

/**
 * Clase abstracta AccionSemantica
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public abstract class AccionSemantica {

	protected AnalizadorLexico analizadorLexico;
	
	//Estado resultante de la verificacion, el cual será seteado por cada implementacion de accion semantica.
	protected int estadoVerificacion;

	public AccionSemantica(AnalizadorLexico analizadorLexico) {
		this.analizadorLexico = analizadorLexico;
	}

	/**
	 * Realiza la verificacion correspondiente de la accion semantica y ejecuta la
	 * el metodo de correccion para la misma en caso de ser necesario.
	 */
	public void execute() {
		if (!verificar())
			corregir();
	}

	/**
	 * Es llamdo por el metodo al ejecutarse el metodo publico execute().; Realiza
	 * la correccion necesaria por parte de la accion semantica. El funcionamiento
	 * por defecto es NO ACCION.
	 */
	protected void corregir() {
	};

	/**
	 * Realiza las validaciones necesarias de la accion semantica que implementa la
	 * clase
	 * 
	 * @return Debe retornar true si el token es valido, en caso contrario false.
	 */
	protected abstract boolean verificar();
}
