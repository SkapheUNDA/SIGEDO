package la.netco.maestras.persistence.dto;

import java.io.Serializable;

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

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "PROPTABLAS", schema = Schemas.REL_SCHEMA)
@NamedQueries({
		@NamedQuery(name = PropTablas.NAMED_QUERY_BY_PROP, query = PropTablas.QUERY_BY_PROP),
		@NamedQuery(name = PropTablas.NAMED_QUERYBY_PROP_VI, query = PropTablas.QUERY_BY_VISIBLES) })
public class PropTablas implements Serializable {


	private static final long serialVersionUID = 1295612349001351866L;

	private int prop_id;

	private String prop_cam;

	private String prop_eti;

	private boolean prop_esedi;

	private boolean prop_esreq;

	private String prop_tipdat;

	private boolean prop_espk;

	private boolean prop_esvi;

	private TablasMaestras tablasMaestras;

	public PropTablas() {

	}

	public PropTablas(int prop_id, String prop_cam, String prop_eti,
			boolean prop_esedi, boolean prop_esreq, String prop_tipdat,
			boolean prop_espk, boolean prop_esvi, TablasMaestras tablasMaestras) {
		this.prop_id = prop_id;
		this.prop_cam = prop_cam;
		this.prop_eti = prop_eti;
		this.prop_esedi = prop_esedi;
		this.prop_esreq = prop_esreq;
		this.prop_tipdat = prop_tipdat;
		this.prop_espk = prop_espk;
		this.prop_esvi = prop_esvi;
		this.tablasMaestras = tablasMaestras;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROP_ID", unique = true, nullable = false, insertable = false)
	public int getProp_id() {
		return prop_id;
	}

	public void setProp_id(int prop_id) {
		this.prop_id = prop_id;
	}

	@Column(name = "PROP_CAM", nullable = false, length = 10)
	public String getProp_cam() {
		return prop_cam;
	}

	public void setProp_cam(String prop_cam) {
		this.prop_cam = prop_cam;
	}

	@Column(name = "PROP_ETI", nullable = false, length = 100)
	public String getProp_eti() {
		return prop_eti;
	}

	public void setProp_eti(String prop_eti) {
		this.prop_eti = prop_eti;
	}

	@Column(name = "PROP_ESEDI", nullable = false)
	public boolean isProp_esedi() {
		return prop_esedi;
	}

	public void setProp_esedi(boolean prop_esedi) {
		this.prop_esedi = prop_esedi;
	}

	@Column(name = "PROP_ESREQ", nullable = false)
	public boolean isProp_esreq() {
		return prop_esreq;
	}

	public void setProp_esreq(boolean prop_esreq) {
		this.prop_esreq = prop_esreq;
	}

	@Column(name = "PROP_TIPDAT", length = 30, nullable = false)
	public String getProp_tipdat() {
		return prop_tipdat;
	}

	public void setProp_tipdat(String prop_tipdat) {
		this.prop_tipdat = prop_tipdat;
	}

	@Column(name = "PROP_ESPK", length = 10, nullable = false)
	public boolean isProp_espk() {
		return prop_espk;
	}

	public void setProp_espk(boolean prop_espk) {
		this.prop_espk = prop_espk;
	}

	@Column(name = "PROP_ESVI", length = 10, nullable = false)
	public boolean isProp_esvi() {
		return prop_esvi;
	}

	public void setProp_esvi(boolean prop_esvi) {
		this.prop_esvi = prop_esvi;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TMA", nullable = false)
	@ForeignKey(name = "FK_PROPTABLAS_TABLASMAESTRAS")
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	public TablasMaestras getTablasMaestras() {
		return tablasMaestras;
	}

	public void setTablasMaestras(TablasMaestras tablasMaestras) {
		this.tablasMaestras = tablasMaestras;
	}

	public static final String NAMED_QUERY_BY_PROP = "getAllPropiedades";
	public static final String QUERY_BY_PROP = "from PropTablas proptablas order by proptablas.prop_id asc";

	public static final String NAMED_QUERYBY_PROP_VI = "getAllPropiedadesVisibles";
	public static final String QUERY_BY_VISIBLES = "from PropTablas proptablas where proptablas.prop_esvi = '1' and proptablas.tablasMaestras.id_tma  = ?";

	
}
