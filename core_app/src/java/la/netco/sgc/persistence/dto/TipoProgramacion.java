package la.netco.sgc.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name= "TIPOSDEPROGRAMACION", schema=Schemas.SGC_SCHEMA)
@NamedQueries(value = {
		@NamedQuery(name=TipoProgramacion.NAMED_QUERY_FIND_BY_ID, query=TipoProgramacion.QUERY_FIND_BY_ID),
		@NamedQuery(name =TipoProgramacion.NAMED_QUERY_ALL, query = TipoProgramacion.QUERY_ALL)
})

public class TipoProgramacion implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer tprCodigo;
	private String tprNombre;
		
	
	@Id
	@Column(name= "TPR_CODIGO", nullable = false, unique = true )
	public Integer getTprCodigo() {
		return tprCodigo;
	}

	public void setTprCodigo(Integer tprCodigo) {
		this.tprCodigo = tprCodigo;
	}

	@Column(name= "TPR_NOMBRE", nullable = false, unique = false, length = 50)
	public String getTprNombre() {
		return tprNombre;
	}

	public void setTprNombre(String tprNombre) {
		this.tprNombre = tprNombre;
	}

	public static final String NAMED_QUERY_FIND_BY_ID= "findTipoProgramacion";
	public static final String QUERY_FIND_BY_ID = "from TipoProgramacion Tp where Tp.tprCodigo = ?";
	public static final String NAMED_QUERY_ALL = "getAllTipoProgramacion";
	public static final String QUERY_ALL = "FROM TipoProgramacion tp order by tp.tprNombre asc";

}
