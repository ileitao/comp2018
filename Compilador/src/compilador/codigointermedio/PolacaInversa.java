package compilador.codigointermedio;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import compilador.Token;
import compilador.analizadorsintactico.Parser;

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
	
	/**
	 * La sentencia FOR sera generada con el equivalente de:
	 * 		1) asignacion de inicializacion del iterador (3 pasos)
	 * 		2) codigo intermedio de while
	 * 		3) asignacion para incrementar iterador
	 * 
	 * Apila posicion de comienzo de la condicion.
	 */
	public void generarInicioCondicionFOR() {
		//El salto atras BI debe apuntar al primer paso de la condicion
		//Por eso se agrega un offset de 3 pasos correspondiente a la inicializacion del iterador
		this.pasosIncompletos.push(this.codigoGenerado.size() + 3);
	}
	
	public void generarBloqueFOR(Token acumulador, Token iterador) {
		
		//genero codigo intermedio para incremento de iterador
		generarIncrementoFOR(acumulador, iterador);
		
		//Se completa paso incompleto a donde deberia saltar en caso de que la
		//condicion fuera falsa.
		completarPasoIncompleto(2);
		
		//Recupero paso de inicio de condicion del FOR para generar el salto atras BI
		int indice_destino = this.pasosIncompletos.pop();
		
		//Genera el salto atras BI hacia el paso recuperado previamente
		generarBifurcacion("BI");
		
		//Recupero el paso geneardo en la linea anterior y lo completo
		int completar = this.pasosIncompletos.pop();
		this.codigoGenerado.get(completar).setElemento(String.valueOf(indice_destino));
	}
	
	/**
	 * Genera codigo intermedio para la asignacion de incremento del iterador del FOR
	 * @param iterador Token variable utilizado como iterador en la sentencia FOR
	 * @param acumulador Token con el valor de incremento para acumular
	 */
	private void generarIncrementoFOR(Token acumulador, Token iterador) {
		
		//Genero asignacion: iterador := iterador + acumulador
		//Codigo intermedio: ( iterador, acumulador, +, iterador, := )
		this.codigoGenerado.add( new ElementoPI(iterador.getLexema(), iterador));
		this.codigoGenerado.add( new ElementoPI(acumulador.getLexema(), acumulador));
		this.codigoGenerado.add( new ElementoPI("+", new Token("+", Parser._PLUS)));
		this.codigoGenerado.add( new ElementoPI(iterador.getLexema(), iterador));
		this.codigoGenerado.add( new ElementoPI(":=", new Token(":=", Parser._ASSIGN)));
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
