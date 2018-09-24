/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 * Clase Token que representa un conjunto de simbolos.
 * 
 * @author ileitao
 * @author gmaiola
 * @author ltapia
 */
public class Token {

	String lexema;
	long id;

	public Token(String lexema, long id) {
		this.lexema = lexema;
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the lexema
	 */
	public String getLexema() {
		return lexema;
	}

	/**
	 * @param lexema
	 *            the lexema to set
	 */
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Token [lexema=" + lexema + ", id=" + id + "]";
	}

}
