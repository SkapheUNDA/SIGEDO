package la.netco.correspondencia.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Evento;
import la.netco.persistencia.dto.commons.Seguimientoentrada;
import la.netco.persistencia.dto.commons.Transicion;

@ManagedBean(name="cambiarEtapaBean")
@ViewScoped
public class CambiarEtapaBean implements Serializable{

	private static Logger log = Logger.getLogger(CambiarEtapaBean.class.getName());
	
	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;
	private Entrada entradaSeleccionada;
	private String observacion;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	private List<SelectItem> eventosItems;
	private Short idEventoSeleccionado;
	
	private List<SelectItem> usuariosItems;	
	private Short 	idUsuarioSeleccionado;
	private String diagramaFlujo = "";
	
	public CambiarEtapaBean(){
		
	}
	
	public void cargarFlujo() {
		log.info("Cargar flujo!");
		diagramaFlujo = "";
		try {
			Integer TRM_ID = (int) entradaSeleccionada.getClasificacion().getTramite().getTrmId();

			String query = "SELECT '\"'+convert(varchar, E1.EST_ID)+' '+E1.EST_NOM+'\" -> \"'+convert(varchar, E2.EST_ID)+' '+E2.EST_NOM+'\" [ label = \"'+convert(varchar, EVN.EVE_ID)+' '+EVN.EVE_NOM+'\"];' as texto "+
					"FROM TRAMITE TRM "+
					"INNER JOIN TRANSICION TRA "+
					"ON TRA.TRS_TRM = TRM.TRM_ID "+
					"INNER JOIN ESTADO E1 "+
					"ON E1.EST_ID = TRA.TRS_ESTINI "+
					"INNER JOIN ESTADO E2 "+
					"ON E2.EST_ID = TRA.TRS_ESTFIN "+
					"LEFT JOIN EVENTO EVN "+
					"ON EVN.EVE_ID = TRA.TRS_EVE "+
					"WHERE TRM.TRM_ID = ? ";

			if (TRM_ID != null) {
				List<Map<String, Object>> lectura = null;
				lectura = serviceDao.getSpringGenericDao().executeQuery(query, TRM_ID);
				if (lectura != null) {
					diagramaFlujo += "digraph G {\nnode [style = filled];\n";
					for (Map<String, Object> elem : lectura) {
						diagramaFlujo += elem.get("texto").toString()+"\n";
					}
					diagramaFlujo += "\""+entradaSeleccionada.getEstado().getEstId()+" "+entradaSeleccionada.getEstado().getEstNom()+"\" [color=greenyellow];\n";
					diagramaFlujo += "}";
				}
			}
		} catch (Exception e) {
			log.info("No se pudo cargar el flujo "+e.getMessage());
		}
	}
	
	public String actualizar(){
		log.info("actualizar!");
		String page =	null;
		try {
				
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, idEventoSeleccionado);
			params.put(1, entradaSeleccionada.getEstado().getEstId());
			
			Transicion transicion = (Transicion) serviceDao.getGenericCommonDao().executeUniqueResult(Transicion.NAMED_QUERY_GET_ALL_BY_EVEIDTRSINICIAL, params);
			Usuario usuario = (Usuario)serviceDao.getGenericCommonDao().read(Usuario.class, idUsuarioSeleccionado);
			
			entradaSeleccionada.setEstado(transicion.getEstadoByTrsEstfin());
			entradaSeleccionada.setUsuario(usuario);
			
			serviceDao.getEntradaDao().update(Entrada.class, entradaSeleccionada);
			
			serviceDao.getSeguimientosDao().addSeguimientoEntrada(entradaSeleccionada, transicion, Seguimientoentrada.SEGUIMIENTO_CAMBIO_ETAPA, observacion, UserDetailsUtils.usuarioLogged(), usuario);
			
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
			
			page = "transaccionExitosa";
			entradaSeleccionada = new Entrada();
				
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
			
			if((entradaSeleccionada == null || entradaSeleccionada.getEntId() == null) 	&&  (idRegSeleccionado != null)){
				entradaSeleccionada =(Entrada) serviceDao.getEntradaDao().read(Entrada.class, idRegSeleccionado);			
			}			
			
			if(eventosItems == null){

				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, entradaSeleccionada.getEstado().getEstId());
				params.put(1, entradaSeleccionada.getClasificacion().getTramite().getTrmId());
				
				List<Transicion> transiciones = (List<Transicion>) serviceDao.getGenericCommonDao().executeFind(Transicion.class, params, Transicion.NAMED_QUERY_BY_TRAMITE_ESTADO_INICIAL2);
				 
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

	public Entrada getEntradaSeleccionada() {
		return entradaSeleccionada;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public void setEntradaSeleccionada(Entrada entradaSeleccionada) {
		this.entradaSeleccionada = entradaSeleccionada;
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

	public String getDiagramaFlujo() {
		return diagramaFlujo;
	}

	public void setDiagramaFlujo(String diagramaFlujo) {
		this.diagramaFlujo = diagramaFlujo;
	}	
	
}
