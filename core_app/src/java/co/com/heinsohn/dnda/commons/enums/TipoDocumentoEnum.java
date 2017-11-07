package co.com.heinsohn.dnda.commons.enums;

import java.io.Serializable;

import javax.faces.model.SelectItem;

public enum TipoDocumentoEnum implements Serializable {
	
	CEDULA_CIUDADANIA(7, "CEDULA DE CIUDADANIA", "CC"),
	CEDULA_EXTRANJERIA(8, "CEDULA DE EXTRANJERIA", "CE"),
	TARJETA_IDENTIDAD(9, "TARJETA DE IDENTIDAD", "TI"),
	PASAPORTE(10, "PASAPORTE", "PAS"),
	TARJETA_PROFESIONAL(11, "TARJETA PROFESIONAL", "TP"),
	OTRO(12, "OTRO", "OTR");
	
	private String nombre;
	
	private String abrebiatura;
	
	private Integer id;

	private TipoDocumentoEnum(Integer id, String nombre, String abrebiatura) {
		this.nombre = nombre;
		this.abrebiatura = abrebiatura;
		this.id = id;
	}
	
	/**
	 * Utilizando la abrebiatura, obtiene el enum que representa el tipo
	 * documento.
	 * 
	 * @param abrebiatura
	 *            La abrebiatura del enumerado.
	 * @return El enumerado que corresponda a la abrebiatura, o null si no se
	 *         corresponde.
	 */
	public static TipoDocumentoEnum asEnum(String abrebiatura) {
		if (abrebiatura == null) {
			return null;
		}
		TipoDocumentoEnum[] values = TipoDocumentoEnum.values();
		for (int i = 0; i < values.length; i++) {
			if (abrebiatura.trim().equals(values[i].abrebiatura)) {
				return values[i];
			}
		}
		return null;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getAbrebiatura() {
		return abrebiatura;
	}

	public void setAbrebiatura(String abrebiatura) {
		this.abrebiatura = abrebiatura;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public SelectItem darEntidad() {
		return new SelectItem(id, nombre, abrebiatura);
	}
}
