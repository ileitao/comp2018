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
	short codigo;
	
	//Referencia al registro en la tabla de simbolos al que pertenece
	RegTablaSimbolos regTabSimbolos;

	public Token(String lexema, short codigo) {
		this.lexema = lexema;
		this.codigo = codigo;
	}

	/**
	 * @return the id
	 */
	public short getCodigo() {
		return codigo;
	}

	public void setCodigo(short codigo) {
		this.codigo = codigo;
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
	
	public RegTablaSimbolos getRegTabSimbolos() {
		return regTabSimbolos;
	}

	public void setRegTabSimbolos(RegTablaSimbolos regTabSimbolos) {
		this.regTabSimbolos = regTabSimbolos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Token [lexema=" + lexema + "\t| id=" + codigo + "]";
	}

}
