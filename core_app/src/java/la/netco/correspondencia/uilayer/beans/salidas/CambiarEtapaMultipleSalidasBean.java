package la.netco.correspondencia.uilayer.beans.salidas;

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
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.core.uilayer.commons.GenericDataModel;
import la.netco.persistencia.dto.commons.Clasificacion;
import la.netco.persistencia.dto.commons.Depend;
import la.netco.persistencia.dto.commons.Estado;
import la.netco.persistencia.dto.commons.Evento;
import la.netco.persistencia.dto.commons.Salida;
import la.netco.persistencia.dto.commons.Seguimientoentrada;
import la.netco.persistencia.dto.commons.Tramite;
import la.netco.persistencia.dto.commons.Transicion;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.richfaces.component.UIDataTable;

@ManagedBean
@ViewScoped
public class CambiarEtapaMultipleSalidasBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	private List<SelectItem> eventosItems;
	private Short idEventoSeleccionado;
	
	private List<SelectItem> usuariosItems;	
	private Short 	idUsuarioSeleccionado;	

	private List<SelectItem> dependenciasItems;
	private Short idDependenciaSeleccionada;
	
	private List<SelectItem> clasificacionItems;
	private Short 	idClasificacionSeleccionada;
	
	private Date	fechaRadicacion;
	
	private List<SelectItem> tramitesItems;
	private Short 	idTramiteSeleccionado;
	
	private List<SelectItem> etapasItems;
	private Short 	idEtapaSeleccionada;	

	private SalidasDataModel principalDataModel;
	
	private String usuarioLogeado;
	
	private Map<Integer, Boolean> idsSalidasSeleccionadas = new HashMap<Integer, Boolean>();
	
	private UIDataTable dataTable;
	private static String ERROR_SELECCIONE_EVENTO = "corresSeleccioneEventoError";
	private static String ERROR_SELECCIONE_USUARIO = "corresCambioEtapaSeleccionarUsario";
	
	public CambiarEtapaMultipleSalidasBean(){
		if(usuarioLogeado == null)
			usuarioLogeado = UserDetailsUtils.usernameLogged();
	}
	
	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	@SuppressWarnings("unchecked")
	public String actualizar(){
		String page =	null;
		try {
				
			
			
			if(idEventoSeleccionado == null || idEventoSeleccionado.equals(new Integer(0).shortValue())){
				FacesUtils.addFacesMessageFromBundle(ERROR_SELECCIONE_EVENTO,FacesMessage.SEVERITY_ERROR);
				return page;
			}

			Map<Object,Salida> wrappedData = (Map<Object, Salida>) principalDataModel.getWrappedData();
			//valida usuario seleccionadao
			for (Entry<Integer, Boolean> salidasId : idsSalidasSeleccionadas.entrySet()) {
				if( salidasId.getValue()){
					Salida entradaSeleccionada = wrappedData.get(salidasId.getKey());
					Short idUsuario = entradaSeleccionada.getUsrCambioEtapa();
					if(idUsuario == null || idUsuario.equals(new Integer(0).shortValue())){
						FacesUtils.addFacesMessageFromBundle(ERROR_SELECCIONE_USUARIO,FacesMessage.SEVERITY_ERROR);
						return page;
					}
				}
			}
			
			for (Entry<Integer, Boolean> salidasId : idsSalidasSeleccionadas.entrySet()) {
				if( salidasId.getValue()){
					Salida salidaSeleccionada = wrappedData.get(salidasId.getKey());
					Map<Integer, Object> params = new HashMap<Integer, Object>();
					params.put(0, idEventoSeleccionado);
					params.put(1, idEtapaSeleccionada);
					
					Transicion transicion = (Transicion) serviceDao.getGenericCommonDao().executeUniqueResult(Transicion.NAMED_QUERY_GET_ALL_BY_EVEIDTRSINICIAL, params);
					Usuario usuario = (Usuario)serviceDao.getGenericCommonDao().read(Usuario.class,salidaSeleccionada.getUsrCambioEtapa());
					
					salidaSeleccionada.setEstado(transicion.getEstadoByTrsEstfin());
					salidaSeleccionada.setUsuarioBySalUsr(usuario);					
					serviceDao.getEntradaDao().update(Salida.class, salidaSeleccionada );	
					
					serviceDao.getSeguimientosDao().addSeguimientoSalida(salidaSeleccionada , transicion, Seguimientoentrada.SEGUIMIENTO_CAMBIO_ETAPA, salidaSeleccionada.getObsEtapa(), UserDetailsUtils.usuarioLogged(), usuario);
				}
				
			}
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
			
			page = "transaccionExitosa";
				
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
		
		return page;
	}
	
	public String cargaFiltrosDataModel(){
    	List<Criterion> filtros = new ArrayList<Criterion>();
    	
    	if(fechaRadicacion != null  && !fechaRadicacion.equals("") ){
    		filtros.add(Restrictions.eq("salFsa", fechaRadicacion));
    	}
    
    	if(idDependenciaSeleccionada != null && idDependenciaSeleccionada != 0){
    		System.out.println(" idDependenciaSeleccionada " + idDependenciaSeleccionada );
    		filtros.add(Restrictions.eq("depend.depId", idDependenciaSeleccionada));
    	}
    	
    	if(idClasificacionSeleccionada != null && idClasificacionSeleccionada != 0){
    		System.out.println(" idClasificacionSeleccionada " + idClasificacionSeleccionada );
    		filtros.add(Restrictions.eq("clasificacion.claId", idClasificacionSeleccionada));
    	}
    	
    	if(idEtapaSeleccionada != null && idEtapaSeleccionada != 0){
    		filtros.add(Restrictions.eq("estado.estId", idEtapaSeleccionada));
    	}
    	filtros.add(Restrictions.eq("usuarioBySalUsr.usrLog", usuarioLogeado));
    	
    	if(principalDataModel == null){
    		principalDataModel = new SalidasDataModel();
    	}
    	
    	Map<String, String> alias = new HashMap<String, String>();
    	alias.put("usuarioBySalUsr", "usuarioBySalUsr");
    	principalDataModel.setAlias(alias);
    	principalDataModel.setFiltros(filtros);
    	return "";
	}
	
	
	@SuppressWarnings("unchecked")	
	public void cargarUsuariosItems(){
		if(usuariosItems == null){	
			List<Usuario> usuarios = (List<Usuario>) serviceDao.getGenericCommonDao().executeFind(Usuario.NAMED_QUERY_ALL_USUARIOS_ACTIVOS);
			usuariosItems = new ArrayList<SelectItem>();			 
			for (Usuario usu : usuarios) {
				usuariosItems.add(new SelectItem(usu.getUsrId(), usu.getUsrNom() + " " + usu.getUsrPap() + " " + usu.getUsrSap()));
			}			
		}else if(usuariosItems == null){
			usuariosItems = new ArrayList<SelectItem>();
		}
	}
	
	@SuppressWarnings("unchecked")	
	public void cargarDependenciasItems(){

		if(dependenciasItems == null){
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_SALIDA);

			List<Depend> dependencias = (List<Depend>) serviceDao.getGenericCommonDao().executeFind(Depend.class, params, Depend.NAMED_QUERY_ALL_DEPEND_BY_TIPO_CLASIFICACION);
			 
			dependenciasItems = new ArrayList<SelectItem>();
			 
			for (Depend dependencia : dependencias) {
				dependenciasItems.add(new SelectItem(dependencia.getDepId(), dependencia.getDepNom()));
			}
		}
	}
	
	

	@SuppressWarnings("unchecked")	
	public void cargarClasificacionItems(){
		if(idDependenciaSeleccionada != null){
		
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_SALIDA);
			params.put(1, idDependenciaSeleccionada);

			List<Clasificacion> clasificaciones = (List<Clasificacion>) serviceDao.getGenericCommonDao().executeFind(Depend.class, params, Clasificacion.NAMED_QUERY_ALL_BY_TIPO_DEPENDENCIA);
			
		
			clasificacionItems = new ArrayList<SelectItem>();
			 
			for (Clasificacion clasificacion : clasificaciones) {
				clasificacionItems.add(new SelectItem(clasificacion.getClaId(), clasificacion.getClaNom()));
			}
		}
	}
	
	@SuppressWarnings("unchecked")	
	public void cargarTramitesItems(){

		if(tramitesItems == null){
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, Clasificacion.TIPO_SALIDA);

			List<Tramite> tramites = (List<Tramite>) serviceDao.getGenericCommonDao().executeFind(Tramite.class, params, Tramite.NAMED_QUERY_DISTINCT_TIPO_CLASIFICACION);
			 
			tramitesItems = new ArrayList<SelectItem>();
			 
			for (Tramite tramite : tramites) {
				tramitesItems.add(new SelectItem(tramite.getTrmId(), tramite.getTrmNom()));
			}
		}
	}
	
	@SuppressWarnings("unchecked")	
	public void cargarEtapasItems(){
		if(idTramiteSeleccionado != null){
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, idTramiteSeleccionado);

			List<Estado> estados = (List<Estado>) serviceDao.getGenericCommonDao().executeFind(Estado.class, params, Estado.NAMED_QUERY_GET_BY_TRAMITE);
			etapasItems = new ArrayList<SelectItem>();
			 
			for (Estado estado : estados) {
				etapasItems.add(new SelectItem(estado.getEstId(), estado.getEstNom()));
			}
		}
	}
	
	@SuppressWarnings("unchecked")	
	public void cargarEventosItems(){
		if(idEtapaSeleccionada != null){

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, idEtapaSeleccionada);
			params.put(1, idTramiteSeleccionado);
			
			List<Transicion> transiciones = (List<Transicion>) serviceDao.getGenericCommonDao().executeFind(Transicion.class, params, Transicion.NAMED_QUERY_BY_TRAMITE_ESTADO_INICIAL);
			 
			eventosItems = new ArrayList<SelectItem>();
			 
			for (Transicion transicion : transiciones) {
				Evento evento = transicion.getEvento();
				eventosItems.add(new SelectItem(evento.getEveId(), evento.getEveNom()));
			}
		}
	}
	
	
	public Date getFechaActual(){
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
	
	private static final class SalidasDataModel extends GenericDataModel<Salida, Integer> {
		private static final long serialVersionUID = 1L;
		
		private SalidasDataModel() {
			super(Salida.class);
			setOrderBy(Order.desc("salFsa"));
		}
		
		@Override
		protected Object getId(Salida t) {
			return t.getSalId();
		}
	}

	public SalidasDataModel getPrincipalDataModel() {
		return principalDataModel;
	}

	public void setPrincipalDataModel(SalidasDataModel principalDataModel) {
		this.principalDataModel = principalDataModel;
	}

	public UIDataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(UIDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public Map<Integer, Boolean> getIdsSalidasSeleccionadas() {
		return idsSalidasSeleccionadas;
	}

	public void setIdsSalidasSeleccionadas(Map<Integer, Boolean> idsSalidasSeleccionadas) {
		this.idsSalidasSeleccionadas = idsSalidasSeleccionadas;
	}
}
