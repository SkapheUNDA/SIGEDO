package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.Area;
import la.netco.sgc.persistence.dto.EstadoAuditoria;
import la.netco.sgc.persistence.dto.PUC;
import la.netco.uilayer.beans.PrimeDataModel;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import la.netco.core.spring.BeansSpringConst;
import la.netco.persistencia.dto.commons.Persona;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author jhmoreno
 *
 */

@ManagedBean(name="estadoAuditoriaBean")
@ViewScoped
public class EstadoAuditoriaBean implements Serializable {
	
	/*Log*/
	private static Logger log = LoggerFactory.getLogger(EstadoAuditoriaBean.class);
	
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private EstadoAuditoriaDataModel listDatamodel = null;
	
	
	private EstadoAuditoria estadoauditoria;
	private EstadoAuditoria estadoauditoriaAux;
	private List<EstadoAuditoria> listaEstadoAuditoria;

	
	private String codigoEstadoAuditoria;
	private String descripcionEstadoAuditoria;
	
	/*Control de edicin*/
	private Integer idEstadoAuditoriaSeleccionada;
	private EstadoAuditoria estadoAuditoriaSeleccionada;
	
	public EstadoAuditoriaBean() {
		estadoauditoria = new EstadoAuditoria();
		estadoAuditoriaSeleccionada = new EstadoAuditoria();
		estadoauditoriaAux =  new EstadoAuditoria();
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String agregarNavigation() {		
		return "agregarEstados";
	}
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String editarNavigation() {		
		return "actualizarEstados";
	}
	
	public String navlistado(){
		return "listadoEstados";
	}
	
	
	/**
	 * Busqueda de areas, segn los atributos dados en pantalla	 */
	public void buscar(){
		log.info("Ingreso a buscar EstadoAuditoria");
		List<Criterion> filtros = new ArrayList<Criterion>();
		
		if(descripcionEstadoAuditoria != null  && !descripcionEstadoAuditoria.trim().equals("") ){
    		filtros.add(Restrictions.ilike("descripcionEstadoAuditoria", "%" + descripcionEstadoAuditoria + "%"));  
    	}
			
		filtros.add(Restrictions.isNotNull("descripcionEstadoAuditoria"));
		filtros.add(Restrictions.ne("descripcionEstadoAuditoria", ""));  
		
		 
		
		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}
	
	/**	
	 * Carga listado inicial 
	 */
	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new EstadoAuditoriaDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();
			
			filtros.add(Restrictions.isNotNull("descripcionEstadoAuditoria"));
			filtros.add(Restrictions.ne("descripcionEstadoAuditoria", ""));  
			
						
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}
	
