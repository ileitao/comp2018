package compilador.accionsemantica.general;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
import compilador.accionsemantica.AccionSemantica;

public class ASInicializarLexema extends AccionSemantica {

	public ASInicializarLexema(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}
	
	@Override
	public void execute() {
		
		//Inicializacion necesaria para evitar problema de mayusculas al reconocer palabras reservadas.
		if(Character.isLetter(this.analizadorLexico.getCharActual()))
			this.analizadorLexico.setTipoToken(TipoToken.PALABRA_RESERVADA);
		
		//No existe ningun token que empiece con letras mayusculas
		if(!Character.isUpperCase(this.analizadorLexico.getCharActual()))
			this.analizadorLexico.setLexemaParcial(this.analizadorLexico.getCharActual().toString());
		else
			this.analizadorLexico.getAccionesSemanticas().get(AccionSemantica.AS_ERROR).execute();
	}
	

}
