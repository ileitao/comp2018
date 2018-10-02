package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
import compilador.accionsemantica.AccionSemantica;

public class ASReconocerToken extends AccionSemantica {

	// Tipo de token reconocido. Es seteado por la clase que extiende.
	protected TipoToken tipotoken;

	// Realiza las verificaciones necesarias para un tipo de token.
	private Validable validador;

	public ASReconocerToken(AnalizadorLexico aLexico, TipoToken tipoToken, Validable validador) {
		super(aLexico);
		this.tipotoken = tipoToken;
		this.validador = validador;
	}

	/**
	 * Este metodo es el que se presenta para ejecutar la accion semantica.
	 * Internamente realiza validaciones y ejecuta acciones correctivas segun
	 * corresponda.
	 * 
	 * Implementa el metodo execute de la superclase. Setea el tipo de token
	 * reconocido, realiza validaciones y acciones segun corresponda.
	 */
	@Override
	public void execute() {

		//ESTADO INICIAL: inicializo lexema y seteo el tipo de token que se esta reconociendo
		if (this.analizadorLexico.getEstadoActual() == AnalizadorLexico.ESTADO_INICIAL) {

			this.analizadorLexico.setLexemaParcial(this.analizadorLexico.getCharActual().toString());
			this.analizadorLexico.setTipoToken(this.tipotoken);
		}
		else {

			//ESTADO FINAL: se ejecuta la funcionalidad especifica de la AS que implementa la clase
			if (this.analizadorLexico.getEstadoSiguiente() == AnalizadorLexico.ESTADO_FINAL) {
				
				//Se ejecuta la logica del validador correspondiente a la accion semantica que implemente la clase.
				if (validador != null)
					if (!validador.validar(this.analizadorLexico, this.tipotoken))
						validador.finalizar(this.analizadorLexico);
			}
			else
				//Si estoy en un estado intermedio solo debo concatenar
				this.analizadorLexico.getLexemaParcial().append(this.analizadorLexico.getCharActual().toString());
		}
	}

}
