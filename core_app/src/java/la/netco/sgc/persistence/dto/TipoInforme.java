package la.netco.sgc.persistence.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import la.netco.core.persistencia.vo.Schemas;

/**
 * 
 * @author Edwin Diaz
 *
 */

@Entity
@Table(name = "TIPOSDEINFORME", schema=Schemas.SGC_SCHEMA)
public class TipoInforme implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codigoInforme;
	private String descripcion;
	private String periocidad;
	
	
	@Id
	@Column(name = "SGC_COD_INFORME", nullable = false, unique=true, length = 50)
	public String getCodigoInforme() {
		return codigoInforme;
	}
	public void setCodigoInforme(String codigoInforme) {
		this.codigoInforme = codigoInforme;
	}
	@Column(name = "SGC_DESCRIPCION", nullable = false, unique=true, length = 250)
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Column(name = "SGC_PERIOCIDAD", nullable = false, unique=true, length = 50)
	public String getPeriocidad() {
		return periocidad;
	}
	public void setPeriocidad(String periocidad) {
		this.periocidad = periocidad;
	}
	
	

	
}
