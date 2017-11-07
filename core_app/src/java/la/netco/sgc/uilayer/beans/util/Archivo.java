/**
 * 
 */
package la.netco.sgc.uilayer.beans.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import la.netco.sgc.persistence.dto.CortesFormato;
import la.netco.sgc.persistence.dto.Entidades;
import la.netco.sgc.persistence.dto.Formatos;

/**
 * @author cguzman
 *
 */
public class Archivo  implements Serializable{
	

	private static final long serialVersionUID = 1L;

	private String nombre;
	
	private String extension;
	
	private String nombreHoja;
	
	private List<Fila> filas;
	
	private List<ErrorGenerico> errores;
	
	private boolean esSobreescrito;
	
	private Date fechaHoraCargue;
	
	private Entidades entidad;
	
	private Formatos formato;
	
	private CortesFormato corte;
	
	
	
	
	public Archivo() {
		// TODO Auto-generated constructor stub
		errores = new ArrayList<ErrorGenerico>();
	}

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
	 * @return the nombreHoja
	 */
	public String getNombreHoja() {
		return nombreHoja;
	}

	/**
	 * @param nombreHoja the nombreHoja to set
	 */
	public void setNombreHoja(String nombreHoja) {
		this.nombreHoja = nombreHoja;
	}

	/**
	 * @return the filas
	 */
	public List<Fila> getFilas() {
		return filas;
	}

	/**
	 * @param filas the filas to set
	 */
	public void setFilas(List<Fila> filas) {
		this.filas = filas;
	}

	/**
	 * @return the error
	 */
	public List<ErrorGenerico> getErrores() {
		return errores;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(List<ErrorGenerico> errores) {
		this.errores = errores;
	}

	/**
	 * @return the esSobreescrito
	 */
	public boolean isEsSobreescrito() {
		return esSobreescrito;
	}

	/**
	 * @param esSobreescrito the esSobreescrito to set
	 */
	public void setEsSobreescrito(boolean esSobreescrito) {
		this.esSobreescrito = esSobreescrito;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the fechaHoraCargue
	 */
	public Date getFechaHoraCargue() {
		return fechaHoraCargue;
	}

	/**
	 * @param fechaHoraCargue the fechaHoraCargue to set
	 */
	public void setFechaHoraCargue(Date fechaHoraCargue) {
		this.fechaHoraCargue = fechaHoraCargue;
	}

	/**
	 * @return the entidad
	 */
	public Entidades getEntidad() {
		return entidad;
	}

	/**
	 * @param entidad the entidad to set
	 */
	public void setEntidad(Entidades entidad) {
		this.entidad = entidad;
	}

	/**
	 * @return the formato
	 */
	public Formatos getFormato() {
		return formato;
	}

	/**
	 * @param formato the formato to set
	 */
	public void setFormato(Formatos formato) {
		this.formato = formato;
	}

	/**
	 * @param errores the errores to set
	 */
	public void setErrores(List<ErrorGenerico> errores) {
		this.errores = errores;
	}

	/**
	 * @return the corte
	 */
	public CortesFormato getCorte() {
		return corte;
	}

	/**
	 * @param corte the corte to set
	 */
	public void setCorte(CortesFormato corte) {
		this.corte = corte;
	}
	
	
	
	
}
