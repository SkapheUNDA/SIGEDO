/**
 * 
 */
package la.netco.sgc.uilayer.beans.util;

import java.io.Serializable;

import la.netco.sgc.persistence.dto.DetallesFormato;

/**
 * @author cguzman
 *
 */
public class Columna implements Serializable{
	

	private static final long serialVersionUID = 1L;

	
	private String nombre;
	
	private Integer poscicion;
	
	private String formato;
	
	private String tipoDato;
	
	private String valor;
	
	private ErrorGenerico error;
	
	private DetallesFormato detalle;
	
	private boolean requerido;

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the poscicion
	 */
	public Integer getPoscicion() {
		return poscicion;
	}

	/**
	 * @param poscicion the poscicion to set
	 */
	public void setPoscicion(Integer poscicion) {
		this.poscicion = poscicion;
	}

	/**
	 * @return the formato
	 */
	public String getFormato() {
		return formato;
	}

	/**
	 * @param formato the formato to set
	 */
	public void setFormato(String formato) {
		this.formato = formato;
	}

	/**
	 * @return the tipoDato
	 */
	public String getTipoDato() {
		return tipoDato;
	}

	/**
	 * @param tipoDato the tipoDato to set
	 */
	public void setTipoDato(String tipoDato) {
		this.tipoDato = tipoDato;
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

	/**
	 * @return the error
	 */
	public ErrorGenerico getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(ErrorGenerico error) {
		this.error = error;
	}

	/**
	 * @return the detalle
	 */
	public DetallesFormato getDetalle() {
		return detalle;
	}

	/**
	 * @param detalle the detalle to set
	 */
	public void setDetalle(DetallesFormato detalle) {
		this.detalle = detalle;
	}

	/**
	 * @return the requerido
	 */
	public boolean isRequerido() {
		return requerido;
	}

	/**
	 * @param requerido the requerido to set
	 */
	public void setRequerido(boolean requerido) {
		this.requerido = requerido;
	}
	
	
	
	
}
