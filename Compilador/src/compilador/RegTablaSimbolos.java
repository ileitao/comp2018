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

	//Id que identifica unicidad de un registro de tabla de simbolos.
	private short regId = -1;
	
	//Ambito en el que fue declarado el token
	private Ambito ambito;
	
	private Token token;
	private TipoToken tipoToken;
	private UsoToken usoToken;
	private int linea;
	private int posicion;

	public RegTablaSimbolos(short regId, Token token, int linea, int posicion) {
		this.regId = regId;
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
	
	public short getRegId() {
		return regId;
	}

	public void setRegId(short regId) {
		this.regId = regId;
	}
	
	public TipoToken getTipoToken() {
		return tipoToken;
	}

	public void setTipoToken(TipoToken tipoToken) {
		this.tipoToken = tipoToken;
	}

	public UsoToken getUsoToken() {
		return usoToken;
	}

	public void setUsoToken(UsoToken usoToken) {
		this.usoToken = usoToken;
	}
	
	/**
	 * @return the ambito
	 */
	public Ambito getAmbito() {
		return ambito;
	}

	/**
	 * @param ambito the ambito to set
	 */
	public void setAmbito(Ambito ambito) {
		this.ambito = ambito;
	}

	@Override
	public String toString() {
		return "RegTS [regId=" + regId + ", " + token + ",\t" + "ambito=" + ambito + ", tipoToken=" + tipoToken + ", usoToken="
				+ usoToken + ", linea=" + linea + ", posicion=" + posicion + "]";
	}
}
