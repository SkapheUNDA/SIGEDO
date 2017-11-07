package la.netco.sgc.persistence.dto;

// Generated 25/10/2012 08:58:29 PM by Hibernate Tools 3.2.1.GA

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

/**
 * Operadores generated by hbm2java
 */
@Entity
@Table(name = "Operadores", schema = Schemas.SGC_SCHEMA)
public class Operadores implements java.io.Serializable {

	private int opeCodigo;
	private String opeDescripcion;
	private Set<Alertas> alertases = new HashSet<Alertas>(0);

	public Operadores() {
	}

	public Operadores(int opeCodigo) {
		this.opeCodigo = opeCodigo;
	}

	public Operadores(int opeCodigo, String opeDescripcion, Set alertases) {
		this.opeCodigo = opeCodigo;
		this.opeDescripcion = opeDescripcion;
		this.alertases = alertases;
	}

	@Id
	@Column(name = "OPE_Codigo", unique = true, nullable = false)
	public int getOpeCodigo() {
		return this.opeCodigo;
	}

	public void setOpeCodigo(int opeCodigo) {
		this.opeCodigo = opeCodigo;
	}

	@Column(name = "OPE_Descripcion", length = 20)
	public String getOpeDescripcion() {
		return this.opeDescripcion;
	}

	public void setOpeDescripcion(String opeDescripcion) {
		this.opeDescripcion = opeDescripcion;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "operadores")
	public Set<Alertas> getAlertases() {
		return this.alertases;
	}

	public void setAlertases(Set<Alertas> alertases) {
		this.alertases = alertases;
	}

}
