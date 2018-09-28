package compilador.accionsemantica.general;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
import compilador.accionsemantica.AccionSemantica;
import compilador.accionsemantica.validacion.ASReconocerToken;
import compilador.accionsemantica.validacion.ASValidarPalabraReservada;

public class ASConcatenarLexema extends AccionSemantica {

	public ASConcatenarLexema(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}
	
	@Override
	public void execute() {
		
		//Validacion necesaria debido a que si estoy reconociendo una palabra reservada y vienen mayusculas,
		//se debe frenar la lectura y validar la palabra reservada.
		if( (this.analizadorLexico.getTipoToken().equals(TipoToken.PALABRA_RESERVADA))
			&& (Character.isUpperCase(this.analizadorLexico.getCharActual())) ) {
			
			//Si esta reconociendo una palabra reservada, y viene una letra mayuscula debo validar la palabra
			new ASReconocerToken(this.analizadorLexico, TipoToken.PALABRA_RESERVADA, new ASValidarPalabraReservada()).execute();
		}
		else
			this.analizadorLexico.getLexemaParcial().append(this.analizadorLexico.getCharActual());
	}

}
