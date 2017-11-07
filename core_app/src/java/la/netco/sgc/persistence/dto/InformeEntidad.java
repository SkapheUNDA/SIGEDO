/**
 * 
 */
package la.netco.sgc.persistence.dto;

import java.io.Serializable;

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

/**
 * @author cguzman
 * 
 */
/**
 * @author cpineros
 * 
 */
@Entity
@Table(name = "Informes_Entidad", schema = Schemas.SGC_SCHEMA)
@NamedQueries({
		@NamedQuery(name = InformeEntidad.NAMED_QUERY_INFORME_ENTIDAD_BY_ENTIDAD, query = InformeEntidad.QUERY_INFORME_ENTIDAD_BY_ENTIDAD),
		@NamedQuery(name = InformeEntidad.NAMED_QUERY_INFORME_ENTIDAD_BY_ENTIDAD_AND_FORMATO, query = InformeEntidad.QUERY_INFORME_ENTIDAD_BY_ENTIDAD_AND_FORMATO),
		@NamedQuery(name = InformeEntidad.NAMED_QUERY_INFORME_ENTIDAD_BY_ID, query = InformeEntidad.QUERY_INFORME_ENTIDAD_BY_ENTIDAD_ID)
		
		
})		
public class InformeEntidad implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Formatos formato;
	private Entidades entidad;
	private Boolean obligatorio;
	private Boolean opcional;

	public static final String NAMED_QUERY_INFORME_ENTIDAD_BY_ENTIDAD = "getInformeEntidadByEntidad";
	public static final String QUERY_INFORME_ENTIDAD_BY_ENTIDAD = "from InformeEntidad rcf where rcf.entidad.entCodigo = ?";
	
	public static final String NAMED_QUERY_INFORME_ENTIDAD_BY_ENTIDAD_AND_FORMATO = "getInformeEntidadByEntidadAndInforme";
	public static final String QUERY_INFORME_ENTIDAD_BY_ENTIDAD_AND_FORMATO = "from InformeEntidad rcf where rcf.entidad.entCodigo = ? and rcf.formato.forCodigo = ?";
	
	public static final String NAMED_QUERY_INFORME_ENTIDAD_BY_ID = "getInformeEntidadById";
	public static final String QUERY_INFORME_ENTIDAD_BY_ENTIDAD_ID = "from InformeEntidad ent  left join fetch  ent.formato left join fetch ent.entidad where ent.id = ?";
	
	
	
	
	

	

	/**
	 * Default Constructor
	 * 
	 */
	public InformeEntidad() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Minimal Constructor
	 * 
	 * @param rcfCodigo
	 */
	public InformeEntidad(Integer id) {
		// TODO Auto-generated constructor stub
		this.id = id;
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
	public InformeEntidad(Integer id, Formatos formato,
			Entidades entidad) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.formato = formato;
		this.entidad = entidad;
	}

	/**
	 * @return the rcfCodigo
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	/**
	 * @param rcfCodigo
	 *            the rcfCodigo to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the formato    
	 */
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FOR_Codigo")
	public Formatos getFormato() {
		return formato;
	}

	/**
	 * @param formato
	 *            the formato to set
	 */
	public void setFormato(Formatos formato) {
		this.formato = formato;
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

	@Column(name = "Obligatorio")
	public Boolean getObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(Boolean obligatorio) {
		this.obligatorio = obligatorio;
	}
	@Column(name = "Opcional")
	public Boolean getOpcional() {
		return opcional;
	}

	public void setOpcional(Boolean opcional) {
		this.opcional = opcional;
	}
	
}
