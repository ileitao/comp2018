package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;

public class ASOperador implements Validable {

	private boolean concatenar = false;
	
	public ASOperador() {
	}
	
	public ASOperador(boolean concatenar) {
		this.concatenar = concatenar;
	}
	
	@Override
	public boolean evaluar(AnalizadorLexico aLexico, TipoToken tipoToken) {
		return false;
	}

	@Override
	public void procesar(AnalizadorLexico aLexico) {
		
		if (this.concatenar)
			//armo el operador de asignacion
			aLexico.getLexemaParcial().append(aLexico.getCharActual());
		else
			//operador aritmetico
			aLexico.setLexemaParcial(aLexico.getCharActual().toString());
	}

}
