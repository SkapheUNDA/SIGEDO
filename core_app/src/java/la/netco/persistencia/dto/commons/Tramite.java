package la.netco.persistencia.dto.commons;

import java.io.Serializable;
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


@Entity
@Table(name = "TRAMITE", schema = Schemas.DBO_SCHEMA)
@NamedQueries({
	@NamedQuery(name = Tramite.NAMED_QUERY_ALL_TRM , query=Tramite.QUERY_ALL_TRM),
	@NamedQuery(name = Tramite.NAMED_QUERY_DISTINCT_TIPO_CLASIFICACION , query=Tramite.QUERY_DISTINCT_TIPO_CLASIFICACION)
})
public class Tramite implements Serializable {

   
	private static final long serialVersionUID = 1L;
	private short trmId;
    private String trmNom;
    private boolean trmLreg;
//	private Set<Expediente> expedientes = new HashSet<Expediente>(0);
	private Set<Clasificacion> clasificacions = new HashSet<Clasificacion>();
//	private Set<Transicion> transicions = new HashSet<Transicion>(0);
//	private Set<Evento> eventos = new HashSet<Evento>(0);
//	private Set<Estado> estados = new HashSet<Estado>(0);

    public Tramite() {
    }

//    public Tramite(short trmId, boolean trmLreg) {
//        this.trmId = trmId;
//        this.trmLreg = trmLreg;
//    }
//
//    public Tramite(short trmId, String trmNom, boolean trmLreg, Set<Expediente> expedientes, Set<Clasificacion> clasificacions, Set<Transicion> transicions, Set<Evento> eventos, Set<Estado> estados) {
//        this.trmId = trmId;
//        this.trmNom = trmNom;
//        this.trmLreg = trmLreg;
//        this.expedientes = expedientes;
//        this.clasificacions = clasificacions;
//        this.transicions = transicions;
//        this.eventos = eventos;
//        this.estados = estados;
//    }

    /**
     * Id de tramite
     * 
     * @return
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRM_ID", unique = true, nullable = false)
    public short getTrmId() {
        return this.trmId;
    }

    public void setTrmId(short trmId) {
        this.trmId = trmId;
    }

    /**
     * 
     * Nombre de tramite
     * @return
     */
    @Column(name = "TRM_NOM", length = 50)
    public String getTrmNom() {
        return this.trmNom;
    }

    public void setTrmNom(String trmNom) {
        this.trmNom = trmNom;
    }

    /**
     * Clasificacipn para l registro de obras porv  el tramite
     * 
     * @return
     */
    @Column(name = "TRM_LREG", nullable = false)
    public boolean isTrmLreg() {
        return this.trmLreg;
    }

    public void setTrmLreg(boolean trmLreg) {
        this.trmLreg = trmLreg;
    }

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tramite")
//    public Set<Expediente> getExpedientes() {
//        return this.expedientes;
//    }
//
//    public void setExpedientes(Set<Expediente> expedientes) {
//        this.expedientes = expedientes;
//    }
//
	    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tramite")
	    public Set<Clasificacion> getClasificacions() {
	        return this.clasificacions;
	    }
	
	    public void setClasificacions(Set<Clasificacion> clasificacions) {
	        this.clasificacions = clasificacions;
	    }
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tramite")
//    public Set<Transicion> getTransicions() {
//        return this.transicions;
//    }
//
//    public void setTransicions(Set<Transicion> transicions) {
//        this.transicions = transicions;
//    }
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tramite")
//    public Set<Evento> getEventos() {
//        return this.eventos;
//    }
//
//    public void setEventos(Set<Evento> eventos) {
//        this.eventos = eventos;
//    }
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tramite")
//    public Set<Estado> getEstados() {
//        return this.estados;
//    }
//
//    public void setEstados(Set<Estado> estados) {
//        this.estados = estados;
//    }
    
    public static final String NAMED_QUERY_ALL_TRM = "getAllTramites";
    public static final String QUERY_ALL_TRM = "FROM Tramite tramite order by tramite.trmNom asc";

    public static final String NAMED_QUERY_DISTINCT_TIPO_CLASIFICACION = "getAllTramitesTipoClasificacion";
    public static final String QUERY_DISTINCT_TIPO_CLASIFICACION = "Select DISTINCT tramite FROM Tramite tramite INNER JOIN tramite.clasificacions clasificacion WHERE  clasificacion.claTip = ?  order by tramite.trmNom asc";
    
}
