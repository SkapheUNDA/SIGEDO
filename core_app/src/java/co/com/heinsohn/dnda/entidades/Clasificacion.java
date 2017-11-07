package co.com.heinsohn.dnda.entidades;

import java.io.Serializable;

public class Clasificacion implements Serializable {

	private static final long serialVersionUID = 1L;

	private Short claId;

	private Tramite tramite;

	private String cod;

	private String nom;

	private Short tip;

	private Boolean lrta;

	private Short tie;

	private String tit;

	private Short trt;

	private Boolean lreg;
	
	public Clasificacion(Short claId, Tramite tramite, String cod, String nom,
			Short tip, Boolean lrta, Short tie, String tit, Short trt,
			Boolean lreg) {
		super();
		this.claId = claId;
		this.tramite = tramite;
		this.cod = cod;
		this.nom = nom;
		this.tip = tip;
		this.lrta = lrta;
		this.tie = tie;
		this.tit = tit;
		this.trt = trt;
		this.lreg = lreg;
	}

	public Short getClaId() {
		return claId;
	}

	public void setClaId(Short claId) {
		this.claId = claId;
	}

	public Tramite getTramite() {
		return tramite;
	}

	public void setTramite(Tramite tramite) {
		this.tramite = tramite;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Short getTip() {
		return tip;
	}

	public void setTip(Short tip) {
		this.tip = tip;
	}

	public Boolean getLrta() {
		return lrta;
	}

	public void setLrta(Boolean lrta) {
		this.lrta = lrta;
	}

	public Short getTie() {
		return tie;
	}

	public void setTie(Short tie) {
		this.tie = tie;
	}

	public String getTit() {
		return tit;
	}

	public void setTit(String tit) {
		this.tit = tit;
	}

	public Short getTrt() {
		return trt;
	}

	public void setTrt(Short trt) {
		this.trt = trt;
	}

	public Boolean getLreg() {
		return lreg;
	}

	public void setLreg(Boolean lreg) {
		this.lreg = lreg;
	}

}
