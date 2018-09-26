package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;

public class ASComparador implements Validable {

	private boolean concatenar = false;
	
	public ASComparador() {
	}
	
	public ASComparador(boolean concatenar) {
		this.concatenar = concatenar;
	}
	
	/**
	 * No requiere validacion
	 */
	@Override
	public boolean evaluar(AnalizadorLexico aLexico, TipoToken tipoToken) {
		return false;
	}

	@Override
	public void procesar(AnalizadorLexico aLexico) {

		if (this.concatenar)
			//armo el comparador compuesto
			aLexico.getLexemaParcial().append(aLexico.getCharActual());
		else
			//es un comparador simple, retrocedo lector
			aLexico.retrocederLectura();
	}

}
