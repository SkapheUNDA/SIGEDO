package la.netco.persistencia.dto.commons;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "TRANSICION", schema =Schemas.DBO_SCHEMA)
@NamedQueries({
	@NamedQuery(name = Transicion.NAMED_QUERY_BY_CLASIFICACION_TIPO_EVENTO , query=Transicion.QUERY_BY_CLASIFICACION_TIPO_EVENTO),
	@NamedQuery(name= Transicion.NAMED_QUERY_GET_ALL_BY_EVEID,query = Transicion.QUERY_GET_ALL_BY_EVEID),
	@NamedQuery(name=Transicion.NAMED_QUERY_GET_ALL_BY_EVEIDTRSFIN,query=Transicion.QUERY_GET_ALL_BY_EVEIDTRSFIN),
	@NamedQuery(name=Transicion.NAMED_QUERY_BY_TRAMITE_ESTADO_INICIAL,query=Transicion.QUERY_BY_TRAMITE_ESTADO_INICIAL),
	@NamedQuery(name=Transicion.NAMED_QUERY_BY_TRAMITE_ESTADO_INICIAL2,query=Transicion.QUERY_BY_TRAMITE_ESTADO_INICIAL2),
	@NamedQuery(name=Transicion.NAMED_QUERY_GET_ALL_BY_EVEIDTRSINICIAL,query=Transicion.QUERY_GET_ALL_BY_EVEIDTRSINICIAL)
})
public class Transicion implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private TransicionId id;
    private Tramite tramite;
    private Evento evento;
    private Estado estadoByTrsEstfin;
    private Estado estadoByTrsEstini;

    public Transicion() {
    }

    public Transicion(TransicionId id, Tramite tramite, Evento evento, Estado estadoByTrsEstfin, Estado estadoByTrsEstini) {
        this.id = id;
        this.tramite = tramite;
        this.evento = evento;
        this.estadoByTrsEstfin = estadoByTrsEstfin;
        this.estadoByTrsEstini = estadoByTrsEstini;
    }

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "trsTrm", column =
        @Column(name = "TRS_TRM", nullable = false)),
        @AttributeOverride(name = "trsEve", column =
        @Column(name = "TRS_EVE", nullable = false)),
        @AttributeOverride(name = "trsEstini", column =
        @Column(name = "TRS_ESTINI", nullable = false)),
        @AttributeOverride(name = "trsEstfin", column =
        @Column(name = "TRS_ESTFIN", nullable = false))})
    public TransicionId getId() {
        return this.id;
    }

    public void setId(TransicionId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRS_TRM", nullable = false, insertable = false, updatable = false)
    public Tramite getTramite() {
        return this.tramite;
    }

    public void setTramite(Tramite tramite) {
        this.tramite = tramite;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRS_EVE", nullable = false, insertable = false, updatable = false)
    public Evento getEvento() {
        return this.evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRS_ESTFIN", nullable = false, insertable = false, updatable = false)
    public Estado getEstadoByTrsEstfin() {
        return this.estadoByTrsEstfin;
    }

    public void setEstadoByTrsEstfin(Estado estadoByTrsEstfin) {
        this.estadoByTrsEstfin = estadoByTrsEstfin;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRS_ESTINI", nullable = false, insertable = false, updatable = false)
    public Estado getEstadoByTrsEstini() {
        return this.estadoByTrsEstini;
    }

    public void setEstadoByTrsEstini(Estado estadoByTrsEstini) {
        this.estadoByTrsEstini = estadoByTrsEstini;
    }
    
    /**
     *Transicion por tipo de evento y clasificacion 
     */
    public static final String NAMED_QUERY_BY_CLASIFICACION_TIPO_EVENTO = "getTransicionClasificacionTipoEvento";
    public static final String QUERY_BY_CLASIFICACION_TIPO_EVENTO  = " Select transicion FROM Transicion transicion INNER JOIN transicion.tramite.clasificacions clasificacion WHERE  clasificacion.claId = ? AND transicion.evento.tiposevento.tevId = ? ";
        
    
    /**
     * Transicion completa por id de evento
     */
    public static final String NAMED_QUERY_GET_ALL_BY_EVEID = "getAllByEveID";
    public static final String QUERY_GET_ALL_BY_EVEID = "FROM Transicion transicion where transicion.evento.eveId = ? ";
    
    /**
     * Transicion completa por id de evento y estado final
     */
    public static final String NAMED_QUERY_GET_ALL_BY_EVEIDTRSFIN = "getAllByEveIDTrsFin";
    public static final String QUERY_GET_ALL_BY_EVEIDTRSFIN = "FROM Transicion transicion where transicion.evento.eveId = ?  and transicion.estadoByTrsEstfin.estId = ?";
    
    
    /**
     *Transicion por tipo de tramite y estado inicial
     */
    public static final String NAMED_QUERY_BY_TRAMITE_ESTADO_INICIAL = "getTransicionTramiteEstadoInicial";
    public static final String QUERY_BY_TRAMITE_ESTADO_INICIAL = " Select transicion FROM Transicion transicion  WHERE  transicion.estadoByTrsEstini.estId = ? AND transicion.id.trsTrm = ? AND  transicion.evento.tiposevento.tevId > 1 ";
    
    
    public static final String NAMED_QUERY_BY_TRAMITE_ESTADO_INICIAL2 = "getTransicionTramiteEstadoInicial2";
    public static final String QUERY_BY_TRAMITE_ESTADO_INICIAL2 = " Select transicion FROM Transicion transicion  WHERE  transicion.estadoByTrsEstini.estId = ? AND transicion.id.trsTrm = ?"; //Uno quiere poder elegir todos los eventos, sin importar si es inicial o no
    
    /**
     * Transicion completa por id de evento y estado inicial
     */
    public static final String NAMED_QUERY_GET_ALL_BY_EVEIDTRSINICIAL = "getAllByEveIDTrsIni";
    public static final String QUERY_GET_ALL_BY_EVEIDTRSINICIAL = "FROM Transicion transicion where transicion.evento.eveId = ?  and transicion.estadoByTrsEstini.estId = ?";
    
     @Override
    public String toString() {
    	return "Transicion("+estadoByTrsEstini+", "+evento+", "+estadoByTrsEstfin+")";
    }
}
