package compilador;

/**
 * Clase AnalizadorLexico
 * 
 * @author ileitao
 * @author gmaiola
 */
public class AnalizadorLexico {
    
    private static final int ESTADO_INICIAL = 0;
    
    private static final int ESTADO_FINAL = -1;
    
    private int estadoActual = ESTADO_INICIAL;
    
    private int lineaActual = 1;
    
    public void setEstadoActual(int estadoActual)
    {
        this.estadoActual = estadoActual;
    }
    
    public int getEstadoActual()
    {
        return this.estadoActual;
    }
    
    public void setLineaActual(int lineaActual)
    {
        this.lineaActual = lineaActual;
    }
    
    public int getLineaActual()
    {
        return this.lineaActual;
    }
    
}
