/**
 * 
 */
package la.netco.sgc.uilayer.beans.util;

import java.io.Serializable;

/**
 * @author cguzman
 *
 */
public class InformesFaltantesDTO implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	private String sociedad;
	
	private String formato;

	public String getSociedad() {
		return sociedad;
	}

	public void setSociedad(String sociedad) {
		this.sociedad = sociedad;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}
	
	
	
	

}
