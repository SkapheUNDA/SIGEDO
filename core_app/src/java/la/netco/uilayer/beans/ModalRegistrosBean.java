package la.netco.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.persistencia.dto.commons.Claseregistro;
import la.netco.persistencia.dto.commons.Registro;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@ManagedBean
@ViewScoped
public class ModalRegistrosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;

	private RegistroDataModel listDatamodel = null;

	private String noRegistro;
	private String titulo;
	

	private List<SelectItem> claseRegistroItems;	
	private Integer 	idClaseRegistroSeleccionado;
	
	public void buscar() {
		List<Criterion> filtros = new ArrayList<Criterion>();
		
		if(noRegistro != null  && !noRegistro.trim().equals("") ){
    		filtros.add(Restrictions.eq("regCod",  noRegistro));  
    	}
		
		if(titulo != null  && !titulo.trim().equals("") ){
    		filtros.add(Restrictions.ilike("regTor",  "%" + titulo+ "%"));  
    	}
		
		if (idClaseRegistroSeleccionado != null &&   idClaseRegistroSeleccionado != 0){
			filtros.add(Restrictions.eq("claseregistro.creId", idClaseRegistroSeleccionado));
		}
		
		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}

	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new RegistroDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}

	@SuppressWarnings("unchecked")
	public void cargaTramiteItems() {
		if (claseRegistroItems == null) {
			List<Claseregistro> registros = (List<Claseregistro>) serviceDao.getGenericCommonDao().executeFind(Claseregistro.NAMED_QUERY_GET_ALL_ACTIVOS);

			claseRegistroItems = new ArrayList<SelectItem>();
			for (Claseregistro registro : registros) {
				claseRegistroItems.add(new SelectItem(registro.getCreId(), registro.getCreNom()));
			}
		}
	}
	
	private static final class RegistroDataModel extends
			PrimeDataModel<Registro, Integer> {

		private static final long serialVersionUID = 1L;

		private RegistroDataModel() {
			super(Registro.class);
			setOrderBy(Order.desc("regId"));
		}

		@Override
		protected Object getId(Registro t) {
			return t.getRegId();
		}
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public RegistroDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(RegistroDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}

	public String getNoRegistro() {
		return noRegistro;
	}

	public String getTitulo() {
		return titulo;
	}

	public List<SelectItem> getClaseRegistroItems() {
		return claseRegistroItems;
	}

	public Integer getIdClaseRegistroSeleccionado() {
		return idClaseRegistroSeleccionado;
	}

	public void setNoRegistro(String noRegistro) {
		this.noRegistro = noRegistro;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setClaseRegistroItems(List<SelectItem> claseRegistroItems) {
		this.claseRegistroItems = claseRegistroItems;
	}

	public void setIdClaseRegistroSeleccionado(Integer idClaseRegistroSeleccionado) {
		this.idClaseRegistroSeleccionado = idClaseRegistroSeleccionado;
	}

}
