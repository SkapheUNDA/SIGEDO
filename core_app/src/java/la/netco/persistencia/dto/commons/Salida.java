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
import javax.persistence.Transient;

import la.netco.core.persistencia.vo.Schemas;
import la.netco.core.persistencia.vo.Usuario;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "SALIDA", schema = Schemas.DBO_SCHEMA	)
@NamedQueries({
	@NamedQuery(name=Salida.NAMED_QUERY_GET_MAX_ALL,query=Salida.QUERY_GET_MAX_ALL),
	@NamedQuery(name=Salida.NAMED_QUERY_GET_MAX_BY_YEAR,query=Salida.QUERY_GET_MAX_BY_YEAR),
	@NamedQuery(name=Salida.NAMED_QUERY_GET_MAX_BY_COD_DEPEND,query=Salida.QUERY_GET_MAX_BY_COD_DEPEND),
	@NamedQuery(name=Salida.NAMED_QUERY_GET_MAX_BY_COD_DEPEN_YEAR,query=Salida.QUERY_GET_MAX_BY_COD_DEPEND_YEAR)
})
public class Salida implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private Integer salId;
	private Lugar lugar;
    private Depend depend;
    private Usuario usuarioBySalUsr;
    private Medioscorrespondencia medio;
    private Tipospersona tipospersona;
    private Usuario usuarioBySalUen;
    private Tiposdocumento tiposdocumento;
	private Estado estado;
    private Clasificacion clasificacion;
    private Paises paises;
    private String salNsa;
    private Date salFsa;
    private String salAsu;
    private boolean salLren;
    private String salNdo;
    private String salNom;
    private String salPap;
    private String salSap;
    private String salDir;
    private String salTel;
    private String salFax;
    private String salCel;
    private String salCar;
    private String salIde;
    private String salEnt;
    private String salObs;
    private Short salFol;
    private Boolean salLent;
    private String salPen;
    private Date salFen;
    private Date salHen;
    private Integer salEnm;
    private Date salFrs;
    private String salOde;
    private String salCdep;
    private Integer salCano;
    private Integer salCnro;
    private Set<Expedientecorrespond> expedientecorresponds = new HashSet<Expedientecorrespond>(0);
