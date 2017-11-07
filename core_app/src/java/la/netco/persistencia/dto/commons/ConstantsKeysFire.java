package la.netco.persistencia.dto.commons;

import javax.faces.context.FacesContext;

public class ConstantsKeysFire {

	public static final String ENTRADA = "1";
	public static final String SALIDA = "2";
	public static final String EFOREXP = "E";
	
	/**
	 * Descripcion para el seguimiento de salida , a una salida entregada.
	 */
	public static final String ENTREGADO_OBS = "ENTREGADO";
	/**
	 * Persona que recibe la salida.
	 */
	public static final String USUARIO_PEN = "Registro en línea";
	/**
	 * Respuesta verdadera cuando es entregada  una salida
	 */
	public static final Boolean ENTREGADA = true;
	/**
	 * Folios asignados a una salida creada desde el registro - listado de impresion
	 */
	public static final Short FOLIOS_SALIDA_DEF=0;
	
	/**
	 * Especifica que la persona vive  -persona
	 */
	public static final Boolean PERVIV = false;
	/**
	 * Ciudad de seudonimo en persona
	 */
	public static final Short PERSEULUG = 1;
	/**
	 * Sector  de persona
	 */
	public static final Short PERSEC= 5;
	/**
	 * Sin especificar fecha Nac.
	 */
	public static final Boolean BOOLFECHNAC= true;
	/**
	 * Estado de creacion para un nuevo expediente.
	 */
	public static final String CREACIONEXP	 = "CREACION";
	/**
	 * Estado para expediente en  devolucion.
	 */
	public static final Integer STADEVEXP	 = 2;
	/**
	 * Texto para descripcion de estado devolucion
	 */
	public static final String  DEVOLUCION	 = "DEVOLUCIÓN";
	/**
	 * Texto para descripcion de estado rechazo
	 */
	public static final String  RECHAZO	 = "RECHAZO";
	/**
	 * Texto para cambio de etapa
	 */
	public static final String  CAMETAPA	 = "CAMBIO DE ETAPA";
	/**
	 * Id clase de contratos
	 */
	public static final Integer CLASECONTRATOS = 69;
	/**
	 * Id tipo de expedientes otros
	 */
	public static final Byte TIPOEXPOTROS = 2;
	
	//ESTADO DEFECTO ENTRADA
	public static final Short ESTADO_DEFECTO_ENTRADA = 236;
	
	// ESTADO DEFETECTO ENTRADA CAMBIO AUTOMATICO DE ETAPA
	public static final Short ESTADO_DEFECTO_ENTRADA_CAMBIO_AUTOMATICO = 237;
	
	//GRUPO ADMIN CORRESPONDENCIA
	public static final String GRUPO_ADMIN_CORRESPONDENCIA = "ADMIN_CORRESPONDENCIA";
	
	public static final String GRUPO_FIRMANTE = "FIRMANTE";
	
	public static final String GRUPO_ADMIN_TEMPLATE = "ADMIN_TEMPLATE";
	
	//GRUPO DIRECTIVO CORRESPONDENCIA
	public static final String GRUPO_DIRECTIVO_CORRESPONDENCIA = "DIRECTIVO_CORRESPONDENCIA";
	

	//GRUPO DIRECTIVO CORRESPONDENCIA
	public static final String GRUPO_REGISTRO_ABOGADOS = "REGISTRO_ABOGADOS";	

	//GRUPO DIRECTIVO CORRESPONDENCIA
	public static final String GRUPO_REGISTRO_TECNICO = "REGISTRO_TECNICOS";
	
	public static String RUTA_ABSOLUTA_TEMP_DOWNLOAD_FILE = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("rutaAbsolutaTempDownloadFile");
	public static String RUTA_PUBLICA_TEMP_DOWNLOAD_FILE = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("rutaPublicaTempDownloadFile");

	public static String RUTA_ARCHIVOS_REPORTES_JASPER=FacesContext.getCurrentInstance().getExternalContext().getInitParameter("rutaArchivosReportesJasper");
	public static String REPORTE_ETIQUETA_ENTRADA = "etiquetaEntrada.jasper";
	public static String REPORTE_ETIQUETA_SALIDA = "etiquetaSalida.jasper";
	public static String REPORTE_ETIQUETA_SALIDA_DESTINATARIO = "etiquetaSalidaDestinatario.jasper";
	public static String REPORTE_REGISTRO = "reporteRegistro.jasper";
	public static String REPORTE_CERTIFICADO = "reporteCertificado.jasper";
	public static String REPORTE_DEVOLUCION = "documentoDevoluciones.jasper";
	
	//ediaz incluir ruta para reportes de devoluciones recursoJasperDevolucion
	public static String RUTA_ABSOLUTA_DOWNLOAD_FILE  = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("rutaAbsolutaDosTempDownloadFile");
	public static String REPORTE_DEVOLUCION_JASPER = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("recursoJasperDevolucion");
	public static String RUTA_RELATIVA_DEVOLUCION =   FacesContext.getCurrentInstance().getExternalContext().getInitParameter("rutaRelativaDevolucion");
	
}
