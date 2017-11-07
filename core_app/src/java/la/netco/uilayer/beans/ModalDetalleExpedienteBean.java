package la.netco.uilayer.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.persistencia.dto.commons.Anotacionesexpedientes;
import la.netco.persistencia.dto.commons.Expediente;
import la.netco.persistencia.dto.commons.Seguimientoentrada;
import la.netco.persistencia.dto.commons.Seguimientoexpediente;


@ManagedBean
@ViewScoped
public class ModalDetalleExpedienteBean implements Serializable {


	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	@SuppressWarnings("unchecked")
	public List<Seguimientoexpediente> seguimientosExp(Expediente expediente) {
		List<Seguimientoexpediente> seguimientos =  null;
		if(expediente != null){
			Map<Integer, Object> params = new HashMap<Integer, Object>();
			params.put(0, expediente.getExpId());
			seguimientos = (List<Seguimientoexpediente>)serviceDao.getEntradaDao().executeFind(Seguimientoentrada.class, params, Seguimientoexpediente.NAMED_QUERY_GET_BY_EXPEDIENTE);
		
		}
		return seguimientos;
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<Anotacionesexpedientes>  anotacionesExpediente(Expediente expediente) {
		List<Anotacionesexpedientes>  listaAnotaciones =null;
		if(expediente != null){
			HashMap<Integer, Integer> params = new HashMap<Integer, Integer>();
			params.put(0, expediente.getExpId());			
			listaAnotaciones = (List<Anotacionesexpedientes>) serviceDao.getGenericCommonDao().executeFind(Anotacionesexpedientes.class, params, Anotacionesexpedientes.NAMED_QUERY_GET_ALL_BYEXP);
		}
		return listaAnotaciones;
	}
	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	
}
