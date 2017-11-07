package la.netco.persistencia.dto.commons;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import la.netco.core.persistencia.vo.Schemas;
import la.netco.core.persistencia.vo.Usuario;

@Entity
@Table(name = "DEPEND", schema =Schemas.DBO_SCHEMA,  uniqueConstraints =@UniqueConstraint(columnNames = "DEP_NOM"))
@NamedQueries({
	@NamedQuery(name=Depend.NAMED_QUERY_ALL_DEPEND,query=Depend.QUERY_ALL_DEPEND),
	@NamedQuery(name=Depend.NAMED_QUERY_ALL_DEPEND_BY_TIPO_CLASIFICACION,query=Depend.QUERY_ALL_DEPEND_BY_TIPO_CLASIFICACION),
	@NamedQuery(name = Depend.NAMED_QUERY_DEP_COD,query=Depend.QUERY_DEP_COD)
	})
public class Depend implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private short depId;
    //private Usuario usuario;
    private String depCod;
    private String depNom;
    private String depAbr;
    private String depTre;
    private String depLoc;
    private Boolean depReg;
    private Set<Asignaclasificacion> asignaclasificacions = new HashSet<Asignaclasificacion>();
/*    private Set<Entrada> entradas = new HashSet<Entrada>(0);
    private Set<Seguimientosalida> seguimientosalidas = new HashSet<Seguimientosalida>(0);
    private Set<Seguimientoentrada> seguimientoentradas = new HashSet<Seguimientoentrada>(0);
    private Set<Salida> salidas = new HashSet<Salida>(0);
    private Set<Expediente> expedientes = new HashSet<Expediente>(0);
    private Set<Usuario> usuarios = new HashSet<Usuario>(0);
    private Set<Programacion> programacions = new HashSet<Programacion>(0);
    private Set<Detalleprogramacion> detalleprogramacions = new HashSet<Detalleprogramacion>(0);*/

    public Depend() {
    }

    public Depend(short depId) {
        this.depId = depId;
    }

    public Depend(short depId, Usuario usuario, String depCod, String depNom, String depAbr, String depTre, String depLoc, Boolean depReg, Set<Entrada> entradas, Set<Seguimientosalida> seguimientosalidas, Set<Seguimientoentrada> seguimientoentradas, Set<Asignaclasificacion> asignaclasificacions, Set<Salida> salidas, Set<Expediente> expedientes, Set<Usuario> usuarios, Set<Programacion> programacions, Set<Detalleprogramacion> detalleprogramacions) {
        this.depId = depId;
       // this.usuario = usuario;
        this.depCod = depCod;
        this.depNom = depNom;
        this.depAbr = depAbr;
        this.depTre = depTre;
        this.depLoc = depLoc;
        this.depReg = depReg;
      /*  this.entradas = entradas;
        this.seguimientosalidas = seguimientosalidas;
        this.seguimientoentradas = seguimientoentradas;
        this.asignaclasificacions = asignaclasificacions;
        this.salidas = salidas;
        this.expedientes = expedientes;
        this.usuarios = usuarios;
        this.programacions = programacions;
        this.detalleprogramacions = detalleprogramacions;*/
    }

    @Id
    @Column(name = "DEP_ID", unique = true, nullable = false)
    public short getDepId() {
        return this.depId;
    }

    public void setDepId(short depId) {
        this.depId = depId;
    }

