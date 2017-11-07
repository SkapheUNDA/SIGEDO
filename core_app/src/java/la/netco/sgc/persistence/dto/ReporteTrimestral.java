/**
 * 
 */
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
 * @author cguzman
 *
 */
@Entity
@Table(name = "Reporte_Trimestral", schema = Schemas.SGC_SCHEMA)
public class ReporteTrimestral implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer rtrIdRegistro;
	
	private String rtrCuenta;
	
	private String rtrConcepto;
	
	private String rtrValorRecaudoA;
	
	private String rtrValorRecaudoB;
	
	private String rtrVariacion;
	
	private String rtrIncremento;
	
	
	public ReporteTrimestral() {
		// TODO Auto-generated constructor stub
	}
	
	public ReporteTrimestral(Integer rtrIdRegistro) {
		// TODO Auto-generated constructor stub
		this.rtrIdRegistro = rtrIdRegistro;
	}
	
	public ReporteTrimestral(Integer rtrIdRegistro, String rtrCuenta, String rtrConcepto, String rtrValorRecaudoA, String rtrValorRecaudoB, String rtrVariacion, String rtrIncremento) {
		// TODO Auto-generated constructor stub		
		this.rtrIdRegistro = rtrIdRegistro;
		this.rtrCuenta = rtrCuenta;		
		this.rtrConcepto = rtrConcepto;		
		this.rtrValorRecaudoA = rtrValorRecaudoA;		
		this.rtrValorRecaudoB = rtrValorRecaudoB;		
		this.rtrVariacion = rtrVariacion;		
		this.rtrIncremento = rtrIncremento;
	}
	
	/**
	 * @return the rtrIdRegistro
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "RTR_Id_Registro", unique = true, nullable = false)
	public Integer getRtrIdRegistro() {
		return rtrIdRegistro;
	}

	/**
	 * @param rtrIdRegistro the rtrIdRegistro to set
	 */
	public void setRtrIdRegistro(Integer rtrIdRegistro) {
		this.rtrIdRegistro = rtrIdRegistro;
	}

	/**
	 * @return the rtrCuenta
	 */
	@Column(name = "RTR_Cuenta", length = 255)
	public String getRtrCuenta() {
		return rtrCuenta;
	}

	/**
	 * @param rtrCuenta the rtrCuenta to set
	 */
	public void setRtrCuenta(String rtrCuenta) {
		this.rtrCuenta = rtrCuenta;
	}

	/**
	 * @return the rtrConcepto
	 */
	@Column(name = "RTR_Concepto", length = 255)
	public String getRtrConcepto() {
		return rtrConcepto;
	}

	/**
	 * @param rtrConcepto the rtrConcepto to set
	 */
	public void setRtrConcepto(String rtrConcepto) {
		this.rtrConcepto = rtrConcepto;
	}

	/**
	 * @return the rtrValorRecaudoA
	 */
	@Column(name = "RTR_Valor_Recaudo_A", length = 255)
	public String getRtrValorRecaudoA() {
		return rtrValorRecaudoA;
	}

	/**
	 * @param rtrValorRecaudoA the rtrValorRecaudoA to set
	 */
	public void setRtrValorRecaudoA(String rtrValorRecaudoA) {
		this.rtrValorRecaudoA = rtrValorRecaudoA;
	}

	/**
	 * @return the rtrValorRecaudoB
	 */
	@Column(name = "RTR_Valor_Recaudo_B", length = 255)
	public String getRtrValorRecaudoB() {
		return rtrValorRecaudoB;
	}

	/**
	 * @param rtrValorRecaudoB the rtrValorRecaudoB to set
	 */
	public void setRtrValorRecaudoB(String rtrValorRecaudoB) {
		this.rtrValorRecaudoB = rtrValorRecaudoB;
	}

	/**
	 * @return the rtrVariacion
	 */
	@Column(name = "RTR_Variacion", length = 255)
	public String getRtrVariacion() {
		return rtrVariacion;
	}

	/**
	 * @param rtrVariacion the rtrVariacion to set
	 */
	public void setRtrVariacion(String rtrVariacion) {
		this.rtrVariacion = rtrVariacion;
	}

	/**
	 * @return the rtrIncremento
	 */
	@Column(name = "RTR_Incremento", length = 255)
	public String getRtrIncremento() {
		return rtrIncremento;
	}

	/**
	 * @param rtrIncremento the rtrIncremento to set
	 */
	public void setRtrIncremento(String rtrIncremento) {
		this.rtrIncremento = rtrIncremento;
	}
	
	

}
