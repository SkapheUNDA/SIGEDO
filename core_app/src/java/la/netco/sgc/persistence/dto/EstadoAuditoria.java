package la.netco.sgc.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "ESTADOAUDITORIA", schema=Schemas.SGC_SCHEMA)
@NamedQueries(value = {
		@NamedQuery(name=EstadoAuditoria.NAMED_QUERY_FIND_BY_ID, query=EstadoAuditoria.QUERY_FIND_BY_ID),
		@NamedQuery(name=EstadoAuditoria.NAMED_QUERY_ALL, query=EstadoAuditoria.QUERY_ALL)
})

public class EstadoAuditoria implements java.io.Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private Integer codigoEstadoAuditoria;
	private String descripcionEstadoAuditoria;
	
	
	@Id
	@Column(name = "EAU_CODIGO", nullable = false, unique = true)
	public Integer getCodigoEstadoAuditoria() {
		return this.codigoEstadoAuditoria;
	}
	
	public void setCodigoEstadoAuditoria(Integer codigoEstadoAuditoria) {
		this.codigoEstadoAuditoria = codigoEstadoAuditoria;
	}
	
	@Column(name = "EAU_DESCRIPCION", nullable = false, unique = false, length = 50)
	public String getDescripcionEstadoAuditoria() {
		return this.descripcionEstadoAuditoria;
	}
	
	public void setDescripcionEstadoAuditoria(String descripcionEstadoAuditoria) {
		this.descripcionEstadoAuditoria = descripcionEstadoAuditoria;
	}
	
	public static final String NAMED_QUERY_FIND_BY_ID= "findEstadoAuditoriaById";
	public static final String QUERY_FIND_BY_ID = "SELECT p from EstadoAuditoria p where p.id like ?";	
	public static final String NAMED_QUERY_ALL= "getAllEstados";
	public static final String QUERY_ALL= "from EstadoAuditoria ea order by ea.descripcionEstadoAuditoria asc";	

}
