package co.com.heinsohn.dnda.dto;

import java.io.Serializable;


/**
 * DTO control de consulta registros fisicos y en linea 
 * @author gmedellin
 */

public class ConsultaCantidadRegistros implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String ubicacion;
	private int cantidad;	
	
	public ConsultaCantidadRegistros() {
	
	}
	
	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubiacion) {
		this.ubicacion = ubiacion;
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
