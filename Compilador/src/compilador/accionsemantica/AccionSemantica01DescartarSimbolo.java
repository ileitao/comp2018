package compilador.accionsemantica;

import compilador.AnalizadorLexico;

/**
 * Clase AccionSemantica01HacerNada
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public class AccionSemantica01DescartarSimbolo extends AccionSemantica {

    public AccionSemantica01DescartarSimbolo(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

	@Override
	protected boolean verificar() {
		return false;
	}
    
	@Override
	protected void corregir() {
		this.analizadorLexico.setLexemaParcial("");
	}
}
