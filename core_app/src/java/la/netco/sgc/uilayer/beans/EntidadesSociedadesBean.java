package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;



import la.netco.persistencia.dto.commons.Entidad;
import la.netco.sgc.persistence.dto.Entidades;
import la.netco.sgc.persistence.dto.PUC;
import la.netco.sgc.uilayer.beans.PUCBean.PucDataModel;
import la.netco.uilayer.beans.PrimeDataModel;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Edwin Diaz
 *
 */
@ManagedBean(name="entidadesSociedadesBean")
@ViewScoped
public class EntidadesSociedadesBean  implements Serializable{
	
	/*Log*/
	private static Logger log = LoggerFactory.getLogger(EntidadesSociedadesBean.class);
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	/*Control visual*/
	
	private Integer codigo;	
	private Integer nit;
	private String razonSocial;
	
	private SociedadDataModel listDatamodel = null;
	private Entidad sociedad;
	private Entidad sociedadSeleccionada;
	private Integer idSocSeleccionada;
	
	
	/*Constructor*/
	private List<Entidades> listaEntidades;
	

	public EntidadesSociedadesBean() {
		sociedad = new Entidad();
		sociedadSeleccionada = new Entidad();
		this.listaEntidades = new ArrayList<Entidades>();

	}
	
	
	/**	
	 * Carga listado inicial 
	 */
	public void cargaListaDataModel() {
		/*if (listDatamodel == null) {
			listDatamodel = new SociedadDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();
			
			
			filtros.add(Restrictions.isNotNull("entCodigo"));
			filtros.add(Restrictions.ne("entCodigo",  ""));  
			
			filtros.add(Restrictions.isNotNull("entNit"));
			filtros.add(Restrictions.ne("entNit",  "")); 
			
			filtros.add(Restrictions.isNotNull("entObjetoSocial"));
			filtros.add(Restrictions.ne("entObjetoSocial",  ""));
			
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		} */
		listaEntidades = (List<Entidades>) serviceDao
				.getGenericCommonDao().loadAll(Entidades.class);
	}
	
	/**
	 * Busqueda de personas, segn los atributos dados en pantalla
	 */
	public void buscar(){
		log.info("Ingreso a buscar Sociedades");
		List<Criterion> filtros = new ArrayList<Criterion>();
		listDatamodel = new SociedadDataModel();
		if(codigo != null  && codigo >0 ){
    		filtros.add(Restrictions.eq("entCodigo",  codigo));  
    	}

		
		
		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
		
	}
	
	
	
	
	public static final class SociedadDataModel extends PrimeDataModel<Entidad, Integer> {
		public SociedadDataModel(Class<Entidad> entityClass) {
			super(entityClass);
			// TODO Auto-generated constructor stub
		}

		private static final long serialVersionUID = 1L;

		private SociedadDataModel() {
			super(Entidad.class);
			setOrderBy(Order.asc("entCodigo"));
		}

		@Override
		protected Object getId(Entidad t) {
			return t.getClass();
		}
	}





	public ServiceDao getServiceDao() {
		return serviceDao;
	}


	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}


	public Integer getCodigo() {
		return codigo;
	}


	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}


	public Integer getNit() {
		return nit;
	}


	public void setNit(Integer nit) {
		this.nit = nit;
	}


	public String getRazonSocial() {
		return razonSocial;
	}


	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}


	public SociedadDataModel getListDatamodel() {
		//cargaListaDataModel();
		return listDatamodel;
	}


	public void setListDatamodel(SociedadDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}


	public Entidad getSociedad() {
		return sociedad;
	}


	public void setSociedad(Entidad sociedad) {
		this.sociedad = sociedad;
	}


	public Entidad getSociedadSeleccionada() {
		return sociedadSeleccionada;
	}


	public void setSociedadSeleccionada(Entidad sociedadSeleccionada) {
		this.sociedadSeleccionada = sociedadSeleccionada;
	}


	public Integer getIdSocSeleccionada() {
		return idSocSeleccionada;
	}


	public void setIdSocSeleccionada(Integer idSocSeleccionada) {
		this.idSocSeleccionada = idSocSeleccionada;
	}


	public List<Entidades> getListaEntidades() {
		cargaListaDataModel();
		return listaEntidades;
	}


	public void setListaEntidades(List<Entidades> listaEntidades) {
		this.listaEntidades = listaEntidades;
	}
	
	

}
