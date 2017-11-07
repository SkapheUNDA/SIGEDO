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
 * Bean encargado de manegar los registros correspondiente a la tabla PUC 
 */

@ManagedBean(name="pucBean")
@ViewScoped

public class PUCBean implements Serializable {
	
	/*Log*/
	private static Logger log = LoggerFactory.getLogger(PUCBean.class);
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private PucDataModel listDatamodel = null;
	
	private PUC puc;
	private List<PUC> listaPuc;
	
	//@ManagedProperty(value = "#{serviceDao}")
	
	
	private String id;	
	private String descripcion;
	private String nivel;
	private String cuentaDetalle;
	private String requiereTercero;
	private String estado;
	
	/*Control de edicin*/
	private Integer idPucSeleccionado;
	private PUC pucSeleccionado;
	private PUC pucAux;
	

	public PUCBean() {
		puc = new PUC();
		pucSeleccionado = new PUC();
		 pucAux = new PUC();
		
	}
	
	/**
	 * Busqueda de personas, segn los atributos dados en pantalla
	 */
	public void buscar(){
		log.info("Ingreso a buscar Puc");
		List<Criterion> filtros = new ArrayList<Criterion>();
		if(id != null  && !id.trim().equals("") ){
    		filtros.add(Restrictions.eq("id",  id));  
    	}
		
		if(descripcion != null  && !descripcion.trim().equals("") ){
    		filtros.add(Restrictions.ilike("descripcion", "%" + descripcion + "%"));  
    	}
		
		if(nivel != null  && !nivel.trim().equals("") ){
    		filtros.add(Restrictions.ilike("nivel",  "%" + nivel + "%"));  
    	}
		
		
		filtros.add(Restrictions.isNotNull("id"));
		filtros.add(Restrictions.ne("id", ""));  
		
		filtros.add(Restrictions.isNotNull("descripcion"));
		filtros.add(Restrictions.ne("descripcion",  "")); 
		
		filtros.add(Restrictions.isNotNull("nivel"));
		filtros.add(Restrictions.ne("nivel",  "")); 
		
		
		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}
	
	
	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUS)
	public String agregarNavigationSociedades() {
		return "sociedades?faces-redirect=true";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUS)
	public String agregarNavigationTiposdeInforme() {
		return "tiposdeinforme?faces-redirect=true";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUS)
	public String agregarNavigationInformeSociedades() {
		return "informesociedades?faces-redirect=true";
	}
	
	
	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUS)
	public String agregarNavigationFechasControl() {
		return "fechascontrol?faces-redirect=true";
	}
	
	/**
	 * Metodo utilizado para navegar hacia la pagina de modificación de
	 * entidades
	 * 
	 * @return
	 */
	public String navModificar() {
		return "navModificar";
	}
	
	/*
	 * Metodos que retonar la regla de navegacion. Tinene la anotacion @SecuredAction que valida si el usuario
	 * registrado tiene permisos sobre la accion
	 *
	 * */
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String agregarNavigation() {		
		return "agregarPuc";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String editarNavigation() {		
		return "actualizarPuc";
	}
	
	public String navlistado(){
		return "listadoSociedades";
	}
	/**	
	 * Carga listado inicial 
	 */
	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new PucDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();
			
			
			filtros.add(Restrictions.isNotNull("id"));
			filtros.add(Restrictions.ne("id",  ""));  
			
			filtros.add(Restrictions.isNotNull("descripcion"));
			filtros.add(Restrictions.ne("descripcion",  "")); 
			
			filtros.add(Restrictions.isNotNull("nivel"));
			filtros.add(Restrictions.ne("nivel",  ""));
			
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}
	
	public void inicializar (){
		if(puc != null){
		puc = new PUC();
		pucAux = new PUC();
		}
		limpiar();
	}
	
	/**
	 * Crear o Actualizar un Puc
	 */
	public String createOrReplace(){
		String page = null;
		try {
			log.info("Ingreso a crear PUC" + puc.getId());
			/*valida si existe cuenta PUC*/
			pucAux = (PUC) serviceDao.getGenericCommonDao().read(PUC.class, puc.getId());
			if(pucAux != null){
			if (pucAux.getId().equals(puc.getId())) {
				FacesUtils.addFacesMessageFromBundle(Constants.CUENTA_PUC_EXISTE,	FacesMessage.SEVERITY_FATAL);

			} 
			} else {
	
			serviceDao.getGenericCommonDao().create(PUC.class, puc);
			FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_CREAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
			page = "transaccionExitosa";
			puc = new PUC();
			/*mostrarModal = true;
			mensaje = "Registro Creado";*/
			limpiar();
		
			}
			
			
		} catch(Exception exception){
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());
			
		}
		return page;
	
	}
	
	
	/**
	 * Actualiza Puc
	 */
	public String actualizarPuc(){
		String page = null;
		try {
			log.info("Ingreso a actualizar Puc" + puc.getId());
			if(puc.getId() != null){					
				serviceDao.getGenericCommonDao().saveOrUpdate(PUC.class, puc);
			}
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_REGISTRO,
					FacesMessage.SEVERITY_INFO);
			page = "transaccionExitosa";
			pucSeleccionado = new PUC();
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
		puc = new PUC();
		pucSeleccionado = new PUC();
	}
	
	public static final class PucDataModel extends PrimeDataModel<PUC, Integer> {
		private static final long serialVersionUID = 1L;

		private PucDataModel() {
			super(PUC.class);
			setOrderBy(Order.asc("id"));
		}

		@Override
		protected Object getId(PUC t) {
			return t.getClass();
		}
	}
	
		
	
	public void cargarRegistro(){	
		
		try {
			log.info("Cargar datos de puc");
			if(idPucSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idPucSeleccionado");			   
			     if(idRegSeleccionado != null) this.idPucSeleccionado = Integer.parseInt(idRegSeleccionado);		
			}
				
			if(idPucSeleccionado != null ){
				puc =(PUC) serviceDao.getGenericCommonDao().read(PUC.class, idPucSeleccionado.toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public PUC getPuc() {
		return puc;
	}


	public void setPuc(PUC puc) {
		this.puc = puc;
	}


	public List<PUC> getListaPuc() {
		return listaPuc;
	}

	public void setListaPuc(List<PUC> listaPuc) {
		this.listaPuc = listaPuc;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}


	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}


	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getCuentaDetalle() {
		return cuentaDetalle;
	}

	public void setCuentaDetalle(String cuentaDetalle) {
		this.cuentaDetalle = cuentaDetalle;
	}

	public String getRequiereTercero() {
		return requiereTercero;
	}

	public void setRequiereTercero(String requiereTercero) {
		this.requiereTercero = requiereTercero;
	}

	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		PUCBean.log = log;
	}

	public PucDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(PucDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}

	public PUC getPucSeleccionado() {
		return pucSeleccionado;
	}

	public void setPucSeleccionado(PUC pucSeleccionado) {
		this.pucSeleccionado = pucSeleccionado;
	}

	public Integer getIdPucSeleccionado() {
		return idPucSeleccionado;
	}

	public void setIdPucSeleccionado(Integer idPucSeleccionado) {
		this.idPucSeleccionado = idPucSeleccionado;
	}

	public String getId() {
		return id;
	}

	public String getNivel() {
		return nivel;
	}
		

}
