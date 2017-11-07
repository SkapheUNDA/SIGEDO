package la.netco.persistencia.dto.commons;

// Clase en uso

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "DOCUMENTOS", schema = Schemas.RLINEA_SCHEMA)
@NamedQueries({
	@NamedQuery(name=Documentos.NAMED_QUERY_ALL_DOCUMENTS,query=Documentos.QUERY_ALL_DOCUMENT),
	@NamedQuery(name=Documentos.NAMED_QUERY_ALL_DOCUMENTS_REGISTRO,query=Documentos.QUERY_ALL_DOCUMENT_REGISTRO)
	})
public class Documentos implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	private Integer docId;
	private String docNombre;
	private String docExt;

	public static final Integer DOC_DEVOLUCION=24;
	
	
	public Documentos() {
	}

	public Documentos(Integer docId) {
		this.docId = docId;
	}

	public Documentos(Integer docId, String docNombre, String docExt) {
		this.docId = docId;
		this.docNombre = docNombre;
		this.docExt = docExt;
	}

	@Id
	@Column(name = "DOC_ID", unique = true, nullable = false)
	public int getDocId() {
		return this.docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	@Column(name = "DOC_NOMBRE", length = 50)
	public String getDocNombre() {
		return this.docNombre;
	}

	public void setDocNombre(String docNombre) {
		this.docNombre = docNombre;
	}

	@Column(name = "DOC_EXT", length = 50)
	public String getDocExt() {
		return this.docExt;
	}

	public void setDocExt(String docExt) {
		this.docExt = docExt;
	}
	
	public static final String NAMED_QUERY_ALL_DOCUMENTS = "getAllDocument";
    public static final String QUERY_ALL_DOCUMENT = "FROM Documentos documentos order by documentos.docNombre asc";
    
    /*Qrys por Obras Registro*/
    public static final String NAMED_QUERY_ALL_DOCUMENTS_REGISTRO = "getAllDocumentRegistro";
    public static final String QUERY_ALL_DOCUMENT_REGISTRO = "FROM Documentos documentos where documentos.docNombre in ('Formulario de Obra','Obra Escaneada','Varios Escaneados') order by documentos.docNombre asc";

}
