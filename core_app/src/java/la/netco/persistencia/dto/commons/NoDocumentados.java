package la.netco.persistencia.dto.commons;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "NODOCUMENTADOS", schema = Schemas.RLINEA_SCHEMA)
@NamedQueries({
	@NamedQuery(name=NoDocumentados.NAMED_QUERY_GET_MAX_ALL,query=NoDocumentados.QUERY_GET_MAX_ALL)
})
public class NoDocumentados implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer nodId;
	private Integer nodConsecutivo;
	public NoDocumentados() {
		
	}
	public NoDocumentados(Integer nodConsecutivo) {
		this.nodConsecutivo = nodConsecutivo;
	}
	@Id
	@Column(name = "NOD_ID", unique = true, nullable = false)
	public Integer getNodId() {
		return nodId;
	}
	public void setNodId(Integer nodId) {
		this.nodId = nodId;
	}
	@Column(name = "NOD_CONSECUTIVO")
	public Integer getNodConsecutivo() {
		return nodConsecutivo;
	}
	public void setNodConsecutivo(Integer nodConsecutivo) {
		this.nodConsecutivo = nodConsecutivo;
	}
	
	  public static final String NAMED_QUERY_GET_MAX_ALL = "getMaxNoDocumentados";
	  public static final String QUERY_GET_MAX_ALL = "SELECT max(nodConsecutivo) From Nodocumentados nodocumentados";
	
	
	

}
