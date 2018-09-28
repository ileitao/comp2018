package compilador.accionsemantica.validacion;

import compilador.AnalizadorLexico;
import compilador.TipoToken;
import compilador.log.EventoLog;

public class ASValidarFlotante implements Validable {

	//Se definen como double para que alcance la precision de decimales
	//1,17549435E-38 < x < 3,40282347E38 (incluir el 0,0)
	private final double MANTISA_MIN = 1.17549435;
	private final double MANTISA_MAX = 3.40282347;
	private final int EXP_MIN = -38;
	private final int EXP_MAX = 38;
	
	public ASValidarFlotante() {
	}

	@Override
	public boolean evaluar(AnalizadorLexico aLexico, TipoToken tipoToken) {
		
		//Se retrocede el lector para volver a leer el ultimo caracter leido.
		aLexico.retrocederLectura();
		
		String[] lexema = aLexico.getLexemaParcial().toString().split("F");
		
		String mantisa = lexema[0];
		String exp = "0";
		
		if (mantisa.equals(".")) {
			aLexico.getLogger().log(new EventoLog("Token . inesperado", EventoLog.ERROR, aLexico.getLineaActual()));
			aLexico.reiniciar();
			return true;
		}
		
		if (lexema.length > 1)
			exp = lexema[1];
			
		//Se valida MIN
		if (Double.valueOf(mantisa) <= MANTISA_MIN && Double.valueOf(exp) <= EXP_MIN)
			return false;
		
		//Se valida MAX
		if (Double.valueOf(mantisa) >= MANTISA_MAX && Double.valueOf(exp) >= EXP_MAX)
			return false;
		
		//Se encuentra dentro del rango permitido
		return true;
	}

	@Override
	public void procesar(AnalizadorLexico aLexico) {
		
		aLexico.getLogger().log(
				new EventoLog("El flotante no se encuentra dentro de rango permitido."
								+ " Sera truncado al maximo valor permitido 3.40282347F38",
								EventoLog.WARNING, aLexico.getLineaActual()
							)
				);
		aLexico.setLexemaParcial(MANTISA_MAX + "F" + EXP_MAX);
	}

}
