package co.com.heinsohn.dnda.entidades;

import java.io.Serializable;

/**
 * Representa una ciudad.
 * 
 * @author cgmartinez
 *
 */
public class Lugar implements Serializable {

	private static final long serialVersionUID = 1L;

	/** Identificador. */
	private short lugId;
	/** Nombre ciudad. */
	private String lugCiu;
	/** Nombre departamento. */
	private String lugDep;
	/** País de la ciudad. */
	private Paises paises;

	public Lugar() {
	}

	public Lugar(short lugId, String lugCiu, String lugDep, Paises paises) {
		super();
		this.lugId = lugId;
		this.lugCiu = lugCiu;
		this.lugDep = lugDep;
		this.paises = paises;
	}

	public short getLugId() {
		return lugId;
	}

	public void setLugId(short lugId) {
		this.lugId = lugId;
	}

	public String getLugCiu() {
		return lugCiu;
	}

	public void setLugCiu(String lugCiu) {
		this.lugCiu = lugCiu;
	}

	public String getLugDep() {
		return lugDep;
	}

	public void setLugDep(String lugDep) {
		this.lugDep = lugDep;
	}

	public Paises getPaises() {
		return paises;
	}

	public void setPaises(Paises paises) {
		this.paises = paises;
	}

}
