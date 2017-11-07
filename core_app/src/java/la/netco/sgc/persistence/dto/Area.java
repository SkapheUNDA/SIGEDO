package la.netco.sgc.persistence.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.richfaces.resource.Java2DAnimatedUserResource;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "AREA", schema=Schemas.SGC_SCHEMA)
@NamedQueries(value = {
@NamedQuery(name=Area.NAMED_QUERY_FIND_BY_ID, query=Area.QUERY_FIND_BY_ID),
@NamedQuery(name=Area.NAMED_QUERY_ALL, query=Area.QUERY_ALL)
})

public class Area implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer codigoArea;
	private String descripcionArea;


	public Area() {
	
	}
	
	

	public Area(Integer codigoArea, String descripcionArea) {
		this.codigoArea = codigoArea;
		this.descripcionArea = descripcionArea;
	}



	@Id
	@Column(name="ARE_CODIGO", nullable = false, unique = true)
	public Integer getCodigoArea() {
		return this.codigoArea;
	}
	
	public void setCodigoArea(Integer codigoArea) {
		this.codigoArea = codigoArea;
	}
	
	@Column (name="ARE_DESCRIPCION", nullable = false, unique = true, length = 50)
	public String getDescripcionArea() {
		return descripcionArea;
	}
	
	public void setDescripcionArea(String descripcionArea) {
		this.descripcionArea = descripcionArea;
	}
	
	public static final String NAMED_QUERY_FIND_BY_ID= "findAreaById";
	public static final String QUERY_FIND_BY_ID = "SELECT p from Area p where p.codigoArea like ?";
	public static final String NAMED_QUERY_ALL= "getAllAreas";
	public static final String QUERY_ALL = "from Area a order by a.descripcionArea asc";
	
}
