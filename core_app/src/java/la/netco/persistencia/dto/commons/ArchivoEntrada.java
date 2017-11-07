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
@Table(name = "ARCHIVOENTRADA",  schema = Schemas.DBO_SCHEMA)
@NamedQuery(name=ArchivoEntrada.NAMED_QUERY_GET_BY_ENTRADA,query=ArchivoEntrada.QUERY_GET_BY_ENTRADA)
public class ArchivoEntrada implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Entrada entrada;
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
    @JoinColumn(name = "id_entrada")
    public Entrada getEntrada() {
        return this.entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
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
	

    public static final String NAMED_QUERY_GET_BY_ENTRADA = "getArchivoEntradaByEntrada";
    public static final String QUERY_GET_BY_ENTRADA = "FROM ArchivoEntrada WHERE  entrada.entId = ? ";
  
	
}
