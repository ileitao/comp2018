package compilador;

/**
 * Clase RegTablaSimbolos
 * 
 * Estructura de datos que contiene toda la informacion asociado a un token
 * valido.
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public class RegTablaSimbolos {

	private Token token;
	private int linea;
	private int posicion;

	public RegTablaSimbolos(Token token, int linea, int posicion) {
		this.token = token;    	
		this.linea = linea;
		this.posicion = posicion;
	}

	/**
	 * @return the token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(Token token) {
		this.token = token;
	}

	public int getLinea() {
		return linea;
	}

	public void setLinea(int linea) {
		this.linea = linea;
	}

	/**
	 * @return the posicion
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion
	 *            the posicion to set
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	@Override
	public String toString() {
		return "RegTablaSimbolos [token=" + token + ", linea=" + linea + ", posicion=" + posicion
				+ "]";
	}

}
