package la.netco.persistencia.dto.commons;

import java.util.Date;
import javax.persistence.*;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "ANOTACIONESEXPEDIENTES", schema = Schemas.DBO_SCHEMA)
@NamedQueries({ @NamedQuery(name = Anotacionesexpedientes.NAMED_QUERY_GET_ALL_BYEXP, query = Anotacionesexpedientes.QUERY_GET_ALL_BYEXP) })
public class Anotacionesexpedientes implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int anoId;
	private Estado estado;
	private Expediente expediente;
	private String anoDes;
	private Date anoFec;
	private String anoNom;
	private Date anoFcr;
	private Date anoFve;
	private boolean anoLfve;

	public Anotacionesexpedientes() {
	}

	public Anotacionesexpedientes(int anoId, boolean anoLfve) {
		this.anoId = anoId;
		this.anoLfve = anoLfve;
	}

	public Anotacionesexpedientes(int anoId, Estado estado,
			Expediente expediente, String anoDes, Date anoFec, String anoNom,
			Date anoFcr, Date anoFve, boolean anoLfve) {
		this.anoId = anoId;
		this.estado = estado;
		this.expediente = expediente;
		this.anoDes = anoDes;
		this.anoFec = anoFec;
		this.anoNom = anoNom;
		this.anoFcr = anoFcr;
		this.anoFve = anoFve;
		this.anoLfve = anoLfve;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ANO_ID", unique = true, nullable = false)
	public int getAnoId() {
		return this.anoId;
	}

	public void setAnoId(int anoId) {
		this.anoId = anoId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ANO_EST")
	public Estado getEstado() {
		return this.estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ANO_EXP")
	public Expediente getExpediente() {
		return this.expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	@Column(name = "ANO_DES", length = 300)
	public String getAnoDes() {
		return this.anoDes;
	}

	public void setAnoDes(String anoDes) {
		this.anoDes = anoDes;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ANO_FEC", length = 23)
	public Date getAnoFec() {
		return this.anoFec;
	}

	public void setAnoFec(Date anoFec) {
		this.anoFec = anoFec;
	}

	@Column(name = "ANO_NOM", length = 100)
	public String getAnoNom() {
		return this.anoNom;
	}

	public void setAnoNom(String anoNom) {
		this.anoNom = anoNom;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ANO_FCR", length = 23)
	public Date getAnoFcr() {
		return this.anoFcr;
	}

	public void setAnoFcr(Date anoFcr) {
		this.anoFcr = anoFcr;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ANO_FVE", length = 23)
	public Date getAnoFve() {
		return this.anoFve;
	}

	public void setAnoFve(Date anoFve) {
		this.anoFve = anoFve;
	}

	@Column(name = "ANO_LFVE", nullable = false)
	public boolean isAnoLfve() {
		return this.anoLfve;
	}

	public void setAnoLfve(boolean anoLfve) {
		this.anoLfve = anoLfve;
	}

	public static final String NAMED_QUERY_GET_ALL_BYEXP = "getAllAnotacionesByExp";
	public static final String QUERY_GET_ALL_BYEXP = "FROM Anotacionesexpedientes anotacionesexpedientes where anotacionesexpedientes.expediente.expId = ?";

}
