package compilador.accionsemantica;

public enum ASTipos {

	AS_INICIALIZAR_LEXEMA,
	AS_CONCATENAR_LEXEMA,
	
	//valida longitud identificador
	AS_TOKEN_IDENTIFICADOR,
	
	//valida numero y descarta postfijo
	AS_TOKEN_ENTERO_SIN_SIGNO,
	//valida numero y descarta postfijo
	AS_TOKEN_FLOTANTE,
	
	AS_TOKEN_OPERADOR_ARITMETICO,
	
	AS_TOKEN_OPERADOR_ASIGNACION,
	
	AS_TOKEN_COMPARADOR,
	
	//valida posible palabra reservada
	AS_TOKEN_PALABRA_RESERVADA,
	
	AS_TOKEN_CADENA_CARACTERES,
	
	AS_TOKEN_OTRO,

	AS_RETROCEDER,
	
	AS_DESCARTAR_TOKEN,
	//no realiza ninguna accion
	AS_NO_ACCION,
	
	AS_ERROR,
	AS_TOKEN_DELIMITADOR,
	AS_TOKEN_COMENTARIO
	
}
