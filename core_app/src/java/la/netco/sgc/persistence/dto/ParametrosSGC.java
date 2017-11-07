package la.netco.sgc.persistence.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

@Entity
@Table(name = "ParametrosSGC", schema = Schemas.SGC_SCHEMA)
@NamedQueries({
		@NamedQuery(name = ParametrosSGC.NAMED_QUERY_GET_BY_TWO, query = ParametrosSGC.QUERY_GET_BY_TWO),
		@NamedQuery(name = ParametrosSGC.NAMED_QUERY_GET_BY_THREE, query = ParametrosSGC.QUERY_GET_BY_THREE),
		@NamedQuery(name = ParametrosSGC.NAMED_QUERY_GET_BY_FOUR, query = ParametrosSGC.QUERY_GET_BY_FOUR) })
public class ParametrosSGC implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idParam;
	private String nombreParam;
	private String valorParam;

	public ParametrosSGC() {
	}

	public ParametrosSGC(Integer idParam, String nombreParam, String valorParam) {
		super();
		this.idParam = idParam;
		this.nombreParam = nombreParam;
		this.valorParam = valorParam;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PARAM_ID", unique = true, nullable = false)
	public Integer getIdParam() {
		return idParam;
	}

	/**
	 * @param idParam
	 *            the idParam to set
	 */
	public void setIdParam(Integer idParam) {
		this.idParam = idParam;
	}

	@Column(name = "PARAM_NOM", length = 200)
	public String getNombreParam() {
		return nombreParam;
	}

	/**
	 * @param nombreParam
	 *            the nombreParam to set
	 */
	public void setNombreParam(String nombreParam) {
		this.nombreParam = nombreParam;
	}

	@Column(name = "PARAM_VAL", length = 200)
	public String getValorParam() {
		return valorParam;
	}

	/**
	 * @param valorParam
	 *            the valorParam to set
	 */
	public void setValorParam(String valorParam) {
		this.valorParam = valorParam;
	}
	
	public static final String NAMED_QUERY_GET_BY_TWO = "getParamsByTwo";
	public static final String QUERY_GET_BY_TWO = "FROM ParametrosSGC psgc where psgc.idParam  = ?  or psgc.idParam  =? ";

	public static final String NAMED_QUERY_GET_BY_THREE = "getParamsByThree";
	public static final String QUERY_GET_BY_THREE = "FROM ParametrosSGC psgc where psgc.idParam  = ?  or psgc.idParam  =? or psgc.idParam =?";

	public static final String NAMED_QUERY_GET_BY_FOUR = "getParamsByFour";
	public static final String QUERY_GET_BY_FOUR = "FROM ParametrosSGC psgc where  psgc.idParam  =? or psgc.idParam  = ? or psgc.idParam  =? or psgc.idParam  =?";

}
