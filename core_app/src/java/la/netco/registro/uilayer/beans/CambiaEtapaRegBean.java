package la.netco.registro.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
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
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Evento;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.Registro;
import la.netco.persistencia.dto.commons.Transicion;

/**
 * @author cpineros
 * 
 */
@ManagedBean
@ViewScoped
public class CambiaEtapaRegBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao daoServicio;
	private Integer idRegSeleccionado;
	private String observacion;
	private Registro registroSeleccionado;

	private List<SelectItem> eventosItems;
	private Short idEventoSeleccionado;

	private List<SelectItem> usuariosItems;
	private Short idUsuarioSeleccionado;
	private List<Registro> registrosSeleccionados = null;
	private Map<Integer, String> idsRegistrosSeleccionadas = new HashMap<Integer, String>();

	public Map<Integer, String> getIdsRegistrosSeleccionadas() {
		return idsRegistrosSeleccionadas;
	}

	public void setIdsRegistrosSeleccionadas(
			Map<Integer, String> idsRegistrosSeleccionadas) {
		this.idsRegistrosSeleccionadas = idsRegistrosSeleccionadas;
	}

	public List<Registro> getRegistrosSeleccionados() {
		return registrosSeleccionados;
	}

	public void setRegistrosSeleccionados(List<Registro> registrosSeleccionados) {
		this.registrosSeleccionados = registrosSeleccionados;
	}

	public CambiaEtapaRegBean() {
		System.out.println("Construcción Cambio de Etapa Registro");
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public List<SelectItem> getUsuariosItems() {
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

	public ServiceDao getDaoServicio() {
		return daoServicio;
	}

	public void setDaoServicio(ServiceDao daoServicio) {
		this.daoServicio = daoServicio;
	}

	public Registro getRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public void setRegistroSeleccionado(Registro registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public List<SelectItem> getEventosItems() {
		return eventosItems;
	}

	public void setEventosItems(List<SelectItem> eventosItems) {
		this.eventosItems = eventosItems;
	}

	public Short getIdEventoSeleccionado() {
		return idEventoSeleccionado;
	}

	public void setIdEventoSeleccionado(Short idEventoSeleccionado) {
		this.idEventoSeleccionado = idEventoSeleccionado;
	}

	@SuppressWarnings("unchecked")
	public void cargarRegistro() {
		try {
			if (idRegSeleccionado == null) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");
				if (idRegSeleccionado != null)
					this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);
			}

			if ((registroSeleccionado == null || registroSeleccionado.getRegId() == null) && (idRegSeleccionado != null)) {
				registroSeleccionado = (Registro) daoServicio.getGenericCommonDao().read(Registro.class,	idRegSeleccionado);
				if (eventosItems == null) {

					Map<Integer, Object> params = new HashMap<Integer, Object>();
					params.put(0, registroSeleccionado.getExpediente().getEstado().getEstId());
					params.put(1, registroSeleccionado.getExpediente().getTramite().getTrmId());

					List<Transicion> transiciones = (List<Transicion>) daoServicio.getGenericCommonDao().executeFind(Transicion.class,	params,	Transicion.NAMED_QUERY_BY_TRAMITE_ESTADO_INICIAL);

					eventosItems = new ArrayList<SelectItem>();

					for (Transicion transicion : transiciones) {
						Evento evento = transicion.getEvento();
						eventosItems.add(new SelectItem(evento.getEveId(), evento.getEveNom()));
					}
					cargarUsuariosItems();
				}
			}
			
			if (idRegSeleccionado == null && registrosSeleccionados == null) {
				idsRegistrosSeleccionadas = (Map<Integer, String>) FacesUtils.flashScope(FacesContext.getCurrentInstance()).get("idsRegistrosSeleccionadas");
				if (idsRegistrosSeleccionadas != null && !idsRegistrosSeleccionadas.isEmpty()) {
					registrosSeleccionados = new ArrayList<Registro>();

					for (Entry<Integer, String> registrosId : idsRegistrosSeleccionadas.entrySet()) {
						System.out.println(" registrosId.getValue() " + registrosId.getValue());
						if (registrosId.getValue().equals("true")) {
							registrosSeleccionados.add((Registro) serviceDao.getGenericCommonDao().read(Registro.class, registrosId.getKey()));
							registroSeleccionado = (Registro) daoServicio.getGenericCommonDao().read(Registro.class,	registrosId.getKey());
						}
					}
					
					if (eventosItems == null) {

						Map<Integer, Object> params = new HashMap<Integer, Object>();
						params.put(0, registroSeleccionado.getExpediente().getEstado().getEstId());
						params.put(1, registroSeleccionado.getExpediente().getTramite().getTrmId());
						registroSeleccionado=null;

						List<Transicion> transiciones = (List<Transicion>) daoServicio.getGenericCommonDao().executeFind(Transicion.class,	params,	Transicion.NAMED_QUERY_BY_TRAMITE_ESTADO_INICIAL);

						eventosItems = new ArrayList<SelectItem>();

						for (Transicion transicion : transiciones) {
							Evento evento = transicion.getEvento();
							eventosItems.add(new SelectItem(evento.getEveId(), evento.getEveNom()));
						}
						cargarUsuariosItems();
					}

				}
			}

			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String cambiaEtapaReg() {
		String page = null;
		try {

			if(registroSeleccionado!=null){
				Expediente objExpediente = registroSeleccionado.getExpediente();
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, idEventoSeleccionado);
				params.put(1, registroSeleccionado.getExpediente().getEstado().getEstId());
				Transicion transicion = (Transicion) daoServicio.getGenericCommonDao().executeUniqueResult(Transicion.NAMED_QUERY_GET_ALL_BY_EVEIDTRSINICIAL,params);
				
				Usuario usuario = (Usuario) daoServicio.getGenericCommonDao().read(	Usuario.class, idUsuarioSeleccionado);
	
				objExpediente.setEstado(transicion.getEstadoByTrsEstfin());
				objExpediente.setUsuarioTemp(usuario);
	
				//registroSeleccionado.setExpediente(objExpediente);
	
				daoServicio.getGenericCommonDao().update(Expediente.class,objExpediente);
	
				daoServicio.getSeguimientosDao().addSeguimientoExp(objExpediente,	ConstantsKeysFire.CAMETAPA, observacion,UserDetailsUtils.usuarioLogged(), usuario, transicion);

			}
			if(registrosSeleccionados!=null){
				for (Registro registro : registrosSeleccionados) {
					
					Expediente objExpediente = registro.getExpediente();
					Map<Integer, Object> params = new HashMap<Integer, Object>();
					params.put(0, idEventoSeleccionado);
					params.put(1, registro.getExpediente().getEstado().getEstId());
					Transicion transicion = (Transicion) daoServicio.getGenericCommonDao().executeUniqueResult(Transicion.NAMED_QUERY_GET_ALL_BY_EVEIDTRSINICIAL,params);
					
					Usuario usuario = (Usuario) daoServicio.getGenericCommonDao().read(	Usuario.class, idUsuarioSeleccionado);
		
					objExpediente.setEstado(transicion.getEstadoByTrsEstfin());
					objExpediente.setUsuarioTemp(usuario);
		
					//registroSeleccionado.setExpediente(objExpediente);
		
					daoServicio.getGenericCommonDao().update(Expediente.class,objExpediente);
		
					daoServicio.getSeguimientosDao().addSeguimientoExp(objExpediente,	ConstantsKeysFire.CAMETAPA, observacion,UserDetailsUtils.usuarioLogged(), usuario, transicion);
				}
				
			}
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,FacesMessage.SEVERITY_INFO);

			page = "transaccionExitosa";
			registroSeleccionado = new Registro();

		} catch (Exception e) {
			System.out.println("--error cambio de etapa "+e);
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}

		return page;
	}

	@SuppressWarnings("unchecked")
	public void cargarUsuariosItems() {
		if (usuariosItems == null) {
			List<Usuario> usuarios = (List<Usuario>) daoServicio
					.getGenericCommonDao().executeFind(
							Usuario.NAMED_QUERY_ALL_USUARIOS_ACTIVOS);
			usuariosItems = new ArrayList<SelectItem>();
			for (Usuario usu : usuarios) {
				usuariosItems.add(new SelectItem(usu.getUsrId(), usu.getUsrNom()+ " "+ usu.getUsrPap()+ " "+ usu.getUsrSap()));
			}
		} else if (usuariosItems == null) {
			usuariosItems = new ArrayList<SelectItem>();
		}
	}

}
