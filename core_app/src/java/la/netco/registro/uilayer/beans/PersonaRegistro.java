package la.netco.registro.uilayer.beans;

import java.io.Serializable;

public class PersonaRegistro implements Serializable {

	private static final long serialVersionUID = -4322000751518883914L;

	private String nombreCompleto;
	private String noDocumento;
	private String tipoDocumento;
	private String nacionalidad;
	private String ciudad;
	private String direccion;
	private Short tipoPersona;
	private String tipoPersonaRegistro;

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getNoDocumento() {
		return noDocumento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public String getCiudad() {
		return ciudad;
	}

	public String getDireccion() {
		return direccion;
	}

	public Short getTipoPersona() {
		return tipoPersona;
	}

	public String getTipoPersonaRegistro() {
		return tipoPersonaRegistro;
	}

	public void setNoDocumento(String noDocumento) {
		this.noDocumento = noDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTipoPersona(Short tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public void setTipoPersonaRegistro(String tipoPersonaRegistro) {
		this.tipoPersonaRegistro = tipoPersonaRegistro;
	}

}
