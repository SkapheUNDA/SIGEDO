package la.netco.correspondencia.uilayer.beans;

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

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.Entrada;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.Expedientecorrespond;
import la.netco.persistencia.dto.commons.Seguimientoentrada;

@ManagedBean
@ViewScoped
public class HistorialBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;
	private Entrada entradaSeleccionada;	
	private Date nuevaFechaRadicacion;
	private List<Seguimientoentrada> seguimientoEntrada = new ArrayList<Seguimientoentrada>();
	private Seguimientoentrada seguimientoSeleccionado;	
	private Expediente expedienteDetalle;	
	
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	
	@SuppressWarnings("unchecked")
	public void cargarExpediente(){
		try {
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, entradaSeleccionada.getEntId());

			List<Expedientecorrespond> expedientecorrespondencias = (List<Expedientecorrespond>) serviceDao.getGenericCommonDao().executeFind(Expedientecorrespond.class, params, Expedientecorrespond.NAMED_QUERY_FIND_BY_ENTRADA);

			if(expedientecorrespondencias.isEmpty()){
				FacesUtils.addFacesMessageFromBundle(Constants.SIN_OTROS_PROCESOS_ENTRADA,	FacesMessage.SEVERITY_INFO);
				return;
			}else {	
				expedienteDetalle = expedientecorrespondencias.get(0).getExpediente();	
			}
		
		} catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
		}
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void cargarRegistroDetalle(){		
		try {						
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);		
			}
			
			if((entradaSeleccionada == null || entradaSeleccionada.getEntId() == null) 	&&  (idRegSeleccionado != null)){
				entradaSeleccionada =(Entrada) serviceDao.getEntradaDao().read(Entrada.class, idRegSeleccionado);			
			
				Map<Integer, Object> params = new HashMap<Integer, Object>();
				params.put(0, entradaSeleccionada.getEntId());
				setSeguimientoEntrada((List<Seguimientoentrada>)serviceDao.getEntradaDao().executeFind(Seguimientoentrada.class, params, Seguimientoentrada.NAMED_QUERY_GET_BY_ENTRADA));
				
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

	public Date getNuevaFechaRadicacion() {
		return nuevaFechaRadicacion;
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

	public void setNuevaFechaRadicacion(Date nuevaFechaRadicacion) {
		this.nuevaFechaRadicacion = nuevaFechaRadicacion;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}


	public List<Seguimientoentrada> getSeguimientoEntrada() {
		return seguimientoEntrada;
	}


	public void setSeguimientoEntrada(List<Seguimientoentrada> seguimientoEntrada) {
		this.seguimientoEntrada = seguimientoEntrada;
	}


	public Seguimientoentrada getSeguimientoSeleccionado() {
		return seguimientoSeleccionado;
	}


	public void setSeguimientoSeleccionado(Seguimientoentrada seguimientoSeleccionado) {
		this.seguimientoSeleccionado = seguimientoSeleccionado;
	}


	public Expediente getExpedienteDetalle() {
		return expedienteDetalle;
	}


	public void setExpedienteDetalle(Expediente expedienteDetalle) {
		this.expedienteDetalle = expedienteDetalle;
	}	
	
}
