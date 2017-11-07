package la.netco.sgc.uilayer.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import la.netco.commons.services.FileSystemRepositoryService;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.ArchivoAuditorias;
import la.netco.sgc.persistence.dto.Auditores;
import la.netco.sgc.persistence.dto.Auditorias;
import la.netco.sgc.persistence.dto.Entidades;
import la.netco.sgc.persistence.dto.TipoAuditoria;
import la.netco.sgc.persistence.dto.TipoProgramacion;
import la.netco.uilayer.beans.PrimeDataModel;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author jhmoreno
 *
 */

@ManagedBean(name = "auditoriasBean")
@ViewScoped
public class AuditoriasBean implements Serializable {

	/* Log */
	private static Logger log = LoggerFactory.getLogger(AuditoriasBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	private AuditoriasDataModel listDatamodel = null;
	private Auditorias auditorias;
	private String anoActual;
	private Auditorias auditoriaSeleccionada;
	private Integer idAuditoriaSeleccionada;

	/* Select menu en pantalla */
	private List<SelectItem> tiposSociedadesItems;
	private Integer idSociedad;
	private List<SelectItem> tiposAuditoriaItems;
	private Integer idTipoAuditoria;
	private List<SelectItem> tiposProgramacionItems;
	private Integer idTipoProgramacion;
	private String idPeriodo;
	private List<SelectItem> funcionariosItems;
	private Short idFuncionario;
	private List<SelectItem> auditoresItems;
	private Integer idAuditor;
	private Date fechaEnvioOficio;
	private Date fechaTrabajoCampo;
	private Date fechaEntregaInforme;
	private String alcance;

	/* Cargue de archivo */
	private ArchivoAuditorias archivoAuditorias;
	private List<ArchivoAuditorias> archivosAuditorias;
	UploadedFile file;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_FILE_SYSTEM_REPO_SERVICE)
	transient private FileSystemRepositoryService repoService;

	public AuditoriasBean() {
		this.auditorias = new Auditorias();
		this.file = null;
		this.archivoAuditorias = null;
		// this.anoActual = Integer.toString(c.get(Calendar.YEAR));
	}

	/**
	 * Auditorias data model
	 * 
	 * @author Alejo Medell�n
	 *
	 */
	private static final class AuditoriasDataModel extends
			PrimeDataModel<Auditorias, Integer> {
		private static final long serialVersionUID = 1L;

		private AuditoriasDataModel() {
			super(Auditorias.class);
			setOrderBy(Order.asc("audFechaEnvioOficio"));
		}

		@Override
		protected Object getId(Auditorias t) {
			return t.getClass();
		}
	}

	/**
	 * Carga listado inicial
	 */
	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new AuditoriasDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();

			filtros.add(Restrictions.isNotNull("audId"));
			filtros.add(Restrictions.isNotNull("audSociedad"));
			filtros.add(Restrictions.isNotNull("audTipo"));
			filtros.add(Restrictions.isNotNull("audPeriodo"));

