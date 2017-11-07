package la.netco.persistencia.dto.commons;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;
import la.netco.persistencia.dto.commons.base.Parteinterviniente;
import la.netco.persistencia.dto.commons.base.Productor;

@Entity
@Table(name = "ENTIDAD", schema = Schemas.DBO_SCHEMA)
public class Entidad implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int etdId;
	private Sector sector;
	// private Lugar lugar;
	private Persona persona;
	private String etdIde;
	private String etdSuc;
	private String etdNom;
	private String etdSgl;
	private String etdCel;
	private String etdDir;
	private String etdCpo;
	private String etdTpp;
	private String etdFax;
	private String etdTl1;
	private String etdTl2;
	private String etdTl3;
	private String etdWeb;
	private String etdCar;
	private String etdAae;
	private Byte etdTip;
	private Integer id;

	/*
	 * private Set<Programacion> programacions = new HashSet<Programacion>(0);
	 * private Set<Productor> productors = new HashSet<Productor>(0); private
	 * Set<Expediente> expedientes = new HashSet<Expediente>(0);
	 */
	/*
	 * private Set<Expedientepersona> expedientepersonas = new
	 * HashSet<Expedientepersona>(0); private Set<Registropersona>
	 * registropersonas = new HashSet<Registropersona>(0); private
	 * Set<Parteinterviniente> parteintervinientes = new
	 * HashSet<Parteinterviniente>(0);
	 */

	public Entidad() {
	}

	public Entidad(int etdId) {
		this.etdId = etdId;
	}

	public Entidad(int etdId, Sector sector, Lugar lugar, Persona persona,
			String etdIde, String etdSuc, String etdNom, String etdSgl,
			String etdCel, String etdDir, String etdCpo, String etdTpp,
			String etdFax, String etdTl1, String etdTl2, String etdTl3,
			String etdWeb, String etdCar, String etdAae, Byte etdTip,
			Integer id, Set<Programacion> programacions,
			Set<Productor> productors, Set<Expediente> expedientes,
			Set<Expedientepersona> expedientepersonas,
			Set<Registropersona> registropersonas,
			Set<Parteinterviniente> parteintervinientes) {
		this.sector = sector;
		// this.lugar = lugar;
		this.persona = persona;
		this.etdIde = etdIde;
		this.etdSuc = etdSuc;
		this.etdNom = etdNom;
		this.etdSgl = etdSgl;
		this.etdCel = etdCel;
		this.etdDir = etdDir;
		this.etdCpo = etdCpo;
		this.etdTpp = etdTpp;
		this.etdFax = etdFax;
		this.etdTl1 = etdTl1;
		this.etdTl2 = etdTl2;
		this.etdTl3 = etdTl3;
		this.etdWeb = etdWeb;
		this.etdCar = etdCar;
		this.etdAae = etdAae;
		this.etdTip = etdTip;
		this.id = id;
		/*
		 * this.programacions = programacions; this.productors = productors;
		 * this.expedientes = expedientes; this.expedientepersonas =
		 * expedientepersonas; this.registropersonas = registropersonas;
		 * this.parteintervinientes = parteintervinientes;
		 */
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ETD_ID", unique = true, nullable = false)
	public int getEtdId() {
		return this.etdId;
	}

	public void setEtdId(int etdId) {
		this.etdId = etdId;
	}

	
	  @ManyToOne(fetch = FetchType.LAZY)
	  
	  @JoinColumn(name = "ETD_SEC") public Sector getSector() { return
	  this.sector; }
	  
	  public void setSector(Sector sector) { this.sector = sector; }
	  
	// @ManyToOne(fetch = FetchType.LAZY)
	//
	// @JoinColumn(name = "ETD_LUG") public Lugar getLugar() { return
	// this.lugar; }
	//
	// public void setLugar(Lugar lugar) { this.lugar = lugar; }
	 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ETD_CON")
	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	@Column(name = "ETD_IDE", length = 20)
	public String getEtdIde() {
		return this.etdIde;
	}

	public void setEtdIde(String etdIde) {
		this.etdIde = etdIde;
	}

	@Column(name = "ETD_SUC", length = 50)
	public String getEtdSuc() {
		return this.etdSuc;
	}

	public void setEtdSuc(String etdSuc) {
		this.etdSuc = etdSuc;
	}

	@Column(name = "ETD_NOM", length = 100)
	public String getEtdNom() {
		return this.etdNom;
	}

	public void setEtdNom(String etdNom) {
		this.etdNom = etdNom;
	}

	@Column(name = "ETD_SGL", length = 50)
	public String getEtdSgl() {
		return this.etdSgl;
	}

	public void setEtdSgl(String etdSgl) {
		this.etdSgl = etdSgl;
	}

	@Column(name = "ETD_CEL", length = 50)
	public String getEtdCel() {
		return this.etdCel;
	}

	public void setEtdCel(String etdCel) {
		this.etdCel = etdCel;
	}

	@Column(name = "ETD_DIR", length = 200)
	public String getEtdDir() {
		return this.etdDir;
	}

	public void setEtdDir(String etdDir) {
		this.etdDir = etdDir;
	}

	@Column(name = "ETD_CPO", length = 20)
	public String getEtdCpo() {
		return this.etdCpo;
	}

	public void setEtdCpo(String etdCpo) {
		this.etdCpo = etdCpo;
	}

	@Column(name = "ETD_TPP", length = 50)
	public String getEtdTpp() {
		return this.etdTpp;
	}

	public void setEtdTpp(String etdTpp) {
		this.etdTpp = etdTpp;
	}

	@Column(name = "ETD_FAX", length = 50)
	public String getEtdFax() {
		return this.etdFax;
	}

	public void setEtdFax(String etdFax) {
		this.etdFax = etdFax;
	}

	@Column(name = "ETD_TL1", length = 50)
	public String getEtdTl1() {
		return this.etdTl1;
	}

	public void setEtdTl1(String etdTl1) {
		this.etdTl1 = etdTl1;
	}

	@Column(name = "ETD_TL2", length = 50)
	public String getEtdTl2() {
		return this.etdTl2;
	}

	public void setEtdTl2(String etdTl2) {
		this.etdTl2 = etdTl2;
	}

	@Column(name = "ETD_TL3", length = 50)
	public String getEtdTl3() {
		return this.etdTl3;
	}

	public void setEtdTl3(String etdTl3) {
		this.etdTl3 = etdTl3;
	}

	@Column(name = "ETD_WEB", length = 50)
	public String getEtdWeb() {
		return this.etdWeb;
	}

	public void setEtdWeb(String etdWeb) {
		this.etdWeb = etdWeb;
	}

	@Column(name = "ETD_CAR", length = 50)
	public String getEtdCar() {
		return this.etdCar;
	}

	public void setEtdCar(String etdCar) {
		this.etdCar = etdCar;
	}

	@Column(name = "ETD_AAE", length = 50)
	public String getEtdAae() {
		return this.etdAae;
	}

	public void setEtdAae(String etdAae) {
		this.etdAae = etdAae;
	}

	@Column(name = "ETD_TIP")
	public Byte getEtdTip() {
		return this.etdTip;
	}

	public void setEtdTip(Byte etdTip) {
		this.etdTip = etdTip;
	}

	@Column(name = "ID")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/*
	 * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	 * "entidad") public Set<Programacion> getProgramacions() { return
	 * this.programacions; }
	 * 
	 * public void setProgramacions(Set<Programacion> programacions) {
	 * this.programacions = programacions; }
	 * 
	 * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	 * "entidad") public Set<Productor> getProductors() { return
	 * this.productors; }
	 * 
	 * public void setProductors(Set<Productor> productors) { this.productors =
	 * productors; }
	 * 
	 * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	 * "entidad") public Set<Expediente> getExpedientes() { return
	 * this.expedientes; }
	 * 
	 * public void setExpedientes(Set<Expediente> expedientes) {
	 * this.expedientes = expedientes; }
	 * 
	 * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	 * "entidad") public Set<Expedientepersona> getExpedientepersonas() { return
	 * this.expedientepersonas; }
	 * 
	 * public void setExpedientepersonas(Set<Expedientepersona>
	 * expedientepersonas) { this.expedientepersonas = expedientepersonas; }
	 * 
	 * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	 * "entidad") public Set<Registropersona> getRegistropersonas() { return
	 * this.registropersonas; }
	 * 
	 * public void setRegistropersonas(Set<Registropersona> registropersonas) {
	 * this.registropersonas = registropersonas; }
	 * 
	 * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	 * "entidad") public Set<Parteinterviniente> getParteintervinientes() {
	 * return this.parteintervinientes; }
	 * 
	 * public void setParteintervinientes(Set<Parteinterviniente>
	 * parteintervinientes) { this.parteintervinientes = parteintervinientes; }
	 */
}
