package la.netco.correspondencia.uilayer.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.persistencia.vo.Usuario;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.spring.security.UserDetailsUtils;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Seguimientoentrada;
import la.netco.persistencia.dto.commons.Transicion;

@ManagedBean
@ViewScoped
public class NoNotificarBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;
	private Entrada entradaSeleccionada;
	private String justificacion;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	
	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	@SuppressWarnings("unchecked")
	public String actualizar(){
		String page =	null;
		try {
				entradaSeleccionada.setEntNot(justificacion);
				entradaSeleccionada.setEntLnot(true);
				entradaSeleccionada.setEntLcon(true);
				
				serviceDao.getEntradaDao().update(Entrada.class, entradaSeleccionada);
				
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, entradaSeleccionada.getEntId());
				List<Seguimientoentrada> seguimientos = (List<Seguimientoentrada>)serviceDao.getEntradaDao().executeFindPaged(0,1, params, Seguimientoentrada.NAMED_QUERY_GET_BY_ENTRADA);
				Seguimientoentrada seguimiento = seguimientos.get(0);
				
				Transicion transicion = new Transicion();
				transicion.setEstadoByTrsEstfin(seguimiento.getEstadoBySenEst());
				transicion.setEstadoByTrsEstini(seguimiento.getEstadoBySenEstini());
				transicion.setEvento(seguimiento.getEvento());
				Usuario usuarioLogged =   UserDetailsUtils.usuarioLogged();
				serviceDao.getSeguimientosDao().addSeguimientoEntrada(entradaSeleccionada, transicion, Seguimientoentrada.SEGUIMIENTO_DESCARGUE_EN_CERO, "", usuarioLogged, usuarioLogged);
				
				FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
				page = "transaccionExitosa";
				entradaSeleccionada = new Entrada();
				
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,
					FacesMessage.SEVERITY_FATAL);
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
			
			if((entradaSeleccionada == null || entradaSeleccionada.getEntId() == null) 	&&  (idRegSeleccionado != null)){
				entradaSeleccionada =(Entrada) serviceDao.getEntradaDao().read(Entrada.class, idRegSeleccionado);			
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
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


	public String getJustificacion() {
		return justificacion;
	}


	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}	
	
}
