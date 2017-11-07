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

import la.netco.core.persistencia.vo.Schemas;
import la.netco.persistencia.dto.commons.Entrada;

/**
 * @author cguzman
 * 
 */
/**
 * @author cpineros
 * 
 */
@Entity
@Table(name = "Registro_Radicado", schema = Schemas.SGC_SCHEMA)
@NamedQueries({
		@NamedQuery(name = RegistroRadicado.NAMED_QUERY_REGISTRO_RADICADO_BY_ENTIDAD_AND_CORTE, query = RegistroRadicado.QUERY_REGISTRO_RADICADO_BY_ENTIDAD_AND_CORTE)})		
public class RegistroRadicado implements Serializable {

	private static final long serialVersionUID = 1L;
	private int crrCodigo;	
	private Entidades entidad;
	private Entrada entrada;
	private CortesFormato cortesFormato;
	private Date fecha;

	public static final String NAMED_QUERY_REGISTRO_RADICADO_BY_ENTIDAD_AND_CORTE = "getRegistroRadicadoByEntidadAndCorte";
	public static final String QUERY_REGISTRO_RADICADO_BY_ENTIDAD_AND_CORTE = "from RegistroRadicado rcf where rcf.entidad.entCodigo =:entidad and rcf.cortesFormato.fcrCodigo=:corte";
	

	

	/**
	 * Default Constructor
	 * 
	 */
	public RegistroRadicado() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Minimal Constructor
	 * 
	 * @param rcfCodigo
	 */
	public RegistroRadicado(int crrCodigo) {
		// TODO Auto-generated constructor stub
		this.crrCodigo = crrCodigo;
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
	public RegistroRadicado(int crrCodigo, CortesFormato cortesFormato,
			Entidades entidad) {
		// TODO Auto-generated constructor stub
		this.crrCodigo = crrCodigo;
		this.cortesFormato = cortesFormato;
		this.entidad = entidad;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CRR_Codigo", unique = true, nullable = false)
	public int getCrrCodigo() {
		return this.crrCodigo;
	}

	public void setCrrCodigo(int crrCodigo) {
		this.crrCodigo = crrCodigo;
	}

	/**
	 * @return the entidades
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENT_Codigo")
	public Entidades getEntidad() {
		return entidad;
	}

	/**
	 * @param entidades
	 *            the entidades to set
	 */
	public void setEntidad(Entidades entidad) {
		this.entidad = entidad;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FCR_Codigo")
	public CortesFormato getCortesFormato() {
		return this.cortesFormato;
	}

	public void setCortesFormato(CortesFormato cortesFormato) {
		this.cortesFormato = cortesFormato;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTR_Codigo")
	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	@Column(name = "CRR_Fecha")
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	

	
	
}
