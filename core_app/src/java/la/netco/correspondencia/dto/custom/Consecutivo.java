package la.netco.correspondencia.dto.custom;

public class Consecutivo {

	private String consecutivoCompleto;
	private Integer consecutivo;
	public static Integer TIPO_ENTRADA = 1;
	public static Integer TIPO_SALIDA = 2;
	
	
	public String getConsecutivoCompleto() {
		return consecutivoCompleto;
	}
	public Integer getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivoCompleto(String consecutivoCompleto) {
		this.consecutivoCompleto = consecutivoCompleto;
	}
	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}
}
