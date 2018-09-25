package compilador.accionsemantica;

import compilador.AnalizadorLexico;

/**
 * Clase abstracta AccionSemantica
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public abstract class AccionSemantica {

	public static final int AS_INICIALIZAR_LEXEMA = 1;
	public static final int AS_CONCATENAR_LEXEMA = 2;
	
	public static final int AS_TIPO_TOKEN_OPERADOR = 3;
	
	//valida longitud identificador
	public static final int AS_TIPO_TOKEN_IDENTIFICADOR = 4;
	
	//valida numero y descarta postfijo
	public static final int AS_TIPO_TOKEN_ENTERO_SIN_SIGNO = 5;
	//valida numero y descarta postfijo
	public static final int AS_TIPO_TOKEN_FLOTANTE = 6;
	//valida posible palabra reservada
	public static final int AS_TIPO_TOKEN_PALABRA_RESERVADA = 7;
	
	public static final int AS_TIPO_TOKEN_CADENA_CARACTERES = 8;
	

	
	public static final int AS_DESCARTAR_TOKEN = 18;
	//no realiza ninguna accion
	public static final int AS_NO_ACCION = 19;
	
	public static final int AS_ERROR = 20;
	
	protected AnalizadorLexico analizadorLexico;

	public AccionSemantica(AnalizadorLexico analizadorLexico) {
		this.analizadorLexico = analizadorLexico;
	}

	/**
	 * Realiza la verificacion correspondiente de la accion semantica y ejecuta la
	 * el metodo de correccion para la misma en caso de ser necesario.
	 */
	public abstract void execute();

}
