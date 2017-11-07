package la.netco.core.persistencia.vo;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "aud_entidad_auditada", schema=Schemas.PRINCIPAL_SCHEMA)
@NamedQuery(name = EntidadAuditada.NAMED_QUERY_GET_ENTIDAD_BY_ENTITY_CLASS, query=EntidadAuditada.QUERY_GET_ENTIDAD_BY_ENTITY_CLASS)
public class EntidadAuditada implements Serializable{


	private static final long serialVersionUID = 1864407615236974790L;
	private Integer id;
	private String entityClass;
	private String nombreModulo;
	private String auditoriaActiva;
	
	public static String ESTADO_ACTIVA = "TRUE";
	public static String ESTADO_INACTIVA = "FALSE";
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	
	@Column(name = "entityClass",  unique = true, nullable = false , length = 100)
	public String getEntityClass() {
		return entityClass;
	}
	
	@Column(name = "nombreModulo", nullable = false , length = 100)
	public String getNombreModulo() {
		return nombreModulo;
	}
	
	@Column(name = "auditoriaActiva", nullable = false , length = 5)
	public String getAuditoriaActiva() {
		return auditoriaActiva;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setEntityClass(String entityClass) {
		this.entityClass = entityClass;
	}
	public void setNombreModulo(String nombreModulo) {
		this.nombreModulo = nombreModulo;
	}

	public void setAuditoriaActiva(String auditoriaActiva) {
		this.auditoriaActiva = auditoriaActiva;
	}
	
	public static final String NAMED_QUERY_GET_ENTIDAD_BY_ENTITY_CLASS  = "getEntidadEntityClass";
	public static final String QUERY_GET_ENTIDAD_BY_ENTITY_CLASS 		= "FROM EntidadAuditada entidad  WHERE entidad.entityClass = ? ";
	
	
	
}
