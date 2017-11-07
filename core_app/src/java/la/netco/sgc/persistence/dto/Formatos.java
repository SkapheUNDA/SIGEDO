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
import javax.persistence.Transient;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "Formatos", schema = Schemas.SGC_SCHEMA)
@NamedQueries(value = {
		@NamedQuery(name=Formatos.NAMED_QUERY_ALL,query=Formatos.QUERY_ALL)
})
public class Formatos implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int forCodigo;
	private String forNombre;
	private Integer forTotalCampos;
	private String forDescripcion;
	private Integer forLineaInicial;
	private String forNombreHoja;	
	private Set<DetallesFormato> detallesFormatos = new HashSet<DetallesFormato>(
			0);
	private Set<RegistroCargue> registroCargues = new HashSet<RegistroCargue>(0);
	private Set<RegistroCargueHistorico> registroCargueHistoricos = new HashSet<RegistroCargueHistorico>(
			0);
	private Set<Autorizaciones> autorizaciones = new HashSet<Autorizaciones>(0);
	private Set<RegistroCargueFormato> registroCargueFormatos = new HashSet<RegistroCargueFormato>(0);
	
	 @Transient
	private Integer corteAsignado;
	
	public Formatos() {
	}

	public Formatos(int forCodigo) {
		this.forCodigo = forCodigo;
	}

	public Formatos(int forCodigo, String forNombre, Integer forTotalCampos,
			String forDescripcion, Integer forLineaInicial,
			String forNombreHoja, Set<CortesFormato> cortesFormatos,
			Set<DetallesFormato> detallesFormatos,
			Set<RegistroCargue> registroCargues,
			Set<RegistroCargueHistorico> registroCargueHistoricos,
			Set<Autorizaciones> autorizaciones) {
		this.forCodigo = forCodigo;
		this.forNombre = forNombre;
		this.forTotalCampos = forTotalCampos;
		this.forDescripcion = forDescripcion;
		this.forLineaInicial = forLineaInicial;
		this.forNombreHoja = forNombreHoja;		
		this.detallesFormatos = detallesFormatos;
		this.registroCargues = registroCargues;
		this.registroCargueHistoricos = registroCargueHistoricos;
		this.autorizaciones = autorizaciones;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "FOR_Codigo", unique = true, nullable = false)
	public int getForCodigo() {
		return this.forCodigo;
	}

	public void setForCodigo(int forCodigo) {
		this.forCodigo = forCodigo;
	}

	@Column(name = "FOR_Nombre", length = 200)
	public String getForNombre() {
		return this.forNombre;
	}

	public void setForNombre(String forNombre) {
		this.forNombre = forNombre;
	}

	@Column(name = "FOR_TotalCampos")
	public Integer getForTotalCampos() {
		return this.forTotalCampos;
	}

	public void setForTotalCampos(Integer forTotalCampos) {
		this.forTotalCampos = forTotalCampos;
	}

	@Column(name = "FOR_Descripcion", length = 1000)
	public String getForDescripcion() {
		return this.forDescripcion;
	}

	public void setForDescripcion(String forDescripcion) {
		this.forDescripcion = forDescripcion;
	}

	@Column(name = "FOR_LineaInicial")
	public Integer getForLineaInicial() {
		return this.forLineaInicial;
	}

	public void setForLineaInicial(Integer forLineaInicial) {
		this.forLineaInicial = forLineaInicial;
	}

	@Column(name = "FOR_NombreHoja", length = 30)
	public String getForNombreHoja() {
		return this.forNombreHoja;
	}

	public void setForNombreHoja(String forNombreHoja) {
		this.forNombreHoja = forNombreHoja;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "formatos")
	public Set<DetallesFormato> getDetallesFormatos() {
		return this.detallesFormatos;
	}

	public void setDetallesFormatos(Set<DetallesFormato> detallesFormatos) {
		this.detallesFormatos = detallesFormatos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "formatos")
	public Set<RegistroCargue> getRegistroCargues() {
		return this.registroCargues;
	}

	public void setRegistroCargues(Set<RegistroCargue> registroCargues) {
		this.registroCargues = registroCargues;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "formatos")
	public Set<RegistroCargueHistorico> getRegistroCargueHistoricos() {
		return this.registroCargueHistoricos;
	}

	public void setRegistroCargueHistoricos(Set<RegistroCargueHistorico> registroCargueHistoricos) {
		this.registroCargueHistoricos = registroCargueHistoricos;
	}

	/**
	 * @return the autorizaciones
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "formatos")
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
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "formatos")
	public Set<RegistroCargueFormato> getRegistroCargueFormatos() {
		return registroCargueFormatos;
	}

	/**
	 * @param registroCargueFormatos the registroCargueFormatos to set
	 */	
	public void setRegistroCargueFormatos(Set<RegistroCargueFormato> registroCargueFormatos) {
		this.registroCargueFormatos = registroCargueFormatos;
	}

	/**
	 * @return the corteAsignado
	 */
	public Integer getCorteAsignado() {
		return corteAsignado;
	}

	/**
	 * @param corteAsignado the corteAsignado to set
	 */
	public void setCorteAsignado(Integer corteAsignado) {
		this.corteAsignado = corteAsignado;
	}
	
	
    public static final String NAMED_QUERY_ALL = "getAllInformes";
    public static final String QUERY_ALL = "FROM Formatos formato order by formato.forNombre asc";
	
	

}
