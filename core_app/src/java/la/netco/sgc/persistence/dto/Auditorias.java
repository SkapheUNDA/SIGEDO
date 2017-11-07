package la.netco.sgc.persistence.dto;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "AUDITORIAS", schema = Schemas.SGC_SCHEMA)
@NamedQueries(value = {
		@NamedQuery(name = Auditorias.NAMED_QUERY_FIND_BY_ID, query = Auditorias.QUERY_FIND_BY_ID)
		 })
public class Auditorias implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
    private Integer audId;
    private Entidades audSociedad;
    private TipoAuditoria audTipo;
    private String audPeriodo;
    private TipoProgramacion audTipoProgramacion; 
    private Date audFechaEnvioOficio;
    private Date audFechaTrabajoCampo;
    private Date audFechaInforme;
    private Integer audResponsable;
    private String audFuncionario;
    private String audAlcance;
        
	public static final String NAMED_QUERY_FIND_BY_ID = "findAuditoriaById";
	public static final String QUERY_FIND_BY_ID = "from Auditorias a where a.audId = ?";

	public Auditorias() {

	}
	
	public Auditorias(Integer audId, Entidades audSociedad,
			TipoAuditoria audTipo, String audPeriodo,
			TipoProgramacion audTipoProgramacion, Date audFechaEnvioOficio,
			Date audFechaTrabajoCampo, Date audFechaInforme,
			Integer audResponsable, String audFuncionario, String audAlcance) {
		super();
		this.audId = audId;
		this.audSociedad = audSociedad;
		this.audTipo = audTipo;
		this.audPeriodo = audPeriodo;
		this.audTipoProgramacion = audTipoProgramacion;
		this.audFechaEnvioOficio = audFechaEnvioOficio;
		this.audFechaTrabajoCampo = audFechaTrabajoCampo;
		this.audFechaInforme = audFechaInforme;
		this.audResponsable = audResponsable;
		this.audFuncionario = audFuncionario;
		this.audAlcance = audAlcance;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "AUD_ID", nullable = false, unique = true)
	public Integer getAudId() {
		return audId;
	}

	public void setAudId(Integer audId) {
		this.audId = audId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AUD_SOCIEDAD")	
	public Entidades getAudSociedad() {
		return audSociedad;
	}

	public void setAudSociedad(Entidades audSociedad) {
		this.audSociedad = audSociedad;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AUD_TIPO")	
	public TipoAuditoria getAudTipo() {
		return audTipo;
	}

	public void setAudTipo(TipoAuditoria audTipo) {
		this.audTipo = audTipo;
	}

	@Column(name = "AUD_PERIODO", nullable = false, length = 50)
	public String getAudPeriodo() {
		return audPeriodo;
	}

	public void setAudPeriodo(String audPeriodo) {
		this.audPeriodo = audPeriodo;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AUD_TIPO_PROG")
	public TipoProgramacion getAudTipoProgramacion() {
		return audTipoProgramacion;
	}

	public void setAudTipoProgramacion(TipoProgramacion audTipoProgramacion) {
		this.audTipoProgramacion = audTipoProgramacion;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "AUD_ENVIO_OFICIO", nullable = false)
	public Date getAudFechaEnvioOficio() {
		return audFechaEnvioOficio;
	}

	public void setAudFechaEnvioOficio(Date audFechaEnvioOficio) {
		this.audFechaEnvioOficio = audFechaEnvioOficio;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "AUD_TRABAJO_CAMPO", nullable = false)
	public Date getAudFechaTrabajoCampo() {
		return audFechaTrabajoCampo;
	}

	public void setAudFechaTrabajoCampo(Date audFechaTrabajoCampo) {
		this.audFechaTrabajoCampo = audFechaTrabajoCampo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "AUD_FECHA_ENTREGA_INF", nullable = false)
	public Date getAudFechaInforme() {
		return audFechaInforme;
	}
	
	public void setAudFechaInforme(Date audFechaInforme) {
		this.audFechaInforme = audFechaInforme;
	}

	@Column(name = "AUD_RESPONSABLE", nullable = true)
	public Integer getAudResponsable() {
		return audResponsable;
	}

	public void setAudResponsable(Integer audResponsable) {
		this.audResponsable = audResponsable;
	}

	@Column(name = "AUD_FUNCIONARIO_AUTORIZA", nullable = false, length = 50)
	public String getAudFuncionario() {
		return audFuncionario;
	}

	public void setAudFuncionario(String audFuncionario) {
		this.audFuncionario = audFuncionario;
	}

	@Column(name = "AUD_ALCANCE", nullable = true, length = 500)
	public String getAudAlcance() {
		return audAlcance;
	}

	public void setAudAlcance(String audAlcance) {
		this.audAlcance = audAlcance;
	}

}
