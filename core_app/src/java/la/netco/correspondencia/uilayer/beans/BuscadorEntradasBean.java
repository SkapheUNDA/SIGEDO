package la.netco.correspondencia.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.Clasificacion;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Depend;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Medioscorrespondencia;
import la.netco.persistencia.dto.commons.Tiposdocumento;
import la.netco.persistencia.dto.commons.Tipospersona;
import la.netco.uilayer.beans.PrimeDataModel;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@ManagedBean
@SessionScoped
public class BuscadorEntradasBean implements Serializable {

	private static Logger log = Logger.getLogger(BuscadorEntradasBean.class.getSimpleName());
	
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	private EntradasDataModel principalDataModel;
	private String noRadicacion;
	private Date fechaRadicacion1;
	
	private Date fechaRespuesta1;
	private Date fechaRespuesta2;
	
	private Integer estaContestada = 0;

	private String docPersona;

	private Date fechaRadicacion2;
	private String asunto;
	private String nomPersonaNatural;
	private String nomPersonaJuridica;

	private List<SelectItem> clasificacionItems;
	private Short idClasificacionSeleccionada;
	
	private List<String> estadosItems;
	private String idEstadoSeleccionado;

	private List<Usuario> usuariosDb;
	private List<SelectItem> usuariosItems;
	private Short idUsuarioSeleccionado;

	private List<SelectItem> dependenciasItems;
	private Short idDependenciaSeleccionada;

	private List<SelectItem> mediosItems;
	private Short idMedioSeleccionada;

	private List<SelectItem> tiposPersonaItems;
	private Short idTiposPersonaSeleccionado;

	private List<SelectItem> tiposDocumentosItems;
	private Integer idTipoDocumentoSol;
	private Boolean adminCorrespondencia;
	
	private Boolean grupoFirmante;
	
	private Boolean grupoAdminTemplate;

	private String usuarioLogeado;
	
	private String ordenarPor;
	
	private Boolean ordenarDir = true;

	private Map<Integer, String> idsEntradasSeleccionadas = new HashMap<Integer, String>();

	public BuscadorEntradasBean() {
		estaContestada = 0;
		if (usuarioLogeado == null)
			usuarioLogeado = UserDetailsUtils.usernameLogged();
	}

	private Long buscarGrupo(String user, String grupo) {
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(0, user);
		params.put(1, grupo);
		Long adminGroup = (Long) serviceDao.getEntradaDao().executeUniqueResult(Usuario.NAMED_QUERY_FIND_BY_USUARIO_GRUPO, params);
		return adminGroup;
	}
	