/*    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEP_JEF")
    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }*/

    @Column(name = "DEP_COD", length = 20)
    public String getDepCod() {
        return this.depCod;
    }

    public void setDepCod(String depCod) {
        this.depCod = depCod;
    }

    /**
     * Retorna el nombre de la dependencia
     * 
     * @return depNom
     */
    @Column(name = "DEP_NOM", unique = true, length = 100)
    public String getDepNom() {
        return this.depNom;
    }

    public void setDepNom(String depNom) {
        this.depNom = depNom;
    }

    @Column(name = "DEP_ABR", length = 50)
    public String getDepAbr() {
        return this.depAbr;
    }

    public void setDepAbr(String depAbr) {
        this.depAbr = depAbr;
    }

    @Column(name = "DEP_TRE", length = 50)
    public String getDepTre() {
        return this.depTre;
    }

    public void setDepTre(String depTre) {
        this.depTre = depTre;
    }

    @Column(name = "DEP_LOC", length = 100)
    public String getDepLoc() {
        return this.depLoc;
    }

    public void setDepLoc(String depLoc) {
        this.depLoc = depLoc;
    }

    @Column(name = "DEP_REG")
    public Boolean getDepReg() {
        return this.depReg;
    }

    public void setDepReg(Boolean depReg) {
        this.depReg = depReg;
    }
    
    
    /**
     *  Relacion con Asignaclasificacion, se mantiene esta relacion para realizar un join hasta Clasificacion y traer las dependencias de acuerdo al tipo
     *  Asignaclasificacion es la entidad donde esta configurado los campos del formulario para correspondencia
     * 
     * @return asignaclasificacions
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "depend")
    public Set<Asignaclasificacion> getAsignaclasificacions() {
        return this.asignaclasificacions;
    }
    
    public void setAsignaclasificacions(Set<Asignaclasificacion> asignaclasificacions) {
        this.asignaclasificacions = asignaclasificacions;
    }

 /*   @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "ENTRADADEPEND", schema = "dbo", catalog = "AUTOR", joinColumns = {
        @JoinColumn(name = "END_DEP", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "END_ENT", nullable = false, updatable = false)})
    public Set<Entrada> getEntradas() {
        return this.entradas;
    }

    public void setEntradas(Set<Entrada> entradas) {
        this.entradas = entradas;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "depend")
    public Set<Seguimientosalida> getSeguimientosalidas() {
        return this.seguimientosalidas;
    }

    public void setSeguimientosalidas(Set<Seguimientosalida> seguimientosalidas) {
        this.seguimientosalidas = seguimientosalidas;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "depend")
    public Set<Seguimientoentrada> getSeguimientoentradas() {
        return this.seguimientoentradas;
    }

    public void setSeguimientoentradas(Set<Seguimientoentrada> seguimientoentradas) {
        this.seguimientoentradas = seguimientoentradas;
    }


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "depend")
    public Set<Salida> getSalidas() {
        return this.salidas;
    }

    public void setSalidas(Set<Salida> salidas) {
        this.salidas = salidas;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "depend")
    public Set<Expediente> getExpedientes() {
        return this.expedientes;
    }

    public void setExpedientes(Set<Expediente> expedientes) {
        this.expedientes = expedientes;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "EMPLEADO", schema = "dbo", catalog = "AUTOR", joinColumns = {
        @JoinColumn(name = "EMP_DEP", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "EMP_USR", nullable = false, updatable = false)})
    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "depend")
    public Set<Programacion> getProgramacions() {
        return this.programacions;
    }

    public void setProgramacions(Set<Programacion> programacions) {
        this.programacions = programacions;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "depend")
    public Set<Detalleprogramacion> getDetalleprogramacions() {
        return this.detalleprogramacions;
    }

    public void setDetalleprogramacions(Set<Detalleprogramacion> detalleprogramacions) {
        this.detalleprogramacions = detalleprogramacions;
    }*/
    
    public static final String NAMED_QUERY_ALL_DEPEND = "getAllDepend";
    public static final String QUERY_ALL_DEPEND = "FROM Depend depend order by depend.depNom asc";
    
    public static final String NAMED_QUERY_DEP_COD= "getDepCod";
    public static final String QUERY_DEP_COD= "FROM Depend depend where depend.depId = ?";
    
    
    public static final String NAMED_QUERY_ALL_DEPEND_BY_TIPO_CLASIFICACION = "getAllDependByTipoClasificacion";
    public static final String QUERY_ALL_DEPEND_BY_TIPO_CLASIFICACION = "SELECT DISTINCT  depend FROM Depend depend  INNER JOIN depend.asignaclasificacions asignacion INNER JOIN asignacion.clasificacion clasificacion WHERE  clasificacion.claTip = ?  order by depend.depNom asc";
    
    
}
