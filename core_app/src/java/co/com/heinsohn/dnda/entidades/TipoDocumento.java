package co.com.heinsohn.dnda.entidades;

import java.io.Serializable;

/**
 * Representa un tipo de documento.
 * 
 * @author cgmartinez
 *
 */
public class TipoDocumento implements Serializable {

	private static final long serialVersionUID = 1L;
	private Short tdoId;
	private String tdoNom;
	private String tdoAbr;

	public TipoDocumento() {
	}

	public TipoDocumento(Short tdoId, String tdoNom, String tdoAbr) {
		super();
		this.tdoId = tdoId;
		this.tdoNom = tdoNom;
		this.tdoAbr = tdoAbr;
	}

	public Short getTdoId() {
		return this.tdoId;
	}

	public void setTdoId(Short tdoId) {
		this.tdoId = tdoId;
	}

	public String getTdoNom() {
		return this.tdoNom;
	}

	public void setTdoNom(String tdoNom) {
		this.tdoNom = tdoNom;
	}

	public String getTdoAbr() {
		return this.tdoAbr;
	}

	public void setTdoAbr(String tdoAbr) {
		this.tdoAbr = tdoAbr;
	}
}
