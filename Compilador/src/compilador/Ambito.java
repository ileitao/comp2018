package compilador;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 *
 * Clase autocontenida que representa un espacio de nombres llamado ambito.
 * Almacena los ambitos en una pila para manejar el manejo de estos de manera anidada.
 * Se guardara una referencia en la clase RegTabSimbolos para los identificadores.
 * 
 * @author leandro
 *
 */
public class Ambito {

	// Nombre del ambito main
	private static final String MAIN = "main";
	
	// Se utiliza una pila para insertar y extraer el ultimo ambito segun se vayan anidando funciones.
	private static Stack<Ambito> pilaAmbitos = new Stack<>();
	
	// Lista de nombres de ambitos anidados
	private List<String> nombresAmbitos = new ArrayList<>();
	
	//El tipo de funcion que genero el ambito
	private Token tokenTipo;
	private Token tokenIdentificador;
	
	// Inicializo pila con ambito main.
	private Ambito(){
		this.nombresAmbitos.add(MAIN);
	}
	
	/**
	 * Se utiliza un constructor privado para manejar las instancias en
	 * el metodo privado nuevoAmbito.
	 * Un nuevo ambito es el resultado del actual mas el recibido por parametro.
	 * @param tokenTipoFuncion Tipo de funcion que genera el nuevo ambito
	 * @param nuevoAmbito Nombre de la funcion para el nuevo ambito
	 */
	private Ambito(Token tokenTipoFuncion, Token tokenIdentificador) {
		
		this.tokenTipo = tokenTipoFuncion;
		this.tokenIdentificador = tokenIdentificador;
		
		this.nombresAmbitos.addAll(pilaAmbitos.peek().getNombresAmbitos());
			
		//Creo un nuevo ambito (ACTUAL + nuevo)
		this.nombresAmbitos.add(tokenIdentificador.getLexema());
	}
	
	/**
	 * Crea un nuevo ambito, lo agrega a la pila y lo retorna
	 * @param tokenTipoFuncion TokenTipoFuncion Tipo de funcion que genera el nuevo ambito
	 * @param nuevoAmbito Nombre del nuevo ambito
	 * @return El ambito creado
	 */
	public static Ambito nuevoAmbito(Token tokenTipoFuncion, Token tokenIdentificador){
		return pilaAmbitos.push(new Ambito(tokenTipoFuncion, tokenIdentificador));
	}
	
	/**
	 * Quita el ultimo ambito anidado
	 * @return
	 */
	public static void finalizarAmbito() {
		//Desapilo ultimo ambito
		pilaAmbitos.pop();
	}
	
	/**
	 * Devuelve el ambito actual.
	 * La primera vez inicializa con ambito main
	 * @return Ambito actual
	 */
	public static Ambito getAmbitoActual() {
		if (pilaAmbitos.empty())
			return pilaAmbitos.push(new Ambito());
		else
			return pilaAmbitos.peek();
	}
	
	/**
	 * Concatena los nombres de cada ambito, anteponiendo un @ a cada uno.
	 * @return Nombre completo del ambito
	 */
	public String getNombre() {
		
		//Retorno los ambitos anteponiendo un @ a cada uno
		return "@"+nombresAmbitos.stream().collect(Collectors.joining("@"));
	}
	
	/**
	 * Devuelve los nombres de ambitos anidados para el objeto de instancia.
	 * Para uso interno de la clase.
	 * @return Lista de ambitos.
	 */
	private List<String> getNombresAmbitos(){
		return nombresAmbitos;
	}
	
	/**
	 * Consulta si el ambito actual es main (tamaño de pila igual a 1)	
	 * @return
	 */
	public boolean isMain() {
		return nombresAmbitos.size() == 1;
	}

	/**
	 * @return the tokenTipoFuncion
	 */
	public Token getTokenTipoFuncion() {
		return tokenTipo;
	}

	/**
	 * @return the tokenIdentificador
	 */
	public Token getTokenIdentificador() {
		return tokenIdentificador;
	}
	
	/**
	 * Verifica si el ambito del token recibido por parametro es ancestro del ambito actual
	 * @param ambitoToken
	 * @return
	 */
	public static boolean esAncestro(Ambito ambitoToken) {

		//Si es ancestro, el nombre del ambito actual comienza con el nombre del ancestro
		return getAmbitoActual().getNombre().startsWith(ambitoToken.getNombre());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getNombre();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombresAmbitos == null) ? 0 : nombresAmbitos.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ambito other = (Ambito) obj;
		if (nombresAmbitos == null) {
			if (other.nombresAmbitos != null)
				return false;
		} else if (!nombresAmbitos.equals(other.nombresAmbitos))
			return false;
		return true;
	}
	
}
