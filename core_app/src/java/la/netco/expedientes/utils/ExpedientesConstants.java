package la.netco.expedientes.utils;

public class ExpedientesConstants {

	/**
	 * Seguridad para accion cambiar etapa
	 */
	public static final String CAMBIA_E = "CAMBIA_E";
	/**
	 * Seguridad para accion de duplicacion d expediente
	 */
	public static final String DUPLEX_E = "DUPLEX_E";
	/**
	 * Seguridad para accion informacion del expediente
	 */
	public static final String INFO_EXP = "INFO_EXP";
	/**
	 * Seguridad para accion de devolver expediente
	 */
	public static final String DEV_EXP = "DEV_EXP";
	/**
	 * Seguridad para accion de adjuntar archivos para un expediente.
	 */
	public static final String ADJ_EXP = "ADJ_EXP";
	/**
	 * Seguridad para accion de generar archivo para un expediente.
	 */
	public static final String DOC_EXP = "DOC_EXP";
	/**
	 * Seguridad para accion de agregar anotacion al expediente
	 */
	public static final String ADD_ANO = "ADD_ANO";
	/**
	 * Seguridad para accion de actualizar anotacion al expediente
	 */
	public static final String UPD_ANO = "UPD_ANO";
	/**
	 * Seguridad para accion de eliminar anotacion al expediente
	 */
	public static final String DEL_ANO = "DEL_ANO";
	/**
	 * Seguridad para accion de agregar anexo al expediente
	 */
	public static final String ADD_ANX = "ADD_ANX";
	/**
	 * Seguridad para accion de actualizar anexo al expediente
	 */
	public static final String UPD_ANX = "UPD_ANX";
	/**
	 * Seguridad para accion de eliminar anexo al expediente
	 */
	public static final String DEL_ANX = "DEL_ANX";
	/**
	 * Seguridad para accion de agregar otra persona al expediente
	 */
	public static final String ADD_OTR = "ADD_OTR";
	/**
	 * Seguridad para accion de actualizar otra persona al expediente
	 */
	public static final String UPD_OTR = "UPD_OTR";
	/**
	 * Seguridad para accion de eliminar otra persona al expediente
	 */
	public static final String DEL_OTR = "DEL_OTR";

	/**
	 * Seguridad para agregar detalle de programacion
	 */
	public static final String ADD_DTL = "ADD_DTL";
	/**
	 * Seguridad para actualizar detalle de programacion
	 */
	public static final String UPD_DTL = "UPD_DTL";
	/**
	 * Seguridad para eliminar detalle de programacion
	 */
	public static final String DEL_DTL = "DEL_DTL";
	/**
	 * Seguridad para duplicar detalle de programacion
	 */
	public static final String DUPLEX_DTL = "DUPLEX_DTL";

	/**
	 * Codigo Exp: Prefijo + Año + Consecutivo
	 */
	public static Integer EXP_OPC_ANIO_CONSECUTIVO = 0;
	/**
	 * Codigo Exp: Prefijo + Consecutivo
	 */
	public static Integer EXP_OPC_CONSECUTIVO = 1;
	/**
	 * Codigo Exp:Prefijo + Dependencia + Año + Consecutivo
	 */
	public static Integer EXP_OPC_DEPEND_ANIO_CONSECUTIVO = 2;
	/**
	 * Codigo Exp: Prefijo + Dependencia + Consecutivo
	 */
	public static Integer EXP_OPC_DEPEND_CONSECUTIVO = 3;
	/**
	 * Codigo Exp: Prefijo + Año + Dependencia + Consecutivo
	 */
	public static Integer EXP_OPC__ANIO_DEPEND_CONSECUTIVO = 4;

	public static final String VAL_EST_INI = "244";
	/**
	 * Status para un nuevo expediente , El Estado de creacion que era 0 cambia
	 * a 1 , el estado de actualizacion de status que era 1 cambia a 2.
	 */
	public static final Integer VAL_EXP_STA = 1;
	/**
	 * Id tramite para un nuevo expediente desde entrada
	 */
	public static final String VAL_TRM_EXP_ENT = "26";
	/**
	 * Ubicacion determinada para el expediente si es igual a 17
	 */
	public static final String VAL_EXP_UBIUNO = "4";
	/**
	 * Ubicacion determinada para el expediente si es diferente a 17
	 */
	public static final String VAL_EXP_UBIDOS = "5";
	/**
	 * VBO defecto para un nuevo expediente
	 */
	public static final String VAL_EXP_VBO = "false";
	/**
	 * Tipo de expediente Clase de registro
	 */
	public static final String TIPOEXP_CR = "1";
	/**
	 * Tipo de expediente Otros expediente
	 */
	public static final String TIPOEXP_OE = "2";
	/**
	 * Evento inicial para un nuevo expediente
	 */
	public static final String TIPOVENTO_INICIAL = "1";
	/**
	 * Evento final para un nuevo expediente
	 */
	public static final String TIPOEVENTO_SECUNDARIO = "2";
	/**
	 * Id tipo de correspondencia para un nuevo expediente desde entrada
	 */
	public static final String TIPOCORRESP_EXP = "1";
	/**
	 * Bit correspondencia inicial
	 */
	public static final String CORRESP_INI_EXP = "true";

}
