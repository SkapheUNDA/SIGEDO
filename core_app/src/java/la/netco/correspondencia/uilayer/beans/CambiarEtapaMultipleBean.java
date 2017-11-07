package la.netco.correspondencia.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
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
import la.netco.core.uilayer.commons.GenericDataModel;
import la.netco.persistencia.dto.commons.Clasificacion;
import la.netco.persistencia.dto.commons.Depend;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Estado;
import la.netco.persistencia.dto.commons.Evento;
import la.netco.persistencia.dto.commons.Seguimientoentrada;
import la.netco.persistencia.dto.commons.Tramite;
import la.netco.persistencia.dto.commons.Transicion;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.richfaces.component.UIDataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@ViewScoped
public class CambiarEtapaMultipleBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	private static Logger log = LoggerFactory.getLogger(CambiarEtapaMultipleBean.class);

	private List<SelectItem> eventosItems;
	private Short idEventoSeleccionado;

	private List<SelectItem> usuariosItems;
	private Short idUsuarioSeleccionado;

	private List<SelectItem> dependenciasItems;
	private Short idDependenciaSeleccionada;

	private List<SelectItem> clasificacionItems;
	private Short idClasificacionSeleccionada;

	private Date fechaRadicacion;

	private List<SelectItem> tramitesItems;
	private Short idTramiteSeleccionado;

	private List<SelectItem> etapasItems;
	private Short idEtapaSeleccionada;

	private EntradasDataModel principalDataModel;
	
	private Short usrCambioEtapa;

	private String obsEtapa;

	private String usuarioLogeado;

	//private Map<Integer, Boolean> idsEntradasSeleccionadas = new HashMap<Integer, Boolean>();
	private Map<Integer, String> idsEntradasSeleccionadas = new HashMap<Integer, String>();
	private List<Entrada> entradasSelecccionadas = null;

	private UIDataTable dataTable;
	//private static String ERROR_SELECCIONE_EVENTO = "corresSeleccioneEventoError";
	//private static String ERROR_SELECCIONE_USUARIO = "corresCambioEtapaSeleccionarUsario";

	public CambiarEtapaMultipleBean() {
		if (usuarioLogeado == null)
			usuarioLogeado = UserDetailsUtils.usernameLogged();
	}

	@SuppressWarnings("unchecked")
	public void cargarEntradasSeleccionadas() {
		try {

			System.out.println("***Cargando Entradas Seleccionadas");

			Entrada inicial;
			boolean primeraVez = true;
			if (entradasSelecccionadas == null) {
				idsEntradasSeleccionadas = (Map<Integer, String>) FacesUtils
						.flashScope(FacesContext.getCurrentInstance()).get(
								"idsEntradasSeleccionadas");
				if (idsEntradasSeleccionadas != null
						&& !idsEntradasSeleccionadas.isEmpty()) {
					entradasSelecccionadas = new ArrayList<Entrada>();

					for (Entry<Integer, String> entradasId : idsEntradasSeleccionadas.entrySet()) {
						if (entradasId.getValue().equals("true")) {
							inicial = (Entrada) serviceDao.getGenericCommonDao().read(Entrada.class, entradasId.getKey());
							entradasSelecccionadas.add(inicial);
							
							if (primeraVez) {
								Clasificacion cla = inicial.getClasificacion();
								
								idTramiteSeleccionado = cla.getTramite().getTrmId();
								idClasificacionSeleccionada = cla.getClaId();
								idEtapaSeleccionada = inicial.getEstado().getEstId();
								idDependenciaSeleccionada = inicial.getDependencia().getDepId();
								
								primeraVez = false;
							}
						}

					}
				}

			}
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.MNUE)
	public String actualizar() {
		String page = null;
		try {

			if (idEventoSeleccionado == null
					|| idEventoSeleccionado.equals(new Integer(0).shortValue())) {
				FacesUtils.addFacesMessage("Debe seleccionar un evento", FacesMessage.SEVERITY_ERROR);
				return page;
			}

			Short idUsuario;
			//for (Entrada entrada : entradasSelecccionadas) {
			//	idUsuario = entrada.getUsrCambioEtapa();
			idUsuario = usrCambioEtapa;
			if (idUsuario == null || idUsuario.equals(new Integer(0).shortValue())) {
				FacesUtils.addFacesMessage("Falta seleccionar el usuario responsable",FacesMessage.SEVERITY_ERROR);
				return page;
			}
			//}

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, idEventoSeleccionado);
			params.put(1, idEtapaSeleccionada);

			Transicion transicion = (Transicion) serviceDao
					.getGenericCommonDao()
					.executeUniqueResult(
							Transicion.NAMED_QUERY_GET_ALL_BY_EVEIDTRSINICIAL,
							params);

			Usuario usuario = (Usuario) serviceDao.getGenericCommonDao().read(Usuario.class,idUsuario);

			Estado fin = transicion.getEstadoByTrsEstfin();

			//Tramite trm = (Tramite) serviceDao.getGenericCommonDao().read(Tramite.class,idTramiteSeleccionado);

			Clasificacion cla = (Clasificacion) serviceDao.getGenericCommonDao().read(Clasificacion.class,idClasificacionSeleccionada);

			Depend dep = (Depend) serviceDao.getGenericCommonDao().read(Depend.class,idDependenciaSeleccionada);

			for (Entrada entrada : entradasSelecccionadas) {
				entrada.setClasificacion(cla);
				entrada.setUsuario(usuario);
				entrada.setEstado(fin);
				entrada.setDependencia(dep);
				serviceDao.getEntradaDao().update(Entrada.class,entrada);

				serviceDao.getSeguimientosDao().addSeguimientoEntrada(
						entrada, transicion,
						Seguimientoentrada.SEGUIMIENTO_CAMBIO_ETAPA,
						obsEtapa,
						UserDetailsUtils.usuarioLogged(), usuario);
			}

			FacesUtils.addFacesMessage("Operación exitosa", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			FacesUtils.addFacesMessage("Error: "+e.getMessage(), FacesMessage.SEVERITY_ERROR);
			log.error("Error: "+e.getMessage());
		}
		return page;
	}
	
//	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.MNUE)
//	@SuppressWarnings("unchecked")
//	public String actualizar() {
//		String page = null;
//		try {
//
//			if (idEventoSeleccionado == null
//					|| idEventoSeleccionado.equals(new Integer(0).shortValue())) {
//				FacesUtils.addFacesMessageFromBundle(ERROR_SELECCIONE_EVENTO,
//						FacesMessage.SEVERITY_ERROR);
//				return page;
//			}
//
////			Map<Object, Entrada> wrappedData = (Map<Object, Entrada>) principalDataModel
////					.getWrappedData();
////			// valida usuario seleccionadao
////			for (Entry<Integer, Boolean> entradasId : idsEntradasSeleccionadas
////					.entrySet()) {
////				if (entradasId.getValue()) {
////					Entrada entradaSeleccionada = wrappedData.get(entradasId
////							.getKey());
////					Short idUsuario = entradaSeleccionada.getUsrCambioEtapa();
////					if (idUsuario == null
////							|| idUsuario.equals(new Integer(0).shortValue())) {
////						FacesUtils.addFacesMessageFromBundle(
////								ERROR_SELECCIONE_USUARIO,
////								FacesMessage.SEVERITY_ERROR);
////						return page;
////					}
////				}
////			}
//
//			for (Entrada entradas : entradasSelecccionadas) {
//				if (entradasId.getValue()) {
//					Entrada entradaSeleccionada = wrappedData.get(entradasId
//							.getKey());
//					Map<Integer, Object> params = new HashMap<Integer, Object>();
//					params.put(0, idEventoSeleccionado);
//					params.put(1, idEtapaSeleccionada);
//
//					Transicion transicion = (Transicion) serviceDao
//							.getGenericCommonDao()
//							.executeUniqueResult(
//									Transicion.NAMED_QUERY_GET_ALL_BY_EVEIDTRSINICIAL,
//									params);
//					Usuario usuario = (Usuario) serviceDao
//							.getGenericCommonDao().read(Usuario.class,
//									entradaSeleccionada.getUsrCambioEtapa());
//
//					entradaSeleccionada.setEstado(transicion
//							.getEstadoByTrsEstfin());
//					entradaSeleccionada.setUsuario(usuario);
//					serviceDao.getEntradaDao().update(Entrada.class,
//							entradaSeleccionada);
//
//					serviceDao.getSeguimientosDao().addSeguimientoEntrada(
//							entradaSeleccionada, transicion,
//							Seguimientoentrada.SEGUIMIENTO_CAMBIO_ETAPA,
//							entradaSeleccionada.getObsEtapa(),
//							UserDetailsUtils.usuarioLogged(), usuario);
//				}
//
//			}
//
//			FacesUtils.addFacesMessageFromBundle(
//					Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
//					FacesMessage.SEVERITY_INFO);
//
//			page = "transaccionExitosa";
//
//		} catch (Exception e) {
//			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
//					FacesMessage.SEVERITY_FATAL);
//			e.printStackTrace();
//		}
//
//		return page;
//	}

	
//	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.MNUE)
//	@SuppressWarnings("unchecked")
//	public String actualizar() {
//		String page = null;
//		try {
//
//			if (idEventoSeleccionado == null
//					|| idEventoSeleccionado.equals(new Integer(0).shortValue())) {
//				FacesUtils.addFacesMessageFromBundle(ERROR_SELECCIONE_EVENTO,
//						FacesMessage.SEVERITY_ERROR);
//				return page;
//			}
//
//			Map<Object, Entrada> wrappedData = (Map<Object, Entrada>) principalDataModel
//					.getWrappedData();
//			// valida usuario seleccionadao
//			for (Entry<Integer, Boolean> entradasId : idsEntradasSeleccionadas
//					.entrySet()) {
//				if (entradasId.getValue()) {
//					Entrada entradaSeleccionada = wrappedData.get(entradasId
//							.getKey());
//					Short idUsuario = entradaSeleccionada.getUsrCambioEtapa();
//					if (idUsuario == null
//							|| idUsuario.equals(new Integer(0).shortValue())) {
//						FacesUtils.addFacesMessageFromBundle(
//								ERROR_SELECCIONE_USUARIO,
//								FacesMessage.SEVERITY_ERROR);
//						return page;
//					}
//				}
//			}
//
//			for (Entry<Integer, Boolean> entradasId : idsEntradasSeleccionadas
//					.entrySet()) {
//				if (entradasId.getValue()) {
//					Entrada entradaSeleccionada = wrappedData.get(entradasId
//							.getKey());
//					Map<Integer, Object> params = new HashMap<Integer, Object>();
//					params.put(0, idEventoSeleccionado);
//					params.put(1, idEtapaSeleccionada);
//
//					Transicion transicion = (Transicion) serviceDao
//							.getGenericCommonDao()
//							.executeUniqueResult(
//									Transicion.NAMED_QUERY_GET_ALL_BY_EVEIDTRSINICIAL,
//									params);
//					Usuario usuario = (Usuario) serviceDao
//							.getGenericCommonDao().read(Usuario.class,
//									entradaSeleccionada.getUsrCambioEtapa());
//
//					entradaSeleccionada.setEstado(transicion
//							.getEstadoByTrsEstfin());
//					entradaSeleccionada.setUsuario(usuario);
//					serviceDao.getEntradaDao().update(Entrada.class,
//							entradaSeleccionada);
//
//					serviceDao.getSeguimientosDao().addSeguimientoEntrada(
//							entradaSeleccionada, transicion,
//							Seguimientoentrada.SEGUIMIENTO_CAMBIO_ETAPA,
//							entradaSeleccionada.getObsEtapa(),
//							UserDetailsUtils.usuarioLogged(), usuario);
//				}
//
//			}
//
//			FacesUtils.addFacesMessageFromBundle(
//					Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
//					FacesMessage.SEVERITY_INFO);
//
//			page = "transaccionExitosa";
//
//		} catch (Exception e) {
//			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
//					FacesMessage.SEVERITY_FATAL);
//			e.printStackTrace();
//		}
//
//		return page;
//	}

	public String cargaFiltrosDataModel() {
		List<Criterion> filtros = new ArrayList<Criterion>();

		if (fechaRadicacion != null && !fechaRadicacion.equals("")) {
			filtros.add(Restrictions.eq("entFen", fechaRadicacion));
		}

		if (idDependenciaSeleccionada != null && idDependenciaSeleccionada != 0) {
			System.out.println(" idDependenciaSeleccionada "
					+ idDependenciaSeleccionada);
			filtros.add(Restrictions.eq("dependencia.depId",
					idDependenciaSeleccionada));
		}

		if (idClasificacionSeleccionada != null
				&& idClasificacionSeleccionada != 0) {
			System.out.println(" idClasificacionSeleccionada "
					+ idClasificacionSeleccionada);
			filtros.add(Restrictions.eq("clasificacion.claId",
					idClasificacionSeleccionada));
		}

		if (idEtapaSeleccionada != null && idEtapaSeleccionada != 0) {
			filtros.add(Restrictions.eq("estado.estId", idEtapaSeleccionada));
		}
		filtros.add(Restrictions.eq("usuario.usrLog", usuarioLogeado));

		if (principalDataModel == null) {
			principalDataModel = new EntradasDataModel();
		}

		Map<String, String> alias = new HashMap<String, String>();
		alias.put("usuario", "usuario");
		principalDataModel.setAlias(alias);
		principalDataModel.setFiltros(filtros);
		return "";
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
		}
	}

	@SuppressWarnings("unchecked")
	public void cargarClasificacionItems() {
		if (idTramiteSeleccionado != null) {

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_ENTRADA);
			params.put(1, idTramiteSeleccionado);
			//params.put(0, Clasificacion.TIPO_ENTRADA);
			//params.put(1, idDependenciaSeleccionada);

			List<Clasificacion> clasificaciones = (List<Clasificacion>) serviceDao
					.getGenericCommonDao().executeFind(Depend.class, params,
							Clasificacion.NAMED_QUERY_ALL_BY_TRAMITE);

			clasificacionItems = new ArrayList<SelectItem>();

			for (Clasificacion clasificacion : clasificaciones) {
				clasificacionItems.add(new SelectItem(clasificacion.getClaId(),
						clasificacion.getClaNom()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void cargarTramitesItems() {

		if (tramitesItems == null) {

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_ENTRADA);

			List<Tramite> tramites = (List<Tramite>) serviceDao
					.getGenericCommonDao().executeFind(Tramite.class, params,
							Tramite.NAMED_QUERY_DISTINCT_TIPO_CLASIFICACION);

			tramitesItems = new ArrayList<SelectItem>();

			for (Tramite tramite : tramites) {
				tramitesItems.add(new SelectItem(tramite.getTrmId(), tramite
						.getTrmNom()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void cargarEtapasItems() {
		if (idTramiteSeleccionado != null) {

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, idTramiteSeleccionado);

			List<Estado> estados = (List<Estado>) serviceDao
					.getGenericCommonDao().executeFind(Estado.class, params,
							Estado.NAMED_QUERY_GET_BY_TRAMITE);
			etapasItems = new ArrayList<SelectItem>();

			for (Estado estado : estados) {
				etapasItems.add(new SelectItem(estado.getEstId(), estado
						.getEstNom()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void cargarEventosItems() {
		if (idEtapaSeleccionada != null) {

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, idEtapaSeleccionada);
			params.put(1, idTramiteSeleccionado);

			List<Transicion> transiciones = (List<Transicion>) serviceDao
					.getGenericCommonDao().executeFind(Transicion.class,
							params,
							Transicion.NAMED_QUERY_BY_TRAMITE_ESTADO_INICIAL);

			eventosItems = new ArrayList<SelectItem>();

			for (Transicion transicion : transiciones) {
				Evento evento = transicion.getEvento();
				eventosItems.add(new SelectItem(evento.getEveId(), evento
						.getEveNom()));
			}
		}
	}

	public Date getFechaActual() {
		return new Date(System.currentTimeMillis());
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public List<SelectItem> getEventosItems() {
		cargarEventosItems();
		return eventosItems;
	}

	public Short getIdEventoSeleccionado() {
		return idEventoSeleccionado;
	}

	public void setEventosItems(List<SelectItem> eventosItems) {
		this.eventosItems = eventosItems;
	}

	public void setIdEventoSeleccionado(Short idEventoSeleccionado) {
		this.idEventoSeleccionado = idEventoSeleccionado;
	}

	public List<SelectItem> getUsuariosItems() {
		cargarUsuariosItems();
		return usuariosItems;
	}

	public Short getIdUsuarioSeleccionado() {
		return idUsuarioSeleccionado;
	}

	public void setUsuariosItems(List<SelectItem> usuariosItems) {
		this.usuariosItems = usuariosItems;
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

	public List<SelectItem> getTramitesItems() {
		cargarTramitesItems();
		return tramitesItems;
	}

	public Short getIdTramiteSeleccionado() {
		return idTramiteSeleccionado;
	}

	public void setTramitesItems(List<SelectItem> tramitesItems) {
		this.tramitesItems = tramitesItems;
	}

	public void setIdTramiteSeleccionado(Short idTramiteSeleccionado) {
		this.idTramiteSeleccionado = idTramiteSeleccionado;
	}

	public List<SelectItem> getEtapasItems() {
		cargarEtapasItems();
		return etapasItems;
	}

	public Short getIdEtapaSeleccionada() {
		return idEtapaSeleccionada;
	}

	public void setEtapasItems(List<SelectItem> etapasItems) {
		this.etapasItems = etapasItems;
	}

	public void setIdEtapaSeleccionada(Short idEtapaSeleccionada) {
		this.idEtapaSeleccionada = idEtapaSeleccionada;
	}

	public Date getFechaRadicacion() {
		return fechaRadicacion;
	}

	public void setFechaRadicacion(Date fechaRadicacion) {
		this.fechaRadicacion = fechaRadicacion;
	}

	private static final class EntradasDataModel extends
			GenericDataModel<Entrada, Integer> {
		private static final long serialVersionUID = 1L;

		private EntradasDataModel() {
			super(Entrada.class);
			setOrderBy(Order.desc("entFen"));
		}

		@Override
		protected Object getId(Entrada t) {
			return t.getEntId();
		}
	}

	public EntradasDataModel getPrincipalDataModel() {
		return principalDataModel;
	}

	public void setPrincipalDataModel(EntradasDataModel principalDataModel) {
		this.principalDataModel = principalDataModel;
	}

	public UIDataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(UIDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public Map<Integer, String> getIdsEntradasSeleccionadas() {
		return idsEntradasSeleccionadas;
	}

	public void setIdsEntradasSeleccionadas(
			Map<Integer, String> idsEntradasSeleccionadas) {
		this.idsEntradasSeleccionadas = idsEntradasSeleccionadas;
	}

	public List<Entrada> getEntradasSelecccionadas() {
		return entradasSelecccionadas;
	}

	public void setEntradasSelecccionadas(List<Entrada> entradasSelecccionadas) {
		this.entradasSelecccionadas = entradasSelecccionadas;
	}
	
	public Short getUsrCambioEtapa() {
		return usrCambioEtapa;
	}

	public void setUsrCambioEtapa(Short usrCambioEtapa) {
		this.usrCambioEtapa = usrCambioEtapa;
	}
	
	public String getObsEtapa() {
		return obsEtapa;
	}

	public void setObsEtapa(String obsEtapa) {
		this.obsEtapa = obsEtapa;
	}
}
