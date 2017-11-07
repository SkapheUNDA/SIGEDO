/**
 * 
 */
package la.netco.sgc.persistence.dto;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import la.netco.core.persistencia.vo.Schemas;
import la.netco.core.persistencia.vo.Usuario;

/**
 * @author cguzman
 * 
 */
/**
 * @author cpineros
 * 
 */
@Entity
@Table(name = "Registro_Cargue_Formato", schema = Schemas.SGC_SCHEMA)
@NamedQueries({
		@NamedQuery(name = RegistroCargueFormato.NAMED_QUERY_REGISTRO_CARGUE_FORMATO_CARGUE, query = RegistroCargueFormato.QUERY_REGISTRO_CARGUE_FORMATO_CARGUE),
		@NamedQuery(name = RegistroCargueFormato.NAMED_QUERY_REGISTRO_CARGUE_FORMATO_SIN_CORTE, query = RegistroCargueFormato.QUERY_REGISTRO_CARGUE_FORMATO_SIN_CORTE)})		
public class RegistroCargueFormato implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer rcfCodigo;
	private Formatos formatos;
	private CortesFormato cortesFormato;
	private Date rcfFecha;
	private Entidades entidades;
	private Usuario usuario;

	public static final String NAMED_QUERY_REGISTRO_CARGUE_FORMATO_CARGUE = "getRegistroCargueFormatoCargue";
	public static final String QUERY_REGISTRO_CARGUE_FORMATO_CARGUE = "from RegistroCargueFormato rcf where rcf.formatos.forCodigo = :codFormato and rcf.entidades.entCodigo = :codEntidad and rcf.cortesFormato.fcrCodigo = :codCorte";
	public static final String NAMED_QUERY_REGISTRO_CARGUE_FORMATO_SIN_CORTE = "getRegistroCargueFormatoSinCorte";
	public static final String QUERY_REGISTRO_CARGUE_FORMATO_SIN_CORTE = "from RegistroCargueFormato rcf where rcf.formatos.forCodigo = :codFormato and rcf.entidades.entCodigo = :codEntidad";
	

	

	/**
	 * Default Constructor
	 * 
	 */
	public RegistroCargueFormato() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Minimal Constructor
	 * 
	 * @param rcfCodigo
	 */
	public RegistroCargueFormato(Integer rcfCodigo) {
		// TODO Auto-generated constructor stub
		this.rcfCodigo = rcfCodigo;
	}

	/**
	 * Full Constructor
	 * 
	 * @param rcfCodigo
	 * @param formatos
	 * @param cortesFormato
	 * @param rcfFecha
	 * @param entidades
	 */
	public RegistroCargueFormato(Integer rcfCodigo, Formatos formatos,
			CortesFormato cortesFormato, Date rcfFecha, Entidades entidades) {
		// TODO Auto-generated constructor stub
		this.rcfCodigo = rcfCodigo;
		this.formatos = formatos;
		this.cortesFormato = cortesFormato;
		this.rcfFecha = rcfFecha;
		this.entidades = entidades;
	}

	/**
	 * @return the rcfCodigo
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RCF_Codigo", unique = true, nullable = false)
	public Integer getRcfCodigo() {
		return rcfCodigo;
	}

	/**
	 * @param rcfCodigo
	 *            the rcfCodigo to set
	 */
	public void setRcfCodigo(Integer rcfCodigo) {
		this.rcfCodigo = rcfCodigo;
	}

	/**
	 * @return the formato
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FOR_Codigo")
	public Formatos getFormatos() {
		return formatos;
	}

	/**
	 * @param formato
	 *            the formato to set
	 */
	public void setFormatos(Formatos formatos) {
		this.formatos = formatos;
	}

	/**
	 * @return the cortesFormato
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCR_Codigo")
	public CortesFormato getCortesFormato() {
		return cortesFormato;
	}

	/**
	 * @param cortesFormato
	 *            the cortesFormato to set
	 */
	public void setCortesFormato(CortesFormato cortesFormato) {
		this.cortesFormato = cortesFormato;
	}

	/**
	 * @return the rcfFecha
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "RFC_Fecha", nullable = true)
	public Date getRcfFecha() {
		return rcfFecha;
	}

	/**
	 * @param rcfFecha
	 *            the rcfFecha to set
	 */
	public void setRcfFecha(Date rcfFecha) {
		this.rcfFecha = rcfFecha;
	}

	/**
	 * @return the entidades
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENT_Codigo")
	public Entidades getEntidades() {
		return entidades;
	}

	/**
	 * @param entidades
	 *            the entidades to set
	 */
	public void setEntidades(Entidades entidades) {
		this.entidades = entidades;
	}

	/**
	 * @return the usuarios
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USR_ID")
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	

}
