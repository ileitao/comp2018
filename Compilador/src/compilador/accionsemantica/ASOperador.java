package compilador.accionsemantica;

import compilador.AnalizadorLexico;
import compilador.analizadorsintactico.Parser;

/**
 * 
 * @author leandro
 *
 */
public class ASOperador extends AccionSemantica {

	public ASOperador(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}

	/**
	 * Setea codigo de operador
	 */
	@Override
	public void execute() {
		
		aLexico.retrocederLectura();
		
		short codigoToken = 0;
		
		switch (aLexico.getLexemaParcial().toString()) {
		case "+":
			codigoToken = Parser._PLUS;
			break;
		case "-":
			codigoToken = Parser._MINUS;
			break;
		case "*":
			codigoToken = Parser._MULT;
			break;
		case "/":
			codigoToken = Parser._DIV;
			break;
		case "=":
			codigoToken = Parser._EQUAL;
			break;
		case "<":
			codigoToken = Parser._LESSER;
			break;
		case "<=":
			codigoToken = Parser._LESSER_OR_EQUAL;
			break;
		case ">":
			codigoToken = Parser._GREATER;
			break;
		case ">=":
			codigoToken = Parser._GREATER_OR_EQUAL;
			break;
		case "!=":
			codigoToken = Parser._UNEQUAL;
			break;
		case ":=":
			codigoToken = Parser._ASSIGN;
			break;
		case ";":
			codigoToken = Parser._SEMICOLON;
			break;
		case ",":
			codigoToken = Parser._COMMA;
			break;
		case "(":
			codigoToken = Parser._LPAREN;
			break;
		case ")":
			codigoToken = Parser._RPAREN;
			break;
		case "{":
			codigoToken = Parser._LCBRACE;
			break;
		case "}":
			codigoToken = Parser._RCBRACE;
			break;
		default:
			codigoToken = -1;
			break;
		}
		
		aLexico.setCodigoTokenReconocido(codigoToken);
	}

}
