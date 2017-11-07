package la.netco.persistencia.dto.commons;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import la.netco.core.persistencia.vo.Schemas;
import la.netco.core.persistencia.vo.Usuario;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "EXPEDIENTE", schema = Schemas.DBO_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = "EXP_COD"))
@NamedQueries({
		@NamedQuery(name = Expediente.NAMED_QUERY_GET_EXP_MAX_BY_YEAR, query = Expediente.QUERY_GET__EXP_MAX_BY_YEAR),
		@NamedQuery(name = Expediente.NAMED_QUERY_GET_EXP_MAX_BY_COD_DEPEND, query = Expediente.QUERY_GET_EXP_MAX_BY_COD_DEPEND),
		@NamedQuery(name = Expediente.NAMED_QUERY_GET_EXP_MAX_ALL, query = Expediente.QUERY_GET_EXP_MAX_ALL),
		@NamedQuery(name = Expediente.NAMED_QUERY_GET_EXP_MAX_BY_COD_DEPEN_YEAR, query = Expediente.QUERY_GET_EXP_MAX_BY_COD_DEPEND_YEAR) })
public class Expediente implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int expId;
	private Usuario usuarioTemp;
	private Tiposexpediente tiposexpediente;
	private Estado estado;
	private Entidad entidad;
	private Tipospersona tipospersona;
	private Depend depend;
	private Persona persona;
	private Ubicacion ubicacion;
	private Tramite tramite;
	private Calidadrepresentante calidadrepresentante;
	private String expCod;
	private String expNom;
	private Date expFso;
	private boolean expVbo;
	private String expCdep;
	private Integer expCano;
	private Integer expCnro;
	//private Integer expSta;
	private Integer expDev;
	private Set<Seguimientoexpediente> seguimientoexpedientes = new HashSet<Seguimientoexpediente>(
			0);
	// private Set<Imagenes> imageneses = new HashSet<Imagenes>(0);
	// private Set<Archivo> archivos = new HashSet<Archivo>(0);
	// private Set<Expedienteanexo> expedienteanexos = new
	// HashSet<Expedienteanexo>(
	// 0);
	private Set<Expedientecorrespond> expedientecorresponds = new HashSet<Expedientecorrespond>(
			0);

	// private Set<Anotacionesexpedientes> anotacionesexpedienteses = new
	// HashSet<Anotacionesexpedientes>(
	// 0);

	// private Set<Anotacionesexpedientes> anotacionesexpedienteses = new
	// HashSet<Anotacionesexpedientes>(
	// 0);

	private ExpedienteEstado estadoGeneral;
	
	public Expediente() {
	}

	public Expediente(int expId, boolean expVbo) {
		this.expId = expId;
		this.expVbo = expVbo;
	}

	// public Expediente(int expId, UsuarioTemp UsuarioTemp,
	// Tiposexpediente tiposexpediente, Estado estado, Entidad entidad,
	// Tipospersona tipospersona, Depend depend, Persona persona,
	// Ubicacion ubicacion, Tramite tramite,
	// Calidadrepresentante calidadrepresentante, String expCod,
	// String expNom, Date expFso, boolean expVbo, String expCdep,
	// Integer expCano, Integer expCnro, Integer expSta, Integer expDev,
	// Set<Seguimientoexpediente> seguimientoexpedientes,
	// Set<Imagenes> imageneses, Set<Archivo> archivos,
	// Set<Expedienteanexo> expedienteanexos,
	// Set<Expedientecorrespond> expedientecorresponds,
	// Set<Anotacionesexpedientes> anotacionesexpedienteses) {
	// this.expId = expId;
	// this.UsuarioTemp = UsuarioTemp;
	// this.tiposexpediente = tiposexpediente;
	// this.estado = estado;
	// this.entidad = entidad;
	// this.tipospersona = tipospersona;
	// this.depend = depend;
	// this.persona = persona;
	// this.ubicacion = ubicacion;
	// this.tramite = tramite;
	// this.calidadrepresentante = calidadrepresentante;
	// this.expCod = expCod;
	// this.expNom = expNom;
	// this.expFso = expFso;
	// this.expVbo = expVbo;
	// this.expCdep = expCdep;
	// this.expCano = expCano;
	// this.expCnro = expCnro;
	// this.expSta = expSta;
	// this.expDev = expDev;
	// this.seguimientoexpedientes = seguimientoexpedientes;
	// this.imageneses = imageneses;
	// this.archivos = archivos;
	// this.expedienteanexos = expedienteanexos;
	// this.expedientecorresponds = expedientecorresponds;
	// this.anotacionesexpedienteses = anotacionesexpedienteses;
	// }

	/**
	 * Id del expediente
	 * 
	 * @return
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EXP_ID", unique = true, nullable = false)
	public int getExpId() {
		return this.expId;
	}

	public void setExpId(int expId) {
		this.expId = expId;
	}

	/**
	 * 
	 * Objeto de Usuario , actualmente conectado a usuariotemp
	 * 
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXP_USR")
	public Usuario getUsuarioTemp() {
		return this.usuarioTemp;
	}

	public void setUsuarioTemp(Usuario usuarioTemp) {
		this.usuarioTemp = usuarioTemp;
	}

	/**
	 * Objeto para obtener los tipos de expediente
	 * 
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXP_TEX")
	public Tiposexpediente getTiposexpediente() {
		return this.tiposexpediente;
	}

	public void setTiposexpediente(Tiposexpediente tiposexpediente) {
		this.tiposexpediente = tiposexpediente;
	}

	@ManyToOne
	@JoinColumn(name = "EXP_EST")
	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	 /**
	  * Objeto de entidad 
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "EXP_ETD")
	 public Entidad getEntidad() {
	 return this.entidad;
	 }
	
	 public void setEntidad(Entidad entidad) {
	 this.entidad = entidad;
	 }

	/**
	 * 
	 * Objeto que obtiene los tipos de persona por
	 * 
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXP_TPE")
	public Tipospersona getTipospersona() {
		return this.tipospersona;
	}

	public void setTipospersona(Tipospersona tipospersona) {
		this.tipospersona = tipospersona;
	}

	/**
	 * 
	 * objeto dependencia del expediente. se obtiene el nombre de la depedencia
	 * en expediente
	 * 
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXP_DEP")
	public Depend getDepend() {
		return this.depend;
	}

	public void setDepend(Depend depend) {
		this.depend = depend;
	}

	/**
	 * 
	 * se obtiene el nombre completo de la persona para expediente
	 * 
	 * @return
	 */
	@ManyToOne
	@JoinColumn(name = "EXP_SOL")
	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	 /**
	  * 
	  * Ubicacion se halla con el id de la entrada consultando a entrada y luego se toma en ent_med de la entrada.
	  * 
	  * si es diferente de 17 se asigna un 5 , si es igual a 17  se asigna un 4  a ala ubicacion
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "EXP_UBI")
	 public Ubicacion getUbicacion() {
	 return this.ubicacion;
	 }
	
	 public void setUbicacion(Ubicacion ubicacion) {
	 this.ubicacion = ubicacion;
	 }

	/**
	 * 
	 * Se obtiene el nombre del tramite para el expediente
	 * 
	 * @return
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXP_TRM")
	public Tramite getTramite() {
		return this.tramite;
	}

	public void setTramite(Tramite tramite) {
		this.tramite = tramite;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXP_CAL")
	public Calidadrepresentante getCalidadrepresentante() {
		return this.calidadrepresentante;
	}

	public void setCalidadrepresentante(
			Calidadrepresentante calidadrepresentante) {
		this.calidadrepresentante = calidadrepresentante;
	}

	/**
	 * 
	 * Codigo del expediente que es diferente al id incrementable del expediente
	 * , este codigo el dado por la entidad
	 * 
	 * @return
	 */
	@Column(name = "EXP_COD", unique = true, length = 20)
	public String getExpCod() {
		return this.expCod;
	}

	public void setExpCod(String expCod) {
		this.expCod = expCod;
	}

	/**
	 * 
	 * nombre del expediente
	 * 
	 * @return
	 */
	@Column(name = "EXP_NOM", length = 1000)
	public String getExpNom() {
		return this.expNom;
	}

	public void setExpNom(String expNom) {
		this.expNom = expNom;
	}

	/**
	 * 
	 * Fecha del expediente.
	 * 
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXP_FSO", length = 23)
	public Date getExpFso() {
		return this.expFso;
	}

	public void setExpFso(Date expFso) {
		this.expFso = expFso;
	}

	/**
	 * 
	 * Visto bueno para el expediente
	 * 
	 * @return
	 */
	@Column(name = "EXP_VBO", nullable = false)
	public boolean isExpVbo() {
		return this.expVbo;
	}

	public void setExpVbo(boolean expVbo) {
		this.expVbo = expVbo;
	}

	/**
	 * 
	 * Codigo de la dependencia
	 * 
	 * @return
	 */
	@Column(name = "EXP_CDEP", length = 20)
	public String getExpCdep() {
		return this.expCdep;
	}

	public void setExpCdep(String expCdep) {
		this.expCdep = expCdep;
	}

	/**
	 * 
	 * Codigo del ano para el expediente.
	 * 
	 * @return
	 */
	@Column(name = "EXP_CANO")
	public Integer getExpCano() {
		return this.expCano;
	}

	public void setExpCano(Integer expCano) {
		this.expCano = expCano;
	}

	/**
	 * 
	 * Coonsecutivo para el expediente
	 * 
	 * @return
	 */
	@Column(name = "EXP_CNRO")
	public Integer getExpCnro() {
		return this.expCnro;
	}

	public void setExpCnro(Integer expCnro) {
		this.expCnro = expCnro;
	}