	public void inicializar (){
		if(estadoauditoria != null){
		estadoauditoria = new EstadoAuditoria();
		}
		limpiar();
	}
	
	
	/**
	 * Crear o Actualizar una Area
	 */
	public String createOrReplace(){
		String page = null;
		try {
			log.info("Ingreso a crear Estado Auditoria" + estadoauditoria.getCodigoEstadoAuditoria());
			estadoauditoriaAux  = (EstadoAuditoria)serviceDao.getGenericCommonDao().read(EstadoAuditoria.class, estadoauditoria.getCodigoEstadoAuditoria());
			if(estadoauditoriaAux != null){
				FacesUtils.addFacesMessageFromBundle(Constants.ESTADO_AUDITORIA_YA_EXISTE,	FacesMessage.SEVERITY_FATAL);
				return page;
				
			} else{
									
			//serviceDao.getGenericCommonDao().saveOrUpdate(Persona.class, persona);
			Integer idEstadoAuditoria = (Integer) serviceDao.getGenericCommonDao().create(EstadoAuditoria.class, estadoauditoria);
			log.info("CODIGO AREA" + idEstadoAuditoria);
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_CREAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
			page = "listadoEstados";
			estadoauditoria = new EstadoAuditoria();
			limpiar();
			}
						
		} catch(Exception exception){
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());
			
		}
		return page;
	
	}
	
	
	/**
	 * Actualiza Estado
	 */
	public String actualizarEstadoAuditoria(){
		String page = null;
		try {
			log.info("Ingreso a actualizar Estado Auditoria" + estadoAuditoriaSeleccionada.getCodigoEstadoAuditoria());
			if(estadoAuditoriaSeleccionada != null){					
			serviceDao.getGenericCommonDao().update(EstadoAuditoria.class, estadoAuditoriaSeleccionada);
			}
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			page = "listadoEstados";
			estadoAuditoriaSeleccionada = new EstadoAuditoria();
			limpiar();
			
		} catch(Exception exception){
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());
			
		}
		return page;
	
	}
	
	/*
	 * Inicializa Objetos utilizados
	 * */
	public void limpiar(){
		estadoauditoria = new EstadoAuditoria();
		estadoAuditoriaSeleccionada = new EstadoAuditoria();
	}
	
	private static final class EstadoAuditoriaDataModel extends PrimeDataModel<EstadoAuditoria, Integer> {
		private static final long serialVersionUID = 1L;

		private EstadoAuditoriaDataModel() {
			super(EstadoAuditoria.class);
			setOrderBy(Order.asc("codigoEstadoAuditoria"));
		}

		@Override
		protected Object getId(EstadoAuditoria t) {
			return t.getClass();
		}
	}
	
	
	/**
	 * 
	 * Metodo para cargar el la informacin de un Estado Auditoria seleccionado 
	 * */
	public void cargarRegistro(){	
		
		try {
			log.info("Cargar datos de persona");
			if(idEstadoAuditoriaSeleccionada == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idEstadoSeleccionado");			   
			     if(idRegSeleccionado != null) this.idEstadoAuditoriaSeleccionada = Integer.parseInt(idRegSeleccionado);		
			}
				
			if(idEstadoAuditoriaSeleccionada != null ){
				estadoAuditoriaSeleccionada =(EstadoAuditoria) serviceDao.getGenericCommonDao().read(EstadoAuditoria.class, idEstadoAuditoriaSeleccionada);
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

	public EstadoAuditoriaDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(EstadoAuditoriaDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}


	public EstadoAuditoria getEstadoauditoria() {
		return estadoauditoria;
	}

	public void setEstadoauditoria(EstadoAuditoria estadoauditoria) {
		this.estadoauditoria = estadoauditoria;
	}

	public List<EstadoAuditoria> getListaEstadoAuditoria() {
		return listaEstadoAuditoria;
	}

	public void setListaEstadoAuditoria(List<EstadoAuditoria> listaEstadoAuditoria) {
		this.listaEstadoAuditoria = listaEstadoAuditoria;
	}

	public String getCodigoEstadoAuditoria() {
		return codigoEstadoAuditoria;
	}


	public void setCodigoEstadoAuditoria(String codigoEstadoAuditoria) {
		this.codigoEstadoAuditoria = codigoEstadoAuditoria;
	}


	public String getDescripcionEstadoAuditoria() {
		return descripcionEstadoAuditoria;
	}


	public void setDescripcionEstadoAuditoria(String descripcionEstadoAuditoria) {
		this.descripcionEstadoAuditoria = descripcionEstadoAuditoria;
	}

	public Integer getIdEstadoAuditoriaSeleccionada() {
		return idEstadoAuditoriaSeleccionada;
	}

	public void setIdEstadoAuditoriaSeleccionada(
			Integer idEstadoAuditoriaSeleccionada) {
		this.idEstadoAuditoriaSeleccionada = idEstadoAuditoriaSeleccionada;
	}


	public EstadoAuditoria getEstadoAuditoriaSeleccionada() {
		return estadoAuditoriaSeleccionada;
	}

	public void setEstadoAuditoriaSeleccionada(
			EstadoAuditoria estadoAuditoriaSeleccionada) {
		this.estadoAuditoriaSeleccionada = estadoAuditoriaSeleccionada;
	}

}
