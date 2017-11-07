package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
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
import la.netco.core.spring.security.CommonsActionsKey;
import la.netco.core.spring.security.CommonsModKeys;
import la.netco.core.spring.security.SecuredAction;
import la.netco.core.uilayer.beans.Constants;
import la.netco.core.uilayer.beans.utils.FacesUtils;
import la.netco.sgc.persistence.dto.Auditores;
import la.netco.sgc.persistence.dto.PUC;
import la.netco.uilayer.beans.PrimeDataModel;

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

@ManagedBean(name="auditorBean")
@ViewScoped
public class AuditoresBean implements Serializable {
	
	/*Log*/
	private static Logger log = LoggerFactory.getLogger(AuditoresBean.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private AuditoresDataModel listDatamodel = null;
	
	private Auditores auditores;
	private Auditores auditoresAux;
	private List<Auditores> listaAuditores;

	
	private String codigoAuditores;
	private String nombreAuditores;
	private String estadoAuditores;
	
	private Integer idAuditoresSeleccionados;
	private Auditores auditoresSeleccionados;
	
	public AuditoresBean(){
		auditores = new Auditores();
		auditoresSeleccionados = new Auditores();
		auditoresAux = new Auditores();
	}
	
	/**
	 * Busqueda de los auditores segun los criterios dados	
	 * @return
	 */	
	public void buscar(){
		log.info("Ingreso a buscar Auditores");
		List<Criterion> filtros = new ArrayList<Criterion>();
		if(nombreAuditores != null  && !nombreAuditores.trim().equals("") ){
    		filtros.add(Restrictions.ilike("nombreAuditores", "%" + nombreAuditores + "%"));  
    	}
		
		filtros.add(Restrictions.isNotNull("nombreAuditores"));
		filtros.add(Restrictions.ne("nombreAuditores",  "")); 

				
		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}
	

	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUS)
	public String agregarNavigationArea() {
		return "area?faces-redirect=true";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUS)
	public String agregarNavigationEstadoAuditoria() {
		return "estadoauditoria?faces-redirect=true";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUS)
	public String agregarNavigationTipodeAuditoria() {
		return "tipodeauditoria?faces-redirect=true";
	}
	
	
	@SecuredAction(keyAction = CommonsActionsKey.ADD, keyMod = CommonsModKeys.MNUS)
	public String agregarNavigationTipoProgramacion() {
		return "tipoprogramacion?faces-redirect=true";
	}
	
	public String navlistado(){
		return "listadoAuditores";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String agregarNavigation() {		
		return "agregarAuditores";
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String editarNavigation() {		
		return "actualizarAuditores";
	}
	/**	
	 * Carga listado inicial 
	 */
	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new AuditoresDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();
		
			
			filtros.add(Restrictions.isNotNull("nombreAuditores"));
			filtros.add(Restrictions.ne("nombreAuditores",  "")); 
		
			
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}
	
	public void inicializar (){
		if(auditores != null){
		auditores = new Auditores();
		}
		limpiar();
	}
	
	/**
	 * Crear o Actualizar un auditor
	 */
	public String createOrReplace(){
		String page = null;
		try {
			log.info("Ingreso a crear Auditor" + auditores.getCodigoAuditores());
			auditoresAux = (Auditores) serviceDao.getGenericCommonDao().read(Auditores.class, auditores.getCodigoAuditores());
			if(auditoresAux != null) {
				if(auditoresAux.getCodigoAuditores() == auditores.getCodigoAuditores()){
					FacesUtils.addFacesMessageFromBundle(Constants.AUDITOR_YA_EXISTE,	FacesMessage.SEVERITY_FATAL);
					return page;
				}
			} else {
				Integer idAuditores = (Integer) serviceDao.getGenericCommonDao().create(Auditores.class, auditores);
				log.info("CODIGO AUDITORES" + idAuditores);
				FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_CREAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
				page = "listadoAuditores";
				auditores = new Auditores();
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
	 * Actualiza Auditor
	 * 
	 */
	public String actualizarAuditores(){
		String page = null;
		try {
			log.info("Ingreso a actualizar Puc" + auditoresSeleccionados.getCodigoAuditores());
			if(auditoresSeleccionados != null){					
			serviceDao.getGenericCommonDao().update(Auditores.class, auditoresSeleccionados);
			}
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_USUARIO,
					FacesMessage.SEVERITY_INFO);
			page = "listadoAuditores";
			auditoresSeleccionados = new Auditores();
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
		auditores = new Auditores();
		auditoresSeleccionados = new Auditores();
	}
	
	private static final class AuditoresDataModel extends PrimeDataModel<Auditores, Integer> {
		private static final long serialVersionUID = 1L;

		private AuditoresDataModel() {
			super(Auditores.class);
			setOrderBy(Order.asc("codigoAuditores"));
		}

		@Override
		protected Object getId(Auditores t) {
			return t.getClass();
		}
	}
	
	
	/**
	 * 
	 * Metodo para cargar el la informacin de una persona seleccionada 
	 * */
	public void cargarRegistro(){	
		
		try {
			log.info("Cargar datos del Auditor");
			if(idAuditoresSeleccionados == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idAutorSeleccionado");			   
			     if(idRegSeleccionado != null) this.idAuditoresSeleccionados = Integer.parseInt(idRegSeleccionado);		
			}
				
			if(idAuditoresSeleccionados != null ){
				auditoresSeleccionados =(Auditores) serviceDao.getGenericCommonDao().read(Auditores.class, idAuditoresSeleccionados);
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

	public AuditoresDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(AuditoresDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}

	public Auditores getAuditores() {
		return auditores;
	}

	public void setAuditores(Auditores auditores) {
		this.auditores = auditores;
	}

	public List<Auditores> getListaAuditores() {
		return listaAuditores;
	}

	public void setListaAuditores(List<Auditores> listaAuditores) {
		this.listaAuditores = listaAuditores;
	}

	public String getCodigoAuditores() {
		return codigoAuditores;
	}

	public void setCodigoAuditores(String codigoAuditores) {
		this.codigoAuditores = codigoAuditores;
	}

	public String getNombreAuditores() {
		return nombreAuditores;
	}

	public void setNombreAuditores(String nombreAuditores) {
		this.nombreAuditores = nombreAuditores;
	}

	public String getEstadoAuditores() {
		return estadoAuditores;
	}

	public void setEstadoAuditores(String estadoAuditores) {
		this.estadoAuditores = estadoAuditores;
	}

	public Integer getIdAuditoresSeleccionados() {
		return idAuditoresSeleccionados;
	}

	public void setIdAuditoresSeleccionados(Integer idAuditoresSeleccionados) {
		this.idAuditoresSeleccionados = idAuditoresSeleccionados;
	}

	public Auditores getAuditoresSeleccionados() {
		return auditoresSeleccionados;
	}

	public void setAuditoresSeleccionados(Auditores auditoresSeleccionados) {
		this.auditoresSeleccionados = auditoresSeleccionados;
	}

}
