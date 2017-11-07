package la.netco.sgc.persistence.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

/**
 * CortesFormato
 */
@Entity
@Table(name = "Cortes_Formato", schema = Schemas.SGC_SCHEMA)
@NamedQueries(value = {
		@NamedQuery(name = CortesFormato.NAMED_QUERY_CORTES_FORMATO_POR_FORMATO, query = CortesFormato.QUERY_CORTES_FORMATO_POR_FORMATO),
		@NamedQuery(name = CortesFormato.NAMED_QUERY_CORTES_FORMATO_POR_FECHA, query = CortesFormato.QUERY_CORTES_FORMATO_POR_FECHA),
		@NamedQuery(name = CortesFormato.NAMED_QUERY_CORTES_FORMATO_ACTIVO_POR_FORMATO, query = CortesFormato.QUERY_CORTES_FORMATO_ACTIVO_POR_FORMATO),
		@NamedQuery(name = CortesFormato.NAMED_QUERY_CORTES_FORMATO_INACTIVO_POR_FORMATO, query = CortesFormato.QUERY_CORTES_FORMATO_INACTIVO_POR_FORMATO),
		@NamedQuery(name = CortesFormato.NAMED_QUERY_GET_BY_FORMATO, query = CortesFormato.QUERY_GET_BY_FORMATO),
		@NamedQuery(name = CortesFormato.NAMED_QUERY_GET_BY_FORMATOF, query = CortesFormato.QUERY_GET_BY_FORMATOF),
		@NamedQuery(name = CortesFormato.NAMED_QUERY_GET_CORTES_DISTINCT_ANOS, query = CortesFormato.QUERY_GET_CORTES_DISTINCT_ANOS),
		@NamedQuery(name = CortesFormato.NAMED_QUERY_GET_BY_DUPLICADO, query = CortesFormato.QUERY_GET_BY_DUPLICADO)
})
public class CortesFormato implements java.io.Serializable {

	private static final long serialVersionUID = -1152155568573382882L;
	private int fcrCodigo;	
	private String fcrCorte;
	private String fcrPeriodo;
	private Boolean fcrVencido;
	private Boolean fcrActivo;
	private String ano;
	private Set<RegistroCargueHistorico> registroCargueHistoricos = new HashSet<RegistroCargueHistorico>(
			0);
	private Set<RegistroCargue> registroCargues = new HashSet<RegistroCargue>(0);
	private Set<RegistroCargueFormato> registroCargueFormatos = new HashSet<RegistroCargueFormato>(
			0);

	public CortesFormato() {
	}

	public CortesFormato(int fcrCodigo) {
		this.fcrCodigo = fcrCodigo;
	}

