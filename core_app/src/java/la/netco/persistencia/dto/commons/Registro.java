package la.netco.persistencia.dto.commons;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import la.netco.core.persistencia.vo.Schemas;
import la.netco.core.persistencia.vo.Usuario;

@Entity
@Table(name = "REGISTRO", schema = Schemas.DBO_SCHEMA)

public class Registro implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer regId;
	private Claseregistro claseregistro;
	private Usuario usuario;
	private Expediente expediente;
	private String regCod;
	private Date regFec;
	private Short regTco;
	private String regTor;
	private String regTes;
	private String regObj;
	private Short regPai;
	private Short regFcr;
	private String regCua;
	private String regPor;
	private String regApf;
	private String regVof;
	private String regNum;
	private Date regFte;
	private String regFpp;
	private String regNed;
	private Integer regNpg;
	private String regTir;
	private String regDob;
	private String regObs;
	private String regNo1;
	private Integer regNo2;
	private Integer regNo3;
	private String regSca;
	private String regCar;
	private boolean regSti;
	private Boolean regLvlr;
	private Boolean regLcua;
	private boolean regLsca;
	private boolean regLcar;
	private boolean regLgen;
	private String regDes;
	private String regFircar;
	private Boolean regLblo;
	private String regNre;
	private Date regFre;
	private Integer regIte;
	private String regIsbn;
	private Date regFecReal;

	// private Set<Parteinterviniente> parteintervinientes = new
	// HashSet<Parteinterviniente>(0);
	// private Set<Productor> productors = new HashSet<Productor>(0);
	// private Set<Obras> obrasesForObrRgt = new HashSet<Obras>(0);
	private Set<Titulos> tituloses = new HashSet<Titulos>(0);
	private Set<Registropersona> registropersonas = new HashSet<Registropersona>(0);
	private Set<Elementoregistro> elementoregistros = new HashSet<Elementoregistro>(0);
	private Set<Obras> obrasesForObrReg = new HashSet<Obras>(0);
	


	public Registro() {
	}

	public Registro(Integer regId, boolean regSti, boolean regLsca, boolean regLcar, boolean regLgen) {
		this.regId = regId;
		this.regSti = regSti;
		this.regLsca = regLsca;
		this.regLcar = regLcar;
		this.regLgen = regLgen;
	}

	// public Registro(int regId, Claseregistro claseregistro, UsuarioTemp
	// usuario, Integer regExp, String regCod, Date regFec, Short regTco, String
	// regTor, String regTes, String regObj, Short regPai, Short regFcr, String
	// regCua, String regPor, String regApf, String regVof, String regNum, Date
	// regFte, String regFpp, String regNed, Integer regNpg, String regTir,
	// String regDob, String regObs, String regNo1, Integer regNo2, Integer
	// regNo3, String regSca, String regCar, boolean regSti, Boolean regLvlr,
	// Boolean regLcua, boolean regLsca, boolean regLcar, boolean regLgen,
	// String regDes, String regFircar, Boolean regLblo, String regNre, Date
	// regFre, Integer regIte, String regIsbn, Set<Parteinterviniente>
	// parteintervinientes, Set<Productor> productors, Set<Obras>
	// obrasesForObrRgt, Set<Titulos> tituloses, Set<Registropersona>
	// registropersonas, Set<Elementoregistro> elementoregistros, Set<Obras>
	// obrasesForObrReg) {
	// this.regId = regId;
	// this.claseregistro = claseregistro;
	// this.usuario = usuario;
	// this.regExp = regExp;
	// this.regCod = regCod;
	// this.regFec = regFec;
	// this.regTco = regTco;
	// this.regTor = regTor;
	// this.regTes = regTes;
	// this.regObj = regObj;
	// this.regPai = regPai;
	// this.regFcr = regFcr;
	// this.regCua = regCua;
	// this.regPor = regPor;
	// this.regApf = regApf;
	// this.regVof = regVof;
	// this.regNum = regNum;
	// this.regFte = regFte;
	// this.regFpp = regFpp;
	// this.regNed = regNed;
	// this.regNpg = regNpg;
	// this.regTir = regTir;
	// this.regDob = regDob;
	// this.regObs = regObs;
	// this.regNo1 = regNo1;
	// this.regNo2 = regNo2;
	// this.regNo3 = regNo3;
	// this.regSca = regSca;
	// this.regCar = regCar;
	// this.regSti = regSti;
	// this.regLvlr = regLvlr;
	// this.regLcua = regLcua;
	// this.regLsca = regLsca;
	// this.regLcar = regLcar;
	// this.regLgen = regLgen;
	// this.regDes = regDes;
	// this.regFircar = regFircar;
	// this.regLblo = regLblo;
	// this.regNre = regNre;
	// this.regFre = regFre;
	// this.regIte = regIte;
	// this.regIsbn = regIsbn;
	// this.parteintervinientes = parteintervinientes;
	// this.productors = productors;
	// this.obrasesForObrRgt = obrasesForObrRgt;
	// this.tituloses = tituloses;
	// this.registropersonas = registropersonas;
	// this.elementoregistros = elementoregistros;
	// this.obrasesForObrReg = obrasesForObrReg;
	// }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "REG_ID", unique = true, nullable = false)
	public Integer getRegId() {
		return this.regId;
	}

	public void setRegId(Integer regId) {
		this.regId = regId;
	}

	@ManyToOne
	@JoinColumn(name = "REG_CRE")
	public Claseregistro getClaseregistro() {
		return this.claseregistro;
	}

	public void setClaseregistro(Claseregistro claseregistro) {
		this.claseregistro = claseregistro;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REG_FIR")
	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Column(name = "REG_COD", length = 100)
	public String getRegCod() {
		return this.regCod;
	}

	public void setRegCod(String regCod) {
		this.regCod = regCod;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REG_FEC", length = 23)
	public Date getRegFec() {
		return this.regFec;
	}

	public void setRegFec(Date regFec) {
		this.regFec = regFec;
	}

	@Column(name = "REG_TCO")
	public Short getRegTco() {
		return this.regTco;
	}

	public void setRegTco(Short regTco) {
		this.regTco = regTco;
	}

	@Column(name = "REG_TOR", length = 700)
	public String getRegTor() {
		return this.regTor;
	}

	public void setRegTor(String regTor) {
		this.regTor = regTor;
	}

	@Column(name = "REG_TES", length = 600)
	public String getRegTes() {
		return this.regTes;
	}

	public void setRegTes(String regTes) {
		this.regTes = regTes;
	}

	@Column(name = "REG_OBJ", length = 2100)
	public String getRegObj() {
		return this.regObj;
	}

	public void setRegObj(String regObj) {
		this.regObj = regObj;
	}

	@Column(name = "REG_PAI")
	public Short getRegPai() {
		return this.regPai;
	}

	public void setRegPai(Short regPai) {
		this.regPai = regPai;
	}

	@Column(name = "REG_FCR")
	public Short getRegFcr() {
		return this.regFcr;
	}

	public void setRegFcr(Short regFcr) {
		this.regFcr = regFcr;
	}

	@Column(name = "REG_CUA", length = 200)
	public String getRegCua() {
		return this.regCua;
	}

	public void setRegCua(String regCua) {
		this.regCua = regCua;
	}

	@Column(name = "REG_POR", length = 200)
	public String getRegPor() {
		return this.regPor;
	}

	public void setRegPor(String regPor) {
		this.regPor = regPor;
	}

	@Column(name = "REG_APF", length = 20)
	public String getRegApf() {
		return this.regApf;
	}

	public void setRegApf(String regApf) {
		this.regApf = regApf;
	}

	@Column(name = "REG_VOF", length = 20)
	public String getRegVof() {
		return this.regVof;
	}

	public void setRegVof(String regVof) {
		this.regVof = regVof;
	}

	@Column(name = "REG_NUM", length = 100)
	public String getRegNum() {
		return this.regNum;
	}

	public void setRegNum(String regNum) {
		this.regNum = regNum;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REG_FTE", length = 23)
	public Date getRegFte() {
		return this.regFte;
	}

	public void setRegFte(Date regFte) {
		this.regFte = regFte;
	}

	@Column(name = "REG_FPP", length = 50)
	public String getRegFpp() {
		return this.regFpp;
	}

	public void setRegFpp(String regFpp) {
		this.regFpp = regFpp;
	}

	@Column(name = "REG_NED", length = 50)
	public String getRegNed() {
		return this.regNed;
	}

	public void setRegNed(String regNed) {
		this.regNed = regNed;
	}

	@Column(name = "REG_NPG")
	public Integer getRegNpg() {
		return this.regNpg;
	}

	public void setRegNpg(Integer regNpg) {
		this.regNpg = regNpg;
	}

	@Column(name = "REG_TIR", length = 50)
	public String getRegTir() {
		return this.regTir;
	}

	public void setRegTir(String regTir) {
		this.regTir = regTir;
	}

	@Column(name = "REG_DOB", length = 100)
	public String getRegDob() {
		return this.regDob;
	}

	public void setRegDob(String regDob) {
		this.regDob = regDob;
	}

	@Column(name = "REG_OBS", length = 2300)
	public String getRegObs() {
		return this.regObs;
	}

	public void setRegObs(String regObs) {
		this.regObs = regObs;
	}

	@Column(name = "REG_NO1", length = 10)
	public String getRegNo1() {
		return this.regNo1;
	}

	public void setRegNo1(String regNo1) {
		this.regNo1 = regNo1;
	}

	@Column(name = "REG_NO2")
	public Integer getRegNo2() {
		return this.regNo2;
	}

	public void setRegNo2(Integer regNo2) {
		this.regNo2 = regNo2;
	}

	@Column(name = "REG_NO3")
	public Integer getRegNo3() {
		return this.regNo3;
	}

	public void setRegNo3(Integer regNo3) {
		this.regNo3 = regNo3;
	}

	@Column(name = "REG_SCA", length = 254)
	public String getRegSca() {
		return this.regSca;
	}

	public void setRegSca(String regSca) {
		this.regSca = regSca;
	}

	@Column(name = "REG_CAR", length = 254)
	public String getRegCar() {
		return this.regCar;
	}

	public void setRegCar(String regCar) {
		this.regCar = regCar;
	}

	/**
	 * 
	 * Sin Titulo
	 * 
	 * @return
	 */
	@Column(name = "REG_STI", nullable = false)
	public boolean isRegSti() {
		return this.regSti;
	}

	public void setRegSti(boolean regSti) {
		this.regSti = regSti;
	}

	@Column(name = "REG_LVLR")
	public Boolean getRegLvlr() {
		return this.regLvlr;
	}

	public void setRegLvlr(Boolean regLvlr) {
		this.regLvlr = regLvlr;
	}

	@Column(name = "REG_LCUA")
	public Boolean getRegLcua() {
		return this.regLcua;
	}

	public void setRegLcua(Boolean regLcua) {
		this.regLcua = regLcua;
	}

	@Column(name = "REG_LSCA", nullable = false)
	public boolean isRegLsca() {
		return this.regLsca;
	}

	public void setRegLsca(boolean regLsca) {
		this.regLsca = regLsca;
	}

	@Column(name = "REG_LCAR", nullable = false)
	public boolean isRegLcar() {
		return this.regLcar;
	}

	public void setRegLcar(boolean regLcar) {
		this.regLcar = regLcar;
	}

	@Column(name = "REG_LGEN", nullable = false)
	public boolean isRegLgen() {
		return this.regLgen;
	}

	public void setRegLgen(boolean regLgen) {
		this.regLgen = regLgen;
	}

	@Column(name = "REG_DES", length = 500)
	public String getRegDes() {
		return this.regDes;
	}

	public void setRegDes(String regDes) {
		this.regDes = regDes;
	}

	@Column(name = "REG_FIRCAR", length = 50)
	public String getRegFircar() {
		return this.regFircar;
	}

	public void setRegFircar(String regFircar) {
		this.regFircar = regFircar;
	}

	@Column(name = "REG_LBLO")
	public Boolean getRegLblo() {
		return this.regLblo;
	}

	public void setRegLblo(Boolean regLblo) {
		this.regLblo = regLblo;
	}

	@Column(name = "REG_NRE", length = 20)
	public String getRegNre() {
		return this.regNre;
	}

	public void setRegNre(String regNre) {
		this.regNre = regNre;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REG_FRE", length = 23)
	public Date getRegFre() {
		return this.regFre;
	}

	public void setRegFre(Date regFre) {
		this.regFre = regFre;
	}

	@Column(name = "REG_ITE")
	public Integer getRegIte() {
		return this.regIte;
	}

	public void setRegIte(Integer regIte) {
		this.regIte = regIte;
	}

	@Column(name = "REG_ISBN", length = 50)
	public String getRegIsbn() {
		return this.regIsbn;
	}

	public void setRegIsbn(String regIsbn) {
		this.regIsbn = regIsbn;
	}

	@ManyToOne
	@JoinColumn(name = "REG_EXP", nullable = false)
	public Expediente getExpediente() {
		return expediente;
	}

	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}

	/*
	 * @Column(name = "REG_EXP") public Integer getRegExp() { return
	 * this.regExp; }
	 * 
	 * public void setRegExp(Integer regExp) { this.regExp = regExp; }
	 */
	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "registro")
	// public Set<Parteinterviniente> getParteintervinientes() {
	// return this.parteintervinientes;
	// }
	//
	// public void setParteintervinientes(Set<Parteinterviniente>
	// parteintervinientes) {
	// this.parteintervinientes = parteintervinientes;
	// }
	//
	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "registro")
	// public Set<Productor> getProductors() {
	// return this.productors;
	// }
	//
	// public void setProductors(Set<Productor> productors) {
	// this.productors = productors;
	// }
	//
	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "registroByObrRgt")
	// public Set<Obras> getObrasesForObrRgt() {
	// return this.obrasesForObrRgt;
	// }
	//
	// public void setObrasesForObrRgt(Set<Obras> obrasesForObrRgt) {
	// this.obrasesForObrRgt = obrasesForObrRgt;
	// }
	//
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "registro")
	public Set<Titulos> getTituloses() {
		return this.tituloses;
	}

	public void setTituloses(Set<Titulos> tituloses) {
		this.tituloses = tituloses;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "registro")
	public Set<Registropersona> getRegistropersonas() {
		return this.registropersonas;
	}

	public void setRegistropersonas(Set<Registropersona> registropersonas) {
		this.registropersonas = registropersonas;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "registro")
	public Set<Elementoregistro> getElementoregistros() {
		return this.elementoregistros;
	}

	public void setElementoregistros(Set<Elementoregistro> elementoregistros) {
		this.elementoregistros = elementoregistros;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "registroByObrReg")
	public Set<Obras> getObrasesForObrReg() {
		return this.obrasesForObrReg;
	}

	public void setObrasesForObrReg(Set<Obras> obrasesForObrReg) {
		this.obrasesForObrReg = obrasesForObrReg;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REG_FECREAL", nullable = true)
	public Date getRegFecReal() {
		return regFecReal;
	}

	public void setRegFecReal(Date regFecReal) {
		this.regFecReal = regFecReal;
	}

}
