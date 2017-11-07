package la.netco.correspondencia.uilayer.beans.salidas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.Evento;
import la.netco.persistencia.dto.commons.Salida;
import la.netco.persistencia.dto.commons.Seguimientoentrada;
import la.netco.persistencia.dto.commons.Transicion;

@ManagedBean
@ViewScoped
public class CambiarEtapaSalidasBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;
	private Salida salidaSeleccionada;
	private String observacion;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	private List<SelectItem> eventosItems;
	private Short idEventoSeleccionado;
	
	private List<SelectItem> usuariosItems;	
	private Short 	idUsuarioSeleccionado;
	
	public CambiarEtapaSalidasBean(){
		
	}
	
	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	public String actualizar(){
		String page =	null;
		try {
				
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, idEventoSeleccionado);
			params.put(1, salidaSeleccionada.getEstado().getEstId());
			
			Transicion transicion = (Transicion) serviceDao.getGenericCommonDao().executeUniqueResult(Transicion.NAMED_QUERY_GET_ALL_BY_EVEIDTRSINICIAL, params);
			Usuario usuario = (Usuario)serviceDao.getGenericCommonDao().read(Usuario.class, idUsuarioSeleccionado);
			
			salidaSeleccionada.setEstado(transicion.getEstadoByTrsEstfin());
			salidaSeleccionada.setUsuarioBySalUsr(usuario);
			
			serviceDao.getGenericCommonDao().update(Salida.class, salidaSeleccionada);
			
			serviceDao.getSeguimientosDao().addSeguimientoSalida(salidaSeleccionada, transicion, Seguimientoentrada.SEGUIMIENTO_CAMBIO_ETAPA, observacion, UserDetailsUtils.usuarioLogged(), usuario);
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
			
			page = "transaccionExitosa";
			salidaSeleccionada = new Salida();
				
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
		
		return page;
	}
	
	
	@SuppressWarnings("unchecked")
	public void cargarRegistroDetalleResumen(){		
		try {						
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);		
			}
			
			if((salidaSeleccionada == null || salidaSeleccionada.getSalId() == null) 	&&  (idRegSeleccionado != null)){
				salidaSeleccionada =(Salida) serviceDao.getGenericCommonDao().read(Salida.class, idRegSeleccionado);
				
			}			
			
			if(eventosItems == null){

				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, salidaSeleccionada.getEstado().getEstId());
				params.put(1, salidaSeleccionada.getClasificacion().getTramite().getTrmId());
				
				List<Transicion> transiciones = (List<Transicion>) serviceDao.getGenericCommonDao().executeFind(Transicion.class, params, Transicion.NAMED_QUERY_BY_TRAMITE_ESTADO_INICIAL);
				 
				eventosItems = new ArrayList<SelectItem>();
				 
				for (Transicion transicion : transiciones) {
					Evento evento = transicion.getEvento();
					eventosItems.add(new SelectItem(evento.getEveId(), evento.getEveNom()));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
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

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Salida getSalidaSeleccionada() {
		return salidaSeleccionada;
	}

	public void setSalidaSeleccionada(Salida salidaSeleccionada) {
		this.salidaSeleccionada = salidaSeleccionada;
	}	
	
}
