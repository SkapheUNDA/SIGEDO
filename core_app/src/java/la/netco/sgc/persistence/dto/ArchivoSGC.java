package la.netco.sgc.persistence.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;


@Entity
@Table(name = "ArchivoSGC",  schema = Schemas.SGC_SCHEMA)
@NamedQueries({
	@NamedQuery(name=ArchivoSGC.NAMED_QUERY_GET_BY_FORMATOS,query=ArchivoSGC.QUERY_GET_BY_FORMATOS),
	@NamedQuery(name=ArchivoSGC.NAMED_QUERY_GET_BY_ENTIDAD,query=ArchivoSGC.QUERY_GET_BY_ENTIDAD),
	@NamedQuery(name=ArchivoSGC.NAMED_QUERY_GET_DISTINCT_ANOS,query=ArchivoSGC.QUERY_GET_DISTINCT_ANOS),	
})
public class ArchivoSGC implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Formatos formatos;
	private String nombre;
	private Long tamano;
	private String contentType;
	private String periodo;
	private String ano;
	private Date fechaRegistro;
	private Entidades entidades;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	@Column(name = "nombre", nullable = false,length=200)
	public String getNombre() {
		return nombre;
	}
	
	@Column(name = "size", nullable = false)
	public Long getTamano() {
		return tamano;
	}
		
	@Column(name = "f_reg", nullable = true)
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	
	@Column(name = "contentType", nullable = false ,length = 200)
	public String getContentType() {
		return contentType;
	}
	
	@Column(name = "periodo", nullable = false ,length = 200)
	 public String getPeriodo() {
			return periodo;
	}
	
	@Column(name = "ano", nullable = false ,length = 10)
	public String getAno() {
		return ano;
	}

	
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_formato")
    public Formatos getFormatos() {
        return this.formatos;
    }
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_entidad")
    public Entidades getEntidades() {
        return this.entidades;
    }
    
    public void setEntidades(Entidades entidades) {
        this.entidades = entidades;
    }
    
    public void setFormatos(Formatos formatos) {
        this.formatos = formatos;
    }
	public void setId(Integer id) {
		this.id = id;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setTamano(Long tamano) {
		this.tamano = tamano;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
   
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}


	/**
     * Obtener Archivos por codigo de formato 
     */
    public static final String NAMED_QUERY_GET_BY_FORMATOS = "getArchivoSGCByFormato";
    public static final String QUERY_GET_BY_FORMATOS = "FROM ArchivoSGC archivoSGC WHERE  archivoSGC.formatos.forCodigo = ? ";
  
	
    /**
     * Obtener archivos por codigo de entidad
     */
    public static final String NAMED_QUERY_GET_BY_ENTIDAD = "getArchivoSGCByEntidad";
    public static final String QUERY_GET_BY_ENTIDAD = "FROM ArchivoSGC archivoSGC WHERE  archivoSGC.entidades.entCodigo = ? ";
    
  
    /**
     * Obtener fechas  que se encuentren en archivoSGC por orden ascendente
     */
    public static final String NAMED_QUERY_GET_DISTINCT_ANOS = "getArchivoSGCAnos";
    public static final String QUERY_GET_DISTINCT_ANOS = "SELECT DISTINCT  year(archivosgc.fechaRegistro)  FROM ArchivoSGC archivosgc ";

	
    
}
