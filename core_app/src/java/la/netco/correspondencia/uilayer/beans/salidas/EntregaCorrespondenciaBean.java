package la.netco.correspondencia.uilayer.beans.salidas;

import java.io.Serializable;
import java.util.ArrayList;
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

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.Medioscorrespondencia;
import la.netco.persistencia.dto.commons.Salida;
import la.netco.persistencia.dto.commons.Seguimientosalida;
import la.netco.persistencia.dto.commons.Transicion;

@ManagedBean
@ViewScoped
public class EntregaCorrespondenciaBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;
	private Salida salidaSeleccionada;
	private String observacion;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	private List<SelectItem> usuariosItems;	
	private Short 	idUsuarioSeleccionado;
	
	private List<SelectItem> mediosItems;
	private Short idMedioSeleccionada;
	
	private Date fechaHoraRecibido;
	private String personaRecibe;
	
	public EntregaCorrespondenciaBean(){
		
	}
	
	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	@SuppressWarnings("unchecked")
	public String actualizar(){
		String page =	null;
		try {
				
			Usuario usuario = (Usuario)serviceDao.getGenericCommonDao().read(Usuario.class, idUsuarioSeleccionado);
			Medioscorrespondencia medio = (Medioscorrespondencia)serviceDao.getGenericCommonDao().read(Medioscorrespondencia.class, idMedioSeleccionada);
			
			salidaSeleccionada.setSalLent(true);
			salidaSeleccionada.setUsuarioBySalUen(usuario);
			salidaSeleccionada.setSalPen(personaRecibe);
			salidaSeleccionada.setSalFen(fechaHoraRecibido);
			salidaSeleccionada.setSalHen(fechaHoraRecibido);
			salidaSeleccionada.setMedio(medio);
			salidaSeleccionada.setSalOde(observacion);
			serviceDao.getGenericCommonDao().update(Salida.class, salidaSeleccionada);
			

			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, salidaSeleccionada.getSalId());
			List<Seguimientosalida> seguimientos = (List<Seguimientosalida>)serviceDao.getEntradaDao().executeFindPaged(0,1, params, Seguimientosalida.NAMED_QUERY_GET_BY_SALIDA);
			Seguimientosalida seguimiento = seguimientos.get(0);
			
			Transicion transicion = new Transicion();
			transicion.setEstadoByTrsEstfin(seguimiento.getEstadoBySsaEst());
			transicion.setEstadoByTrsEstini(seguimiento.getEstadoBySsaEstini());
			transicion.setEvento(seguimiento.getEvento());
			
			serviceDao.getSeguimientosDao().addSeguimientoSalida(salidaSeleccionada, transicion, Seguimientosalida.SEGUIMIENTO_ENTREGADO, observacion, UserDetailsUtils.usuarioLogged(), usuario);
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
			
			page = "transaccionExitosa";
			salidaSeleccionada = new Salida();
				
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
		
		return page;
	}
	
	public void cargarRegistroDetalleResumen(){		
		try {						
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);		
			}
			
			if((salidaSeleccionada == null || salidaSeleccionada.getSalId() == null) 	&&  (idRegSeleccionado != null)){
				salidaSeleccionada =(Salida) serviceDao.getGenericCommonDao().read(Salida.class, idRegSeleccionado);
				
				if(salidaSeleccionada.getSalLent() != null && salidaSeleccionada.getSalLent().equals(Boolean.TRUE)){
					idUsuarioSeleccionado = salidaSeleccionada.getUsuarioBySalUen().getUsrId();
					personaRecibe = salidaSeleccionada.getSalPen();
					fechaHoraRecibido = salidaSeleccionada.getSalFen();
					idMedioSeleccionada = salidaSeleccionada.getMedio().getMedId();
					observacion = salidaSeleccionada.getSalOde();
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
	
	@SuppressWarnings("unchecked")	
	public void cargarMediosItems(){

		if(mediosItems == null){			

			List<Medioscorrespondencia> allData = (List<Medioscorrespondencia>) serviceDao.getGenericCommonDao().executeFind(Medioscorrespondencia.NAMED_QUERY_ALL);
			 
			mediosItems = new ArrayList<SelectItem>();
			 
			for (Medioscorrespondencia dato : allData) {
				mediosItems.add(new SelectItem(dato.getMedId(), dato.getMedNom()));
			}
			
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

	public List<SelectItem> getMediosItems() {
		cargarMediosItems();
		return mediosItems;
	}

	public Short getIdMedioSeleccionada() {
		return idMedioSeleccionada;
	}

	public void setMediosItems(List<SelectItem> mediosItems) {
		this.mediosItems = mediosItems;
	}

	public void setIdMedioSeleccionada(Short idMedioSeleccionada) {
		this.idMedioSeleccionada = idMedioSeleccionada;
	}

	public Date getFechaHoraRecibido() {
		return fechaHoraRecibido;
	}

	public void setFechaHoraRecibido(Date fechaHoraRecibido) {
		this.fechaHoraRecibido = fechaHoraRecibido;
	}

	public String getPersonaRecibe() {
		return personaRecibe;
	}

	public void setPersonaRecibe(String personaRecibe) {
		this.personaRecibe = personaRecibe;
	}	
	
}
