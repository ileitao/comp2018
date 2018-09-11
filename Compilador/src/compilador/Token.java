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

	private static long nextId = 1;

	StringBuffer lexema;
	long id;

	public Token(String lexema) {
		this.lexema = new StringBuffer(lexema);
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the next id
	 */
	public void setId() {
		this.id = nextId++;
	}

	/**
	 * @return the lexema
	 */
	public StringBuffer getLexema() {
		return lexema;
	}

	/**
	 * @param lexema
	 *            the lexema to set
	 */
	public void setLexema(StringBuffer lexema) {
		this.lexema = lexema;
	}

	public void concatenarSimbolo(Character simbolo) {
		this.lexema.append(simbolo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lexema == null) ? 0 : lexema.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		Token other = (Token) obj;
		if (lexema == null) {
			if (other.lexema != null)
				return false;
		} else if (!lexema.equals(other.lexema))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Token [lexema=" + lexema + ", id=" + id + "]";
	}

}
