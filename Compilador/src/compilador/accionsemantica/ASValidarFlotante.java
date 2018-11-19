package compilador.accionsemantica;

import compilador.AnalizadorLexico;
import compilador.analizadorsintactico.Parser;
import compilador.log.EventoLog;

public class ASValidarFlotante extends AccionSemantica {

	//Se definen como double para que alcance la precision de decimales
	//1,17549435E-38 < x < 3,40282347E38 (incluir el 0,0)
	private final double MANTISA_MIN = 1.17549435;
	private final double MANTISA_MAX = 3.40282347;
	private final int EXP_MIN = -38;
	private final int EXP_MAX = 38;
	
	public ASValidarFlotante(AnalizadorLexico analizadorLexico) {
		super(analizadorLexico);
	}
	
	public boolean validar() {
		
		String[] lexema = aLexico.getLexemaParcial().toString().split("F");
		
		String mantisa = lexema[0];
		String exp = "0";
		
		if (lexema.length > 1)
			exp = lexema[1];
		
		//Si no tiene exponente solo se compara la mantisa
		if (exp.equals("0"))
			if (Double.valueOf(mantisa) <= MANTISA_MIN || Double.valueOf(mantisa) >= MANTISA_MAX)
				return false;
		
		//Se valida MIN
		if (Double.valueOf(mantisa) <= MANTISA_MIN && Double.valueOf(exp) <= EXP_MIN)
			return false;
		
		//Se valida MAX
		if (Double.valueOf(mantisa) >= MANTISA_MAX && Double.valueOf(exp) >= EXP_MAX)
			return false;
		
		//Se encuentra dentro del rango permitido
		return true;
	}

	public void finalizar() {
		
		aLexico.getLogger().log(
				new EventoLog("El flotante " +  aLexico.getLexemaParcial().toString() + " no se encuentra dentro de rango permitido."
								+ " Sera truncado al maximo valor permitido 3.40282347F38",
								EventoLog.WARNING, aLexico.getLineaActual(), aLexico.getPunteroActual()
							)
				);
		aLexico.setLexemaParcial(MANTISA_MAX + "F" + EXP_MAX);
	}

	@Override
	public void execute() {
		
		aLexico.setCodigoTokenReconocido(Parser._CONSTANT_SINGLE);
		
		//Se retrocede el lector para volver a leer el ultimo caracter leido.
		aLexico.retrocederLectura();
		
		if (!validar())
			finalizar();
	}

}
