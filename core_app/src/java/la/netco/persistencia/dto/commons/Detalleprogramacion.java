package la.netco.persistencia.dto.commons;

import java.util.Date;
import javax.persistence.*;

import la.netco.core.persistencia.vo.Schemas;
import la.netco.core.persistencia.vo.Usuario;

@Entity
@Table(name = "DETALLEPROGRAMACION", schema = Schemas.DBO_SCHEMA)
@NamedQueries({
		@NamedQuery(name = Detalleprogramacion.NAMED_QUERY_GET_BYPROG, query = Detalleprogramacion.QUERY_GET_BYPROG),
		@NamedQuery(name = Detalleprogramacion.NAMED_QUERY_GET_BYPOR, query = Detalleprogramacion.QUERY_GET_BYPOR) })
public class Detalleprogramacion implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int dprId;
	private Salida salida;
	private Depend depend;
	private Programacion programacion;
	private Usuario usuario;
	private Date dprFve;
	private Short dprPor;
	private Date dprFen;
	private String dprCom;
	private String dprPrd;
	private Date dprFre;
	private Date dprFeo;
	private String dprNsa;

	public Detalleprogramacion() {
	}

	public Detalleprogramacion(int dprId) {
		this.dprId = dprId;
	}

	public Detalleprogramacion(int dprId, Salida salida, Depend depend,
			Programacion programacion, Usuario usuario, Date dprFve,
			Short dprPor, Date dprFen, String dprCom, String dprPrd,
			Date dprFre, Date dprFeo, String dprNsa) {
		this.dprId = dprId;
		this.salida = salida;
		this.depend = depend;
		this.programacion = programacion;
		this.usuario = usuario;
		this.dprFve = dprFve;
		this.dprPor = dprPor;
		this.dprFen = dprFen;
		this.dprCom = dprCom;
		this.dprPrd = dprPrd;
		this.dprFre = dprFre;
		this.dprFeo = dprFeo;
		this.dprNsa = dprNsa;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DPR_ID", unique = true, nullable = false)
	public int getDprId() {
		return this.dprId;
	}

	public void setDprId(int dprId) {
		this.dprId = dprId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DPR_SAL")
	public Salida getSalida() {
		return this.salida;
	}

	public void setSalida(Salida salida) {
		this.salida = salida;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DPR_DEP")
	public Depend getDepend() {
		return this.depend;
	}

	public void setDepend(Depend depend) {
		this.depend = depend;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DPR_PRG")
	public Programacion getProgramacion() {
		return this.programacion;
	}

	public void setProgramacion(Programacion programacion) {
		this.programacion = programacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DPR_USR")
	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DPR_FVE", length = 23)
	public Date getDprFve() {
		return this.dprFve;
	}

	public void setDprFve(Date dprFve) {
		this.dprFve = dprFve;
	}

	@Column(name = "DPR_POR", precision = 3, scale = 0)
	public Short getDprPor() {
		return this.dprPor;
	}

	public void setDprPor(Short dprPor) {
		this.dprPor = dprPor;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DPR_FEN", length = 23)
	public Date getDprFen() {
		return this.dprFen;
	}

	public void setDprFen(Date dprFen) {
		this.dprFen = dprFen;
	}

	@Column(name = "DPR_COM", length = 1000)
	public String getDprCom() {
		return this.dprCom;
	}

	public void setDprCom(String dprCom) {
		this.dprCom = dprCom;
	}

	@Column(name = "DPR_PRD", length = 100)
	public String getDprPrd() {
		return this.dprPrd;
	}

	public void setDprPrd(String dprPrd) {
		this.dprPrd = dprPrd;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DPR_FRE", length = 23)
	public Date getDprFre() {
		return this.dprFre;
	}

	public void setDprFre(Date dprFre) {
		this.dprFre = dprFre;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DPR_FEO", length = 23)
	public Date getDprFeo() {
		return this.dprFeo;
	}

	public void setDprFeo(Date dprFeo) {
		this.dprFeo = dprFeo;
	}

	@Column(name = "DPR_NSA", length = 50)
	public String getDprNsa() {
		return this.dprNsa;
	}

	public void setDprNsa(String dprNsa) {
		this.dprNsa = dprNsa;
	}

	public static final String NAMED_QUERY_GET_BYPROG = "getByProg";
	public static final String QUERY_GET_BYPROG = "FROM Detalleprogramacion detalleprogramacion where detalleprogramacion.programacion.prgId = ?";

	public static final String NAMED_QUERY_GET_BYPOR = "getByPorc";
	public static final String QUERY_GET_BYPOR = "FROM Detalleprogramacion detalleprogramacion where detalleprogramacion.dprPor  <> ? and detalleprogramacion.programacion.prgId = ? ";

}
