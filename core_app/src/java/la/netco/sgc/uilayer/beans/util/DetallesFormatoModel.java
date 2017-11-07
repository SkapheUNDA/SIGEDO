/**
 * 
 */
package la.netco.sgc.uilayer.beans.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * @author carlos
 *
 */
public class DetallesFormatoModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2240158282131929342L;

	private Integer index;
	
	private String nombreCampo;
	
	private String descipcionCampo;
	
	private Integer tipoDato;
	
	private String tipoDatoSeleccionado;
	
	private Integer formatoTipoDato;
	
	private List<SelectItem> itemsTiposDato;
	
	private List<SelectItem> itemsFormatosTiposDato;
	
	private boolean requerido;
	
	public DetallesFormatoModel() {
		// TODO Auto-generated constructor stub
		this.itemsTiposDato = new ArrayList<SelectItem>();
		this.itemsFormatosTiposDato = new ArrayList<SelectItem>();
	}
	
			

	/**
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * @return the nombreCampo
	 */
	public String getNombreCampo() {
		return nombreCampo;
	}

	/**
	 * @param nombreCampo the nombreCampo to set
	 */
	public void setNombreCampo(String nombreCampo) {
		this.nombreCampo = nombreCampo;
	}

	/**
	 * @return the descipcionCampo
	 */
	public String getDescipcionCampo() {
		return descipcionCampo;
	}

	/**
	 * @param descipcionCampo the descipcionCampo to set
	 */
	public void setDescipcionCampo(String descipcionCampo) {
		this.descipcionCampo = descipcionCampo;
	}

	/**
	 * @return the tipoDato
	 */
	public Integer getTipoDato() {
		return tipoDato;
	}

	/**
	 * @param tipoDato the tipoDato to set
	 */
	public void setTipoDato(Integer tipoDato) {
		this.tipoDato = tipoDato;
	}

	/**
	 * @return the formatoTipoDato
	 */
	public Integer getFormatoTipoDato() {
		return formatoTipoDato;
	}

	/**
	 * @param formatoTipoDato the formatoTipoDato to set
	 */
	public void setFormatoTipoDato(Integer formatoTipoDato) {
		this.formatoTipoDato = formatoTipoDato;
	}

	/**
	 * @return the itemsTiposDato
	 */
	public List<SelectItem> getItemsTiposDato() {
		return itemsTiposDato;
	}

	/**
	 * @param itemsTiposDato the itemsTiposDato to set
	 */
	public void setItemsTiposDato(List<SelectItem> itemsTiposDato) {
		this.itemsTiposDato = itemsTiposDato;
	}

	/**
	 * @return the itemsFormatosTiposDato
	 */
	public List<SelectItem> getItemsFormatosTiposDato() {
		return itemsFormatosTiposDato;
	}

	/**
	 * @param itemsFormatosTiposDato the itemsFormatosTiposDato to set
	 */
	public void setItemsFormatosTiposDato(List<SelectItem> itemsFormatosTiposDato) {
		this.itemsFormatosTiposDato = itemsFormatosTiposDato;
	}


	/**
	 * @return the tipoDatoSeleccionado
	 */
	public String getTipoDatoSeleccionado() {
		return tipoDatoSeleccionado;
	}


	/**
	 * @param tipoDatoSeleccionado the tipoDatoSeleccionado to set
	 */
	public void setTipoDatoSeleccionado(String tipoDatoSeleccionado) {
		this.tipoDatoSeleccionado = tipoDatoSeleccionado;
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