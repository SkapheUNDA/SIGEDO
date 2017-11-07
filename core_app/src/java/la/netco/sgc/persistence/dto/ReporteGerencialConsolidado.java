package la.netco.sgc.persistence.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

/**
 * @author cpineros
 *
 */
@Entity
@Table(name = "Reporte_Gerencial_Consolidado", schema = Schemas.SGC_SCHEMA)
public class ReporteGerencialConsolidado implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer rgcIdRegistro;
	private String rgcNomEntidad;
	private String rgcValorConceptoUno;
	private String rgcValorConceptoDos;
	private String rgcValorConceptoTres;
	private String rgcValorConceptoCuatro;

	public ReporteGerencialConsolidado() {
	}

	public ReporteGerencialConsolidado(Integer rgcIdRegistro) {
		this.rgcIdRegistro = rgcIdRegistro;
	}
	
	

	public ReporteGerencialConsolidado(Integer rgcIdRegistro,
			String rgcNomEntidad, String rgcValorConceptoUno,
			String rgcValorConceptoDos, String rgcValorConceptoTres,
			String rgcValorConceptoCuatro) {
		super();
		this.rgcIdRegistro = rgcIdRegistro;
		this.rgcNomEntidad = rgcNomEntidad;
		this.rgcValorConceptoUno = rgcValorConceptoUno;
		this.rgcValorConceptoDos = rgcValorConceptoDos;
		this.rgcValorConceptoTres = rgcValorConceptoTres;
		this.rgcValorConceptoCuatro = rgcValorConceptoCuatro;
	}

	/**
	 * @return the rgcIdRegistro
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "RGC_Id_Registro", unique = true, nullable = false)
	public Integer getRgcIdRegistro() {
		return rgcIdRegistro;
	}

	/**
	 * @param rgcIdRegistro
	 *            the rgcIdRegistro to set
	 */
	public void setRgcIdRegistro(Integer rgcIdRegistro) {
		this.rgcIdRegistro = rgcIdRegistro;
	}

	/**
	 * @return the rgcNomEntidad
	 */
	@Column(name = "RGC_Nom_Entidad", length = 500)
	public String getRgcNomEntidad() {
		return rgcNomEntidad;
	}

	/**
	 * @param rgcNomEntidad
	 *            the rgcNomEntidad to set
	 */
	public void setRgcNomEntidad(String rgcNomEntidad) {
		this.rgcNomEntidad = rgcNomEntidad;
	}

	/**
	 * @return the rgcValorConceptoUno
	 */
	@Column(name = "RGC_Valor_ConceptoUno", length = 500)
	public String getRgcValorConceptoUno() {
		return rgcValorConceptoUno;
	}

	/**
	 * @param rgcValorConceptoUno
	 *            the rgcValorConceptoUno to set
	 */
	public void setRgcValorConceptoUno(String rgcValorConceptoUno) {
		this.rgcValorConceptoUno = rgcValorConceptoUno;
	}

	/**
	 * @return the rgcValorConceptoDos
	 */
	@Column(name = "RGC_Valor_ConceptoDos", length = 500)
	public String getRgcValorConceptoDos() {
		return rgcValorConceptoDos;
	}

	/**
	 * @param rgcValorConceptoDos
	 *            the rgcValorConceptoDos to set
	 */
	public void setRgcValorConceptoDos(String rgcValorConceptoDos) {
		this.rgcValorConceptoDos = rgcValorConceptoDos;
	}

	/**
	 * @return the rgcValorConceptoTres
	 */
	@Column(name = "RGC_Valor_ConceptoTres", length = 500)
	public String getRgcValorConceptoTres() {
		return rgcValorConceptoTres;
	}

	/**
	 * @param rgcValorConceptoTres
	 *            the rgcValorConceptoTres to set
	 */
	public void setRgcValorConceptoTres(String rgcValorConceptoTres) {
		this.rgcValorConceptoTres = rgcValorConceptoTres;
	}

	/**
	 * @return the rgcValorConceptoCuatro
	 */
	@Column(name = "RGC_Valor_ConceptoCuatro", length = 500)
	public String getRgcValorConceptoCuatro() {
		return rgcValorConceptoCuatro;
	}

	/**
	 * @param rgcValorConceptoCuatro
	 *            the rgcValorConceptoCuatro to set
	 */
	public void setRgcValorConceptoCuatro(String rgcValorConceptoCuatro) {
		this.rgcValorConceptoCuatro = rgcValorConceptoCuatro;
	}

}
