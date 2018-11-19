package compilador.accionsemantica;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
import compilador.log.MensajesEventos;

/**
 * 
 * @author leandro
 *
 */
public class ASReconocerToken extends AccionSemantica {

	// Tipo de token reconocido.
	protected int codigoToken = -1;
	
	public ASReconocerToken(AnalizadorLexico aLexico) {
		super(aLexico);
	}

	/**
	 * Concatena el simbolo leido al lexema parcial.
	 * En caso de ser estado inicial valida que el simbolo no sea una mayuscula y setea error en caso
	 * de ser necesario.
	 */
	@Override
	public void execute() {

		Character simbolo = aLexico.getCharActual();
		
		//ESTADO INICIAL: inicializo lexema
		if (this.aLexico.getEstadoActual() == AnalizadorLexico.ESTADO_INICIAL) {

			//En caso de venir una mayuscula setea error indicando el simbolo inesperado
			if(Character.isUpperCase(simbolo)) {
			
				new ASError(aLexico, MensajesEventos.ERROR_MAYUSCULA + simbolo).execute();
			}
			else {
				this.aLexico.setLexemaParcial(simbolo.toString());
				
				//En caso de reconocer palabra reservada, pre-seteo codigo de token
				if(Character.isLetter(simbolo))
					aLexico.setTipoToken(TipoToken.PALABRA_RESERVADA);
			}
		}
		else {
			//ESTADO INTERMEDIO: concatenarse acepten mayusculas para palabras reservadas
			
			//En caso de estar reconociendo una palabra reservada y llega una mayuscula
			//Se procede a validar la palabra
			if(TipoToken.PALABRA_RESERVADA.equals(aLexico.getTipoToken())
				&& (Character.isUpperCase(simbolo)))
					new ASValidarPalabraReservada(this.aLexico).execute();
			
			else
				this.aLexico.getLexemaParcial().append(simbolo.toString());
		}
	}

}
