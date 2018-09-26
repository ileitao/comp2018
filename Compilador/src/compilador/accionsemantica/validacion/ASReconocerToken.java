package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
import compilador.accionsemantica.AccionSemantica;

public class ASReconocerToken extends AccionSemantica {

	//Estado resultante de la verificacion, el cual será seteado por cada clase concreta que extienda de esta clase.
	protected int estadoVerificacion;
	
	//Tipo de token reconocido. Es seteado por la clase que extiende.
	protected TipoToken tipotoken;
	
	//Realiza las verificaciones necesarias para un tipo de token.
	private Validable validador;
	
	public ASReconocerToken(AnalizadorLexico aLexico, TipoToken tipoToken, Validable validador) {
		super(aLexico);
		this.tipotoken = tipoToken;
		this.validador = validador;
	}

	/**
	 * Este metodo es el que se presenta para ejecutar la accion semantica.
	 * Internamente realiza validaciones y ejecuta acciones correctivas segun corresponda.
	 * 
	 * Implementa el metodo execute de la superclase.
	 * Setea el tipo de token reconocido, realiza validaciones y acciones segun corresponda.
	 */
	@Override
	public void execute() {
		//seteo el tipo de token reconocido
		this.analizadorLexico.setTipoToken(this.tipotoken);
		
		//
		if (validador != null)
			if(!validador.evaluar(this.analizadorLexico, this.tipotoken))
				validador.procesar(this.analizadorLexico);
	}

}