/*	@Column(name = "EXP_STA")
	public Integer getExpSta() {
		return this.expSta;
	}
	public void setExpSta(Integer expSta) {
		this.expSta = expSta;
	}*/

	@ManyToOne
 	@JoinColumn(name = "EXP_STA", nullable = true)
 	@ForeignKey(name = "FK_EXPEDIENTE_ESTADO_GEN")
 	@OnDelete(action = OnDeleteAction.NO_ACTION)	
	public ExpedienteEstado getEstadoGeneral() {
		return estadoGeneral;
	}
	
	

	@Column(name = "EXP_DEV")
	public Integer getExpDev() {
		return this.expDev;
	}

	public void setExpDev(Integer expDev) {
		this.expDev = expDev;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "expediente")
	public Set<Seguimientoexpediente> getSeguimientoexpedientes() {
		return this.seguimientoexpedientes;
	}

	public void setSeguimientoexpedientes(
			Set<Seguimientoexpediente> seguimientoexpedientes) {
		this.seguimientoexpedientes = seguimientoexpedientes;
	}

	//
	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "expediente")
	// public Set<Imagenes> getImageneses() {
	// return this.imageneses;
	// }
	//
	// public void setImageneses(Set<Imagenes> imageneses) {
	// this.imageneses = imageneses;
	// }
	//
	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "expediente")
	// public Set<Archivo> getArchivos() {
	// return this.archivos;
	// }
	//
	// public void setArchivos(Set<Archivo> archivos) {
	// this.archivos = archivos;
	// }
	//
	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "expediente")
	// public Set<Expedienteanexo> getExpedienteanexos() {
	// return this.expedienteanexos;
	// }
	//
	// public void setExpedienteanexos(Set<Expedienteanexo> expedienteanexos) {
	// this.expedienteanexos = expedienteanexos;
	// }
	//
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "expediente")
	public Set<Expedientecorrespond> getExpedientecorresponds() {
		return this.expedientecorresponds;
	}

	public void setExpedientecorresponds(
			Set<Expedientecorrespond> expedientecorresponds) {
		this.expedientecorresponds = expedientecorresponds;
	}

	//
	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "expediente")
	// public Set<Anotacionesexpedientes> getAnotacionesexpedienteses() {
	// return this.anotacionesexpedienteses;
	// }
	//
	// public void setAnotacionesexpedienteses(
	// Set<Anotacionesexpedientes> anotacionesexpedienteses) {
	// this.anotacionesexpedienteses = anotacionesexpedienteses;
	// }


	public void setEstadoGeneral(ExpedienteEstado estadoGeneral) {
		this.estadoGeneral = estadoGeneral;
	}

	/**
	 * Maximo Consecutivo por Año
	 */
	public static final String NAMED_QUERY_GET_EXP_MAX_BY_YEAR = "getMaxExpByYear";
	public static final String QUERY_GET__EXP_MAX_BY_YEAR = "SELECT max(expCnro) From Expediente expediente WHERE expCano = ? ";

	/**
	 * Maximo consecutivo
	 */
	public static final String NAMED_QUERY_GET_EXP_MAX_ALL = "getMaxExpediente";
	public static final String QUERY_GET_EXP_MAX_ALL = "SELECT max(expCnro) From Expediente expediente";

	/**
	 * Maximo consecutivo por codigo de dependencia
	 */
	public static final String NAMED_QUERY_GET_EXP_MAX_BY_COD_DEPEND = "getMaxExpCodDepend";
	public static final String QUERY_GET_EXP_MAX_BY_COD_DEPEND = "SELECT max(expCnro) From Expediente expediente WHERE expCdep = ? ";

	/**
	 * Maximo consecutivo por codigo dependencia y año
	 */
	public static final String NAMED_QUERY_GET_EXP_MAX_BY_COD_DEPEN_YEAR = "getMaxExpCodDependYear";
	public static final String QUERY_GET_EXP_MAX_BY_COD_DEPEND_YEAR = "SELECT max(expCnro) From Expediente expediente WHERE expCdep = ? and  expCano = ?";
	
	

}
