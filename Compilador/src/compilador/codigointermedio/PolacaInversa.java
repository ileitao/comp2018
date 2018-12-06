package compilador.codigointermedio;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PolacaInversa {

	//La posicion del elemento en la lista indica la direccion
	//del codigo generado, al cual se hara referencia en las bifurcaciones.
	private List<ElementoPI> codigoGenerado = new ArrayList<>();
	
	//Se utiliza para ir guardando las posiciones de los pasos a completar para las bifurcaciones.
	private Stack<Integer> pasosIncompletos = new Stack<>();
	
	public PolacaInversa() {
	}

	/**
	 * Genera un nuevo elemento y agregandolo a la lista de codigo intermedio generado.
	 * @param elementoPI
	 */
	public void addElemento(ElementoPI elementoPI) {
		this.codigoGenerado.add(elementoPI);
	}
	
	/**
	 * Genera codigo intermedio para una bifurcacion
	 * Pasos:
	 * 		1) Genera un elemento incompleto
	 * 		2) Un elemento de bifurcacion BF
	 * 		3) Apila posicion actual como paso incompleto
	 * 
	 * @param tipoBifurcacion Codigo de bifurcacion: BF / BI
	 */
	public void generarBifurcacion(String tipoBifurcacion) {
		
		//Se genera elemento incompleto
		ElementoPI elemIncompleto = new ElementoPI("", null);
		this.codigoGenerado.add(elemIncompleto);
		
		//Se apila posicion actual como paso incompleto
		this.pasosIncompletos.push( this.codigoGenerado.size() -1 );
		
		//Se genera elemento BF
		ElementoPI elemBF = new ElementoPI(tipoBifurcacion, null);
		this.codigoGenerado.add(elemBF);
	}
	
	/**
	 * Genera codigo intermedio para completar el paso incompleto (tope pila) al cual deberia saltar
	 * en caso de que la condicion de la sentencia de control fuera falsa.
	 * Se utiliza para los casos de finalizacion para:
	 * 		IF (condicion) { sentencias }
	 * 		IF (condicion) { sentencias } ELSE { sentencias }
	 * 
	 * Tambien se reutiliza para otros casos en las sentencias de control.
	 * 
	 * @param offsetPasosDestino Numero de pasos (offset) desde el ultimo pas generado hasta el paso en que deberia continuar.
	 */
	public void completarPasoIncompleto(int offsetPasosDestino) {
		int indice_a_completar = this.pasosIncompletos.pop();
		int indice_destino = this.codigoGenerado.size() + offsetPasosDestino;
//		a_marcar.push(indice_destino);
		
		this.codigoGenerado.get(indice_a_completar).setElemento(Integer.toString(indice_destino));
	}
	
	/**
	 * Genera codigo intermedio para
	 */
	public void generarElse() {
		
		//Se completa paso incompleto a donde deberia saltar en caso de que la
		//condicion fuera falsa.
		completarPasoIncompleto(2);
		
		//Se genera codigo intermedio para saltar rama ELSE
		generarBifurcacion("BI");

	}
	
	public void despuesBloqueWhile() {
		
		//Se completa paso incompleto a donde deberia saltar en caso de que la
		//condicion fuera falsa.
		completarPasoIncompleto(2);
		
//		indice_destino = pila.pop();
//		a_marcar.push(indice_destino);
//		e = new Elemento(Integer.toString(indice_destino), -1);
//		codigo.add(e);
//		
//		e = new Elemento("BI", -1);
//		codigo.add(e);

	}
	
	/**
	 * Imprime una representacion del codigo intermedio generado de manera legible
	 */
	public void imprimir() {
		
		System.out.println("**** POLACA INVERSA ****");
		for(int i = 0; i < codigoGenerado.size() ; i++)
			System.out.println(i + " " + codigoGenerado.get(i).getElemento());
	}
}
