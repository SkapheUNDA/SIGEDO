package la.netco.sgc.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.sgc.persistence.dto.TipoInforme;
import la.netco.uilayer.beans.PrimeDataModel;

/**
 * 
 * @author Edwin Diaz
 *
 */
@ManagedBean(name="tipoInformeBean")
@ViewScoped
public class TipoInformeBean  implements Serializable
{

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private TipoInformeDataModel listDatamodel = null;
	
	/*Control VIsual*/
	private String codInforme;	
	private String descripcion;
	private String periocidad;
	private TipoInforme tipoInforme;
	
	/*Control de edicin*/
	private Integer idTipoInformeSeleccionado;
	private TipoInforme tipoInformeSeleccionado;
	
	/*Constructor*/
	public TipoInformeBean() {
		tipoInforme = new TipoInforme();
		tipoInformeSeleccionado = new TipoInforme();
		
	}
	
	
	
	
	/**	
	 * Carga listado inicial 
	 */
	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new TipoInformeDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();
			
			
			filtros.add(Restrictions.isNotNull("codigoInforme"));
			filtros.add(Restrictions.ne("codigoInforme",  ""));  
			
			filtros.add(Restrictions.isNotNull("descripcion"));
			filtros.add(Restrictions.ne("descripcion",  "")); 
			
			filtros.add(Restrictions.isNotNull("periocidad"));
			filtros.add(Restrictions.ne("periocidad",  ""));
			
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}
	
	
	
	public static final class TipoInformeDataModel extends PrimeDataModel<TipoInforme, Integer> {
		private static final long serialVersionUID = 1L;

		private TipoInformeDataModel() {
			super(TipoInforme.class);
			setOrderBy(Order.asc("codigoInforme"));
		}

		@Override
		protected Object getId(TipoInforme t) {
			return t.getClass();
		}
	}


	public ServiceDao getServiceDao() {
		return serviceDao;
	}


	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}


	public TipoInformeDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}


	public void setListDatamodel(TipoInformeDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}


	public String getCodInforme() {
		return codInforme;
	}


	public void setCodInforme(String codInforme) {
		this.codInforme = codInforme;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getPeriocidad() {
		return periocidad;
	}


	public void setPeriocidad(String periocidad) {
		this.periocidad = periocidad;
	}


	public TipoInforme getTipoInforme() {
		return tipoInforme;
	}


	public void setTipoInforme(TipoInforme tipoInforme) {
		this.tipoInforme = tipoInforme;
	}


	public Integer getIdTipoInformeSeleccionado() {
		return idTipoInformeSeleccionado;
	}


	public void setIdTipoInformeSeleccionado(Integer idTipoInformeSeleccionado) {
		this.idTipoInformeSeleccionado = idTipoInformeSeleccionado;
	}


	public TipoInforme getTipoInformeSeleccionado() {
		return tipoInformeSeleccionado;
	}


	public void setTipoInformeSeleccionado(TipoInforme tipoInformeSeleccionado) {
		this.tipoInformeSeleccionado = tipoInformeSeleccionado;
	}
	
	
	
}
