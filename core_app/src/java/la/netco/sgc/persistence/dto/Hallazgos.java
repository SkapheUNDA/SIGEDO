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
@Table(name = "HALLAZGOS", schema = Schemas.SGC_SCHEMA)
@NamedQueries(value = {
		@NamedQuery(name = Hallazgos.NAMED_QUERY_FIND_BY_ID, query = Hallazgos.QUERY_FIND_BY_ID)
		 })
public class Hallazgos implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
    private Integer halId;
    private String halTipo;
    private Date halFechaVencimiento;
    private String halCondicion;
    private String halNorma;
    private String halCausa;
    private String halRecomendacion;
    private String halObservacion;
    private Area halArea;
    private EstadoAuditoria halEstado;
    private Auditorias halAuditoria;
    
	public static final String NAMED_QUERY_FIND_BY_ID = "findHallazgosById";
	public static final String QUERY_FIND_BY_ID = "from Hallazgos h where h.halId = ?";

	public Hallazgos() {

	}
	
	public Hallazgos(Integer halId, String halTipo, Date halFechaVencimiento,
			String halCondicion, String halNorma, String halCausa,
			String halRecomendacion, String halObservacion, Area halArea,
			EstadoAuditoria halEstado, Auditorias halAuditoria) {
		super();
		this.halId = halId;
		this.halTipo = halTipo;
		this.halFechaVencimiento = halFechaVencimiento;
		this.halCondicion = halCondicion;
		this.halNorma = halNorma;
		this.halCausa = halCausa;
		this.halRecomendacion = halRecomendacion;
		this.halObservacion = halObservacion;
		this.halArea = halArea;
		this.halEstado = halEstado;
		this.halAuditoria = halAuditoria;
	}

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "HAL_ID", nullable = false, unique = true)
	public Integer getHalId() {
		return halId;
	}

	public void setHalId(Integer halId) {
		this.halId = halId;
	}

	@Column(name = "HAL_TIPO", nullable = false, length = 50)
	public String getHalTipo() {
		return halTipo;
	}

	public void setHalTipo(String halTipo) {
		this.halTipo = halTipo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "HAL_FECHA_VENCIMIENTO", nullable = false)
	public Date getHalFechaVencimiento() {
		return halFechaVencimiento;
	}

	public void setHalFechaVencimiento(Date halFechaVencimiento) {
		this.halFechaVencimiento = halFechaVencimiento;
	}

	@Column(name = "HAL_CONDICION", nullable = false, length = 500)
	public String getHalCondicion() {
		return halCondicion;
	}

	public void setHalCondicion(String halCondicion) {
		this.halCondicion = halCondicion;
	}

	@Column(name = "HAL_NORMA", nullable = false, length = 500)
	public String getHalNorma() {
		return halNorma;
	}

	public void setHalNorma(String halNorma) {
		this.halNorma = halNorma;
	}

	
	@Column(name = "HAL_CAUSA", nullable = false, length = 500)
	public String getHalCausa() {
		return halCausa;
	}
	
	public void setHalCausa(String halCausa) {
		this.halCausa = halCausa;
	}

	@Column(name = "HAL_RECOMENDACION", nullable = false, length = 500)	
	public String getHalRecomendacion() {
		return halRecomendacion;
	}

	public void setHalRecomendacion(String halRecomendacion) {
		this.halRecomendacion = halRecomendacion;
	}

	@Column(name = "HAL_OBSERVACION", nullable = true, length = 500)	
	public String getHalObservacion() {
		return halObservacion;
	}

	public void setHalObservacion(String halObservacion) {
		this.halObservacion = halObservacion;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HAL_AREA")
	public Area getHalArea() {
		return halArea;
	}

	public void setHalArea(Area halArea) {
		this.halArea = halArea;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HAL_ESTADO")
	public EstadoAuditoria getHalEstado() {
		return halEstado;
	}

	public void setHalEstado(EstadoAuditoria halEstado) {
		this.halEstado = halEstado;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HAL_AUDITORIA")
	public Auditorias getHalAuditoria() {
		return halAuditoria;
	}

	public void setHalAuditoria(Auditorias halAuditoria) {
		this.halAuditoria = halAuditoria;
	}

}
