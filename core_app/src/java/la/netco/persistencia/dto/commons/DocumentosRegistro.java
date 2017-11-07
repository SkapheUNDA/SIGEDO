package la.netco.persistencia.dto.commons;

import java.io.Serializable;
/**
 * 
 * @author ediaz
 *
 */
public class DocumentosRegistro implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Object files;
	private Integer documentoId;
	
	
	public DocumentosRegistro() {
	
	}
	public Object getFiles() {
		return files;
	}
	public void setFiles(Object files) {
		this.files = files;
	}
	public Integer getDocumentoId() {
		return documentoId;
	}
	public void setDocumentoId(Integer documentoId) {
		this.documentoId = documentoId;
	}
	
	

}
