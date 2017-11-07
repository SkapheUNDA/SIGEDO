package la.netco.sgc.uilayer.beans.util;

import java.util.Arrays;
import java.util.List;

public enum FormatosEnum {
	BALANCE_GENERAL("BALANCE GENERAL"), BALANCE_TERCEROS("BCE POR TERCEROS"), EJECUCION_PRESUPUESTAL(
			"EJECUCION PPTAL"), SOCIOS_Y_ESTADO("LISTADO DE SOCIOS"), DETALLADO_DE_DISTRIBUCION(
			"DETALLADO DISTRIBUCION"), DETALLADO_DE_BIENESTAR_SOCIAL(
			"DETALLADO BIENESTAR SOCIAL"), INFORME_ANUAL_DE_GESTION_COLECTIVA(
			"INFORME GESTION COLECTIVA"), INFORME_ANUAL_DE_RESERVAS_APROBADAS(
			"INFORME ANUAL RESERVAS"), INFORME_ANUAL_DE_CARTERA_CASTIGADA(
			"INFORME ANUAL CARTERA CASTIGADA"), INFORME_ANUAL_DE_CARTERA_VENCIDA(
			"INFORME ANUAL CARTERA VENCIDA"), INFORME_ANUAL_DE_COBROS_JURIDICOS(
			"INFORME ANUAL COBROS JURIDICOS"), INFORME_ANUAL_DE_DONACIONES(
			"INFORME ANUAL DONACIONES"), INFORME_TRIMESTRAL_DE_RECAUDOS(
			"INFORME TRIMESTRAL RECAUDO"), INFORME_SOCIEDADES_EXTRANJERAS(
			"INFORME SOCIEDADES EXTRANJERAS");

	/**
	 * Contiene el identificador del enumerado
	 */
	private String id;

	/**
	 * Contiene el nombre del enumerado
	 */
	private String nombre;

	/**
	 * Crea una nueva instancia del AccionAprobacionEnum
	 * 
	 * @param id
	 *            identificador de la Accion
	 */
	private FormatosEnum(String id) {
		this.id = id;
		setNombre(this.toString());
	}

	/**
	 * Metodo que retorna el valor de id
	 * 
	 * @return El valor de id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Metodo que retorna el valor de nombre
	 * 
	 * @return El valor de nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Metodo encargado de asignar el valor de nombre
	 * 
	 * @param nombre
	 *            , Contiene el valor a asignar
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Metodo encargado de asignar el valor de id
	 * 
	 * @param id
	 *            , Contiene el valor a asignar
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Metodo encargado de devolver los enumerado de la clase en una lista
	 * 
	 * @return List<AccionAprobacionEnum>, Contiene la lista de enumerados
	 */
	public static List<FormatosEnum> asList() {
		return Arrays.asList(values());
	}

	/**
	 * Metodo encargado de devolver el enumerdo que corresponde al identificador
	 * recibido como parametro,
	 * 
	 * @param Long
	 *            id, Contiene el identificador del enumerado
	 * @return AccionAprobacionEnum, Contiene el enumerado encontrado
	 */
	public static FormatosEnum valueOf(Long id) {
		FormatosEnum[] values = FormatosEnum.values();
		for (int i = 0; i < values.length; i++) {
			if (id.equals(values[i].id)) {
				return values[i];
			}
		}
		return null;
	}
}
