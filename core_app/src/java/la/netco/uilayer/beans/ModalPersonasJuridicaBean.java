package la.netco.uilayer.beans;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
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
import org.primefaces.context.RequestContext;

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.persistencia.dto.commons.Entidad;

@ManagedBean(name = "modalPersonasJuridicaBean")
@ViewScoped
public class ModalPersonasJuridicaBean implements Serializable {


	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	
	private EntidadDataModel listDatamodel = null;

	private String numIdentificacion = "";
	private String nombres = "";
	
	private String personaBorrar;
	
	public String getPersonaBorrar() {
		return personaBorrar;
	}

	public void setPersonaBorrar(String personaBorrar) {
		this.personaBorrar = personaBorrar;
	}

	public void buscar(){
		List<Criterion> filtros = new ArrayList<Criterion>();
		if(numIdentificacion != null  && numIdentificacion.trim().length()>0 ){
    		filtros.add(Restrictions.ilike("etdIde",  "%"+numIdentificacion.trim()+"%"));  
    	}
		
		if(nombres != null  && nombres.trim().length() > 0 ){
    		filtros.add(Restrictions.ilike("etdNom", "%" + nombres + "%"));  
    	}
		
		filtros.add(Restrictions.isNotNull("etdNom"));
		filtros.add(Restrictions.ne("etdNom",  "")); 
		
		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}

	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new EntidadDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();
			
			filtros.add(Restrictions.isNotNull("etdNom"));
			filtros.add(Restrictions.ne("etdNom",  "")); 
			
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}

	
	private static final class EntidadDataModel extends PrimeDataModel<Entidad, Integer> {
		private static final long serialVersionUID = 1L;

		private EntidadDataModel() {
			super(Entidad.class);
			setOrderBy(Order.asc("etdNom"));
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

	public EntidadDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(EntidadDataModel listDatamodel) {
		this.listDatamodel = listDatamodel;
	}

	public String getNumIdentificacion() {
		return numIdentificacion;
	}

	public void setNumIdentificacion(String numIdentificacion) {
		this.numIdentificacion = numIdentificacion;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	
	public void nada() {
		//Mtodo usado para hacer doble invocacin
		//para confirmar borrar una entidad antes de 
		//hacerlo realmente.
	}
	
	public void eliminarPersona() {
		System.out.println("eliminarPersonaJuridica... "+personaBorrar);
		if (personaBorrar == null) return;
		
		Connection con = serviceDao.getGenericCommonDao().connectionFromHibernate();
		try {
			CallableStatement stm = con.prepareCall("{call dbo.PaDelEntidad(?, ?, ?)}");
			int i=1;
			stm.setString(i++, personaBorrar);
			stm.registerOutParameter(i++, Types.INTEGER);
			stm.registerOutParameter(i++, Types.VARCHAR);
			stm.setQueryTimeout(60);
			stm.execute();
			
			int error = stm.getInt(2);
			String mensaje = stm.getString(3);
			mensaje = mensaje.replace("'", "\"");
			System.out.println("error="+error);
			RequestContext.getCurrentInstance().execute("alert('"+mensaje+"')");
		} catch (Exception e) {
			RequestContext.getCurrentInstance().execute("alert('Error: "+e.getMessage()+"')");
			e.printStackTrace();
		}
	}
}
