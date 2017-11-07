package la.netco.sgc.persistence.dto;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;





@Entity
@Table(name = "PUC", schema=Schemas.SGC_SCHEMA)
@NamedQueries(value = {  
		@NamedQuery(name=PUC.NAMED_QUERY_FIND_BY_ID, query=PUC.QUERY_FIND_BY_ID),
		@NamedQuery(name=PUC.NAMED_QUERY_FIND_BY_ID_EXACT, query=PUC.QUERY_FIND_BY_ID_EXACT),

})
public class PUC implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	private String id;	
	private String descripcion;
	private String nivel;
	private String cuentaDetalle;
	private String requiereTercero;
	private String estado;
	
		
	public void setId(String id) {
		this.id = id;
	}

	@Id
   // @GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "SGC_COD_CUENTA", nullable = false, unique=true, length = 50)
	public String getId() {
		return this.id;
	}
	
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	
	@Column(name = "SGC_NIVEL", nullable = false, unique=true, length = 50)
	public String getNivel() {
		return this.nivel;
	}

	@Column(name = "SGC_DESCRIPCION", nullable = false, unique=true, length = 50)	
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "SGC_CUENTA_DETALLE", nullable = false,length = 50)
	public String getCuentaDetalle() {
		return this.cuentaDetalle;
	}

	public void setCuentaDetalle(String cuentaDetalle) {
		this.cuentaDetalle = cuentaDetalle;
	}

	@Column(name = "SGC_REQUIERE_TERCERO", nullable = false)
	public String getRequiereTercero() {
		return this.requiereTercero;
	}

	public void setRequiereTercero(String requiereTercero) {
		this.requiereTercero = requiereTercero;
	}

	
	@Column(name = "SGC_ESTADO", nullable = false)
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}


	public static final String NAMED_QUERY_FIND_BY_ID= "findPucById";
	public static final String QUERY_FIND_BY_ID = "SELECT p from PUC p where p.id like concat (?,'%')";	
	
	public static final String NAMED_QUERY_FIND_BY_ID_EXACT= "findPucByIdExact";
	public static final String QUERY_FIND_BY_ID_EXACT = "SELECT p from PUC p where p.id = ?";
	
	
}
