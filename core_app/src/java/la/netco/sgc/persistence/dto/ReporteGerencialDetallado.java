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
@Table(name = "Reporte_Gerencial_Detallado", schema = Schemas.SGC_SCHEMA)
public class ReporteGerencialDetallado implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer rgdIdRegistro;
	private String rgdNomEntidad;
	private String rgdConceptoUno;
	private String rgdConceptoDos;
	private String rgdConceptoTres;
	private String rgdConceptoCuatro;

	private String rgdValorRecaudoUno;
	private String rgdValorRecaudoDos;
	private String rgdValorRecaudoTres;
	private String rgdValorRecaudoCuatro;

	private String rgdValorGAdmonUno;
	private String rgdValorGAdmonDos;
	private String rgdValorGAdmonTres;
	private String rgdValorGAdmonCuatro;

	private String rgdValorGBienestarUno;
	private String rgdValorGBienestarDos;
	private String rgdValorGBienestarTres;
	private String rgdValorGBienestarCuatro;

	private String rgdValorDistribucionesUno;
	private String rgdValorDistribucionesDos;
	private String rgdValorDistribucionesTres;
	private String rgdValorDistribucionesCuatro;

	private String rgdSumaConceptoUno;
	private String rgdSumaConceptoDos;
	private String rgdSumaConceptoTres;
	private String rgdSumaConceptoCuatro;

	public ReporteGerencialDetallado() {
	}

	public ReporteGerencialDetallado(Integer rgdIdRegistro) {
		this.rgdIdRegistro = rgdIdRegistro;
	}

	public ReporteGerencialDetallado(Integer rgdIdRegistro,
			String rgdNomEntidad, String rgdConceptoUno, String rgdConceptoDos,
			String rgdConceptoTres, String rgdConceptoCuatro,
			String rgdValorRecaudoUno, String rgdValorRecaudoDos,
			String rgdValorRecaudoTres, String rgdValorRecaudoCuatro,
			String rgdValorGAdmonUno, String rgdValorGAdmonDos,
			String rgdValorGAdmonTres, String rgdValorGAdmonCuatro,
			String rgdValorGBienestarUno, String rgdValorGBienestarDos,
			String rgdValorGBienestarTres, String rgdValorGBienestarCuatro,
			String rgdValorDistribucionesUno, String rgdValorDistribucionesDos,
			String rgdValorDistribucionesTres,
			String rgdValorDistribucionesCuatro, String rgdSumaConceptoUno,
			String rgdSumaConceptoDos, String rgdSumaConceptoTres,
			String rgdSumaConceptoCuatro) {
		super();
		this.rgdIdRegistro = rgdIdRegistro;
		this.rgdNomEntidad = rgdNomEntidad;
		this.rgdConceptoUno = rgdConceptoUno;
		this.rgdConceptoDos = rgdConceptoDos;
		this.rgdConceptoTres = rgdConceptoTres;
		this.rgdConceptoCuatro = rgdConceptoCuatro;
		this.rgdValorRecaudoUno = rgdValorRecaudoUno;
		this.rgdValorRecaudoDos = rgdValorRecaudoDos;
		this.rgdValorRecaudoTres = rgdValorRecaudoTres;
		this.rgdValorRecaudoCuatro = rgdValorRecaudoCuatro;
		this.rgdValorGAdmonUno = rgdValorGAdmonUno;
		this.rgdValorGAdmonDos = rgdValorGAdmonDos;
		this.rgdValorGAdmonTres = rgdValorGAdmonTres;
		this.rgdValorGAdmonCuatro = rgdValorGAdmonCuatro;
		this.rgdValorGBienestarUno = rgdValorGBienestarUno;
		this.rgdValorGBienestarDos = rgdValorGBienestarDos;
		this.rgdValorGBienestarTres = rgdValorGBienestarTres;
		this.rgdValorGBienestarCuatro = rgdValorGBienestarCuatro;
		this.rgdValorDistribucionesUno = rgdValorDistribucionesUno;
		this.rgdValorDistribucionesDos = rgdValorDistribucionesDos;
		this.rgdValorDistribucionesTres = rgdValorDistribucionesTres;
		this.rgdValorDistribucionesCuatro = rgdValorDistribucionesCuatro;
		this.rgdSumaConceptoUno = rgdSumaConceptoUno;
		this.rgdSumaConceptoDos = rgdSumaConceptoDos;
		this.rgdSumaConceptoTres = rgdSumaConceptoTres;
		this.rgdSumaConceptoCuatro = rgdSumaConceptoCuatro;
	}

	/**
	 * @return the rgdIdRegistro
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "RGD_Id_Registro", unique = true, nullable = false)
	public Integer getRgdIdRegistro() {
		return rgdIdRegistro;
	}

	/**
	 * @param rgdIdRegistro the rgdIdRegistro to set
	 */
	public void setRgdIdRegistro(Integer rgdIdRegistro) {
		this.rgdIdRegistro = rgdIdRegistro;
	}

	/**
	 * @return the rgdNomEntidad
	 */
	@Column(name = "RGD_Nom_Entidad", length = 500)
	public String getRgdNomEntidad() {
		return rgdNomEntidad;
	}

	/**
	 * @param rgdNomEntidad the rgdNomEntidad to set
	 */
	public void setRgdNomEntidad(String rgdNomEntidad) {
		this.rgdNomEntidad = rgdNomEntidad;
	}

	/**
	 * @return the rgdConceptoUno
	 */
	@Column(name = "RGD_Concepto_Uno", length = 500)
	public String getRgdConceptoUno() {
		return rgdConceptoUno;
	}

	/**
	 * @param rgdConceptoUno the rgdConceptoUno to set
	 */
	public void setRgdConceptoUno(String rgdConceptoUno) {
		this.rgdConceptoUno = rgdConceptoUno;
	}

	/**
	 * @return the rgdConceptoDos
	 */
	@Column(name = "RGD_Concepto_Dos", length = 500)
	public String getRgdConceptoDos() {
		return rgdConceptoDos;
	}

	/**
	 * @param rgdConceptoDos the rgdConceptoDos to set
	 */
	public void setRgdConceptoDos(String rgdConceptoDos) {
		this.rgdConceptoDos = rgdConceptoDos;
	}

	/**
	 * @return the rgdConceptoTres
	 */
	@Column(name = "RGD_Concepto_Tres", length = 500)
	public String getRgdConceptoTres() {
		return rgdConceptoTres;
	}

	/**
	 * @param rgdConceptoTres the rgdConceptoTres to set
	 */
	public void setRgdConceptoTres(String rgdConceptoTres) {
		this.rgdConceptoTres = rgdConceptoTres;
	}

	/**
	 * @return the rgdConceptoCuatro
	 */
	@Column(name = "RGD_Concepto_Cuatro", length = 500)
	public String getRgdConceptoCuatro() {
		return rgdConceptoCuatro;
	}

	/**
	 * @param rgdConceptoCuatro the rgdConceptoCuatro to set
	 */
	public void setRgdConceptoCuatro(String rgdConceptoCuatro) {
		this.rgdConceptoCuatro = rgdConceptoCuatro;
	}

	/**
	 * @return the rgdValorRecaudoUno
	 */
	@Column(name = "RGD_Valor_Recaudo_Uno", length = 500)
	public String getRgdValorRecaudoUno() {
		return rgdValorRecaudoUno;
	}

	/**
	 * @param rgdValorRecaudoUno the rgdValorRecaudoUno to set
	 */
	public void setRgdValorRecaudoUno(String rgdValorRecaudoUno) {
		this.rgdValorRecaudoUno = rgdValorRecaudoUno;
	}

	/**
	 * @return the rgdValorRecaudoDos
	 */
	@Column(name = "RGD_Valor_Recaudo_Dos", length = 500)
	public String getRgdValorRecaudoDos() {
		return rgdValorRecaudoDos;
	}

	/**
	 * @param rgdValorRecaudoDos the rgdValorRecaudoDos to set
	 */
	public void setRgdValorRecaudoDos(String rgdValorRecaudoDos) {
		this.rgdValorRecaudoDos = rgdValorRecaudoDos;
	}

	/**
	 * @return the rgdValorRecaudoTres
	 */
	@Column(name = "RGD_Valor_Recaudo_Tres", length = 500)
	public String getRgdValorRecaudoTres() {
		return rgdValorRecaudoTres;
	}

	/**
	 * @param rgdValorRecaudoTres the rgdValorRecaudoTres to set
	 */
	public void setRgdValorRecaudoTres(String rgdValorRecaudoTres) {
		this.rgdValorRecaudoTres = rgdValorRecaudoTres;
	}

	/**
	 * @return the rgdValorRecaudoCuatro
	 */
	@Column(name = "RGD_Valor_Recaudo_Cuatro", length = 500)
	public String getRgdValorRecaudoCuatro() {
		return rgdValorRecaudoCuatro;
	}

	/**
	 * @param rgdValorRecaudoCuatro the rgdValorRecaudoCuatro to set
	 */
	public void setRgdValorRecaudoCuatro(String rgdValorRecaudoCuatro) {
		this.rgdValorRecaudoCuatro = rgdValorRecaudoCuatro;
	}

	/**
	 * @return the rgdValorGAdmonUno
	 */
	@Column(name = "RGD_Valor_GAdmon_Uno", length = 500)
	public String getRgdValorGAdmonUno() {
		return rgdValorGAdmonUno;
	}

	/**
	 * @param rgdValorGAdmonUno the rgdValorGAdmonUno to set
	 */
	public void setRgdValorGAdmonUno(String rgdValorGAdmonUno) {
		this.rgdValorGAdmonUno = rgdValorGAdmonUno;
	}

	/**
	 * @return the rgdValorGAdmonDos
	 */
	@Column(name = "RGD_Valor_GAdmon_Dos", length = 500)
	public String getRgdValorGAdmonDos() {
		return rgdValorGAdmonDos;
	}

	/**
	 * @param rgdValorGAdmonDos the rgdValorGAdmonDos to set
	 */
	public void setRgdValorGAdmonDos(String rgdValorGAdmonDos) {
		this.rgdValorGAdmonDos = rgdValorGAdmonDos;
	}

	/**
	 * @return the rgdValorGAdmonTres
	 */
	@Column(name = "RGD_Valor_GAdmon_Tres", length = 500)
	public String getRgdValorGAdmonTres() {
		return rgdValorGAdmonTres;
	}

	/**
	 * @param rgdValorGAdmonTres the rgdValorGAdmonTres to set
	 */
	public void setRgdValorGAdmonTres(String rgdValorGAdmonTres) {
		this.rgdValorGAdmonTres = rgdValorGAdmonTres;
	}

	/**
	 * @return the rgdValorGAdmonCuatro
	 */
	@Column(name = "RGD_Valor_GAdmon_Cuatro", length = 500)
	public String getRgdValorGAdmonCuatro() {
		return rgdValorGAdmonCuatro;
	}

	/**
	 * @param rgdValorGAdmonCuatro the rgdValorGAdmonCuatro to set
	 */
	public void setRgdValorGAdmonCuatro(String rgdValorGAdmonCuatro) {
		this.rgdValorGAdmonCuatro = rgdValorGAdmonCuatro;
	}

	/**
	 * @return the rgdValorGBienestarUno
	 */
	@Column(name = "RGD_Valor_GBienestar_Uno", length = 500)
	public String getRgdValorGBienestarUno() {
		return rgdValorGBienestarUno;
	}

	/**
	 * @param rgdValorGBienestarUno the rgdValorGBienestarUno to set
	 */
	public void setRgdValorGBienestarUno(String rgdValorGBienestarUno) {
		this.rgdValorGBienestarUno = rgdValorGBienestarUno;
	}

	/**
	 * @return the rgdValorGBienestarDos
	 */
	@Column(name = "RGD_Valor_GBienestar_Dos", length = 500)
	public String getRgdValorGBienestarDos() {
		return rgdValorGBienestarDos;
	}

	/**
	 * @param rgdValorGBienestarDos the rgdValorGBienestarDos to set
	 */
	public void setRgdValorGBienestarDos(String rgdValorGBienestarDos) {
		this.rgdValorGBienestarDos = rgdValorGBienestarDos;
	}

	/**
	 * @return the rgdValorGBienestarTres
	 */
	@Column(name = "RGD_Valor_GBienestar_Tres", length = 500)
	public String getRgdValorGBienestarTres() {
		return rgdValorGBienestarTres;
	}

	/**
	 * @param rgdValorGBienestarTres the rgdValorGBienestarTres to set
	 */
	public void setRgdValorGBienestarTres(String rgdValorGBienestarTres) {
		this.rgdValorGBienestarTres = rgdValorGBienestarTres;
	}

	/**
	 * @return the rgdValorGBienestarCuatro
	 */
	@Column(name = "RGD_Valor_GBienestar_Cuatro", length = 500)
	public String getRgdValorGBienestarCuatro() {
		return rgdValorGBienestarCuatro;
	}

	/**
	 * @param rgdValorGBienestarCuatro the rgdValorGBienestarCuatro to set
	 */
	public void setRgdValorGBienestarCuatro(String rgdValorGBienestarCuatro) {
		this.rgdValorGBienestarCuatro = rgdValorGBienestarCuatro;
	}

	/**
	 * @return the rgdValorDistribucionesUno
	 */
	@Column(name = "RGD_Valor_Distribuciones_Uno", length = 500)
	public String getRgdValorDistribucionesUno() {
		return rgdValorDistribucionesUno;
	}

	/**
	 * @param rgdValorDistribucionesUno the rgdValorDistribucionesUno to set
	 */
	public void setRgdValorDistribucionesUno(String rgdValorDistribucionesUno) {
		this.rgdValorDistribucionesUno = rgdValorDistribucionesUno;
	}

	/**
	 * @return the rgdValorDistribucionesDos
	 */
	@Column(name = "RGD_Valor_Distribuciones_Dos", length = 500)
	public String getRgdValorDistribucionesDos() {
		return rgdValorDistribucionesDos;
	}

	/**
	 * @param rgdValorDistribucionesDos the rgdValorDistribucionesDos to set
	 */
	public void setRgdValorDistribucionesDos(String rgdValorDistribucionesDos) {
		this.rgdValorDistribucionesDos = rgdValorDistribucionesDos;
	}

	/**
	 * @return the rgdValorDistribucionesTres
	 */
	@Column(name = "RGD_Valor_Distribuciones_Tres", length = 500)
	public String getRgdValorDistribucionesTres() {
		return rgdValorDistribucionesTres;
	}

	/**
	 * @param rgdValorDistribucionesTres the rgdValorDistribucionesTres to set
	 */
	public void setRgdValorDistribucionesTres(String rgdValorDistribucionesTres) {
		this.rgdValorDistribucionesTres = rgdValorDistribucionesTres;
	}

	/**
	 * @return the rgdValorDistribucionesCuatro
	 */
	@Column(name = "RGD_Valor_Distribuciones_Cuatro", length = 500)
	public String getRgdValorDistribucionesCuatro() {
		return rgdValorDistribucionesCuatro;
	}

	/**
	 * @param rgdValorDistribucionesCuatro the rgdValorDistribucionesCuatro to set
	 */
	public void setRgdValorDistribucionesCuatro(String rgdValorDistribucionesCuatro) {
		this.rgdValorDistribucionesCuatro = rgdValorDistribucionesCuatro;
	}

	/**
	 * @return the rgdSumaConceptoUno
	 */
	@Column(name = "RGD_Suma_Concepto_Uno", length = 500)
	public String getRgdSumaConceptoUno() {
		return rgdSumaConceptoUno;
	}

	/**
	 * @param rgdSumaConceptoUno the rgdSumaConceptoUno to set
	 */
	public void setRgdSumaConceptoUno(String rgdSumaConceptoUno) {
		this.rgdSumaConceptoUno = rgdSumaConceptoUno;
	}

	/**
	 * @return the rgdSumaConceptoDos
	 */
	@Column(name = "RGD_Suma_Concepto_Dos", length = 500)
	public String getRgdSumaConceptoDos() {
		return rgdSumaConceptoDos;
	}

	/**
	 * @param rgdSumaConceptoDos the rgdSumaConceptoDos to set
	 */
	public void setRgdSumaConceptoDos(String rgdSumaConceptoDos) {
		this.rgdSumaConceptoDos = rgdSumaConceptoDos;
	}

	/**
	 * @return the rgdSumaConceptoTres
	 */
	@Column(name = "RGD_Suma_Concepto_Tres", length = 500)
	public String getRgdSumaConceptoTres() {
		return rgdSumaConceptoTres;
	}

	/**
	 * @param rgdSumaConceptoTres the rgdSumaConceptoTres to set
	 */
	public void setRgdSumaConceptoTres(String rgdSumaConceptoTres) {
		this.rgdSumaConceptoTres = rgdSumaConceptoTres;
	}

	/**
	 * @return the rgdSumaConceptoCuatro
	 */
	@Column(name = "RGD_Suma_Concepto_Cuatro", length = 500)
	public String getRgdSumaConceptoCuatro() {
		return rgdSumaConceptoCuatro;
	}

	/**
	 * @param rgdSumaConceptoCuatro the rgdSumaConceptoCuatro to set
	 */
	public void setRgdSumaConceptoCuatro(String rgdSumaConceptoCuatro) {
		this.rgdSumaConceptoCuatro = rgdSumaConceptoCuatro;
	}

	
	
	
	
}
