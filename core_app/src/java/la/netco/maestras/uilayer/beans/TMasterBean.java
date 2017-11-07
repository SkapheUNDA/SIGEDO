package la.netco.maestras.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import la.netco.core.uilayer.commons.BaseBean;
import la.netco.core.uilayer.commons.GenericDataModel;
import la.netco.maestras.persistence.dto.PropTablas;
import la.netco.maestras.persistence.dto.TablasMaestras;

@ManagedBean (name="tMasterBean")
@ViewScoped
public class TMasterBean extends BaseBean implements Serializable {
	
	/**
	 * Si
	 */
	private static final long serialVersionUID = -1231802958180501924L;
	
	
	@ManagedProperty (value ="#{formMaster}")
	private VarTMaestrasBean tMaster;
	
	private TMaestrasDataModel tMaestrasDataModel;
	private String nombre;
	
	
	public void InitOP()
	{
		cargaFiltrosDataModel();
	}
	
	public TMasterBean() {
	}

	public VarTMaestrasBean gettMaster() {
		return tMaster;
	}

	public void settMaster(VarTMaestrasBean tMaster) {
		this.tMaster = tMaster;
	}
	
	public String funcionVerificarMap()
	{
	
		for (Map.Entry<String, String> variMap : tMaster.entrySet()) {
			System.out.println("Key: " + variMap.getKey() + " Value: " + variMap.getValue());
		}
		
		return "tmaestrasSecc";
	}
		
	public String cargaFiltrosDataModel()
	{
		List<Criterion> filtros =  new ArrayList<Criterion>();
		if(nombre != null  && nombre.trim().equals(""))
		{
			Criterion nombre =  Restrictions.ilike("tdoNom","%" + this.nombre + "%");
			filtros.add(nombre);
		}
		
		tMaestrasDataModel =  new TMaestrasDataModel();
		tMaestrasDataModel.setFiltros(filtros);
		return "";

	}
	
	public TMaestrasDataModel gettMaestrasDataModel() {
		return tMaestrasDataModel;
	}

	public void settMaestrasDataModel(TMaestrasDataModel tMaestrasDataModel) {
		this.tMaestrasDataModel = tMaestrasDataModel;
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	/*
	 * DataModel
	 * 
	 */


	private  static final class TMaestrasDataModel  extends GenericDataModel<TablasMaestras, Integer>{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private TMaestrasDataModel()
		{
			super(TablasMaestras.class);
			setOrderBy(Order.asc("nom_tma"));
		}
		
		@Override
		protected Object getId(TablasMaestras ObjTablasMaestras)
		{
			return ObjTablasMaestras.getId_tma();
		}
		
	}

}



