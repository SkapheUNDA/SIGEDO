package la.netco.core.persistencia.vo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.sgc.persistence.dto.Entidades;

import org.hibernate.annotations.ForeignKey;


@Entity
@Table(name = "USUARIO",schema = Schemas.DBO_SCHEMA)
@NamedQueries(value = { 
	@NamedQuery(name=Usuario.NAMED_QUERY_ALL_USUARIOS,query=Usuario.QUERY_ALL_USUARIOS),
	@NamedQuery(name=Usuario.NAMED_QUERY_FIND_BY_USERNAME, query=Usuario.QUERY_FIND_BY_USERNAME),
	@NamedQuery(name=Usuario.NAMED_QUERY_FIND_BY_USUARIO_GRUPO, query=Usuario.QUERY_FIND_BY_USUARIO_GRUPO),
	@NamedQuery(name=Usuario.NAMED_QUERY_DATOS_BY_USERNAME, query=Usuario.QUERY_DATOS_BY_USERNAME),
	@NamedQuery(name=Usuario.NAMED_QUERY_ALL_USUARIOS_ACTIVOS,query=Usuario.QUERY_ALL_USUARIOS_ACTIVOS)
})
public class Usuario implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Short usrId;
    private String usrLog;
    private String usrNom;
    private String usrPap;
    private String usrSap;
    private String usrCon;
	private Date usrFreg;
	private Date usrFhact;
	
    private boolean usrLact; 
    private boolean habilitaAlerta;

	private Set<Perfil> perfiles = new HashSet<Perfil>();
	private Set<Grupo> grupos = new HashSet<Grupo>();
	private Set<Entidades> 	   entidades = new HashSet<Entidades>();

    public static final String NAMED_QUERY_ALL_USUARIOS = "getAllUsuarios";
    public static final String QUERY_ALL_USUARIOS = "FROM Usuario usu order by usu.usrNom asc";
	public static final String NAMED_QUERY_FIND_BY_USERNAME  = "findByUsername";
	public static final String QUERY_FIND_BY_USERNAME 	= "from Usuario usuario   WHERE usuario.usrLog=? ";
	public static final String NAMED_QUERY_DATOS_BY_USERNAME  = "findDatosByUsername";
	public static final String QUERY_DATOS_BY_USERNAME 	= "select usuario.usrNom, usuario.usrPap, usuario.usrSap from Usuario usuario WHERE usuario.usrLog=? ";
	public static final String NAMED_QUERY_FIND_BY_USUARIO_GRUPO  = "findByUserIdGrupo";
	public static final String QUERY_FIND_BY_USUARIO_GRUPO 	= "SELECT count(usuario.usrId) FROM Usuario usuario INNER JOIN usuario.grupos grupo WHERE usuario.usrLog=? AND  grupo.nombre = ? ";
	public static final String NAMED_QUERY_ALL_USUARIOS_ACTIVOS = "getAllUsuariosActivos";
	public static final String QUERY_ALL_USUARIOS_ACTIVOS = "FROM Usuario usu  WHERE usu.usrLact = 1 order by usu.usrNom asc";
	
	/**
	 * Solicitudes de registro en linea-Usuario 
	 */
	public static final Short SOLICITUD_REL=1;
    
    public Usuario() {
    }

    public Usuario(Short usrId, boolean usrLact) {
        this.usrId = usrId;
        this.usrLact = usrLact;
    }

    @Column(name = "USR_CON", length = 100)
    public String getUsrCon() {
        return this.usrCon;
    }
	@Column(name = "USR_FHACT", nullable = true)
    public Date getUsrFhact() {
		return usrFhact;
	}
	@Column(name = "USR_FREG", nullable = true)
    public Date getUsrFreg() {
		return usrFreg;
	}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USR_ID", unique = true, nullable = false)
    public Short getUsrId() {
        return this.usrId;
    }

    /**
     * 
     * Retorna el login del usuario del sistema
     * 
     * @return usrLog
     */
    @Column(name = "USR_LOG", length = 20)
    public String getUsrLog() {
        return this.usrLog;
    }

    @Column(name = "USR_NOM", length = 50)
    public String getUsrNom() {
        return this.usrNom;
    }

    @Column(name = "USR_PAP", length = 50)
    public String getUsrPap() {
        return this.usrPap;
    }

    @Column(name = "USR_SAP", length = 50)
    public String getUsrSap() {
        return this.usrSap;
    }

    @Column(name = "USR_LACT", nullable = false)
    public boolean isUsrLact() {
        return this.usrLact;
    }
    
	@Column(name = "USR_ALERTAS", nullable = false)
	public boolean isHabilitaAlerta() {
		return habilitaAlerta;
	}

	
    @ManyToMany()
	@JoinTable(name = "seg_perfiles_usuarios",   schema=Schemas.PRINCIPAL_SCHEMA,
			joinColumns = { @JoinColumn(name = "id_usuario", nullable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "id_perfil", nullable = false) })
	@ForeignKey(name = "FK_seg_perfiles_usuarios_seg_perfiles"   )
	public Set<Perfil> getPerfiles() {
		return perfiles;
	}
	
	@ManyToMany()
	@JoinTable(name = "seg_grupos_usuarios",  schema=Schemas.PRINCIPAL_SCHEMA,
			joinColumns = { @JoinColumn(name = "id_usuario", nullable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "id_grupo", nullable = false) })
	@ForeignKey(name = "FK_seg_grupos_usuarios_seg_grupos"   )
	public Set<Grupo> getGrupos() {
		return grupos;
	}
	
	@ManyToMany()
	@JoinTable(name = "seg_usuarios_sgc_entidades",  schema=Schemas.PRINCIPAL_SCHEMA,
			joinColumns = { @JoinColumn(name = "USR_ID", nullable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "ENT_Codigo", nullable = false) })
	@ForeignKey(name = "FK_seg_usuarios_sgc_entidades"   )
	public Set<Entidades> getEntidades() {
		return entidades;
	}
	
    public void setUsrCon(String usrCon) {
        this.usrCon = usrCon;
    }

    public void setUsrFhact(Date usrFhact) {
		this.usrFhact = usrFhact;
	}
	public void setUsrFreg(Date usrFreg) {
		this.usrFreg = usrFreg;
	}

	public void setUsrId(Short usrId) {
        this.usrId = usrId;
    }

    public void setUsrLact(boolean usrLact) {
        this.usrLact = usrLact;
    }
    
    public void setUsrLog(String usrLog) {
        this.usrLog = usrLog;
    }
    public void setUsrNom(String usrNom) {
        this.usrNom = usrNom;
    }
	public void setUsrPap(String usrPap) {
        this.usrPap = usrPap;
    }

	public void setUsrSap(String usrSap) {
        this.usrSap = usrSap;
    }
	public void setPerfiles(Set<Perfil> perfiles) {
		this.perfiles = perfiles;
	}
	public void setGrupos(Set<Grupo> grupos) {
		this.grupos = grupos;
	}

	public void setEntidades(Set<Entidades> entidades) {
		this.entidades = entidades;
	}
	
	public void setHabilitaAlerta(boolean habilitaAlerta) {
		this.habilitaAlerta = habilitaAlerta;
	}


}
