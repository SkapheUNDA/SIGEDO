package la.netco.persistencia.dto.commons;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import la.netco.core.persistencia.vo.Schemas;
import la.netco.core.persistencia.vo.Usuario;



/**
 * @author smontanez
 *
 */
@Entity
@Table(name = "ENTRADA", schema = Schemas.DBO_SCHEMA)
@NamedQueries({
	@NamedQuery(name=Entrada.NAMED_QUERY_GET_MAX_ALL,query=Entrada.QUERY_GET_MAX_ALL),
	@NamedQuery(name=Entrada.NAMED_QUERY_GET_MAX_BY_YEAR,query=Entrada.QUERY_GET_MAX_BY_YEAR),
	@NamedQuery(name=Entrada.NAMED_QUERY_GET_MAX_BY_COD_DEPEND,query=Entrada.QUERY_GET_MAX_BY_COD_DEPEND),
	@NamedQuery(name=Entrada.NAMED_QUERY_GET_MAX_BY_COD_DEPEN_YEAR,query=Entrada.QUERY_GET_MAX_BY_COD_DEPEND_YEAR)
})
public class Entrada implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer entId;
    private String entNen;
    private Date entFen;
    private String entAsu;
    private String entNrf;
    private Date entFrf;
    private String entNrd;
    private boolean entLrsa;
  /*private Short entMed;
   	private Short entTpe;
    private Short entTdo;*/
   
    
    private String entNdo;
    private String entNom;
    private String entPap;
    private String entSap;
    private String entCar;
/*    private Short entNac;*/
    private String entDir;