			Map<String, String> alias = new HashMap<String, String>();
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
			Calendar c = Calendar.getInstance();
			this.anoActual = Integer.toString(c.get(Calendar.YEAR));
		}
	}

	/**
	 * Busqueda de auditorias, seg�n los atributos dados en pantalla
	 */

	public void buscar() {
		log.info("Ingreso a buscar Auditorias");
		List<Criterion> filtros = new ArrayList<Criterion>();
		if (anoActual == null || anoActual.trim().length() == 0) {
			Calendar c = Calendar.getInstance();
			this.anoActual = Integer.toString(c.get(Calendar.YEAR));
		}
		filtros.add(Restrictions.isNotNull("audId"));
		filtros.add(Restrictions.isNotNull("audSociedad"));
		filtros.add(Restrictions.isNotNull("audTipo"));
		filtros.add(Restrictions.isNotNull("audPeriodo"));

		// Fecha inicial del a�o
		if (!anoActual.equals("") && anoActual != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, Integer.parseInt(anoActual));
			calendar.set(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date fechaInicial = calendar.getTime();
			log.info(fechaInicial.toString());

			// Fecha final del a�o
			calendar.set(Calendar.MONTH, 11);
			calendar.set(Calendar.DAY_OF_MONTH, 31);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			Date fechaFinal = calendar.getTime();
			log.info(fechaFinal.toString());

			filtros.add(Restrictions.ge("audFechaEnvioOficio", fechaInicial));
			filtros.add(Restrictions.lt("audFechaEnvioOficio", fechaFinal));
		}

		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}

	/**
	 * Cargar Sociedades
	 */
	@SuppressWarnings("unchecked")
	public void cargarSociedadesItems() {

		if (tiposSociedadesItems == null) {
			/* Obtener List de sociedades */
			List<Entidades> listSociedades = (List<Entidades>) serviceDao
					.getGenericCommonDao().executeFind(
							Entidades.NAMED_QUERY_ALL_ACTIVOS);

			tiposSociedadesItems = new ArrayList<SelectItem>();

			for (Entidades sociedades : listSociedades) {
				tiposSociedadesItems.add(new SelectItem(sociedades
						.getEntCodigo(), sociedades.getEntObjetoSocial()));
			}

		} else if (tiposSociedadesItems == null) {
			tiposSociedadesItems = new ArrayList<SelectItem>();
		}
	}

	/**
	 * Cargar tipos de auditoria
	 */
	@SuppressWarnings("unchecked")
	public void cargarTipoAuditoriaItems() {

		if (tiposAuditoriaItems == null) {
			/* Obtener List de sociedades */
			List<TipoAuditoria> listTipoAuditorias = (List<TipoAuditoria>) serviceDao
					.getGenericCommonDao().executeFind(
							TipoAuditoria.NAMED_QUERY_ALL);

			tiposAuditoriaItems = new ArrayList<SelectItem>();

			for (TipoAuditoria tipoAuditoria : listTipoAuditorias) {
				tiposAuditoriaItems.add(new SelectItem(tipoAuditoria
						.getTauCodigo(), tipoAuditoria.getTauNombre()));
			}

		} else if (tiposAuditoriaItems == null) {
			tiposAuditoriaItems = new ArrayList<SelectItem>();
		}
	}

	/**
	 * Cargar tipos de programacion
	 */
	@SuppressWarnings("unchecked")
	public void cargarTipoProgramacionItems() {

		if (tiposProgramacionItems == null) {
			/* Obtener List de sociedades */
			List<TipoProgramacion> listTipoProgramacion = (List<TipoProgramacion>) serviceDao
					.getGenericCommonDao().executeFind(
							TipoProgramacion.NAMED_QUERY_ALL);

			tiposProgramacionItems = new ArrayList<SelectItem>();

			for (TipoProgramacion tipoProgramacion : listTipoProgramacion) {
				tiposProgramacionItems.add(new SelectItem(tipoProgramacion
						.getTprCodigo(), tipoProgramacion.getTprNombre()));
			}

		} else if (tiposProgramacionItems == null) {
			tiposProgramacionItems = new ArrayList<SelectItem>();
		}
	}

	/**
	 * Cargar funcionarios
	 */
	@SuppressWarnings("unchecked")
	public void cargarFuncionariosItems() {

		if (funcionariosItems == null) {
			/* Obtener List de sociedades */
			List<Usuario> listFuncionarios = (List<Usuario>) serviceDao
					.getGenericCommonDao().executeFind(
							Usuario.NAMED_QUERY_ALL_USUARIOS);

			funcionariosItems = new ArrayList<SelectItem>();

			for (Usuario funcionario : listFuncionarios) {
				funcionariosItems
						.add(new SelectItem(funcionario.getUsrId(), funcionario
								.getUsrNom() + " " + funcionario.getUsrPap()));
			}

		} else if (funcionariosItems == null) {
			funcionariosItems = new ArrayList<SelectItem>();
		}
	}

	/**
	 * Cargar auditores
	 */
	@SuppressWarnings("unchecked")
	public void cargarAuditoresItems() {
		if (auditoresItems == null) {
			/* Obtener List de sociedades */
			List<Auditores> listAuditores = (List<Auditores>) serviceDao
					.getGenericCommonDao().executeFind(
							Auditores.NAMED_QUERY_ALL_ACTIVOS);

			auditoresItems = new ArrayList<SelectItem>();

			for (Auditores auditor : listAuditores) {
				auditoresItems.add(new SelectItem(auditor.getCodigoAuditores(),
						auditor.getNombreAuditores()));
			}

		} else if (auditoresItems == null) {
			auditoresItems = new ArrayList<SelectItem>();
		}
	}

	/**
	 * Crear Auditoria
	 */
	public String createOrReplace() {
		String page = null;
		try {
			log.info("Ingreso a crear auditorias");

			// Setea sociedad
			if (idSociedad != null) {
				Entidades sociedad = (Entidades) serviceDao
						.getGenericCommonDao()
						.read(Entidades.class, idSociedad);
				auditorias.setAudSociedad(sociedad);
			}

			// Setea tipo auditoria
			if (idTipoAuditoria != null) {
				TipoAuditoria tipoAuditoria = (TipoAuditoria) serviceDao
						.getGenericCommonDao().read(TipoAuditoria.class,
								idTipoAuditoria);
				auditorias.setAudTipo(tipoAuditoria);
			}

			// Setea tipo programacion
			if (idTipoProgramacion != null) {
				TipoProgramacion tipoProgramacion = (TipoProgramacion) serviceDao
						.getGenericCommonDao().read(TipoProgramacion.class,
								idTipoProgramacion);
				auditorias.setAudTipoProgramacion(tipoProgramacion);
			}

			// Setea periodo
			if (idPeriodo != null) {
				auditorias.setAudPeriodo(idPeriodo);
			}

			// Setea funcionario
			if (idFuncionario != null) {
				Usuario usuario = (Usuario) serviceDao.getGenericCommonDao()
						.read(Usuario.class, idFuncionario);
				auditorias.setAudFuncionario(idFuncionario.toString());
			}

			// Setea auditor
			if (idAuditor != null) {
				auditorias.setAudResponsable(idAuditor);
			}

			// Setea fecha envio oficio
			if (fechaEnvioOficio != null) {
				auditorias.setAudFechaEnvioOficio(fechaEnvioOficio);
			}

			// Setea fecha trabajo campo
			if (fechaTrabajoCampo != null) {
				auditorias.setAudFechaTrabajoCampo(fechaTrabajoCampo);
			}

			// Setea fecha entrega informe
			if (fechaEntregaInforme != null) {
				auditorias.setAudFechaInforme(fechaEntregaInforme);
			}

			// Setea fecha entrega informe
			if (alcance != null) {
				auditorias.setAudAlcance(alcance);
			} else {
				auditorias.setAudAlcance("");
			}

			serviceDao.getGenericCommonDao().create(Auditorias.class,
					auditorias);

			if (archivoAuditorias != null && file != null) {
				archivoAuditorias.setAauAuditoria(auditorias);
				repoService.guardarArchivo(file.getInputstream(),
						archivoAuditorias);
			}

			page = "programar";

			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_CREAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);

			auditorias = new Auditorias();

		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());
		}
		return page;
	}

	/**
	 * Actualiza Auditoria
	 */
	public String actualizarTipoAuditoria() {
		String page = null;
		try {
			log.info("Ingreso a actualizar Tipo Auditoria"
					+ auditoriaSeleccionada.getAudId());

			if (auditoriaSeleccionada != null) {
				// Setea sociedad
				if (idSociedad != null) {
					Entidades sociedad = (Entidades) serviceDao
							.getGenericCommonDao().read(Entidades.class,
									idSociedad);
					auditoriaSeleccionada.setAudSociedad(sociedad);
				}

				// Setea tipo auditoria
				if (idTipoAuditoria != null) {
					TipoAuditoria tipoAuditoria = (TipoAuditoria) serviceDao
							.getGenericCommonDao().read(TipoAuditoria.class,
									idTipoAuditoria);
					auditoriaSeleccionada.setAudTipo(tipoAuditoria);
				}

				// Setea tipo programacion
				if (idTipoProgramacion != null) {
					TipoProgramacion tipoProgramacion = (TipoProgramacion) serviceDao
							.getGenericCommonDao().read(TipoProgramacion.class,
									idTipoProgramacion);
					auditoriaSeleccionada
							.setAudTipoProgramacion(tipoProgramacion);
				}

				// Setea periodo
				if (idPeriodo != null) {
					auditoriaSeleccionada.setAudPeriodo(idPeriodo);
				}

				// Setea funcionario
				if (idFuncionario != null) {
					Usuario usuario = (Usuario) serviceDao
							.getGenericCommonDao().read(Usuario.class,
									idFuncionario);
					auditoriaSeleccionada.setAudFuncionario(idFuncionario
							.toString());
				}

				// Setea auditor
				if (idAuditor != null) {
					auditoriaSeleccionada.setAudResponsable(idAuditor);
				}

				// Setea fecha envio oficio
				if (fechaEnvioOficio != null) {
					auditoriaSeleccionada
							.setAudFechaEnvioOficio(fechaEnvioOficio);
				}

				// Setea fecha trabajo campo
				if (fechaTrabajoCampo != null) {
					auditoriaSeleccionada
							.setAudFechaTrabajoCampo(fechaTrabajoCampo);
				}

				// Setea fecha entrega informe
				if (fechaEntregaInforme != null) {
					auditoriaSeleccionada
							.setAudFechaInforme(fechaEntregaInforme);
				}

				// Setea fecha entrega informe
				if (alcance != null) {
					auditoriaSeleccionada.setAudAlcance(alcance);
				} else {
					auditoriaSeleccionada.setAudAlcance("");
				}

				serviceDao.getGenericCommonDao().saveOrUpdate(Auditorias.class,
						auditoriaSeleccionada);
			}
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			page = "registrar";
			auditoriaSeleccionada = new Auditorias();
			limpiar();

		} catch (Exception exception) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());

		}
		return page;

	}

	public void limpiar() {
		auditorias = new Auditorias();
		auditoriaSeleccionada = new Auditorias();
	}

	/**
	 * Metodo para cargar el la informaci�n de una auditoria
	 * */
	@SuppressWarnings("unchecked")
	public void cargarRegistro() {
		try {
			log.info("Cargar datos Auditoria");
			if (idAuditoriaSeleccionada == null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext()
						.getRequestParameterMap()
						.get("idAuditoriaSeleccionada");
				if (idRegSeleccionado != null)
					this.idAuditoriaSeleccionada = Integer
							.parseInt(idRegSeleccionado);
			}
			if (idAuditoriaSeleccionada != null) {
				auditoriaSeleccionada = (Auditorias) serviceDao
						.getGenericCommonDao().read(Auditorias.class,
								idAuditoriaSeleccionada);
			}
			if (auditoriaSeleccionada != null) {
				idSociedad = auditoriaSeleccionada.getAudSociedad()
						.getEntCodigo();

				idTipoAuditoria = auditoriaSeleccionada.getAudTipo()
						.getTauCodigo();

				idTipoProgramacion = auditoriaSeleccionada
						.getAudTipoProgramacion().getTprCodigo();

				idPeriodo = auditoriaSeleccionada.getAudPeriodo();
				log.info("Periodo" + idPeriodo);

				idFuncionario = Short.valueOf(auditoriaSeleccionada
						.getAudFuncionario());
				log.info("Funcionario" + idFuncionario.toString());

				idAuditor = auditoriaSeleccionada.getAudResponsable();
				log.info("Auditor" + idAuditor.toString());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adjuntar archivo
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public void uploadFile(FileUploadEvent event) {
		try {

			file = event.getFile();
			log.info("Cargar archivo");
			archivosAuditorias = new ArrayList<ArchivoAuditorias>();
			archivoAuditorias = new ArchivoAuditorias();
			archivoAuditorias.setAauNombre(file.getFileName());
			archivoAuditorias.setAauContentType(file.getContentType());
			archivoAuditorias.setAauSize(file.getSize());
			archivoAuditorias.setAauFechaRegistro(new Date(System
					.currentTimeMillis()));
			archivosAuditorias.add(archivoAuditorias);
			log.info(archivoAuditorias.getAauNombre());

		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
	}

	/**
	 * Descargar archivo
	 * 
	 * @param idArchivo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public StreamedContent descargar(Integer idAuditoria) {
		try {
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, idAuditoria);

			List<ArchivoAuditorias> archivos = (List<ArchivoAuditorias>) serviceDao
					.getGenericCommonDao().executeFind(ArchivoAuditorias.class,
							params,
							ArchivoAuditorias.NAMED_QUERY_GET_BY_AUDITORIA);

			if (archivos.size() > 0) {
				InputStream stream = repoService.obtenerInputStream(archivos.get(0));

				StreamedContent file = new DefaultStreamedContent(stream,
						archivos.get(0).getAauContentType(),
						archivos.get(0).getAauNombre());
				return file;
			}
			
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Descargar archivo
	 * 
	 * @param idArchivo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean existeArchivo(Integer idAuditoria) {
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(0, idAuditoria);

		List<ArchivoAuditorias> archivos = (List<ArchivoAuditorias>) serviceDao
				.getGenericCommonDao().executeFind(ArchivoAuditorias.class,
						params, ArchivoAuditorias.NAMED_QUERY_GET_BY_AUDITORIA);

		if (archivos.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public String navProgramarAuditoria() {
		return "programarAuditoria";
	}

	public String navModificarAuditoria() {
		return "modificarAuditoria";
	}

	public String navCancelar() {
		return "registrar";
	}

	public String navListadoHallazgo() {
		return "listadoHallazgo";
	}

	public String navCancelarP() {
		return "programar";
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		AuditoriasBean.log = log;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public AuditoriasDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(AuditoriasDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}

	public Auditorias getAuditorias() {
		return auditorias;
	}

	public void setAuditorias(Auditorias auditorias) {
		this.auditorias = auditorias;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAnoActual() {
		return anoActual;
	}

	public void setAnoActual(String anoActual) {
		this.anoActual = anoActual;
	}

	public List<SelectItem> getTiposSociedadesItems() {
		cargarSociedadesItems();
		return tiposSociedadesItems;
	}

	public void setTiposSociedadesItems(List<SelectItem> tiposSociedadesItems) {
		this.tiposSociedadesItems = tiposSociedadesItems;
	}

	public Integer getIdSociedad() {
		return idSociedad;
	}

	public void setIdSociedad(Integer idSociedad) {
		this.idSociedad = idSociedad;
	}

	public List<SelectItem> getTiposAuditoriaItems() {
		cargarTipoAuditoriaItems();
		return tiposAuditoriaItems;
	}

	public void setTiposAuditoriaItems(List<SelectItem> tiposAuditoriaItems) {
		this.tiposAuditoriaItems = tiposAuditoriaItems;
	}

	public Integer getIdTipoAuditoria() {
		return idTipoAuditoria;
	}

	public void setIdTipoAuditoria(Integer idTipoAuditoria) {
		this.idTipoAuditoria = idTipoAuditoria;
	}

	public List<SelectItem> getTiposProgramacionItems() {
		cargarTipoProgramacionItems();
		return tiposProgramacionItems;
	}

	public void setTiposProgramacionItems(
			List<SelectItem> tiposProgramacionItems) {
		this.tiposProgramacionItems = tiposProgramacionItems;
	}

	public Integer getIdTipoProgramacion() {
		return idTipoProgramacion;
	}

	public void setIdTipoProgramacion(Integer idTipoProgramacion) {
		this.idTipoProgramacion = idTipoProgramacion;
	}

	public String getIdPeriodo() {
		return idPeriodo;
	}

	public void setIdPeriodo(String idPeriodo) {
		this.idPeriodo = idPeriodo;
	}

	public List<SelectItem> getFuncionariosItems() {
		cargarFuncionariosItems();
		return funcionariosItems;
	}

	public void setFuncionariosItems(List<SelectItem> funcionariosItems) {
		this.funcionariosItems = funcionariosItems;
	}

	public Short getIdFuncionario() {
		return idFuncionario;
	}

	public void setIdFuncionario(Short idFuncionario) {
		this.idFuncionario = idFuncionario;
	}

	public List<SelectItem> getAuditoresItems() {
		cargarAuditoresItems();
		return auditoresItems;
	}

	public void setAuditoresItems(List<SelectItem> auditoresItems) {
		this.auditoresItems = auditoresItems;
	}

	public Integer getIdAuditor() {
		return idAuditor;
	}

	public void setIdAuditor(Integer idAuditor) {
		this.idAuditor = idAuditor;
	}

	public Date getFechaEnvioOficio() {
		return fechaEnvioOficio;
	}

	public void setFechaEnvioOficio(Date fechaEnvioOficio) {
		this.fechaEnvioOficio = fechaEnvioOficio;
	}

	public Date getFechaTrabajoCampo() {
		return fechaTrabajoCampo;
	}

	public void setFechaTrabajoCampo(Date fechaTrabajoCampo) {
		this.fechaTrabajoCampo = fechaTrabajoCampo;
	}

	public Date getFechaEntregaInforme() {
		return fechaEntregaInforme;
	}

	public void setFechaEntregaInforme(Date fechaEntregaInforme) {
		this.fechaEntregaInforme = fechaEntregaInforme;
	}

	public String getAlcance() {
		return alcance;
	}

	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}

	public Auditorias getAuditoriaSeleccionada() {
		return auditoriaSeleccionada;
	}

	public void setAuditoriaSeleccionada(Auditorias auditoriaSeleccionada) {
		this.auditoriaSeleccionada = auditoriaSeleccionada;
	}

	public Integer getIdAuditoriaSeleccionada() {
		return idAuditoriaSeleccionada;
	}

	public void setIdAuditoriaSeleccionada(Integer idAuditoriaSeleccionada) {
		this.idAuditoriaSeleccionada = idAuditoriaSeleccionada;
	}

	public String getSizeLimit() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		String maxRequestSize = ctx.getExternalContext().getInitParameter(
				"maxRequestSize");
		return maxRequestSize;
	}

	public ArchivoAuditorias getArchivoAuditorias() {
		return archivoAuditorias;
	}

	public void setArchivoAuditorias(ArchivoAuditorias archivoAuditorias) {
		this.archivoAuditorias = archivoAuditorias;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public FileSystemRepositoryService getRepoService() {
		return repoService;
	}

	public void setRepoService(FileSystemRepositoryService repoService) {
		this.repoService = repoService;
	}

	public List<ArchivoAuditorias> getArchivosAuditorias() {
		return archivosAuditorias;
	}

	public void setArchivosAuditorias(List<ArchivoAuditorias> archivosAuditorias) {
		this.archivosAuditorias = archivosAuditorias;
	}

}
