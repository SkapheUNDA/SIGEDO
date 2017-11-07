package la.netco.registro.uilayer.beans;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.FileDataSource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import la.netco.commons.services.FileSystemRepositoryService;
import la.netco.commons.utils.JasperReports;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.ArchivoRegistro;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Devoluciones;
import la.netco.persistencia.dto.commons.Documentos;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.ExpedienteEstado;
import la.netco.persistencia.dto.commons.Registro;
import la.netco.core.businesslogic.services.impl.SpringApplicationContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@ViewScoped
public class DevolucionRegistroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Registro registroSeleccionado;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_FILE_SYSTEM_REPO_SERVICE)
	transient private FileSystemRepositoryService repoService;

	private Integer idRegSeleccionado;

	private List<SelectItem> devolucionesItems;
	private Integer idDevolucionSeleccionada;

	private String observacion;

	private Map<Integer, String> idsRegistrosSeleccionadas = new HashMap<Integer, String>();

	private List<Registro> registrosSeleccionados = null;

	@SuppressWarnings("unchecked")
	public void cargarRegistro() {
		try {

			FacesContext facesContext = FacesContext.getCurrentInstance();
			if (idRegSeleccionado == null) {
				facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");
				if (idRegSeleccionado != null)
					this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);
			}

			if ((registroSeleccionado == null || registroSeleccionado.getRegId() == null) && (idRegSeleccionado != null)) {
				registroSeleccionado = (Registro) serviceDao.getGenericCommonDao().read(Registro.class, idRegSeleccionado);
			}

			if (idRegSeleccionado == null && registrosSeleccionados == null) {
				idsRegistrosSeleccionadas = (Map<Integer, String>) FacesUtils.flashScope(FacesContext.getCurrentInstance()).get("idsRegistrosSeleccionadas");
				if (idsRegistrosSeleccionadas != null && !idsRegistrosSeleccionadas.isEmpty()) {
					registrosSeleccionados = new ArrayList<Registro>();

					for (Entry<Integer, String> registrosId : idsRegistrosSeleccionadas.entrySet()) {
						System.out.println(" registrosId.getValue() " + registrosId.getValue());
						if (registrosId.getValue().equals("true")) {
							registrosSeleccionados.add((Registro) serviceDao.getGenericCommonDao().read(Registro.class, registrosId.getKey()));
						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod =
	// CommonsModKeys.MNUEPAE)
	public String devolverRechazoExpediente() {
		try {
			if (registroSeleccionado != null) {
				String justifiFinal;

				Expediente expedienteDevol = registroSeleccionado.getExpediente();
				Devoluciones devolucionConsul = (Devoluciones) serviceDao.getGenericCommonDao().read(Devoluciones.class, idDevolucionSeleccionada);

				expedienteDevol.setExpDev(idDevolucionSeleccionada);
				expedienteDevol.setEstadoGeneral((ExpedienteEstado) serviceDao.getGenericCommonDao().read(ExpedienteEstado.class, ConstantsKeysFire.STADEVEXP));
				justifiFinal = devolucionConsul.getDevNom() + " || " + observacion;

				serviceDao.getGenericCommonDao().update(Expediente.class, expedienteDevol);

				if (devolucionConsul.getDevLdev() == false) {
					serviceDao.getSeguimientosDao().addSeguimientoExp(expedienteDevol, ConstantsKeysFire.RECHAZO, justifiFinal);
				} else {
					serviceDao.getSeguimientosDao().addSeguimientoExp(expedienteDevol, ConstantsKeysFire.DEVOLUCION, justifiFinal);
				}

				imprimirDevolucion();

			} else if (registrosSeleccionados != null && !registrosSeleccionados.isEmpty()) {

				for (Registro registro : registrosSeleccionados) {

					if (registro.getRegLblo() == null || registro.getRegLblo().equals(Boolean.FALSE)) {
						String justifiFinal;

						Expediente expedienteDevol = registro.getExpediente();
						Devoluciones devolucionConsul = (Devoluciones) serviceDao.getGenericCommonDao().read(Devoluciones.class, idDevolucionSeleccionada);

						expedienteDevol.setExpDev(idDevolucionSeleccionada);
						expedienteDevol.setEstadoGeneral((ExpedienteEstado) serviceDao.getGenericCommonDao().read(ExpedienteEstado.class, ConstantsKeysFire.STADEVEXP));
						justifiFinal = devolucionConsul.getDevNom() + " || " + observacion;

						serviceDao.getGenericCommonDao().update(Expediente.class, expedienteDevol);

						if (devolucionConsul.getDevLdev() == false) {
							serviceDao.getSeguimientosDao().addSeguimientoExp(expedienteDevol, ConstantsKeysFire.RECHAZO, justifiFinal);
						} else {
							serviceDao.getSeguimientosDao().addSeguimientoExp(expedienteDevol, ConstantsKeysFire.DEVOLUCION, justifiFinal);
						}
					}
				}
				imprimirDevoluciones();

			}

			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO, FacesMessage.SEVERITY_INFO);

			return "transaccionExitosa";
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION, FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
			return "";
		}

	}

	// @SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod =
	// CommonsModKeys.MNUEPAE)
	/** 
	 * Ediaz
	 * metodo encargado de imprimir (generar pdf) de la devolucin.
	 * */
	public void imprimirDevolucion() {
		try {
			System.out.println("<<<< Ingreso a Imprimir Devolucion EJD>>>");
			//ServiceDao service = (ServiceDao) SpringApplicationContext.getBean("serviceDao");
			SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es_ES"));
			String enlaceArchivo;
			Map<String, Object> parametros = new HashMap<String, Object>();
			Date fechaParam = new Date();

			Entrada objEntrada = registroSeleccionado.getExpediente().getExpedientecorresponds().iterator().next().getEntrada();
			
			//incluir parametrizacin imagenes
			//OpcionesGenerales opcionesGenerales = (OpcionesGenerales) service.getGenericCommonDao().loadAll(OpcionesGenerales.class).get(0);
			
			//String imagenDocUno = opcionesGenerales.getRutaImagenReporte();
			//String imagenDocDos = opcionesGenerales.getRutaImagenReporte();
			String imagenDocUno = FacesUtils.getValorPropiedad("ruta.imgUnoDev");
			String imagenDocDos = FacesUtils.getValorPropiedad("ruta.imgDosDev");
			System.out.println("<<<Rutas de las imagenes: >>>" + imagenDocUno +  "Segunda Imagen " + imagenDocDos);
			String fechaActual = formateadorFecha.format(fechaParam);

			/*Incluir parametros para el reporte de devolucin*/
			parametros.put("PLOGO_ENTIDAD", imagenDocUno);
			parametros.put("SLOGO_ENTIDAD", imagenDocDos);
			parametros.put("ASUNTO_DEV", this.observacion);
			parametros.put("FECHA_ACTUAL", fechaActual);
			parametros.put("NO_RADICACION", objEntrada.getEntNen());
			parametros.put("NOM_SOLICITANTE", objEntrada.getEntNom() + " " + objEntrada.getEntPap() + " " + objEntrada.getEntSap());
			parametros.put("EMAIL_SOLICITANTE", objEntrada.getEntCel());
			parametros.put("NOM_FUNCIONARIO", UserDetailsUtils.usuarioLogged().getUsrNom() + " " + UserDetailsUtils.usuarioLogged().getUsrPap() + " "
					+ UserDetailsUtils.usuarioLogged().getUsrSap());
			
			/* arhivo pdf a generar*/
			//ediaz
			//File archivoPDF = new File(ConstantsKeysFire.RUTA_ABSOLUTA_TEMP_DOWNLOAD_FILE + "/" + System.currentTimeMillis() + "Devolucion.pdf");
			
			String rutaAbsoluta = ConstantsKeysFire.RUTA_ABSOLUTA_DOWNLOAD_FILE;
			System.out.println("<<< Ruta donde se deja la devolucion >>> EJD " + ConstantsKeysFire.RUTA_ABSOLUTA_DOWNLOAD_FILE);
			
			// * 1 paso 
			//  Dejar el pdf en el servido
			
			File archivoPDF = new File(rutaAbsoluta + objEntrada.getEntNen().trim()+"-Devolucion.pdf");
			System.out.println("<<< Nombre del archivo >>> EJD " + archivoPDF.getName());
			//ediaz
			System.out.println("<<<  RUta completa jasper >>> EJD " + ConstantsKeysFire.REPORTE_DEVOLUCION_JASPER);
			//JasperReports.generarReportePDF(ConstantsKeysFire.RUTA_ARCHIVOS_REPORTES_JASPER + ConstantsKeysFire.REPORTE_DEVOLUCION, archivoPDF, parametros);
			InputStream jasperCargado = null;
			jasperCargado = this.getClass().getClassLoader().getResourceAsStream(ConstantsKeysFire.REPORTE_DEVOLUCION_JASPER);
			//JasperReports.generarReportePDF(jasperCargado, archivoPDF, parametros);
			JasperReports.generarReporteDatosPDF(jasperCargado, archivoPDF, parametros);
			
			
			//ediaz
			//enlaceArchivo = ConstantsKeysFire.RUTA_PUBLICA_TEMP_DOWNLOAD_FILE + "/" + archivoPDF.getName();
			
			

			FileDataSource archivoUp = new FileDataSource(archivoPDF);

			ArchivoRegistro archivoRegistro = new ArchivoRegistro();
			archivoRegistro.setContentType(ArchivoRegistro.CONTENT_PDF);
			archivoRegistro.setDocumentos((Documentos) serviceDao.getGenericCommonDao().read(Documentos.class, Documentos.DOC_DEVOLUCION));
			archivoRegistro.setRegistro(registroSeleccionado);
			//ediaz
			//archivoRegistro.setNombre("Devolucion-" + registroSeleccionado.getRegCod() + " " + System.currentTimeMillis() + ".pdf");
			archivoRegistro.setNombre(objEntrada.getEntNen().trim()+"-Devolucion.pdf");
			archivoRegistro.setSize(archivoPDF.length());
			archivoRegistro.setFechaRegistro(new Date(System.currentTimeMillis()));
			repoService.guardarArchivo(archivoUp.getInputStream(), archivoRegistro);
			
			//2 paso dejarlo en el contexto web
			String rutaArchivo2 = ConstantsKeysFire.RUTA_ABSOLUTA_TEMP_DOWNLOAD_FILE;
			File archivoPDF2 = new File( rutaArchivo2 + objEntrada.getEntNen().trim()+"-Devolucion.pdf");
			InputStream jasperCargado2 = null;
			jasperCargado2 = this.getClass().getClassLoader().getResourceAsStream(ConstantsKeysFire.REPORTE_DEVOLUCION_JASPER);
			//JasperReports.generarReportePDF(jasperCargado, archivoPDF, parametros);
			JasperReports.generarReporteDatosPDF(jasperCargado2, archivoPDF2, parametros);
			
			
			enlaceArchivo = ConstantsKeysFire.RUTA_RELATIVA_DEVOLUCION+archivoPDF.getName();
			System.out.println("<<< VALOR ENLACE ARCHIVO >>> "+ enlaceArchivo);
			

			FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("enlaceArchivo", enlaceArchivo);
			FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("modalDocumentoVisible", true);

		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION, FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}

	}

	// @SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod =
	// CommonsModKeys.MNUEPAE)
	public void imprimirDevoluciones() {
		try {

			SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es_ES"));
			String enlaceArchivo;
			Map<String, Object> parametros = new HashMap<String, Object>();
			List<String> nombreArchivosList = new ArrayList<String>();
			List<String> enlacesArchivosList = new ArrayList<String>();
			Date fechaParam = new Date();

			String imagenDocUno = FacesUtils.getValorPropiedad("ruta.imgUnoDev");
			String imagenDocDos = FacesUtils.getValorPropiedad("ruta.imgDosDev");
			String fechaActual = formateadorFecha.format(fechaParam);

			for (Registro registroCorr : registrosSeleccionados) {
				if (registroCorr.getRegLblo() == null || registroCorr.getRegLblo().equals(Boolean.FALSE)) {
					Entrada objEntrada = registroCorr.getExpediente().getExpedientecorresponds().iterator().next().getEntrada();
					parametros.put("PLOGO_ENTIDAD", imagenDocUno);
					parametros.put("SLOGO_ENTIDAD", imagenDocDos);
					parametros.put("ASUNTO_DEV", this.observacion);
					parametros.put("FECHA_ACTUAL", fechaActual);
					parametros.put("NO_RADICACION", objEntrada.getEntNen());
					parametros.put("NOM_SOLICITANTE", objEntrada.getEntNom() + " " + objEntrada.getEntPap() + " " + objEntrada.getEntSap());
					parametros.put("EMAIL_SOLICITANTE", objEntrada.getEntCel());
					parametros.put("NOM_FUNCIONARIO", UserDetailsUtils.usuarioLogged().getUsrNom() + " " + UserDetailsUtils.usuarioLogged().getUsrPap() + " "
							+ UserDetailsUtils.usuarioLogged().getUsrSap());
					File archivoPDF = new File(ConstantsKeysFire.RUTA_ABSOLUTA_TEMP_DOWNLOAD_FILE + "/" + System.currentTimeMillis() + "Devolucion.pdf");
					JasperReports.generarReportePDF(ConstantsKeysFire.RUTA_ARCHIVOS_REPORTES_JASPER + ConstantsKeysFire.REPORTE_DEVOLUCION, archivoPDF, parametros);
					enlaceArchivo = ConstantsKeysFire.RUTA_PUBLICA_TEMP_DOWNLOAD_FILE + "/" + archivoPDF.getName();

					FileDataSource archivoUp = new FileDataSource(archivoPDF);

					ArchivoRegistro archivoRegistro = new ArchivoRegistro();
					archivoRegistro.setContentType(ArchivoRegistro.CONTENT_PDF);
					archivoRegistro.setDocumentos((Documentos) serviceDao.getGenericCommonDao().read(Documentos.class, Documentos.DOC_DEVOLUCION));
					archivoRegistro.setRegistro(registroCorr);
					archivoRegistro.setNombre("Devolucion-" + registroCorr.getRegCod() + " " + System.currentTimeMillis() + ".pdf");
					archivoRegistro.setSize(archivoPDF.length());
					archivoRegistro.setFechaRegistro(new Date(System.currentTimeMillis()));
					repoService.guardarArchivo(archivoUp.getInputStream(), archivoRegistro);

					nombreArchivosList.add(archivoRegistro.getNombre());
					enlacesArchivosList.add(enlaceArchivo);
				}
			}
			FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("modalDocumentosVisible", true);
			FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("nombreArchivosList", nombreArchivosList);
			FacesUtils.flashScope(FacesContext.getCurrentInstance()).put("enlacesArchivosList", enlacesArchivosList);

		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION, FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public StreamedContent descargar(String nombreArchivo) {
		try {
			HashMap<Integer, String> paramsBusq = new HashMap<Integer, String>();
			paramsBusq.put(0, nombreArchivo);

			List<ArchivoRegistro> listaAuxiArchivoRegistro = (List<ArchivoRegistro>) serviceDao.getGenericCommonDao().executeFind(ArchivoRegistro.class, paramsBusq,
					ArchivoRegistro.NAMED_QUERY_GET_BY_NOMBRE);

			ArchivoRegistro archivo = (ArchivoRegistro) serviceDao.getGenericCommonDao().read(ArchivoRegistro.class, listaAuxiArchivoRegistro.get(0).getId());
			InputStream stream = repoService.obtenerInputStream(archivo);

			StreamedContent file = new DefaultStreamedContent(stream, archivo.getContentType(), archivo.getNombre());

			// File fileCarga = new File(
			// Constants.RUTA_ABSOLUTA_DOS_TEMP_DOWNLOAD_FILE);
			//
			// if (!fileCarga.exists()) {
			// fileCarga.mkdirs();
			// }
			//
			// String rutaFile = fileCarga.getPath() + File.separator
			// + archivo.getNombre();
			// fileCarga = new File(rutaFile);
			// if (!fileCarga.exists()) {
			// MessageDigest digest = MessageDigest.getInstance("SHA-1");
			// OutputStream salidaFile = new DigestOutputStream(
			// new FileOutputStream(rutaFile), digest);
			// try {
			// IOUtils.copyLarge(stream, salidaFile);
			// } finally {
			// salidaFile.close();
			// }
			// }
			//
			// HttpServletRequest requestPag = (HttpServletRequest) FacesContext
			// .getCurrentInstance().getExternalContext().getRequest();
			//
			// String rutaPort = getProtocolHttp(requestPag)
			// + requestPag.getServerName();
			//
			// String rutaFinal = rutaPort
			// + Constants.RUTA_PUBLICA_TEMP_DOWNLOAD_FILE + "/"
			// + archivo.getNombre();

			return file;
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION, FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
		return null;
	}

	public static String getProtocolHttp(HttpServletRequest request) {
		String protocol = "http://";
		if (request.isSecure())
			protocol = "https://";
		return protocol;
	}

	/**
	 * Carga items de devoluciones
	 */
	@SuppressWarnings("unchecked")
	public void cargaDevolucionesItems() {
		if (devolucionesItems == null) {
			List<Devoluciones> devolucionesAuxi = (List<Devoluciones>) serviceDao.getGenericCommonDao().executeFind(Devoluciones.NAMED_QUERY_ALL_DEVOLUCION);

			devolucionesItems = new ArrayList<SelectItem>();
			for (Devoluciones devoluciones : devolucionesAuxi) {
				devolucionesItems.add(new SelectItem(devoluciones.getDevId(), devoluciones.getDevNom()));
			}

		}
	}

	public Registro getRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setRegistroSeleccionado(Registro registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public List<SelectItem> getDevolucionesItems() {
		cargaDevolucionesItems();
		return devolucionesItems;
	}

	public Integer getIdDevolucionSeleccionada() {
		return idDevolucionSeleccionada;
	}

	public void setDevolucionesItems(List<SelectItem> devolucionesItems) {
		this.devolucionesItems = devolucionesItems;
	}

	public void setIdDevolucionSeleccionada(Integer idDevolucionSeleccionada) {
		this.idDevolucionSeleccionada = idDevolucionSeleccionada;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Map<Integer, String> getIdsRegistrosSeleccionadas() {
		return idsRegistrosSeleccionadas;
	}

	public void setIdsRegistrosSeleccionadas(Map<Integer, String> idsRegistrosSeleccionadas) {
		this.idsRegistrosSeleccionadas = idsRegistrosSeleccionadas;
	}

	public List<Registro> getRegistrosSeleccionados() {
		return registrosSeleccionados;
	}

	public void setRegistrosSeleccionados(List<Registro> registrosSeleccionados) {
		this.registrosSeleccionados = registrosSeleccionados;
	}

	/**
	 * @return the repoService
	 */
	public FileSystemRepositoryService getRepoService() {
		return repoService;
	}

	/**
	 * @param repoService
	 *            the repoService to set
	 */
	public void setRepoService(FileSystemRepositoryService repoService) {
		this.repoService = repoService;
	}

}
