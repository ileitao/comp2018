package compilador;

/**
 * Clase Compilador
 * 
 * @author ileitao
 * @author gmaiola
 */
public class Compilador {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String rutaDelArchivo = "codigo.txt";
        if (args.length != 0) {
            rutaDelArchivo = (String)args[0];
        }
        
        LectorDeArchivo lector = new LectorDeArchivo(rutaDelArchivo);
    }
}