//    private Set<Archivo> archivos = new HashSet<Archivo>(0);
//    private Set<Detalleprogramacion> detalleprogramacions = new HashSet<Detalleprogramacion>(0);
//    private Set<Imagenes> imageneses = new HashSet<Imagenes>(0);
//    private Set<Enlace> enlaces = new HashSet<Enlace>(0);
//    private Set<Seguimientosalida> seguimientosalidas = new HashSet<Seguimientosalida>(0);
    private Set<Anexosalida> anexosalidas = new HashSet<Anexosalida>();

    private String nombreCompletoDest;    
    
    public static final String NAMED_QUERY_GET_MAX_ALL = "getMaxSalida";

    public static final String QUERY_GET_MAX_ALL = "SELECT max(salCnro) From Salida salida";

    public static final String NAMED_QUERY_GET_MAX_BY_YEAR= "getMaxSalidaYear";

    public static final String QUERY_GET_MAX_BY_YEAR = "SELECT max(salCnro) From Salida salida WHERE salCano = ? ";

    public static final String NAMED_QUERY_GET_MAX_BY_COD_DEPEND= "getMaxSalidaCodDepend";

    public static final String QUERY_GET_MAX_BY_COD_DEPEND = "SELECT max(salCnro) From Salida salida WHERE salCdep = ? ";

    public static final String NAMED_QUERY_GET_MAX_BY_COD_DEPEN_YEAR= "getMaxSalidaCodDependYear";

    public static final String QUERY_GET_MAX_BY_COD_DEPEND_YEAR = "SELECT max(salCnro) From Salida salida WHERE salCdep = ? and  salCano = ?  ";
    

    private String obsEtapa;
	private Short usrCambioEtapa;
	private Short usrEntrega;
	private Short idMedioEntrega;
    
    public Salida() {
    }

    public Salida(Integer salId, boolean salLren) {
        this.salId = salId;
        this.salLren = salLren;
    }

    @Override
	public boolean equals(Object object) {
		if (!(object instanceof Salida)) {
			return false;
		}
		Salida other = (Salida) object;
		if ((this.salId == null && other.salId != null)	|| (this.salId != null && !this.salId.equals(other.salId))) {
			return false;
		}
		return true;
	}

    /**
	 *  
	 * Retorna la clasificacion a la que pertenence la salida
	 * 
	 * @return clasificacion
	 */
	@ManyToOne
	@JoinColumn(name = "SAL_CLA", nullable = false)
	@ForeignKey(name = "FK_SALIDA_CLASIFICACION")
	public Clasificacion getClasificacion() {
	    return this.clasificacion;
	}

    /**
     *  
     * Retorna la dependencia a la que pertenence la salida
     * 
     * @return depend
     */
    @ManyToOne
 	@JoinColumn(name = "SAL_DEP", nullable = false)
 	@ForeignKey(name = "FK_SALIDA_DEPENDENCIA")
    public Depend getDepend() {
        return this.depend;
    }

    /**
	 * Retorna estado actual de la salida
	 * 
	 * @return estado
	 */
	@ManyToOne
	@JoinColumn(name = "SAL_EST", nullable = false)
	@ForeignKey(name = "FK_SALIDA_ESTADO")
	public Estado getEstado() {
	    return this.estado;
	}

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "salida")
    public Set<Expedientecorrespond> getExpedientecorresponds() {
        return this.expedientecorresponds;
    }

    @Formula(value="SAL_NOM + ' ' + SAL_PAP + ' ' + SAL_SAP")
	public String getNombreCompletoDest() {
		return nombreCompletoDest;
	}

    /**
     * Retorna el asunto de la salida
     * 
     * @return salAsu
     */
    @Column(name = "SAL_ASU", length = 100)
    public String getSalAsu() {
        return this.salAsu;
    }

    @Column(name = "SAL_CANO")
    public Integer getSalCano() {
        return this.salCano;
    }

    @Column(name = "SAL_CAR", length = 50)
    public String getSalCar() {
        return this.salCar;
    }

    @Column(name = "SAL_CDEP", length = 20)
    public String getSalCdep() {
        return this.salCdep;
    }

	@Column(name = "SAL_CEL", length = 50)
	public String getSalCel() {
		return this.salCel;
	}

	@Column(name = "SAL_CNRO")
	public Integer getSalCnro() {
		return this.salCnro;
	}

	@Column(name = "SAL_DIR", length = 200)
	public String getSalDir() {
		return this.salDir;
	}

	@Column(name = "SAL_ENM")
	public Integer getSalEnm() {
		return this.salEnm;
	}

    @Column(name = "SAL_ENT", length = 100)
    public String getSalEnt() {
        return this.salEnt;
    }

    @Column(name = "SAL_FAX", length = 50)
    public String getSalFax() {
        return this.salFax;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SAL_FEN", length = 23)
    public Date getSalFen() {
        return this.salFen;
    }

    @Column(name = "SAL_FOL")
    public Short getSalFol() {
        return this.salFol;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SAL_FRS", length = 23)
    public Date getSalFrs() {
        return this.salFrs;
    }

    /**
     * Retorna la fecha de radicación 
     * 
     * @return salFsa
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SAL_FSA", length = 23)
    public Date getSalFsa() {
        return this.salFsa;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SAL_HEN", length = 23)
    public Date getSalHen() {
        return this.salHen;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "SAL_ID", unique = true, nullable = false)
    public Integer getSalId() {
        return this.salId;
    }

    @Column(name = "SAL_IDE", length = 50)
    public String getSalIde() {
        return this.salIde;
    }

    @Column(name = "SAL_LENT")
    public Boolean getSalLent() {
        return this.salLent;
    }

    @Column(name = "SAL_NDO", length = 50)
    public String getSalNdo() {
        return this.salNdo;
    }

    /**
     * Retorna el nombre del destinatario
     * 
     * @return salNom
     */
    @Column(name = "SAL_NOM", length = 50)
    public String getSalNom() {
        return this.salNom;
    }

    /**
     * Retorna el Número de Correspondencia o número de radicación
     * 
     * @return salNsa
     */
    @Column(name = "SAL_NSA", length = 20)
    public String getSalNsa() {
        return this.salNsa;
    }

    @Column(name = "SAL_OBS", length = 5000)
    public String getSalObs() {
        return this.salObs;
    }

    @Column(name = "SAL_ODE", length = 254)
    public String getSalOde() {
        return this.salOde;
    }

    /**
     * Retorna el primer apellido del destinatario
     * 
     * @return salPap
     */
    @Column(name = "SAL_PAP", length = 50)
    public String getSalPap() {
        return this.salPap;
    }

    @Column(name = "SAL_PEN", length = 200)
    public String getSalPen() {
        return this.salPen;
    }

    /**
     * Retorna el segundo apellido del destinatario
     * 
     * 
     * @return salSap
     */
    @Column(name = "SAL_SAP", length = 50)
    public String getSalSap() {
        return this.salSap;
    }

    @Column(name = "SAL_TEL", length = 50)
    public String getSalTel() {
        return this.salTel;
    }

    @ManyToOne
    @JoinColumn(name = "SAL_TPE", nullable = false)
 	@ForeignKey(name = "FK_SALIDA_TIPO_PERSONA")
    public Tipospersona getTipospersona() {
        return this.tipospersona;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SAL_UEN", nullable = true)
 	@ForeignKey(name = "FK_SALIDA_USUARIO_UEN")
    public Usuario getUsuarioBySalUen() {
        return this.usuarioBySalUen;
    }

    @ManyToOne
    @JoinColumn(name = "SAL_USR", nullable = false)
 	@ForeignKey(name = "FK_SALIDA_USUARIO")
    public Usuario getUsuarioBySalUsr() {
        return this.usuarioBySalUsr;
    }

    @ManyToOne
	@JoinColumn(name = "SAL_MED", nullable = false)
 	@ForeignKey(name = "FK_SALIDA_MEDIO")
	public Medioscorrespondencia getMedio() {
		return medio;
	}
    
	@ManyToOne
	@JoinColumn(name = "SAL_TDO", nullable = false)
 	@ForeignKey(name = "FK_SALIDA_TIPO_DOC")
	public Tiposdocumento getTiposdocumento() {
		return tiposdocumento;
	}
	@ManyToOne
	@JoinColumn(name = "SAL_LUG", nullable = true)
	@ForeignKey(name = "FK_SALIDA_LUGAR")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public Lugar getLugar() {
		return this.lugar;
	}

    @Column(name = "SAL_LREN", nullable = false)
    public boolean isSalLren() {
        return this.salLren;
    }
    @ManyToOne
	@JoinColumn(name = "SAL_NAC", nullable = true)
	@ForeignKey(name = "FK_SALIDA_PAIS")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public Paises getPaises() {
		return this.paises;
	}

    @OneToMany(mappedBy = "salida")
	public Set<Anexosalida> getAnexosalidas() {
		return this.anexosalidas;
	}

	public void setAnexosalidas(Set<Anexosalida> anexosalidas) {
		this.anexosalidas = anexosalidas;
	}

	public void setPaises(Paises paises) {
		this.paises = paises;
	}


	public void setLugar(Lugar lugar) {
		this.lugar = lugar;
	}
    
    
    public void setClasificacion(Clasificacion clasificacion) {
	    this.clasificacion = clasificacion;
	}

    public void setDepend(Depend depend) {
        this.depend = depend;
    }

    public void setEstado(Estado estado) {
	    this.estado = estado;
	}

    public void setExpedientecorresponds(Set<Expedientecorrespond> expedientecorresponds) {
        this.expedientecorresponds = expedientecorresponds;
    }

    public void setNombreCompletoDest(String nombreCompletoSol) {
		this.nombreCompletoDest = nombreCompletoSol;
	}

    public void setSalAsu(String salAsu) {
        this.salAsu = salAsu;
    }

    public void setSalCano(Integer salCano) {
        this.salCano = salCano;
    }

    public void setSalCar(String salCar) {
        this.salCar = salCar;
    }

    public void setSalCdep(String salCdep) {
        this.salCdep = salCdep;
    }

    public void setSalCel(String salCel) {
        this.salCel = salCel;
    }

    public void setSalCnro(Integer salCnro) {
        this.salCnro = salCnro;
    }

    public void setSalDir(String salDir) {
        this.salDir = salDir;
    }

    public void setSalEnm(Integer salEnm) {
        this.salEnm = salEnm;
    }

    public void setSalEnt(String salEnt) {
        this.salEnt = salEnt;
    }

    public void setSalFax(String salFax) {
        this.salFax = salFax;
    }

    public void setSalFen(Date salFen) {
        this.salFen = salFen;
    }

    public void setSalFol(Short salFol) {
        this.salFol = salFol;
    }

    public void setSalFrs(Date salFrs) {
        this.salFrs = salFrs;
    }

    public void setSalFsa(Date salFsa) {
        this.salFsa = salFsa;
    }

    public void setSalHen(Date salHen) {
        this.salHen = salHen;
    }

    public void setSalId(Integer salId) {
        this.salId = salId;
    }

    public void setSalIde(String salIde) {
        this.salIde = salIde;
    }

    public void setSalLent(Boolean salLent) {
        this.salLent = salLent;
    }

    public void setSalLren(boolean salLren) {
        this.salLren = salLren;
    }

    public void setSalNdo(String salNdo) {
        this.salNdo = salNdo;
    }

    public void setSalNom(String salNom) {
        this.salNom = salNom;
    }

    public void setSalNsa(String salNsa) {
        this.salNsa = salNsa;
    }

    public void setSalObs(String salObs) {
        this.salObs = salObs;
    }

    public void setSalOde(String salOde) {
        this.salOde = salOde;
    }

    public void setSalPap(String salPap) {
        this.salPap = salPap;
    }

    public void setSalPen(String salPen) {
        this.salPen = salPen;
    }

    public void setSalSap(String salSap) {
        this.salSap = salSap;
    }

    public void setSalTel(String salTel) {
        this.salTel = salTel;
    }

    
	public void setTipospersona(Tipospersona tipospersona) {
        this.tipospersona = tipospersona;
    }

	public void setUsuarioBySalUen(Usuario usuarioBySalUen) {
        this.usuarioBySalUen = usuarioBySalUen;
    }
	
	
	public void setUsuarioBySalUsr(Usuario usuarioBySalUsr) {
        this.usuarioBySalUsr = usuarioBySalUsr;
    }

	public void setMedio(Medioscorrespondencia medio) {
		this.medio = medio;
	}

	public void setTiposdocumento(Tiposdocumento tiposdocumento) {
		this.tiposdocumento = tiposdocumento;
	}

	@Transient
	public String getObsEtapa() {
		return obsEtapa;
	}

	public void setObsEtapa(String obsEtapa) {
		this.obsEtapa = obsEtapa;
	}
	@Transient
	public Short getUsrCambioEtapa() {
		return usrCambioEtapa;
	}

	public void setUsrCambioEtapa(Short usrCambioEtapa) {
		this.usrCambioEtapa = usrCambioEtapa;
	}
	@Transient
	public Short getIdMedioEntrega() {
		if(medio != null &&  (salLent != null && salLent.equals(Boolean.TRUE))){
			idMedioEntrega = medio.getMedId();
		}
		return idMedioEntrega;
	}

	public void setIdMedioEntrega(Short idMedioEntrega) {
		this.idMedioEntrega = idMedioEntrega;
	}
	@Transient
	public Short getUsrEntrega() {
		if(usuarioBySalUen != null && (salLent != null && salLent.equals(Boolean.TRUE))){
			usrEntrega = usuarioBySalUen.getUsrId();
		}
		return usrEntrega;
	}

	public void setUsrEntrega(Short usrEntrega) {
		this.usrEntrega = usrEntrega;
	}
    
	
/*	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "salida")
	public Set<Archivo> getArchivos() {
		return this.archivos;
	}

	public void setArchivos(Set<Archivo> archivos) {
		this.archivos = archivos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "salida")
	public Set<Detalleprogramacion> getDetalleprogramacions() {
		return this.detalleprogramacions;
	}

	public void setDetalleprogramacions(
			Set<Detalleprogramacion> detalleprogramacions) {
		this.detalleprogramacions = detalleprogramacions;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "salida")
	public Set<Imagenes> getImageneses() {
		return this.imageneses;
	}

	public void setImageneses(Set<Imagenes> imageneses) {
		this.imageneses = imageneses;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "salida")
	public Set<Enlace> getEnlaces() {
		return this.enlaces;
	}

	public void setEnlaces(Set<Enlace> enlaces) {
		this.enlaces = enlaces;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "salida")
	public Set<Seguimientosalida> getSeguimientosalidas() {
		return this.seguimientosalidas;
	}

	public void setSeguimientosalidas(Set<Seguimientosalida> seguimientosalidas) {
		this.seguimientosalidas = seguimientosalidas;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SAL_TDO")
	public Tiposdocumento getTiposdocumento() {
		return this.tiposdocumento;
	}

	public void setTiposdocumento(Tiposdocumento tiposdocumento) {
		this.tiposdocumento = tiposdocumento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SAL_MED")
	public Medioscorrespondencia getMedioscorrespondencia() {
		return this.medioscorrespondencia;
	}

	public void setMedioscorrespondencia(
			Medioscorrespondencia medioscorrespondencia) {
		this.medioscorrespondencia = medioscorrespondencia;
	}

*/
	
	
}