	public CortesFormato(int fcrCodigo, Formatos formatos, String fcrCorte,
			Boolean fcrVencido, Boolean fcrActivo,
			Set<RegistroCargueHistorico> registroCargueHistoricos,
			Set<RegistroCargue> registroCargues, String fcrPeriodo, String ano) {
		this.fcrCodigo = fcrCodigo;		
		this.fcrCorte = fcrCorte;
		this.fcrPeriodo = fcrPeriodo;
		this.fcrVencido = fcrVencido;
		this.fcrActivo = fcrActivo;
		this.registroCargueHistoricos = registroCargueHistoricos;
		this.registroCargues = registroCargues;
		this.ano=ano;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FCR_Codigo", unique = true, nullable = false)
	public int getFcrCodigo() {
		return this.fcrCodigo;
	}

	public void setFcrCodigo(int fcrCodigo) {
		this.fcrCodigo = fcrCodigo;
	}

	@Column(name = "FCR_Corte", length = 10)
	public String getFcrCorte() {
		return this.fcrCorte;
	}

	public void setFcrCorte(String fcrCorte) {
		this.fcrCorte = fcrCorte;
	}
	
	@Column(name = "FCR_Periodo", length = 50)
	public String getFcrPeriodo() {
		return fcrPeriodo;
	}

	public void setFcrPeriodo(String fcrPeriodo) {
		this.fcrPeriodo = fcrPeriodo;
	}

	@Column(name = "FCR_Vencido")
	public Boolean getFcrVencido() {
		return this.fcrVencido;
	}

	public void setFcrVencido(Boolean fcrVencido) {
		this.fcrVencido = fcrVencido;
	}

	@Column(name = "FCR_Activo")
	public Boolean getFcrActivo() {
		return this.fcrActivo;
	}

	public void setFcrActivo(Boolean fcrActivo) {
		this.fcrActivo = fcrActivo;
	}
	
	@Column(name = "FCR_Ano")
	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cortesFormato")
	public Set<RegistroCargueHistorico> getRegistroCargueHistoricos() {
		return this.registroCargueHistoricos;
	}

	public void setRegistroCargueHistoricos(
			Set<RegistroCargueHistorico> registroCargueHistoricos) {
		this.registroCargueHistoricos = registroCargueHistoricos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cortesFormato")
	public Set<RegistroCargue> getRegistroCargues() {
		return this.registroCargues;
	}

	public void setRegistroCargues(Set<RegistroCargue> registroCargues) {
		this.registroCargues = registroCargues;
	}

	/**
	 * @return the registroCargueFormatos
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cortesFormato")
	public Set<RegistroCargueFormato> getRegistroCargueFormatos() {
		return registroCargueFormatos;
	}

	/**
	 * @param registroCargueFormatos
	 *            the registroCargueFormatos to set
	 */
	public void setRegistroCargueFormatos(
			Set<RegistroCargueFormato> registroCargueFormatos) {
		this.registroCargueFormatos = registroCargueFormatos;
	}

	public static final String NAMED_QUERY_CORTES_FORMATO_POR_FORMATO = "getCorteFormatoByFormato";
	public static final String QUERY_CORTES_FORMATO_POR_FORMATO = "from CortesFormato cf";

	public static final String NAMED_QUERY_CORTES_FORMATO_POR_FECHA = "getCorteFormatoByFecha";
	public static final String QUERY_CORTES_FORMATO_POR_FECHA = "from CortesFormato cf where cf.fcrCorte = :corte";

	public static final String NAMED_QUERY_CORTES_FORMATO_ACTIVO_POR_FORMATO = "getCorteFormatoActivoByFormato";
	public static final String QUERY_CORTES_FORMATO_ACTIVO_POR_FORMATO = "from CortesFormato cf where cf.fcrActivo = true";
	
	public static final String NAMED_QUERY_CORTES_FORMATO_INACTIVO_POR_FORMATO = "getCorteFormatoInactivoByFormato";
	public static final String QUERY_CORTES_FORMATO_INACTIVO_POR_FORMATO = "from CortesFormato cf where cf.fcrActivo = false ORDER BY cf.fcrCorte desc";

	public static final String NAMED_QUERY_GET_BY_FORMATOF = "getCFByFormatoF";
	public static final String QUERY_GET_BY_FORMATOF = "FROM CortesFormato cortesformato where (cortesformato.fcrCorte LIKE  (?) or cortesformato.fcrCorte LIKE  (?) or cortesformato.fcrCorte LIKE  (?))";

	public static final String NAMED_QUERY_GET_BY_FORMATO = "getCFByFormato";
	public static final String QUERY_GET_BY_FORMATO = "from CortesFormato cf ORDER BY cf.fcrCorte ASC";

	public static final String NAMED_QUERY_GET_CORTES_DISTINCT_ANOS = "getCortesFormatoAnos";
	public static final String QUERY_GET_CORTES_DISTINCT_ANOS = "SELECT DISTINCT  year(cortesformato.fcrCorte)  FROM CortesFormato cortesformato ";
	
	public static final String NAMED_QUERY_GET_BY_DUPLICADO = "getCFByFormatoDuplicado";
	public static final String QUERY_GET_BY_DUPLICADO = "from CortesFormato cf where cf.fcrCorte  = ? and cf.fcrPeriodo = ? and cf.ano = ?";
	
	
	

}
