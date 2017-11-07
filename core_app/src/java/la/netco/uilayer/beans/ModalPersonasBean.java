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

import la.netco.core.businesslogic.services.ServiceDao;
import la.netco.core.spring.BeansSpringConst;
import la.netco.persistencia.dto.commons.Persona;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;


@ManagedBean(name = "modalPersonasBean")
@ViewScoped
public class ModalPersonasBean implements Serializable {


	private static final long serialVersionUID = 1L;
	@ManagedProperty(value = BeansSpringConst.BEAN_ID_SERVICE_DAO)
	transient private ServiceDao serviceDao;
	private PersonaDataModel listDatamodel = null;

	private String numIdentificacion;
	private String nombres;
	private String primerApellido;
	private String segundoApellido;
	private String seudonimo;
	
	//private Persona usuarioBorrar;
	
	//@ManagedProperty(value="#{param.usuarioBorrar}")
	private Integer usuarioBorrar;
	
	public Integer getUsuarioBorrar() {
		return usuarioBorrar;
	}

	public void setUsuarioBorrar(Integer usuarioBorrar) {
		this.usuarioBorrar = usuarioBorrar;
	}

	public void buscar(){
		List<Criterion> filtros = new ArrayList<Criterion>();
		if(numIdentificacion != null  && !numIdentificacion.trim().equals("") ){
    		filtros.add(Restrictions.eq("perDoc",  numIdentificacion));  
    	}
		
		if(nombres != null  && !nombres.trim().equals("") ){
    		filtros.add(Restrictions.ilike("perNom", "%" + nombres + "%"));  
    	}
		
		if(primerApellido != null  && !primerApellido.trim().equals("") ){
    		filtros.add(Restrictions.ilike("perPap",  "%" + primerApellido + "%"));  
    	}
		
		if(segundoApellido != null  && !segundoApellido.trim().equals("") ){
    		filtros.add(Restrictions.ilike("perSap",  "%" + segundoApellido + "%"));  
    	}
		
		if(seudonimo != null  && !seudonimo.trim().equals("") ){
    		filtros.add(Restrictions.ilike("perSeunom",  "%" + seudonimo + "%"));  
    	}
		
		filtros.add(Restrictions.isNotNull("perDoc"));
		filtros.add(Restrictions.ne("perDoc",  ""));  
		
		filtros.add(Restrictions.isNotNull("perNom"));
		filtros.add(Restrictions.ne("perNom",  "")); 
		
		filtros.add(Restrictions.isNotNull("perPap"));
		filtros.add(Restrictions.ne("perPap",  "")); 
		
		
		Map<String, String> alias = new HashMap<String, String>();
		listDatamodel.setAlias(alias);
		listDatamodel.setFiltros(filtros);
	}

	public void cargaListaDataModel() {
		if (listDatamodel == null) {
			listDatamodel = new PersonaDataModel();
			List<Criterion> filtros = new ArrayList<Criterion>();
			Map<String, String> alias = new HashMap<String, String>();
			
			
			filtros.add(Restrictions.isNotNull("perDoc"));
			filtros.add(Restrictions.ne("perDoc",  ""));  
			
			filtros.add(Restrictions.isNotNull("perNom"));
			filtros.add(Restrictions.ne("perNom",  "")); 
			
			filtros.add(Restrictions.isNotNull("perPap"));
			filtros.add(Restrictions.ne("perPap",  "")); 
			
			listDatamodel.setAlias(alias);
			listDatamodel.setFiltros(filtros);
		}
	}

	public void nada() {
		//Mtodo usado para hacer doble invocacin
		//para confirmar borrar un usuario antes de 
		//hacerlo realmente.
	}
	
	public void eliminarUsuario() {
		System.out.println("eliminarUsuario... "+usuarioBorrar);
		if (usuarioBorrar == null) return;
		
		Connection con = serviceDao.getGenericCommonDao().connectionFromHibernate();
		try {
			CallableStatement stm = con.prepareCall("{call dbo.PaEliPersona(?, ?, ?)}");
			int i=1;
			stm.setInt(i++, usuarioBorrar);
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
	
	private static final class PersonaDataModel extends PrimeDataModel<Persona, Integer> {
		private static final long serialVersionUID = 1L;

		private PersonaDataModel() {
			super(Persona.class);
			setOrderBy(Order.asc("perNom"));
		}

		@Override
		protected Object getId(Persona t) {
			return t.getClass();
		}
	}

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public PersonaDataModel getListDatamodel() {
		cargaListaDataModel();
		return listDatamodel;
	}

	public void setListDatamodel(PersonaDataModel listDatamodel) {
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

	public String getPrimerApellido() {
		return primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public String getSeudonimo() {
		return seudonimo;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public void setSeudonimo(String seudonimo) {
		this.seudonimo = seudonimo;
	}
		
}
