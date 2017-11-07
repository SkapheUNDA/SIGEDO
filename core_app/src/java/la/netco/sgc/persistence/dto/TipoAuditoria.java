package la.netco.sgc.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;
import la.netco.persistencia.dto.commons.Paises;

@Entity
@Table(name = "TIPODEAUDITORIA", schema = Schemas.SGC_SCHEMA)
@NamedQueries(value = {
		@NamedQuery(name = TipoAuditoria.NAMED_QUERY_FIND_BY_ID, query = TipoAuditoria.QUERY_FIND_BY_ID),
		@NamedQuery(name = TipoAuditoria.NAMED_QUERY_ALL, query = TipoAuditoria.QUERY_ALL) })
public class TipoAuditoria implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer tauCodigo;
	private String tauNombre;

	public TipoAuditoria() {

	}

	public TipoAuditoria(Integer tauCodigo) {
		this.tauCodigo = tauCodigo;
	}

	public TipoAuditoria(Integer tauCodigo, String tauNombre) {
		this.tauCodigo = tauCodigo;
		this.tauNombre = tauNombre;
	}

	@Id
	@Column(name = "TAU_CODIGO", nullable = false, unique = true)
	public Integer getTauCodigo() {
		return tauCodigo;
	}

	public void setTauCodigo(Integer tauCodigo) {
		this.tauCodigo = tauCodigo;
	}

	@Column(name = "TAU_NOMBRE", length = 50)
	public String getTauNombre() {
		return tauNombre;
	}

	public void setTauNombre(String tauNombre) {
		this.tauNombre = tauNombre;
	}

	public static final String NAMED_QUERY_FIND_BY_ID = "findTipoAuditoriaById";
	public static final String QUERY_FIND_BY_ID = "from TipoAuditoria ta where ta.tauCodigo = ?";
	public static final String NAMED_QUERY_ALL = "getAllTipoAuditoria";
	public static final String QUERY_ALL = "FROM TipoAuditoria ta order by ta.tauNombre asc";
}
