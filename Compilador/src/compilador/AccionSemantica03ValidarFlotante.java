package compilador;

/**
 * Clase AccionSemantica03ValidarFlotante
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public class AccionSemantica03ValidarFlotante extends AccionSemantica {

    public AccionSemantica03ValidarFlotante(AnalizadorLexico analizadorLexico) {
        super(analizadorLexico);
    }

    @Override
    public void execute() {
        //Obtener linea para finalidades de loggeo
        int line = this.analizadorLexico.getLineaActual();
        //Descartar "cualquier" caracter final acompañando al lexema
        this.analizadorLexico.retrocederLectura();
        this.analizadorLexico.setLexemaParcial(this.analizadorLexico.getLexemaParcial().substring(this.analizadorLexico.getLexemaParcial().length() - 1));
        //Chequear el rango
        try {
            float lexemaEvaluado = Float.parseFloat(this.analizadorLexico.getLexemaParcial());
            float maximoFlotante = Float.MAX_VALUE;
            float minimoFlotante = Float.MIN_VALUE;
            //Si está dentro del rango
            if ((lexemaEvaluado >= minimoFlotante) && (lexemaEvaluado <= maximoFlotante)) {
                this.analizadorLexico.setLexemaParcial(Float.toString(lexemaEvaluado));
                //TODO acá sería necesario hacer algo más...
            } else {
                //TODO Loggear WARNING por salirse del rango, informar linea, setear el valor máximo
                this.analizadorLexico.setLexemaParcial(Float.toString(maximoFlotante));
            }
        }
        catch (NumberFormatException e) {
            //TODO catch
        }
    }
    
}
