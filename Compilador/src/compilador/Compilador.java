package compilador;

import java.io.IOException;

/**
 * Clase Compilador
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public class Compilador {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String rutaDelArchivo = "codigo.txt";
        if (args.length != 0) {
            rutaDelArchivo = (String)args[0];
        }
        
        //// Probando que lector de archivos funciona correctamente
        LectorDeArchivo lector = new LectorDeArchivo(rutaDelArchivo);
        System.out.println(lector.getLineaActual());
        lector.leerLinea();
        System.out.println(lector.getLineaActual());
        
        lector.inicializar();
        for (int i = 0; i < 50; i++) {
            lector.leerChar();
        }
        
        lector.retrocederPuntero();
        lector.retrocederPuntero();
        lector.retrocederPuntero();
        lector.retrocederPuntero();
        lector.retrocederPuntero();
//        lector.retrocederPuntero();
        System.out.println(lector.getLineaActual());
        System.out.println(lector.leerChar());
        
        //// TablaDeSimbolos tablaDeSimbolos = new TablaDeSimbolos();
        //// LectorDeArchivo lectorDeArchivo = new LectorDeArchivo(rutaDelAchivo);
        //// AnalizadorLexico analizadorLexico = new AnalizadorLexico(lectorDeArchivo, tablaDeSimbolos);
    }
}
