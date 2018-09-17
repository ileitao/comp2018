package compilador.accionsemantica;

import static compilador.log.EventoLog.ERROR;
import static compilador.log.EventoLog.WARNING;
import static compilador.log.MensajesEventos.FLOTANTE_FUERA_DE_RANGO;
import static compilador.log.MensajesEventos.TOKEN_INESPERADO;

import compilador.AnalizadorLexico;
import compilador.log.EventoLog;

/**
 * Clase AccionSemantica03ValidarFlotante
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public class AccionSemantica03ValidarFlotante extends AccionSemantica {

	private static final int ESTADO_VERIFICACION_FUERA_DE_RANGO 	= 1;
	private static final int ESTADO_VERIFICACION_CARACTER_INVALIDO 	= 2;
	private static final String MAXIMO_VALOR_FLOTANTE = "999.F999";
	
    public AccionSemantica03ValidarFlotante(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

	@Override
	protected boolean verificar() {

		//Obtener linea para finalidades de loggeo
        int linea = this.analizadorLexico.getLineaActual();
        //Descartar "cualquier" caracter final acompañando al lexema
//        this.analizadorLexico.retrocederLectura();
//        this.analizadorLexico.setLexemaParcial(this.analizadorLexico.getLexemaParcial().substring(0, this.analizadorLexico.getLexemaParcial().length() - 1));
        //Chequear el rango
        try {
            float lexemaEvaluado = Float.parseFloat(this.analizadorLexico.getLexemaParcial());
            float maximoFlotante = Float.MAX_VALUE;
            float minimoFlotante = Float.MIN_VALUE;
            //Si está dentro del rango
            if ((lexemaEvaluado >= minimoFlotante) && (lexemaEvaluado <= maximoFlotante)) {
                this.analizadorLexico.setLexemaParcial(Float.toString(lexemaEvaluado));
                //TODO acá sería necesario hacer algo más...
                
                return true;
            } else
       		 	//TODO Loggear WARNING por salirse del rango, informar linea, setear el valor máximo
            	this.analizadorLexico.getLogger().log(new EventoLog(FLOTANTE_FUERA_DE_RANGO, WARNING, linea));
            	this.estadoVerificacion = ESTADO_VERIFICACION_FUERA_DE_RANGO;
            	return false;
        }
        catch (NumberFormatException e) {
        	//ESTO NO DEBERIA SUCEDER, LO DEBEMOS CONTROLAR PARA QUE NO SE LANCE LA EXCEPCION!
        	this.estadoVerificacion = ESTADO_VERIFICACION_CARACTER_INVALIDO;
        	this.analizadorLexico.getLogger().log(new EventoLog(TOKEN_INESPERADO, ERROR, linea));
            return false;
        }
	}
	
	@Override
	protected void corregir() {
		
		switch (this.estadoVerificacion) {
		case ESTADO_VERIFICACION_FUERA_DE_RANGO:
			this.analizadorLexico.setLexemaParcial(MAXIMO_VALOR_FLOTANTE);
			break;
		case ESTADO_VERIFICACION_CARACTER_INVALIDO:
		default:
			//ACCION A REALIZAR EN CASO DE QUE LLEGUE ALGO QUE NO SEA UN DIGITO.
//			System.out.println("ACCION SEMANTICA FLOTANTE: SE RECIBIO ALGO QUE NO ERA UN DIGITO");
			break;
		}
	}
    
}
