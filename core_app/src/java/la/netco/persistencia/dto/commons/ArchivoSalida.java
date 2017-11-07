package la.netco.persistencia.dto.commons;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "ARCHIVOSALIDA",  schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=ArchivoSalida.NAMED_QUERY_GET_BY_SALIDA,query=ArchivoSalida.QUERY_GET_BY_SALIDA)
public class ArchivoSalida implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Salida salida;
	private String nombre;
	private Long size;
	private String contentType;
	private Date fechaRegistro;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	@Column(name = "nombre", nullable = false, length = 100)
	public String getNombre() {
		return nombre;
	}
	
	@Column(name = "size", nullable = false)
	public Long getSize() {
		return size;
	}
		
	@Column(name = "f_reg", nullable = true)
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	
	@Column(name = "contentType", nullable = false, length = 50)
	public String getContentType() {
		return contentType;
	}
	
    @ManyToOne
    @JoinColumn(name = "id_salida")
    public Salida getSalida() {
        return this.salida;
    }

    public void setSalida(Salida salida) {
        this.salida = salida;
    }
	public void setId(Integer id) {
		this.id = id;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	

    public static final String NAMED_QUERY_GET_BY_SALIDA = "getArchivoSalidaBySalida";
    public static final String QUERY_GET_BY_SALIDA = "FROM ArchivoSalida WHERE  salida.salId = ? ";
  
	
}
