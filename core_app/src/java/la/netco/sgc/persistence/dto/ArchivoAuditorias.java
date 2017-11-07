package la.netco.sgc.persistence.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "ARCHIVOAUDITORIA",  schema = Schemas.SGC_SCHEMA)
@NamedQuery(name=ArchivoAuditorias.NAMED_QUERY_GET_BY_AUDITORIA,query=ArchivoAuditorias.QUERY_GET_BY_AUDITORIA)
public class ArchivoAuditorias implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer aauId;
	private String aauNombre;
	private Long aauSize;
	private String aauContentType;
	private Date aauFechaRegistro;
	private Auditorias aauAuditoria;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AAU_ID", unique = true, nullable = false)
	public Integer getAauId() {
		return aauId;
	}
	
	public void setAauId(Integer aauId) {
		this.aauId = aauId;
	}
	
	@Column(name = "AAU_NOMBRE", nullable = false, length = 100)	
	public String getAauNombre() {
		return aauNombre;
	}
	
	public void setAauNombre(String aauNombre) {
		this.aauNombre = aauNombre;
	}
	
	@Column(name = "AAU_SIZE", nullable = false)
	public Long getAauSize() {
		return aauSize;
	}
	
	public void setAauSize(Long aauSize) {
		this.aauSize = aauSize;
	}
	
	@Column(name = "AAU_CONTENT_TYPE", nullable = false, length = 50)
	public String getAauContentType() {
		return aauContentType;
	}
	
	public void setAauContentType(String aauContentType) {
		this.aauContentType = aauContentType;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "AAU_FECHA_REG", nullable = true)
	public Date getAauFechaRegistro() {
		return aauFechaRegistro;
	}
	
	public void setAauFechaRegistro(Date aauFechaRegistro) {
		this.aauFechaRegistro = aauFechaRegistro;
	}
	   
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AAU_ID_AUDITORIA")
	public Auditorias getAauAuditoria() {
		return aauAuditoria;
	}
	
	public void setAauAuditoria(Auditorias aauAuditoria) {
		this.aauAuditoria = aauAuditoria;
	}

    public static final String NAMED_QUERY_GET_BY_AUDITORIA = "getArchivoAuditoriasByAuditoria";
    public static final String QUERY_GET_BY_AUDITORIA = "FROM ArchivoAuditorias WHERE aauAuditoria.audId = ?";
  
}
