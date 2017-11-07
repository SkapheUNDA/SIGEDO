package la.netco.persistencia.dto.commons;

// Generated 9/10/2012 04:26:59 PM by Hibernate Tools 3.2.1.GA

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "CONFIGURACION", schema = Schemas.DBO_SCHEMA)
@NamedQueries({ @NamedQuery(name = Configuracion.NAMED_QUERY_GET_ALL_CONF, query = Configuracion.QUERY_GET_ALL_CONF) })
public class Configuracion implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private byte id;
	private String entidad;
	private String ciudad;
	private String titulo1;
	private String titulo2;
	private String titulo3;
	private String sigla;
	private String pbx;
	private String fax;
	private String email;
	private String website;
	private String entPre;
	private String salPre;
	private String expPre;
	private Integer entOpc;
	private Integer salOpc;
	private Integer expOpc;
	private String regTit1;
	private String regTit2;
	private String regTit3;
	private Integer regOpc1;
	private Integer regOpc2;
	private Integer regOpc3;
	private String regFij1;
	private Integer regFij2;
	private Integer regFij3;
	private Integer regIni1;
	private Integer regIni2;
	private Integer regIni3;
	private Integer regTer1;
	private Integer regTer2;
	private Integer regTer3;
	private boolean regUti1;
	private boolean regUti2;
	private boolean regUti3;
	private Integer regInic1;
	private Integer regInic2;
	private Integer regInic3;
	private Integer regTerc1;
	private Integer regTerc2;
	private Integer regTerc3;
	private String nombrerad;
	private String tablaret;
	private String provincia;
	private String localizacion;
	private Integer limite;
	private String nombrecodigo;
	private boolean alertacorrespond;
	private String sucursal;
	private Boolean alertaprogramacion;
	private String slogan;
	private Boolean alertaregistro;
	private String cargojefe;
	private String cuerpo1;
	private String cuerpo2;
	private Boolean lfirmec;
	private String direccion;
	private Date horalimite;
	private Date horainicio;
	private Integer limitediascontrasenia;
	private Blob logotipo;
	private Integer paisdefecto;
	private Integer ciudaddefecto;
	private Integer clasificaciondefecto;
	private Integer clasificacionsaldefecto;
	private Integer dependenciadefecto;
	private Integer calidaddefecto;
	private Integer tipopersonadefecto;
	private Boolean codManualEnt;
	private Boolean codManualSal;
	private Boolean codManualExp;
	private Byte tipoLetras;
	private String nombreAplicacion;
	private Boolean segManualExp;
	private Boolean segManualSal;
	private Boolean segManualEnt;
	private String cuerpotramite;
	
	public static Integer ENT_OPC_CONSECUTIVO = 1;

	public static Integer ENT_OPC_ANIO_CONSECUTIVO = 0;

	public static Integer ENT_OPC_DEPEND_CONSECUTIVO = 3;	

	public static Integer ENT_OPC_DEPEND_ANIO_CONSECUTIVO = 2;

	public static Integer ENT_OPC__ANIO_DEPEND_CONSECUTIVO = 4;
	

	public Configuracion() {
	}

	public Configuracion(byte id, boolean regUti1, boolean regUti2,
			boolean regUti3, boolean alertacorrespond) {
		this.id = id;
		this.regUti1 = regUti1;
		this.regUti2 = regUti2;
		this.regUti3 = regUti3;
		this.alertacorrespond = alertacorrespond;
	}

	public Configuracion(byte id, String entidad, String ciudad,
			String titulo1, String titulo2, String titulo3, String sigla,
			String pbx, String fax, String email, String website,
			String entPre, String salPre, String expPre, Integer entOpc,
			Integer salOpc, Integer expOpc, String regTit1, String regTit2,
			String regTit3, Integer regOpc1, Integer regOpc2, Integer regOpc3,
			String regFij1, Integer regFij2, Integer regFij3, Integer regIni1,
			Integer regIni2, Integer regIni3, Integer regTer1, Integer regTer2,
			Integer regTer3, boolean regUti1, boolean regUti2, boolean regUti3,
			Integer regInic1, Integer regInic2, Integer regInic3,
			Integer regTerc1, Integer regTerc2, Integer regTerc3,
			String nombrerad, String tablaret, String provincia,
			String localizacion, Integer limite, String nombrecodigo,
			boolean alertacorrespond, String sucursal,
			Boolean alertaprogramacion, String slogan, Boolean alertaregistro,
			String cargojefe, String cuerpo1, String cuerpo2, Boolean lfirmec,
			String direccion, Date horalimite, Date horainicio,
			Integer limitediascontrasenia, Blob logotipo, Integer paisdefecto,
			Integer ciudaddefecto, Integer clasificaciondefecto,
			Integer clasificacionsaldefecto, Integer dependenciadefecto,
			Integer calidaddefecto, Integer tipopersonadefecto,
			Boolean codManualEnt, Boolean codManualSal, Boolean codManualExp,
			Byte tipoLetras, String nombreAplicacion, Boolean segManualExp,
			Boolean segManualSal, Boolean segManualEnt, String cuerpotramite) {
		this.id = id;
		this.entidad = entidad;
		this.ciudad = ciudad;
		this.titulo1 = titulo1;
		this.titulo2 = titulo2;
		this.titulo3 = titulo3;
		this.sigla = sigla;
		this.pbx = pbx;
		this.fax = fax;
		this.email = email;
		this.website = website;
		this.entPre = entPre;
		this.salPre = salPre;
		this.expPre = expPre;
		this.entOpc = entOpc;
		this.salOpc = salOpc;
		this.expOpc = expOpc;
		this.regTit1 = regTit1;
		this.regTit2 = regTit2;
		this.regTit3 = regTit3;
		this.regOpc1 = regOpc1;
		this.regOpc2 = regOpc2;
		this.regOpc3 = regOpc3;
		this.regFij1 = regFij1;
		this.regFij2 = regFij2;
		this.regFij3 = regFij3;
		this.regIni1 = regIni1;
		this.regIni2 = regIni2;
		this.regIni3 = regIni3;
		this.regTer1 = regTer1;
		this.regTer2 = regTer2;
		this.regTer3 = regTer3;
		this.regUti1 = regUti1;
		this.regUti2 = regUti2;
		this.regUti3 = regUti3;
		this.regInic1 = regInic1;
		this.regInic2 = regInic2;
		this.regInic3 = regInic3;
		this.regTerc1 = regTerc1;
		this.regTerc2 = regTerc2;
		this.regTerc3 = regTerc3;
		this.nombrerad = nombrerad;
		this.tablaret = tablaret;
		this.provincia = provincia;
		this.localizacion = localizacion;
		this.limite = limite;
		this.nombrecodigo = nombrecodigo;
		this.alertacorrespond = alertacorrespond;
		this.sucursal = sucursal;
		this.alertaprogramacion = alertaprogramacion;
		this.slogan = slogan;
		this.alertaregistro = alertaregistro;
		this.cargojefe = cargojefe;
		this.cuerpo1 = cuerpo1;
		this.cuerpo2 = cuerpo2;
		this.lfirmec = lfirmec;
		this.direccion = direccion;
		this.horalimite = horalimite;
		this.horainicio = horainicio;
		this.limitediascontrasenia = limitediascontrasenia;
		this.logotipo = logotipo;
		this.paisdefecto = paisdefecto;
		this.ciudaddefecto = ciudaddefecto;
		this.clasificaciondefecto = clasificaciondefecto;
		this.clasificacionsaldefecto = clasificacionsaldefecto;
		this.dependenciadefecto = dependenciadefecto;
		this.calidaddefecto = calidaddefecto;
		this.tipopersonadefecto = tipopersonadefecto;
		this.codManualEnt = codManualEnt;
		this.codManualSal = codManualSal;
		this.codManualExp = codManualExp;
		this.tipoLetras = tipoLetras;
		this.nombreAplicacion = nombreAplicacion;
		this.segManualExp = segManualExp;
		this.segManualSal = segManualSal;
		this.segManualEnt = segManualEnt;
		this.cuerpotramite = cuerpotramite;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public byte getId() {
		return this.id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	@Column(name = "ENTIDAD", length = 100)
	public String getEntidad() {
		return this.entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	@Column(name = "CIUDAD", length = 100)
	public String getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	@Column(name = "TITULO1", length = 100)
	public String getTitulo1() {
		return this.titulo1;
	}

	public void setTitulo1(String titulo1) {
		this.titulo1 = titulo1;
	}

	@Column(name = "TITULO2", length = 100)
	public String getTitulo2() {
		return this.titulo2;
	}

	public void setTitulo2(String titulo2) {
		this.titulo2 = titulo2;
	}

	@Column(name = "TITULO3", length = 100)
	public String getTitulo3() {
		return this.titulo3;
	}

	public void setTitulo3(String titulo3) {
		this.titulo3 = titulo3;
	}

	@Column(name = "SIGLA", length = 100)
	public String getSigla() {
		return this.sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Column(name = "PBX", length = 100)
	public String getPbx() {
		return this.pbx;
	}

	public void setPbx(String pbx) {
		this.pbx = pbx;
	}

	@Column(name = "FAX", length = 100)
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "EMAIL", length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "WEBSITE", length = 100)
	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Column(name = "ENT_PRE", length = 20)
	public String getEntPre() {
		return this.entPre;
	}

	public void setEntPre(String entPre) {
		this.entPre = entPre;
	}

	@Column(name = "SAL_PRE", length = 20)
	public String getSalPre() {
		return this.salPre;
	}

	public void setSalPre(String salPre) {
		this.salPre = salPre;
	}

	@Column(name = "EXP_PRE", length = 20)
	public String getExpPre() {
		return this.expPre;
	}

	public void setExpPre(String expPre) {
		this.expPre = expPre;
	}

	@Column(name = "ENT_OPC")
	public Integer getEntOpc() {
		return this.entOpc;
	}

	public void setEntOpc(Integer entOpc) {
		this.entOpc = entOpc;
	}

	@Column(name = "SAL_OPC")
	public Integer getSalOpc() {
		return this.salOpc;
	}

	public void setSalOpc(Integer salOpc) {
		this.salOpc = salOpc;
	}

	@Column(name = "EXP_OPC")
	public Integer getExpOpc() {
		return this.expOpc;
	}

	public void setExpOpc(Integer expOpc) {
		this.expOpc = expOpc;
	}

	@Column(name = "REG_TIT1", length = 20)
	public String getRegTit1() {
		return this.regTit1;
	}

	public void setRegTit1(String regTit1) {
		this.regTit1 = regTit1;
	}

	@Column(name = "REG_TIT2", length = 20)
	public String getRegTit2() {
		return this.regTit2;
	}

	public void setRegTit2(String regTit2) {
		this.regTit2 = regTit2;
	}

	@Column(name = "REG_TIT3", length = 20)
	public String getRegTit3() {
		return this.regTit3;
	}

	public void setRegTit3(String regTit3) {
		this.regTit3 = regTit3;
	}

	@Column(name = "REG_OPC1")
	public Integer getRegOpc1() {
		return this.regOpc1;
	}

	public void setRegOpc1(Integer regOpc1) {
		this.regOpc1 = regOpc1;
	}

	@Column(name = "REG_OPC2")
	public Integer getRegOpc2() {
		return this.regOpc2;
	}

	public void setRegOpc2(Integer regOpc2) {
		this.regOpc2 = regOpc2;
	}

	@Column(name = "REG_OPC3")
	public Integer getRegOpc3() {
		return this.regOpc3;
	}

	public void setRegOpc3(Integer regOpc3) {
		this.regOpc3 = regOpc3;
	}

	@Column(name = "REG_FIJ1", length = 10)
	public String getRegFij1() {
		return this.regFij1;
	}

	public void setRegFij1(String regFij1) {
		this.regFij1 = regFij1;
	}

	@Column(name = "REG_FIJ2")
	public Integer getRegFij2() {
		return this.regFij2;
	}

	public void setRegFij2(Integer regFij2) {
		this.regFij2 = regFij2;
	}

	@Column(name = "REG_FIJ3")
	public Integer getRegFij3() {
		return this.regFij3;
	}

	public void setRegFij3(Integer regFij3) {
		this.regFij3 = regFij3;
	}

	@Column(name = "REG_INI1")
	public Integer getRegIni1() {
		return this.regIni1;
	}

	public void setRegIni1(Integer regIni1) {
		this.regIni1 = regIni1;
	}

	@Column(name = "REG_INI2")
	public Integer getRegIni2() {
		return this.regIni2;
	}

	public void setRegIni2(Integer regIni2) {
		this.regIni2 = regIni2;
	}

	@Column(name = "REG_INI3")
	public Integer getRegIni3() {
		return this.regIni3;
	}

	public void setRegIni3(Integer regIni3) {
		this.regIni3 = regIni3;
	}

	@Column(name = "REG_TER1")
	public Integer getRegTer1() {
		return this.regTer1;
	}

	public void setRegTer1(Integer regTer1) {
		this.regTer1 = regTer1;
	}

	@Column(name = "REG_TER2")
	public Integer getRegTer2() {
		return this.regTer2;
	}

	public void setRegTer2(Integer regTer2) {
		this.regTer2 = regTer2;
	}

	@Column(name = "REG_TER3")
	public Integer getRegTer3() {
		return this.regTer3;
	}

	public void setRegTer3(Integer regTer3) {
		this.regTer3 = regTer3;
	}

	@Column(name = "REG_UTI1", nullable = false)
	public boolean isRegUti1() {
		return this.regUti1;
	}

	public void setRegUti1(boolean regUti1) {
		this.regUti1 = regUti1;
	}

	@Column(name = "REG_UTI2", nullable = false)
	public boolean isRegUti2() {
		return this.regUti2;
	}

	public void setRegUti2(boolean regUti2) {
		this.regUti2 = regUti2;
	}

	@Column(name = "REG_UTI3", nullable = false)
	public boolean isRegUti3() {
		return this.regUti3;
	}

	public void setRegUti3(boolean regUti3) {
		this.regUti3 = regUti3;
	}

	@Column(name = "REG_INIC1")
	public Integer getRegInic1() {
		return this.regInic1;
	}

	public void setRegInic1(Integer regInic1) {
		this.regInic1 = regInic1;
	}

	@Column(name = "REG_INIC2")
	public Integer getRegInic2() {
		return this.regInic2;
	}

	public void setRegInic2(Integer regInic2) {
		this.regInic2 = regInic2;
	}

	@Column(name = "REG_INIC3")
	public Integer getRegInic3() {
		return this.regInic3;
	}

	public void setRegInic3(Integer regInic3) {
		this.regInic3 = regInic3;
	}

	@Column(name = "REG_TERC1")
	public Integer getRegTerc1() {
		return this.regTerc1;
	}

	public void setRegTerc1(Integer regTerc1) {
		this.regTerc1 = regTerc1;
	}

	@Column(name = "REG_TERC2")
	public Integer getRegTerc2() {
		return this.regTerc2;
	}

	public void setRegTerc2(Integer regTerc2) {
		this.regTerc2 = regTerc2;
	}

	@Column(name = "REG_TERC3")
	public Integer getRegTerc3() {
		return this.regTerc3;
	}

	public void setRegTerc3(Integer regTerc3) {
		this.regTerc3 = regTerc3;
	}

	@Column(name = "NOMBRERAD", length = 50)
	public String getNombrerad() {
		return this.nombrerad;
	}

	public void setNombrerad(String nombrerad) {
		this.nombrerad = nombrerad;
	}

	@Column(name = "TABLARET", length = 50)
	public String getTablaret() {
		return this.tablaret;
	}

	public void setTablaret(String tablaret) {
		this.tablaret = tablaret;
	}

	@Column(name = "PROVINCIA", length = 50)
	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	@Column(name = "LOCALIZACION", length = 50)
	public String getLocalizacion() {
		return this.localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	@Column(name = "LIMITE")
	public Integer getLimite() {
		return this.limite;
	}

	public void setLimite(Integer limite) {
		this.limite = limite;
	}

	@Column(name = "NOMBRECODIGO", length = 50)
	public String getNombrecodigo() {
		return this.nombrecodigo;
	}

	public void setNombrecodigo(String nombrecodigo) {
		this.nombrecodigo = nombrecodigo;
	}

	@Column(name = "ALERTACORRESPOND", nullable = false)
	public boolean isAlertacorrespond() {
		return this.alertacorrespond;
	}

	public void setAlertacorrespond(boolean alertacorrespond) {
		this.alertacorrespond = alertacorrespond;
	}

	@Column(name = "SUCURSAL", length = 50)
	public String getSucursal() {
		return this.sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	@Column(name = "ALERTAPROGRAMACION")
	public Boolean getAlertaprogramacion() {
		return this.alertaprogramacion;
	}

	public void setAlertaprogramacion(Boolean alertaprogramacion) {
		this.alertaprogramacion = alertaprogramacion;
	}

	@Column(name = "SLOGAN", length = 200)
	public String getSlogan() {
		return this.slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	@Column(name = "ALERTAREGISTRO")
	public Boolean getAlertaregistro() {
		return this.alertaregistro;
	}

	public void setAlertaregistro(Boolean alertaregistro) {
		this.alertaregistro = alertaregistro;
	}

	@Column(name = "CARGOJEFE", length = 50)
	public String getCargojefe() {
		return this.cargojefe;
	}

	public void setCargojefe(String cargojefe) {
		this.cargojefe = cargojefe;
	}

	@Column(name = "CUERPO1", length = 2000)
	public String getCuerpo1() {
		return this.cuerpo1;
	}

	public void setCuerpo1(String cuerpo1) {
		this.cuerpo1 = cuerpo1;
	}

	@Column(name = "CUERPO2", length = 2000)
	public String getCuerpo2() {
		return this.cuerpo2;
	}

	public void setCuerpo2(String cuerpo2) {
		this.cuerpo2 = cuerpo2;
	}

	@Column(name = "LFIRMEC")
	public Boolean getLfirmec() {
		return this.lfirmec;
	}

	public void setLfirmec(Boolean lfirmec) {
		this.lfirmec = lfirmec;
	}

	@Column(name = "DIRECCION", length = 100)
	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HORALIMITE", length = 23)
	public Date getHoralimite() {
		return this.horalimite;
	}

	public void setHoralimite(Date horalimite) {
		this.horalimite = horalimite;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HORAINICIO", length = 23)
	public Date getHorainicio() {
		return this.horainicio;
	}

	public void setHorainicio(Date horainicio) {
		this.horainicio = horainicio;
	}

	@Column(name = "LIMITEDIASCONTRASENIA")
	public Integer getLimitediascontrasenia() {
		return this.limitediascontrasenia;
	}

	public void setLimitediascontrasenia(Integer limitediascontrasenia) {
		this.limitediascontrasenia = limitediascontrasenia;
	}

	@Column(name = "LOGOTIPO")
	public Blob getLogotipo() {
		return this.logotipo;
	}

	public void setLogotipo(Blob logotipo) {
		this.logotipo = logotipo;
	}

	@Column(name = "PAISDEFECTO")
	public Integer getPaisdefecto() {
		return this.paisdefecto;
	}

	public void setPaisdefecto(Integer paisdefecto) {
		this.paisdefecto = paisdefecto;
	}

	@Column(name = "CIUDADDEFECTO")
	public Integer getCiudaddefecto() {
		return this.ciudaddefecto;
	}

	public void setCiudaddefecto(Integer ciudaddefecto) {
		this.ciudaddefecto = ciudaddefecto;
	}

	@Column(name = "CLASIFICACIONDEFECTO")
	public Integer getClasificaciondefecto() {
		return this.clasificaciondefecto;
	}

	public void setClasificaciondefecto(Integer clasificaciondefecto) {
		this.clasificaciondefecto = clasificaciondefecto;
	}

	@Column(name = "CLASIFICACIONSALDEFECTO")
	public Integer getClasificacionsaldefecto() {
		return this.clasificacionsaldefecto;
	}

	public void setClasificacionsaldefecto(Integer clasificacionsaldefecto) {
		this.clasificacionsaldefecto = clasificacionsaldefecto;
	}

	@Column(name = "DEPENDENCIADEFECTO")
	public Integer getDependenciadefecto() {
		return this.dependenciadefecto;
	}

	public void setDependenciadefecto(Integer dependenciadefecto) {
		this.dependenciadefecto = dependenciadefecto;
	}

	@Column(name = "CALIDADDEFECTO")
	public Integer getCalidaddefecto() {
		return this.calidaddefecto;
	}

	public void setCalidaddefecto(Integer calidaddefecto) {
		this.calidaddefecto = calidaddefecto;
	}

	@Column(name = "TIPOPERSONADEFECTO")
	public Integer getTipopersonadefecto() {
		return this.tipopersonadefecto;
	}

	public void setTipopersonadefecto(Integer tipopersonadefecto) {
		this.tipopersonadefecto = tipopersonadefecto;
	}

	@Column(name = "COD_MANUAL_ENT")
	public Boolean getCodManualEnt() {
		return this.codManualEnt;
	}

	public void setCodManualEnt(Boolean codManualEnt) {
		this.codManualEnt = codManualEnt;
	}

	@Column(name = "COD_MANUAL_SAL")
	public Boolean getCodManualSal() {
		return this.codManualSal;
	}

	public void setCodManualSal(Boolean codManualSal) {
		this.codManualSal = codManualSal;
	}

	@Column(name = "COD_MANUAL_EXP")
	public Boolean getCodManualExp() {
		return this.codManualExp;
	}

	public void setCodManualExp(Boolean codManualExp) {
		this.codManualExp = codManualExp;
	}

	@Column(name = "TIPO_LETRAS")
	public Byte getTipoLetras() {
		return this.tipoLetras;
	}

	public void setTipoLetras(Byte tipoLetras) {
		this.tipoLetras = tipoLetras;
	}

	@Column(name = "NOMBRE_APLICACION", length = 100)
	public String getNombreAplicacion() {
		return this.nombreAplicacion;
	}

	public void setNombreAplicacion(String nombreAplicacion) {
		this.nombreAplicacion = nombreAplicacion;
	}

	@Column(name = "SEG_MANUAL_EXP")
	public Boolean getSegManualExp() {
		return this.segManualExp;
	}

	public void setSegManualExp(Boolean segManualExp) {
		this.segManualExp = segManualExp;
	}

	@Column(name = "SEG_MANUAL_SAL")
	public Boolean getSegManualSal() {
		return this.segManualSal;
	}

	public void setSegManualSal(Boolean segManualSal) {
		this.segManualSal = segManualSal;
	}

	@Column(name = "SEG_MANUAL_ENT")
	public Boolean getSegManualEnt() {
		return this.segManualEnt;
	}

	public void setSegManualEnt(Boolean segManualEnt) {
		this.segManualEnt = segManualEnt;
	}

	@Column(name = "CUERPOTRAMITE")
	public String getCuerpotramite() {
		return this.cuerpotramite;
	}

	public void setCuerpotramite(String cuerpotramite) {
		this.cuerpotramite = cuerpotramite;
	}

	public static final String NAMED_QUERY_GET_ALL_CONF = "getAllConf";
	public static final String QUERY_GET_ALL_CONF = "From Configuracion configuracion";
}
