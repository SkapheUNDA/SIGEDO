package la.netco.registro.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import la.netco.persistencia.dto.commons.ArchivoRegistro;


public class RegistroResumen implements Serializable {

	private static final long serialVersionUID = 460219261852283929L;
	private Integer regId;
	private String entNen;
	private String creNom;
	private String regTor;
	private String soliciante;
	private Date expFso;
	private String medNom;
	private String regCod;
	private String estadoGeneral;
	private String etapa;
	private Boolean regLblo;
	private Boolean regLgen;
	private int expId;
	private String usuario;
	private Date regFec;
	private String regObs;
	private String regDes;
	private Short regFcr;
	
	//solicitante
	private String nombreCompleto;
	private String perDoc;
	private String nacionalidad;
	private String perDir;
	private String perTl1;
	private String ciudad;
	private String perCe1;
	private String calNom;
	
	private List<PersonaRegistro> personas = new ArrayList<PersonaRegistro>();
	private List<ElementoReg> elementos = new ArrayList<ElementoReg>();
	
	// incluir lista de documentos
	public List<ArchivoRegistro> archivosregistro = null;
	
	public Integer getRegId() {
		return regId;
	}
	public String getEntNen() {
		return entNen;
	}
	public String getCreNom() {
		return creNom;
	}
	public String getRegTor() {
		return regTor;
	}
	public String getPerDoc() {
		return perDoc;
	}

	public Date getExpFso() {
		return expFso;
	}
	public String getMedNom() {
		return medNom;
	}
	public String getRegCod() {
		return regCod;
	}
	public String getEstadoGeneral() {
		return estadoGeneral;
	}
	public String getEtapa() {
		return etapa;
	}
	public void setRegId(Integer regId) {
		this.regId = regId;
	}
	public void setEntNen(String entNen) {
		this.entNen = entNen;
	}
	public void setCreNom(String creNom) {
		this.creNom = creNom;
	}
	public void setRegTor(String regTor) {
		this.regTor = regTor;
	}
	public void setPerDoc(String perDoc) {
		this.perDoc = perDoc;
	}

	public void setExpFso(Date expFso) {
		this.expFso = expFso;
	}
	public void setMedNom(String medNom) {
		this.medNom = medNom;
	}
	public void setRegCod(String regCod) {
		this.regCod = regCod;
	}
	public void setEstadoGeneral(String estadoGeneral) {
		this.estadoGeneral = estadoGeneral;
	}
	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}
	public String getSoliciante() {
		return soliciante;
	}
	public void setSoliciante(String soliciante) {
		this.soliciante = soliciante;
	}
	public Boolean getRegLblo() {
		return regLblo;
	}
	public Boolean getRegLgen() {
		return regLgen;
	}
	public int getExpId() {
		return expId;
	}
	public void setRegLblo(Boolean regLblo) {
		this.regLblo = regLblo;
	}
	public void setRegLgen(Boolean regLgen) {
		this.regLgen = regLgen;
	}
	public void setExpId(int expId) {
		this.expId = expId;
	}
	
	
	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		
		if (!(other instanceof RegistroResumen)) {
			return false;
		}
		
		RegistroResumen castOther = (RegistroResumen) other;

		return (this.getRegId().equals(castOther.getRegId()));
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.getRegId();
		return result;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public List<PersonaRegistro> getPersonas() {
		return personas;
	}
	public void setPersonas(List<PersonaRegistro> personas) {
		this.personas = personas;
	}
	public String getRegObs() {
		return regObs;
	}
	public String getRegDes() {
		return regDes;
	}
	public void setRegObs(String regObs) {
		this.regObs = regObs;
	}
	public void setRegDes(String regDes) {
		this.regDes = regDes;
	}
	public Date getRegFec() {
		return regFec;
	}
	public void setRegFec(Date regFec) {
		this.regFec = regFec;
	}
	public Short getRegFcr() {
		return regFcr;
	}
	public void setRegFcr(Short regFcr) {
		this.regFcr = regFcr;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public String getNacionalidad() {
		return nacionalidad;
	}
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	public String getPerDir() {
		return perDir;
	}
	public String getPerTl1() {
		return perTl1;
	}
	public String getCiudad() {
		return ciudad;
	}
	public String getPerCe1() {
		return perCe1;
	}
	public void setPerDir(String perDir) {
		this.perDir = perDir;
	}
	public void setPerTl1(String perTl1) {
		this.perTl1 = perTl1;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	public void setPerCe1(String perCe1) {
		this.perCe1 = perCe1;
	}
	public String getCalNom() {
		return calNom;
	}
	public void setCalNom(String calNom) {
		this.calNom = calNom;
	}
	public List<ElementoReg> getElementos() {
		return elementos;
	}
	public void setElementos(List<ElementoReg> elementos) {
		this.elementos = elementos;
	}
	public List<ArchivoRegistro> getArchivosregistro() {
		return archivosregistro;
	}
	public void setArchivosregistro(List<ArchivoRegistro> archivosregistro) {
		this.archivosregistro = archivosregistro;
	}
	
	
}
