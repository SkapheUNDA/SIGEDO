package la.netco.correspondencia.uilayer.beans.salidas;

import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import la.netco.commons.services.FileSystemRepositoryService;
import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.persistencia.dto.commons.ConstantsKeysFire;
import la.netco.persistencia.dto.commons.Salida;

@ManagedBean
@ViewScoped
public class CambiarFechaSalidasBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;
	private Salida salidaSeleccionada;	
	private Date nuevaFechaRadicacion;
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_FILE_SYSTEM_REPO_SERVICE)
	transient private FileSystemRepositoryService repoService;
	
	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	public String actualizar(){
		String page =	null;
		try {
				Date fechaAct = salidaSeleccionada.getSalFsa();
				salidaSeleccionada.setSalFsa(nuevaFechaRadicacion);
				
				serviceDao.getGenericCommonDao().update(Salida.class, salidaSeleccionada);
				repoService.moverCarpeta(fechaAct, nuevaFechaRadicacion, salidaSeleccionada.getSalId(), ConstantsKeysFire.SALIDA);
			
				FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
				page = "transaccionExitosa";
				salidaSeleccionada = new Salida();
				
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
			
			if((salidaSeleccionada == null || salidaSeleccionada.getSalId() == null) 	&&  (idRegSeleccionado != null)){
				salidaSeleccionada =(Salida) serviceDao.getGenericCommonDao().read(Salida.class, idRegSeleccionado);
					
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


	public FileSystemRepositoryService getRepoService() {
		return repoService;
	}

	public void setRepoService(FileSystemRepositoryService repoService) {
		this.repoService = repoService;
	}


	public Salida getSalidaSeleccionada() {
		return salidaSeleccionada;
	}


	public void setSalidaSeleccionada(Salida salidaSeleccionada) {
		this.salidaSeleccionada = salidaSeleccionada;
	}

}
