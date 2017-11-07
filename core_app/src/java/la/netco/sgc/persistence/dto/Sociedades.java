package la.netco.sgc.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

/**
 * 
 * @author jhmoreno
 * Entidad que contiene todos los datos de la tabla SOCIEDADES 
 */
@Entity
@Table(name = "SOCIEDADES", schema = Schemas.SGC_SCHEMA)
public class Sociedades implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String codSociedad;
	private Integer nit;
	private String razonSocial;
	private Integer idRepresentanteLegal;
	private String direccion;
	private Integer telefono;
	private String nombreContacto;
	private String emailContacto;
	private String estado;
	private String nombreRepresentanteLegal;
	
	public Sociedades(){
		
	}

	public Sociedades(String codSociedad, Integer nit, String razonSocial,
			Integer idRepresentanteLegal, String direccion, Integer telefono,
			String nombreContacto, String emailContacto, String estado,
			String nombreRepresentanteLegal) {
		super();
		this.codSociedad = codSociedad;
		this.nit = nit;
		this.razonSocial = razonSocial;
		this.idRepresentanteLegal = idRepresentanteLegal;
		this.direccion = direccion;
		this.telefono = telefono;
		this.nombreContacto = nombreContacto;
		this.emailContacto = emailContacto;
		this.estado = estado;
		this.nombreRepresentanteLegal = nombreRepresentanteLegal;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "COD_SOCIEDAD", unique = false, nullable = false)
	public String getCodSociedad() {
		return codSociedad;
	}

	public void setCodSociedad(String codSociedad) {
		this.codSociedad = codSociedad;
	}

	@Column(name = "NIT", nullable = false)
	public Integer getNit() {
		return nit;
	}

	public void setNit(Integer nit) {
		this.nit = nit;
	}

	@Column(name = "RAZON_SOCIAL", nullable = false, length = 150)
	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	@Column(name = "ID_REPRESENTANTE_LEGAL", nullable = false)
	public Integer getIdRepresentanteLegal() {
		return idRepresentanteLegal;
	}

	public void setIdRepresentanteLegal(Integer idRepresentanteLegal) {
		this.idRepresentanteLegal = idRepresentanteLegal;
	}
	
	@Column(name = "DIRECCION", nullable = false, length = 150)
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Column(name = "TELEFONO", nullable = false, length = 150)
	public Integer getTelefono() {
		return telefono;
	}

	public void setTelefono(Integer telefono) {
		this.telefono = telefono;
	}

	@Column(name = "NOMBRE_CONTACTO", nullable = false, length = 150)
	public String getNombreContacto() {
		return nombreContacto;
	}

	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	@Column(name = "EMAIL_CONTACTO", nullable = false, length = 150)
	public String getEmailContacto() {
		return emailContacto;
	}

	public void setEmailContacto(String emailContacto) {
		this.emailContacto = emailContacto;
	}
	
	@Column(name = "ESTADO", nullable = false, length = 150)
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	@Column(name = "NOMBRE_REPRESENTANTE_LEGAL", nullable = false, length = 150)
	public String getNombreRepresentanteLegal() {
		return nombreRepresentanteLegal;
	}

	public void setNombreRepresentanteLegal(String nombreRepresentanteLegal) {
		this.nombreRepresentanteLegal = nombreRepresentanteLegal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
	
}
