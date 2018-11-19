package compilador.accionsemantica;

import compilador.AnalizadorLexico;
import compilador.analizadorsintactico.Parser;

/**
 * 
 * @author leandro
 *
 */
public class ASValidarIdentificador extends AccionSemantica {

	private final int LARGO_MAXIMO = 25;
	
	public ASValidarIdentificador(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}

	@Override
	public void execute() {
		
		aLexico.retrocederLectura();
		
		aLexico.setCodigoTokenReconocido(Parser._IDENTIFIER);
		
		// Verifica longitud de identificar y setea error en caso de superar el maximo permitido
		if (aLexico.getLexemaParcial().length() > LARGO_MAXIMO) {
			
			String lexema = aLexico.getLexemaParcial().toString();
			
			new ASError(this.aLexico, "El identificador '" + lexema 
					+ "' supera el largo maximo permitido "
					+ LARGO_MAXIMO + " caracteres.")
			.execute();
			
			aLexico.reiniciar();
		}
		
	}

}
