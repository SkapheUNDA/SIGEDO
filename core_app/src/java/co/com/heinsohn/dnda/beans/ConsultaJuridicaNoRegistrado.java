package co.com.heinsohn.dnda.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import la.netco.core.persistencia.dao.SpringGenericDao;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import sun.misc.BASE64Decoder;
import co.com.heinsohn.dnda.commons.enums.TipoDocumentoEnum;
import co.com.heinsohn.dnda.entidades.Consulta;
import co.com.heinsohn.dnda.entidades.TipoDocumento;

@ManagedBean(name = "consultaJuridicaNoRegistrado")
@ViewScoped
public class ConsultaJuridicaNoRegistrado implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static BASE64Decoder dec=new BASE64Decoder();
	
	public static final String DEFAULT_ENCODING="UTF-8";
	
	private static final Logger log = Logger.getLogger("ConsultaJuridicaNoRegistrado");

	transient private SpringGenericDao springGenericDao;
	
	private JavaMailSenderImpl mailSender;

	/** Tipo de mensaje seleccionado. */
	private String tipoMensajeSel;

	/** La nacionalidad seleccionada en pantalla. */
	private Integer nacionalidadSel;

	/** Los tipos de documento del sistema. */
	private List<SelectItem> tiposDocumento;

	/** El tipo de documento seleccionado en pantalla. */
	private Integer tipoDocumentoSel;

	/** Los paises registrados en el sistema. */
	private List<SelectItem> paises;
	
	/** El pais seleccionado en pantalla. */
	private Integer paisSel;
	
	/** Los departamentos del sistema. */
	private List<SelectItem> departamentos;
	
	/** Departamento seleccionado en pantalla. */
	private String departamentoSel;

	/** Las ciudades del sistema. */
	private List<SelectItem> ciudades;

	/** Ciudad seleccionada en pantalla. */
	private Integer ciudadSel;

	/** Los nombres registrados en pantalla. */
	private String nombres;

	/** El numero de identificaci�n. */
	private String numeroDocumento;

	/** Primer apellido. */
	private String primerApellido;

	/** Segundo apellido. */
	private String segundoApellido;
	
	private String requestQ = "";
	
	private String requestLay = "";

	/** email. */
	private String email;

	/** direccion. */
	private String direccion;

	/** Telfono. */
	private String telefono;
	
	private Integer maxJointFiles = 5;
	
	private Integer maxFileSize = 15728640;

	/** Telfono. */
	private String asunto;

	private String mensajeConsulta;
	
	private List<UploadedFile> archivos;

	private final Map<TipoDocumentoEnum, TipoDocumento> tiposDocMap = new HashMap<TipoDocumentoEnum, TipoDocumento>();

	private List<SelectItem> tramites;

	private Integer tramiteSel;

	private List<SelectItem> clasificaciones;

	private Integer clasificacionSel;
	
	private String instruccion;
	
	private String estado;
	
	private String msgFinal;
	
	private boolean usuarioLogeado = false;
	
	private List<Consulta> consultas;
	
	private Consulta selectedCons;
	
	private Boolean verSoloPqrs = null;
	
	File carpetaAdjuntosLiferay;
	File carpetaRespuestasLiferay;
	File carpetaRespuestas;//Esta carpeta es virtual
	
	Integer COLOMBIA;
	
	private boolean esColombia;
	
	Integer currentUserId = null;
	
	private static final String EXT_DOCS = ".html";
	private static final String EXT_DOCS2 = ".pdf";
	
	private static final String SQL_HISTORICO = "SELECT * FROM vwHistoricoConsultas(?) order by fecha DESC, id DESC";
	
	private static final String SQL_EXISTE_PER_ID = "SELECT * from vwLiferayRelacionAutor(?)";
	
	/*------ */
	private enum PREFS {
		GESTDOC_MAX_ADJ,
		GESTDOC_MAX_TAM_ADJ,
		
		MAIL_GD_BODY_RECI,
		MAIL_GD_ASUNTO_RECI,
		
		MAIL_USER,
		MAIL_PASS,
		MAIL_HOST,
		MAIL_PORT,
		MAIL_PROTOCOL,
		MAIL_AUTH,
		MAIL_TLS,
		
		CONS_DOC_SOLI_INST,
		GESTDOC_DIR,
		CONS_DOC_CARPETA_ADJ,
		
		LIFER_PLID_PQRS
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

	/*--------*/

	@PostConstruct
	public void init() {
		
	}
	
	@Autowired
	@Resource(name="mailSender")
	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}
	
	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		log.info("handleFileUpload!");
		if (archivos == null) {
			archivos = new ArrayList<UploadedFile>();
		}
		
		if (archivos.size() < maxJointFiles) {
            final UploadedFile uploadedFile = event.getFile();
            if (uploadedFile != null) {
                archivos.add(uploadedFile);
                notificarMensaje(FacesMessage.SEVERITY_INFO, event.getFile().getFileName() + " se adjuntó.", 0);
            } else {
            	notificarMensaje(FacesMessage.SEVERITY_WARN, event.getFile().getFileName() + " no se pudo agregar.", 0);
            }
        } else {
        	notificarMensaje(FacesMessage.SEVERITY_WARN, "Solo puede agregar "+maxJointFiles+" archivos", 1);
        }
	}
	
	private void notificarMensaje(Severity severidad, String msg, int tipo) {
		if (tipo == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, "Info", msg));
		} else {
			RequestContext.getCurrentInstance().execute("alert('"+msg+"')");
		}
	}
	
	private boolean isNewRequest() {
        final FacesContext fc = FacesContext.getCurrentInstance();
        final boolean getMethod = true;
        //final boolean getMethod = ((RenderRequest) fc.getExternalContext().getRequest()).getCookies();
        final boolean ajaxRequest = fc.getPartialViewContext().isAjaxRequest();
        final boolean validationFailed = fc.isValidationFailed();
        return getMethod && !ajaxRequest && !validationFailed;
    }
	
	public void preRender() {
		
		if (isNewRequest()) {
			log.info("preRender new");
			estado = "1";
			archivos = new ArrayList<UploadedFile>();
			consultas = new ArrayList<Consulta>();
			nacionalidadSel = null;
			ciudadSel = null;
			nombres = null;
			numeroDocumento = null;
			primerApellido = null;
			segundoApellido = null;
			tiposDocumento = null;
			ciudades = null;
			tramiteSel = null;
			clasificacionSel = null;
			clasificaciones = null;
			mensajeConsulta = "";
			asunto = "";
			cargarPreferencias();
			COLOMBIA = null;
			cargarPaises();
			cargarTiposDocumento();
			// Cargar pais por defecto.
			paisSel = obtenerPaisDefecto("colombia");
			esColombia = true;
			// Cargar nacionalidad por defecto
			nacionalidadSel = paisSel;
			// Como la nacionalidad por defecto es Col, el tipo documento def es CC
			actualizarListaTiposDocumento(paisSel);
			tipoDocumentoSel = (Integer)(TipoDocumentoEnum.CEDULA_CIUDADANIA.darEntidad().getValue());
			// Cargar departamentos por defecto
			cargarDepartamentos(paisSel);
			// Cargar ciudades por defecto
			//cargarCiudades(departamentoSel);
			cargarTramites();
			cargarClasificaciones(tramiteSel);
			
			verSoloPqrs = false;
			usuarioLogeado = false;
			
			try {
				HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				String q = req.getParameter("request_q");
				String LayoutId = req.getParameter("request_layout");
				
				if (q != null) {
					//Verifico si hay usuario logeado
					requestQ = q;
					try {
						q = base64decode(q);
						currentUserId = Integer.parseInt(q);
						List<Map<String, Object>> lectura = springGenericDao.executeQuery(SQL_EXISTE_PER_ID, currentUserId);
						if (lectura != null && lectura.size() > 0) {
							usuarioLogeado = true;
							cargarConsultas();
						} else {
							log.error("Hay usuario logeado pero no hay relación entre Liferay y Autor");
						}
					} catch (Exception e1) {
						log.error("Error capturando usuario: "+e1.getMessage());
					}
				}
				
				if (LayoutId != null) {
					requestLay = LayoutId;
					
					String idPrefe = preferencias.get(PREFS.LIFER_PLID_PQRS);
					log.info("LayoutId="+LayoutId);
					log.info("idPrefe="+idPrefe);
					if (LayoutId.equals(idPrefe)) {
						verSoloPqrs = true;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			log.info("preRender old");
		}
	}

	public static String base64decode(String text){
		try {
			return new String(dec.decodeBuffer( text ), DEFAULT_ENCODING);
		}
		catch ( IOException e ) {
			return null;
		}
	}
	
	public void rehacer() {
		estado = "1";
		asunto = "";
		tramiteSel = null;
		clasificacionSel = null;
		mensajeConsulta = "";
		archivos = new ArrayList<UploadedFile>();
	}
	
	public String convertirRuta(String in) {
		if (in == null || in.trim().length() == 0) {return null;}
		
		File rutaVirtual = new File(in);
		
		String ans =  rutaVirtual.getAbsolutePath();
		
		String carpetaVirtual = carpetaRespuestas.getAbsolutePath();
		if (!carpetaVirtual.endsWith(File.separator)) {
			carpetaVirtual = carpetaVirtual + File.separator;
		}
		
		String carpetaReal = carpetaRespuestasLiferay.getAbsolutePath();
		if (!carpetaReal.endsWith(File.separator)) {
			carpetaReal = carpetaReal + File.separator;
		}
		
		ans = ans.substring(carpetaVirtual.length(), ans.length());
		ans = carpetaReal + ans;
		
		if (ans.toLowerCase().endsWith(EXT_DOCS)) {
			ans = ans.substring(0, ans.length() - EXT_DOCS.length());
			ans = ans + EXT_DOCS2;
		}
		
		if (!new File(ans).exists()) {
			log.error("No se encontró el archivo "+ans);
			return null;
		}
		
		return ans;
	}
	
	public void cargarConsultas() {
		log.info("cargarConsultas!");
		consultas = new ArrayList<Consulta>();
		if (!usuarioLogeado) {return;}
		List<Map<String, Object>> lectura = null;
		
		try {
			lectura = springGenericDao.executeQuery(SQL_HISTORICO, currentUserId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		if (lectura != null) {
			for (Map<String, Object> elem : lectura) {
				java.sql.Timestamp fecha = (java.sql.Timestamp)elem.get("fecha");
				java.sql.Timestamp fechaMax = (java.sql.Timestamp)elem.get("fechaMax");
				Consulta temp = new Consulta(
						(fecha == null ? null : new Date(fecha.getTime())), 
						((String)elem.get("radicado")), 
						((String)elem.get("asunto")), 
						((Integer)elem.get("estado")), 
						((String)elem.get("detalle")), 
						(convertirRuta((String)elem.get("urlAdjunto"))), 
						(fechaMax == null ? null : new Date(fechaMax.getTime())));
				consultas.add(temp);
			}
		}
	}
	
	public boolean isPuedeDescargar() {
		if (selectedCons != null && selectedCons.getUrlAdjunto() != null) {
			File descargable = new File(selectedCons.getUrlAdjunto());
			if (descargable.exists()) {
				return true;
			}
		}
		return false;
	}
	
	public StreamedContent getDescargarArchivo() {
		FileInputStream fis = null;
		try {
			if (selectedCons != null && selectedCons.getUrlAdjunto() != null) {
				File descargable = new File(selectedCons.getUrlAdjunto());
				if (descargable.exists()) {
					// 1. initialize the fileInputStream
					fis = new FileInputStream(descargable);
					
					// 2. get Response
					HttpServletResponse res = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
					res.setHeader("Content-Disposition", "attachment; filename=\""
							+ descargable.getName() + "\"");//
					res.setHeader("Content-Transfer-Encoding", "binary");
					res.setContentType("application/octet-stream");
					res.flushBuffer();

					// 3. write the file into the outputStream
					OutputStream out = res.getOutputStream();
					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = fis.read(buffer)) != -1) {
						out.write(buffer, 0, bytesRead);
						buffer = new byte[4096];
					}

					// 4. return null to this method
					return null;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		return null;
	}

	public boolean isUsuarioLogeado() {
		return usuarioLogeado;
	}

	public void setUsuarioLogeado(boolean usuarioLogeado) {
		this.usuarioLogeado = usuarioLogeado;
	}

	private void cargarPreferencias() {
		List<Map<String, Object>> lectura = null;

		try {
			lectura = springGenericDao.executeQuery(QUERY_PREFS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		preferencias = new HashMap<PREFS, String>();

		if (lectura != null) {
			for (Map<String, Object> elem : lectura) {
				preferencias.put(PREFS.valueOf(elem.get("llave").toString()),
						elem.get("valor").toString());
			}
		}
		
		instruccion = preferencias.get(PREFS.CONS_DOC_SOLI_INST);
		
		carpetaAdjuntosLiferay = new File(preferencias.get(PREFS.CONS_DOC_CARPETA_ADJ));
		carpetaRespuestas = new File(preferencias.get(PREFS.GESTDOC_DIR));
		carpetaRespuestasLiferay = new File(preferencias.get(PREFS.GESTDOC_DIR));
		
		maxJointFiles = Integer.parseInt(preferencias.get(PREFS.GESTDOC_MAX_ADJ));
		maxFileSize = Integer.parseInt(preferencias.get(PREFS.GESTDOC_MAX_TAM_ADJ));
		
		if (!carpetaRespuestasLiferay.exists()) {
			log.error("No se encotró la carpeta de documentos de respuesta "+carpetaRespuestasLiferay.getAbsolutePath());
		}
		
		if (!carpetaAdjuntosLiferay.exists()) {
			log.error("No se encontró la carpeta de adjuntos "+carpetaAdjuntosLiferay.getAbsolutePath());
		}
	}

	/**
	 * Carga los tipos documento utilizados en el sistema.
	 * 
	 * @return Una lista de TipoDocumento con los tipos registrados en el
	 *         sistema, o una lista vac�a en caso de fallo.
	 */
	private void cargarTiposDocumento() {
		List<Map<String, Object>> tiposObject = null;
		try {
			tiposObject = springGenericDao
					.executeQuery("SELECT [TDO_ID], [TDO_NOM], [TDO_ABR] FROM [dbo].[TIPOSDOCUMENTO]");
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			return;
		}
		tiposDocMap.clear();
		if (tiposObject != null) {
			for (Map<String, Object> elem : tiposObject) {
				tiposDocMap.put(
						TipoDocumentoEnum
								.asEnum(elem.get("TDO_ABR").toString()),
						new TipoDocumento(((Integer) elem.get("TDO_ID")).shortValue(), elem.get(
								"TDO_NOM").toString(), elem.get("TDO_ABR")
								.toString()));

			}
		}
	}

	/**
	 * Actualiza la lista desplegable de tipo documento.
	 * 
	 * @param event
	 */
	public void actualizarListaTiposDocumento(ValueChangeEvent event) {
		if (event.getNewValue() == null) {
			return;
		}
		actualizarListaTiposDocumento((Integer) event.getNewValue());
	}

	/**
	 * Actualiza la lista desplegable de tipo documento.
	 * 
	 */
	private void actualizarListaTiposDocumento(Integer nacionalidad) {
		if (nacionalidad == null) {return;}
		if (tiposDocumento == null) {
			tiposDocumento = new ArrayList<SelectItem>();
		}
		tiposDocumento.clear();
		
		if (COLOMBIA.equals(nacionalidad)) {
			tiposDocumento.add(TipoDocumentoEnum.CEDULA_CIUDADANIA.darEntidad());
			tiposDocumento.add(TipoDocumentoEnum.CEDULA_EXTRANJERIA.darEntidad());
		} else {
			tiposDocumento.add(TipoDocumentoEnum.PASAPORTE.darEntidad());
		}
		tipoDocumentoSel = (Integer)tiposDocumento.get(0).getValue();
	}

	/**
	 * Carga los paises utilizados en el sistema.
	 * 
	 * @return Una lista de Paises registrados en el sistema, o una lista vacia
	 *         en caso de fallo.
	 */
	private void cargarPaises() {
		this.paisSel = null;
		if (this.paises == null) {
			this.paises = new ArrayList<SelectItem>();
		}
		this.paises.clear();
		List<Map<String, Object>> paisesObject = null;
		try {
			paisesObject = springGenericDao
					.executeQuery("SELECT [PAI_ID], [PAI_NOM], [PAI_NAC], [PAI_SGL] FROM [dbo].[PAISES] ORDER BY PAI_NOM ASC");
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			return;
		}
		if (paisesObject != null) {
			for (Map<String, Object> elem : paisesObject) {
				SelectItem nuevo = new SelectItem((Integer) elem.get("PAI_ID"), elem.get("PAI_NOM").toString());
				paises.add(nuevo);
				if (COLOMBIA == null && nuevo.getLabel().trim().toLowerCase().startsWith("colombia")) {
					COLOMBIA = (Integer)nuevo.getValue();
				}
			}
		}
	}

	/**
	 * Selecciona un pais por defecto.
	 * 
	 * @param nombre
	 */
	private Integer obtenerPaisDefecto(String nombre) {
		if (nombre == null || nombre.trim().isEmpty() || paises == null) {
			return null;
		}
		for (SelectItem p : paises) {
			if (p.getLabel().trim().toLowerCase().equals(nombre.trim())) {
				return (Integer)p.getValue();
			}
		}
		return null;
	}

	/**
	 * Carga los departamentos teniendo en cuenta el pais seleccionado.<br>
	 * Si el pais seleccionado NO es Colombia, el departamento correspondera al mismo
	 * pais.
	 */
	public void cargarDepartamentos(ValueChangeEvent event) {
		cargarDepartamentos((Integer) event.getNewValue());
	}
	
	/**
	 * Carga las ciudades teniendo en cuenta el pa�s seleccionado.<br>
	 * Si el pais seleccionado NO es Colombia, la ciudad correspondera al mismo
	 * pais.
	 */
	public void cargarCiudades(ValueChangeEvent event) {
		cargarCiudades((String) event.getNewValue());
	}

	/**
	 * Carga los departamentos teniendo en cuenta el pais seleccionado.<br>
	 * Si el pais seleccionado NO es Colombia, el departamento correspondera al mismo
	 * pais.
	 */
	private void cargarDepartamentos(Integer paisSel) {
		log.info("cargarDepartamentos! "+paisSel);
		if (paisSel == null) {
			return;
		}
		ciudadSel = null;
		departamentoSel = null;
		if (departamentos == null) {
			departamentos = new ArrayList<SelectItem>();
		}
		departamentos.clear();
		List<Map<String, Object>> departamentosObject = null;
		try {
			departamentosObject = springGenericDao
					.executeQuery(
							"SELECT [LUG_DEP] FROM [dbo].[LUGAR] WHERE LUG_PAI = ? GROUP BY LUG_DEP ORDER BY LUG_DEP ASC",
							paisSel);
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			return;
		}
		if (departamentosObject != null) {
			for (Map<String, Object> elem : departamentosObject) {
				departamentos.add(new SelectItem(elem.get("LUG_DEP").toString(), elem.get("LUG_DEP").toString()));
			}
		}
	}
	
	/**
	 * Carga las ciudades teniendo en cuenta el pais seleccionado.<br>
	 * Si el pais seleccionado NO es Colombia, la ciudad correspondera al mismo
	 * pais.
	 */
	private void cargarCiudades(String departamentoSel) {
		log.info("cargarCiudades! "+departamentoSel);
		if (departamentoSel == null) {
			return;
		}
		ciudadSel = null;
		if (ciudades == null) {
			ciudades = new ArrayList<SelectItem>();
		}
		ciudades.clear();
		List<Map<String, Object>> ciudadesObject = null;
		try {
			ciudadesObject = springGenericDao
					.executeQuery(
							"SELECT [LUG_ID], [LUG_CIU], [LUG_DEP], [LUG_PAI] FROM [dbo].[LUGAR] WHERE LUG_DEP = ? ORDER BY LUG_CIU ASC",
							departamentoSel);
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			return;
		}
		if (ciudadesObject != null) {
			for (Map<String, Object> elem : ciudadesObject) {
				SelectItem nuevo = new SelectItem((Integer) elem.get("LUG_ID"), elem.get("LUG_CIU").toString());
				ciudades.add(nuevo);
			}
		}
	}

	/**
	 * Envia la consulta preparada por el usuario.
	 */
	public void enviarConsulta() {
		log.info("enviarConsulta!");
		crearEntrada();
	}

	private void crearEntrada() {
		log.info("crearEntrada! archivos = "+(archivos != null ? archivos.size() : "null"));
		
		//Primero se guardan los archivos en una ruta temporal
		String rutaFinalAdjuntos = preferencias.get(PREFS.CONS_DOC_CARPETA_ADJ);
		File raiz = new File(rutaFinalAdjuntos);
		
		List<File> adjuntos;
		
		adjuntos = new ArrayList<File>();
		
		File carpetaTemporal = null;
		if (archivos.size() > 0) {
			carpetaTemporal = new File(raiz, "temp"+new Date().getTime());
			if (!carpetaTemporal.exists() && !carpetaTemporal.mkdirs()) {
				//Si no existe y ademas no se puede crear
				notificarMensaje(FacesMessage.SEVERITY_WARN, "El servidor no tiene la carpeta para adjuntar los archivos.", 0);
				return;
			}
		}
		
		boolean hayError = false;
		
		try {
			//primero guarda los archivos en una carpeta temporal
			for (UploadedFile adjunto : archivos) {
				byte[] input = null;
				input = IOUtils.toByteArray(adjunto.getInputstream());
				File archivoTemporal = new File(carpetaTemporal, adjunto.getFileName());
				FileOutputStream output = new FileOutputStream(archivoTemporal);
				output.write(input);
				
				output.close();
				adjuntos.add(archivoTemporal);
			}
		} catch (Exception e) {
			log.error("Error leyendo los archivos adjuntos "+e.getMessage());
			hayError = true;
		}
		
		if (hayError) {
			notificarMensaje(FacesMessage.SEVERITY_WARN, "Error adjuntando algún archivo, por favor rehacer la solicitud", 0);
			return;
		}
		
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>();

		Map<String, Object> ans = null;
		int i = 0;
		if (!usuarioLogeado) {
			declaredParameters.add(i++, new SqlParameter(Types.SMALLINT));
			declaredParameters.add(i++, new SqlParameter(Types.SMALLINT));
			declaredParameters.add(i++, new SqlParameter(Types.VARCHAR));
			declaredParameters.add(i++, new SqlParameter(Types.VARCHAR));
			declaredParameters.add(i++, new SqlParameter(Types.VARCHAR));
			declaredParameters.add(i++, new SqlParameter(Types.SMALLINT));
			declaredParameters.add(i++, new SqlParameter(Types.VARCHAR));
			declaredParameters.add(i++, new SqlParameter(Types.VARCHAR));
			declaredParameters.add(i++, new SqlParameter(Types.VARCHAR));
			declaredParameters.add(i++, new SqlParameter(Types.SMALLINT));
			declaredParameters.add(i++, new SqlParameter(Types.DATE));
			declaredParameters.add(i++, new SqlParameter(Types.SMALLINT));
			declaredParameters.add(i++, new SqlParameter(Types.VARCHAR));
			declaredParameters.add(i++, new SqlParameter(Types.VARCHAR));
			declaredParameters.add(i++, new SqlParameter(Types.VARCHAR));
			declaredParameters.add(i++, new SqlParameter(Types.SMALLINT));

			declaredParameters.add(i++, new SqlOutParameter("@AHORA", Types.TIMESTAMP));
			declaredParameters.add(i++, new SqlOutParameter("@NRO_RADICADO", Types.VARCHAR));
			declaredParameters.add(i++, new SqlOutParameter("@xERROR", Types.INTEGER));
			declaredParameters.add(i++, new SqlOutParameter("@xMSG", Types.VARCHAR));

			ans = springGenericDao.getJdbcTemplate().call(
					new CallableStatementCreator() {
						@Override
						public CallableStatement createCallableStatement(
								Connection con) throws SQLException {
							CallableStatement stmnt = con.prepareCall("{call dbo.PaAdiConsultaPer(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
							int i = 1;

							stmnt.setObject(i++, tramiteSel);// Tramite
							stmnt.setObject(i++, clasificacionSel);// Clasificacion
							// Asunto personalizado
							stmnt.setObject(i++,
									ConsultaJuridicaNoRegistrado.this.asunto.toUpperCase());
							stmnt.setObject(
									i++,
									ConsultaJuridicaNoRegistrado.this.mensajeConsulta);// Detalle
							// Numero Documento
							stmnt.setObject(
									i++,
									ConsultaJuridicaNoRegistrado.this.numeroDocumento.toUpperCase());
							// Tipo de documento

							stmnt.setObject(i++, tipoDocumentoSel);
							stmnt.setObject(i++,
									ConsultaJuridicaNoRegistrado.this.nombres.toUpperCase());// Nombre
							// Primer apellido
							stmnt.setObject(
									i++,
									ConsultaJuridicaNoRegistrado.this.primerApellido.toUpperCase());
							// Segundo apellido
							stmnt.setObject(
									i++,
									ConsultaJuridicaNoRegistrado.this.segundoApellido.toUpperCase());
							stmnt.setObject(i++, ConsultaJuridicaNoRegistrado.this.nacionalidadSel);// Nacionalidad NO pais
							// Fecha de nacimiento
							stmnt.setObject(i++,
									new java.sql.Date(new Date().getTime()));
							// Lugar de ac se deduce el pais
							stmnt.setObject(i++, ConsultaJuridicaNoRegistrado.this.ciudadSel);
							stmnt.setObject(i++,
									ConsultaJuridicaNoRegistrado.this.direccion.toUpperCase());// Direccion
							stmnt.setObject(i++,
									ConsultaJuridicaNoRegistrado.this.telefono.toUpperCase());// Telfono
							stmnt.setObject(i++,
									ConsultaJuridicaNoRegistrado.this.email.toLowerCase());// Correo
							stmnt.setObject(i++, archivos.size());// Cantidad de adjuntos

							stmnt.registerOutParameter(i++, Types.TIMESTAMP);
							stmnt.registerOutParameter(i++, Types.VARCHAR);
							stmnt.registerOutParameter(i++, Types.INTEGER);
							stmnt.registerOutParameter(i++, Types.VARCHAR);

							return stmnt;
						}
					}, declaredParameters);
		} else {
			declaredParameters.add(i++, new SqlParameter(Types.INTEGER));//userId
			declaredParameters.add(i++, new SqlParameter(Types.SMALLINT));//tramite
			declaredParameters.add(i++, new SqlParameter(Types.SMALLINT));//clasificacion
			declaredParameters.add(i++, new SqlParameter(Types.VARCHAR));//asunto
			declaredParameters.add(i++, new SqlParameter(Types.VARCHAR));//detalle
			declaredParameters.add(i++, new SqlParameter(Types.SMALLINT));//cantidad de adjuntos

			declaredParameters.add(i++, new SqlOutParameter("@AHORA", Types.TIMESTAMP));
			declaredParameters.add(i++, new SqlOutParameter("@NRO_RADICADO", Types.VARCHAR));
			declaredParameters.add(i++, new SqlOutParameter("@xERROR", Types.INTEGER));
			declaredParameters.add(i++, new SqlOutParameter("@xMSG", Types.VARCHAR));
			declaredParameters.add(i++, new SqlOutParameter("@xPERCE1", Types.VARCHAR));
			
			ans = springGenericDao.getJdbcTemplate().call(
					new CallableStatementCreator() {
						@Override
						public CallableStatement createCallableStatement(
								Connection con) throws SQLException {
							CallableStatement stmnt = con.prepareCall("{call dbo.PaAdiConsultaLiferay(?,?,?,?,?,?,?,?,?,?,?)}");
							int i = 1;
							
							stmnt.setObject(i++, currentUserId);// userId
							stmnt.setObject(i++, tramiteSel);// Tramite
							stmnt.setObject(i++, clasificacionSel);// Clasificacion
							stmnt.setObject(i++, ConsultaJuridicaNoRegistrado.this.asunto.toUpperCase());// Asunto personalizado
							stmnt.setObject(i++, ConsultaJuridicaNoRegistrado.this.mensajeConsulta);// Detalle
							stmnt.setObject(i++, archivos.size());// Cantidad de adjuntos

							stmnt.registerOutParameter(i++, Types.TIMESTAMP);
							stmnt.registerOutParameter(i++, Types.VARCHAR);
							stmnt.registerOutParameter(i++, Types.INTEGER);
							stmnt.registerOutParameter(i++, Types.VARCHAR);
							stmnt.registerOutParameter(i++, Types.VARCHAR);

							return stmnt;
						}
					}, declaredParameters);
		}
		

		if (ans != null && ans.containsKey("@xERROR")) {
			Integer error = (Integer) ans.get("@xERROR");
			String mensaje = (String) ans.get("@xMSG");
			String numRadicado = (String) ans.get("@NRO_RADICADO");
			java.sql.Timestamp fecha = (java.sql.Timestamp) ans.get("@AHORA");

			log.info("error=" + error);
			log.info("mensaje=" + mensaje);
			log.info("radicado=" + numRadicado);

			if (error == 0) {
				msgFinal = numRadicado;
				estado = "2";
				
				notificarMensaje(FacesMessage.SEVERITY_INFO, "Su consulta ha sido enviada y recibida con el número de radicado: "+msgFinal, 1);
				
				if (numRadicado != null && fecha != null && raiz.exists()) {
					SimpleDateFormat format = new SimpleDateFormat(
							"/yyyy/MM/dd/");
					File raiz2 = new File(raiz, format.format(fecha));
					// Se corrige porque puede traer espacios
					numRadicado = numRadicado.trim();
					File subcarpeta = new File(raiz2, numRadicado);
					subcarpeta.mkdirs();
					if (subcarpeta.exists()) {
						for (File adjunto : adjuntos) {
							adjunto.renameTo(new File(subcarpeta, adjunto.getName()));
						}
						//elimina la carpeta temporal
						if (carpetaTemporal != null) {
							carpetaTemporal.delete();
						}
						log.info("Se guarda el adjunto en "+ subcarpeta.getAbsolutePath());
					} else {
						log.info("Error de permisos de carpeta para crear subcarpetas");
					}
				}
				
				try {
					
					SimpleDateFormat fechaCorreo = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					
					String template = preferencias.get(PREFS.MAIL_GD_BODY_RECI);
					
					template = template.replace("$fecha", fechaCorreo.format(fecha));
					template = template.replace("$numero", numRadicado);
					
					if (!usuarioLogeado) {
						enviarCorreoElectronico(ConsultaJuridicaNoRegistrado.this.email, template);
					} else {
						String correo = (String) ans.get("@xPERCE1");
						enviarCorreoElectronico(correo, template);
					}
				} catch (Exception e) {
					log.error("Error enviando el correo electrónico "+e.getMessage());
				}
				
			} else {
				notificarMensaje(FacesMessage.SEVERITY_WARN, "Notificar error: " + mensaje, 0);
			}
		} else {
			log.error("Error código: no se encontró el parametro @ERROR o ans es null");
		}
	}
	
	private void enviarCorreoElectronico(String to, String cuerpo) throws Exception {
		
		
		String login = preferencias.get(PREFS.MAIL_USER);
		String asunto = preferencias.get(PREFS.MAIL_GD_ASUNTO_RECI);
		
		String pass = preferencias.get(PREFS.MAIL_PASS);
		String host = preferencias.get(PREFS.MAIL_HOST);
		String protocol = preferencias.get(PREFS.MAIL_PROTOCOL);
		String auth = preferencias.get(PREFS.MAIL_AUTH);
		String tlsEna = preferencias.get(PREFS.MAIL_TLS);
		Integer port = Integer.parseInt(preferencias.get(PREFS.MAIL_PORT));
		
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(login);
		mailSender.setPassword(pass);
		Properties prop = mailSender.getJavaMailProperties();
		prop.put("mail.transport.protocol", protocol);
		prop.put("mail.smtp.auth", auth);
		prop.put("mail.smtp.starttls.enable", tlsEna);
		prop.put("mail.debug", "false");
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage, true);
		
		mailMsg.setFrom(login);
		mailMsg.setTo(to);
		mailMsg.setText(cuerpo, true);
		mailMsg.setSubject(asunto);
		
		mailSender.send(mimeMessage);
	}
	
	/**
	 * Carga los tramites utilizados en el sistema.
	 * 
	 * @return Una lista de Tramite con los tramites registrados en el sistema,
	 *         o una lista vacia en caso de fallo.
	 */
	private void cargarTramites() {
		log.info("cargarTramites!");
		tramiteSel = null;
		List<Map<String, Object>> tiposObject = null;
		try {
			tiposObject = springGenericDao
					.executeQuery("SELECT [TRM_ID], [TRM_NOM], [TRM_LREG], [ESTADO] FROM [dbo].[TRAMITE] WHERE TRM_NOM IN ('PQRS', 'CONSULTAS JURIDICAS')");
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			return;
		}
		if (tramites == null) {
			tramites = new ArrayList<SelectItem>();
		}
		tramites.clear();
		if (tiposObject != null) {
			for (Map<String, Object> elem : tiposObject) {
				SelectItem nuevo = new SelectItem((Integer)elem.get("TRM_ID"), elem.get("TRM_NOM").toString());
				tramites.add(nuevo);
				if (nuevo.getLabel().toLowerCase().equals("pqrs")) {
					tramiteSel = (Integer)nuevo.getValue();
				}
			}
		}
	}

	public void cargarClasificaciones(ValueChangeEvent event) {
		cargarClasificaciones((Integer) event.getNewValue());
	}

	/**
	 * Carga los tramites utilizados en el sistema.
	 * 
	 * @return Una lista de Tramite con los tramites registrados en el sistema,
	 *         o una lista vacia en caso de fallo.
	 */
	private void cargarClasificaciones(Integer tramiteSel) {
		log.info("cargarClasificaciones! "+tramiteSel);
		clasificacionSel = null;
		if (tramiteSel == null) {
			return;
		}
		List<Map<String, Object>> tiposObject = null;
		try {
			tiposObject = springGenericDao
					.executeQuery(
							"SELECT [CLA_ID], [CLA_COD], [CLA_NOM], [CLA_TIP], [CLA_LRTA], [CLA_TIE], "
									+ "[CLA_TIT], [CLA_TRT], [CLA_LREG] FROM [dbo].[CLASIFICACION] WHERE CLA_TRM = ?",
							tramiteSel);
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			return;
		}
		if (clasificaciones == null) {
			clasificaciones = new ArrayList<SelectItem>();
		}
		clasificaciones.clear();
		if (tiposObject != null) {
			for (Map<String, Object> elem : tiposObject) {
				SelectItem nueva = new SelectItem((Integer) elem.get("CLA_ID"), elem.get("CLA_NOM").toString());
				clasificaciones.add(nueva);
			}
		}
	}

	public void actualizarAsunto(ValueChangeEvent event) {
		final Object cla = event.getNewValue();
		if (cla != null) {
			for (SelectItem uno : clasificaciones) {
				if (uno.getValue().equals(cla)) {
					this.asunto = uno.getLabel();
				}
			}
		}
	}

	/*----- GETS AND SETS -----*/

	@Autowired
	@Resource(name = "springGenericDao")
	public void setSpringGenericDao(SpringGenericDao genericDAO) {
		springGenericDao = genericDAO;
	}

	public SpringGenericDao getSpringGenericDao() {
		return springGenericDao;
	}

	public Integer getTipoDocumentoSel() {
		return tipoDocumentoSel;
	}

	public void setTipoDocumentoSel(Integer tipoDocumentoSel) {
		this.tipoDocumentoSel = tipoDocumentoSel;
	}

	public String getTipoMensajeSel() {
		return tipoMensajeSel;
	}
	
	public void setTipoMensajeSel(String tipoMensajeSel) {
		this.tipoMensajeSel = tipoMensajeSel;
	}

	public Integer getPaisSel() {
		return paisSel;
	}

	public void setPaisSel(Integer paisSel) {
		boolean inicial;
		inicial = esColombia;
		boolean ultimo = paisSel != null && paisSel.equals(COLOMBIA);
		if (ultimo != inicial) {ciudadSel = null;}
		setEsColombia(ultimo);
		this.paisSel = paisSel;
	}

	public List<SelectItem> getTiposDocumento() {
		return tiposDocumento;
	}

	public List<SelectItem> getPaises() {
		return paises;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public List<SelectItem> getCiudades() {
		return ciudades;
	}

	public Integer getCiudadSel() {
		return ciudadSel;
	}

	public void setCiudadSel(Integer ciudadSel) {
		this.ciudadSel = ciudadSel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getMensajeConsulta() {
		return mensajeConsulta;
	}

	public void setMensajeConsulta(String mensajeConsulta) {
		this.mensajeConsulta = mensajeConsulta;
	}

	public Integer getNacionalidadSel() {
		return nacionalidadSel;
	}

	public void setNacionalidadSel(Integer nacionalidadSel) {
		this.nacionalidadSel = nacionalidadSel;
	}

	public Map<TipoDocumentoEnum, TipoDocumento> getTiposDocMap() {
		return tiposDocMap;
	}

	public List<SelectItem> getTramites() {
		return tramites;
	}

	public void setTramites(List<SelectItem> tramites) {
		this.tramites = tramites;
	}

	public Integer getTramiteSel() {
		return tramiteSel;
	}

	public void setTramiteSel(Integer tramiteSel) {
		log.info("setTramiteSel!"+tramiteSel);
		this.tramiteSel = tramiteSel;
	}

	public List<SelectItem> getClasificaciones() {
		return clasificaciones;
	}

	public void setClasificaciones(List<SelectItem> clasificaciones) {
		this.clasificaciones = clasificaciones;
	}

	public Integer getClasificacionSel() {
		return clasificacionSel;


	}

	public void setClasificacionSel(Integer clasificacionSel) {
		this.clasificacionSel = clasificacionSel;


	}
	public List<UploadedFile> getArchivos() {
		return archivos;
	}

	public void setArchivos(List<UploadedFile> archivos) {
		this.archivos = archivos;
	}

	public String getInstruccion() {
		return instruccion;
	}

	public void setInstruccion(String instruccion) {
		this.instruccion = instruccion;
	}

	public Integer getMaxJointFiles() {
		return maxJointFiles;
	}

	public void setMaxJointFiles(Integer maxJointFiles) {
		this.maxJointFiles = maxJointFiles;
	}

	public Integer getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(Integer maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	
	public String getMsgTamanioMax() {
		return "Sobrepasó el tamaño máximo del archivo ("+maxFileSize/(1024*1024)+"MB)";
	}
	
	public String getMsgInstruccionAdjunto() {
		return "A continuación puede adjuntar "+maxJointFiles+" archivos de máximo "+maxFileSize/(1024*1024)+"MB cada uno. Actualmente tiene "+(archivos == null ? "0" : archivos.size())+" archivos. IMPORTANTE: Debe adjuntarlos después de buscarlos, de lo contrario no se enviarón.";
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMsgFinal() {
		return msgFinal;
	}

	public void setMsgFinal(String msgFinal) {
		this.msgFinal = msgFinal;
	}

	public List<Consulta> getConsultas() {
		return consultas;
	}

	public void setConsultas(List<Consulta> consultas) {
		this.consultas = consultas;
	}

	public Consulta getSelectedCons() {
		return selectedCons;
	}

	public void setSelectedCons(Consulta selectedCons) {
		this.selectedCons = selectedCons;
	}
	
	public List<SelectItem> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<SelectItem> departamentos) {
		this.departamentos = departamentos;
	}

	public String getDepartamentoSel() {
		return departamentoSel;
	}

	public void setDepartamentoSel(String departamentoSel) {
		log.info("setDepartamentoSel!");
		this.departamentoSel = departamentoSel;
	}

	public Boolean getVerSoloPqrs() {
		return verSoloPqrs;
	}

	public void setVerSoloPqrs(Boolean verSoloPqrs) {
		this.verSoloPqrs = verSoloPqrs;
	}

	/**
	 * @return the esColombia
	 */
	public boolean isEsColombia() {
		return esColombia;
	}

	/**
	 * @param esColombia the esColombia to set
	 */
	public void setEsColombia(boolean esColombia) {
		log.info("esColombia="+esColombia);
		this.esColombia = esColombia;
	}

	/**
	 * @return the requestQ
	 */
	public String getRequestQ() {
		return requestQ;
	}

	/**
	 * @param requestQ the requestQ to set
	 */
	public void setRequestQ(String requestQ) {
		this.requestQ = requestQ;
	}

	/**
	 * @return the requestLay
	 */
	public String getRequestLay() {
		return requestLay;
	}

	/**
	 * @param requestLay the requestLay to set
	 */
	public void setRequestLay(String requestLay) {
		this.requestLay = requestLay;
	}
}
