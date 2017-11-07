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
import la.netco.persistencia.dto.commons.Persona;
import la.netco.sgc.persistence.dto.Area;
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

@ManagedBean(name="areaBean")
@ViewScoped

public class AreaBean implements Serializable {
	
	/*Log*/
	private static Logger log = LoggerFactory.getLogger(AreaBean.class);
	
	
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private AreaDataModel listDatamodel = null;
	
	private Area area;
	private Area areaAux;
	private List<Area> listaArea;
	
	@ManagedProperty(value = "#{serviceDao}")
	
	private String codigoArea;
	private String descripcionArea;
	
	/*Control de edicin*/
	private Integer idAreaSeleccionada;
	private Area areaSeleccionada;
	
	public AreaBean(){
		area = new Area();
		areaSeleccionada = new Area();
		areaAux = new Area();
	}
	
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String agregarNavigation() {		
		return "agregarAreas";
	}
	@SecuredAction(keyAction = CommonsActionsKey.UPDATE, keyMod = CommonsModKeys.SE_USU_USU)
	public String editarNavigation() {		
		return "actualizarAreas";
	}
	
	public String navlistado(){
		return "listadoAreas";
	}
	
	/**
	 * Busqueda de areas, segn los atributos dados en pantalla	 */
	public void buscar(){
		log.info("Ingreso a buscar Area");
		List<Criterion> filtros = new ArrayList<Criterion>();
		
		if(descripcionArea != null  && !descripcionArea.trim().equals("") ){
    		filtros.add(Restrictions.ilike("descripcionArea", "%" + descripcionArea + "%"));  
    	}
				
		filtros.add(Restrictions.isNotNull("descripcionArea"));
		filtros.add(Restrictions.ne("descripcionArea",  "")); 
		
		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}
	
	/**	
	 * Carga listado inicial 
	 */
	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new AreaDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();
			
			filtros.add(Restrictions.isNotNull("descripcionArea"));
			filtros.add(Restrictions.ne("descripcionArea",  "")); 
						
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}
	
	public void inicializar (){
		if(area != null){
		area = new Area();
		}
		limpiar();
	}
	
	/**
	 * Crear o Actualizar una Area
	 */
	public String createOrReplace(){
		String page = null;
		try {
			log.info("Ingreso a crear Area" + area.getCodigoArea());
			
			areaAux = (Area)serviceDao.getGenericCommonDao().read(Area.class, area.getCodigoArea());
			if(areaAux != null){
				if(areaAux.getCodigoArea() == area.getCodigoArea()){
					FacesUtils.addFacesMessageFromBundle(Constants.AREA_YA_EXISTE,	FacesMessage.SEVERITY_FATAL);
					return page;
				}
				
			} else {
				Integer idArea = (Integer) serviceDao.getGenericCommonDao().create(Area.class, area);
				log.info("CODIGO AREA" + idArea);
				FacesUtils.addFacesMessageFromBundle(Constants.OPERACION_EXITOSA_CREAR_REGISTRO,	FacesMessage.SEVERITY_INFO);
				page = "listadoAreas";
				area = new Area();
				limpiar();
			}
		
			
		} catch(Exception exception){
			FacesUtils.addFacesMessageFromBundle(Constants.ERROR_OPERACION,	FacesMessage.SEVERITY_FATAL);
			log.error(exception.getMessage());
			
		}
		return page;
	
	}
	
	/**
	 * Actualiza Area
	 */
	public String actualizarArea(){
		String page = null;
		try {
			log.info("Ingreso a actualizar Puc" + areaSeleccionada.getCodigoArea());
			if(areaSeleccionada != null){					
			serviceDao.getGenericCommonDao().saveOrUpdate(Area.class, areaSeleccionada);
			}
			FacesUtils.addFacesMessageFromBundle(
					Constants.OPERACION_EXITOSA_ACTUALIZAR_USUARIO,
					FacesMessage.SEVERITY_INFO);
			page = "listadoAreas";
			areaSeleccionada = new Area();
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
		area = new Area();
		areaSeleccionada = new Area();
	}
	
	private static final class AreaDataModel extends PrimeDataModel<Area, Integer> {
		private static final long serialVersionUID = 1L;

		private AreaDataModel() {
			super(Area.class);
			setOrderBy(Order.asc("codigoArea"));
		}

		@Override
		protected Object getId(Area t) {
			return t.getClass();
		}
	}
	
	
	/**
	 * 
	 * Metodo para cargar el la informacin de una Area seleccionada 
	 * */
	public void cargarRegistro(){	
		
		try {
			log.info("Cargar datos de Area Seleccionada");
			if(idAreaSeleccionada == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idAreaSeleccionado");			   
			     if(idRegSeleccionado != null) this.idAreaSeleccionada = Integer.parseInt(idRegSeleccionado);		
			}
				
			if(idAreaSeleccionada != null ){
				areaSeleccionada =(Area) serviceDao.getGenericCommonDao().read(Area.class, idAreaSeleccionada);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public List<Area> getListaArea() {
		return listaArea;
	}

	public void setListaArea(List<Area> listaArea) {
		this.listaArea = listaArea;
	}

	public String getCodigoArea() {
		return codigoArea;
	}

	public void setCodigoArea(String codigoArea) {
		this.codigoArea = codigoArea;
	}

	public String getDescripcionArea() {
		return descripcionArea;
	}

	public void setDescripcionArea(String descripcionArea) {
		this.descripcionArea = descripcionArea;
	}

	public Integer getIdAreaSeleccionada() {
		return idAreaSeleccionada;
	}

	public void setIdAreaSeleccionada(Integer idAreaSeleccionada) {
		this.idAreaSeleccionada = idAreaSeleccionada;
	}

	public Area getAreaSeleccionada() {
		return areaSeleccionada;
	}

	public void setAreaSeleccionada(Area areaSeleccionada) {
		this.areaSeleccionada = areaSeleccionada;
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public AreaDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(AreaDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
