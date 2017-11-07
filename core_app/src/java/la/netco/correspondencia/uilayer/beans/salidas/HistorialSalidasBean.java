package la.netco.correspondencia.uilayer.beans.salidas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.Salida;
import la.netco.persistencia.dto.commons.Seguimientosalida;

@ManagedBean
@ViewScoped
public class HistorialSalidasBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;
	private Date nuevaFechaRadicacion;
	private List<Seguimientosalida> seguimientoSalida = new ArrayList<Seguimientosalida>();
	private Seguimientosalida seguimientoSeleccionado;	
	private Expediente expedienteDetalle;	

	private Salida salidaSeleccionada;
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	

	@SuppressWarnings("unchecked")
	public void cargarRegistroDetalle(){		
		try {						
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);		
			}
			
			if((salidaSeleccionada == null || salidaSeleccionada.getSalId() == null) 	&&  (idRegSeleccionado != null)){
				salidaSeleccionada =(Salida) serviceDao.getGenericCommonDao().read(Salida.class, idRegSeleccionado);
				
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, salidaSeleccionada.getSalId());
				seguimientoSalida = (List<Seguimientosalida>)serviceDao.getEntradaDao().executeFind(Seguimientosalida.class, params, Seguimientosalida.NAMED_QUERY_GET_BY_SALIDA);
			
			}					
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public Date getNuevaFechaRadicacion() {
		return nuevaFechaRadicacion;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}


	public void setNuevaFechaRadicacion(Date nuevaFechaRadicacion) {
		this.nuevaFechaRadicacion = nuevaFechaRadicacion;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}


	public Expediente getExpedienteDetalle() {
		return expedienteDetalle;
	}


	public void setExpedienteDetalle(Expediente expedienteDetalle) {
		this.expedienteDetalle = expedienteDetalle;
	}


	public Seguimientosalida getSeguimientoSeleccionado() {
		return seguimientoSeleccionado;
	}


	public void setSeguimientoSeleccionado(Seguimientosalida seguimientoSeleccionado) {
		this.seguimientoSeleccionado = seguimientoSeleccionado;
	}


	public Salida getSalidaSeleccionada() {
		return salidaSeleccionada;
	}


	public void setSalidaSeleccionada(Salida salidaSeleccionada) {
		this.salidaSeleccionada = salidaSeleccionada;
	}

	public List<Seguimientosalida> getSeguimientoSalida() {
		return seguimientoSalida;
	}

	public void setSeguimientoSalida(List<Seguimientosalida> seguimientoSalida) {
		this.seguimientoSalida = seguimientoSalida;
	}	
	
}
