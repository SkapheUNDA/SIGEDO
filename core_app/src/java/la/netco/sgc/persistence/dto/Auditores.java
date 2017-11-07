package la.netco.sgc.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name= "AUDITORES", schema=Schemas.SGC_SCHEMA)
@NamedQueries(value = {
		@NamedQuery(name=Auditores.NAMED_QUERY_FIND_AUD_BY_ID, query=Auditores.QUERY_FIND_AUD_BY_ID),
		@NamedQuery(name=Auditores.NAMED_QUERY_ALL, query=Auditores.QUERY_ALL),
		@NamedQuery(name=Auditores.NAMED_QUERY_ALL_ACTIVOS, query=Auditores.QUERY_ALL_ACTIVOS)
})



public class Auditores implements java.io.Serializable  {
	
	private static final long serialVersionUID = 1L;
	private Integer codigoAuditores;
	private String nombreAuditores;
	private String estadoAuditores;
	

	public Auditores() {

	}
	

	public Auditores(Integer codigoAuditores) {
		this.codigoAuditores = codigoAuditores;
	}


	public Auditores(Integer codigoAuditores, String nombreAuditores,
			String estadoAuditores) {
		this.codigoAuditores = codigoAuditores;
		this.nombreAuditores = nombreAuditores;
		this.estadoAuditores = estadoAuditores;
	}


	@Id
	@Column(name="AUDR_CODIGO", nullable = false, unique=true)
	public Integer getCodigoAuditores() {
		return this.codigoAuditores;
	}
	
	public void setCodigoAuditores(Integer codigoAuditores) {
		this.codigoAuditores = codigoAuditores;
	}
	
	@Column(name="AUDR_NOMBRE", nullable = false, unique = true, length = 60)
	public String getNombreAuditores() {
		return this.nombreAuditores;
	}
	
	public void setNombreAuditores(String nombreAuditores) {
		this.nombreAuditores = nombreAuditores;
	}
	
	@Column(name="AUDR_ESTADO", nullable = false, unique = true, length = 10)
	public String getEstadoAuditores() {
		return this.estadoAuditores;
	}
	
	public void setEstadoAuditores(String estadoAuditores) {
		this.estadoAuditores = estadoAuditores;
	}
	public static final String NAMED_QUERY_FIND_AUD_BY_ID="findAuditorByid";
	public static final String QUERY_FIND_AUD_BY_ID = "SELECT p from Auditores p ";
	public static final String NAMED_QUERY_ALL ="getAllAuditores";
	public static final String QUERY_ALL = "FROM Auditores a order by a.nombreAuditores asc";
	public static final String NAMED_QUERY_ALL_ACTIVOS ="getAllAuditoresActivos";
	public static final String QUERY_ALL_ACTIVOS = "FROM Auditores a where estadoAuditores = '1' order by a.nombreAuditores asc";
}
