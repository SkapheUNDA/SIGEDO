package la.netco.persistencia.dto.commons;

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

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "EVENTO", schema = Schemas.DBO_SCHEMA)
@NamedQueries(value = { 
		@NamedQuery(name = Evento.NAMED_QUERY_GET_EVEBYEXP, query = Evento.QUERY_GET_EVEBYEXP),
		@NamedQuery(name=Evento.NAMED_QUERY_GET_EVEID,query=Evento.QUERY_GET_EVEID)
})
public class Evento implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Short eveId;
	private Tiposevento tiposevento;
	private Tramite tramite;
	private String eveNom;
	private Boolean eveLdoc;
	private Boolean eveLgendoc;
	private String eveArc;
	private Set<Transicion> transicions = new HashSet<Transicion>();

	// private Set<Seguimientosalida> seguimientosalidas = new
	// HashSet<Seguimientosalida>(0);
	// private Set<Seguimientoexpediente> seguimientoexpedientes = new
	// HashSet<Seguimientoexpediente>(0);
	// private Set<Seguimientoentrada> seguimientoentradas = new
	// HashSet<Seguimientoentrada>(0);

	@Override
	public String toString() {
		return "Evento("+eveId+", "+eveNom+")";
	}
	
	public Evento() {
	}

	public Evento(short eveId) {
		this.eveId = eveId;
	}

	// public Evento(short eveId, Tiposevento tiposevento, Tramite tramite,
	// String eveNom, Boolean eveLdoc, Boolean eveLgendoc, String eveArc,
	// Set<Transicion> transicions, Set<Seguimientosalida> seguimientosalidas,
	// Set<Seguimientoexpediente> seguimientoexpedientes,
	// Set<Seguimientoentrada> seguimientoentradas) {
	// this.eveId = eveId;
	// this.tiposevento = tiposevento;
	// this.tramite = tramite;
	// this.eveNom = eveNom;
	// this.eveLdoc = eveLdoc;
	// this.eveLgendoc = eveLgendoc;
	// this.eveArc = eveArc;
	// this.transicions = transicions;
	// this.seguimientosalidas = seguimientosalidas;
	// this.seguimientoexpedientes = seguimientoexpedientes;
	// this.seguimientoentradas = seguimientoentradas;
	// }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EVE_ID", unique = true, nullable = false)
	public Short getEveId() {
		return this.eveId;
	}

	public void setEveId(Short eveId) {
		this.eveId = eveId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVE_TEV")
	public Tiposevento getTiposevento() {
		return this.tiposevento;
	}

	public void setTiposevento(Tiposevento tiposevento) {
		this.tiposevento = tiposevento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVE_TRM")
	public Tramite getTramite() {
		return this.tramite;
	}

	public void setTramite(Tramite tramite) {
		this.tramite = tramite;
	}

	@Column(name = "EVE_NOM", length = 50)
	public String getEveNom() {
		return this.eveNom;
	}

	public void setEveNom(String eveNom) {
		this.eveNom = eveNom;
	}

	@Column(name = "EVE_LDOC")
	public Boolean getEveLdoc() {
		return this.eveLdoc;
	}

	public void setEveLdoc(Boolean eveLdoc) {
		this.eveLdoc = eveLdoc;
	}

	@Column(name = "EVE_LGENDOC")
	public Boolean getEveLgendoc() {
		return this.eveLgendoc;
	}

	public void setEveLgendoc(Boolean eveLgendoc) {
		this.eveLgendoc = eveLgendoc;
	}

	@Column(name = "EVE_ARC", length = 2000)
	public String getEveArc() {
		return this.eveArc;
	}

	public void setEveArc(String eveArc) {
		this.eveArc = eveArc;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "evento")
	public Set<Transicion> getTransicions() {
		return this.transicions;
	}

	public void setTransicions(Set<Transicion> transicions) {
		this.transicions = transicions;
	}

	//
	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "evento")
	// public Set<Seguimientosalida> getSeguimientosalidas() {
	// return this.seguimientosalidas;
	// }
	//
	// public void setSeguimientosalidas(Set<Seguimientosalida>
	// seguimientosalidas) {
	// this.seguimientosalidas = seguimientosalidas;
	// }
	//
	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "evento")
	// public Set<Seguimientoexpediente> getSeguimientoexpedientes() {
	// return this.seguimientoexpedientes;
	// }
	//
	// public void setSeguimientoexpedientes(Set<Seguimientoexpediente>
	// seguimientoexpedientes) {
	// this.seguimientoexpedientes = seguimientoexpedientes;
	// }
	//
	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "evento")
	// public Set<Seguimientoentrada> getSeguimientoentradas() {
	// return this.seguimientoentradas;
	// }
	//
	// public void setSeguimientoentradas(Set<Seguimientoentrada>
	// seguimientoentradas) {
	// this.seguimientoentradas = seguimientoentradas;
	// }

	/**
	 * Eventos por expediente
	 */
	public static final String NAMED_QUERY_GET_EVEBYEXP = "getEventosByExp";
	public static final String QUERY_GET_EVEBYEXP = "SELECT evento FROM Evento evento INNER JOIN evento.transicions  transicion WHERE   transicion.tramite.trmId = ?  AND transicion.estadoByTrsEstini.estId = ? AND evento.tiposevento.tevId = ? ";
	
	/**
	 * Id de evento por tramite y por tipo de evento
	 */
	public static final String NAMED_QUERY_GET_EVEID = "getEventoID";
	public static final String QUERY_GET_EVEID = "SELECT evento.eveId FROM Evento evento WHERE  evento.tramite.trmId = ? AND evento.tiposevento.tevId = ? ";
	
}
