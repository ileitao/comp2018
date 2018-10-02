package compilador.accionsemantica;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
import compilador.accionsemantica.general.ASConcatenarLexema;
import compilador.accionsemantica.general.ASDescartarToken;
import compilador.accionsemantica.general.ASError;
import compilador.accionsemantica.general.ASInicializarLexema;
import compilador.accionsemantica.general.ASNoAccion;
import compilador.accionsemantica.general.ASRetroceder;
import compilador.accionsemantica.validacion.ASComentario;
import compilador.accionsemantica.validacion.ASComparador;
import compilador.accionsemantica.validacion.ASOperador;
import compilador.accionsemantica.validacion.ASReconocerToken;
import compilador.accionsemantica.validacion.ASValidarCadenaCarateres;
import compilador.accionsemantica.validacion.ASValidarEnteroSinSigno;
import compilador.accionsemantica.validacion.ASValidarFlotante;
import compilador.accionsemantica.validacion.ASValidarIdentificador;
import compilador.accionsemantica.validacion.ASValidarPalabraReservada;

public class ASFactory {

	private AnalizadorLexico aLexico;

	public ASFactory(AnalizadorLexico aLexico) {
		this.aLexico = aLexico;
	}

	public AccionSemantica getInstancia(ASTipos tipoAS) {

		switch (tipoAS) {
		
		//ACCIONES SEMANTICAS DE ACCION
		case AS_NO_ACCION:
			return new ASNoAccion(aLexico);
		case AS_INICIALIZAR_LEXEMA:
			return new ASInicializarLexema(aLexico);
		case AS_CONCATENAR_LEXEMA:
			return new ASConcatenarLexema(aLexico);
		case AS_ERROR:
			return new ASError(aLexico);
		case AS_DESCARTAR_TOKEN:
			return new ASDescartarToken(aLexico);
		case AS_RETROCEDER:
			return new ASRetroceder(aLexico);
			
		//ACCIONES SEMANTICAS DE RECONOCIMIENTO
		case AS_TOKEN_IDENTIFICADOR:
			return new ASReconocerToken(aLexico, TipoToken.IDENTIFICADOR, new ASValidarIdentificador());
		case AS_TOKEN_ENTERO_SIN_SIGNO:
			return new ASReconocerToken(aLexico, TipoToken.CONSTANTE_ENTERO_SIN_SIGNO, new ASValidarEnteroSinSigno());
		case AS_TOKEN_FLOTANTE:
			return new ASReconocerToken(aLexico, TipoToken.CONSTANTE_FLOTANTE, new ASValidarFlotante());
		case AS_TOKEN_OPERADOR_ARITMETICO:
			return new ASReconocerToken(aLexico, TipoToken.OPERADOR_ARITMETICO, new ASOperador());
		case AS_TOKEN_OPERADOR_ASIGNACION:
			return new ASReconocerToken(aLexico, TipoToken.OPERADOR_ASIGNACION, new ASOperador());		
		case AS_TOKEN_COMPARADOR:
			return new ASReconocerToken(aLexico, TipoToken.COMPARADOR, new ASComparador());		
		case AS_TOKEN_CADENA_CARACTERES:
			return new ASReconocerToken(aLexico, TipoToken.CADENA_CARACTERES, new ASValidarCadenaCarateres());
		case AS_TOKEN_PALABRA_RESERVADA:
			return new ASReconocerToken(aLexico, TipoToken.PALABRA_RESERVADA, new ASValidarPalabraReservada());
		case AS_TOKEN_DELIMITADOR:
			return new ASReconocerToken(aLexico, TipoToken.DELIMITADOR, null);
		case AS_TOKEN_COMENTARIO:
			return new ASReconocerToken(aLexico, TipoToken.COMENTARIO, new ASComentario());
		case AS_TOKEN_OTRO:
			return new ASReconocerToken(aLexico, TipoToken.OTRO, null);	
			
		default:
			return null;
		}
	}

}
