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
	private TipoToken tipo;
	private int posicion;

	public RegTablaSimbolos(Token token, TipoToken tipo, int posicion) {
		this.token = token;
		this.tipo = tipo;
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

	/**
	 * @return the tipo
	 */
	public TipoToken getTipo() {
		return tipo;
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(TipoToken tipo) {
		this.tipo = tipo;
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

}