/*    private Short entLug;*/
/*    private Short entCal;*/
    private Short entEve;
    
    private Estado estado;
    private Depend dependencia;
    private Usuario usuario;
    private Clasificacion clasificacion;
    private Tipospersona tipoPersona;
    private Tiposdocumento tiposdocumento;
    private Medioscorrespondencia medio;
    private Paises pais;
    private Lugar lugar;
    private Calidadrepresentante calidadRepresentante;
    
    private String nombreCompletoSol;    
  //  private Short entDep;
    
    //private Short entCla;
    
    
    
    private String entTel;
    private String entFax;
    private String entCel;
    private String entIde;
    private String entEnt;
    private String entObs;
    private Short entFol;
    private boolean entLrta;
    private Date entFrt;
    private Short entTrt;
    private boolean entLcon;
    private Date entFef;
    private Short entTef;
  //  private Short entUsr;
    private String entNot;
    private Date entFre;
    private String entCdep;
    private Integer entCano;
    private Integer entCnro;
    private Date entFdr;
    private Integer sal;
    private Boolean entLnot;   
    
    private String obsEtapa;
	private Short usrCambioEtapa;

	private Set<Anexoentrada> anexos = new HashSet<Anexoentrada>();
    
   // private Set<Depend> depends = new HashSet<Depend>(0);
    //private Set<Archivo> archivos = new HashSet<Archivo>(0);
    //private Set<Imagenes> imageneses = new HashSet<Imagenes>(0);
    //private Set<Seguimientoentrada> seguimientoentradas = new HashSet<Seguimientoentrada>(0);
   // private Set<Expedientecorrespond> expedientecorresponds = new HashSet<Expedientecorrespond>(0);

    public static final String NAMED_QUERY_GET_MAX_ALL = "getMaxEntrada";

    public static final String QUERY_GET_MAX_ALL = "SELECT max(entCnro) From Entrada entrada";

    public static final String NAMED_QUERY_GET_MAX_BY_YEAR= "getMaxEntradaYear";

    public static final String QUERY_GET_MAX_BY_YEAR = "SELECT max(entCnro) From Entrada entrada WHERE entCano = ? ";

    public static final String NAMED_QUERY_GET_MAX_BY_COD_DEPEND= "getMaxEntradaCodDepend";

    public static final String QUERY_GET_MAX_BY_COD_DEPEND = "SELECT max(entCnro) From Entrada entrada WHERE entCdep = ? ";

    public static final String NAMED_QUERY_GET_MAX_BY_COD_DEPEN_YEAR= "getMaxEntradaCodDependYear";

    public static final String QUERY_GET_MAX_BY_COD_DEPEND_YEAR = "SELECT max(entCnro) From Entrada entrada WHERE entCdep = ? and  entCano = ?  ";
    

    public Entrada() {
    }

    public Entrada(Integer entId, Date entFen, String entAsu, boolean entLrsa, Short entMed, boolean entLrta, boolean entLcon) {
        this.entId = entId;
        this.entFen = entFen;
        this.entAsu = entAsu;
        this.entLrsa = entLrsa;
       // this.entMed = entMed;
        this.entLrta = entLrta;
        this.entLcon = entLcon;
    }
    
    @Override
	public boolean equals(Object object) {
		if (!(object instanceof Entrada)) {
			return false;
		}
		Entrada other = (Entrada) object;
		if ((this.entId == null && other.entId != null)	|| (this.entId != null && !this.entId.equals(other.entId))) {
			return false;
		}
		return true;
	}

    @ManyToOne
	@JoinColumn(name = "ENT_CAL", nullable = false)
	@ForeignKey(name = "FK_ENTRADA_CALIDAD")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public Calidadrepresentante getCalidadRepresentante() {
		return calidadRepresentante;
	}

    @ManyToOne
	@JoinColumn(name = "ENT_CLA", nullable = false)
	@ForeignKey(name = "FK_ENTRADA_CLASIFICACION")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public Clasificacion getClasificacion() {
		return clasificacion;
	}

    @ManyToOne
 	@JoinColumn(name = "ENT_DEP", nullable = false)
 	@ForeignKey(name = "FK_ENTRADA_DEPENDENCIA")
 	@OnDelete(action = OnDeleteAction.NO_ACTION)	
	public Depend getDependencia() {
		return dependencia;
	}

    /**
     * Retorna asunto
     * 
     * @return entAsu
     */
    @Column(name = "ENT_ASU", nullable = false, length = 100)
    public String getEntAsu() {
        return this.entAsu;
    }

    /**
     * Retorna el año con el que fue registrada la Entrada, es el mismo del número de radicación
     * 
     * @return entCano
     */
    @Column(name = "ENT_CANO")
    public Integer getEntCano() {
        return this.entCano;
    }

    @Column(name = "ENT_CAR", length = 50)
    public String getEntCar() {
        return this.entCar;
    }

    /**
     * 
     * Retorna el código de la dependencia con el que fue registrada la Entrada
     * 
     * @return
     */
    @Column(name = "ENT_CDEP", length = 20)
    public String getEntCdep() {
        return this.entCdep;
    }

    /**
     * Retorna Email de Datos de Correspondencia
     * 
     * @return entCel
     */
    @Column(name = "ENT_CEL", length = 50)
    public String getEntCel() {
        return this.entCel;
    }

    /**
     * Retorna el número de consecutivo, de acuerdo al número de radicación generado. Este valor es la base para el siguiente consecutivo 
     * 
     * @return
     */
    @Column(name = "ENT_CNRO")
    public Integer getEntCnro() {
        return this.entCnro;
    }

    /**
     * 
     * Dirección correspondencia
     * 
     * @return entDir
     */
    @Column(name = "ENT_DIR", length = 200)
    public String getEntDir() {
        return this.entDir;
    }

    /**
     * 
     * Retorna entidad. Estos datos se ingresan al registro de acuerdo a la persona jurídica selecccionada
     * Sinembargo no exite una llave relacional con persona
     * 
     * @return entEnt
     */
    @Column(name = "ENT_ENT", length = 100)
    public String getEntEnt() {
        return this.entEnt;
    }

    @Column(name = "ENT_EVE")
    public Short getEntEve() {
        return this.entEve;
    }

/*    @Column(name = "ENT_LUG")
    public Short getEntLug() {
        return this.entLug;
    }*/

/*    @Column(name = "ENT_MED", nullable = false)
    public Short getEntMed() {
        return this.entMed;
    }*/


