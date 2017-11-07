package co.com.heinsohn.dnda.entidades;

import java.io.Serializable;
import java.util.Date;

public class Consulta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4528855557462824164L;

	private Date fecha;
	
	private Date fechaMax;
	
	private String radicado;
	
	private String asunto;
	
	private Integer estado;
	
	private String detalle;
	
	private String urlAdjunto;
	
	public Consulta() {
		
	}

	public Consulta(Date fecha, String radicado, String asunto, Integer estado,
			String detalle, String urlAdjunto, Date fechaMax) {
		super();
		this.fecha = fecha;
		this.radicado = radicado;
		this.asunto = asunto;
		this.estado = estado;
		this.detalle = detalle;
		this.urlAdjunto = urlAdjunto;
		this.fechaMax = fechaMax;
	}
	
	public String getNombreEstado() {
		switch(estado) {
		case 1:
			return "Radicado";
		case 2:
			return "Respondido";
		default:
			return "Indefinido";
		}
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getRadicado() {
		return radicado;
	}

	public void setRadicado(String radicado) {
		this.radicado = radicado;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public String getUrlAdjunto() {
		return urlAdjunto;
	}

	public void setUrlAdjunto(String urlAdjunto) {
		this.urlAdjunto = urlAdjunto;
	}

	/**
	 * @return the fechaMax
	 */
	public Date getFechaMax() {
		return fechaMax;
	}

	/**
	 * @param fechaMax the fechaMax to set
	 */
	public void setFechaMax(Date fechaMax) {
		this.fechaMax = fechaMax;
	}
}
