package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
import compilador.accionsemantica.AccionSemantica;

public abstract class ASValidarToken extends AccionSemantica {

	//Estado resultante de la verificacion, el cual será seteado por cada clase concreta que extienda de esta clase.
	protected int estadoVerificacion;
	
	//Tipo de token reconocido. Es seteado por la clase que extiende.
	protected TipoToken tipotoken;
	
	public ASValidarToken(AnalizadorLexico analizadorLexico, TipoToken tipoToken) {
		super(analizadorLexico);
		this.tipotoken = tipoToken;
	}

	/**
	 * Este metodo es el que se presenta para ejecutar la accion semantica.
	 * Internamente realiza validaciones y ejecuta acciones correctivas segun corresponda.
	 * Cada clase concreta que implemente a esta clase debera implementar los metodos validar() y hacer()
	 * 
	 * Implementa el metodo execute de la superclase.
	 * Setea el tipo de token reconocido, realiza validaciones y acciones segun corresponda.
	 */
	@Override
	public void execute() {
		//seteo el tipo de token reconocido
		this.analizadorLexico.setTipoToken(this.tipotoken);
		
		if(!validar())
			hacer();
	}
	
	/**
	 * Realiza las verificaciones correspondientes al tipo de token que corresponda.
	 * Ademas setea un valor de estado de verificacion, el cual sera usado posteriormente por el metodo corregir()
	 * para poder determinar que accion debe tomar.
	 * 
	 * @return true si supera todas las validaciones, en otro caso false.
	 */
	protected abstract boolean validar();
	
	/**
	 * Realiza una accion dependiente del resultado del metodo validar.
	 */
	protected abstract void hacer();

}
