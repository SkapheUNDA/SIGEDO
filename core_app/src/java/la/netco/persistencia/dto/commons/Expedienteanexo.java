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
@Table(name = "EXPEDIENTEANEXO", schema = Schemas.DBO_SCHEMA)
@NamedQueries({
	@NamedQuery(name = Expedienteanexo.NAMED_QUERY_GET_ALL_BYEXPANX, query = Expedienteanexo.QUERY_GET_ALL_BYEXP) 
	
})
public class Expedienteanexo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private ExpedienteanexoId id;
	private Anexo anexo;
	private Expediente expediente;
	private Integer exaCan;
	private String exaURL;

	public Expedienteanexo() {
	}

	public Expedienteanexo(ExpedienteanexoId id, Anexo anexo,
			Expediente expediente) {
		this.id = id;
		this.anexo = anexo;
		this.expediente = expediente;
	}

	public Expedienteanexo(ExpedienteanexoId id, Anexo anexo,
			Expediente expediente, Integer exaCan) {
		this.id = id;
		this.anexo = anexo;
		this.expediente = expediente;
		this.exaCan = exaCan;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "exaExp", column = @Column(name = "EXA_EXP", nullable = false)),
			@AttributeOverride(name = "exaAnx", column = @Column(name = "EXA_ANX", nullable = false)) })
	public ExpedienteanexoId getId() {
		return this.id;
	}

	public void setId(ExpedienteanexoId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXA_ANX", nullable = false, insertable = false, updatable = false)
	public Anexo getAnexo() {
		return this.anexo;
	}

	public void setAnexo(Anexo anexo) {
		this.anexo = anexo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXA_EXP", nullable = false, insertable = false, updatable = false)
	public Expediente getExpediente() {
		return this.expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	@Column(name = "EXA_CAN")
	public Integer getExaCan() {
		return this.exaCan;
	}

	public void setExaCan(Integer exaCan) {
		this.exaCan = exaCan;
	}

	@Column(name = "EXA_URL")
	public String getExaURL() {
		return exaURL;
	}

	public void setExaURL(String exaURL) {
		this.exaURL = exaURL;
	}

	public static final String NAMED_QUERY_GET_ALL_BYEXPANX = "getAllByExpAnx";
	public static final String QUERY_GET_ALL_BYEXP = "FROM  Expedienteanexo expedienteanexo where expedienteanexo.expediente.expId = ?";

}
