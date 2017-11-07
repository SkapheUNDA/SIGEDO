package la.netco.correspondencia.uilayer.beans;

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
import la.netco.persistencia.dto.commons.Entrada;

@ManagedBean
@ViewScoped
public class CambiarFechaBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer idRegSeleccionado;
	private Entrada entradaSeleccionada;	
	private Date nuevaFechaRadicacion;
	
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_FILE_SYSTEM_REPO_SERVICE)
	transient private FileSystemRepositoryService repoService;
	
	//@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod =CommonsModKeys.MNUE)
	public String actualizar(){
		String page =	null;
		try {
				Date fechaAct = entradaSeleccionada.getEntFen();
				entradaSeleccionada.setEntFen(nuevaFechaRadicacion);
				entradaSeleccionada.setEntFdr(serviceDao.getEntradaDao().obtenerFechaRespuesta(entradaSeleccionada));
				
				serviceDao.getEntradaDao().update(Entrada.class, entradaSeleccionada);
				repoService.moverCarpeta(fechaAct, nuevaFechaRadicacion, entradaSeleccionada.getEntId(), ConstantsKeysFire.SALIDA);
			
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


	public FileSystemRepositoryService getRepoService() {
		return repoService;
	}

	public void setRepoService(FileSystemRepositoryService repoService) {
		this.repoService = repoService;
	}

}
