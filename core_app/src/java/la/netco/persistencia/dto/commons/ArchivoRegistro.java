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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "ARCHIVOREGISTRO", schema = Schemas.DBO_SCHEMA)
@NamedQueries({
		@NamedQuery(name = ArchivoRegistro.NAMED_QUERY_GET_BY_REGISTRO, query = ArchivoRegistro.QUERY_GET_BY_REGISTRO),
		@NamedQuery(name = ArchivoRegistro.NAMED_QUERY_GET_BY_NOMBRE, query = ArchivoRegistro.QUERY_GET_BY_NOMBRE),
		@NamedQuery(name= ArchivoRegistro.NAMED_QUERY_GET_BY_REGISTRO_SIN_PDF,query = ArchivoRegistro.QUERY_GET_BY_REGISTRO_SIN_PDF)
		})
public class ArchivoRegistro implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Registro registro;
	private String nombre;
	private Long size;
	private String contentType;
	private Date fechaRegistro;
	private Documentos documentos;

	public static final String CONTENT_PDF = "application/pdf";

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
	@JoinColumn(name = "id_registro")
	public Registro getRegistro() {
		return this.registro;
	}

	@ManyToOne
	@JoinColumn(name = "id_Documentos")
	public Documentos getDocumentos() {
		return this.documentos;
	}

	public void setDocumentos(Documentos documentos) {
		this.documentos = documentos;
	}

	public void setRegistro(Registro registro) {
		this.registro = registro;
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

	/**
     * 
     */
	public static final String NAMED_QUERY_GET_BY_REGISTRO = "getArchivoRegistroByRegistro";
	public static final String QUERY_GET_BY_REGISTRO = "FROM ArchivoRegistro archivoRegistro WHERE  archivoRegistro.registro.regId = ? ";

	/**
	 * Archivo por nombre
	 */
	public static final String NAMED_QUERY_GET_BY_NOMBRE = "getArchivoRegistroByNombre";
	public static final String QUERY_GET_BY_NOMBRE = "FROM ArchivoRegistro archivoRegistro WHERE  archivoRegistro.nombre = ?";
	
	
	/*
	 * Arhivo por nombre y no inluir PDF
	 */
	public static final String NAMED_QUERY_GET_BY_REGISTRO_SIN_PDF ="getArchivoRegistroByRegistroSinPDF";
	public static final String QUERY_GET_BY_REGISTRO_SIN_PDF  ="FROM ArchivoRegistro archivoRegistro WHERE  archivoRegistro.registro.regId = ? and archivoRegistro.documentos.docId <>26"; 

}