/*    @Column(name = "ENT_NAC")
    public Short getEntNac() {
        return this.entNac;
    }*/
    
   /*    @Column(name = "ENT_CAL")
    public Short getEntCal() {
        return this.entCal;
    }*/
    
    /*    @Column(name = "ENT_CLA")
    public Short getEntCla() {
        return this.entCla;
    }*/    
    /**
     * 
     * Retorna FAX correspondencia
     * 
     * @return entFax
     */
    @Column(name = "ENT_FAX", length = 50)
    public String getEntFax() {
        return this.entFax;
    }

    /**
     * Retorna fecha de respuesta calculada deacuerdo a la fecha de entrada y el tiempo limite de clasificacion
     * 
     * @return entFdr
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENT_FDR", length = 23)
    public Date getEntFdr() {
        return this.entFdr;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENT_FEF", length = 23)
    public Date getEntFef() {
        return this.entFef;
    }

    /**
     * 
     * Retorna Fecha de radicación
     * 
     * @return entFen
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENT_FEN", nullable = false, length = 23)
    public Date getEntFen() {
        return this.entFen;
    }

    @Column(name = "ENT_FOL")
    public Short getEntFol() {
        return this.entFol;
    }

    /**
     * Fecha real de entrega
     * 
     * @return entFre
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENT_FRE", length = 23)
    public Date getEntFre() {
        return this.entFre;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENT_FRF", length = 23)
    public Date getEntFrf() {
        return this.entFrf;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ENT_FRT", length = 23)
    public Date getEntFrt() {
        return this.entFrt;
    }

    /**
     * Retorna ID Entrada
     * 
     * @return entId
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "ENT_ID", unique = true, nullable = false)    
    public Integer getEntId() {
        return this.entId;
    }

    @Column(name = "ENT_IDE", length = 50)
    public String getEntIde() {
        return this.entIde;
    }

    /**
     * 
     * Descargue
     * 
     * @return
     */
    @Column(name = "ENT_LNOT")
    public Boolean getEntLnot() {
        return this.entLnot;
    }

/**
     * Retorna el numero de documento del solicitante
     * 
     * @return entNdo
     */
    @Column(name = "ENT_NDO", length = 50)
    public String getEntNdo() {
        return this.entNdo;
    }

/*    @Column(name = "ENT_USR")
    public Short getEntUsr() {
        return this.entUsr;
    }*/

    /**
     * 
     * Retorna Numero de radicacion
     *  
     * @return entNen
     */
    @Column(name = "ENT_NEN", length = 20)
    public String getEntNen() {
        return this.entNen;
    }
  
    /**
     * Retorna el nombre del solicitante. Este valor lo trae inicialmente de los datos de Persona.
     * Se debe tener en cuenta que no existe una relación con Persona.
     * 
     * @return entNom
     */
    @Column(name = "ENT_NOM", length = 50)
    public String getEntNom() {
        return this.entNom;
    }

    @Column(name = "ENT_NOT", length = 100)
    public String getEntNot() {
        return this.entNot;
    }

    @Column(name = "ENT_NRD", length = 50)
    public String getEntNrd() {
        return this.entNrd;
    }

    @Column(name = "ENT_NRF", length = 50)
    public String getEntNrf() {
        return this.entNrf;
    }
    
    @Column(name = "ENT_OBS", length = 5000)
    public String getEntObs() {
        return this.entObs;
    }

    /**
     * Retorna el primer apellido del solicitante. Este valor lo trae inicialmente de los datos de Persona.
     * Se debe tener en cuenta que no existe una relación con Persona.
     * 
     * @return entPap
     */
    @Column(name = "ENT_PAP", length = 50)
    public String getEntPap() {
        return this.entPap;
    }

