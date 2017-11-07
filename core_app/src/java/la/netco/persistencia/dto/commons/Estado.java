package la.netco.persistencia.dto.commons;
// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

/*import java.util.HashSet;
import java.util.Set;*/
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
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

/*import la.netco.persistencia.dto.commons.base.Anotacionesexpedientes;
import la.netco.persistencia.dto.commons.base.Expediente;
import la.netco.persistencia.dto.commons.base.Salida;
import la.netco.persistencia.dto.commons.base.Seguimientoentrada;
import la.netco.persistencia.dto.commons.base.Seguimientoexpediente;
import la.netco.persistencia.dto.commons.base.Seguimientosalida;
import la.netco.persistencia.dto.commons.base.Tramite;
import la.netco.persistencia.dto.commons.base.Transicion;
import la.netco.persistencia.dto.commons.base.Usuario;*/


@Entity
@Table(name = "ESTADO", schema = Schemas.DBO_SCHEMA)
@NamedQueries({
	@NamedQuery(name=Estado.NAMED_QUERY_GET_NOMEST,query=Estado.QUERY_GET_NOMEST),
	@NamedQuery(name=Estado.NAMED_QUERY_GET_BY_TRAMITE,query=Estado.QUERY_GET_BY_TRAMITE)
})
public class Estado implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private short estId;
    private Tramite tramite;
    private String estNom;
    private boolean estIni;
    private boolean estFin;
 /*   private Set<Seguimientoexpediente> seguimientoexpedientesForSgeEst = new HashSet<Seguimientoexpediente>(0);
    private Set<Salida> salidas = new HashSet<Salida>(0);
    private Set<Anotacionesexpedientes> anotacionesexpedienteses = new HashSet<Anotacionesexpedientes>(0);
    private Set<Transicion> transicionsForTrsEstini = new HashSet<Transicion>(0);
    private Set<Seguimientoentrada> seguimientoentradasForSenEstini = new HashSet<Seguimientoentrada>(0);
    private Set<Expediente> expedientes = new HashSet<Expediente>(0);
    private Set<Seguimientosalida> seguimientosalidasForSsaEst = new HashSet<Seguimientosalida>(0);
    private Set<Seguimientoentrada> seguimientoentradasForSenEst = new HashSet<Seguimientoentrada>(0);
    private Set<Transicion> transicionsForTrsEstfin = new HashSet<Transicion>(0);
    private Set<Seguimientosalida> seguimientosalidasForSsaEstini = new HashSet<Seguimientosalida>(0);
    private Set<Usuario> usuarios = new HashSet<Usuario>(0);
    private Set<Seguimientoexpediente> seguimientoexpedientesForSgeEstini = new HashSet<Seguimientoexpediente>(0);*/

    @Override
    public String toString() {
    	return "Estado("+estId+", "+estNom+")";
    }
    
    public Estado() {
    }

    public Estado(short estId, boolean estIni, boolean estFin) {
        this.estId = estId;
        this.estIni = estIni;
        this.estFin = estFin;
    }
    /*
    public Estado(short estId, Tramite tramite, String estNom, boolean estIni, boolean estFin, Set<Seguimientoexpediente> seguimientoexpedientesForSgeEst, Set<Salida> salidas, Set<Anotacionesexpedientes> anotacionesexpedienteses, Set<Transicion> transicionsForTrsEstini, Set<Seguimientoentrada> seguimientoentradasForSenEstini, Set<Expediente> expedientes, Set<Seguimientosalida> seguimientosalidasForSsaEst, Set<Seguimientoentrada> seguimientoentradasForSenEst, Set<Transicion> transicionsForTrsEstfin, Set<Seguimientosalida> seguimientosalidasForSsaEstini, Set<Usuario> usuarios, Set<Seguimientoexpediente> seguimientoexpedientesForSgeEstini) {
        this.estId = estId;
        this.tramite = tramite;
        this.estNom = estNom;
        this.estIni = estIni;
        this.estFin = estFin;
       this.seguimientoexpedientesForSgeEst = seguimientoexpedientesForSgeEst;
        this.salidas = salidas;
        this.anotacionesexpedienteses = anotacionesexpedienteses;
        this.transicionsForTrsEstini = transicionsForTrsEstini;
        this.seguimientoentradasForSenEstini = seguimientoentradasForSenEstini;
        this.expedientes = expedientes;
        this.seguimientosalidasForSsaEst = seguimientosalidasForSsaEst;
        this.seguimientoentradasForSenEst = seguimientoentradasForSenEst;
        this.transicionsForTrsEstfin = transicionsForTrsEstfin;
        this.seguimientosalidasForSsaEstini = seguimientosalidasForSsaEstini;
        this.usuarios = usuarios;
        this.seguimientoexpedientesForSgeEstini = seguimientoexpedientesForSgeEstini;
    }*/

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "EST_ID", unique = true, nullable = false)
    public short getEstId() {
        return this.estId;
    }

    public void setEstId(short estId) {
        this.estId = estId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EST_TRM")
    public Tramite getTramite() {
        return this.tramite;
    }

    public void setTramite(Tramite tramite) {
        this.tramite = tramite;
    }

    /**
     * 
     * Rerorna nombre de estado
     * 
     * @return estNom
     */
    @Column(name = "EST_NOM", length = 50)
    public String getEstNom() {
        return this.estNom;
    }

    public void setEstNom(String estNom) {
        this.estNom = estNom;
    }

    @Column(name = "EST_INI", nullable = false)
    public boolean isEstIni() {
        return this.estIni;
    }

    public void setEstIni(boolean estIni) {
        this.estIni = estIni;
    }

    @Column(name = "EST_FIN", nullable = false)
    public boolean isEstFin() {
        return this.estFin;
    }

    public void setEstFin(boolean estFin) {
        this.estFin = estFin;
    }

/*    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "estadoBySgeEst")
    public Set<Seguimientoexpediente> getSeguimientoexpedientesForSgeEst() {
        return this.seguimientoexpedientesForSgeEst;
    }

    public void setSeguimientoexpedientesForSgeEst(Set<Seguimientoexpediente> seguimientoexpedientesForSgeEst) {
        this.seguimientoexpedientesForSgeEst = seguimientoexpedientesForSgeEst;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "estado")
    public Set<Salida> getSalidas() {
        return this.salidas;
    }

    public void setSalidas(Set<Salida> salidas) {
        this.salidas = salidas;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "estado")
    public Set<Anotacionesexpedientes> getAnotacionesexpedienteses() {
        return this.anotacionesexpedienteses;
    }

    public void setAnotacionesexpedienteses(Set<Anotacionesexpedientes> anotacionesexpedienteses) {
        this.anotacionesexpedienteses = anotacionesexpedienteses;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "estadoByTrsEstini")
    public Set<Transicion> getTransicionsForTrsEstini() {
        return this.transicionsForTrsEstini;
    }

    public void setTransicionsForTrsEstini(Set<Transicion> transicionsForTrsEstini) {
        this.transicionsForTrsEstini = transicionsForTrsEstini;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "estadoBySenEstini")
    public Set<Seguimientoentrada> getSeguimientoentradasForSenEstini() {
        return this.seguimientoentradasForSenEstini;
    }

    public void setSeguimientoentradasForSenEstini(Set<Seguimientoentrada> seguimientoentradasForSenEstini) {
        this.seguimientoentradasForSenEstini = seguimientoentradasForSenEstini;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "estado")
    public Set<Expediente> getExpedientes() {
        return this.expedientes;
    }

    public void setExpedientes(Set<Expediente> expedientes) {
        this.expedientes = expedientes;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "estadoBySsaEst")
    public Set<Seguimientosalida> getSeguimientosalidasForSsaEst() {
        return this.seguimientosalidasForSsaEst;
    }

    public void setSeguimientosalidasForSsaEst(Set<Seguimientosalida> seguimientosalidasForSsaEst) {
        this.seguimientosalidasForSsaEst = seguimientosalidasForSsaEst;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "estadoBySenEst")
    public Set<Seguimientoentrada> getSeguimientoentradasForSenEst() {
        return this.seguimientoentradasForSenEst;
    }

    public void setSeguimientoentradasForSenEst(Set<Seguimientoentrada> seguimientoentradasForSenEst) {
        this.seguimientoentradasForSenEst = seguimientoentradasForSenEst;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "estadoByTrsEstfin")
    public Set<Transicion> getTransicionsForTrsEstfin() {
        return this.transicionsForTrsEstfin;
    }

    public void setTransicionsForTrsEstfin(Set<Transicion> transicionsForTrsEstfin) {
        this.transicionsForTrsEstfin = transicionsForTrsEstfin;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "estadoBySsaEstini")
    public Set<Seguimientosalida> getSeguimientosalidasForSsaEstini() {
        return this.seguimientosalidasForSsaEstini;
    }

    public void setSeguimientosalidasForSsaEstini(Set<Seguimientosalida> seguimientosalidasForSsaEstini) {
        this.seguimientosalidasForSsaEstini = seguimientosalidasForSsaEstini;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "USUARIOEST", schema = "dbo", catalog = "AUTOR", joinColumns = {
        @JoinColumn(name = "UES_EST", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "UES_USR", nullable = false, updatable = false)})
    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "estadoBySgeEstini")
    public Set<Seguimientoexpediente> getSeguimientoexpedientesForSgeEstini() {
        return this.seguimientoexpedientesForSgeEstini;
    }

    public void setSeguimientoexpedientesForSgeEstini(Set<Seguimientoexpediente> seguimientoexpedientesForSgeEstini) {
        this.seguimientoexpedientesForSgeEstini = seguimientoexpedientesForSgeEstini;
    }*/
    
    public static final String NAMED_QUERY_GET_NOMEST =  "getNomEstByIdEst";
    public static final String QUERY_GET_NOMEST = "SELECT estado.estNom FROM Estado estado WHERE estado.estId = ?";
    
    public static final String NAMED_QUERY_GET_BY_TRAMITE =  "getEstadoByTramite";
    public static final String QUERY_GET_BY_TRAMITE = "SELECT estado FROM Estado estado WHERE estado.tramite.trmId = ?";

    
}
