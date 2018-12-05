package compilador.codigointermedio;

import java.util.ArrayList;
import java.util.List;

public class PolacaInversa {

	//La posicion del elemento en la lista indica la direccion
	//del codigo generado, al cual se hara referencia en las bifurcaciones.
	private List<ElementoPI> codigoGenerado = new ArrayList<>();
	
	public PolacaInversa() {
	}

	/**
	 * Genera un nuevo elemento y agregandolo a la lista de codigo intermedio generado.
	 * @param elementoPI
	 */
	public void addElemento(ElementoPI elementoPI) {
		this.codigoGenerado.add(elementoPI);
	}
	
	public void imprimir() {
		
		System.out.println("**** POLACA INVERSA ****");
		for(int i = 0; i < codigoGenerado.size() ; i++)
			System.out.println(i + " " + codigoGenerado.get(i).getElemento());
	}
}