/*    public void setEntCal(Short entCal) {
        this.entCal = entCal;
    }*/

    /**
     * Retorna el segundo apellido del solicitante. Este valor lo trae inicialmente de los datos de Persona.
     * Se debe tener en cuenta que no existe una llave con Persona.
     * 
     * @return entSap
     */
    @Column(name = "ENT_SAP", length = 50)
    public String getEntSap() {
        return this.entSap;
    }

    @Column(name = "ENT_TEF")
    public Short getEntTef() {
        return this.entTef;
    }

    /**
     * 
     * Retorna telefono correspondencia
     * 
     * @return entTel
     */
    @Column(name = "ENT_TEL", length = 50)
    public String getEntTel() {
        return this.entTel;
    }

    /*    @Column(name = "ENT_TPE")
    public Short getEntTpe() {
        return this.entTpe;
    }
*/
    /**
     * Retorna el tiempo limite de respuesta en días
     * 
     * @return
     */
    @Column(name = "ENT_TRT")
    public Short getEntTrt() {
        return this.entTrt;
    }

    @ManyToOne
	@JoinColumn(name = "ENT_EST", nullable = true)
	@ForeignKey(name = "FK_ENTRADA_ESTADO")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public Estado getEstado() {
		return estado;
	}
    @ManyToOne
	@JoinColumn(name = "ENT_LUG", nullable = true)
	@ForeignKey(name = "FK_ENTRADA_LUGAR")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public Lugar getLugar() {
		return lugar;
	}

    @ManyToOne
	@JoinColumn(name = "ENT_MED", nullable = false)
	@ForeignKey(name = "FK_ENTRADA_MEDIO")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
    public Medioscorrespondencia getMedio() {
		return medio;
	}

    @Formula(value="ENT_NOM + ' ' + ENT_PAP + ' ' + ENT_SAP")
	public String getNombreCompletoSol() {
		return nombreCompletoSol;
	}

    @ManyToOne
	@JoinColumn(name = "ENT_NAC", nullable = true)
	@ForeignKey(name = "FK_ENTRADA_PAIS")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public Paises getPais() {
		return pais;
	}

    @Column(name = "sal")
    public Integer getSal() {
        return this.sal;
    }

    @ManyToOne
	@JoinColumn(name = "ENT_TPE", nullable = false)
	@ForeignKey(name = "FK_ENTRADA_TIPO_PERSONA")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public Tipospersona getTipoPersona() {
		return tipoPersona;
	}

    @ManyToOne
	@JoinColumn(name = "ENT_TDO", nullable = true)
	@ForeignKey(name = "FK_ENTRADA_TIPO_DOCUMENTO")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public Tiposdocumento getTiposdocumento() {
		return tiposdocumento;
	}

    @ManyToOne
	@JoinColumn(name = "ENT_USR", nullable = false)
	@ForeignKey(name = "FK_ENTRADA_USUARIO")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public Usuario getUsuario() {
		return usuario;
	}

    @Column(name = "ENT_LCON", nullable = false)
    public boolean isEntLcon() {
        return this.entLcon;
    }

    @Column(name = "ENT_LRSA", nullable = false)
    public boolean isEntLrsa() {
        return this.entLrsa;
    }

    @Column(name = "ENT_LRTA", nullable = false)
    public boolean isEntLrta() {
        return this.entLrta;
    }

	@OneToMany(mappedBy = "entrada")
	public Set<Anexoentrada> getAnexos() {
		return anexos;
	}
	
    public void setCalidadRepresentante(Calidadrepresentante calidadRepresentante) {
		this.calidadRepresentante = calidadRepresentante;
	}

    public void setClasificacion(Clasificacion clasificacion) {
		this.clasificacion = clasificacion;
	}

    public void setDependencia(Depend dependencia) {
		this.dependencia = dependencia;
	}

    public void setEntAsu(String entAsu) {
        this.entAsu = entAsu;
    }

    public void setEntCano(Integer entCano) {
        this.entCano = entCano;
    }

    public void setEntCar(String entCar) {
        this.entCar = entCar;
    }

/*    public void setEntLug(Short entLug) {
        this.entLug = entLug;
    }*/

    public void setEntCdep(String entCdep) {
        this.entCdep = entCdep;
    }

    public void setEntCel(String entCel) {
        this.entCel = entCel;
    }
/*
    public void setEntCla(Short entCla) {
        this.entCla = entCla;
    }*/

    public void setEntCnro(Integer entCnro) {
        this.entCnro = entCnro;
    }

    public void setEntDir(String entDir) {
        this.entDir = entDir;
    }

    public void setEntEnt(String entEnt) {
        this.entEnt = entEnt;
    }

    public void setEntEve(Short entEve) {
        this.entEve = entEve;
    }

    public void setEntFax(String entFax) {
        this.entFax = entFax;
    }

    public void setEntFdr(Date entFdr) {
        this.entFdr = entFdr;
    }

    public void setEntFef(Date entFef) {
        this.entFef = entFef;
    }

    public void setEntFen(Date entFen) {
        this.entFen = entFen;
    }

    public void setEntFol(Short entFol) {
        this.entFol = entFol;
    }

/*    public void setEntTpe(Short entTpe) {
        this.entTpe = entTpe;
    }*/

    public void setEntFre(Date entFre) {
        this.entFre = entFre;
    }

