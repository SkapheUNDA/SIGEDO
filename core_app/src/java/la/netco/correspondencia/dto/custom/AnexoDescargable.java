package la.netco.correspondencia.dto.custom;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class AnexoDescargable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9083199370533889668L;
	private String path;
	private String nombre;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public AnexoDescargable() {
		
	}
	
	public StreamedContent getDato() {
		try {
			return new DefaultStreamedContent(new FileInputStream(new File(path)), "application/octet-stream", nombre);
		} catch (Exception e) {
			return null;
		}
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
