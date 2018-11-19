package compilador.accionsemantica;

import compilador.AnalizadorLexico;

/**
 * 
 * @author leandro
 *
 */
public class ASReiniciar extends AccionSemantica {

	public ASReiniciar(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}

	/**
	 * Reinicia el analizador lexico
	 */
	@Override
	public void execute() {
		
		this.aLexico.reiniciar();
		
	}

}
