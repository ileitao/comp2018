package compilador.codigointermedio;

import compilador.Token;

public class ElementoPI {

	//Representa el lexema de un token, direccion de bifurcacion,
	//o cualquier otro simbolo especial necesario.
	private String elemento;
	
	private Token token;
	
	/**
	 * 
	 */
	public ElementoPI(String elem, Token token) {
		this.elemento = elem;
		this.token = token;
	}

	/**
	 * @return the elemento
	 */
	public String getElemento() {
		return elemento;
	}

	/**
	 * @param elemento the elemento to set
	 */
	public void setElemento(String elemento) {
		this.elemento = elemento;
	}

	/**
	 * @return the token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(Token token) {
		this.token = token;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ElementoPI [" + elemento + "]\n";
	}
}
