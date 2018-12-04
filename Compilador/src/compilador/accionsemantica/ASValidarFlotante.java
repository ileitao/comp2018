package compilador.accionsemantica;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
import compilador.analizadorsintactico.Parser;
import compilador.log.EventoLog;
import compilador.log.Logger;

public class ASValidarFlotante extends AccionSemantica {

	//Se definen como double para que alcance la precision de decimales
	//1,17549435E-38 < x < 3,40282347E38 (incluir el 0,0)
	private static final double MANTISA_MIN = 1.17549435;
	private static final double MANTISA_MAX = 3.40282347;
	private static final int EXP_MIN = -38;
	private static final int EXP_MAX = 38;
	
	public ASValidarFlotante(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}
	
	public static boolean validar(String lexema) {
		
		String[] lexemaSplit = lexema.split("F");
		
		String mantisa = lexemaSplit[0];
		String exp = "0";
		
		if (lexemaSplit.length > 1)
			exp = lexemaSplit[1];
		
		//Si no tiene exponente solo se compara la mantisa
		if (exp.equals("0"))
			if (Double.valueOf(mantisa) <= MANTISA_MIN || Double.valueOf(mantisa) >= MANTISA_MAX)
				return false;
		
		double numLexema = Double.valueOf(mantisa) * Math.pow(10, Double.valueOf(exp));
		double numRangoMin = MANTISA_MIN * Math.pow(10, EXP_MIN);
		double numRangoMax = MANTISA_MAX * Math.pow(10, EXP_MAX); 
		
		if (numLexema <= numRangoMin || numLexema >= numRangoMax)
			return false;
		
		//Se encuentra dentro del rango permitido
		return true;
	}

	public static String truncar(Logger logger, String lexema, int linea, int posicion) {
		
		logger.log(
				new EventoLog("El flotante " +  lexema + " no se encuentra dentro de rango permitido."
								+ " Sera truncado al maximo valor permitido 3.40282347F38",
								EventoLog.WARNING, linea, posicion
							)
				);
		return MANTISA_MAX + "F" + EXP_MAX;
	}

	@Override
	public void execute() {
		
		aLexico.setCodigoTokenReconocido(Parser._CONSTANT_SINGLE);
		aLexico.setTipoToken(TipoToken.CONSTANTE_FLOTANTE);
		//Se retrocede el lector para volver a leer el ultimo caracter leido.
		aLexico.retrocederLectura();
		
		Logger logger = aLexico.getLogger();
		String lexema = aLexico.getLexemaParcial().toString();
		int lineaActual = aLexico.getLineaActual();
		int posicion = aLexico.getPunteroActual();
		
		if (!validar(aLexico.getLexemaParcial().toString()))
			aLexico.setLexemaParcial(truncar(logger, lexema, lineaActual, posicion));
	}

}
