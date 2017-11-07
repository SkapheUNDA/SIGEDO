package co.com.heinsohn.dnda.entidades;

import java.io.Serializable;

public class Tramite implements Serializable {

	private static final long serialVersionUID = 1L;

	private Short trmId;

	private String trmNom;

	private Boolean lreg;

	private Boolean estado;

	public Tramite(Short trmId, String trmNom, Boolean lreg, Boolean estado) {
		super();
		this.trmId = trmId;
		this.trmNom = trmNom;
		this.lreg = lreg;
		this.estado = estado;
	}

	public Short getTrmId() {
		return trmId;
	}

	public void setTrmId(Short trmId) {
		this.trmId = trmId;
	}

	public String getTrmNom() {
		return trmNom;
	}

	public void setTrmNom(String trmNom) {
		this.trmNom = trmNom;
	}

	public Boolean getLreg() {
		return lreg;
	}

	public void setLreg(Boolean lreg) {
		this.lreg = lreg;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "Tramite("+trmId+","+trmNom+")";
	}
}
