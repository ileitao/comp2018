package compilador;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.InputMismatchException;
import java.util.Scanner;

import compilador.analizadorsintactico.Parser;

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
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        String rutaDelArchivo;
        
        //archivo por defecto
        rutaDelArchivo = "codigo-para-el-sintactico.txt";
        
        //En caso de pasarse, se usa el archivo pasado como argumento
        if (args.length != 0) {
            rutaDelArchivo = (String)args[0];
        }  
        
        LectorDeArchivo lector = new LectorDeArchivo(rutaDelArchivo);
        AnalizadorLexico analizadorLexico = new AnalizadorLexico(lector);
        
        int opcionMenu = -1;
        
//        opcionMenu = menu();
        opcionMenu = 2;
        
        switch (opcionMenu) {
        case 1:
        	testAnalizadorLexico(analizadorLexico);
        	break;
        case 2:
	        Parser p = new Parser(analizadorLexico, analizadorLexico.getTablaSimbolos());
	        p.Run();
	        break;
        default:
        	System.out.println("Opcion invalida");
        }
        
//        testCoverage();
    }

	private static void testAnalizadorLexico(AnalizadorLexico analizadorLexico) {

		Token token = analizadorLexico.getToken();
		
		while(token != null) {
//			System.out.println(token.toString());
			token = analizadorLexico.getToken();
		}
		
		System.out.println("************** TIRA DE TOKENS **************");
		
		analizadorLexico.getTiraTokens().forEach( t -> System.out.println(t));
		System.out.println("Cant. Tokens detectados: " + analizadorLexico.getTiraTokens().size());
		System.out.println();
		
		System.out.println("************** Logger ************** ");
		analizadorLexico.getLogger().imprimir();
		System.out.println();
		
		System.out.println("************** TABLA DE SIMBOLOS **************");
		analizadorLexico.getTablaSimbolos().imprimirTablaDeSimbolos();
		
		
	}

    private static void testLectorArchivos(LectorDeArchivo lector) throws IOException {
	//// Probando que lector de archivos funciona correctamente
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
        //lector.retrocederPuntero();
        System.out.println(lector.getLineaActual());
        System.out.println(lector.leerChar());
        //// TablaDeSimbolos tablaDeSimbolos = new TablaDeSimbolos();
        //// LectorDeArchivo lectorDeArchivo = new LectorDeArchivo(rutaDelAchivo);
        //// AnalizadorLexico analizadorLexico = new AnalizadorLexico(lectorDeArchivo, tablaDeSimbolos);
    }
    
    public static int menu() throws IOException {
    	Scanner scanner = new Scanner(System.in);
    	int seleccion = -1;
	    	
    	System.out.println("Ingrese el numero de opcion correspondiente a la funcionalidad que desee ejecutar");
    	System.out.println("1: Analizador Lexico");
    	System.out.println("2: Analizador Sintactico");
		try {
			seleccion = scanner.nextInt();
			
			if (seleccion != 1 && seleccion != 2)
				System.out.println("\nDebe ingresar un numero de las opciones!");
			
		} catch (InputMismatchException e) {
			System.out.println("\nDebe ingresar un numero de las opciones!");
		}
    	finally {
    		scanner.close();
    	}

    	return seleccion;    	    	
    }
    
    public static void testCoverage() throws IOException {
    	
    	File directory = new File("/home/leandro/Desktop/test-coverage");
    	
    	Files.list(directory.toPath())
    		.sorted((f1, f2) -> f1.compareTo(f2))
    		.forEach( f -> {
    			System.out.println("-*-*-*-*-*-*-* FILE: " + f.getFileName());
    			AnalizadorLexico aLexico = new AnalizadorLexico(new LectorDeArchivo(f.toAbsolutePath().toString()));
    	    	Parser parser = new Parser(aLexico, aLexico.getTablaSimbolos());
    			try {
					parser.Run();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		});
    }
}
