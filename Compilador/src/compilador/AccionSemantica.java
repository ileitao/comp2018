package compilador;

/**
 * Clase abstracta AccionSemantica
 * 
 * @author ileitao
 * @author gmaiola
 */
public abstract class AccionSemantica {
    
    AnalizadorLexico analizadorLexico;
    
    public AccionSemantica(AnalizadorLexico analizadorLexico){
        this.analizadorLexico = analizadorLexico;
    }
    
    public abstract void execute();
}
