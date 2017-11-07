package co.com.heinsohn.dnda.documents;

import java.awt.Dimension;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.mail.internet.MimeMessage;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.businesslogic.services.impl.ServiceMailSenderImpl;
import la.netco.core.businesslogic.services.impl.SpringApplicationContext;
import la.netco.core.persistencia.dao.UsuariosDao;
import la.netco.core.persistencia.vo.FirmaUsuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.persistencia.dto.commons.Entrada;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4PageMark;

import com.google.common.base.Throwables;

@ManagedBean(name="treeBean")
@ViewScoped
public class TreeBean implements Serializable {

	private static Logger log = Logger.getLogger(TreeBean.class.getSimpleName());

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_PBE_BYTE_ENCRYPTOR)
	private transient StandardPBEByteEncryptor jasyptPBEByteEncryptor;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	private transient ServiceDao daoServicio;
	
	@ManagedProperty(value = "#{mailService}")
	private transient ServiceMailSenderImpl mailService;

	private String ARCHIVO_NOMBRE_PREDEF = "";
	
	private static final long serialVersionUID = 3350653785168926842L;
	private TreeNode root;
	private TreeNode selectedNode = null;
	private String selectedNodePath;
	private String archivoNombre;
	private String archivoNombreTemplate;
	private String cuerpoDocumento;
	private String mensaje;
	HashMap<PREFS, String> preferencias;

	private StreamedContent archivoPdf;
	private String templateSeleccionado;
	private List<String> templates;
	private boolean configurado;
	
	private List<String> entradasParaSalSel;
	private List<SelectItem> entradasParaSalida;
	private String textoBuscEntNen;
	private String msgMultipleEntrada;

	private Entrada entrada = null;

	private enum PREFS {
		MAIL_USER,
		MAIL_PASS,
		MAIL_HOST,
		MAIL_PORT,
		MAIL_PROTOCOL,
		MAIL_AUTH,
		MAIL_TLS,
		
		GESTDOC_DIR,
		GESTDOC_HEADER,
		GESTDOC_FOOTER,
		GESTDOC_HTML_HEAD,
		GESTDOC_HTML_IDSALIDA,
		GESTDOC_HTML_FOOT,
		GESTDOC_HTML_FIRMA,
		GESTDOCTEMPL_DIR;

	};

	private static final String EXT_HTML = ".html";
	private static final String EXT_PDF = ".pdf";
	private static final Pattern PAT_HTML_HEAD = Pattern.compile("^(.)*(<body)(.)*?(>)", Pattern.DOTALL);
	private static final Pattern PAT_HTML_FOOT = Pattern.compile("(</body>)(.)*$", Pattern.DOTALL);
	private static final Pattern PAT_EXT_HTML = Pattern.compile("\\.(.)*$", Pattern.CASE_INSENSITIVE);

	//Archivo digital
	private static Integer ANEXO_ID = 43;

	private static String QUERY_PREFS;

	static {
		QUERY_PREFS = "SELECT llave, valor from Preferencias WHERE llave in (";
		for (PREFS pref : PREFS.values()) {
			QUERY_PREFS += "'"+pref+"', ";
		}
		QUERY_PREFS = QUERY_PREFS.substring(0, QUERY_PREFS.length()-2);
		QUERY_PREFS += ") AND ESTADO = 1";
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}
	
	public void comenzarSalida() {
		entradasParaSalida = new ArrayList<SelectItem>();
		String nen = entrada.getEntNen().trim();
		entradasParaSalida.add(new SelectItem(nen, nen+" - "+entrada.getEntAsu()));
	}
	
	public String darListaEntradas() {
		String ans = "";
		for (SelectItem e : entradasParaSalida) {
			ans+=e.getValue()+"|";
		}
		return ans;
	}
	
	public void agregarEntrada() {
		//Si ya esta no se debe agregar
		textoBuscEntNen = textoBuscEntNen.trim();
		for (SelectItem ent : entradasParaSalida) {
			if (ent.getValue().equals(textoBuscEntNen)) {
				msgMultipleEntrada = "La entrada "+textoBuscEntNen+" ya está agregada.";
				return;
			}
		}
		
		//Se debe buscar
		String query = "select ent_asu, ent_lcon from entrada where ent_nen = ?";
		//Se debe verificar que no est ya contestada
		
		List<Map<String, Object>> lectura = null;
		lectura = daoServicio.getSpringGenericDao().executeQuery(query, textoBuscEntNen);
		if (lectura != null && lectura.size() > 0) {
			
			boolean agregada = false;
			for (Map<String, Object> elem : lectura) {
				String asunto = (String)elem.get("ent_asu");
				Boolean contestada = (Boolean)elem.get("ent_lcon");
				if (contestada == false) {
					entradasParaSalida.add(new SelectItem(textoBuscEntNen, textoBuscEntNen+" - "+asunto));
					msgMultipleEntrada = "";
					agregada = true;
				}
				
				if (!agregada) {
					msgMultipleEntrada = "La entrada ya est� contestada";
				}
			}
		} else {
			msgMultipleEntrada = "No existe entrada con radicado "+textoBuscEntNen;
		}
	}
	
	public void borrarEntradas() {
		//No se puede borrar la entrada actual
		msgMultipleEntrada = "";
		for (String id : entradasParaSalSel) {
			for (SelectItem ent : entradasParaSalida) {
				if (ent.getValue().toString().equals(id)) {
					if (id.trim().equals(entrada.getEntNen().trim())) {
						msgMultipleEntrada = "No se puede borrar la entrada "+entrada.getEntNen().trim();
					} else {
						entradasParaSalida.remove(ent);
						break;
					}
				}
			}
		}
	}

	public List<String> leerAnexoExpediente(Integer ent_id) {
		List<String> ans;
		ans = new ArrayList<String>();
		String query = "SELECT exa.exa_url as url "+
				"FROM EXPEDIENTEANEXO exa "+
				"INNER JOIN EXPEDIENTE expe "+
				"ON expe.exp_id = EXA_EXP "+
				"INNER JOIN EXPEDIENTECORRESPOND expcor "+
				"ON expcor.exc_exp = expe.exp_id "+
				"WHERE expcor.EXC_ENT = ? and exa.EXA_ANX = ?";
		List<Map<String, Object>> lectura = null;
		lectura = daoServicio.getSpringGenericDao().executeQuery(query, ent_id, ANEXO_ID);
		for (Map<String, Object> elem : lectura) {
			if (elem.get("url") == null) {
				ans.add(null);
			} else {
				ans.add((elem.get("url").toString()).trim());
			}
		}
		return ans;
	}

	public void crearRemplazarAnexoExpediente(final Integer ent_id, final String url) {
		
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>();
		int i = 0;
		
		declaredParameters.add(i++, new SqlParameter(Types.INTEGER));
		declaredParameters.add(i++, new SqlParameter(Types.VARCHAR));
		declaredParameters.add(i++, new SqlOutParameter("@xERROR", Types.INTEGER));
		declaredParameters.add(i++, new SqlOutParameter("@xMSG", Types.VARCHAR));
		
		Map<String, Object> ans = null;
		
		ans = daoServicio.getSpringGenericDao().getJdbcTemplate().call(
				new CallableStatementCreator() {
					@Override
					public CallableStatement createCallableStatement(
							Connection con) throws SQLException {
						CallableStatement stmnt = con.prepareCall("{call dbo.PaAsocUrlEntrada(?,?,?,?)}");
						int i = 1;

						stmnt.setObject(i++, ent_id);// Entrada
						stmnt.setObject(i++, url);// Url
						
						stmnt.registerOutParameter(i++, Types.INTEGER);
						stmnt.registerOutParameter(i++, Types.VARCHAR);

						return stmnt;
					}
				}, declaredParameters);
		
		if (ans != null && ans.containsKey("@xERROR")) {
			Integer error = (Integer) ans.get("@xERROR");
			String mensaje = (String) ans.get("@xMSG");
			
			if (error > 0) {
				notificarMensaje(FacesMessage.SEVERITY_WARN, "Error: "+mensaje, 0);
			}
		} else {
			log.severe("Parece que el SP PaAsocUrlEntrada no existe");
		}
	}
	
	public void generarSalida() {
		log.info("generar salida!");

		//1. Invocar un procedimiento almacenado para crear la salida asociada a la entrada
		int i = 0;
		List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>();

		declaredParameters.add(i++, new SqlParameter(Types.VARCHAR));

		declaredParameters.add(i++, new SqlOutParameter("@xPERCE1", Types.VARCHAR));
		declaredParameters.add(i++, new SqlOutParameter("@xENT_ASU", Types.VARCHAR));
		declaredParameters.add(i++, new SqlOutParameter("@xSAL_OBS", Types.VARCHAR));
		declaredParameters.add(i++, new SqlOutParameter("@xSAL_ID", Types.VARCHAR));
		declaredParameters.add(i++, new SqlOutParameter("@xSAL_DEPTO", Types.VARCHAR));
		declaredParameters.add(i++, new SqlOutParameter("@xERROR", Types.INTEGER));
		declaredParameters.add(i++, new SqlOutParameter("@xMSG", Types.VARCHAR));

		Map<String, Object> ans = null;
		
		try {
			ans = daoServicio.getSpringGenericDao().getJdbcTemplate().call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement stmnt = con.prepareCall("{call dbo.PaResponderConsulta(?,?,?,?,?,?,?,?)}");
					int i = 1;
	
					stmnt.setObject(i++, darListaEntradas());
	
					stmnt.registerOutParameter(i++, Types.VARCHAR);
					stmnt.registerOutParameter(i++, Types.VARCHAR);
					stmnt.registerOutParameter(i++, Types.VARCHAR);
					stmnt.registerOutParameter(i++, Types.VARCHAR);
					stmnt.registerOutParameter(i++, Types.VARCHAR);
					stmnt.registerOutParameter(i++, Types.INTEGER);
					stmnt.registerOutParameter(i++, Types.VARCHAR);
	
					return stmnt;
				}
			}, declaredParameters);
		} catch (Exception e) {
			notificarMensaje(FacesMessage.SEVERITY_WARN, "Error "+e.getMessage(), 0);
		}

		if (ans != null) {
			if (ans.containsKey("@xPERCE1") && ans.containsKey("@xENT_ASU")) {
				String correos = (String) ans.get("@xPERCE1");
				String asunto = (String) ans.get("@xENT_ASU");
				String cuerpo = (String) ans.get("@xSAL_OBS");
				String id = (String) ans.get("@xSAL_ID");
				String depto = (String) ans.get("@xSAL_DEPTO");
				Integer error = (Integer) ans.get("@xERROR");
				String mensaje = (String) ans.get("@xMSG");
				
				
				
				if (error == 0) {
					notificarMensaje(FacesMessage.SEVERITY_INFO, "Se asoció la salida a la entrada", 0);
					
					//2. Asegurar la existencia del archivo pdf
					File file = generarPdfSolo(id, depto);
					
					if (file == null) {return;}
					
					//3. Enviar el correo electrnico al destinatario
					String correo = "";
					
					MimeMessage mimeMessage = mailService.getMailSender().createMimeMessage();
					
					String login = preferencias.get(PREFS.MAIL_USER);
					String pass = preferencias.get(PREFS.MAIL_PASS);
					String host = preferencias.get(PREFS.MAIL_HOST);
					String protocol = preferencias.get(PREFS.MAIL_PROTOCOL);
					String auth = preferencias.get(PREFS.MAIL_AUTH);
					String tlsEna = preferencias.get(PREFS.MAIL_TLS);
					Integer port = Integer.parseInt(preferencias.get(PREFS.MAIL_PORT));
					
					mailService.getMailSender().setHost(host);
					mailService.getMailSender().setPort(port);
					mailService.getMailSender().setUsername(login);
					mailService.getMailSender().setPassword(pass);
					Properties prop = mailService.getMailSender().getJavaMailProperties();
					prop.put("mail.transport.protocol", protocol);
					prop.put("mail.smtp.auth", auth);
					prop.put("mail.smtp.starttls.enable", tlsEna);
					prop.put("mail.debug", "false");
					
					List<String> correos2 = partirTextoNoDupli(correos, "\\|");

					//Se envia correo a todos los destinatarios
					for (int j=0; j<correos2.size(); j++){
						try {
							correo = correos2.get(j);
							MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage, true);
							mailMsg.setFrom(login);
							mailMsg.setTo(correo);
							mailMsg.setSubject(asunto);
							mailMsg.setText(cuerpo, true);
							//FileSystemResource object for Attachment
							FileSystemResource adjunto = new FileSystemResource(file);
							mailMsg.addAttachment(file.getName(), adjunto);
							mailService.getMailSender().send(mimeMessage);
							notificarMensaje(FacesMessage.SEVERITY_INFO, "Se envió el correo a "+correo, 0);
						} catch (Exception e) {
							log.info(Throwables.getStackTraceAsString(e));
							notificarMensaje(FacesMessage.SEVERITY_WARN, "No se pudo enviar el correo electrónico a "+correo, 0);
						}
					}
					
				} else {
					notificarMensaje(FacesMessage.SEVERITY_WARN, "Error código "+error+" al ejecutar el procedimiento."+(mensaje != null ? " "+mensaje : ""), 0);
				}
			} else {
				notificarMensaje(FacesMessage.SEVERITY_WARN, "El procedimiento no retornó valores", 0);
			}
		}
	}
	
	private static List<String> partirTextoNoDupli(String texto, String delim) {
		List<String> correos2 = new ArrayList<String>();
		if (texto==null || texto.trim().length() == 0) {return correos2;}
		
		String[] arr = texto.split(delim);
		
		for (String parte : arr) {
			if (parte.trim().length() > 0 && !correos2.contains(parte)) {
				correos2.add(parte);
			}
		}
		
		return correos2;
	}

	@PostConstruct
	public void init() {
		try {
			
			entradasParaSalida = new ArrayList<SelectItem>();
			entradasParaSalSel = new ArrayList<String>();
			
			log.info("@PostConstruct");
			List<Map<String, Object>> lectura = null;
	
			try {
				if (daoServicio != null) {
					lectura = daoServicio.getSpringGenericDao().executeQuery(QUERY_PREFS);
				}
			} catch (Exception e) {
				log.info(Throwables.getStackTraceAsString(e));
			}
	
			templates = new ArrayList<String>();
			preferencias = new HashMap<PREFS, String>();
	
			if (lectura != null) {
				for (Map<String, Object> elem : lectura) {
					preferencias.put(PREFS.valueOf(elem.get("llave").toString()), elem.get("valor").toString());
				}
			}
	
			setSelectedNodePath("Seleccione una carpeta...");
			archivoNombre = ARCHIVO_NOMBRE_PREDEF;
	
			configurado = true;
	
			for (PREFS pref : PREFS.values()) {
				if (!preferencias.containsKey(pref)) {
					log.info("Falta configurar la preferencia "+pref.name());
					notificarMensaje(FacesMessage.SEVERITY_WARN, "Falta configurar la preferencia "+pref.name(), 1);
					configurado = false;
				}
			}
	
			if (configurado) {
				String rutaRaiz = preferencias.get(PREFS.GESTDOC_DIR);
				File carpetaRaiz = new File(rutaRaiz);
				if (!carpetaRaiz.exists()) {
					log.info("ERROR: La carpeta "+rutaRaiz+" no existe!!!!");
				}
				root = new DefaultTreeNode(new Elemento(rutaRaiz, preferencias.get(PREFS.GESTDOC_DIR), true), null);
				agregarHijos((DefaultTreeNode)root);
			}
		} catch (Exception e) {
			log.severe("Error en init "+e.getMessage());
		}
	}

	public void firmar() {
		try {
			String logedUser = UserDetailsUtils.usernameLogged();
			if (logedUser == null) {return;}
			String imagen = generarImagenFirmaBase64(logedUser); 
			if (imagen == null) {
				notificarMensaje(FacesMessage.SEVERITY_WARN, "El usuario actual no tiene definida la firma", 0);
				return;
			}

			UsuariosDao usuariosDao = (UsuariosDao)SpringApplicationContext.getBean("usuariosDao");
			Object[] datos = (Object[])usuariosDao.findDatosByUsername(logedUser);
			String nombre = datos[0]+" "+datos[1]+(datos[2] == null ? "" : " "+datos[2]);

			String campoFirma;
			campoFirma = preferencias.get(PREFS.GESTDOC_HTML_FIRMA).replace("$img", imagen);
			campoFirma = campoFirma.replace("$espacios", StringUtils.repeat("_", (int)(nombre.length()*1.5)));
			campoFirma = campoFirma.replace("$nombres", nombre);
			limpiar();
			cuerpoDocumento = cuerpoDocumento + campoFirma;
		} catch (Exception e) {
			notificarMensaje(FacesMessage.SEVERITY_WARN, "Error:"+e.getMessage(), 0);
		}
	}

	public File generarPdfSolo(String idSalida, String depto) {
		log.info("generarPdfSolo");
		
		if (selectedNode == null) {
			if (!navegarHasta((DefaultTreeNode)root, selectedNodePath, false)) {
				notificarMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar una carpeta", 0);
				return null;
			}
		}
		if (archivoNombre == null || archivoNombre.trim().length() == 0 ) {
			notificarMensaje(FacesMessage.SEVERITY_WARN, "Debe asignar el nombre del archivo", 0);
			return null;
		}
		
		if (!guardar(1)) {return null;}
		
		File carpetaDestino = new File(((Elemento)selectedNode.getData()).path);
		File file = new File(carpetaDestino, eliminarExtension(archivoNombre)+EXT_PDF);
		file.delete();
		try {
			limpiar();
			String texto = cuerpoDocumento;
			texto = texto.replaceAll("(<br/>)", "&nbsp;<br/>");
			
			texto = generarSelloSalida(idSalida, depto)+texto;
			
			String rutaConpagina = "<p style=\"width: 100%;\">"+StringEscapeUtils.escapeHtml(file.getAbsolutePath())+"</p><p style=\"width: 100%; text-align: center;\">P&aacute;gina $[page] de $[total]</p>";
			
			convertirAPDFPD4ML(preferencias.get(PREFS.GESTDOC_HTML_HEAD)+texto+preferencias.get(PREFS.GESTDOC_HTML_FOOT), file, preferencias.get(PREFS.GESTDOC_HEADER), rutaConpagina+preferencias.get(PREFS.GESTDOC_FOOTER));
			notificarMensaje(FacesMessage.SEVERITY_INFO, "Archivo pdf generado exitosamente", 0);
			return file;
		} catch (IOException e) {
			notificarMensaje(FacesMessage.SEVERITY_WARN, "Error:"+e.getMessage(), 0);
		}
		return null;
	}

	private String generarSelloSalida(String idSalida, String departamento) {
		String template;
		
		template = preferencias.get(PREFS.GESTDOC_HTML_IDSALIDA);
		
		template = template.replace("$idSalida", idSalida);
		
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy h:mm a");

		template = template.replace("$fecha", format.format(new Date()).toUpperCase());
		
		template = template.replace("$depto", departamento);
		
		return template;
	}

	public void generarPdf() {
		log.info("generarPdf");

		File file = generarPdfSolo("X-XXXX-XX", "UNIDAD DE SISTEMAS");
		if (file != null) {
			try {
				setArchivoPdf(new DefaultStreamedContent(new FileInputStream(file), "application/pdf", file.getName()));
			} catch (FileNotFoundException e) {

			}
		} else {
			notificarMensaje(FacesMessage.SEVERITY_WARN, "No se gener� el archivo pdf", 1);
		}
	}

	public static void convertirAPDFPD4ML(String texto, File archivoPDF, String textoHeader, String textoFooter) throws IOException {
		Dimension format = PD4Constants.LETTER;
		boolean landscapeValue = false;
		int topValue = 5;
		int leftValue = 10;
		int rightValue = 20;
		int bottomValue = 10;
		String unitsValue = "mm";
		int userSpaceWidth = 900;

		java.io.FileOutputStream fos = new java.io.FileOutputStream(archivoPDF);

		try {
			PD4ML pd4ml = new PD4ML();

			//Se agrega el pie
			PD4PageMark footer = new PD4PageMark();  
			footer.setHtmlTemplate(textoFooter);  
			footer.setAreaHeight(-1);
			pd4ml.setPageFooter(footer);

			//Se agrega el encabezado
			PD4PageMark header = new PD4PageMark();  
			header.setHtmlTemplate(textoHeader);  
			header.setAreaHeight(-1);
			pd4ml.setPageHeader(header);

			try {                                                              
				pd4ml.setPageSize( landscapeValue ? pd4ml.changePageOrientation( format ): format );
			} catch (Exception e) {
				e.printStackTrace();
			}

			if ( unitsValue.equals("mm") ) {
				pd4ml.setPageInsetsMM( new Insets(topValue, leftValue,bottomValue, rightValue) );
			} else {
				pd4ml.setPageInsets( new Insets(topValue, leftValue,bottomValue, rightValue) );
			}
			pd4ml.setHtmlWidth( userSpaceWidth );
			StringReader reader = new StringReader(texto);
			pd4ml.render( reader, fos );
		} catch (Exception e) {
			throw e;
		} finally {
			fos.close();
		}


	}

	private String generarImagenFirmaBase64(String logedUser) {
		try {

			if (logedUser != null) {
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, logedUser);
				FirmaUsuario firma = (FirmaUsuario) daoServicio.getGenericCommonDao().executeUniqueResult(FirmaUsuario.NAMED_QUERY_FIND_BY_LOGIN, params);
				if (firma != null) {
					byte[] imageByteArray = firma.getFirmaStream();
					StringBuilder sb = new StringBuilder();
					sb.append("data:"+firma.getMimeType()+";base64,");
					sb.append(imageToBase64(imageByteArray));
					String firmaTexto = sb.toString();
					return firmaTexto;
				}
			}

		} catch (Exception e) {
			log.severe(e.getMessage());
		}
		return null;
	}

	public void actualizarListaTemplates()  {
		File carpetaTemplates = carpetaTemplatesValida();
		if (carpetaTemplates == null) return;

		File[] archivos = carpetaTemplates.listFiles();
		templates.clear();
		for (File archivo : archivos) {
			if (archivo.isFile() && archivo.getName().toLowerCase().endsWith(EXT_HTML)) {
				templates.add(archivo.getName());
			}
		}
		Collections.sort(templates);
	}

	public void setup(Entrada existente) {
		log.info("setup");
		entrada = existente;
		String rutaCarpeta = null;
		try {
			List<String> anexosExpediente = leerAnexoExpediente(entrada.getEntId());
			log.info("anexosExpediente.size()="+anexosExpediente.size());
			if (anexosExpediente.size() > 0) {
				File ruta = new File(anexosExpediente.get(0));
				rutaCarpeta = ruta.getParentFile().getAbsolutePath();
				archivoNombre = eliminarExtension(ruta.getName());
			} else {
				String raizPath = preferencias.get(PREFS.GESTDOC_DIR);
				//Date fecha = existente.getEntFre();
				//String numRegistro = existente.getEntNen();
				File raiz = new File(raizPath);
				rutaCarpeta = raiz.getAbsolutePath();
				archivoNombre = ARCHIVO_NOMBRE_PREDEF;
				/*
				if (fecha != null && numRegistro != null) {
					numRegistro = numRegistro.trim();//Se corrige porque trae espacios!!!
					SimpleDateFormat format = new SimpleDateFormat("/yyyy/MM/dd/");
					File carpetaEntradaBase = new File(raiz, format.format(fecha));
					File carpetaEntrada = new File(carpetaEntradaBase, numRegistro);
					if ((carpetaEntrada.exists() && carpetaEntrada.isDirectory()) || carpetaEntrada.mkdirs()) {
						log.info("carpeta existe o es creada");
						rutaCarpeta = carpetaEntrada.getAbsolutePath();
						archivoNombre = "Respuesta-"+numRegistro;
					}
				}
				*/
			}
			log.info("setup archivoNombre = "+archivoNombre);
			log.info("setup rutaCarpeta = "+rutaCarpeta);
			if (rutaCarpeta != null) {
				setSelectedNodePath(rutaCarpeta);				
				abrirArchivo();
			}
		} catch (Exception e) {
			log.severe(e.getMessage());
		}
	}

	private void leerArchivo(File archivo) {
		log.info("leerArchivo");
		if (archivo.exists() && archivo.isFile()) {
			BufferedReader br = null;
			cuerpoDocumento = "";
			try {
				String sCurrentLine;
				br = new BufferedReader(new FileReader(archivo));
				while ((sCurrentLine = br.readLine()) != null) {
					cuerpoDocumento+=sCurrentLine;
				}

				cuerpoDocumento = PAT_HTML_HEAD.matcher(cuerpoDocumento).replaceAll("");
				cuerpoDocumento = PAT_HTML_FOOT.matcher(cuerpoDocumento).replaceAll("");

				limpiar();
			} catch (IOException e) {
				log.severe(e.getMessage());
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					log.severe(ex.getMessage());
					ex.printStackTrace();
				}
			}
		} else {
			cuerpoDocumento = "";
		}
	}

	private void abrirArchivo() {
		if (selectedNodePath != null) {
			if (navegarHasta((DefaultTreeNode)root, selectedNodePath, false)) {
				File raiz = new File(selectedNodePath);
				if (raiz.exists() && raiz.isDirectory()) {
					File archivo = new File(raiz, eliminarExtension(archivoNombre)+EXT_HTML);
					log.info("archivo="+archivo.getAbsolutePath());
					leerArchivo(archivo);
				}
			} else {
				log.info("El archivo no se puede abrir porque no está dentro de la ruta parametrizada");
			}
		} 
	}

	private boolean navegarHasta(DefaultTreeNode padre, String ruta, boolean recargarHijos) {

		Elemento data = (Elemento)padre.getData();
		File origen = new File(data.path);
		File destino = new File(ruta);

		if (!destino.getAbsolutePath().startsWith(origen.getAbsolutePath())) {
			return false;
		}

		if (!destino.exists() || !destino.isDirectory()) {
			destino.mkdirs();
		}

		if (origen.getAbsolutePath().equals(destino.getAbsolutePath())) {
			selectedNode = padre;
			log.info("selectedNode es "+selectedNode);
			//Se valida si se debe recargar los hijos a este nivel
			if (recargarHijos) {
				log.info("Recargar hijos de  "+data);
				agregarHijos((DefaultTreeNode)selectedNode);
			}
			return true;
		} else {
			//Es una ruta hija
			File[] children = origen.listFiles();
			for (File child : children) {
				if (destino.getAbsolutePath().startsWith(child.getAbsolutePath())) {
					Elemento dato;
					dato = new Elemento(child.getAbsolutePath(), child.getName(), true);
					DefaultTreeNode nuevo;
					nuevo = nodeFindChild(padre, dato);
					if (nuevo == null) {
						nuevo = new DefaultTreeNode(dato, null);
						padre.getChildren().add(nuevo);
					}
					return navegarHasta(nuevo, ruta, recargarHijos);
				}
			}
		}
		return false;
	}
	
	private DefaultTreeNode nodeFindChild(DefaultTreeNode padre, Elemento nuevo) {
		List<TreeNode> lista = padre.getChildren();
		for (TreeNode elem : lista) {
			if (elem.getData().equals(nuevo)) {
				return (DefaultTreeNode)elem;
			}
		}
		return null;
	}

	private void agregarHijos(DefaultTreeNode padre) {
		File d = new File(((Elemento)padre.getData()).path);
		DefaultTreeNode temp;
		if(d.isDirectory()) {
			padre.getChildren().clear();
			File[] children = d.listFiles();
			if (children != null) {
				for(File child : children) {
					if (!child.isHidden()) {
						if(child.isDirectory()) {
							temp = new DefaultTreeNode(new Elemento(child.getAbsolutePath(), child.getName(), true), null);
							padre.getChildren().add(temp);
						} else {
							temp = new DefaultTreeNode("file", new Elemento(child.getAbsolutePath(), child.getName(), false), null);
							padre.getChildren().add(temp);
						}
					}
				}
			}
		}
	}

	public void onNodeExpand(NodeExpandEvent event) {

	}

	public void notificarMensaje(Severity severidad, String msg, int tipo) {
		mensaje = msg;
		if (tipo == 0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidad, "Info", mensaje));
		} else {
			RequestContext.getCurrentInstance().execute("alert('"+msg+"')");
		}


	}

	public void aceptar() {
		RequestContext requestContext = RequestContext.getCurrentInstance();
		if (selectedNode == null) {
			notificarMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar una carpeta", 0);
		} else {
			requestContext.execute("treeover.hide();");
		}
	}

	public void cancelar() {

	}

	public void onNodeSelect(NodeSelectEvent event) {
		selectedNode = event.getTreeNode();
		File d = new File(((Elemento)selectedNode.getData()).path);
		if (d.isDirectory()) {
			//Solo funciona cuando se selecciona la carpeta
			agregarHijos((DefaultTreeNode)selectedNode);
			Elemento elem = ((Elemento)selectedNode.getData());
			selectedNodePath = elem.path;
		}
	}

	private File carpetaTemplatesValida() {
		String carpeta = preferencias.get(PREFS.GESTDOCTEMPL_DIR);
		File carpetaTemplates = new File(carpeta);
		if (!carpetaTemplates.exists()) {
			if (!carpetaTemplates.mkdirs()) {
				notificarMensaje(FacesMessage.SEVERITY_WARN, "No se pudo crear la carpeta de plantillas", 0);
				return null;
			}
		}
		return carpetaTemplates;
	}
	
	public void copiarTemplate(boolean reescribir) {

		File carpetaTemplates = carpetaTemplatesValida();
		if (carpetaTemplates == null) return;

		if (archivoNombreTemplate == null || archivoNombreTemplate.trim().length() == 0) {
			notificarMensaje(FacesMessage.SEVERITY_WARN, "El nombre del archivo de la plantilla no puede ser vacio", 0);
			return;
		}

		File file = new File(carpetaTemplates, eliminarExtension(archivoNombreTemplate)+EXT_HTML);
		if (file.exists() && file.isFile() && !reescribir) {
			notificarMensaje(FacesMessage.SEVERITY_WARN, "Ya existe una plantilla con ese nombre; cambie el nombre.", 0);
			RequestContext requestContext = RequestContext.getCurrentInstance();
			requestContext.execute("dlgReescribir.show();");
			return;
		} else {
			if (guardar(file, true)) {
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.execute("dlgTemplate.hide();");
			}
		}
	}

	public void cargarTemplate() {
		File carpetaTemplates = carpetaTemplatesValida();
		if (carpetaTemplates == null) return;

		File template = new File(carpetaTemplates, templateSeleccionado);

		if (!template.exists() || !template.isFile()) {
			notificarMensaje(FacesMessage.SEVERITY_WARN, "la plantilla "+templateSeleccionado+" no existe.", 0);
			return;
		}
		leerArchivo(template);
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.execute("dlgTemplateOpen.hide();");
	}

	public boolean guardar(int remplazar) {
		if (selectedNode == null) {
			if (!navegarHasta((DefaultTreeNode)root, selectedNodePath, false)) {
				notificarMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar una carpeta", 0);
				return false;
			}
		}

		if (archivoNombre == null || archivoNombre.trim().length() == 0 ) {
			notificarMensaje(FacesMessage.SEVERITY_WARN, "Debe asignar el nombre del archivo", 0);
			return false;
		}

		File carpetaDestino = new File(((Elemento)selectedNode.getData()).path);
		File file = new File(carpetaDestino, eliminarExtension(archivoNombre)+EXT_HTML);
		
		RequestContext requestContext = RequestContext.getCurrentInstance();
		
		if (file.exists() && remplazar == 0) {
			requestContext.execute("dlgTemplateConfRemp.show();");
			return false;
		}
		
		requestContext.execute("dlgTemplateConfRemp.hide();");

		return guardar(file, false);
	}

	public boolean guardar(File file, boolean esTemplate) {
		try {

			File archivoCarpeta = file.getParentFile();
			if (!archivoCarpeta.exists()) {
				if (!archivoCarpeta.mkdirs()) {
					notificarMensaje(FacesMessage.SEVERITY_INFO, "No se pudo crear la carpeta", 0);
					return false;
				}
			}
			
			if (archivoNombre == null || archivoNombre.trim().length() == 0 ) {
				notificarMensaje(FacesMessage.SEVERITY_WARN, "Debe asignar el nombre del archivo", 0);
				return false;
			}

			if (!file.exists()) {
				file.createNewFile();
			} else {
				//Forza borrarlo
				file.delete();
			}
			limpiar();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(preferencias.get(PREFS.GESTDOC_HTML_HEAD)+cuerpoDocumento+preferencias.get(PREFS.GESTDOC_HTML_FOOT));
			bw.close();
			notificarMensaje(FacesMessage.SEVERITY_INFO, "Archivo guardado exitosamente", 0);

			if (!esTemplate) {
				try {
					log.info("Persisitendo "+entrada.getEntId()+" "+file.getAbsolutePath());
					crearRemplazarAnexoExpediente(entrada.getEntId(), file.getAbsolutePath());
				} catch (Exception e1) {
					log.warning("Error: "+e1.getMessage());
				}
			}

			return true;
		} catch (Exception e) {
			notificarMensaje(FacesMessage.SEVERITY_WARN, "Error: "+e.getMessage(), 0);
			return false;
		}
	}

	public TreeNode getRoot() {
		return root;
	}

	public String getSelectedNodePath() {
		return normalizarPathCarpeta(selectedNodePath);
	}

	public void setSelectedNodePath(String selectedNodePath) {

		this.selectedNodePath = normalizarPathCarpeta(selectedNodePath);
	}

	public String getArchivoNombre() {
		return eliminarExtension(archivoNombre);
	}

	public void setArchivoNombre(String archivoNombre) {
		this.archivoNombre = eliminarExtension(archivoNombre);
	}

	public String getCuerpoDocumento() {
		return cuerpoDocumento;
	}

	public void setCuerpoDocumento(String cuerpoDocumento) {
		this.cuerpoDocumento = cuerpoDocumento;
		limpiar();
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public ServiceDao getDaoServicio() {
		return daoServicio;
	}

	public void setDaoServicio(ServiceDao daoServicio) {
		this.daoServicio = daoServicio;
	}

	public String getArchivoNombreTemplate() {
		return eliminarExtension(archivoNombreTemplate);
	}

	public void setArchivoNombreTemplate(String archivoNombreTemplate) {
		this.archivoNombreTemplate = eliminarExtension(archivoNombreTemplate);
	}

	private static String eliminarExtension(String in) {
		if (in == null) return null;
		in = in.trim();
		in = PAT_EXT_HTML.matcher(in).replaceAll("");
		return in;
	}

	private static String normalizarPathCarpeta(String texto) {
		if (texto == null) return null;
		texto = texto.replace("\\", "/");
		return texto;
	}

	public String getTemplateSeleccionado() {
		return templateSeleccionado;
	}

	public void setTemplateSeleccionado(String templateSeleccionado) {
		this.templateSeleccionado = templateSeleccionado;
	}

	public List<String> getTemplates() {
		return templates;
	}

	public void setTemplates(List<String> templates) {
		this.templates = templates;
	}

	public String imageToBase64(byte[] imageByteArray) {	
		byte[] temp = jasyptPBEByteEncryptor.decrypt(imageByteArray);
		return Base64.encodeBase64String(temp);
	}

	public static byte[] extractBytes (File imgPath) throws IOException {
		byte imageData[];
		FileInputStream imageInFile = new FileInputStream(imgPath);
		imageData = IOUtils.toByteArray(imageInFile);
		return imageData;
	}

	public void setJasyptPBEByteEncryptor(StandardPBEByteEncryptor jasyptPBEByteEncryptor) {
		this.jasyptPBEByteEncryptor = jasyptPBEByteEncryptor;
	}
	public StandardPBEByteEncryptor getJasyptPBEByteEncryptor() {
		return jasyptPBEByteEncryptor;
	}

	public boolean isConfigurado() {
		return configurado;
	}

	public void setConfigurado(boolean configurado) {
		this.configurado = configurado;
	}

	public void limpiar() {
		cuerpoDocumento = desformatearTexto(cuerpoDocumento);
	}

	private final static Pattern TAMANIO_TEXTO = Pattern.compile("font-size:(.)*?;", Pattern.CASE_INSENSITIVE);
	private final static Pattern FAMILIA_TEXTO = Pattern.compile("font-family:(.)*?;", Pattern.CASE_INSENSITIVE);
	private final static Pattern HTML_FONT_FACE1 = Pattern.compile("<font face(.)*?(>)", Pattern.CASE_INSENSITIVE);
	private final static Pattern HTML_FONT_FACE2 = Pattern.compile("</font>", Pattern.CASE_INSENSITIVE);

	/**
	 * Elimina:
	 * <font face="Arial, Verdana o lo qu sea">
	 * </font>
	 * font-size: bla bla;
	 * @param texto
	 * @return
	 */
	public static String desformatearTexto(String texto) {
		texto = TAMANIO_TEXTO.matcher(texto).replaceAll("");
		texto = FAMILIA_TEXTO.matcher(texto).replaceAll("");
		texto = HTML_FONT_FACE1.matcher(texto).replaceAll("");
		texto = HTML_FONT_FACE2.matcher(texto).replaceAll("");
		return texto;
	}

	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	public StreamedContent getArchivoPdf() {
		return archivoPdf;
	}

	public void setArchivoPdf(StreamedContent archivoPdf) {
		this.archivoPdf = archivoPdf;
	}
	
	public ServiceMailSenderImpl getMailService() {
		return mailService;
	}

	public void setMailService(ServiceMailSenderImpl mailService) {
		this.mailService = mailService;
	}

	/**
	 * @return the entradasParaSalSel
	 */
	public List<String> getEntradasParaSalSel() {
		return entradasParaSalSel;
	}

	/**
	 * @param entradasParaSalSel the entradasParaSalSel to set
	 */
	public void setEntradasParaSalSel(List<String> entradasParaSalSel) {
		this.entradasParaSalSel = entradasParaSalSel;
	}

	/**
	 * @return the entradasParaSalida
	 */
	public List<SelectItem> getEntradasParaSalida() {
		return entradasParaSalida;
	}

	/**
	 * @param entradasParaSalida the entradasParaSalida to set
	 */
	public void setEntradasParaSalida(List<SelectItem> entradasParaSalida) {
		this.entradasParaSalida = entradasParaSalida;
	}

	public String getTextoBuscEntNen() {
		return textoBuscEntNen;
	}

	public void setTextoBuscEntNen(String textoBuscEntNen) {
		this.textoBuscEntNen = textoBuscEntNen;
	}

	/**
	 * @return the msgMultipleEntrada
	 */
	public String getMsgMultipleEntrada() {
		return msgMultipleEntrada;
	}

	/**
	 * @param msgMultipleEntrada the msgMultipleEntrada to set
	 */
	public void setMsgMultipleEntrada(String msgMultipleEntrada) {
		this.msgMultipleEntrada = msgMultipleEntrada;
	}
}