package la.netco.sgc.persistence.dto;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;
import la.netco.persistencia.dto.commons.Persona;


/**
 * @author cpineros
 *
 */
@Entity
@Table(name = "Entidades", schema = Schemas.SGC_SCHEMA)
@NamedQueries(value = {
		@NamedQuery(name=Entidades.NAMEQUERY_SELECT_BY_NIT, query = Entidades.SELECT_BY_NIT),
		@NamedQuery(name=Entidades.NAMED_QUERY_ALL,query=Entidades.QUERY_ALL),
	    @NamedQuery(name=Entidades.NAMED_QUERY_ALL_ACTIVOS,query=Entidades.QUERY_ALL_ACTIVOS)		
})
public class Entidades implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private Integer entCodigo;
	private Integer entAcreacion;
	private String entPjuridica;
	private String entAutorizacionF;
	private String entObjetoSocial;
	private String entDomicilio;
	private String entDireccion;
	private String entTelefonos;
	private String entFax;
	private String entWeb;
	private Integer entNoSedes;
	private Set<Autorizaciones> autorizaciones = new HashSet<Autorizaciones>(0);
	private Set<RegistroCargueFormato> registroCargueFormatos = new HashSet<RegistroCargueFormato>(0);
	/*Nuevos campos
	 * ENT_Nit -- Integer
	 * ENT_Reprelegal -- Integer
	 * ENT_Nombrelegal  -- varchar
	 * ENT_Nombrecontacto
	 * ENT_Emailcontacto 
	 * ENT_Estado varchar
	 * 
	 * */
	private Integer entNit;
	private Integer entReprelegal;
	private String entNombrelegal;
	private String entNombreContacto;
	private String entEmailcontacto ;
	private String entEstado;
	

	public Entidades() {
	}

	public Entidades(int entCodigo, int entAcreacion, String entPjuridica,
			String entAutorizacionF, String entObjetoSocial,
			String entDomicilio, String entDireccion, String entTelefonos,
			String entFax, String entWeb, int entNoSedes,
			Set<Autorizaciones> autorizaciones, int entNit, int entReprelegal, String entNombrelegal , String entNombreContacto, String entEmailcontacto, String entEstado) {
		this.entCodigo = entCodigo;
		this.entAcreacion = entAcreacion;
		this.entPjuridica = entPjuridica;
		this.entAutorizacionF = entAutorizacionF;
		this.entObjetoSocial = entObjetoSocial;
		this.entDomicilio = entDomicilio;
		this.entDireccion = entDireccion;
		this.entTelefonos = entTelefonos;
		this.entFax = entFax;
		this.entWeb = entWeb;
		this.entNoSedes = entNoSedes;
		this.entNit=entNit;
		this.entReprelegal=entReprelegal;
		this.entNombreContacto = entNombreContacto;
		this.entNombrelegal=entNombrelegal;
		this.entEmailcontacto=entEmailcontacto;
		this.entEstado=entEstado;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ENT_Codigo", unique = true, nullable = false)
	public Integer getEntCodigo() {
		return this.entCodigo;
	}

	public void setEntCodigo(Integer entCodigo) {
		this.entCodigo = entCodigo;
	}

	@Column(name = "ENT_ACreacion", nullable = false)
	public Integer getEntAcreacion() {
		return this.entAcreacion;
	}

	public void setEntAcreacion(Integer entAcreacion) {
		this.entAcreacion = entAcreacion;
	}

	@Column(name = "ENT_PJuridica", nullable = false, length = 150)
	public String getEntPjuridica() {
		return this.entPjuridica;
	}

	public void setEntPjuridica(String entPjuridica) {
		this.entPjuridica = entPjuridica;
	}

	@Column(name = "ENT_AutorizacionF", nullable = false, length = 500)
	public String getEntAutorizacionF() {
		return this.entAutorizacionF;
	}

	public void setEntAutorizacionF(String entAutorizacionF) {
		this.entAutorizacionF = entAutorizacionF;
	}

	@Column(name = "ENT_ObjetoSocial", nullable = false, length = 250)
	public String getEntObjetoSocial() {
		return this.entObjetoSocial;
	}

	public void setEntObjetoSocial(String entObjetoSocial) {
		this.entObjetoSocial = entObjetoSocial;
	}

	@Column(name = "ENT_Domicilio", nullable = true, length = 150)
	public String getEntDomicilio() {
		return this.entDomicilio;
	}

	public void setEntDomicilio(String entDomicilio) {
		this.entDomicilio = entDomicilio;
	}

	@Column(name = "ENT_Direccion", nullable = true, length = 150)
	public String getEntDireccion() {
		return this.entDireccion;
	}

	public void setEntDireccion(String entDireccion) {
		this.entDireccion = entDireccion;
	}

	@Column(name = "ENT_Telefonos", nullable = true, length = 150)
	public String getEntTelefonos() {
		return this.entTelefonos;
	}

	public void setEntTelefonos(String entTelefonos) {
		this.entTelefonos = entTelefonos;
	}

	@Column(name = "ENT_Fax", nullable = true, length = 150)
	public String getEntFax() {
		return this.entFax;
	}

	public void setEntFax(String entFax) {
		this.entFax = entFax;
	}

	@Column(name = "ENT_Web", nullable = true, length = 150)
	public String getEntWeb() {
		return this.entWeb;
	}

	public void setEntWeb(String entWeb) {
		this.entWeb = entWeb;
	}

	@Column(name = "ENT_NoSedes", nullable = true)
	public Integer getEntNoSedes() {
		return this.entNoSedes;
	}

	public void setEntNoSedes(Integer entNoSedes) {
		this.entNoSedes = entNoSedes;
	}

	/**
	 * @return the autorizaciones
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entidades")
	public Set<Autorizaciones> getAutorizaciones() {
		return this.autorizaciones;
	}

	/**
	 * @param autorizaciones the autorizaciones to set
	 */
	public void setAutorizaciones(Set<Autorizaciones> autorizaciones) {
		this.autorizaciones = autorizaciones;
	}

	/**
	 * @return the registroCargueFormatos
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entidades")
	public Set<RegistroCargueFormato> getRegistroCargueFormatos() {
		return registroCargueFormatos;
	}

	/**
	 * @param registroCargueFormatos the registroCargueFormatos to set
	 */
	public void setRegistroCargueFormatos(Set<RegistroCargueFormato> registroCargueFormatos) {
		this.registroCargueFormatos = registroCargueFormatos;
	}
	
	@Column(name = "ENT_Nit", nullable = true)
	public Integer getEntNit() {
		return entNit;
	}

	public void setEntNit(Integer entNit) {
		this.entNit = entNit;
	}
	@Column(name = "ENT_Reprelegal", nullable = true)
	public Integer getEntReprelegal() {
		return entReprelegal;
	}

	public void setEntReprelegal(Integer entReprelegal) {
		this.entReprelegal = entReprelegal;
	}
	@Column(name = "ENT_Nombrelegal", nullable = true, length = 200)
	public String getEntNombrelegal() {
		return entNombrelegal;
	}

	public void setEntNombrelegal(String entNombrelegal) {
		this.entNombrelegal = entNombrelegal;
	}
	@Column(name = "ENT_Emailcontacto", nullable = true, length = 200)
	public String getEntEmailcontacto() {
		return entEmailcontacto;
	}

	public void setEntEmailcontacto(String entEmailcontacto) {
		this.entEmailcontacto = entEmailcontacto;
	}
	@Column(name = "ENT_Estado", nullable = true, length = 200)
	public String getEntEstado() {
		return entEstado;
	}

	public void setEntEstado(String entEstado) {
		this.entEstado = entEstado;
	}
	@Column(name = "ENT_Nombrecontacto", nullable = true, length = 200)
	public String getEntNombreContacto() {
		return entNombreContacto;
	}

	public void setEntNombreContacto(String entNombreContacto) {
		this.entNombreContacto = entNombreContacto;
	}
	
	
	public static final String NAMEQUERY_SELECT_BY_NIT ="SelectByNit";
    public static final String SELECT_BY_NIT = "from Entidades entidad Where entidad.entNit = ?";
    public static final String NAMED_QUERY_ALL = "getAllSociedades";
    public static final String QUERY_ALL = "FROM Entidades entidad order by entidad.entObjetoSocial asc";
    public static final String NAMED_QUERY_ALL_ACTIVOS = "getAllSociedadesActivos";
    public static final String QUERY_ALL_ACTIVOS = "FROM Entidades entidad where entEstado = '1' order by entidad.entObjetoSocial asc";
	

}
