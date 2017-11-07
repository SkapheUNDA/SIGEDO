package co.com.heinsohn.dnda.dto;

import java.io.Serializable;


/**
 * DTO control de informe de devoluciones
 * @author gmedellin
 */

public class ConsultaDevoluciones implements Serializable {
	

	private static final long serialVersionUID = 1L;
	private String numeroSalida;
	private String asunto;	
	
	public ConsultaDevoluciones() {
	
	}

	public String getNumeroSalida() {
		return numeroSalida;
	}

	public void setNumeroSalida(String numeroSalida) {
		this.numeroSalida = numeroSalida;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
