package la.netco.registro.uilayer.beans;

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
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.Registro;
import la.netco.persistencia.dto.commons.Seguimientoexpediente;
import la.netco.persistencia.dto.commons.Transicion;


@ManagedBean
@ViewScoped
public class BloqueoRegistroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Registro registroSeleccionado;

	private Integer idRegSeleccionado;

	
	private static String BLOQUEO_REGISTRO ="BLOQUEO REGISTRO";

	private static String DESBLOQUEO_REGISTRO ="DESBLOQUEO REGISTRO";

	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	


	@SuppressWarnings("unchecked")
	//@SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod = CommonsModKeys.MNUEPAE)
	public String bloquear(Integer regId){
		try {
		
			Registro registro = (Registro) serviceDao.getGenericCommonDao().read(Registro.class, regId);
			registro.setRegLblo(true);			
		
			serviceDao.getGenericCommonDao().update(Registro.class, registro);

			Expediente expediente = registro.getExpediente();
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, expediente.getExpId());
			List<Seguimientoexpediente> seguimientos = (List<Seguimientoexpediente>)serviceDao.getEntradaDao().executeFindPaged(0,1, params, Seguimientoexpediente.NAMED_QUERY_GET_BY_EXPEDIENTE);
			Seguimientoexpediente seguimiento = seguimientos.get(0);


			Transicion transicion = new Transicion();
			transicion.setEstadoByTrsEstfin(seguimiento.getEstadoBySgeEst());
			transicion.setEstadoByTrsEstini(seguimiento.getEstadoBySgeEstini());
			transicion.setEvento(seguimiento.getEvento());
			Usuario usuario = UserDetailsUtils.usuarioLogged();
			serviceDao.getSeguimientosDao().addSeguimientoExp(expediente, BLOQUEO_REGISTRO, "", usuario, seguimiento.getSgeUsr(), transicion);		

			registroSeleccionado = registro;
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
			
			return "transaccionExitosa";
			
		}catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
			return "";
		}
	}

	
	@SuppressWarnings("unchecked")
	//@SecuredAction(keyAction = ExpedientesConstants.DEV_EXP, keyMod = CommonsModKeys.MNUEPAE)
	public String desbloquear(Integer regId){
		try {
		
			Registro registro = (Registro) serviceDao.getGenericCommonDao().read(Registro.class, regId);
			registro.setRegLblo(false);			
		
			serviceDao.getGenericCommonDao().update(Registro.class, registro);

			Expediente expediente = registro.getExpediente();
			
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, expediente.getExpId());
			List<Seguimientoexpediente> seguimientos = (List<Seguimientoexpediente>)serviceDao.getEntradaDao().executeFindPaged(0,1, params, Seguimientoexpediente.NAMED_QUERY_GET_BY_EXPEDIENTE);
			Seguimientoexpediente seguimiento = seguimientos.get(0);


			Transicion transicion = new Transicion();
			transicion.setEstadoByTrsEstfin(seguimiento.getEstadoBySgeEst());
			transicion.setEstadoByTrsEstini(seguimiento.getEstadoBySgeEstini());
			transicion.setEvento(seguimiento.getEvento());
			Usuario usuario = UserDetailsUtils.usuarioLogged();
			serviceDao.getSeguimientosDao().addSeguimientoExp(expediente, DESBLOQUEO_REGISTRO, "", usuario, seguimiento.getSgeUsr(), transicion);		

			registroSeleccionado = registro;
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
			
			return "transaccionExitosa";
			
		}catch (Exception e) {
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			e.printStackTrace();
			return "";
		}
	}

	public void cargarRegistro() {
		try {


			FacesContext facesContext = FacesContext.getCurrentInstance();
			if (idRegSeleccionado == null) {
				facesContext = FacesContext.getCurrentInstance();
				String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");
				if (idRegSeleccionado != null)
					this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);
			}

			if ((registroSeleccionado == null || registroSeleccionado.getRegId() == null) && (idRegSeleccionado != null)) {
				registroSeleccionado = (Registro) serviceDao.getGenericCommonDao().read(Registro.class,	idRegSeleccionado);			
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
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
}
