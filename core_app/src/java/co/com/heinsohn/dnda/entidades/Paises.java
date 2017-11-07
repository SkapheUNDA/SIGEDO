package co.com.heinsohn.dnda.entidades;

import java.io.Serializable;

/**
 * Representa un país.
 * 
 * @author cgmartinez
 *
 */
public class Paises implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Identificador. */
	private Short paiId;
	/** Nombre de país. */
	private String paiNom;
	/** Gentilicio del país. */
	private String paiNac;
	/** Siglas del país. */
	private String paiSgl;

	public Paises() {
	}

	public Paises(Short paiId, String paiNom, String paiNac, String paiSgl) {
		super();
		this.paiId = paiId;
		this.paiNom = paiNom;
		this.paiNac = paiNac;
		this.paiSgl = paiSgl;
	}

	public Short getPaiId() {
		return paiId;
	}

	public void setPaiId(Short paiId) {
		this.paiId = paiId;
	}

	public String getPaiNom() {
		return paiNom;
	}

	public void setPaiNom(String paiNom) {
		this.paiNom = paiNom;
	}

	public String getPaiNac() {
		return paiNac;
	}

	public void setPaiNac(String paiNac) {
		this.paiNac = paiNac;
	}

	public String getPaiSgl() {
		return paiSgl;
	}

	public void setPaiSgl(String paiSgl) {
		this.paiSgl = paiSgl;
	}

	@Override
	public String toString() {
		
		return "Pais("+paiNom+")";
	}
}
