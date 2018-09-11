package compilador.log;

public class EventoLog {

	// Identificadores del tipo de evento ocurrido (Si fueran enum quedaria mejor)
	public static final String ERROR = "ERROR";
	public static final String WARNING = "WARNING";

	// Mensaje del evento
	private String mensaje;

	// Tipo de evento ocurrido: Error o Warning
	private String tipo;

	public EventoLog(String mensaje, String tipo) {
		this.mensaje = mensaje;
		this.tipo = tipo;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje
	 *            the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "EventoLog [mensaje=" + mensaje + ", tipo=" + tipo + "]";
	}

}
