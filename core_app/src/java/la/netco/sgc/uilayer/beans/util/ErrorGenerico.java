/**
 * 
 */
package la.netco.sgc.uilayer.beans.util;

import java.io.Serializable;

/**
 * @author cguzman
 *
 */
public class ErrorGenerico implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	private int idFila;
	
	private String columna;
	
	private String valor;
	
	private String severidad;
	
	private String mensaje;

	/**
	 * @return the severidad
	 */
	public String getSeveridad() {
		return severidad;
	}

	/**
	 * @param severidad the severidad to set
	 */
	public void setSeveridad(String severidad) {
		this.severidad = severidad;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the idFila
	 */
	public int getIdFila() {
		return idFila;
	}

	/**
	 * @param idFila the idFila to set
	 */
	public void setIdFila(int idFila) {
		this.idFila = idFila;
	}

	/**
	 * @return the columna
	 */
	public String getColumna() {
		return columna;
	}

	/**
	 * @param columna the columna to set
	 */
	public void setColumna(String columna) {
		this.columna = columna;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	

}
