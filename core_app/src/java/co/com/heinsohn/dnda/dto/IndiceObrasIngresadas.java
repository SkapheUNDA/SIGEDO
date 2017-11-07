package co.com.heinsohn.dnda.dto;

import java.io.Serializable;


/**
 * DTO control de informe indice obras ingresadas
 * @author gmedellin
 */

public class IndiceObrasIngresadas implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String clasificacion;
	private int cantidad;	
	
	public IndiceObrasIngresadas() {
	
	}
	
	public String getClasificacion() {
		return clasificacion;
	}
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