/*    public void setEntUsr(Short entUsr) {
        this.entUsr = entUsr;
    }
*/
   /* @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "ENTRADADEPEND", schema = "dbo", catalog = "AUTOR", joinColumns = {
        @JoinColumn(name = "END_ENT", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "END_DEP", nullable = false, updatable = false)})
    public Set<Depend> getDepends() {
        return this.depends;
    }

    public void setDepends(Set<Depend> depends) {
        this.depends = depends;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entrada")
    public Set<Archivo> getArchivos() {
        return this.archivos;
    }

    public void setArchivos(Set<Archivo> archivos) {
        this.archivos = archivos;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entrada")
    public Set<Imagenes> getImageneses() {
        return this.imageneses;
    }

    public void setImageneses(Set<Imagenes> imageneses) {
        this.imageneses = imageneses;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entrada")
    public Set<Seguimientoentrada> getSeguimientoentradas() {
        return this.seguimientoentradas;
    }

    public void setSeguimientoentradas(Set<Seguimientoentrada> seguimientoentradas) {
        this.seguimientoentradas = seguimientoentradas;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entrada")
    public Set<Expedientecorrespond> getExpedientecorresponds() {
        return this.expedientecorresponds;
    }

    public void setExpedientecorresponds(Set<Expedientecorrespond> expedientecorresponds) {
        this.expedientecorresponds = expedientecorresponds;
    }*/

    public void setEntFrf(Date entFrf) {
        this.entFrf = entFrf;
    }

	public void setEntFrt(Date entFrt) {
        this.entFrt = entFrt;
    }

	public void setEntId(Integer entId) {
        this.entId = entId;
    }

	public void setEntIde(String entIde) {
        this.entIde = entIde;
    }
	
	public void setEntLcon(boolean entLcon) {
        this.entLcon = entLcon;
    }

	public void setEntLnot(Boolean entLnot) {
        this.entLnot = entLnot;
    }
	
	public void setEntLrsa(boolean entLrsa) {
        this.entLrsa = entLrsa;
    }

	public void setEntLrta(boolean entLrta) {
        this.entLrta = entLrta;
    }

	/*    public void setEntMed(Short entMed) {
        this.entMed = entMed;
    }
*/
/*    public void setEntNac(Short entNac) {
        this.entNac = entNac;
    }
*/
    public void setEntNdo(String entNdo) {
        this.entNdo = entNdo;
    }
	
	public void setEntNen(String entNen) {
        this.entNen = entNen;
    }
	
	public void setEntNom(String entNom) {
        this.entNom = entNom;
    }

	
	public void setEntNot(String entNot) {
        this.entNot = entNot;
    }
	
	public void setEntNrd(String entNrd) {
        this.entNrd = entNrd;
    }
	
	public void setEntNrf(String entNrf) {
        this.entNrf = entNrf;
    }

	public void setEntObs(String entObs) {
        this.entObs = entObs;
    }


	public void setEntPap(String entPap) {
        this.entPap = entPap;
    }

	public void setEntSap(String entSap) {
        this.entSap = entSap;
    }	
	

	public void setEntTef(Short entTef) {
        this.entTef = entTef;
    }

	public void setEntTel(String entTel) {
        this.entTel = entTel;
    }


	public void setEntTrt(Short entTrt) {
        this.entTrt = entTrt;
    }

	public void setEstado(Estado estado) {
		this.estado = estado;
	}


	public void setLugar(Lugar lugar) {
		this.lugar = lugar;
	} 
    public void setMedio(Medioscorrespondencia medio) {
		this.medio = medio;
	}
    

    public void setNombreCompletoSol(String nombreCompletoSol) {
		this.nombreCompletoSol = nombreCompletoSol;
	} 
    public void setPais(Paises pais) {
		this.pais = pais;
	}   


    public void setSal(Integer sal) {
        this.sal = sal;
    } 
    public void setTipoPersona(Tipospersona tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
    

    public void setTiposdocumento(Tiposdocumento tiposdocumento) {
		this.tiposdocumento = tiposdocumento;
	} 
    public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setAnexos(Set<Anexoentrada> anexos) {
		this.anexos = anexos;
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
    
	public void setEstiloClase(String in) {
		
	}
	
    public String getEstiloClase() {
    	if (entFdr != null) {
    		long diferencia = entFdr.getTime() - new Date().getTime();
    		double dias = ((double)diferencia)/(1000*60*60*24);
    		if (!entLcon) {
	    		if (dias < 0) {
	    			return "filaEntradaRojo";
	    		} else if (dias <= 3) {
	    			return "filaEntradaAmarillo";
	    		} else {
	    			return "filaEntradaVerde";
	    		}
    		} else {
    			return "filaEntradaNormal";
    		}
    	}
    	return "filaEntradaSinClasificar";
    }
}
