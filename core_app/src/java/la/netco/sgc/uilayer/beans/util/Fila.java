/**
 * 
 */
package la.netco.sgc.uilayer.beans.util;

import java.io.Serializable;
import java.util.List;

/**
 * @author cguzman
 *
 */
public class Fila implements Serializable{	

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private List<Columna> columnas;
	
	private List<ErrorGenerico> errores;
	
	private boolean esEncabezado;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the columnas
	 */
	public List<Columna> getColumnas() {
		return columnas;
	}

	/**
	 * @param columnas the columnas to set
	 */
	public void setColumnas(List<Columna> columnas) {
		this.columnas = columnas;
	}

	public List<ErrorGenerico> getErrores() {
		return errores;
	}

	public void setErrores(List<ErrorGenerico> errores) {
		this.errores = errores;
	}

	/**
	 * @return the esEncabezado
	 */
	public boolean isEsEncabezado() {
		return esEncabezado;
	}

	/**
	 * @param esEncabezado the esEncabezado to set
	 */
	public void setEsEncabezado(boolean esEncabezado) {
		this.esEncabezado = esEncabezado;
	}
	
	
	
	

}
