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
 */
public class TablaDeSimbolos {

    private HashMap<Integer, Simbolo> tablaDeSimbolos;
    
    public TablaDeSimbolos() {
        this.tablaDeSimbolos = new HashMap<Integer, Simbolo>();
    }
    
    public HashMap<Integer, Simbolo> getTablaDeSimbolos() {
        return this.tablaDeSimbolos;
    }
    
    public void agregarSimbolo(Simbolo simbolo) {
        
    }
    
    public Simbolo getSimbolo() {
        return new Simbolo();
    }
    
    /**
     * Imprime la tabla de simbolos
     */
    public void imprimirTablaDeSimbolos() {
        
    }
}