	@PostConstruct
	public void init() {
		log.info("init");
		if (adminCorrespondencia == null && usuarioLogeado != null) {
			Long adminGroup = buscarGrupo(usuarioLogeado, ConstantsKeysFire.GRUPO_ADMIN_CORRESPONDENCIA);
			adminCorrespondencia = (adminGroup != null && !adminGroup.equals(new Integer(0).longValue()));
		}
		
		if (grupoFirmante == null && usuarioLogeado != null) {
			Long adminGroup = buscarGrupo(usuarioLogeado, ConstantsKeysFire.GRUPO_FIRMANTE);
			grupoFirmante = (adminGroup != null && !adminGroup.equals(new Integer(0).longValue()));
		}
		
		if (grupoAdminTemplate == null && usuarioLogeado != null) {
			Long adminGroup = buscarGrupo(usuarioLogeado, ConstantsKeysFire.GRUPO_ADMIN_TEMPLATE);
			grupoAdminTemplate = (adminGroup != null && !adminGroup.equals(new Integer(0).longValue()));
		}

		seleccionarUsuarioPredeterminado();
		
		if (principalDataModel == null) {
			principalDataModel = new EntradasDataModel();
			cargaFiltrosDataModel();
		}
		
		cargarClasificacionItems();
		
		ordenarPor = "entFdr";
	}

	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUE)
	public String agregarNavigation() {
		return "agregar";
	}

	@SecuredAction(keyAction = CommonsActionsKey.VIEW, keyMod = CommonsModKeys.MNUE)
	public String detalleNavigation() {
		return "detalle";
	}

	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.MNUE)
	public String actualizarNavigation() {
		return "actualizar";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String duplicarNavigation() {
		return "duplicar";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String cambiarFechaNavigation() {
		return "cambiarFecha";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String historialNavigation() {
		return "historial";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.VIEW, keyMod
	// =CommonsModKeys.MNUE)
	public String noNotificarNavigation() {
		return "noNotificar";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.VIEW, keyMod
	// =CommonsModKeys.MNUE)
	public String cambiarEtapaNavigation() {
		return "cambiarEtapa";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String cambiarEtapasMultiplesNavigation() {
		return "cambiarEtapaMultiple?faces-redirect=true";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String archivosAdjuntosNavigation() {
		return "archivosAdjuntos";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String alertasGeneralesNavigation() {
		return "alertasGenerales?faces-redirect=true";
	}

	// @SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod
	// =CommonsModKeys.MNUE)
	public String alertasEntradaNavigation() {
		return "alertasGenerales";
	}

	public String cargaFiltrosDataModel() {
		List<Criterion> filtros = new ArrayList<Criterion>();

		boolean filtro = false;
		if (noRadicacion != null && (noRadicacion = noRadicacion.trim()).length() > 0) {
			filtros.add(Restrictions.eq("entNen", noRadicacion));
			filtro = true;
		}
		
		if (fechaRadicacion1 != null && !fechaRadicacion1.equals("")) {
			filtros.add(Restrictions.ge("entFen", fechaRadicacion1));
			filtro = true;
		}
		
		if (fechaRadicacion2 != null && !fechaRadicacion2.equals("")) {
			filtros.add(Restrictions.le("entFen", fechaRadicacion2));
			filtro = true;
		}
		
		if (fechaRespuesta1 != null && !fechaRespuesta1.equals("")) {
			filtros.add(Restrictions.ge("entFdr", fechaRespuesta1));
			filtro = true;
		}
		
		if (fechaRespuesta2 != null && !fechaRespuesta2.equals("")) {
			filtros.add(Restrictions.le("entFdr", fechaRespuesta2));
			filtro = true;
		}

		if (nomPersonaNatural != null && (nomPersonaNatural = nomPersonaNatural.trim()).length() > 0) {
			filtros.add(Restrictions.ilike("nombreCompletoSol", "%"+ nomPersonaNatural + "%"));
			filtro = true;
		}
		
		if (docPersona != null && (docPersona = docPersona.trim()).length() > 0) {
			filtros.add(Restrictions.ilike("entNdo", "%"+ docPersona + "%"));
			filtro = true;
		}

		if (asunto != null && (asunto = asunto.trim()).length() > 0) {
			filtros.add(Restrictions.ilike("entAsu", "%" + asunto + "%"));
			filtro = true;
		}

		if (nomPersonaJuridica != null && (nomPersonaJuridica = nomPersonaJuridica.trim()).length() > 0) {
			filtros.add(Restrictions.ilike("entEnt", "%" + nomPersonaJuridica + "%"));
			filtro = true;
		}

		if (idDependenciaSeleccionada != null && idDependenciaSeleccionada != 0) {
			filtros.add(Restrictions.eq("dependencia.depId", idDependenciaSeleccionada));
			filtro = true;
		}

		if (idClasificacionSeleccionada != null && idClasificacionSeleccionada != 0) {
			filtros.add(Restrictions.eq("clasificacion.claId", idClasificacionSeleccionada));
			filtro = true;
		}
		
		if (idEstadoSeleccionado != null && idEstadoSeleccionado.trim().length() > 0) {
			filtros.add(Restrictions.eq("estado.estNom", idEstadoSeleccionado));
			filtro = true;
		}
		
		if (estaContestada == 0) {
			filtros.add(Restrictions.eq("entLcon", false));
			filtro = true;
		} else if (estaContestada == 1) {
			filtros.add(Restrictions.eq("entLcon", true));
			filtro = true;
		}
		
		if (adminCorrespondencia == true) {
			if (idUsuarioSeleccionado != null && idUsuarioSeleccionado != 0) {
				filtros.add(Restrictions.eq("usuario.usrId", idUsuarioSeleccionado));
			} else if (!filtro) {
				filtros.add(Restrictions.eq("usuario.usrId", UserDetailsUtils.usuarioLogged().getUsrId()));
			}
		} else {
			filtros.add(Restrictions.eq("usuario.usrId", UserDetailsUtils.usuarioLogged().getUsrId()));
		}

		Map<String, String> alias = new HashMap<String, String>();
		alias.put("usuario", "usuario");
		alias.put("estado", "estado");
		principalDataModel.setAlias(alias);
		if (ordenarPor != null) {
			if (ordenarDir == null || ordenarDir) {
				principalDataModel.setOrderBy(Order.asc(ordenarPor));
			} else {
				principalDataModel.setOrderBy(Order.desc(ordenarPor));
			}
		}
		principalDataModel.setFiltros(filtros);
		
		
		
		return "";
	}

	public void borrarSeleccion() {
		//Se borran las seleccionadas
		idsEntradasSeleccionadas = new HashMap<Integer, String>();
	}
	
	// @SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod =
	// CommonsModKeys.MNUEPAE)
	public String guardaFlashEntradasSeleccionadasCambioEtapa() {
		FacesUtils.flashScope(FacesContext.getCurrentInstance()).put(
				"idsEntradasSeleccionadas", idsEntradasSeleccionadas);
		return "cambiarEtapaMultiple?faces-redirect=true";
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

	private void cargarEstadosItems() {
		//estadosItems
		if (idClasificacionSeleccionada != null && idClasificacionSeleccionada > 0) {
			List<Map<String, Object>> lectura = null;
			String query = "select distinct(est.est_nom) as nombre "+
				"from tramite tra "+
				"inner join CLASIFICACION cla "+
				"on cla.cla_trm = tra.trm_id "+
				"inner join ESTADO est "+
				"on est.EST_TRM = tra.trm_id "+
				"where cla.cla_id = ?";
			lectura = serviceDao.getSpringGenericDao().executeQuery(query, idClasificacionSeleccionada);
			
			estadosItems = new ArrayList<String>();
			for (Map<String, Object> elem : lectura) {
				estadosItems.add(elem.get("nombre").toString());
			}
		} else {
			List<Map<String, Object>> lectura = null;
			String query = "select distinct(est.est_nom) as nombre "+
				"from tramite tra "+
				"inner join CLASIFICACION cla "+
				"on cla.cla_trm = tra.trm_id "+
				"inner join ESTADO est "+
				"on est.EST_TRM = tra.trm_id";
			lectura = serviceDao.getSpringGenericDao().executeQuery(query);
			
			estadosItems = new ArrayList<String>();
			for (Map<String, Object> elem : lectura) {
				estadosItems.add(elem.get("nombre").toString());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void cargarClasificacionItems() {
		
		List<Clasificacion> clasificaciones;
		
		if (idDependenciaSeleccionada != null && idDependenciaSeleccionada > 0) {
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_ENTRADA);
			params.put(1, idDependenciaSeleccionada);
			
			clasificaciones = (List<Clasificacion>)
			serviceDao.getGenericCommonDao().executeFind(Depend.class,
			params, Clasificacion.NAMED_QUERY_ALL_BY_TIPO_DEPENDENCIA);
		} else {
			clasificaciones = (List<Clasificacion>) serviceDao.getGenericCommonDao().loadAll(Clasificacion.class);
		}

		clasificacionItems = new ArrayList<SelectItem>();

		for (Clasificacion clasificacion : clasificaciones) {
			clasificacionItems.add(new SelectItem(clasificacion.getClaId(), clasificacion.getClaNom()));
		}
		
		ordenarListaClasificacion();
		
	}
	
	private void ordenarListaClasificacion() {
		Collections.sort(clasificacionItems, new Comparator<SelectItem>(){
		    public int compare(SelectItem s1, SelectItem s2) {
		        return s1.getLabel().compareToIgnoreCase(s2.getLabel());
		    }
		});
	}

	@SuppressWarnings("unchecked")
	public void cargarUsuariosItems() {

		if (usuariosItems == null || usuariosDb == null) {

			usuariosDb = (List<Usuario>) serviceDao
					.getGenericCommonDao().executeFind(
							Usuario.NAMED_QUERY_ALL_USUARIOS_ACTIVOS);

			usuariosItems = new ArrayList<SelectItem>();

			for (Usuario usu : usuariosDb) {
				usuariosItems.add(new SelectItem(usu.getUsrId(), usu
						.getUsrNom()
						+ " "
						+ usu.getUsrPap()
						+ " "
						+ usu.getUsrSap()));
				
			}

		}
	}

	public void seleccionarUsuarioPredeterminado() {
		log.info("seleccionarUsuarioPredeterminado!");
		if (adminCorrespondencia) {
			idUsuarioSeleccionado = UserDetailsUtils.usuarioLogged().getUsrId();
		}
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
		} else if (tiposPersonaItems == null) {
			tiposPersonaItems = new ArrayList<SelectItem>();
		}
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public EntradasDataModel getPrincipalDataModel() {
		return principalDataModel;
	}

	public void setPrincipalDataModel(EntradasDataModel principalDataModel) {
		this.principalDataModel = principalDataModel;
	}

	private static final class EntradasDataModel extends
			PrimeDataModel<Entrada, Integer> {
		private static final long serialVersionUID = 1L;

		private EntradasDataModel() {
			super(Entrada.class);
		}

		@Override
		protected Object getId(Entrada t) {
			return t.getEntId();
		}
	}

	public String getNoRadicacion() {
		return noRadicacion;
	}

	public String getAsunto() {
		return asunto;
	}

	public String getNomPersonaNatural() {
		return nomPersonaNatural;
	}

	public String getNomPersonaJuridica() {
		return nomPersonaJuridica;
	}

	public Short getIdDependenciaSeleccionada() {
		return idDependenciaSeleccionada;
	}

	public Short getIdClasificacionSeleccionada() {
		return idClasificacionSeleccionada;
	}

	public Short getIdUsuarioSeleccionado() {
		return idUsuarioSeleccionado;
	}

	public void setNoRadicacion(String noRadicacion) {
		this.noRadicacion = noRadicacion;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public void setNomPersonaNatural(String nomPersonaNatural) {
		this.nomPersonaNatural = nomPersonaNatural;
	}

	public void setNomPersonaJuridica(String nomPersonaJuridica) {
		this.nomPersonaJuridica = nomPersonaJuridica;
	}

	public void setIdDependenciaSeleccionada(Short idDependenciaSeleccionada) {
		this.idDependenciaSeleccionada = idDependenciaSeleccionada;
	}

	public void setIdClasificacionSeleccionada(Short idClasificacionSeleccionada) {
		this.idClasificacionSeleccionada = idClasificacionSeleccionada;
	}

	public void setIdUsuarioSeleccionado(Short idUsuarioSeleccionado) {
		this.idUsuarioSeleccionado = idUsuarioSeleccionado;
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

	public List<SelectItem> getUsuariosItems() {
		cargarUsuariosItems();
		return usuariosItems;
	}

	public void setUsuariosItems(List<SelectItem> usuariosItems) {
		this.usuariosItems = usuariosItems;
	}

	public List<SelectItem> getMediosItems() {
		cargarMediosItems();
		return mediosItems;
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

	public Boolean getAdminCorrespondencia() {
		return adminCorrespondencia;
	}

	public void setAdminCorrespondencia(Boolean adminCorrespondencia) {
		this.adminCorrespondencia = adminCorrespondencia;
	}

	public Map<Integer, String> getIdsEntradasSeleccionadas() {
		return idsEntradasSeleccionadas;
	}

	public void setIdsEntradasSeleccionadas(
			Map<Integer, String> idsEntradasSeleccionadas) {
		this.idsEntradasSeleccionadas = idsEntradasSeleccionadas;
	}

	public Date getFechaRadicacion1() {
		return fechaRadicacion1;
	}

	public void setFechaRadicacion1(Date fechaRadicacion1) {
		this.fechaRadicacion1 = fechaRadicacion1;
	}

	public Date getFechaRadicacion2() {
		return fechaRadicacion2;
	}

	public void setFechaRadicacion2(Date fechaRadicacion2) {
		this.fechaRadicacion2 = fechaRadicacion2;
	}

	public String getDocPersona() {
		return docPersona;
	}

	public void setDocPersona(String docPersona) {
		this.docPersona = docPersona;
	}
	
	public List<String> getEstadosItems() {
		cargarEstadosItems();
		return estadosItems;
	}

	public void setEstadosItems(List<String> estadosItems) {
		this.estadosItems = estadosItems;
	}

	public String getIdEstadoSeleccionado() {
		return idEstadoSeleccionado;
	}

	public void setIdEstadoSeleccionado(String idEstadoSeleccionado) {
		this.idEstadoSeleccionado = idEstadoSeleccionado;
	}
	
	public Boolean getGrupoFirmante() {
		return grupoFirmante;
	}

	public void setGrupoFirmante(Boolean grupoFirmante) {
		this.grupoFirmante = grupoFirmante;
	}
	
	public Date getFechaRespuesta1() {
		return fechaRespuesta1;
	}

	public void setFechaRespuesta1(Date fechaRespuesta1) {
		this.fechaRespuesta1 = fechaRespuesta1;
	}

	public Date getFechaRespuesta2() {
		return fechaRespuesta2;
	}

	public void setFechaRespuesta2(Date fechaRespuesta2) {
		this.fechaRespuesta2 = fechaRespuesta2;
	}

	public Integer getEstaContestada() {
		return estaContestada;
	}

	public void setEstaContestada(Integer estaContestada) {
		this.estaContestada = estaContestada;
	}

	/**
	 * @return the grupoAdminTemplate
	 */
	public Boolean getGrupoAdminTemplate() {
		return grupoAdminTemplate;
	}

	/**
	 * @param grupoAdminTemplate the grupoAdminTemplate to set
	 */
	public void setGrupoAdminTemplate(Boolean grupoAdminTemplate) {
		this.grupoAdminTemplate = grupoAdminTemplate;
	}

	/**
	 * @return the ordenarPor
	 */
	public String getOrdenarPor() {
		return ordenarPor;
	}

	/**
	 * @param ordenarPor the ordenarPor to set
	 */
	public void setOrdenarPor(String ordenarPor) {
		this.ordenarPor = ordenarPor;
	}

	/**
	 * @return the ordenarDir
	 */
	public Boolean getOrdenarDir() {
		return ordenarDir;
	}

	/**
	 * @param ordenarDir the ordenarDir to set
	 */
	public void setOrdenarDir(Boolean ordenarDir) {
		this.ordenarDir = ordenarDir;
	}
}
