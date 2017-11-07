package la.netco.persistencia.dto.commons;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "EXPEDIENTEESTADO", schema = Schemas.DBO_SCHEMA)

@NamedQuery(name=ExpedienteEstado.NAMED_QUERY_GET_ALL,query=ExpedienteEstado.QUERY_GET_BY_ALL)
public class ExpedienteEstado implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private Integer id;
    private String nombre;
    
    public static Integer TRAMITE = 0;
    public static Integer REGISTRADO = 1;
    public static Integer REGISTRADO_AUTO = 5;
    public static Integer IMPRESO = 6;
    
    
    public static final String NAMED_QUERY_GET_ALL = "getAllExpedienteEstado";
    public static final String QUERY_GET_BY_ALL = "FROM ExpedienteEstado expediente  order by expediente.nombre asc";
    
    
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
    
    @Column(name = "nombre", length = 50, nullable = false)
	public String getNombre() {
		return nombre;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
