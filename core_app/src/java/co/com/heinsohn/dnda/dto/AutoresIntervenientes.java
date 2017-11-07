package co.com.heinsohn.dnda.dto;

import java.io.Serializable;

public class AutoresIntervenientes  implements Serializable {

	/**
	 * Autores e Intervenientes Informe de Indice de libros de registro
	 */
	private static final long serialVersionUID = 1L;
	private Integer codRegistro;
	private String autId;
	private String rol;
	private String autorNom;
	private String autorDoc;
	public AutoresIntervenientes() {
		
	}
	public Integer getCodRegistro() {
		return codRegistro;
	}
	public void setCodRegistro(Integer codRegistro) {
		this.codRegistro = codRegistro;
	}
	public String getAutId() {
		return autId;
	}
	public void setAutId(String autId) {
		this.autId = autId;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getAutorNom() {
		return autorNom;
	}
	public void setAutorNom(String autorNom) {
		this.autorNom = autorNom;
	}
	public String getAutorDoc() {
		return autorDoc;
	}
	public void setAutorDoc(String autorDoc) {
		this.autorDoc = autorDoc;
	}
	
	
	
	
	

}
