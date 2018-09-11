package compilador;

import java.util.HashMap;

/**
 * Clase TablaDeSimbolos
 * 
 * Estructura de datos que contiene un registro para
 * cada identificador utilizado en el código fuente.
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public class TablaDeSimbolos {

    private HashMap<Token, RegTablaSimbolos> tablaDeSimbolos;
    
    public TablaDeSimbolos() {
        this.tablaDeSimbolos = new HashMap<Token, RegTablaSimbolos>();
    }
    
    public HashMap<Token, RegTablaSimbolos> getTablaDeSimbolos() {
        return this.tablaDeSimbolos;
    }
    
    public void agregarSimbolo(RegTablaSimbolos regSimbolo) {
        this.tablaDeSimbolos.put(regSimbolo.getToken(), regSimbolo);
    }
    
    public RegTablaSimbolos getRegistro(Token token) {
        return this.tablaDeSimbolos.get(token);
    }
    
    /**
     * Imprime la tabla de simbolos
     */
    public void imprimirTablaDeSimbolos() {
        
    }
}
