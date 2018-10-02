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
	public boolean validar(AnalizadorLexico aLexico, TipoToken tipoToken) {
		return false;
	}

	@Override
	public void finalizar(AnalizadorLexico aLexico) {

		if (this.concatenar)
			//armo el comparador compuesto
			aLexico.getLexemaParcial().append(aLexico.getCharActual());
		else
			
			//es un comparador simple, inicializo lexema
			aLexico.setLexemaParcial(aLexico.getCharActual().toString());
	}

}
