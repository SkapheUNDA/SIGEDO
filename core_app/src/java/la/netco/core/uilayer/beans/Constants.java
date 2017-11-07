package la.netco.core.uilayer.beans;

import javax.faces.context.FacesContext;


public class Constants {



	public static String RUTA_ABSOLUTA_TEMP_DOWNLOAD_FILE = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("rutaAbsolutaTempDownloadFile");
	public static String RUTA_ABSOLUTA_DOS_TEMP_DOWNLOAD_FILE = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("rutaAbsolutaDosTempDownloadFile");
	public static String RUTA_PUBLICA_TEMP_DOWNLOAD_FILE = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("rutaPublicaTempDownloadFile");
	public static String RUTA_ARCHIVOS_TEMPLATE_EXPORT_EXCELL = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("rutaTemplateExportExcell");

	
	public static String TEMPLATE_XLS_USUARIOS_PERFILES = "usuariosPerfiles.xls";
	public static String TEMPLATE_XLS_REPORTE_DINAMICO = "reporteDinamico.xls";

	//MESSAGES DE FACES
	public static String OPERACION_EXITOSA_CREAR_USUARIO = "operacionExitosaCrearUsuario";
	public static String OPERACION_EXITOSA_ACTUALIZAR_USUARIO = "operacionExitosaActualizarUsuario";
	public static String OPERACION_EXITOSA_RELACIONAR_USUARIOS_PERFILES= "operacionExitosaRelacionarUsuariosPerfil";
	public static String OPERACION_EXITOSA_ANEXAR_ARCHIVOS = "operacionExitosaAnexarArchivos";
	public static String OPERACION_EXITOSA_ELIMINAR_USUARIO= "operacionExitosaEliminarUsuario";
	public static String OPERACION_EXITOSA_CREAR_REGISTRO = "operacionExitosaCrearRegistro";
	public static String OPERACION_EXITOSA_ACTUALIZAR_REGISTRO = "operacionExitosaActualizarRegistro";
	public static String OPERACION_EXITOSA_GENERAR_REPORTE = "operacionExitosaGenerarReporte";
	public static String OPERACION_EXITOSA_ELIMINAR_REGISTRO  = "operacionExitosaEliminarRegistro";
	public static String OPERACION_EXITOSA_ENVIONOTIFICACION = "operacionExitosaEnvioNotificacion";
	
	public static String ERROR_OPERACION = "errorOperacion";	
	public static String ERROR_DOCUMENTO_EXISTE = "errorDocumentoExiste";
	public static String ERROR_NOMBRE_USUARIO_EXITE = "errorUserNameExiste";
	public static String ERROR_ARCHIVO_REQUERIDO = "errorArchivoRequerido";
	public static String ERROR_SELECCIONE_DEPENDENCIA = "errorSeleccioneDependencia";	
	public static String ERROR_SELECCIONE_USUARIOS_RELACIONAR = "errorSeleccioneUsuariosRelacionar";	
	public static String ERROR_ELIMINAR_USUARIO_PERFILES = "errorEliminarUsuarioPerfiles";
	public static String ERROR_SELECCIONE_SECCIONAL_USUARIO = "errorSeleccioneUsuarioSeccional";
	public static String ERROR_ELIMINAR_DEPENDECIA_USUARIOS = "errorEliminarDependenciaUsuario";
	public static String ERROR_SECCIONAL_SELECCIONADO_EXITE = "errorSeccionalSeleccionadoExiste";
	public static String ERROR_BAD_CREDENTIALS = "errorBadCredentialsException";
	public static String ERROR_MAX_SESSIONS_BY_USERS= "errorMaxSessionsByUser";
	public static String ERROR_MAX_SYSTEM_SESSIONS = "errorMaxSystemSessions";
	public static String ERROR_EXPORTAR_REPORTE = "errorExportarReporte";	
	public static String ERROR_ELIMINAR_GRUPO_USUARIO = "errorEliminarGrupoUsuario";
	public static String ERROR_ELIMINAR_PERFIL_USUARIO = "errorEliminarPerfilUsuario";
	public static String ERROR_ELIMINAR_PERFIL_RECURSOS = "errorEliminarPerfilRecurso";
	public static String ERROR_SELECCIONE_TRAMITE = "errorSeleccioneTramite";
	public static String ERROR_SELECCIONE_TIPOPERSONA = "errorSeleccioneTipoPersona";
	
	public static String OPERACION_EXITOSA_GUARDAR_GENERAL = "operacionExitosaGuardarGeneral";
	public static String OPERACION_EXITOSA_ELIMINAR_GENERAL = "operacionExitosaEliminarGeneral";
	
	public static String ERROR_CLAVE_ACTUAL = "errorClaveActual";
	public static String ERROR_VERIFICAR_CLAVE = "errorVerificarClave";
	public static String OPERACION_EXITOSA_CLAVE = "operacionClave";
	
	public static String SIN_OTROS_PROCESOS_ENTRADA = "corresSinProcesos";	

	
	public static String ERROR_OPERACIONES_ALERTAS = "administrarAlertasMsgOperacionFallida";
	public static String EXITO_OPERACIONES_ALERTAS = "administrarAlertasMsgOperacionExitosa";
	public static String INFO_OPERACION_FORMATOR = "consultaFormatosConCorte";
	public static String NO_ROWS_OPERACIONES_ALERTAS = "consularAlertasNoRowsFilters";
	public static String CAMPOS_REQUERIDOS = "camposRequeridos";
	public static String CARGUE_ASOCIADO = "cargueAsociado";
	
	public static String CUENTA_PUC_EXISTE ="cuentaPucExiste";
	public static String NIT_SOCIEDAD_EXISTE = "nitYaExiste";
	public static String INFORME_ENTIDAD_EXISTE="informeEntidadExiste";
	public static String FECHA_CONTROL_ANIO_DIFERENTE="msjVencimiento";
	public static String FECHA_CONTROL_EXISTE="msdDuplicado";
	public static String FECHA_CONTROL_PERIODO_YA_ACTIVO="msdPeriodoActivo";
	public static String AUDITOR_YA_EXISTE="auditorYaExiste";
	public static String AREA_YA_EXISTE="areaYaExiste";
	public static String ESTADO_AUDITORIA_YA_EXISTE="estadoYaExiste";
	public static String TIPO_AUDITORIA_YA_EXISTE = "tipoAuditoriaYaExiste";
	public static String TIPO_PROGRAMACION_YA_EXISTE = "tipoProgramacionYaExiste";
}
