package la.netco.correspondencia.uilayer.beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import la.netco.commons.utils.JasperReports;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.correspondencia.dto.custom.AnexoDescargable;
import la.netco.correspondencia.dto.custom.Consecutivo;
import la.netco.persistencia.dto.commons.Anexo;
import la.netco.persistencia.dto.commons.Anexoentrada;
import la.netco.persistencia.dto.commons.AnexoentradaId;
import la.netco.persistencia.dto.commons.ArchivoEntrada;
import la.netco.persistencia.dto.commons.ArchivoRegistro;
import la.netco.persistencia.dto.commons.Calidadrepresentante;
import la.netco.persistencia.dto.commons.Claseregistro;
import la.netco.persistencia.dto.commons.Clasificacion;
import la.netco.persistencia.dto.commons.Configuracion;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Depend;
import la.netco.persistencia.dto.commons.Documentos;
import la.netco.persistencia.dto.commons.DocumentosRegistro;
import la.netco.persistencia.dto.commons.Enlace;
import la.netco.persistencia.dto.commons.EnlaceId;
import la.netco.persistencia.dto.commons.Entidad;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Estado;
import la.netco.persistencia.dto.commons.Lugar;
import la.netco.persistencia.dto.commons.Medioscorrespondencia;
import la.netco.persistencia.dto.commons.Paises;
import la.netco.persistencia.dto.commons.Persona;
import la.netco.persistencia.dto.commons.Registro;
import la.netco.persistencia.dto.commons.Salida;
import la.netco.persistencia.dto.commons.Seguimientoentrada;
import la.netco.persistencia.dto.commons.Tiposdocumento;
import la.netco.persistencia.dto.commons.Tiposevento;
import la.netco.persistencia.dto.commons.Tipospersona;
import la.netco.persistencia.dto.commons.Transicion;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "entradasBean")
@ViewScoped
public class EntradasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(EntradasBean.class);

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	// @ManagedProperty(value =
	// BeansSpringConst.BEAN_ID_FILE_SYSTEM_REPO_SERVICE)
	// transient private FileSystemRepositoryService repoService;

	private List<SelectItem> clasificacionItems;
	private Short idClasificacionSeleccionada;

	private List<SelectItem> dependenciasItems;
	private Short idDependenciaSeleccionada;

	private List<SelectItem> mediosItems;
	private Short idMedioSeleccionada;

	private List<SelectItem> tiposPersonaItems;
	private Short idTiposPersonaSeleccionado;

	private List<SelectItem> tiposDocumentosItems;
	private Integer idTipoDocumentoSol;

	private List<SelectItem> paisesItems;
	private Short idPaisSeleccionado;

	private List<SelectItem> lugaresItems;
	private Short idLugarSeleccionado;

	private List<SelectItem> estadosItems;
	private short idEstadoSeleccionado;

	private Persona personaNaturalSeleccionada;

	private Entidad personaJuridicaSeleccionada;

	private Entrada nuevoRegistro;

	private Map<String, Propiedad> configuracionFormulario;

	private List<AnexoDescargable> descargables = new ArrayList<AnexoDescargable>();

	private List<Anexo> anexosClasificacion = new ArrayList<Anexo>();

	private List<SelectItem> calidadRepresentanteItems;
	private Short idCalidadRepreSeleccionado;

	private List<Salida> salidasEntradas = new ArrayList<Salida>();
	private List<Salida> salidasSeleccionadas = new ArrayList<Salida>();
	private boolean respuestaSalida;
	private Salida salidaSeleccionada;
	private Entrada entradaSeleccionada;
	private Integer idRegSeleccionado;
	private List<Seguimientoentrada> seguimientoEntrada = new ArrayList<Seguimientoentrada>();
	private boolean generarEtiqueta;
	// control Archivos que vienen
	private HashMap<String, Object> files = new HashMap<String, Object>();
	private List<String> dataFiles;
	private short TAG_DEPENDENCIA_REGISTRO = 19;
	private short TAG_REGISTRO_OBRA_LITERARIA_INEDITA = 91;
	private short TAG_NATURAL = 1;
	private short TAG_EN_NOMBRE_PROPIO = 8;
	private List<SelectItem> usuariosItems;
	private Short idUsuarioSeleccionado;
	private String observacion;
	private Short ESTADO_INICIAL_REPARTIR = 237;
	private Short ID_EVENTO_REPARTIR = 150;
	private static final Short TAG_MEDIO_CORRESPONDENCIA = 9;
	private static final Integer TAG_TIPO_DOCUMENTO = 7;
	private static final Short TAG_PAIS_PREDETERMINADO = 1;
	private Boolean flagAdjArchivos = false;
	private Boolean flagAdjArchivosCorrespondencia = false;

	private enum PREFS {
		CONS_DOC_CARPETA_ADJ
	}

	HashMap<PREFS, String> preferencias;
	private static String QUERY_PREFS;
	static {
		QUERY_PREFS = "SELECT llave, valor from Preferencias WHERE llave in (";
		for (PREFS pref : PREFS.values()) {
			QUERY_PREFS += "'" + pref + "', ";
		}
		QUERY_PREFS = QUERY_PREFS.substring(0, QUERY_PREFS.length() - 2);
		QUERY_PREFS += ") AND ESTADO = 1";
	}
	private Integer id_Registro;
	private Registro registroSeleccionado;

	private List<SelectItem> itemsDocumentos;
	private short idTipoDocumentoSeleccionado;
	private Integer docid;
	private HashMap<String, DocumentosRegistro> filesRegistro = new HashMap<String, DocumentosRegistro>();
	private HashMap<String, DocumentosRegistro> filesRegistroInedita = new HashMap<String, DocumentosRegistro>();
	private List<ArchivoRegistro> dataFilesArchivoRegistro;
	private List<ArchivoEntrada> dataFilesArchivoEntrada;
	private String activeTab;
	private boolean muestrese = true;
	private boolean buttonEtiqueta = false;
	private boolean modalVisible;
	private String enlaceEtiqueta;
	private Entrada entradaAux;

	
	

	public EntradasBean() {
		nuevoRegistro = new Entrada();
		
		if (personaNaturalSeleccionada == null)
			personaNaturalSeleccionada = new Persona();

		if (personaJuridicaSeleccionada == null)
			personaJuridicaSeleccionada = new Entidad();

	}

	@PostConstruct
	public void init() {

		List<Map<String, Object>> lectura = null;

		try {
			lectura = serviceDao.getSpringGenericDao()
					.executeQuery(QUERY_PREFS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		preferencias = new HashMap<PREFS, String>();
		EntradasBean.PREFS llave;
		String valor;
		if (lectura != null) {
			for (Map<String, Object> elem : lectura) {
				llave = PREFS.valueOf(elem.get("llave").toString());
				valor = elem.get("valor").toString();
				preferencias.put(llave, valor);
			}
		}
		/* Cargar configuraci�n predeterminada al crear una entrada */
		seleccionarDependeciaPredeterminada();
		seleccionarClasificacionPredeterminada(); 
		/*Validar Tab a mostrar dependiendo de la clasificacion */
		cargarTabAdjuntosRegistro();
		seleccionarTipoPersonaPredeterminada();
		seleccionarCalidadRepresentantePredeterminada();
		seleccionarMedioCorrespondenciaPredeterminado();
		seleccionarTipoDocumentoPredeterminado();
		seleccionarPaisPredeterminada();
		// borrar memoria
		if(idClasificacionSeleccionada ==91){
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mapaImagenes", null);
			FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().put("documentInedita", null);
		}
		
		
	}
	
	/**
	 * Cargar Tab de Archivos Adjuntos de registro Dependencia de Registro
	 * (Clasificaciones de la 91 a 99)
	 * de lo contrario aplican para las demas Dependecias
	 */
	public void cargarTabAdjuntosRegistro() {
		switch (idClasificacionSeleccionada) {
		case 91:
			flagAdjArchivos = true;
			flagAdjArchivosCorrespondencia = false;
			activeTab = "Tab1";
			break;
		case 92:
			flagAdjArchivos = true;
			flagAdjArchivosCorrespondencia = false;
			activeTab = "Tab1";
			break;
		case 93:
			flagAdjArchivos = true;
			flagAdjArchivosCorrespondencia = false;
			activeTab = "Tab1";
			break;
		case 94:
			flagAdjArchivos = true;
			flagAdjArchivosCorrespondencia = false;
			activeTab = "Tab1";
			break;
		case 95:
			flagAdjArchivos = true;
			flagAdjArchivosCorrespondencia = false;
			activeTab = "Tab1";
			break;
		case 96:
			flagAdjArchivos = true;
			flagAdjArchivosCorrespondencia = false;
			activeTab = "Tab1";
			break;
		case 97:
			flagAdjArchivos = true;
			flagAdjArchivosCorrespondencia = false;
			activeTab = "Tab1";
			break;
		case 98:
			flagAdjArchivos = true;
			flagAdjArchivosCorrespondencia = false;
			activeTab = "Tab1";
			break;
		case 99:
			flagAdjArchivos = true;
			flagAdjArchivosCorrespondencia = false;
			activeTab = "Tab1";
			break;
		default:
			flagAdjArchivos = false;
			flagAdjArchivosCorrespondencia = true;
			activeTab = "Tab4";
			break;
		}
		
		
		
	}

	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUE)
	@SuppressWarnings("unchecked")
	public String agregar() {
		String page = null;
		try {
			Integer totalFolios = 0;
			if (anexosClasificacion != null && !anexosClasificacion.isEmpty()) {
				for (Anexo anexo : anexosClasificacion) {
					totalFolios = totalFolios.intValue()
							+ anexo.getNumFolios().intValue();
				}
			}

			Depend dependencia = (Depend) serviceDao.getGenericCommonDao()
					.read(Depend.class, idDependenciaSeleccionada);
			Clasificacion clasificacion = (Clasificacion) serviceDao
					.getGenericCommonDao().read(Clasificacion.class,
							idClasificacionSeleccionada);
			Medioscorrespondencia medio = (Medioscorrespondencia) serviceDao
					.getGenericCommonDao().read(Medioscorrespondencia.class,
							idMedioSeleccionada);
			Tipospersona tipoPersona = (Tipospersona) serviceDao
					.getGenericCommonDao().read(Tipospersona.class,
							idTiposPersonaSeleccionado);
			// Usuario usuario = UserDetailsUtils.usuarioLogged();
			// Usuario seleccionado
			Usuario usuario = (Usuario) serviceDao.getGenericCommonDao().read(
					Usuario.class, idUsuarioSeleccionado);
			Calidadrepresentante calidadRepresentante = (Calidadrepresentante) serviceDao
					.getGenericCommonDao().read(Calidadrepresentante.class,
							idCalidadRepreSeleccionado);

			Short estadoId = null;

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, idClasificacionSeleccionada);
			params.put(1, Tiposevento.ETAPA_INICIAL);

			List<Transicion> transicions = (List<Transicion>) serviceDao
					.getEntradaDao()
					.executeFind(Transicion.class, params,
							Transicion.NAMED_QUERY_BY_CLASIFICACION_TIPO_EVENTO);
			Transicion transicion = null;
			if (transicions != null && !transicions.isEmpty()) {
				transicion = transicions.get(0);
				estadoId = transicion.getId().getTrsEstfin();
			}

			//if (estadoId == null)
				estadoId = ConstantsKeysFire.ESTADO_DEFECTO_ENTRADA_CAMBIO_AUTOMATICO;

			Estado estadoFinal = (Estado) serviceDao.getGenericCommonDao()
					.read(Estado.class, estadoId);

			boolean requiereRespuesta = clasificacion.isClaLrta();
			Short tiempoRespuesta = clasificacion.getClaTie();

			nuevoRegistro.setEntFre(new Date(System.currentTimeMillis()));
			nuevoRegistro.setEntLrsa(respuestaSalida);
			nuevoRegistro.setMedio(medio);
			nuevoRegistro.setDependencia(dependencia);
			nuevoRegistro.setClasificacion(clasificacion);
			nuevoRegistro.setCalidadRepresentante(calidadRepresentante);

			nuevoRegistro.setTipoPersona(tipoPersona);
			if (idTipoDocumentoSol != null) {
				Tiposdocumento tipoDocumento = (Tiposdocumento) serviceDao
						.getGenericCommonDao().read(Tiposdocumento.class,
								idTipoDocumentoSol);
				nuevoRegistro.setTiposdocumento(tipoDocumento);
			}
			if (idPaisSeleccionado != null) {
				Paises pais = (Paises) serviceDao.getGenericCommonDao().read(
						Paises.class, idPaisSeleccionado);
				nuevoRegistro.setPais(pais);
			}
			if (idLugarSeleccionado != null) {
				Lugar lugar = (Lugar) serviceDao.getGenericCommonDao().read(
						Lugar.class, idLugarSeleccionado);
				nuevoRegistro.setLugar(lugar);
			}

			nuevoRegistro.setEntCdep(dependencia.getDepCod());
			nuevoRegistro.setEntCano(Calendar.getInstance().get(Calendar.YEAR));

			nuevoRegistro.setEntLnot(false);
			nuevoRegistro.setUsuario(usuario);
			nuevoRegistro.setEstado(estadoFinal);
			nuevoRegistro.setEntLrta(requiereRespuesta);
			nuevoRegistro.setEntTrt(tiempoRespuesta);
			nuevoRegistro.setEntFol(totalFolios.shortValue());
			Date fechaCorrespondencia = serviceDao.getEntradaDao()
					.obtenerFechaRadicacion();
			nuevoRegistro.setEntFen(fechaCorrespondencia);
			nuevoRegistro.setEntFdr(serviceDao.getEntradaDao()
					.obtenerFechaRespuesta(nuevoRegistro));

			Consecutivo numCorrespondencia = serviceDao.getEntradaDao()
					.obtenerConsecutivo(dependencia.getDepCod().trim(),
							Consecutivo.TIPO_ENTRADA);
			nuevoRegistro.setEntCnro(numCorrespondencia.getConsecutivo()-1);
			nuevoRegistro
					.setEntNen(numCorrespondencia.getConsecutivoCompleto());
			Integer idEntrada = (Integer) serviceDao.getEntradaDao().create(
					Entrada.class, nuevoRegistro);

			if (idEntrada != null) {
				if (anexosClasificacion != null
						&& !anexosClasificacion.isEmpty()) {
					for (Anexo anexo : anexosClasificacion) {
						if (anexo.getNumFolios() != null
								&& !anexo.getNumFolios().equals(
										new Integer(0).shortValue())) {
							Anexoentrada anexoEntrada = new Anexoentrada(
									new AnexoentradaId(idEntrada,
											anexo.getAnxId()),
									anexo.getNumFolios());
							serviceDao.getEntradaDao().create(
									Anexoentrada.class, anexoEntrada);
						}
					}
				}

				if (salidasSeleccionadas != null
						&& !salidasSeleccionadas.isEmpty()) {
					for (Salida salida : salidasSeleccionadas) {
						Enlace enlaceEntrada = new Enlace(new EnlaceId(
								Enlace.ORIGEN_ENTRADA, idEntrada,
								salida.getSalId()));
						serviceDao.getEntradaDao().create(Enlace.class,
								enlaceEntrada);
					}
				}

				// Control de archivos adjuntos
				/*Validacion para archivos solo de Entradas Correspondencia*/
				if (flagAdjArchivosCorrespondencia) {
					// Valida si crea o no la persona
					serviceDao.getExpedienteDao().validaPersona(
							nuevoRegistro); 
					String rutaentradas = FacesUtils
							.getValorPropiedad("archivos.rutaentradas");
					File raiz = new File(rutaentradas);
					for (Map.Entry<String, Object> file : files.entrySet()) {

						if (file.getValue() instanceof UploadedFile) {
							UploadedFile temp = (UploadedFile) file.getValue();
							System.out.println("--> Valor Archivos "
									+ file.getKey().toString() + "  "
									+ temp.getFileName());
							// Archivo Entrada
							ArchivoEntrada archivoEntrada = new ArchivoEntrada();
							archivoEntrada
									.setContentType(temp.getContentType());
							archivoEntrada.setFechaRegistro(new Date(System
									.currentTimeMillis()));
							archivoEntrada.setNombre(temp.getFileName());
							archivoEntrada.setSize(temp.getSize());
							archivoEntrada.setEntrada(nuevoRegistro);
							serviceDao.getEntradaDao().create(
									ArchivoEntrada.class, archivoEntrada);
							System.out.println("Creo arhivo Entrada");

							// Persistir Adjuntos
							String numEntrada = Integer.toString(idEntrada);
							SimpleDateFormat format = new SimpleDateFormat(
									"/yyyy/MM/dd/");
							File raiz2 = new File(raiz,
									format.format(new Date()));
							// Se corrige porque puede traer espacios
							numEntrada = numEntrada.trim();
							File subcarpeta = new File(raiz2, numEntrada);
							// cambio
							File folderRoot = subcarpeta;
							if (!folderRoot.exists()) {
								folderRoot.mkdirs();
							}

							File archivo = new File(
									folderRoot.getAbsolutePath()
											+ File.separator
											+ temp.getFileName());
							OutputStream output = new FileOutputStream(archivo);
							IOUtils.copy(temp.getInputstream(), output);
							output.close();
							log.info("Se guarda el adjunto en "
									+ folderRoot.getAbsolutePath());

						}
					}
				}
			}
			Entrada entradaReg = (Entrada) serviceDao.getEntradaDao().read(
					Entrada.class, idEntrada);

			Usuario usuarioLogged = UserDetailsUtils.usuarioLogged();

			serviceDao.getSeguimientosDao().addSeguimientoEntrada(entradaReg,
					transicion, Seguimientoentrada.SEGUIMIENTO_CREACION, "",
					usuarioLogged, usuario);
			try {
				if (idClasificacionSeleccionada >= Clasificacion.CAL_INI_REGISTRO
						&& idClasificacionSeleccionada <= Clasificacion.CAL_FIN_REGISTRO) {
					id_Registro = serviceDao.getExpedienteDao()
							.crearExpedienteEsp(entradaReg,
									idClasificacionIdClaseRegistro(entradaReg));
					log.info("Entrada #Rad " + entradaReg.getEntNen());
					log.info("Entrada #id_Registro " + id_Registro);
					/*documentos para registro*/
					if (flagAdjArchivos) {
						/* Leer Objeto del registro creado, atravez del ID- */
						if ((registroSeleccionado == null || registroSeleccionado
								.getRegId() == null) && (id_Registro != null)) {
							registroSeleccionado = (Registro) serviceDao
									.getGenericCommonDao().read(Registro.class,
											id_Registro);
						}
						/*Ruta Registro*/
						String rutaRegistro = FacesUtils
								.getValorPropiedad("archivos.rutaregistros");
						File raiz = new File(rutaRegistro);
						if(idClasificacionSeleccionada ==91 && idTipoDocumentoSeleccionado != 0 ){
							filesRegistroInedita =(HashMap<String, DocumentosRegistro>) FacesContext
							.getCurrentInstance().getExternalContext()
							.getSessionMap().get("mapaImagenes");
							filesRegistro.putAll(filesRegistroInedita);
						}
						
						/* Iterar sobre los archivos a subir */
						for (Map.Entry<String, DocumentosRegistro> file : filesRegistro.entrySet()) {
							log.info("Tiene archivos!!");
							DocumentosRegistro value = file.getValue();
							UploadedFile tempFile = (UploadedFile) value.getFiles();
							log.info("Valor Archivos Registro: " + "id Documento: " + value.getDocumentoId() + " Nombre Archivo: " + tempFile.getFileName());
						
								// Archivo Registro
								ArchivoRegistro archivoRegistro = new ArchivoRegistro();
								archivoRegistro
										.setRegistro(registroSeleccionado);
								archivoRegistro.setNombre(tempFile.getFileName());
								archivoRegistro.setContentType(tempFile
										.getContentType());
								archivoRegistro.setSize(tempFile.getSize());
								archivoRegistro.setFechaRegistro(new Date(
										System.currentTimeMillis()));
								archivoRegistro
										.setDocumentos((Documentos) serviceDao
												.getGenericCommonDao()
												.read(Documentos.class,
														value.getDocumentoId()));
								/*Guardar archivo en el Repositorio*/
								/**repoService.guardarArchivo(
										temp.getInputstream(), archivoRegistro);*/
								serviceDao.getEntradaDao().create(
										ArchivoRegistro.class, archivoRegistro);
								System.out.println("Creo arhivo archivoRegistro");

								// Persistir Adjuntos
								String numRegistro = Integer.toString(id_Registro);
								SimpleDateFormat format = new SimpleDateFormat(
										"/yyyy/MM/dd/");
								File raiz2 = new File(raiz,
										format.format(new Date()));
								// Se corrige porque puede traer espacios
								numRegistro = numRegistro.trim();
								File subcarpeta = new File(raiz2, numRegistro);
								// cambio
								File folderRoot = subcarpeta;
								if (!folderRoot.exists()) {
									folderRoot.mkdirs();
								}

								File archivo = new File(
										folderRoot.getAbsolutePath()
												+ File.separator
												+ tempFile.getFileName());
								OutputStream output = new FileOutputStream(archivo);
								IOUtils.copy(tempFile.getInputstream(), output);
								output.close();
								log.info("Se guarda el adjunto en "
										+ folderRoot.getAbsolutePath());
							
						}

					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
			// cambioDeEtapaAutomatico(nuevoRegistro);
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_CREAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			page = "transaccionExitosa6";
			
			// nuevoRegistro = new Entrada();
			if(idClasificacionSeleccionada ==91){
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mapaImagenes", null);
				FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().put("documentInedita", null);
			}
			buttonEtiqueta = true;
			FacesUtils.flashScope(FacesContext.getCurrentInstance()).put(
					"idEntradaGenerado", idEntrada);
			FacesUtils.flashScope(FacesContext.getCurrentInstance()).put(
					"generarEtiqueta", generarEtiqueta);
			/*Para generar la etiqueta */
			FacesContext.getCurrentInstance().getExternalContext()
			.getSessionMap().put("entrada", nuevoRegistro);

		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}

		return page;
	}

	public String cambioDeEtapaAutomatico(Entrada entrada) {
		log.info("actualizar!");
		String page = null;
		try {
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, ID_EVENTO_REPARTIR);
			params.put(1, ESTADO_INICIAL_REPARTIR);
			Transicion transicion = (Transicion) serviceDao
					.getGenericCommonDao().executeUniqueResult(
							Transicion.NAMED_QUERY_GET_ALL_BY_EVEIDTRSINICIAL,
							params);
			Usuario usuario = (Usuario) serviceDao.getGenericCommonDao().read(
					Usuario.class, idUsuarioSeleccionado);
			// entrada.setEstado(transicion.getEstadoByTrsEstfin());
			entrada.setUsuario(usuario);

			serviceDao.getEntradaDao().update(Entrada.class, entrada);

			serviceDao.getSeguimientosDao().addSeguimientoEntrada(entrada,
					transicion, Seguimientoentrada.SEGUIMIENTO_CAMBIO_ETAPA,
					observacion, UserDetailsUtils.usuarioLogged(), usuario);

			page = "transaccionExitosa";
			entradaSeleccionada = new Entrada();

		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}

		return page;
	}

	@SuppressWarnings("unchecked")
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.MNUE)
	public String actualizar() {
		String page = null;
		try {
			Integer totalFolios = 0;
			if (anexosClasificacion != null && !anexosClasificacion.isEmpty()) {
				for (Anexo anexo : anexosClasificacion) {
					totalFolios = totalFolios.intValue()
							+ anexo.getNumFolios().intValue();
				}
			}

			Depend dependencia = (Depend) serviceDao.getGenericCommonDao()
					.read(Depend.class, idDependenciaSeleccionada);
			Clasificacion clasificacion = (Clasificacion) serviceDao
					.getGenericCommonDao().read(Clasificacion.class,
							idClasificacionSeleccionada);
			Estado estado = (Estado) serviceDao.getGenericCommonDao().read(
					Estado.class, idEstadoSeleccionado);
			Medioscorrespondencia medio = (Medioscorrespondencia) serviceDao
					.getGenericCommonDao().read(Medioscorrespondencia.class,
							idMedioSeleccionada);
			Tipospersona tipoPersona = (Tipospersona) serviceDao
					.getGenericCommonDao().read(Tipospersona.class,
							idTiposPersonaSeleccionado);
			Calidadrepresentante calidadRepresentante = (Calidadrepresentante) serviceDao
					.getGenericCommonDao().read(Calidadrepresentante.class,
							idCalidadRepreSeleccionado);

			boolean requiereRespuesta = clasificacion.isClaLrta();
			Short tiempoRespuesta = clasificacion.getClaTie();

			log.info("Actualizando");
			log.info("idClasificacionSeleccionada="
					+ idClasificacionSeleccionada);
			log.info("idEstadoSeleccionado=" + idEstadoSeleccionado);

			entradaSeleccionada.setEntLrsa(respuestaSalida);
			entradaSeleccionada.setMedio(medio);
			entradaSeleccionada.setDependencia(dependencia);
			entradaSeleccionada.setClasificacion(clasificacion);
			entradaSeleccionada.setEstado(estado);
			entradaSeleccionada.setCalidadRepresentante(calidadRepresentante);

			entradaSeleccionada.setTipoPersona(tipoPersona);
			if (idTipoDocumentoSol != null) {
				Tiposdocumento tipoDocumento = (Tiposdocumento) serviceDao
						.getGenericCommonDao().read(Tiposdocumento.class,
								idTipoDocumentoSol);
				entradaSeleccionada.setTiposdocumento(tipoDocumento);
			}
			if (idPaisSeleccionado != null) {
				Paises pais = (Paises) serviceDao.getGenericCommonDao().read(
						Paises.class, idPaisSeleccionado);
				entradaSeleccionada.setPais(pais);
			}
			if (idLugarSeleccionado != null) {
				Lugar lugar = (Lugar) serviceDao.getGenericCommonDao().read(
						Lugar.class, idLugarSeleccionado);
				entradaSeleccionada.setLugar(lugar);
			}

			entradaSeleccionada.setEntLrta(requiereRespuesta);
			entradaSeleccionada.setEntTrt(tiempoRespuesta);
			entradaSeleccionada.setEntFol(totalFolios.shortValue());
			entradaSeleccionada.setEntFdr(serviceDao.getEntradaDao()
					.obtenerFechaRespuesta(entradaSeleccionada));

			serviceDao.getEntradaDao().update(Entrada.class,
					entradaSeleccionada);
			Integer idEntrada = entradaSeleccionada.getEntId();
			if (idEntrada != null) {

				// ELIMINA LA RELACION DE ANEXOS
				for (Anexoentrada anexoEntrada : this.entradaSeleccionada
						.getAnexos()) {
					serviceDao.getEntradaDao().delete(Anexoentrada.class,
							anexoEntrada);
				}

				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, entradaSeleccionada.getEntId());

				// ELIMINA ENLACES
				List<Enlace> enlaces = (List<Enlace>) serviceDao
						.getGenericCommonDao()
						.executeFind(
								Enlace.class,
								params,
								Enlace.NAMED_QUERY_GET_BY_ENTRADA_ORIGEN_ENTRADA);
				for (Enlace enlace : enlaces) {
					serviceDao.getEntradaDao().delete(Enlace.class, enlace);
				}

				if (anexosClasificacion != null
						&& !anexosClasificacion.isEmpty()) {
					// RELACIONA LOS ANEXOS
					for (Anexo anexo : anexosClasificacion) {
						if (anexo.getNumFolios() != null
								&& !anexo.getNumFolios().equals(
										new Integer(0).shortValue())) {
							Anexoentrada anexoEntrada = new Anexoentrada(
									new AnexoentradaId(idEntrada,
											anexo.getAnxId()),
									anexo.getNumFolios());
							serviceDao.getEntradaDao().create(
									Anexoentrada.class, anexoEntrada);
						}
					}
				}

				if (salidasSeleccionadas != null
						&& !salidasSeleccionadas.isEmpty()) {
					for (Salida salida : salidasSeleccionadas) {
						Enlace enlaceEntrada = new Enlace(new EnlaceId(
								Enlace.ORIGEN_ENTRADA, idEntrada,
								salida.getSalId()));
						serviceDao.getEntradaDao().create(Enlace.class,
								enlaceEntrada);
					}
				}
			}

			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			page = "transaccionExitosa";
			entradaSeleccionada = new Entrada();

		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}

		return page;
	}

	private Claseregistro idClasificacionIdClaseRegistro(Entrada entrada) {
		Short idClasificacion = entrada.getClasificacion().getClaId();
		Claseregistro claseRegistro = null;
		if (idClasificacion >= 91 && idClasificacion <= 99) {

			switch (idClasificacion) {
			case 91:
				claseRegistro = (Claseregistro) serviceDao
						.getGenericCommonDao().read(Claseregistro.class, 46);
				break;
			case 92:
				claseRegistro = (Claseregistro) serviceDao
						.getGenericCommonDao().read(Claseregistro.class, 45);
				break;
			case 93:
				claseRegistro = (Claseregistro) serviceDao
						.getGenericCommonDao().read(Claseregistro.class, 52);
				break;
			case 94:
				claseRegistro = (Claseregistro) serviceDao
						.getGenericCommonDao().read(Claseregistro.class, 59);
				break;
			case 95:
				claseRegistro = (Claseregistro) serviceDao
						.getGenericCommonDao().read(Claseregistro.class, 56);
				break;
			case 96:
				claseRegistro = (Claseregistro) serviceDao
						.getGenericCommonDao().read(Claseregistro.class, 64);
				break;
			case 98:
				claseRegistro = (Claseregistro) serviceDao
						.getGenericCommonDao().read(Claseregistro.class, 69);
				break;
			case 99:
				claseRegistro = (Claseregistro) serviceDao
						.getGenericCommonDao().read(Claseregistro.class, 61);
				break;
			}

		}
		return claseRegistro;

	}

	public void actualizarListaSalidas() {
		log.info("actualizarListaSalidas!");
		salidasEntradas.clear();
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(0, entradaSeleccionada.getEntId());
		List<?> enlacesSalidas = (List<?>) serviceDao.getGenericCommonDao()
				.executeFind(Enlace.class, params,
						Enlace.NAMED_QUERY_GET_BY_ENTRADA_ORIGEN_SALIDA);
		for (Object enlace : enlacesSalidas) {
			salidasEntradas.add(((Enlace) enlace).getSalida());
		}
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formDetalle:panelSalidasEntradas");
	}

	@SuppressWarnings("unchecked")
	public void cargarRegistroDetalle() {
		try {
			if (idRegSeleccionado == null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext()
						.getRequestParameterMap().get("idRegSeleccionado");
				if (idRegSeleccionado != null)
					this.idRegSeleccionado = Integer
							.parseInt(idRegSeleccionado);
			}

			if ((entradaSeleccionada == null || entradaSeleccionada.getEntId() == null)
					&& (idRegSeleccionado != null)) {
				entradaSeleccionada = (Entrada) serviceDao.getEntradaDao()
						.read(Entrada.class, idRegSeleccionado);
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, entradaSeleccionada.getEntId());
				seguimientoEntrada = (List<Seguimientoentrada>) serviceDao
						.getEntradaDao().executeFind(Seguimientoentrada.class,
								params,
								Seguimientoentrada.NAMED_QUERY_GET_BY_ENTRADA);

				List<Enlace> enlaces = (List<Enlace>) serviceDao
						.getGenericCommonDao()
						.executeFind(
								Enlace.class,
								params,
								Enlace.NAMED_QUERY_GET_BY_ENTRADA_ORIGEN_ENTRADA);
				for (Enlace enlace : enlaces) {
					salidasSeleccionadas.add(enlace.getSalida());
				}

				List<Enlace> enlacesSalidas = (List<Enlace>) serviceDao
						.getGenericCommonDao()
						.executeFind(Enlace.class, params,
								Enlace.NAMED_QUERY_GET_BY_ENTRADA_ORIGEN_SALIDA);
				for (Enlace enlace : enlacesSalidas) {
					salidasEntradas.add(enlace.getSalida());
				}

				for (Anexoentrada anexoEntrada : this.entradaSeleccionada
						.getAnexos()) {
					Anexo anexo = anexoEntrada.getAnexo();
					anexo.setNumFolios(anexoEntrada.getDaeCan());
					anexosClasificacion.add(anexo);
				}

				// Leer los descargables
				actualizarDescargables(this.entradaSeleccionada);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void actualizarDescargables(Entrada entradaSeleccionada2) {
		String carpeta = preferencias.get(PREFS.CONS_DOC_CARPETA_ADJ);
		if (carpeta != null) {
			Date fecha;
			File raiz = new File(carpeta);
			// fecha = entradaSeleccionada2.getEntFrf();
			fecha = entradaSeleccionada2.getEntFre();
			String numRadicado = entradaSeleccionada2.getEntNen();
			Integer id = entradaSeleccionada2.getEntId();

			descargables.clear();
			if (numRadicado != null && fecha != null && raiz.exists()) {
				SimpleDateFormat format = new SimpleDateFormat("/yyyy/MM/dd/");
				SimpleDateFormat format2 = new SimpleDateFormat("/yyyy/M/d/");
				File raiz2 = new File(raiz, format.format(fecha));
				File raiz2b = new File(raiz, format2.format(fecha));
				numRadicado = numRadicado.trim();// Se corrige porque trae
													// espacios
				File subcarpeta = new File(raiz2, numRadicado);
				File subcarpeta2 = new File(raiz2b, "" + id);
				log.info("subcarpeta=" + subcarpeta);
				log.info("subcarpeta2=" + subcarpeta2);
				if (subcarpeta.exists()) {
					File[] archivos = subcarpeta.listFiles();
					for (File archivo : archivos) {
						if (archivo.isFile()) {
							try {
								AnexoDescargable temp;
								temp = new AnexoDescargable();
								temp.setNombre(archivo.getName());
								temp.setPath(archivo.getAbsolutePath());
								descargables.add(temp);
							} catch (Exception e) {
								log.error("Error abriendo el adjunto "
										+ archivo.getAbsolutePath() + ". "
										+ e.getMessage());
							}
						}
					}
				}

				if (subcarpeta2.exists()) {
					File[] archivos = subcarpeta2.listFiles();
					for (File archivo : archivos) {
						if (archivo.isFile()) {
							try {
								AnexoDescargable temp;
								temp = new AnexoDescargable();
								temp.setNombre(archivo.getName());
								temp.setPath(archivo.getAbsolutePath());
								descargables.add(temp);
							} catch (Exception e) {
								log.error("Error abriendo el adjunto "
										+ archivo.getAbsolutePath() + ". "
										+ e.getMessage());
							}
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void cargarRegistroActualizar() {

		try {
			log.info("<<< Ingreso a cargarRegistroActualizar() ");
			if (idRegSeleccionado == null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext()
						.getRequestParameterMap().get("idRegSeleccionado");
				if (idRegSeleccionado != null)
					this.idRegSeleccionado = Integer
							.parseInt(idRegSeleccionado);
			}
			log.info("<<< idRegSeleccionado >>>  " + idRegSeleccionado);
			if ((entradaSeleccionada == null || entradaSeleccionada.getEntId() == null)
					&& (idRegSeleccionado != null)) {
				entradaSeleccionada = (Entrada) serviceDao.getEntradaDao()
						.read(Entrada.class, idRegSeleccionado);
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, entradaSeleccionada.getEntId());

				respuestaSalida = entradaSeleccionada.isEntLrsa();
				if (respuestaSalida == true) {
					List<Enlace> enlaces = (List<Enlace>) serviceDao
							.getGenericCommonDao()
							.executeFind(
									Enlace.class,
									params,
									Enlace.NAMED_QUERY_GET_BY_ENTRADA_ORIGEN_ENTRADA);
					for (Enlace enlace : enlaces) {
						salidasSeleccionadas.add(enlace.getSalida());
					}

				}

				for (Anexoentrada anexoEntrada : this.entradaSeleccionada
						.getAnexos()) {
					Anexo anexo = anexoEntrada.getAnexo();
					anexo.setNumFolios(anexoEntrada.getDaeCan());
					anexosClasificacion.add(anexo);
				}
				idDependenciaSeleccionada = entradaSeleccionada
						.getDependencia().getDepId();
				idClasificacionSeleccionada = entradaSeleccionada
						.getClasificacion().getClaId();
				idEstadoSeleccionado = entradaSeleccionada.getEstado()
						.getEstId();
				idMedioSeleccionada = entradaSeleccionada.getMedio().getMedId();
				idTiposPersonaSeleccionado = entradaSeleccionada
						.getTipoPersona().getTpeId();
				idCalidadRepreSeleccionado = entradaSeleccionada
						.getCalidadRepresentante().getCalId();

				if (entradaSeleccionada.getTiposdocumento() != null) {
					idTipoDocumentoSol = entradaSeleccionada
							.getTiposdocumento().getTdoId();
				}
				if (entradaSeleccionada.getPais() != null) {
					idPaisSeleccionado = entradaSeleccionada.getPais()
							.getPaiId();
				}
				if (entradaSeleccionada.getLugar() != null) {
					idLugarSeleccionado = entradaSeleccionada.getLugar()
							.getLugId();
				}

				cargarConfiguracionCampos();

			}

		} catch (Exception e) {
			log.error("*** Error en cargarRegistroActualizar ***", e);
			e.printStackTrace();
		}
	}

	public void cargarRegistroDuplicar() {

		try {

			if (idRegSeleccionado == null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext()
						.getRequestParameterMap().get("idRegSeleccionado");
				if (idRegSeleccionado != null)
					this.idRegSeleccionado = Integer
							.parseInt(idRegSeleccionado);
			}

			if (idRegSeleccionado != null) {

				System.out.println(" en cargar duplicar ");
				nuevoRegistro = (Entrada) serviceDao.getEntradaDao().read(
						Entrada.class, idRegSeleccionado);
				nuevoRegistro.setEntId(0);
				nuevoRegistro.setEntCano(null);
				nuevoRegistro.setEntFre(null);

				nuevoRegistro.setEntCdep(null);
				nuevoRegistro.setEntCano(null);
				nuevoRegistro.setUsuario(null);
				nuevoRegistro.setEntFen(null);
				nuevoRegistro.setEntFdr(null);
				nuevoRegistro.setEntCnro(null);
				nuevoRegistro.setEntNen(null);
				nuevoRegistro.setEntLrsa(false);
				respuestaSalida = false;

				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, idRegSeleccionado);

				for (Anexoentrada anexoEntrada : this.nuevoRegistro.getAnexos()) {
					Anexo anexo = anexoEntrada.getAnexo();
					anexo.setNumFolios(anexoEntrada.getDaeCan());
					anexosClasificacion.add(anexo);
				}

				idDependenciaSeleccionada = nuevoRegistro.getDependencia()
						.getDepId();
				idClasificacionSeleccionada = nuevoRegistro.getClasificacion()
						.getClaId();
				idEstadoSeleccionado = nuevoRegistro.getEstado().getEstId();
				idMedioSeleccionada = nuevoRegistro.getMedio().getMedId();
				idTiposPersonaSeleccionado = nuevoRegistro.getTipoPersona()
						.getTpeId();
				idCalidadRepreSeleccionado = nuevoRegistro
						.getCalidadRepresentante().getCalId();

				if (nuevoRegistro.getTiposdocumento() != null) {
					idTipoDocumentoSol = nuevoRegistro.getTiposdocumento()
							.getTdoId();
				}
				if (nuevoRegistro.getPais() != null) {
					idPaisSeleccionado = nuevoRegistro.getPais().getPaiId();
				}
				if (nuevoRegistro.getLugar() != null) {
					idLugarSeleccionado = nuevoRegistro.getLugar().getLugId();
				}

				cargarConfiguracionCampos();
				idRegSeleccionado = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void cargarDependenciasItems() {

		if (dependenciasItems == null) {

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_ENTRADA);

			List<Depend> dependencias = (List<Depend>) serviceDao
					.getGenericCommonDao()
					.executeFind(Depend.class, params,
							Depend.NAMED_QUERY_ALL_DEPEND_BY_TIPO_CLASIFICACION);

			dependenciasItems = new ArrayList<SelectItem>();

			for (Depend dependencia : dependencias) {
				dependenciasItems.add(new SelectItem(dependencia.getDepId(),
						dependencia.getDepNom()));
			}
		} else if (dependenciasItems == null) {
			dependenciasItems = new ArrayList<SelectItem>();
		}
	}

	public void seleccionarDependeciaPredeterminada() {
		log.info("seleccionarDependeciaPredeterminada!");
		idDependenciaSeleccionada = TAG_DEPENDENCIA_REGISTRO;
	}

	@SuppressWarnings("unchecked")
	public void cargarConfiguracionCampos() {
		try {
			log.info("<<< Ingreso a cargarConfiguracionCampos:");
			if (idClasificacionSeleccionada != null
					&& idDependenciaSeleccionada != null) {
				configuracionFormulario = new ConfigForms(
						idDependenciaSeleccionada, idClasificacionSeleccionada);

				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, Clasificacion.TIPO_ENTRADA);
				params.put(1, idClasificacionSeleccionada);

				anexosClasificacion = (List<Anexo>) serviceDao
						.getGenericCommonDao().executeFind(Anexo.class, params,
								Anexo.NAMED_QUERY_ALL_BY_TIPO_CLASIFICACION);

				cargarEstadosPosibles();
			}
		} catch (Exception e) {
			log.error("** Se genero error en cargarConfiguracionCampos ", e);

		}
	}

	public void cargarEstadosPosibles() {
		try {
			log.info("<<< Ingreso a cargarEstadosPosibles >>");
			// Debo actualizar los estados posibles
			estadosItems = new ArrayList<SelectItem>();
			if (idClasificacionSeleccionada != null) {
				log.info("<<< VAlor del idClasificacionSeleccionada "
						+ idClasificacionSeleccionada);
				String sql = "select est.est_id as est_id, est.est_nom as est_nom from clasificacion cla "
						+ "inner join tramite trm on cla.cla_trm = trm.trm_id "
						+ "inner join estado est on est.est_trm = trm.trm_id "
						+ "where cla.cla_id = ?";
				log.info("<<< Valor del sql: " + sql);
				List<Map<String, Object>> lectura = null;
				lectura = serviceDao.getSpringGenericDao().executeQuery(sql,
						idClasificacionSeleccionada);

				for (Map<String, Object> elem : lectura) {
					Short est_id = ((Integer) elem.get("est_id")).shortValue();
					String est_nom = (String) elem.get("est_nom");
					estadosItems.add(new SelectItem(est_id, est_nom));
				}

				// Asignar el estado en la entrada actual /cCONTROLAR
				// entradaSeleccionada.getClasificacion().getClaId() null
				if (entradaSeleccionada != null) {
					if (entradaSeleccionada.getClasificacion().getClaId() == idClasificacionSeleccionada) {
						// Si la clasificación no ha cambiado, dejar el estado
						// que tenga la entrada
						idEstadoSeleccionado = entradaSeleccionada.getEstado()
								.getEstId();
					} else {
						if (estadosItems.size() > 0) {
							// Se selecciona de manera predeterminada el primer
							// estado
							idEstadoSeleccionado = (short) estadosItems.get(0).getValue();
						}
					}
				}

			}
		} catch (Exception e) {
			log.error("*** Error en cargarEstadosPosibles ", e);
		}
	}

	@SuppressWarnings("unchecked")
	public void cargarClasificacionItems() {
		log.info("idDependenciaSeleccionada " + idDependenciaSeleccionada);
		
		if (idDependenciaSeleccionada != null) {
			/*Validar Tab a mostrar dependiendo de la clasificacion */
			cargarTabAdjuntosRegistro();
			log.info("Flag Archivos Registro: " + flagAdjArchivos);
			log.info("Flag archivos Correspondencia: "+ flagAdjArchivosCorrespondencia);
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_ENTRADA);
			params.put(1, idDependenciaSeleccionada);

			List<Clasificacion> clasificaciones = (List<Clasificacion>) serviceDao
					.getGenericCommonDao().executeFind(Depend.class, params,
							Clasificacion.NAMED_QUERY_ALL_BY_TIPO_DEPENDENCIA);

			clasificacionItems = new ArrayList<SelectItem>();

			for (Clasificacion clasificacion : clasificaciones) {
				clasificacionItems.add(new SelectItem(clasificacion.getClaId(),
						clasificacion.getClaNom()));
			}
		} else if (clasificacionItems == null) {
			clasificacionItems = new ArrayList<SelectItem>();
		}
	}

	/**
	 * Seleccionar Clasificacion predeterminada
	 */
	public void seleccionarClasificacionPredeterminada() {
		log.info("seleccionarClasificacionPredeterminada!");
		idClasificacionSeleccionada = TAG_REGISTRO_OBRA_LITERARIA_INEDITA;
		cargarConfiguracionCampos();
	}

	@SuppressWarnings("unchecked")
	public void cargarMediosItems() {

		if (mediosItems == null) {

			List<Medioscorrespondencia> allData = (List<Medioscorrespondencia>) serviceDao
					.getGenericCommonDao().executeFind(
							Medioscorrespondencia.NAMED_QUERY_ALL);

			mediosItems = new ArrayList<SelectItem>();

			for (Medioscorrespondencia dato : allData) {
				mediosItems.add(new SelectItem(dato.getMedId(), dato
						.getMedNom()));
			}

		} else if (mediosItems == null) {
			mediosItems = new ArrayList<SelectItem>();
		}
	}

	public void seleccionarMedioCorrespondenciaPredeterminado() {
		log.info("seleccionarMedioCorrespondenciaPredeterminado!");
		idMedioSeleccionada = TAG_MEDIO_CORRESPONDENCIA;
	}

	@SuppressWarnings("unchecked")
	public void cargarTiposDocItems() {

		if (tiposDocumentosItems == null) {

			List<Tiposdocumento> allData = (List<Tiposdocumento>) serviceDao
					.getGenericCommonDao().executeFind(
							Tiposdocumento.NAMED_QUERY_ALL);

			tiposDocumentosItems = new ArrayList<SelectItem>();

			for (Tiposdocumento dato : allData) {
				tiposDocumentosItems.add(new SelectItem(dato.getTdoId(), dato
						.getTdoNom()));
			}

		} else if (tiposDocumentosItems == null) {
			tiposDocumentosItems = new ArrayList<SelectItem>();
		}
	}

	public void seleccionarTipoDocumentoPredeterminado() {
		log.info("seleccionarTipoDocumentoPredeterminado!");
		idTipoDocumentoSol = TAG_TIPO_DOCUMENTO;
	}

	/**
	 * Carga lista tipo de peronas tipo de personas.
	 */
	@SuppressWarnings("unchecked")
	public void cargaTipoPersonaItems() {
		if (tiposPersonaItems == null) {
			List<Tipospersona> tipospersonaAuxi = (List<Tipospersona>) serviceDao
					.getGenericCommonDao().executeFind(
							Tipospersona.NAMED_QUERY_ALL_TP);

			tiposPersonaItems = new ArrayList<SelectItem>();
			for (Tipospersona tipospersona : tipospersonaAuxi) {
				tiposPersonaItems.add(new SelectItem(tipospersona.getTpeId(),
						tipospersona.getTpeNom()));
			}
		}
	}

	public void seleccionarTipoPersonaPredeterminada() {
		log.info("seleccionarTipoPersonaPredeterminada!");
		idTiposPersonaSeleccionado = TAG_NATURAL;
	}

	@SuppressWarnings("unchecked")
	public void cargaPaisesItems() {
		if (paisesItems == null) {
			List<Paises> allData = (List<Paises>) serviceDao
					.getGenericCommonDao().executeFind(Paises.NAMED_QUERY_ALL);

			paisesItems = new ArrayList<SelectItem>();
			for (Paises dato : allData) {
				paisesItems.add(new SelectItem(dato.getPaiId(), dato
						.getPaiNom()));
			}
		}
	}

	public void seleccionarPaisPredeterminada() {
		log.info("seleccionarPaisPredeterminada!");
		idPaisSeleccionado = TAG_PAIS_PREDETERMINADO;
	}

	@SuppressWarnings("unchecked")
	public void cargaLugaresItems() {
		if (lugaresItems == null) {
			List<Lugar> allData = (List<Lugar>) serviceDao
					.getGenericCommonDao().executeFind(Lugar.NAMED_QUERY_ALL);
			lugaresItems = new ArrayList<SelectItem>();
			for (Lugar dato : allData) {
				lugaresItems.add(new SelectItem(dato.getLugId(), dato
						.getLugCiu()));
			}
		} else if (lugaresItems == null) {
			lugaresItems = new ArrayList<SelectItem>();
		}
	}

	/**
	 * Carga lista calidad representante
	 */
	@SuppressWarnings("unchecked")
	public void cargaCalidadRepresentanteItems() {
		if (calidadRepresentanteItems == null) {
			List<Calidadrepresentante> data = (List<Calidadrepresentante>) serviceDao
					.getGenericCommonDao().executeFind(
							Calidadrepresentante.NAMED_QUERY_ALL_CR);

			calidadRepresentanteItems = new ArrayList<SelectItem>();
			for (Calidadrepresentante dato : data) {
				calidadRepresentanteItems.add(new SelectItem(dato.getCalId(),
						dato.getCalNom()));
			}
		} else if (calidadRepresentanteItems == null) {
			calidadRepresentanteItems = new ArrayList<SelectItem>();
		}
	}

	public void seleccionarCalidadRepresentantePredeterminada() {
		log.info("seleccionarCalidadRepresentantePredeterminada!");
		idCalidadRepreSeleccionado = TAG_EN_NOMBRE_PROPIO;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public Short getIdDependenciaSeleccionada() {
		return idDependenciaSeleccionada;
	}

	public Short getIdClasificacionSeleccionada() {
		return idClasificacionSeleccionada;
	}

	public void setIdDependenciaSeleccionada(Short idDependenciaSeleccionada) {
		this.idDependenciaSeleccionada = idDependenciaSeleccionada;
	}

	public void setIdClasificacionSeleccionada(Short idClasificacionSeleccionada) {
		this.idClasificacionSeleccionada = idClasificacionSeleccionada;
	}

	public List<SelectItem> getDependenciasItems() {
		cargarDependenciasItems();
		return dependenciasItems;
	}

	public void setDependenciasItems(List<SelectItem> dependenciasItems) {
		this.dependenciasItems = dependenciasItems;
	}

	public List<SelectItem> getClasificacionItems() {
		cargarClasificacionItems();
		return clasificacionItems;
	}

	public void setClasificacionItems(List<SelectItem> clasificacionItems) {
		this.clasificacionItems = clasificacionItems;
	}

	public Persona getPersonaNaturalSeleccionada() {
		return personaNaturalSeleccionada;
	}

	public void setPersonaNaturalSeleccionada(Persona personaNaturalSeleccionada) {
		this.personaNaturalSeleccionada = personaNaturalSeleccionada;

		if (entradaSeleccionada != null
				&& entradaSeleccionada.getEntId() != null) {
			entradaSeleccionada.setEntPap(personaNaturalSeleccionada
					.getPerPap());
			entradaSeleccionada.setEntSap(personaNaturalSeleccionada
					.getPerSap());
			entradaSeleccionada.setEntNom(personaNaturalSeleccionada
					.getPerNom());
			entradaSeleccionada.setEntNdo(personaNaturalSeleccionada
					.getPerDoc());
			if (personaNaturalSeleccionada.getTiposdocumento() != null)
				setIdTipoDocumentoSol(personaNaturalSeleccionada
						.getTiposdocumento().getTdoId());

			if (personaNaturalSeleccionada.getPaises() != null)
				setIdPaisSeleccionado(personaNaturalSeleccionada.getPaises()
						.getPaiId());

			entradaSeleccionada.setEntDir(personaNaturalSeleccionada
					.getPerDir());
			entradaSeleccionada.setEntTel(personaNaturalSeleccionada
					.getPerTl1());
			entradaSeleccionada.setEntFax(personaNaturalSeleccionada
					.getPerFax());
			entradaSeleccionada.setEntCel(personaNaturalSeleccionada
					.getPerCe1());
			if (personaNaturalSeleccionada.getLugarByPerLug() != null)
				setIdLugarSeleccionado(personaNaturalSeleccionada
						.getLugarByPerLug().getLugId());

		} else if (personaNaturalSeleccionada != null && nuevoRegistro != null) {
			nuevoRegistro.setEntPap(personaNaturalSeleccionada.getPerPap());
			nuevoRegistro.setEntSap(personaNaturalSeleccionada.getPerSap());
			nuevoRegistro.setEntNom(personaNaturalSeleccionada.getPerNom());
			nuevoRegistro.setEntNdo(personaNaturalSeleccionada.getPerDoc());
			if (personaNaturalSeleccionada.getTiposdocumento() != null)
				setIdTipoDocumentoSol(personaNaturalSeleccionada
						.getTiposdocumento().getTdoId());

			if (personaNaturalSeleccionada.getPaises() != null)
				setIdPaisSeleccionado(personaNaturalSeleccionada.getPaises()
						.getPaiId());

			nuevoRegistro.setEntDir(personaNaturalSeleccionada.getPerDir());
			nuevoRegistro.setEntTel(personaNaturalSeleccionada.getPerTl1());
			nuevoRegistro.setEntFax(personaNaturalSeleccionada.getPerFax());
			nuevoRegistro.setEntCel(personaNaturalSeleccionada.getPerCe1());
			if (personaNaturalSeleccionada.getLugarByPerLug() != null)
				setIdLugarSeleccionado(personaNaturalSeleccionada
						.getLugarByPerLug().getLugId());
		}
	}

	public Entidad getPersonaJuridicaSeleccionada() {
		return personaJuridicaSeleccionada;
	}

	public void setPersonaJuridicaSeleccionada(
			Entidad personaJuridicaSeleccionada) {
		this.personaJuridicaSeleccionada = personaJuridicaSeleccionada;

		if (entradaSeleccionada != null
				&& entradaSeleccionada.getEntId() != null) {
			entradaSeleccionada.setEntIde(personaJuridicaSeleccionada
					.getEtdIde());
			entradaSeleccionada.setEntEnt(personaJuridicaSeleccionada
					.getEtdNom());
		} else if (personaJuridicaSeleccionada != null && nuevoRegistro != null) {
			nuevoRegistro.setEntIde(personaJuridicaSeleccionada.getEtdIde());
			nuevoRegistro.setEntEnt(personaJuridicaSeleccionada.getEtdNom());
		}
	}

	public List<SelectItem> getMediosItems() {
		cargarMediosItems();
		return mediosItems;
	}

	public void uploadFile(FileUploadEvent event) throws Exception {
		try {
			FacesMessage msg = new FacesMessage("Archivo ", event.getFile()
					.getFileName() + " agregado correctamente.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			log.info("Entro uploadFile");
			UploadedFile file = event.getFile();
			System.out.println("---Nombre" + file.getFileName());
			System.out.println("---Size Data " + file.getSize());
			/*Files para registro */
			if (flagAdjArchivos) {
				DocumentosRegistro documentosRegistro = new DocumentosRegistro();
				documentosRegistro.setDocumentoId((int) idTipoDocumentoSeleccionado);
				documentosRegistro.setFiles(file);
				if (idClasificacionSeleccionada == 91) {
					log.info("<<<Ingreso Clasificacion Inedita>>>");
					short documentInedita = (short) FacesContext
							.getCurrentInstance().getExternalContext()
							.getSessionMap().get("documentInedita");
					log.info("<<<documentInedita>>> "+ documentInedita);
					// condicionarlo a obra inedita
					// recupera objeto en session
					documentosRegistro.setDocumentoId((int) documentInedita);
					filesRegistro = (HashMap<String, DocumentosRegistro>) FacesContext
							.getCurrentInstance().getExternalContext()
							.getSessionMap().get("mapaImagenes");
					if (filesRegistro == null) {
						filesRegistro = new HashMap<String, DocumentosRegistro>();
					}
					// guardarlo
	
					filesRegistro.put(file.getFileName(), documentosRegistro);
					FacesContext.getCurrentInstance().getExternalContext()
							.getSessionMap().put("mapaImagenes", filesRegistro);
				}else{
					log.info("<<<Ingreso Clasificacion Diferente a Inedita>>>");
					filesRegistro.put(file.getFileName(), documentosRegistro);
				}
				listarArchivosRegistro();
			}
			/*Files para correspondencia entradas Normales*/
			/*if (flagAdjArchivosCorrespondencia) {
				files.put(file.getFileName(), file);
				listarArchivos();
			}*/

		} catch (Exception e) {
			log.error("Error en uploadFile: ", e);
		}

	}
	/**
	 * Upload File, para documento de registro (TAG)
	 * @param event
	 */
	public void handleFileUpload(FileUploadEvent fileUploadEvent) throws Exception {
		try {
			FacesMessage msg = new FacesMessage("Archivo ", fileUploadEvent.getFile()
					.getFileName() + " agregado correctamente.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			log.info("Entro uploadFile Entradas");
			/*Files para correspondencia entradas Normales*/
			if (flagAdjArchivosCorrespondencia) {
				UploadedFile file = fileUploadEvent.getFile();
				files.put(file.getFileName(), file);
				listarArchivos();
			}
		

		} catch (Exception e) {
			log.error("Error en uploadFile para registro: ", e);
			
		}
	}
	
	
	public void valueChanged() {
		/*Obra Inedita*/
		if (idClasificacionSeleccionada == 91) {
			short documentInedita = idTipoDocumentoSeleccionado;
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().put("documentInedita", documentInedita);
		}
	    log.info("change document" + idTipoDocumentoSeleccionado);
	  
	}
	
	public void valueChangedTipoDoc(){
		log.info("change tipo documento " + idTipoDocumentoSol);
		if(idTipoDocumentoSol == 15){
			muestrese = false;
		} else{
			muestrese = true;
		}
		log.info("bandera " + muestrese);
	}

	public String getSizeLimit() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		String maxRequestSize = ctx.getExternalContext().getInitParameter(
				"maxRequestSize");
		return maxRequestSize;
	}

	public void listarArchivos() {
		try {

			dataFiles = new ArrayList<String>();
			dataFilesArchivoEntrada = new ArrayList<ArchivoEntrada>();
			System.out.println("Map size " + files.size());
			for (Map.Entry<String, Object> file : files.entrySet()) {
				if (file.getValue() instanceof UploadedFile) {
					UploadedFile temp = (UploadedFile) file.getValue();
					ArchivoEntrada arhEntrada = new ArchivoEntrada();
					arhEntrada.setNombre(temp.getFileName());
					arhEntrada.setFechaRegistro(new Date(System
							.currentTimeMillis()));
					dataFiles.add(file.getKey().toString() + ": "
							+ temp.getFileName());
					System.out.println("--> Valor Archivos "
							+ file.getKey().toString() + "  "
							+ temp.getFileName());
					dataFilesArchivoEntrada.add(arhEntrada);
				}
			}
		} catch (Exception e) {

		}

	}
	public void listarArchivosRegistro() {
		try {

			dataFilesArchivoRegistro = new ArrayList<ArchivoRegistro>();
			log.info("Map size Registro " + filesRegistro.size());
			for (Map.Entry<String, DocumentosRegistro> file : filesRegistro.entrySet()) {
				DocumentosRegistro value = file.getValue();
				UploadedFile tempFile = (UploadedFile) value.getFiles();
				ArchivoRegistro arhRegistro = new ArchivoRegistro();
				arhRegistro.setNombre(tempFile.getFileName());
				arhRegistro.setDocumentos((Documentos)serviceDao.getGenericCommonDao().read(Documentos.class, value.getDocumentoId()));
				log.info("Valor Archivos Registro: " + "id Documento: " + value.getDocumentoId() + " Nombre Archivo: " + tempFile.getFileName());
				dataFilesArchivoRegistro.add(arhRegistro);
				}
			
			log.info("Salio de listar ");
			
		} catch (Exception e) {

		}

	}

	@SuppressWarnings("unchecked")
	public void cargarUsuariosItems() {
		if (usuariosItems == null) {
			List<Usuario> usuarios = (List<Usuario>) serviceDao
					.getGenericCommonDao().executeFind(
							Usuario.NAMED_QUERY_ALL_USUARIOS_ACTIVOS);
			usuariosItems = new ArrayList<SelectItem>();
			for (Usuario usu : usuarios) {
				usuariosItems.add(new SelectItem(usu.getUsrId(), usu
						.getUsrNom()
						+ " "
						+ usu.getUsrPap()
						+ " "
						+ usu.getUsrSap()));
			}
		} else if (usuariosItems == null) {
			usuariosItems = new ArrayList<SelectItem>();
		}
	}
	/**
	 * Generar la etiqueta de la entrada correspondiente
	 */
	@SuppressWarnings("unchecked")
	public void generarEtiquetaEntrada(){
		entradaAux = (Entrada) FacesContext
				.getCurrentInstance().getExternalContext()
				.getSessionMap().get("entrada");
		if(entradaAux != null){
			log.info("Cargo Entrada Aux!!");
			
			Configuracion conf = (Configuracion) serviceDao.getGenericCommonDao().executeFind(Configuracion.NAMED_QUERY_GET_ALL_CONF).get(0);

			String radicado = entradaAux.getEntNen();
			String fecha = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(entradaAux.getEntFen());
			String dependencia = entradaAux.getDependencia().getDepNom();
			String telefono	= conf.getPbx();
			String folios = "0";
			if(entradaAux.getEntFol() != null)
				folios = entradaAux.getEntFol().toString();
			
	    	Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("radicado", radicado);
			parametros.put("fecha", fecha);
			parametros.put("dependencia", dependencia);
			parametros.put("telefono", telefono);
			parametros.put("folios",folios);
			
	    	File archivoPDF = new File(ConstantsKeysFire.RUTA_ABSOLUTA_TEMP_DOWNLOAD_FILE+"/"+ System.currentTimeMillis()+"Reporte.pdf");
	    	
	    	JasperReports.generarReportePDF(ConstantsKeysFire.RUTA_ARCHIVOS_REPORTES_JASPER+ConstantsKeysFire.REPORTE_ETIQUETA_ENTRADA,archivoPDF , parametros);
	    	
	    	enlaceEtiqueta = ConstantsKeysFire.RUTA_PUBLICA_TEMP_DOWNLOAD_FILE +"/"+ archivoPDF.getName();
	    	modalVisible = true;
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void cargaItemsDocumentos() {
		if (itemsDocumentos == null) {
			if (idClasificacionSeleccionada >= Clasificacion.CAL_INI_REGISTRO
					&& idClasificacionSeleccionada <= Clasificacion.CAL_FIN_REGISTRO) {
				List<Documentos> listaAuxiDocumentos = (List<Documentos>) serviceDao
						.getGenericCommonDao().executeFind(Documentos.NAMED_QUERY_ALL_DOCUMENTS_REGISTRO);

				itemsDocumentos = new ArrayList<SelectItem>();

				for (Documentos documentos : listaAuxiDocumentos) {
					itemsDocumentos.add(new SelectItem(documentos.getDocId(),
							documentos.getDocNombre()));
				
				}
				
			} else {
			List<Documentos> listaAuxiDocumentos = (List<Documentos>) serviceDao
					.getGenericCommonDao().executeFind(Documentos.NAMED_QUERY_ALL_DOCUMENTS);

			itemsDocumentos = new ArrayList<SelectItem>();

			for (Documentos documentos : listaAuxiDocumentos) {
				itemsDocumentos.add(new SelectItem(documentos.getDocId(),
						documentos.getDocNombre()));
			
			}
			}
		}else if (itemsDocumentos == null) {
			itemsDocumentos = new ArrayList<SelectItem>();
		}
	}
	
	public List<SelectItem> getUsuariosItems() {
		cargarUsuariosItems();
		return usuariosItems;
	}

	public void setUsuariosItems(List<SelectItem> usuariosItems) {
		this.usuariosItems = usuariosItems;
	}

	public Short getIdUsuarioSeleccionado() {
		return idUsuarioSeleccionado;
	}

	public void setIdUsuarioSeleccionado(Short idUsuarioSeleccionado) {
		this.idUsuarioSeleccionado = idUsuarioSeleccionado;
	}

	public void setMediosItems(List<SelectItem> mediosItems) {
		this.mediosItems = mediosItems;
	}

	public Short getIdMedioSeleccionada() {
		return idMedioSeleccionada;
	}

	public void setIdMedioSeleccionada(Short idMedioSeleccionada) {
		this.idMedioSeleccionada = idMedioSeleccionada;
	}

	public Entrada getNuevoRegistro() {
		return nuevoRegistro;
	}

	public void setNuevoRegistro(Entrada nuevoRegistro) {
		this.nuevoRegistro = nuevoRegistro;
	}

	public List<SelectItem> getTiposPersonaItems() {
		cargaTipoPersonaItems();
		return tiposPersonaItems;
	}

	public void setTiposPersonaItems(List<SelectItem> tiposPersonaItems) {
		this.tiposPersonaItems = tiposPersonaItems;
	}

	public Short getIdTiposPersonaSeleccionado() {
		return idTiposPersonaSeleccionado;
	}

	public void setIdTiposPersonaSeleccionado(Short idTiposPersonaSeleccionado) {
		this.idTiposPersonaSeleccionado = idTiposPersonaSeleccionado;
	}

	public void quitarSalida(Salida salida) {
		if (salidasSeleccionadas.contains(salida)) {
			salidasSeleccionadas.remove(salida);
			System.out.println(" remove");
		}
	}

	public List<SelectItem> getTiposDocumentosItems() {
		cargarTiposDocItems();
		return tiposDocumentosItems;
	}

	public Integer getIdTipoDocumentoSol() {
		return idTipoDocumentoSol;
	}

	public void setTiposDocumentosItems(List<SelectItem> tiposDocumentosItems) {
		this.tiposDocumentosItems = tiposDocumentosItems;
	}

	public void setIdTipoDocumentoSol(Integer idTipoDocumentoSol) {
		this.idTipoDocumentoSol = idTipoDocumentoSol;
	}

	public List<SelectItem> getPaisesItems() {
		cargaPaisesItems();
		return paisesItems;
	}

	public Short getIdPaisSeleccionado() {
		return idPaisSeleccionado;
	}

	public List<SelectItem> getLugaresItems() {
		cargaLugaresItems();
		return lugaresItems;
	}

	public Short getIdLugarSeleccionado() {
		return idLugarSeleccionado;
	}

	public void setPaisesItems(List<SelectItem> paisesItems) {
		this.paisesItems = paisesItems;
	}

	public void setIdPaisSeleccionado(Short idPaisSeleccionado) {
		this.idPaisSeleccionado = idPaisSeleccionado;
	}

	public void setLugaresItems(List<SelectItem> lugaresItems) {
		this.lugaresItems = lugaresItems;
	}

	public void setIdLugarSeleccionado(Short idLugarSeleccionado) {
		this.idLugarSeleccionado = idLugarSeleccionado;
	}

	public Map<String, Propiedad> getConfiguracionFormulario() {
		return configuracionFormulario;
	}

	public void setConfiguracionFormulario(
			Map<String, Propiedad> configuracionFormulario) {
		this.configuracionFormulario = configuracionFormulario;
	}

	public List<SelectItem> getCalidadRepresentanteItems() {
		cargaCalidadRepresentanteItems();
		return calidadRepresentanteItems;
	}

	public Short getIdCalidadRepreSeleccionado() {
		return idCalidadRepreSeleccionado;
	}

	public void setCalidadRepresentanteItems(
			List<SelectItem> calidadRepresentanteItems) {
		this.calidadRepresentanteItems = calidadRepresentanteItems;
	}

	public void setIdCalidadRepreSeleccionado(Short idCalidadRepreSeleccionado) {
		this.idCalidadRepreSeleccionado = idCalidadRepreSeleccionado;
	}

	public List<Anexo> getAnexosClasificacion() {
		return anexosClasificacion;
	}

	public void setAnexosClasificacion(List<Anexo> anexosClasificacion) {
		this.anexosClasificacion = anexosClasificacion;
	}

	public List<Salida> getSalidasSeleccionadas() {
		return salidasSeleccionadas;
	}

	public void setSalidasSeleccionadas(List<Salida> salidasSeleccionadas) {
		this.salidasSeleccionadas = salidasSeleccionadas;
	}

	public boolean isRespuestaSalida() {
		return respuestaSalida;
	}

	public void setRespuestaSalida(boolean respuestaSalida) {
		this.respuestaSalida = respuestaSalida;
	}

	public Salida getSalidaSeleccionada() {
		return salidaSeleccionada;
	}

	public void setSalidaSeleccionada(Salida salidaSeleccionada) {
		this.salidaSeleccionada = salidaSeleccionada;

		if (salidaSeleccionada != null && salidasSeleccionadas != null) {
			if (!salidasSeleccionadas.contains(salidaSeleccionada)) {
				salidasSeleccionadas.add(salidaSeleccionada);
			}

		}

	}

	public Entrada getEntradaSeleccionada() {
		return entradaSeleccionada;
	}

	public void setEntradaSeleccionada(Entrada entradaSeleccionada) {
		this.entradaSeleccionada = entradaSeleccionada;
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public List<Seguimientoentrada> getSeguimientoEntrada() {
		return seguimientoEntrada;
	}

	public void setSeguimientoEntrada(
			List<Seguimientoentrada> seguimientoEntrada) {
		this.seguimientoEntrada = seguimientoEntrada;
	}

	public List<Salida> getSalidasEntradas() {
		return salidasEntradas;
	}

	public void setSalidasEntradas(List<Salida> salidasEntradas) {
		this.salidasEntradas = salidasEntradas;
	}

	public boolean isGenerarEtiqueta() {
		return generarEtiqueta;
	}

	public void setGenerarEtiqueta(boolean generarEtiqueta) {
		this.generarEtiqueta = generarEtiqueta;
	}

	public List<AnexoDescargable> getDescargables() {
		return descargables;
	}

	public void setDescargables(List<AnexoDescargable> descargables) {
		this.descargables = descargables;
	}

	/**
	 * @return the idEstadoSeleccionado
	 */
	public short getIdEstadoSeleccionado() {
		return idEstadoSeleccionado;
	}

	/**
	 * @param idEstadoSeleccionado
	 *            the idEstadoSeleccionado to set
	 */
	public void setIdEstadoSeleccionado(short idEstadoSeleccionado) {
		this.idEstadoSeleccionado = idEstadoSeleccionado;
	}

	/**
	 * @return the estadosItems
	 */
	public List<SelectItem> getEstadosItems() {
		cargarEstadosPosibles();
		return estadosItems;
	}

	/**
	 * @param estadosItems
	 *            the estadosItems to set
	 */
	public void setEstadosItems(List<SelectItem> estadosItems) {
		this.estadosItems = estadosItems;
	}
	/**
	 * 
	 * @return the flagAdjArchivos
	 */
	public Boolean getFlagAdjArchivos() {
		return flagAdjArchivos;
	}
	/**
	 *
	 * @param flagAdjArchivos
	 *     the flagAdjArchivos to set
	 */
	public void setFlagAdjArchivos(Boolean flagAdjArchivos) {
		this.flagAdjArchivos = flagAdjArchivos;
	}
	/**
	 * 
	 * @return the flagAdjArchivosCorrespondencia
	 */
	public Boolean getFlagAdjArchivosCorrespondencia() {
		return flagAdjArchivosCorrespondencia;
	}
	/**
	 * 
	 * @param flagAdjArchivosCorrespondencia
	 *  the flagAdjArchivosCorrespondencia to set
	 */
	public void setFlagAdjArchivosCorrespondencia(
			Boolean flagAdjArchivosCorrespondencia) {
		this.flagAdjArchivosCorrespondencia = flagAdjArchivosCorrespondencia;
	}
	
	/**
	 * 
	 * @return the idTipoDocumentoSeleccionado
	 */
	public short getIdTipoDocumentoSeleccionado() {
		return idTipoDocumentoSeleccionado;
	}
	/**
	 * 
	 * @param idTipoDocumentoSeleccionado
	 *  the idTipoDocumentoSeleccionado to set
	 */
	public void setIdTipoDocumentoSeleccionado(short idTipoDocumentoSeleccionado) {
		this.idTipoDocumentoSeleccionado = idTipoDocumentoSeleccionado;
	}
	/**
	 * @return the itemsDocumentos
	 */
	public List<SelectItem> getItemsDocumentos() {
		cargaItemsDocumentos();
		return itemsDocumentos;
	}
	/**
	 * @param itemsDocumentos
	 *            the itemsDocumentos to set
	 */
	public void setItemsDocumentos(List<SelectItem> itemsDocumentos) {
		this.itemsDocumentos = itemsDocumentos;
	}

	public Integer getDocid() {
		return docid;
	}

	public void setDocid(Integer docid) {
		this.docid = docid;
	}

	public List<ArchivoRegistro> getDataFilesArchivoRegistro() {
		return dataFilesArchivoRegistro;
	}

	public void setDataFilesArchivoRegistro(
			List<ArchivoRegistro> dataFilesArchivoRegistro) {
		this.dataFilesArchivoRegistro = dataFilesArchivoRegistro;
	}

	public List<ArchivoEntrada> getDataFilesArchivoEntrada() {
		return dataFilesArchivoEntrada;
	}

	public void setDataFilesArchivoEntrada(
			List<ArchivoEntrada> dataFilesArchivoEntrada) {
		this.dataFilesArchivoEntrada = dataFilesArchivoEntrada;
	}

	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}

	public boolean isMuestrese() {
		return muestrese;
	}

	public void setMuestrese(boolean muestrese) {
		this.muestrese = muestrese;
	}

	public boolean isButtonEtiqueta() {
		return buttonEtiqueta;
	}

	public void setButtonEtiqueta(boolean buttonEtiqueta) {
		this.buttonEtiqueta = buttonEtiqueta;
	}
	
	public boolean isModalVisible() {
		return modalVisible;
	}


	public void setModalVisible(boolean modalVisible) {
		this.modalVisible = modalVisible;
	}

	public String getEnlaceEtiqueta() {
		return enlaceEtiqueta;
	}

	public void setEnlaceEtiqueta(String enlaceEtiqueta) {
		this.enlaceEtiqueta = enlaceEtiqueta;
	}
	
	
	
	
}
